/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.service;

import com.thirdpillar.codify.loanpath.model.AttributeChoice;
import com.thirdpillar.codify.loanpath.model.Role;
import com.thirdpillar.codify.loanpath.model.Team;
import com.thirdpillar.codify.loanpath.model.User;

import com.thirdpillar.foundation.util.reflect.ReflectionUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.beanutils.MethodUtils;

import org.apache.log4j.Logger;

import org.springframework.util.ReflectionUtils;


/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
public class ModelHelper {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    private static final Logger logger = Logger.getLogger(ModelHelper.class);
    public static final String USER_USERNAME = "username";
    public static final String ROLE_CATEGORY = "getRoleCategory";
    public static final String ATTRIBUTE_CHOICE_KEY = "key";
    public static final String TEAM_NAME = "name";

    //~ Methods --------------------------------------------------------------------------------------------------------

    public static String getKeyFromAttributeChoice(Object attributeChoice) {

        if (attributeChoice instanceof AttributeChoice) {
            String key = null;

            try {
                key = BeanUtils.getProperty(attributeChoice, ATTRIBUTE_CHOICE_KEY);
            } catch (Exception e) {
                logger.error("Unable to retrive key from AttributeChoice object", e);
            }

            return key;
        } else {
            throw new IllegalArgumentException("Only objects of type " + AttributeChoice.class.getName() + " expected");
        }
    }

    public static String getRoleCategoryFromRole(Object role) {

        if (role instanceof Role) {
            String roleCategoryKey = null;

            try {
                Object roleCategoryAttrChoice = MethodUtils.invokeExactMethod(role, ROLE_CATEGORY, null);

                if ((roleCategoryAttrChoice != null) && (roleCategoryAttrChoice instanceof AttributeChoice)) {
                    roleCategoryKey = getKeyFromAttributeChoice(roleCategoryAttrChoice);
                } else {
                    return null;
                }
            } catch (Exception e) {
                logger.error("Unable to retrive name from Role object", e);
            }

            return roleCategoryKey;
        } else {
            throw new IllegalArgumentException("Only objects of type " + Role.class.getName() + " expected");
        }
    }

    public static String getTeamNameFromTeam(Object team) {

        if (team instanceof Team) {
            String teamName = null;

            try {
                teamName = BeanUtils.getProperty(team, TEAM_NAME);
            } catch (Exception e) {
                logger.error("Unable to retrive name from team object", e);
            }

            return teamName;
        } else {
            throw new IllegalArgumentException("Only objects of type " + Team.class.getName() + " expected");
        }
    }

    public static String getUserNameFromUser(Object user) {

        if (user instanceof User) {
            String userName = null;

            try {
                userName = BeanUtils.getProperty(user, USER_USERNAME);
            } catch (Exception e) {
                logger.error("Unable to retrive username from user object", e);
            }

            return userName;
        } else {
            throw new IllegalArgumentException("Only objects of type " + User.class.getName() + " expected");
        }
    }
}
