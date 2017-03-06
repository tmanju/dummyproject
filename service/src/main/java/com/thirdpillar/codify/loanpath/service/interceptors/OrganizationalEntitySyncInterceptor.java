/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.service.interceptors;

import com.thirdpillar.codify.loanpath.model.Role;
import com.thirdpillar.codify.loanpath.model.Team;
import com.thirdpillar.codify.loanpath.model.User;
import com.thirdpillar.codify.loanpath.service.ModelHelper;

import com.thirdpillar.foundation.service.interceptors.*;
import com.thirdpillar.foundation.service.task.EmbeddedTaskService;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;


/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
public class OrganizationalEntitySyncInterceptor extends BasicInterceptor {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    private static final Logger logger = Logger.getLogger(OrganizationalEntitySyncInterceptor.class);

    //~ Instance fields ------------------------------------------------------------------------------------------------

    @Autowired private EmbeddedTaskService embeddedTaskService;

    //~ Methods --------------------------------------------------------------------------------------------------------

    public void setEmbeddedTaskService(EmbeddedTaskService embeddedTaskService) {
        this.embeddedTaskService = embeddedTaskService;
    }

    @Override public void intercept(InterceptorContext context) throws Throwable {

        try {
            Object[] entities = context.getJoinPoint().getArgs();

            for (Object entity : entities) {
                logger.debug("Got entity of type " + entity.getClass());

                if (isAnOrganizationalEntity(entity)) {
                    addEntityToOrganizationalEntity(entity);
                } else if (Collection.class.isAssignableFrom(entity.getClass())) {
                    Collection collectionOfEntities = (Collection) entity;

                    for (Object collObject : collectionOfEntities) {

                        if (isAnOrganizationalEntity(collObject)) {
                            addEntityToOrganizationalEntity(collObject);
                        }
                    }
                }
            }
        } catch (Exception throwable) {
            logger.error(context.getTarget() + " is not a business entity", throwable);
        } finally {
            super.invokeNext(context);
        }
    }

    private void addEntityToOrganizationalEntity(Object entity) throws Exception {

        if (entity instanceof User) {
            User user = (User) entity;

            if (user.getId() == null) {
                String userName = ModelHelper.getUserNameFromUser(user);
                logger.debug("Adding user " + userName + " to organizational entity");
                embeddedTaskService.addUser(userName);
            } else {
                logger.debug("User with id " + user.getId() + " should already exist in organizational entity");
            }
        } else if (entity instanceof Team) {
            Team team = (Team) entity;

            if (team.getId() == null) {
                String teamName = ModelHelper.getTeamNameFromTeam(team);

                if (StringUtils.isNotBlank(teamName)) {
                    logger.debug("Adding team " + teamName + "to organizational entity");
                    embeddedTaskService.addGroup(teamName);
                }
            } else {
                logger.debug("Team with id " + team.getId() + " should already exist in organizational entity");
            }
        } else if (entity instanceof Role) {
            Role role = (Role) entity;

            if (role.getId() == null) {
                String roleCategoryName = ModelHelper.getRoleCategoryFromRole(role);

                if (StringUtils.isNotBlank(roleCategoryName)) {
                    logger.debug("Adding role " + roleCategoryName + "to organizational entity");
                    embeddedTaskService.addGroup(roleCategoryName);
                }
            } else {
                logger.debug("Role with id " + role.getId() + " should already exist in organizational entity");
            }
        }
    }

    private boolean isAnOrganizationalEntity(Object entity) {
    	boolean flag = false;
        if (entity != null) {

            if ((entity instanceof User) || (entity instanceof Team) || (entity instanceof Role)) {
            	flag =  true;
            }
        }
        return flag;
    }
}
