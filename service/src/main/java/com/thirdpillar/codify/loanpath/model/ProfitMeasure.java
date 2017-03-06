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
public class ProfitMeasure extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /**  */
    private static final long serialVersionUID = 1L;

    //~ Methods --------------------------------------------------------------------------------------------------------
    public Boolean isCapitizationRatio(){
    	boolean type = false;
    	if(getProfitMeasures() != null && ("PROFIT_MEASURES_TYPE_CR".equals(getProfitMeasures().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isEstimateMarketValue(){
    	boolean type = false;
    	if(getProfitMeasures() != null && ("PROFIT_MEASURES_TYPE_UCREMV".equals(getProfitMeasures().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isRateBeforeTax(){
    	boolean type = false;
    	if(getProfitMeasures() != null && ("PROFIT_MEASURES_TYPE_RBT".equals(getProfitMeasures().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isRateAfterTax(){
    	boolean type = false;
    	if(getProfitMeasures() != null && ("PROFIT_MEASURES_TYPE_RAT".equals(getProfitMeasures().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public BigDecimal getMarketPrice(){
    	BigDecimal price = BigDecimal.ZERO;
    	if(getForcastedFinancial() != null && getForcastedFinancial().getFooterTotalAmt() != null && getForcastedFinancial().getFooterTotalAmtExpense() != null){
    		price = getForcastedFinancial().getFooterTotalAmt().subtract(getForcastedFinancial().getFooterTotalAmtExpense());
    	}
    	
    	return price.equals(BigDecimal.ZERO)? null : price;
    }
    public BigDecimal getGrossRent(){
    	BigDecimal rent = BigDecimal.ZERO;
    	if(isCapitizationRatio() && getForcastedFinancial() != null && getForcastedFinancial().getPurchasePrice() != null){
    		rent = getForcastedFinancial().getPurchasePrice();
    	}
    	if(isEstimateMarketValue()){
    		Customer customer = (Customer)ContextHolder.getContext().getNamedContext().get("root");
    		if(customer.getKeyPricing() != null && customer.getKeyPricing().getCapRate() != null ){
    			rent = customer.getKeyPricing().getCapRate();
        	}
    	}
    	if(isRateAfterTax() || isRateBeforeTax()){
    		if(	getForcastedFinancial() != null && getForcastedFinancial().getPurchasePrice() != null && getForcastedFinancial().getTransactionCost() != null){
    			rent = getForcastedFinancial().getPurchasePrice();
    			rent = rent.add(getForcastedFinancial().getTransactionCost());
        		
        		Customer customer = (Customer)ContextHolder.getContext().getNamedContext().get("root");
        		if(customer.getKeyPricing() != null && customer.getKeyPricing().getLoanAmount() != null ){
        			rent = rent.subtract(customer.getKeyPricing().getLoanAmount());
            	}
        	}
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