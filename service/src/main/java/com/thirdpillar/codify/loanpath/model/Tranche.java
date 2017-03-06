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


public class Tranche {

    //~ Methods --------------------------------------------------------------------------------------------------------

	public BigDecimal getTotalTrancheAmt() {
		BigDecimal total = BigDecimal.ZERO;
		for (Syndication syndication : getSyndications()) {
			if (syndication.getSyndicationAmount() != null) {
				total = total.add(syndication.getSyndicationAmount());
			}
		}
		return total;
	}

	public BigDecimal getRemainingTrancheAmt() {
		return (getMaxAmount() != null) ? getMaxAmount().subtract(getTotalTrancheAmt()) : BigDecimal.ZERO;
	}	
	
	public BigDecimal getTotalTrancheFee() {
		BigDecimal total = BigDecimal.ZERO;
		for (Syndication syndication : getSyndications()) {
			if (syndication.getTotalCalculatedFee() != null) {
				total = total.add(syndication.getTotalCalculatedFee());
			}
		}
		return total;
	}
	
}
