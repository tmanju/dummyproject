/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.thirdpillar.codify.loanpath.model.Customer;
import com.thirdpillar.codify.loanpath.model.Request;
import com.thirdpillar.foundation.codify.exception.CodifyException;
import com.thirdpillar.foundation.codify.exception.ValidationException;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.rules.RuleLogger;
import com.thirdpillar.foundation.rules.RuleService;
import com.thirdpillar.foundation.service.AbstractBusinessOperation;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.util.StringUtils;

/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */


public class VerifySsnDobBizOp extends AbstractBusinessOperation {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    private static final Log LOG = LogFactory.getLog(VerifySsnDobBizOp.class);

    //~ Instance fields ------------------------------------------------------------------------------------------------

    @Autowired EntityService entityService;

    //~ Methods --------------------------------------------------------------------------------------------------------

    public Object execute(BaseDataObject entity, String operation, Object... params) {

    	if (!VerifySSNDOBHelper.verifySSNDOB((Customer) entity)) {
    		LOG.warn("SSN & DOB doesnot match with an existing customer");
		   throw new CodifyException("Please check the Social Security Number and Date of Birth values entered.");
		}

        return null;
    }
}
