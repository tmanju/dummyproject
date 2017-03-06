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
public class IncomeMultiplier extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /**  */
    private static final long serialVersionUID = 1L;

    //~ Methods --------------------------------------------------------------------------------------------------------
    public Boolean isGrossRent(){
    	boolean type = false;
    	if(getIncomeMultiplier() != null && ("INCOME_MULTIPLIER_TYPE_GRM".equals(getIncomeMultiplier().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isGrossIncome(){
    	boolean type = false;
    	if(getIncomeMultiplier() != null && ("INCOME_MULTIPLIER_TYPE_GIM".equals(getIncomeMultiplier().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isNetIncome(){
    	boolean type = false;
    	if(getIncomeMultiplier() != null && ("INCOME_MULTIPLIER_TYPE_NIM".equals(getIncomeMultiplier().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    
    public BigDecimal getMarketPrice(){
    	BigDecimal price = BigDecimal.ZERO;
    	if(	getForcastedFinancial() != null && getForcastedFinancial().getPurchasePrice() != null){
    		price = getForcastedFinancial().getPurchasePrice();
    	}
    	
    	return price.equals(BigDecimal.ZERO)? null : price;
    }
    public BigDecimal getGrossRent(){
    	BigDecimal rent = BigDecimal.ZERO;
    	if(isGrossRent() && getForcastedFinancial() != null && getForcastedFinancial().getGPR() != null){
    		rent = getForcastedFinancial().getGPR();
    	}
    	if(isGrossIncome() && getForcastedFinancial() != null && getForcastedFinancial().getFooterTotalAmt() != null){
    		rent = getForcastedFinancial().getFooterTotalAmt();
    	}
    	if(isNetIncome() && getForcastedFinancial() != null && getForcastedFinancial().getFooterTotalAmt() != null && getForcastedFinancial().getFooterTotalAmtExpense() != null){
    		rent = getForcastedFinancial().getFooterTotalAmt().subtract(getForcastedFinancial().getFooterTotalAmtExpense());
    	}
    	
    	return rent.equals(BigDecimal.ZERO)? null : rent;
    }
    public BigDecimal getMultipliers(){
    	BigDecimal amount = BigDecimal.ZERO;
    	if(getMarketPrice() != null && getGrossRent() != null && getGrossRent().compareTo(BigDecimal.ZERO) > 0){
    		amount = getMarketPrice().divide(getGrossRent(),2,RoundingMode.HALF_UP);
    	}
    	
    	return amount.equals(BigDecimal.ZERO)? null : amount;
    }
}