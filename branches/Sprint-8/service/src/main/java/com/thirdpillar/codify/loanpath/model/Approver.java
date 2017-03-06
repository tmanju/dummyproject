/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */

package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.ContextHolder;




public  class Approver extends BaseDataObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -244479236126619389L;
	
	 //~ Static fields/initializers -------------------------------------------------------------------------------------



    //~ Methods --------------------------------------------------------------------------------------------------------
     
    public String getDecisionDer(){
    	String programStatus = "";
    	Program program = (Program)ContextHolder.getContext().getNamedContext().get("root");
		if( ((WorkflowStatus)program.getWfStatus()) != null){
			programStatus = ((WorkflowStatus)program.getWfStatus()).getValue();
			if(!("Declined".equalsIgnoreCase(programStatus) || "Approved".equalsIgnoreCase(programStatus))){
				return "";
			}
		}
		return programStatus;
			 
    }
}
