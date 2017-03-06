/*
 * Copyright (c) 2005-2015 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.codify.loanpath.lookup.AttributeChoiceLookup;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.LookupService;


/**
 * DOCUMENT ME!
 *
 * @author   Ankush.Bhardwaj
 * @version  1.0
 * @since    1.0
 */
public class PricingPolicy extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = -6604576372288529264L;

    //~ Methods --------------------------------------------------------------------------------------------------------

    public String getDescriptionDer() {
        return getDescription();
    }

    public String getNameDer() {
        return getName();
    }

    public String getOuSbuDer() {
        return getOu().getValue() + " - " + getSbu().getValue();
    }

    public String getStatusDer() {
        String status = null;

        if (getActive()) {
            status = "Enabled";
        } else {
             status = "Disabled";
        }
        return status;
    }
    
    public boolean validatePolicyName() {
    	boolean flag=true;
    	String name=getName();
    	PricingPolicy pricingPolicy = (PricingPolicy)LookupService.getResult("PricingPolicy.byNameCaseInsensitive", "name",name);	
    	if(pricingPolicy != null){
    		if(!(getRefNumber()!=null && getRefNumber()==pricingPolicy.getRefNumber())){
    			
    			flag=false;
    		}
    	}
    	return flag;
    }
}
