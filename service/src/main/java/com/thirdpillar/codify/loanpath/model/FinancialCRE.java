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

import com.thirdpillar.codify.loanpath.model.PricingOption;
import com.thirdpillar.codify.loanpath.model.Program;
import com.thirdpillar.foundation.service.ContextHolder;
import com.thirdpillar.foundation.service.ContextImpl;
import com.thirdpillar.foundation.service.LookupService;
import com.thirdpillar.codify.loanpath.model.AttributeChoice;

/**
 * DOCUMENT ME!
 *
 * @author   Ankush.Bhardwaj
 * @version  1.0
 * @since    1.0
 */
public class FinancialCRE extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /**  */
    private static final long serialVersionUID = 1L;

    //~ Methods --------------------------------------------------------------------------------------------------------

    public BigDecimal getGrossIncomeYearOne(){
    	BigDecimal grossIncomeYearOne = BigDecimal.ZERO;
    	for(IncomeExpense incomeExpense : getIncomeExpenses()){
    		if(incomeExpense.getDetails() != null){
    			if("DETAILS_TYPE_YEAR_ONE".equalsIgnoreCase(incomeExpense.getDetails().getKey())){
    				grossIncomeYearOne = incomeExpense.getTotalRevenue();
    			}
    			
    		}
    	}
    	return grossIncomeYearOne;
    }
    public BigDecimal getGrossIncomeYearTwo(){
    	BigDecimal grossIncomeYearTwo = BigDecimal.ZERO;
    	for(IncomeExpense incomeExpense : getIncomeExpenses()){
    		if(incomeExpense.getDetails() != null){
    			if("DETAILS_TYPE_YEAR_TWO".equalsIgnoreCase(incomeExpense.getDetails().getKey())){
    				grossIncomeYearTwo = incomeExpense.getTotalRevenue();
    			}
    			
    		}
    	}
    	return grossIncomeYearTwo;
    }
    public BigDecimal getGrossIncomeYearThree(){
    	BigDecimal grossIncomeYearThree = BigDecimal.ZERO;
    	for(IncomeExpense incomeExpense : getIncomeExpenses()){
    		if(incomeExpense.getDetails() != null){
    			if("DETAILS_TYPE_YEAR_THREE".equalsIgnoreCase(incomeExpense.getDetails().getKey())){
    				grossIncomeYearThree = incomeExpense.getTotalRevenue();
    			}
    			
    		}
    	}
    	return grossIncomeYearThree;
    }
    public BigDecimal getGrossIncomeAppraisal(){
    	BigDecimal grossIncomeAppraisal = BigDecimal.ZERO;
    	for(IncomeExpense incomeExpense : getIncomeExpenses()){
    		if(incomeExpense.getDetails() != null){
    			if("DETAILS_TYPE_APPRAISAL".equalsIgnoreCase(incomeExpense.getDetails().getKey())){
    				grossIncomeAppraisal = incomeExpense.getTotalRevenue();
    			}
    			
    		}
    	}
    	return grossIncomeAppraisal;
    }
    public BigDecimal getGrossIncomeUnderWriting(){
    	BigDecimal grossUnderWriting = BigDecimal.ZERO;
    	for(FinancialAssessmentRevenue revenue : getFinAssttRevDer()){
    		if(revenue.getUnderWriting() != null){
    				grossUnderWriting = grossUnderWriting.add(revenue.getUnderWriting());
    			
    		}
    	}
    	return grossUnderWriting.equals( BigDecimal.ZERO)? null : grossUnderWriting;
    }
    // Expense
    public BigDecimal getGrossExpenseYearOne(){
    	BigDecimal grossExpenseYearOne = BigDecimal.ZERO;
    	for(IncomeExpense incomeExpense : getIncomeExpenses()){
    		if(incomeExpense.getDetails() != null){
    			if("DETAILS_TYPE_YEAR_ONE".equalsIgnoreCase(incomeExpense.getDetails().getKey())){
    				grossExpenseYearOne = incomeExpense.getTotalExpense();
    			}
    			
    		}
    	}
    	return grossExpenseYearOne;
    }
    public BigDecimal getGrossExpenseYearTwo(){
    	BigDecimal grossExpenseYearTwo = BigDecimal.ZERO;
    	for(IncomeExpense incomeExpense : getIncomeExpenses()){
    		if(incomeExpense.getDetails() != null){
    			if("DETAILS_TYPE_YEAR_TWO".equalsIgnoreCase(incomeExpense.getDetails().getKey())){
    				grossExpenseYearTwo = incomeExpense.getTotalExpense();
    			}
    			
    		}
    	}
    	return grossExpenseYearTwo;
    }
    public BigDecimal getGrossExpenseYearThree(){
    	BigDecimal grossExpenseYearThree = BigDecimal.ZERO;
    	for(IncomeExpense incomeExpense : getIncomeExpenses()){
    		if(incomeExpense.getDetails() != null){
    			if("DETAILS_TYPE_YEAR_THREE".equalsIgnoreCase(incomeExpense.getDetails().getKey())){
    				grossExpenseYearThree = incomeExpense.getTotalExpense();
    			}
    			
    		}
    	}
    	return grossExpenseYearThree;
    }
    public BigDecimal getGrossExpenseAppraisal(){
    	BigDecimal grossExpenseAppraisal = BigDecimal.ZERO;
    	for(IncomeExpense incomeExpense : getIncomeExpenses()){
    		if(incomeExpense.getDetails() != null){
    			if("DETAILS_TYPE_APPRAISAL".equalsIgnoreCase(incomeExpense.getDetails().getKey())){
    				grossExpenseAppraisal = incomeExpense.getTotalExpense();
    			}
    			
    		}
    	}
    	return grossExpenseAppraisal;
    }
    public BigDecimal getGrossExpenseUnderWriting(){
    	BigDecimal grossUnderWriting = BigDecimal.ZERO;
    	for(FinancialAssessmentExpense expense : getFinAssttExpDer()){
    		if(expense.getUnderWriting() != null){
    				grossUnderWriting = grossUnderWriting.add(expense.getUnderWriting());
    			
    		}
    	}
    	return grossUnderWriting.equals( BigDecimal.ZERO)? null : grossUnderWriting;
    }
    public List<FinancialAssessmentRevenue> getFinAssttRevDer() {
    	List<FinancialAssessmentRevenue> revenueList = new ArrayList<FinancialAssessmentRevenue>();
    	if(getFinancialAssessmentRevenues() != null){
    		for(FinancialAssessmentRevenue finRevenue : getFinancialAssessmentRevenues()){
    			if(finRevenue.getYearOne() != null || finRevenue.getYearTwo() != null ||
    					finRevenue.getYearThree() != null || finRevenue.getYearAppraisal() != null){
    				revenueList.add(finRevenue);
    			}
    		}
    	}
    	return revenueList;
    }
    public List<FinancialAssessmentExpense> getFinAssttExpDer() {
    	List<FinancialAssessmentExpense> expenseList = new ArrayList<FinancialAssessmentExpense>();
    	if(getFinancialAssessmentExpenses() != null){
    		for(FinancialAssessmentExpense finExpense : getFinancialAssessmentExpenses()){
    			if(finExpense.getYearOne() != null || finExpense.getYearTwo() != null || 
    					finExpense.getYearThree() != null || finExpense.getYearAppraisal() != null){
    				expenseList.add(finExpense);
    			}
    		}
    	}
    	return expenseList;
    }
   public List<FinancialCRE> getHeadersNoi(){
    	List<FinancialCRE> list = new ArrayList<FinancialCRE>();
    	list.add(this);
	    return list;
    	
    }
   public BigDecimal getGrossNoiYearOne(){
	   BigDecimal grossNoiYearOne = BigDecimal.ZERO;
	   if(getGrossIncomeYearOne() != null && getGrossExpenseYearOne() != null){
		   grossNoiYearOne = getGrossIncomeYearOne().subtract(getGrossExpenseYearOne());
	   }
	   return grossNoiYearOne.equals( BigDecimal.ZERO)? null : grossNoiYearOne;
   }
   public BigDecimal getGrossNoiUnderWriting(){
	   BigDecimal grossUnderwriting = BigDecimal.ZERO;
	   if(getGrossIncomeUnderWriting() != null && getGrossExpenseUnderWriting()!= null){
		   grossUnderwriting = getGrossIncomeUnderWriting().subtract(getGrossExpenseUnderWriting());
	   }
	   return grossUnderwriting.equals( BigDecimal.ZERO)? null : grossUnderwriting;
	}
	public BigDecimal getGrossNoiYearTwo(){
		BigDecimal grossNoiYearTwo = BigDecimal.ZERO;
		if(getGrossIncomeYearTwo() != null && getGrossExpenseYearTwo() != null){
			grossNoiYearTwo= getGrossIncomeYearTwo().subtract(getGrossExpenseYearTwo()); 
		}
		return grossNoiYearTwo.equals( BigDecimal.ZERO)? null : grossNoiYearTwo;
	}
	public BigDecimal getGrossNoiYearThree(){
		BigDecimal grossNoiYearThree = BigDecimal.ZERO;
		if(getGrossIncomeYearThree() != null && getGrossExpenseYearThree() != null ){
			grossNoiYearThree = getGrossIncomeYearThree().subtract(getGrossExpenseYearThree());
		}
		return grossNoiYearThree.equals( BigDecimal.ZERO)? null : grossNoiYearThree;
	}
	public BigDecimal getGrossNoiAppraisal(){
		BigDecimal grossNoiAppraisal = BigDecimal.ZERO;
		if(getGrossIncomeAppraisal() != null  && getGrossExpenseAppraisal() != null){
			grossNoiAppraisal = getGrossIncomeAppraisal().subtract(getGrossExpenseAppraisal());
		}
		return grossNoiAppraisal.equals( BigDecimal.ZERO)? null : grossNoiAppraisal;
	}
   public String getFooterNameRevenue(){
	   return "";
   }
   public String getFooterNameExpense(){
	   return "";
   }
}