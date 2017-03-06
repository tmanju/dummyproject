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


public class Syndication {

    //~ Methods --------------------------------------------------------------------------------------------------------

	public BigDecimal getTotalCalculatedFee() {
		BigDecimal total = BigDecimal.ZERO;
		for (FeeAllocation feeAlloc : getFeeAllocations()) {
			if (feeAlloc.getCalculatedFee() != null) {
				total = total.add(feeAlloc.getCalculatedFee());
			}
		}
		return total;
	}
	
	public FeeAllocation getFeeAllocation(Fee fee) {
		if (fee != null && getFeeAllocations() != null) {
			for (FeeAllocation feeAllocation : getFeeAllocations()) {
				if (feeAllocation.getFee() != null
						&& ((feeAllocation.getFee().getId() != null && fee.getId() != null
								&& feeAllocation.getFee().getId() == fee.getId())
							|| (feeAllocation.getFee() == fee))
					){
					return feeAllocation;
				}
			}
		}
		return null;
	}
	
	
}
