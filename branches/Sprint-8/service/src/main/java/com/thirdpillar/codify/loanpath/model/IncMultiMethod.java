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
public class IncMultiMethod extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /**  */
    private static final long serialVersionUID = 1L;

    //~ Methods --------------------------------------------------------------------------------------------------------
    public Boolean isGrossRentMethod(){
    	boolean type = false;
    	if(getIncomeMultiMethod() != null && ("INCOME_MULTIPLIER_METHOD_TYPE_GRMM".equals(getIncomeMultiMethod().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isGrossIncomeMethod(){
    	boolean type = false;
    	if(getIncomeMultiMethod() != null && ("INCOME_MULTIPLIER_METHOD_TYPE_GIMM".equals(getIncomeMultiMethod().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isNetIncomeMethod(){
    	boolean type = false;
    	if(getIncomeMultiMethod() != null && ("INCOME_MULTIPLIER_METHOD_TYPE_NIMM".equals(getIncomeMultiMethod().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    
    public BigDecimal getMarketPrice(){
    	BigDecimal price = BigDecimal.ZERO;
    	if(getMultipliers() != null && getGrossRent() != null){
    		price = getMultipliers().multiply(getGrossRent());
    	}
    	
    	return price.equals(BigDecimal.ZERO)? null : price;
    }
    public BigDecimal getGrossRent(){
    	BigDecimal rent = BigDecimal.ZERO;
    	if(isGrossRentMethod() && getForcastedFinancial() != null && getForcastedFinancial().getGPR() != null){
    		rent = getForcastedFinancial().getGPR();
    	}
    	if(isGrossIncomeMethod() && getForcastedFinancial() != null && getForcastedFinancial().getFooterTotalAmt() != null){
    		rent = getForcastedFinancial().getFooterTotalAmt();
    	}
    	if(isNetIncomeMethod() && getForcastedFinancial() != null && getForcastedFinancial().getFooterTotalAmt() != null && getForcastedFinancial().getFooterTotalAmtExpense() != null){
    		rent = getForcastedFinancial().getFooterTotalAmt().subtract(getForcastedFinancial().getFooterTotalAmtExpense());
    	}
    	
    	return rent.equals(BigDecimal.ZERO)? null : rent;
    }
    public BigDecimal getMultipliers(){
    	BigDecimal amount = BigDecimal.ZERO;
    	if(getForcastedFinancial() != null){
    		for(IncomeMultiplier incomeMultiplier: getForcastedFinancial().getIncomeMultipliers()){
    			if(isGrossRentMethod() && "INCOME_MULTIPLIER_TYPE_GRM".equals(incomeMultiplier.getIncomeMultiplier().getKey())){
    				amount = incomeMultiplier.getMultipliers();
    			}
    			if(isGrossIncomeMethod() && "INCOME_MULTIPLIER_TYPE_GIM".equals(incomeMultiplier.getIncomeMultiplier().getKey())){
    				amount = incomeMultiplier.getMultipliers();
    			}
    			if(isNetIncomeMethod() && "INCOME_MULTIPLIER_TYPE_NIM".equals(incomeMultiplier.getIncomeMultiplier().getKey())){
    				amount = incomeMultiplier.getMultipliers();
    			}
    		}
    	}
    	
    	
    	return amount.equals(BigDecimal.ZERO)? null : amount;
    }
}