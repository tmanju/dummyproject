/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;

import org.springframework.security.core.userdetails.UserDetails;


/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
@Aspect public class UserDecorator {

    //~ Static fields/initializers -------------------------------------------------------------------------------------
	
	private UserDecorator(){
		
	}

    @DeclareParents(value = "com.thirdpillar.codify.loanpath.model.User")
    public static UserDetails springSecurityPrincipal;
}
