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
public class FinancialRatio extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /**  */
    private static final long serialVersionUID = 1L;

    //~ Methods --------------------------------------------------------------------------------------------------------
    public Boolean isOperatingRatio(){
    	boolean type = false;
    	if(getFinancialRatio() != null && ("FINANCIAL_RATIO_TYPE_OR".equals(getFinancialRatio().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isBreakEvenRatio(){
    	boolean type = false;
    	if(getFinancialRatio() != null && ("FINANCIAL_RATIO_TYPE_BER".equals(getFinancialRatio().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isDebtCoverageRatio(){
    	boolean type = false;
    	if(getFinancialRatio() != null && ("FINANCIAL_RATIO_TYPE_DCR".equals(getFinancialRatio().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isLoanToValueRatio(){
    	boolean type = false;
    	if(getFinancialRatio() != null && ("FINANCIAL_RATIO_TYPE_LTV".equals(getFinancialRatio().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public BigDecimal getMarketPrice(){
    	BigDecimal price = BigDecimal.ZERO;
    	if((isOperatingRatio() || isBreakEvenRatio()) && getForcastedFinancial() != null && getForcastedFinancial().getFooterTotalAmtExpense() != null){
    		price = getForcastedFinancial().getFooterTotalAmtExpense();
    	}
    	if(isDebtCoverageRatio() && getForcastedFinancial() != null && getForcastedFinancial().getFooterTotalAmt() != null && getForcastedFinancial().getFooterTotalAmtExpense() != null){
    		price = getForcastedFinancial().getFooterTotalAmt().subtract(getForcastedFinancial().getFooterTotalAmtExpense());
    	}
    	if(isLoanToValueRatio()){
    		Customer customer = (Customer)ContextHolder.getContext().getNamedContext().get("root");
    		if(customer.getKeyPricing() != null && customer.getKeyPricing().getLoanAmount() != null ){
    			price = customer.getKeyPricing().getLoanAmount();
        	}
    	}
    	return price.equals(BigDecimal.ZERO)? null : price;
    }
    public BigDecimal getGrossRent(){
    	BigDecimal rent = BigDecimal.ZERO;
    	if((isOperatingRatio() || isBreakEvenRatio()) && getForcastedFinancial() != null && getForcastedFinancial().getFooterTotalAmt() != null){
    		rent = getForcastedFinancial().getFooterTotalAmt();
    	}
    	if(	isLoanToValueRatio() && getForcastedFinancial() != null && getForcastedFinancial().getPurchasePrice() != null){
    		rent = getForcastedFinancial().getPurchasePrice();
    	}
    	return rent.equals(BigDecimal.ZERO)? null : rent;
    }
    public BigDecimal getMultipliers(){
    	BigDecimal amount = BigDecimal.ZERO;
    	if(getMarketPrice() != null && getGrossRent() != null && getGrossRent().compareTo(BigDecimal.ZERO) > 0){
    		amount = getMarketPrice().divide(getGrossRent(),4,RoundingMode.HALF_UP);
    		amount = amount.multiply(new BigDecimal(100));
    	}
    	return amount.equals(BigDecimal.ZERO)? null : amount;
    }
}