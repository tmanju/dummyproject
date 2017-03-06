/*
 * Copyright (c) 2005-2013 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
/**
 *
 */
package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.DocumentAware;
import com.thirdpillar.foundation.model.WorkflowAware;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;
import com.thirdpillar.foundation.util.StringUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.math.BigDecimal;

import javax.swing.text.MaskFormatter;


public class FeeAllocation {

    //~ Methods --------------------------------------------------------------------------------------------------------

	public BigDecimal getCalculatedFee() {
		
		AttributeChoice feeDist = (getFee() != null) ? getFee().getFeeDistribution() : null;
		String feeDistKey = (feeDist != null) ? feeDist.getKey() : "FEE_DISTRIBUTION_PERCENT";
		
		BigDecimal feeAmt = (getFee() != null && getFee().getFeeAmt() != null) ? getFee().getFeeAmt() : BigDecimal.ZERO;
		if("FEE_DISTRIBUTION_AMOUNT".equals(feeDistKey)) {
			return getFeeAmount();
		} else if (getFeePct() != null){			 
			return getFeePct().multiply(feeAmt).divide(new BigDecimal(100));
		} else {
			// calcualte fee amount based on approtioned pct
			BigDecimal pct = BigDecimal.ZERO; 
			if (getSyndication() != null && getSyndication().getTranche() != null && getSyndication().getTranche().getFacility() != null) {
				pct = getSyndication().getTranche().getFacility().getFeeApportionPct(this);
			}
			return feeAmt.multiply(pct).divide(new BigDecimal(100));			
		}
	}
}
