/*
 * Copyright (c) 2005-2012 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.drools.process.instance.WorkItemHandler;
import org.drools.process.instance.impl.WorkItemImpl;
import org.drools.runtime.KnowledgeRuntime;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;
import org.jbpm.task.Status;
import org.mvel2.templates.TemplateRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import com.thirdpillar.codify.loanpath.model.Role;
import com.thirdpillar.codify.loanpath.model.Team;
import com.thirdpillar.codify.loanpath.model.User;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.model.task.TaskMapping;
import com.thirdpillar.foundation.service.ContextHolder;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.workitems.CommandBasedWSHumanTaskHandler;
import com.thirdpillar.foundation.service.workitems.NotificationHelper;
import com.thirdpillar.foundation.service.workitems.WorkItemConstants;
import com.thirdpillar.foundation.util.DateCalcUtil;

/**
 * DOCUMENT ME!
 * 
 * @author ENTER YOUR FULL NAME
 * @version 1.0
 * @since 1.0
 */
public class LPUserTaskHandler implements WorkItemHandler {

	// ~ Static fields/initializers
	// -------------------------------------------------------------------------------------

	private static final Logger logger = Logger
			.getLogger(LPUserTaskHandler.class);

	// ~ Instance fields
	// ------------------------------------------------------------------------------------------------

	@Autowired
	private CommandBasedWSHumanTaskHandler commandBasedWSHumanTaskHandler;
	@Autowired
	TemplateRegistry templateRegistry;
	private KnowledgeRuntime session;
	private String eventName;

	// ~ Constructors
	// ---------------------------------------------------------------------------------------------------

	public LPUserTaskHandler(KnowledgeRuntime session, String eventName) {
		logger.debug("commandBasedWSHumanTaskHandler is "
				+ commandBasedWSHumanTaskHandler);
		logger.debug("eventName is " + eventName);
		this.session = session;
		this.eventName = eventName;
		// TODO Auto-generated constructor stub
	}

	// ~ Methods
	// --------------------------------------------------------------------------------------------------------

