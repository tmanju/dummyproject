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
import com.thirdpillar.codify.loanpath.model.AttributeChoice;
import com.thirdpillar.foundation.service.ContextHolder;

import java.util.List;
import java.util.ArrayList;


/**
 * DOCUMENT ME!
 *
 * @author   Ankush.Bhardwaj
 * @version  1.0
 * @since    1.0
 */
public class PaymentSchedule extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = -6604576372288529264L;

    //~ Methods --------------------------------------------------------------------------------------------------------
    public boolean isPaymentScheduleType(String key) {
    	
    	if ((getPaymentScheduleType() != null) && key.equalsIgnoreCase(getPaymentScheduleType().getKey())) {
            return true;
        }
            return false;
    }

    public boolean isSeasonal() {
        return isPaymentScheduleType("PAYMENT_SCHEDULE_TYPE_SEASONAL");
    }
    
    public boolean isIrregular() {
        return isPaymentScheduleType("PAYMENT_SCHEDULE_TYPE_IRREGULAR");
    }
    
    public boolean isRegular() {
        return isPaymentScheduleType("PAYMENT_SCHEDULE_TYPE_REGULAR");
    }
    
    
    public boolean isPaymentFrequencyType(String key) {
    	
    	if ((getPaymentFrequency() != null) && key.equalsIgnoreCase(getPaymentFrequency().getKey())) {
            return true;
        }
            return false;
    }

    public boolean isMonthly() {
        return isPaymentFrequencyType("PAYMENT_FREQUENCY_MONTHLY");
    }
    
    public boolean isQuarterly() {
        return isPaymentFrequencyType("PAYMENT_FREQUENCY_QUARTERLY");
    }
    
    public boolean isSemiAnnual() {
        return isPaymentFrequencyType("PAYMENT_FREQUENCY_SEMI_ANNUAL");
    }
    
    public boolean isAnnual() {
        return isPaymentFrequencyType("PAYMENT_FREQUENCY_ANNUAL");
    }
    
       
}
