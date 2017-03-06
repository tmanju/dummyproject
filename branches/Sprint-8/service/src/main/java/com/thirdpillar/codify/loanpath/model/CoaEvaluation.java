/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.BaseDataObject;


/**
 * DOCUMENT ME!
 *
 * @author   VIKAS GUPTA
 * @version  1.0
 * @since    1.0
 */


public class CoaEvaluation extends BaseDataObject {
	 
	  //~ Static fields/initializers -------------------------------------------------------------------------------------

	    /** Use serialVersionUID for interoperability. */
	    private static final long serialVersionUID = 5418621932978769945L;

	    //~ Methods --------------------------------------------------------------------------------------------------------


	    public AttributeChoice getVariableTextExist(){
	    	
	    	if(getVariableText() == null || getVariableText().length() ==  0 ){
	    			
	    		return (AttributeChoice) com.thirdpillar.foundation.service.LookupService.getResult("AttributeChoice.byKey", "key","YES_OR_NO_NO");
	    	} else {
	    		return (AttributeChoice) com.thirdpillar.foundation.service.LookupService.getResult("AttributeChoice.byKey", "key","YES_OR_NO_YES");
	    	}
	    	
	    }
	
//	    public String getCoaConditonWithNotes(){
//	    	String result = getCoaDefinition().getCoaCondition() ;   
//	    	
//	    	if (  getVariableText() !=null &&  getVariableText().length() > 0  ) {
//	    		result = result  + " *** " +  getVariableText();   
//	    	}
//	    	
//	    	
//	    	
//	    	return result; 
//	    	
//	    }
	    
	    
}