	public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
		commandBasedWSHumanTaskHandler.abortWorkItem(workItem, manager);
	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {

		// Validate if LP Task Name is set
		String taskName = (String) workItem.getParameter("TaskName");

		if (StringUtils.isBlank(taskName)) {
			throw new IllegalArgumentException("TaskName is required");
		}

		String comment = (String) workItem.getParameter("Comment");
																	// Comment/Subject/Content
																	// all are
																	// currently
																	// the same
																	// thing.
																	// This
																	// needs to
																	// be
																	// cleaned
		String subject = (String) workItem.getParameter("Subject");

		if (StringUtils.isBlank(comment)) {
			throw new IllegalArgumentException("Comment is required");
		}

		// Determine the priority of the task
		Integer priorityInt = WorkItemConstants.WORKITEM_PARAMETER_DEFAULT_PRIORITY; // Default
																						// is
																						// 3
		String priority = (String) workItem
				.getParameter(WorkItemConstants.WORKITEM_PARAMETER_PRIORITY);

		if (priority != null) {

			try {
				priorityInt = Integer.parseInt(priority);
			} catch (NumberFormatException e) {
				logger.error("Error occured at execute", e);
			}
		}

		workItem.getParameters().put(
				WorkItemConstants.WORKITEM_PARAMETER_PRIORITY,
				priorityInt.toString());

		// Determine if there are any incoming owners set by the prior steps of
		// the process
		Object collectionOfOwners = workItem
				.getParameter(WorkItemConstants.WORKITEM_PARAMETER_OWNERS);
		StringBuilder pActorIds = new StringBuilder();
		StringBuilder pGroupIds = new StringBuilder();

		if (collectionOfOwners != null) {

			if (Collection.class
					.isAssignableFrom(collectionOfOwners.getClass())) {
				Collection ownerCollection = (Collection) collectionOfOwners;

				for (Object ownerObj : ownerCollection) {

					if (ownerObj instanceof User) {
						String userName = ModelHelper
								.getUserNameFromUser(ownerObj);
						pActorIds.append(userName).append(",");
					} else if (ownerObj instanceof Role) {
						String roleCategoryName = ModelHelper
								.getRoleCategoryFromRole(ownerObj);

						if (StringUtils.isNotBlank(roleCategoryName)) {
							pGroupIds.append(roleCategoryName).append(",");
						}
					} else if (ownerObj instanceof Team) {
						String teamName = ModelHelper
								.getTeamNameFromTeam(ownerObj);

						if (StringUtils.isNotBlank(teamName)) {
							pGroupIds.append(teamName).append(",");
						}
					}
				}
			} else {
				logger.error("Unexpected holder of owner objects");
			}
		}

		// Merge the actorIds with those configured in workitem definition
		String actorIds = (String) workItem.getParameter("ActorId");

		if ((actorIds != null) && (actorIds.trim().length() > 0)) {
			pActorIds.append(actorIds);
		}

		List<String> userList = new LinkedList<String>(Arrays.asList(pActorIds
				.toString().split(",")));

		for (Iterator<String> itr = userList.iterator(); itr.hasNext();) {
			String nextUserId = itr.next();

			if (StringUtils.isBlank(nextUserId)) {
				itr.remove();
			}
		}

		workItem.getParameters()
				.put("ActorId", StringUtils.join(userList, ","));

		// Merge the groupids with those configured in workitem definition
		String groupIds = (String) workItem.getParameter("GroupId");
		String to = (String) workItem
				.getParameter(WorkItemConstants.WORKITEM_PARAMETER_TO);

		if ((groupIds != null) && (groupIds.trim().length() > 0)) {
			pGroupIds.append(groupIds);
		}

		List<String> groupList = new LinkedList<String>(Arrays.asList(pGroupIds
				.toString().split(",")));

		for (Iterator<String> itr = groupList.iterator(); itr.hasNext();) {
			String nextGroupId = itr.next();

			if (StringUtils.isBlank(nextGroupId)) {
				itr.remove();
			}
		}

		// Deadlines
		Calendar dueDate = Calendar.getInstance();
		String dueDateDelayTime = (String) workItem
				.getParameter("DueDateDelayTime");
		String escalationTo = (String) workItem.getParameter("EscalationTo");
		String escalationActorId = (String) workItem
				.getParameter("EscalationActorId");
		String escalationGroupId = (String) workItem
				.getParameter("EscalationGroupId");
		String deadlineDueDate = (String) workItem.getParameter("DueDate");

		if (dueDateDelayTime != null) {
			if ((escalationActorId == null || escalationGroupId == null)
					&& escalationTo == null) {
				throw new IllegalStateException(
						"\""
								+ taskName
								+ " defines a Deadline time but it doesn't define any notification recipient or reassignment potential owner");
			}
		}

		workItem.getParameters().put("GroupId",
				StringUtils.join(groupList, ","));

		// Execute the work item
		commandBasedWSHumanTaskHandler.executeWorkItem(workItem, manager);

		String actions = (String) workItem
				.getParameter(WorkItemConstants.WORKITEM_PARAMETER_ACTIONS);
		BaseDataObject entity = (BaseDataObject) workItem
				.getParameter(WorkItemConstants.WORKITEM_PARAMETER_ENTITY);
		BaseDataObject validateEntity = (BaseDataObject) workItem
				.getParameter(WorkItemConstants.WORKITEM_PARAMETER_VALIDATE_ENTITY);
		if (validateEntity == null) {
			validateEntity = entity;
		}
		logger.debug("Actions " + actions);
		logger.debug("workitem id " + workItem.getId());
		logger.debug("process id " + workItem.getProcessInstanceId());

		NotificationHelper notificationHelper = new NotificationHelper();
		String notify = (String) workItem.getParameter("notify");
		com.thirdpillar.foundation.model.notification.Notification dueDateNotification = null;
		if ("true".equals(notify)) {
			notificationHelper.sendEmail(workItem, templateRegistry);
			// Get Notification Object here and set it to TaskMapping
			try {
				if (StringUtils.isNotEmpty(escalationTo)) {
					subject = subject
							+ " has met its deadline and requies your review";
					((WorkItemImpl) workItem).setParameter(
							WorkItemConstants.WORKITEM_PARAMETER_SUBJECT,
							subject);
					dueDateNotification = notificationHelper
							.getNotificationEntity(workItem, templateRegistry,
									true);
				}

			} catch (Exception e) {
				logger.error("Unable to send mail", e);
			}

			// if notify flag is false, create the body yourself.
		} else {
			if (StringUtils.isNotEmpty(escalationTo)) {

				// This is the case where we need to construct body and subject
				subject = "This task " + taskName + "assigned to user/group "
						+ actorIds + groupIds
						+ " has met its deadline. Please review";
				String body = "This task " + taskName
						+ "assigned to user/group " + actorIds + groupIds
						+ " has met its deadline. Please review";
				((WorkItemImpl) workItem).setParameter(
						WorkItemConstants.WORKITEM_PARAMETER_TO, escalationTo);
				((WorkItemImpl) workItem).setParameter(
						WorkItemConstants.WORKITEM_PARAMETER_SUBJECT, subject);
				((WorkItemImpl) workItem).setParameter(
						WorkItemConstants.WORKITEM_PARAMETER_BODY, body);
				dueDateNotification = notificationHelper.getNotificationEntity(
						workItem, templateRegistry, true);
			}

		}

		String from = (String) workItem
				.getParameter(WorkItemConstants.WORKITEM_PARAMETER_FROM);

		// populating the mapping between entity and task
		EntityService<BaseDataObject> es = new EntityService<BaseDataObject>();
		TaskMapping mapping = new TaskMapping();
		mapping.setWorkItemId(workItem.getId());
		mapping.setSessionId(new Long(((StatefulKnowledgeSession) session)
				.getId()));
		mapping.setTypeId(WorkItemConstants.WORKITEM_TYPE_TASK);

		String processKBaseId = session.getProcessInstance(
				workItem.getProcessInstanceId()).getProcessId();
		mapping.setProcessKBaseId(processKBaseId);
		mapping.setProcessInstanceId(workItem.getProcessInstanceId());
		mapping.setEventName(this.eventName);
		mapping.setWorkItemPayload(entity);
		mapping.setValidatingPayload(validateEntity);
		mapping.setActions(actions);
		mapping.setTaskName(taskName);
		mapping.setSubject(comment);
		mapping.setPriority(priorityInt);
		mapping.setDueDateDelayTime(dueDateDelayTime);
		mapping.setEscalationTo(escalationTo);
		mapping.setDueDateNotification(dueDateNotification);
		if (StringUtils.isNotEmpty(escalationActorId)) {
			mapping.setEscalationActorId(escalationActorId);
		} else if (StringUtils.isNotEmpty(escalationGroupId)) {
			mapping.setEscalationGroupId(escalationGroupId);
		}
		mapping.setToRecipients(to);
		mapping.setFromEmail(from);
		mapping.setEscalatedFlag(false);
		DateCalcUtil dateCalcUtil = new DateCalcUtil();

		if (deadlineDueDate != null) {
			dueDate = (Calendar) dateCalcUtil.convertToDate(deadlineDueDate);
			if (dueDate != null) {
				mapping.setDueDate(dueDate.getTime());
			}
		} else if (dueDateDelayTime != null) {
			dueDate = dateCalcUtil.addDelayTime(Calendar.getInstance(),
					dueDateDelayTime);
			mapping.setDueDate(dueDate.getTime());
		}

		if (userList.size() == 1 && groupList.isEmpty()) {
			// only one actor is added as potential owner
			// HACK since we have no callback
			mapping.setActualOwner(userList.get(0));
			mapping.setStatus(Status.Reserved.name());
		} else {
			// status in ready state
			mapping.setStatus(Status.Ready.name());
		}

		Date createdAt = new Date();
		mapping.setCreatedDate(createdAt);
		mapping.setUpdatedDate(createdAt);

		if (ContextHolder.getContext() != null
				&& ContextHolder.getContext().getUser() != null) {
			mapping.setCreatedBy(ContextHolder.getContext().getUsername());
			mapping.setUpdatedBy(ContextHolder.getContext().getUsername());
		}
		if (dueDateNotification != null) {
			es.save(dueDateNotification);
		}
		es.save(mapping);
	}
}
