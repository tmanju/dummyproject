/*
 * Copyright (c) 2005-2015 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.ArrayList;



import com.thirdpillar.foundation.service.ContextHolder;
import com.thirdpillar.foundation.service.ContextImpl;
import com.thirdpillar.foundation.service.LookupService;

/**
 * DOCUMENT ME!
 *
 * @author   Ankush.Bhardwaj
 * @version  1.0
 * @since    1.0
 */
public class RevenueLine extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /**  */
    private static final long serialVersionUID = 1L;

    //~ Methods --------------------------------------------------------------------------------------------------------
    public BigDecimal getPu(){
    	BigDecimal pu = BigDecimal.ZERO;
    	if(getAssessmentCollateral() != null && getAssessmentCollateral().getUnitArea() != null  &&
    			getAssessmentCollateral().getUnitArea().compareTo(BigDecimal.ZERO) > 0 ){
    		pu = getTotalAmount().divide(getAssessmentCollateral().getUnitArea(),2, RoundingMode.HALF_UP);
    	}
    	return pu;
    }
    public BigDecimal getProRated(){
    	BigDecimal prorated = null;
    	if(getIncomeExpense().getMonths() != null && getTotalAmount() != null){
    		BigDecimal month = new BigDecimal(getIncomeExpense().getMonths().getValue());
    		prorated = getTotalAmount().divide(month,2, RoundingMode.HALF_UP).multiply(new BigDecimal(12));
    	}
    	return prorated;
    	
    }
    public BigDecimal getPuProRata(){
    	BigDecimal puProrated = BigDecimal.ZERO;
    	if(getAssessmentCollateral() != null && getAssessmentCollateral().getUnitArea() != null  &&
    			getProRated() != null && getAssessmentCollateral().getUnitArea().compareTo(BigDecimal.ZERO) > 0 ){
    		puProrated = getProRated().divide(getAssessmentCollateral().getUnitArea(),2, RoundingMode.HALF_UP);
    	}
    	return puProrated;
    }
    public BigDecimal getTotalAmountProRata(){
    	BigDecimal totalProrated = BigDecimal.ZERO;
    	if(getIncomeExpense().getProRata()){
    		totalProrated = getProRated();
    	}else{
    		totalProrated = getTotalAmount();
    	}
    	return totalProrated;
    }
    public BigDecimal getAmountProRata(){
    	BigDecimal amountProrated = BigDecimal.ZERO;
    	if(getIncomeExpense().getProRata()){
    		amountProrated = getProRated();
    	}else{
    		amountProrated = getAmount();
    	}
    	return amountProrated;
    }
    public Boolean isRevenueCalledOperating(){
    	boolean operatingRev = true;
    	if(getRevenueLegend() != null && ("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(getRevenueLegend().getKey()) 
    	    	|| "REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(getRevenueLegend().getKey())
    	   		|| "REVENUE_LEGEND_TYPE_CONCESSIONS".equals(getRevenueLegend().getKey()) 
    	   		|| "REVENUE_LEGEND_TYPE_BASE_RENT".equals(getRevenueLegend().getKey())
    	    	|| "REVENUE_LEGEND_TYPE_BAD_DEBTS".equals(getRevenueLegend().getKey()))){
    	   		operatingRev = false;
    	   	}
       	return operatingRev;
        }
}