package com.thirdpillar.codify.loanpath.util;

import java.util.HashMap;
import java.util.Map;

import org.drools.runtime.process.WorkItem;
import org.mvel2.templates.TemplateRegistry;

import com.thirdpillar.codify.loanpath.model.NotificationConfiguration;
import com.thirdpillar.foundation.model.notification.Notification;
import com.thirdpillar.foundation.rules.MvelExecutor;
import com.thirdpillar.foundation.service.LookupService;
import com.thirdpillar.foundation.service.NotificationService;
import com.thirdpillar.foundation.service.workitems.WorkItemConstants;
import com.thirdpillar.foundation.util.StringUtils;

public class NotificationUtil {

	public WorkItem getNotificationEntity(WorkItem workItem, String notificationTemplateName,Map paramMap){
		NotificationConfiguration notifConfiguration = (NotificationConfiguration) LookupService.getResult
																		("NotificationConfiguration.byNotificationName", "notificationName", notificationTemplateName);
		
		String to = (String) notifConfiguration.getToRecipients();
		String subject ="";
		String body="";
		String richBodyText = "";
		if(StringUtils.isNotEmpty(notifConfiguration.getSubject())){
			subject= (String) MvelExecutor.evalTemplate(notifConfiguration.getSubject(),paramMap);
		}
		if(StringUtils.isNotEmpty(notifConfiguration.getBody())){
			subject= (String) MvelExecutor.evalTemplate(notifConfiguration.getBody(),paramMap);
		}
        String from = (String) notifConfiguration.getFrom();
        String ccRecipients = (String) notifConfiguration.getCcRecipients();
        String notificationName = (String) notifConfiguration.getNotificationName();
        
        if(StringUtils.isNotEmpty(notifConfiguration.getBodyText())){
        	richBodyText= (String) MvelExecutor.evalTemplate(notifConfiguration.getBodyText(),paramMap);
		}        
        
        workItem.getParameters().put(WorkItemConstants.WORKITEM_PARAMETER_NAME, notificationName);
        workItem.getParameters().put(WorkItemConstants.WORKITEM_PARAMETER_FROM, from);
        workItem.getParameters().put(WorkItemConstants.WORKITEM_PARAMETER_TO, to);
        workItem.getParameters().put(WorkItemConstants.WORKITEM_PARAMETER_CC, ccRecipients);
//        workItem.getParameters().put(WORKITEM_PARAMETER_BCC, bccRecipients);//TODO:need to implement
        workItem.getParameters().put(WorkItemConstants.WORKITEM_PARAMETER_SUBJECT, subject);
        workItem.getParameters().put("Comment", subject);
        workItem.getParameters().put(WorkItemConstants.WORKITEM_PARAMETER_BODY, richBodyText);
		return workItem;
	}
}
