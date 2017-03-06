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
public class FinancialAssessmentExpense extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /**  */
    private static final long serialVersionUID = 1L;

    //~ Methods --------------------------------------------------------------------------------------------------------
    public BigDecimal getYearOne(){
    	BigDecimal yearOne = null;
    	for(IncomeExpense incomeExpense : getFinancialCRE().getIncomeExpenses()){
    		if( incomeExpense.getDetails() != null){
    			if("DETAILS_TYPE_YEAR_ONE".equalsIgnoreCase(incomeExpense.getDetails().getKey())){
    				for(ExpenseLine expenseLine : incomeExpense.getExpenseLinesDer()){
        				if(expenseLine.getExpenseLegend().getKey().equals(getExpenseLegend().getKey())){
        					yearOne = expenseLine.getTotalAmountProRata();
        				}
            		}
    			}
    			
    		}
    		
    	}
    	return yearOne;
    }
    public BigDecimal getYearTwo(){
    	BigDecimal yearTwo = null;
    	for(IncomeExpense incomeExpense : getFinancialCRE().getIncomeExpenses()){
    		if( incomeExpense.getDetails() != null){
    			if("DETAILS_TYPE_YEAR_TWO".equalsIgnoreCase(incomeExpense.getDetails().getKey())){
    				for(ExpenseLine expenseLine : incomeExpense.getExpenseLinesDer()){
        				if(expenseLine.getExpenseLegend().getKey().equals(getExpenseLegend().getKey())){
        					yearTwo = expenseLine.getTotalAmountProRata();
        				}
            		}
    			}
    			
    		}
    		
    	}
    	return yearTwo;
    }
    public BigDecimal getYearThree(){
    	BigDecimal yearThree = null;
    	for(IncomeExpense incomeExpense : getFinancialCRE().getIncomeExpenses()){
    		if( incomeExpense.getDetails() != null){
    			if("DETAILS_TYPE_YEAR_THREE".equalsIgnoreCase(incomeExpense.getDetails().getKey())){
    				for(ExpenseLine expenseLine : incomeExpense.getExpenseLinesDer()){
        				if(expenseLine.getExpenseLegend().getKey().equals(getExpenseLegend().getKey())){
        					yearThree = expenseLine.getTotalAmountProRata();
        				}
            		}
    			}
    			
    		}
    		
    	}
    	return yearThree;
    }
    public BigDecimal getYearAppraisal(){
    	BigDecimal yearAppraisal = null;
    	for(IncomeExpense incomeExpense : getFinancialCRE().getIncomeExpenses()){
    		if( incomeExpense.getDetails() != null){
    			if("DETAILS_TYPE_APPRAISAL".equalsIgnoreCase(incomeExpense.getDetails().getKey())){
    				for(ExpenseLine expenseLine : incomeExpense.getExpenseLinesDer()){
        				if(expenseLine.getExpenseLegend().getKey().equals(getExpenseLegend().getKey())){
        					yearAppraisal = expenseLine.getTotalAmountProRata();
        				}
            		}
    			}
    			
    		}
    		
    	}
    	return yearAppraisal;
    }
    public BigDecimal getGrossExpense(){
    	BigDecimal grossExpense =  BigDecimal.ZERO;
    	for(IncomeExpense incomeExpense : getFinancialCRE().getIncomeExpenses()){
    		if(incomeExpense.getTotalExpense() != null){
    			grossExpense = grossExpense.add(incomeExpense.getTotalExpense());
    			
    		}
    	}
    	return grossExpense;
    }
    public BigDecimal getYearOnePu(){
    	BigDecimal pu =  BigDecimal.ZERO;
    	if(getYearOne() != null && getAssessmentCollateral() != null && getAssessmentCollateral().getUnitArea() != null  &&
    			getAssessmentCollateral().getUnitArea().compareTo(BigDecimal.ZERO) > 0 ){
    		pu = getYearOne().divide(getAssessmentCollateral().getUnitArea(),2, RoundingMode.HALF_UP);
    	}
    	return  pu;
    }
    
    public BigDecimal getYearTwoPu(){
    	BigDecimal pu =  BigDecimal.ZERO;
    	if( getYearTwo() != null && getAssessmentCollateral() != null && getAssessmentCollateral().getUnitArea() != null  &&
    			getAssessmentCollateral().getUnitArea().compareTo(BigDecimal.ZERO) > 0 ){
    		pu = getYearTwo().divide(getAssessmentCollateral().getUnitArea(),2, RoundingMode.HALF_UP);
    	}
    	return  pu;
    }
    
    public BigDecimal getYearThreePu(){
    	BigDecimal pu =  BigDecimal.ZERO;
    	if(getYearThree()!= null && getAssessmentCollateral() != null && getAssessmentCollateral().getUnitArea() != null  &&
    			getAssessmentCollateral().getUnitArea().compareTo(BigDecimal.ZERO) > 0 ){
    		pu = getYearThree().divide(getAssessmentCollateral().getUnitArea(),2, RoundingMode.HALF_UP);
    	}
    	return  pu;
    }
    
    public BigDecimal getYearAppraisalPu(){
    	BigDecimal pu =  BigDecimal.ZERO;
    	if(getYearAppraisal() != null && getAssessmentCollateral() != null && getAssessmentCollateral().getUnitArea() != null  &&
    			getAssessmentCollateral().getUnitArea().compareTo(BigDecimal.ZERO) > 0 ){
    		pu = getYearAppraisal().divide(getAssessmentCollateral().getUnitArea(),2, RoundingMode.HALF_UP);
    	}
    	return  pu;
    }
    
    public BigDecimal getUnderWritingPu(){
    	BigDecimal pu =  BigDecimal.ZERO;
    	if(getUnderWriting() != null && getAssessmentCollateral() != null && getAssessmentCollateral().getUnitArea() != null  &&
    			getAssessmentCollateral().getUnitArea().compareTo(BigDecimal.ZERO) > 0 ){
    		pu = getUnderWriting().divide(getAssessmentCollateral().getUnitArea(),2, RoundingMode.HALF_UP);
    	}
    	return  pu;
    }
    //ProRated 
    public BigDecimal getProRataYearOne(){
    	BigDecimal yearOne = null;
    	for(IncomeExpense incomeExpense : getFinancialCRE().getIncomeExpenses()){
    		if( incomeExpense.getDetails() != null){
    			if("DETAILS_TYPE_YEAR_ONE".equalsIgnoreCase(incomeExpense.getDetails().getKey())){
    				for(ExpenseLine expenseLine : incomeExpense.getExpenseLinesDer()){
        				if(expenseLine.getExpenseLegend().getKey().equals(getExpenseLegend().getKey())){
        					yearOne = expenseLine.getProRated();
        				}
            		}
    			}
    			
    		}
    		
    	}
    	return yearOne;
    }
    public BigDecimal getProRataYearTwo(){
    	BigDecimal yearTwo = null;
    	for(IncomeExpense incomeExpense : getFinancialCRE().getIncomeExpenses()){
    		if( incomeExpense.getDetails() != null){
    			if("DETAILS_TYPE_YEAR_TWO".equalsIgnoreCase(incomeExpense.getDetails().getKey())){
    				for(ExpenseLine expenseLine : incomeExpense.getExpenseLinesDer()){
        				if(expenseLine.getExpenseLegend().getKey().equals(getExpenseLegend().getKey())){
        					yearTwo = expenseLine.getProRated();
        				}
            		}
    			}
    			
    		}
    		
    	}
    	return yearTwo;
    }
    public BigDecimal getProRataYearThree(){
    	BigDecimal yearThree = null;
    	for(IncomeExpense incomeExpense : getFinancialCRE().getIncomeExpenses()){
    		if( incomeExpense.getDetails() != null){
    			if("DETAILS_TYPE_YEAR_THREE".equalsIgnoreCase(incomeExpense.getDetails().getKey())){
    				for(ExpenseLine expenseLine : incomeExpense.getExpenseLinesDer()){
        				if(expenseLine.getExpenseLegend().getKey().equals(getExpenseLegend().getKey())){
        					yearThree = expenseLine.getProRated();
        				}
            		}
    			}
    			
    		}
    		
    	}
    	return yearThree;
    }
    public BigDecimal getProRataAppraisal(){
    	BigDecimal yearAppraisal = null;
    	for(IncomeExpense incomeExpense : getFinancialCRE().getIncomeExpenses()){
    		if( incomeExpense.getDetails() != null){
    			if("DETAILS_TYPE_APPRAISAL".equalsIgnoreCase(incomeExpense.getDetails().getKey())){
    				for(ExpenseLine expenseLine : incomeExpense.getExpenseLinesDer()){
        				if(expenseLine.getExpenseLegend().getKey().equals(getExpenseLegend().getKey())){
        					yearAppraisal = expenseLine.getProRated();
        				}
            		}
    			}
    			
    		}
    		
    	}
    	return yearAppraisal;
    }
    public BigDecimal getProRataYearOnePu(){
    	BigDecimal pu =  BigDecimal.ZERO;
    	if(getProRataYearOne() != null && getAssessmentCollateral() != null && getAssessmentCollateral().getUnitArea() != null  &&
    			getAssessmentCollateral().getUnitArea().compareTo(BigDecimal.ZERO) > 0 ){
    		pu = getProRataYearOne().divide(getAssessmentCollateral().getUnitArea(),2, RoundingMode.HALF_UP);
    	}
    	return  pu;
    }
    
    public BigDecimal getProRataYearTwoPu(){
    	BigDecimal pu =  BigDecimal.ZERO;
    	if( getProRataYearTwo() != null && getAssessmentCollateral() != null && getAssessmentCollateral().getUnitArea() != null  &&
    			getAssessmentCollateral().getUnitArea().compareTo(BigDecimal.ZERO) > 0 ){
    		pu = getProRataYearTwo().divide(getAssessmentCollateral().getUnitArea(),2, RoundingMode.HALF_UP);
    	}
    	return  pu;
    }
    
    public BigDecimal getProRataYearThreePu(){
    	BigDecimal pu =  BigDecimal.ZERO;
    	if(getProRataYearThree()!= null && getAssessmentCollateral() != null && getAssessmentCollateral().getUnitArea() != null  &&
    			getAssessmentCollateral().getUnitArea().compareTo(BigDecimal.ZERO) > 0 ){
    		pu = getProRataYearThree().divide(getAssessmentCollateral().getUnitArea(),2, RoundingMode.HALF_UP);
    	}
    	return  pu;
    }
    
    public BigDecimal getProRataAppraisalPu(){
    	BigDecimal pu =  BigDecimal.ZERO;
    	if(getProRataAppraisal() != null && getAssessmentCollateral() != null && getAssessmentCollateral().getUnitArea() != null  &&
    			getAssessmentCollateral().getUnitArea().compareTo(BigDecimal.ZERO) > 0 ){
    		pu = getProRataAppraisal().divide(getAssessmentCollateral().getUnitArea(),2, RoundingMode.HALF_UP);
    	}
    	return  pu;
    }
}