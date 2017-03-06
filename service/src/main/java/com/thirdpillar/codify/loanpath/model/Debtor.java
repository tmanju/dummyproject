/*
 * Copyright (c) 2005-2012 Third Pillar Systems Inc. All Rights Reserved.
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
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
public class Debtor extends BaseDataObject {

    //~ Methods --------------------------------------------------------------------------------------------------------

    public Address getCustAddress() {

        if ((this.getCustomer() != null) &&
                    this.getCustomer().isNonIndividual()) {

        	if (this.getCustomer().getPrimarySite() != null) {
        		return this.getCustomer().getPrimarySite().getAddress("SITE_USE_TYPE_PHYSICAL_ADDRESS");
        	}
        } else if ((this.getCustomer() != null) &&
                    this.getCustomer().isIndividual()) {
        	
        	if (this.getCustomer().getPrimarySite() != null) {
        		return this.getCustomer().getPrimarySite().getAddress("SITE_USE_TYPE_HOME_ADDRESS");
        	}        	

        }

        return null;
    }
}
