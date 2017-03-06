/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.codify.loanpath.model.PricingOption;
import com.thirdpillar.codify.loanpath.model.Program;
import com.thirdpillar.foundation.service.ContextHolder;
import com.thirdpillar.foundation.service.ContextImpl;
import com.thirdpillar.foundation.service.LookupService;
import com.thirdpillar.codify.loanpath.model.AttributeChoice;
import com.thirdpillar.codify.loanpath.model.Attribute;

/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
public class Structure extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /**  */
    private static final long serialVersionUID = 1L;
    
    @javax.persistence.Transient
    Facility facility;

    //~ Methods --------------------------------------------------------------------------------------------------------

	public Integer getAmortizationTermInMonths() {
        return convertTermValueToMonths(getAmortizationTerm(), getAmortizationUnit());
    }

    public Integer convertTermValueToMonths(Integer termValue, AttributeChoice termUnit) {


            if (termValue != null && (termUnit != null) && StringUtils.isNotEmpty(termUnit.getKey())) {

                if (StringUtils.equals(termUnit.getKey(), "TERM_UNIT_DAYS")) {
                    termValue = termValue / (365 / 12);
                } else if (StringUtils.equals(termUnit.getKey(), "TERM_UNIT_YEARS")) {
                    termValue = termValue * 12;
                }
            }

        return termValue;
    }

    public Integer getPaymentFrequencyInMonths() {
        Integer paymentFrequencyValue = Integer.valueOf(1);
        AttributeChoice paymentFrequency = null;

        if (this.getPaymentSchedule() != null) {
            paymentFrequency = this.getPaymentSchedule().getPaymentFrequency();
        }

        if ((paymentFrequency != null) && StringUtils.isNotEmpty(paymentFrequency.getKey())) {

            if (StringUtils.equals(paymentFrequency.getKey(), "PAYMENT_FREQUENCY_MONTHLY")) {
                paymentFrequencyValue = Integer.valueOf(1);
            } else if (StringUtils.equals(paymentFrequency.getKey(), "PAYMENT_FREQUENCY_QUARTERLY")) {
                paymentFrequencyValue = Integer.valueOf(3);
            } else if (StringUtils.equals(paymentFrequency.getKey(), "PAYMENT_FREQUENCY_SEMIANNUAL")) {
                paymentFrequencyValue = Integer.valueOf(6);
            } else if (StringUtils.equals(paymentFrequency.getKey(), "PAYMENT_FREQUENCY_ANNUAL")) {
                paymentFrequencyValue = Integer.valueOf(12);
            }
        }

        return paymentFrequencyValue;
    }

    public Integer getTermInMonths() {
        return convertTermValueToMonths(getTerm(), getTermUnit());
    }
    public BigDecimal getTotalFee(){
    	BigDecimal total = BigDecimal.ZERO;
    	if(getFees() != null){
    		for(Fee fee : getFees()){
    			if(fee.getFeeAmt() !=null){
    				total = total.add(fee.getFeeAmt());
    			}
    		}
    	}
    	return total;
    }
    
    public List <SeasonalSchedule> getSelectedSeasonalSchedules() {
        List <SeasonalSchedule> list  = new ArrayList<SeasonalSchedule>();
    	for (SeasonalSchedule seasonal: getPaymentSchedule().getSeasonalSchedules() ) {
    		if (seasonal.getIncludeFlag()) {
    			list.add(seasonal);
    		}
    	}
    	return list;
    }
    
    public boolean updateBorrowingBaseAllowed(){
		boolean match = true;
		Request request = (Request) ContextHolder.getContext().getNamedContext().get("root");
		if(!Constants.REQUEST_STATUS_PORTFOLIO_MANAGMENT_KEY.equals(request.getWfStatus().getStatusKey())){
			return match;
		}
		match = false;
		User user = (User)ContextHolder.getContext().getUser();
		List<Team> teams = user.getTeams();
		for(Team team : teams){
			if(Constants.REQUEST_STATUS_PORTFOLIO_MANAGMENT_KEY.equals(request.getWfStatus().getStatusKey()) && "credit decision team".equals(team.getName().toLowerCase())){
				match = true;
				break;
			}
		}
		return match;
	}
    
    public boolean updateReserveReqAllowed(){
		boolean match = false;
		User user = (User)ContextHolder.getContext().getUser();
		List<Team> teams = user.getTeams();
		for(Team team : teams){
			if("credit analyst team".equals(team.getName().toLowerCase())){
				match = true;
				break;
			}
		}
		return match;
	}
    
    public Facility getFacility() {
		return facility;
	}

	public void setFacility(Facility facility) {
		this.facility = facility;
	}

}
