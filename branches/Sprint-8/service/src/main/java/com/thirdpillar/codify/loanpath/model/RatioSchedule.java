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
import com.thirdpillar.codify.loanpath.service.MortgageBalanceCalculationService;

/**
 * DOCUMENT ME!
 *
 * @author   Ankush.Bhardwaj
 * @version  1.0
 * @since    1.0
 */
public class RatioSchedule extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /**  */
    private static final long serialVersionUID = 1L;

    //~ Methods --------------------------------------------------------------------------------------------------------
    //Boolean method to find each Ratio/Schedule 
    public Boolean isNoi(){
    	boolean type = false;
    	if(getRatioSchedule() != null && ("RATIO_SCHEDULE_TYPE_NOI".equals(getRatioSchedule().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isForcastedRatio(){
    	boolean type = false;
    	if(getRatioSchedule() != null && ("RATIO_SCHEDULE_TYPE_FORCASTED_EXPENSE_RATIO".equals(getRatioSchedule().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isDebtService(){
    	boolean type = false;
    	if(getRatioSchedule() != null && ("RATIO_SCHEDULE_TYPE_DEBT_SERVICE".equals(getRatioSchedule().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isCashFlowBeforeTax(){
    	boolean type = false;
    	if(getRatioSchedule() != null && ("RATIO_SCHEDULE_TYPE_CASHFLOW_BEFORE_TAX".equals(getRatioSchedule().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isEstInterestPaid(){
    	boolean type = false;
    	if(getRatioSchedule() != null && ("RATIO_SCHEDULE_TYPE_EST_INTEREST_PAID".equals(getRatioSchedule().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isPrincipalPaid(){
    	boolean type = false;
    	if(getRatioSchedule() != null && ("RATIO_SCHEDULE_TYPE_PRINICIPAL_PAID".equals(getRatioSchedule().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isMortageBalance(){
    	boolean type = false;
    	if(getRatioSchedule() != null && ("RATIO_SCHEDULE_TYPE_MORTAGE_BALANCE".equals(getRatioSchedule().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isDepreciation(){
    	boolean type = false;
    	if(getRatioSchedule() != null && ("RATIO_SCHEDULE_TYPE_DEPRECIATION".equals(getRatioSchedule().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isIncometax(){
    	boolean type = false;
    	if(getRatioSchedule() != null && ("RATIO_SCHEDULE_TYPE_INCOME_TAX".equals(getRatioSchedule().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    public Boolean isCashFlowAfterTax(){
    	boolean type = false;
    	if(getRatioSchedule() != null && ("RATIO_SCHEDULE_TYPE_CASHFLOW_AFTER_TAX".equals(getRatioSchedule().getKey()))){
    		type = true;
    	   	}
       	return type;
    }
    //Debt Service
    public BigDecimal getDebtService(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	Customer customer = (Customer)ContextHolder.getContext().getNamedContext().get("root");
    	if(customer.getKeyPricing() != null && customer.getKeyPricing().getDebtService() != null ){
    		forcast = customer.getKeyPricing().getDebtService();
    	}
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    //GetMortagageBalance 
    public BigDecimal getMortageBalance(BigDecimal index){
    	BigDecimal forcast = BigDecimal.ZERO;
    	BigDecimal findValue = index;
    	Customer customer = (Customer)ContextHolder.getContext().getNamedContext().get("root");
    	if(customer.getKeyPricing() != null && customer.getKeyPricing().getRequestedAmount() != null &&
    			customer.getKeyPricing().getInterestRate() != null && customer.getKeyPricing().getRequestedTerm() != null){
    		BigDecimal term = new BigDecimal(customer.getKeyPricing().getRequestedTerm());
    		MortgageBalanceCalculationService mBalance = new MortgageBalanceCalculationService();
    		List<BigDecimal> balanceList = mBalance.getMortgageBalance(customer.getKeyPricing().getRequestedAmount(), term, customer.getKeyPricing().getInterestRate());
    		forcast = balanceList.get(findValue.intValue());
    	}
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    	
    }
    //getLoan Amount
    public BigDecimal getLoanAmount(){
    	BigDecimal amount = BigDecimal.ZERO;
    	Customer customer = (Customer)ContextHolder.getContext().getNamedContext().get("root");
    	if(customer.getKeyPricing() != null && customer.getKeyPricing().getLoanAmount() != null ){
    		amount = customer.getKeyPricing().getLoanAmount();
    	}
    	return amount.equals(BigDecimal.ZERO)? null : amount;
    }
    //Find Depreciation
    public BigDecimal getDepreciation(){
    	BigDecimal amount = BigDecimal.ZERO;
    	if(getForcastedFinancial() != null && getForcastedFinancial().getDepAmt() != null && getForcastedFinancial().getRecoveryPeriod() != null && getForcastedFinancial().getRecoveryPeriod().compareTo(BigDecimal.ZERO) > 0  ){
    		amount = getForcastedFinancial().getDepAmt().divide(getForcastedFinancial().getRecoveryPeriod(),2, RoundingMode.HALF_UP);
    	}
    	return amount.equals(BigDecimal.ZERO)? null : amount;
    }
   
    //Calcuation Methods
    public BigDecimal getTotalAmount(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	//Calulation for NOI
    	if(isNoi() && getForcastedFinancial() != null && getForcastedFinancial().getFooterTotalAmt() != null && getForcastedFinancial().getFooterTotalAmtExpense() != null){
    		forcast = getForcastedFinancial().getFooterTotalAmt().subtract(getForcastedFinancial().getFooterTotalAmtExpense());
    	}
    	//Cal for Forcast Ratio
    	if(isForcastedRatio() && getForcastedFinancial() != null && getForcastedFinancial().getFooterTotalAmt() != null && getForcastedFinancial().getFooterTotalAmtExpense() != null &&
    			(getForcastedFinancial().getFooterTotalAmt().compareTo(BigDecimal.ZERO) > 0 )){
    		forcast = getForcastedFinancial().getFooterTotalAmtExpense().divide(getForcastedFinancial().getFooterTotalAmt(),4, RoundingMode.HALF_UP);
    		forcast = forcast.multiply(new BigDecimal(100));
    	}
    	//Debt Servvice will be null
    	
    	//CashFlow before Tax
    	if(isCashFlowBeforeTax() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast1() != null && getForcastedFinancial().getFooterExpenseForcast1() != null){
    		forcast = getForcastedFinancial().getFooterTotalAmt().subtract(getForcastedFinancial().getFooterTotalAmtExpense());
    	}
    	//CashFlow After Tax
    	if(isCashFlowAfterTax() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast1() != null && getForcastedFinancial().getFooterExpenseForcast1() != null){
    		forcast = getForcastedFinancial().getFooterTotalAmt().subtract(getForcastedFinancial().getFooterTotalAmtExpense());
    	}
    	
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    public BigDecimal getForcast1(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	//Calulation for NOI
    	if(isNoi() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast1() != null && getForcastedFinancial().getFooterExpenseForcast1() != null){
    		forcast = getForcastedFinancial().getFooterForcast1().subtract(getForcastedFinancial().getFooterExpenseForcast1());
    	}
    	//Cal for Forcasted Ratio
    	if(isForcastedRatio() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast1() != null && getForcastedFinancial().getFooterExpenseForcast1() != null &&
    			(getForcastedFinancial().getFooterForcast1().compareTo(BigDecimal.ZERO) > 0 )){
    		forcast = getForcastedFinancial().getFooterExpenseForcast1().divide(getForcastedFinancial().getFooterForcast1(),4, RoundingMode.HALF_UP);
    		forcast = forcast.multiply(new BigDecimal(100));
    	}
    	//Debt Service
    	if(isDebtService() && getDebtService()!= null ){
    		forcast = getDebtService();
    	} 
    	//CashFlow Index
    	if(isCashFlowBeforeTax() && getDebtService()!= null && getForcastedFinancial() != null && getForcastedFinancial().getFooterTotalAmt() != null && getForcastedFinancial().getFooterTotalAmtExpense() != null){
    		forcast = getForcastedFinancial().getFooterForcast1().subtract(getForcastedFinancial().getFooterExpenseForcast1());
    		forcast = forcast.subtract(getDebtService());
    	}
    	//Mortage Balance
    	if(isMortageBalance() && getMortageBalance(new BigDecimal(11)) != null){
    		forcast = getMortageBalance(new BigDecimal(11));
    	}
    	//Pricinpal Paid
    	if(isPrincipalPaid() && getLoanAmount() != null && getMortageBalance(new BigDecimal(11)) != null){
    		forcast = getLoanAmount().subtract(getMortageBalance(new BigDecimal(11)));
    	}
    	//Estimated Interest Paid
    	if(isEstInterestPaid() && getDebtService()!= null && getLoanAmount() != null && getMortageBalance(new BigDecimal(11)) != null){
    		forcast = getDebtService().subtract(getLoanAmount().subtract(getMortageBalance(new BigDecimal(11))));
    	}
    	//Depreciation
    	if(isDepreciation() && getDepreciation() != null){
    		forcast = getDepreciation();
    	}
    	//IncomeTax
    	if(isIncometax() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast1() != null && getForcastedFinancial().getFooterExpenseForcast1() != null &&
    			getDebtService()!= null && getLoanAmount() != null && getMortageBalance(new BigDecimal(11)) != null && getDepreciation() != null && getForcastedFinancial().getTaxRate() != null){
        		
    		forcast = getForcastedFinancial().getFooterForcast1().subtract(getForcastedFinancial().getFooterExpenseForcast1());
        	forcast = forcast.subtract(getDebtService().subtract(getLoanAmount().subtract(getMortageBalance(new BigDecimal(11)))));
        	forcast = forcast.subtract(getDepreciation());
        	forcast = forcast.multiply(getForcastedFinancial().getTaxRate());
        	forcast = forcast.divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
    	}
    	//CashFlow After Tax
    	if(isCashFlowAfterTax() && getDebtService()!= null && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast1() != null && 
    			getForcastedFinancial().getFooterExpenseForcast1() != null && getLoanAmount() != null && getMortageBalance(new BigDecimal(11)) != null && getDepreciation() != null && getForcastedFinancial().getTaxRate() != null ){
    		
    		forcast = getForcastedFinancial().getFooterForcast1().subtract(getForcastedFinancial().getFooterExpenseForcast1());
    		forcast = forcast.subtract(getDebtService());
			BigDecimal incomeTax = getForcastedFinancial().getFooterForcast1().subtract(getForcastedFinancial().getFooterExpenseForcast1());
        	incomeTax = incomeTax.subtract(getDebtService().subtract(getLoanAmount().subtract(getMortageBalance(new BigDecimal(11)))));
        	incomeTax = incomeTax.subtract(getDepreciation());
        	incomeTax = incomeTax.multiply(getForcastedFinancial().getTaxRate());
        	incomeTax = incomeTax.divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
			forcast = forcast.subtract(incomeTax);
    	}
    	
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    public BigDecimal getForcast2(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	//Calulation for NOI
    	if(isNoi() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast2() != null && getForcastedFinancial().getFooterExpenseForcast2() != null){
    		forcast = getForcastedFinancial().getFooterForcast2().subtract(getForcastedFinancial().getFooterExpenseForcast2());
    	}
    	//Cal for Forcasted Ratio
    	if(isForcastedRatio() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast2() != null && getForcastedFinancial().getFooterExpenseForcast2() != null &&
    			(getForcastedFinancial().getFooterForcast2().compareTo(BigDecimal.ZERO) > 0 )){
    		forcast = getForcastedFinancial().getFooterExpenseForcast2().divide(getForcastedFinancial().getFooterForcast2(),4, RoundingMode.HALF_UP);
    		forcast = forcast.multiply(new BigDecimal(100));
    	}
    	//Debt Service
    	if(isDebtService() && getDebtService()!= null ){
    		forcast = getDebtService();
    	} 
    	//CashFlow Index
    	if(isCashFlowBeforeTax() && getDebtService()!= null && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast1() != null && getForcastedFinancial().getFooterExpenseForcast1() != null){
    		forcast = getForcastedFinancial().getFooterForcast2().subtract(getForcastedFinancial().getFooterExpenseForcast2());
    		forcast = forcast.subtract(getDebtService());
    	}
    	//Mortage Balance
    	if(isMortageBalance() && getMortageBalance(new BigDecimal(23)) != null){
    		forcast = getMortageBalance(new BigDecimal(23));
    	}
    	//Pricinpal Paid
    	if(isPrincipalPaid() && getLoanAmount() != null && getMortageBalance(new BigDecimal(23)) != null){
    		forcast = getMortageBalance(new BigDecimal(11)).subtract(getMortageBalance(new BigDecimal(23)));
    	}
    	//Estimated Interest Paid
    	if(isEstInterestPaid() && getDebtService()!= null && getLoanAmount() != null && getMortageBalance(new BigDecimal(23)) != null){
    		forcast = getDebtService().subtract(getMortageBalance(new BigDecimal(11)).subtract(getMortageBalance(new BigDecimal(23))));
    	}
    	//Depreciation
    	if(isDepreciation() && getDepreciation() != null){
    		forcast = getDepreciation();
    	}
    	//IncomeTax
    	if(isIncometax() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast2() != null && getForcastedFinancial().getFooterExpenseForcast2() != null &&
    			getDebtService()!= null && getLoanAmount() != null && getMortageBalance(new BigDecimal(23)) != null && getDepreciation() != null && getForcastedFinancial().getTaxRate() != null){
        		
    		forcast = getForcastedFinancial().getFooterForcast2().subtract(getForcastedFinancial().getFooterExpenseForcast2());
        	forcast = forcast.subtract(getDebtService().subtract(getMortageBalance(new BigDecimal(11)).subtract(getMortageBalance(new BigDecimal(23)))));
        	forcast = forcast.subtract(getDepreciation());
        	forcast = forcast.multiply(getForcastedFinancial().getTaxRate());
        	forcast = forcast.divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
    	}
    	//CashFlow after Tax
    	if(isCashFlowAfterTax() && getDebtService()!= null && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast2() != null && 
    			getForcastedFinancial().getFooterExpenseForcast2() != null  && getMortageBalance(new BigDecimal(23)) != null && getDepreciation() != null && getForcastedFinancial().getTaxRate() != null ){
    		
    		forcast = getForcastedFinancial().getFooterForcast2().subtract(getForcastedFinancial().getFooterExpenseForcast2());
    		forcast = forcast.subtract(getDebtService());
			
			BigDecimal incomeTax = getForcastedFinancial().getFooterForcast2().subtract(getForcastedFinancial().getFooterExpenseForcast2());
        	incomeTax = incomeTax.subtract(getDebtService().subtract(getMortageBalance(new BigDecimal(11)).subtract(getMortageBalance(new BigDecimal(23)))));
        	incomeTax = incomeTax.subtract(getDepreciation());
        	incomeTax = incomeTax.multiply(getForcastedFinancial().getTaxRate());
        	incomeTax = incomeTax.divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
			
			forcast = forcast.subtract(incomeTax);
    	}
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    public BigDecimal getForcast3(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	//Calulation for NOI
    	if(isNoi() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast3() != null && getForcastedFinancial().getFooterExpenseForcast3() != null){
    		forcast = getForcastedFinancial().getFooterForcast3().subtract(getForcastedFinancial().getFooterExpenseForcast3());
    	}
    	//Cal for Forcasted Ratio
    	if(isForcastedRatio() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast3() != null && getForcastedFinancial().getFooterExpenseForcast3() != null &&
    			(getForcastedFinancial().getFooterForcast3().compareTo(BigDecimal.ZERO) > 0 )){
    		forcast = getForcastedFinancial().getFooterExpenseForcast3().divide(getForcastedFinancial().getFooterForcast3(),4, RoundingMode.HALF_UP);
    		forcast = forcast.multiply(new BigDecimal(100));
    	}
    	//Debt Service
    	if(isDebtService() && getDebtService()!= null ){
    		forcast = getDebtService();
    	} 
    	//CashFlow Index
    	if(isCashFlowBeforeTax() && getDebtService()!= null && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast1() != null && getForcastedFinancial().getFooterExpenseForcast1() != null){
    		forcast = getForcastedFinancial().getFooterForcast3().subtract(getForcastedFinancial().getFooterExpenseForcast3());
    		forcast = forcast.subtract(getDebtService());
    	}
    	//Mortage Balance
    	if(isMortageBalance() && getMortageBalance(new BigDecimal(35)) != null){
    		forcast = getMortageBalance(new BigDecimal(35));
    	}
    	//Pricinpal Paid
    	if(isPrincipalPaid() && getLoanAmount() != null && getMortageBalance(new BigDecimal(35)) != null){
    		forcast = getMortageBalance(new BigDecimal(23)).subtract(getMortageBalance(new BigDecimal(35)));
    	}
    	//Estimated Interest Paid
    	if(isEstInterestPaid() && getDebtService()!= null && getLoanAmount() != null && getMortageBalance(new BigDecimal(35)) != null){
    		forcast = getDebtService().subtract(getMortageBalance(new BigDecimal(23)).subtract(getMortageBalance(new BigDecimal(35))));
    	}
    	//Depreciation
    	if(isDepreciation() && getDepreciation() != null){
    		forcast = getDepreciation();
    	}
    	//IncomeTax
    	if(isIncometax() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast3() != null && getForcastedFinancial().getFooterExpenseForcast3() != null &&
    			getDebtService()!= null && getMortageBalance(new BigDecimal(35)) != null && getDepreciation() != null && getForcastedFinancial().getTaxRate() != null){
        		
    		forcast = getForcastedFinancial().getFooterForcast3().subtract(getForcastedFinancial().getFooterExpenseForcast3());
        	forcast = forcast.subtract(getDebtService().subtract(getMortageBalance(new BigDecimal(23)).subtract(getMortageBalance(new BigDecimal(35)))));
        	forcast = forcast.subtract(getDepreciation());
        	forcast = forcast.multiply(getForcastedFinancial().getTaxRate());
        	forcast = forcast.divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
    	}
    	//CashFlow after Tax
    	if(isCashFlowAfterTax() && getDebtService()!= null && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast3() != null && 
    			getForcastedFinancial().getFooterExpenseForcast3() != null  && getMortageBalance(new BigDecimal(35)) != null && getDepreciation() != null && getForcastedFinancial().getTaxRate() != null ){
    		
    		forcast = getForcastedFinancial().getFooterForcast3().subtract(getForcastedFinancial().getFooterExpenseForcast3());
    		forcast = forcast.subtract(getDebtService());
			
			BigDecimal incomeTax = getForcastedFinancial().getFooterForcast3().subtract(getForcastedFinancial().getFooterExpenseForcast3());
        	incomeTax = incomeTax.subtract(getDebtService().subtract(getMortageBalance(new BigDecimal(23)).subtract(getMortageBalance(new BigDecimal(35)))));
        	incomeTax = incomeTax.subtract(getDepreciation());
        	incomeTax = incomeTax.multiply(getForcastedFinancial().getTaxRate());
        	incomeTax = incomeTax.divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
			
			forcast = forcast.subtract(incomeTax);
    	}
    	
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    public BigDecimal getForcast4(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	//Calulation for NOI
    	if(isNoi() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast4() != null && getForcastedFinancial().getFooterExpenseForcast4() != null){
    		forcast = getForcastedFinancial().getFooterForcast4().subtract(getForcastedFinancial().getFooterExpenseForcast4());
    	}
    	//Cal for Forcasted Ratio
    	if(isForcastedRatio() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast4() != null && getForcastedFinancial().getFooterExpenseForcast4() != null &&
    			(getForcastedFinancial().getFooterForcast4().compareTo(BigDecimal.ZERO) > 0 )){
    		forcast = getForcastedFinancial().getFooterExpenseForcast4().divide(getForcastedFinancial().getFooterForcast4(),4, RoundingMode.HALF_UP);
    		forcast = forcast.multiply(new BigDecimal(100));
    	}
    	//Debt Service
    	if(isDebtService() && getDebtService()!= null ){
    		forcast = getDebtService();
    	} 
    	//CashFlow Index
    	if(isCashFlowBeforeTax() && getDebtService()!= null && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast1() != null && getForcastedFinancial().getFooterExpenseForcast1() != null){
    		forcast = getForcastedFinancial().getFooterForcast4().subtract(getForcastedFinancial().getFooterExpenseForcast4());
    		forcast = forcast.subtract(getDebtService());
    	}
    	//Mortage Balance
    	if(isMortageBalance() && getMortageBalance(new BigDecimal(47)) != null){
    		forcast = getMortageBalance(new BigDecimal(47));
    	}
    	//Pricinpal Paid
    	if(isPrincipalPaid() && getLoanAmount() != null && getMortageBalance(new BigDecimal(47)) != null){
    		forcast = getMortageBalance(new BigDecimal(35)).subtract(getMortageBalance(new BigDecimal(47)));
    	}
    	//Estimated Interest Paid
    	if(isEstInterestPaid() && getDebtService()!= null  && getLoanAmount() != null && getMortageBalance(new BigDecimal(47)) != null){
    		forcast = getDebtService().subtract(getMortageBalance(new BigDecimal(35)).subtract(getMortageBalance(new BigDecimal(47))));
    	}
    	//Depreciation
    	if(isDepreciation() && getDepreciation() != null){
    		forcast = getDepreciation();
    	}
    	//IncomeTax
    	if(isIncometax() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast4() != null && getForcastedFinancial().getFooterExpenseForcast4() != null &&
    			getDebtService()!= null && getMortageBalance(new BigDecimal(47)) != null && getDepreciation() != null && getForcastedFinancial().getTaxRate() != null){
        		
    		forcast = getForcastedFinancial().getFooterForcast4().subtract(getForcastedFinancial().getFooterExpenseForcast4());
        	forcast = forcast.subtract(getDebtService().subtract(getMortageBalance(new BigDecimal(35)).subtract(getMortageBalance(new BigDecimal(47)))));
        	forcast = forcast.subtract(getDepreciation());
        	forcast = forcast.multiply(getForcastedFinancial().getTaxRate());
        	forcast = forcast.divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
    	}
    	//CashFlow after Tax
    	if(isCashFlowAfterTax() && getDebtService()!= null && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast4() != null && 
    			getForcastedFinancial().getFooterExpenseForcast4() != null  && getMortageBalance(new BigDecimal(47)) != null && getDepreciation() != null && getForcastedFinancial().getTaxRate() != null ){
    		
    		forcast = getForcastedFinancial().getFooterForcast4().subtract(getForcastedFinancial().getFooterExpenseForcast4());
    		forcast = forcast.subtract(getDebtService());
			
			BigDecimal incomeTax = getForcastedFinancial().getFooterForcast4().subtract(getForcastedFinancial().getFooterExpenseForcast4());
        	incomeTax = incomeTax.subtract(getDebtService().subtract(getMortageBalance(new BigDecimal(35)).subtract(getMortageBalance(new BigDecimal(47)))));
        	incomeTax = incomeTax.subtract(getDepreciation());
        	incomeTax = incomeTax.multiply(getForcastedFinancial().getTaxRate());
        	incomeTax = incomeTax.divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
			
			forcast = forcast.subtract(incomeTax);
    	}
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    public BigDecimal getForcast5(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	//Calulation for NOI
    	if(isNoi() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast5() != null && getForcastedFinancial().getFooterExpenseForcast5() != null){
    		forcast = getForcastedFinancial().getFooterForcast5().subtract(getForcastedFinancial().getFooterExpenseForcast5());
    	}
    	//Cal for Forcasted Ratio
    	if(isForcastedRatio() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast5() != null && getForcastedFinancial().getFooterExpenseForcast5() != null &&
    			(getForcastedFinancial().getFooterForcast5().compareTo(BigDecimal.ZERO) > 0 )){
    		forcast = getForcastedFinancial().getFooterExpenseForcast5().divide(getForcastedFinancial().getFooterForcast5(),4, RoundingMode.HALF_UP);
    		forcast = forcast.multiply(new BigDecimal(100));
    	}
    	//Debt Service
    	if(isDebtService() && getDebtService()!= null ){
    		forcast = getDebtService();
    	} 
    	//CashFlow Index
    	if(isCashFlowBeforeTax() && getDebtService()!= null && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast1() != null && getForcastedFinancial().getFooterExpenseForcast1() != null){
    		forcast = getForcastedFinancial().getFooterForcast5().subtract(getForcastedFinancial().getFooterExpenseForcast5());
    		forcast = forcast.subtract(getDebtService());
    	}
    	//Mortage Balance
    	if(isMortageBalance() && getMortageBalance(new BigDecimal(59)) != null){
    		forcast = getMortageBalance(new BigDecimal(59));
    	}
    	//Pricinpal Paid
    	if(isPrincipalPaid() && getLoanAmount() != null && getMortageBalance(new BigDecimal(59)) != null){
    		forcast = getMortageBalance(new BigDecimal(47)).subtract(getMortageBalance(new BigDecimal(59)));
    	}
    	//Estimated Interest Paid
    	if(isEstInterestPaid() && getDebtService()!= null  && getLoanAmount() != null && getMortageBalance(new BigDecimal(59)) != null){
    		forcast = getDebtService().subtract(getMortageBalance(new BigDecimal(47)).subtract(getMortageBalance(new BigDecimal(59))));
    	}
    	//Depreciation
    	if(isDepreciation() && getDepreciation() != null){
    		forcast = getDepreciation();
    	}
    	//IncomeTax
    	if(isIncometax() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast5() != null && getForcastedFinancial().getFooterExpenseForcast5() != null &&
    			getDebtService()!= null && getMortageBalance(new BigDecimal(59)) != null && getDepreciation() != null && getForcastedFinancial().getTaxRate() != null){
        		
    		forcast = getForcastedFinancial().getFooterForcast5().subtract(getForcastedFinancial().getFooterExpenseForcast5());
        	forcast = forcast.subtract(getDebtService().subtract(getMortageBalance(new BigDecimal(47)).subtract(getMortageBalance(new BigDecimal(59)))));
        	forcast = forcast.subtract(getDepreciation());
        	forcast = forcast.multiply(getForcastedFinancial().getTaxRate());
        	forcast = forcast.divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
    	}
    	//CashFlow after Tax
    	if(isCashFlowAfterTax() && getDebtService()!= null && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast5() != null && 
    			getForcastedFinancial().getFooterExpenseForcast5() != null  && getMortageBalance(new BigDecimal(59)) != null && getDepreciation() != null && getForcastedFinancial().getTaxRate() != null ){
    		
    		forcast = getForcastedFinancial().getFooterForcast5().subtract(getForcastedFinancial().getFooterExpenseForcast5());
    		forcast = forcast.subtract(getDebtService());
			
			BigDecimal incomeTax = getForcastedFinancial().getFooterForcast5().subtract(getForcastedFinancial().getFooterExpenseForcast5());
        	incomeTax = incomeTax.subtract(getDebtService().subtract(getMortageBalance(new BigDecimal(47)).subtract(getMortageBalance(new BigDecimal(59)))));
        	incomeTax = incomeTax.subtract(getDepreciation());
        	incomeTax = incomeTax.multiply(getForcastedFinancial().getTaxRate());
        	incomeTax = incomeTax.divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
			
			forcast = forcast.subtract(incomeTax);
    	}
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    public BigDecimal getForcast6(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	//Calulation for NOI
    	if(isNoi() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast6() != null && getForcastedFinancial().getFooterExpenseForcast6() != null){
    		forcast = getForcastedFinancial().getFooterForcast6().subtract(getForcastedFinancial().getFooterExpenseForcast6());
    	}
    	//Cal for Forcasted Ratio
    	if(isForcastedRatio() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast6() != null && getForcastedFinancial().getFooterExpenseForcast6() != null &&
    			(getForcastedFinancial().getFooterForcast6().compareTo(BigDecimal.ZERO) > 0 )){
    		forcast = getForcastedFinancial().getFooterExpenseForcast6().divide(getForcastedFinancial().getFooterForcast6(),4, RoundingMode.HALF_UP);
    		forcast = forcast.multiply(new BigDecimal(100));
    	}
    	//Debt Service
    	if(isDebtService() && getDebtService()!= null ){
    		forcast = getDebtService();
    	} 
    	//CashFlow Index
    	if(isCashFlowBeforeTax() && getDebtService()!= null && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast1() != null && getForcastedFinancial().getFooterExpenseForcast1() != null){
    		forcast = getForcastedFinancial().getFooterForcast6().subtract(getForcastedFinancial().getFooterExpenseForcast6());
    		forcast = forcast.subtract(getDebtService());
    	}
    	//Mortage Balance
    	if(isMortageBalance() && getMortageBalance(new BigDecimal(71)) != null){
    		forcast = getMortageBalance(new BigDecimal(71));
    	}
    	//Pricinpal Paid
    	if(isPrincipalPaid() && getLoanAmount() != null && getMortageBalance(new BigDecimal(71)) != null){
    		forcast = getMortageBalance(new BigDecimal(59)).subtract(getMortageBalance(new BigDecimal(71)));
    	}
    	//Estimated Interest Paid
    	if(isEstInterestPaid() && getDebtService()!= null  && getLoanAmount() != null && getMortageBalance(new BigDecimal(71)) != null){
    		forcast = getDebtService().subtract(getMortageBalance(new BigDecimal(59)).subtract(getMortageBalance(new BigDecimal(71))));
    	}
    	//Depreciation
    	if(isDepreciation() && getDepreciation() != null){
    		forcast = getDepreciation();
    	}
    	//IncomeTax
    	if(isIncometax() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast6() != null && getForcastedFinancial().getFooterExpenseForcast6() != null &&
    			getDebtService()!= null && getMortageBalance(new BigDecimal(71)) != null && getDepreciation() != null && getForcastedFinancial().getTaxRate() != null){
        		
    		forcast = getForcastedFinancial().getFooterForcast6().subtract(getForcastedFinancial().getFooterExpenseForcast6());
        	forcast = forcast.subtract(getDebtService().subtract(getMortageBalance(new BigDecimal(59)).subtract(getMortageBalance(new BigDecimal(71)))));
        	forcast = forcast.subtract(getDepreciation());
        	forcast = forcast.multiply(getForcastedFinancial().getTaxRate());
        	forcast = forcast.divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
    	}
    	//CashFlow after Tax
    	if(isCashFlowAfterTax() && getDebtService()!= null && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast6() != null && 
    			getForcastedFinancial().getFooterExpenseForcast6() != null  && getMortageBalance(new BigDecimal(71)) != null && getDepreciation() != null && getForcastedFinancial().getTaxRate() != null ){
    		
    		forcast = getForcastedFinancial().getFooterForcast6().subtract(getForcastedFinancial().getFooterExpenseForcast6());
    		forcast = forcast.subtract(getDebtService());
			
			BigDecimal incomeTax = getForcastedFinancial().getFooterForcast6().subtract(getForcastedFinancial().getFooterExpenseForcast6());
        	incomeTax = incomeTax.subtract(getDebtService().subtract(getMortageBalance(new BigDecimal(59)).subtract(getMortageBalance(new BigDecimal(71)))));
        	incomeTax = incomeTax.subtract(getDepreciation());
        	incomeTax = incomeTax.multiply(getForcastedFinancial().getTaxRate());
        	incomeTax = incomeTax.divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
			
			forcast = forcast.subtract(incomeTax);
    	}
    	
    	
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    public BigDecimal getForcast7(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	//Calulation for NOI
    	if(isNoi() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast7() != null && getForcastedFinancial().getFooterExpenseForcast7() != null){
    		forcast = getForcastedFinancial().getFooterForcast7().subtract(getForcastedFinancial().getFooterExpenseForcast7());
    	}
    	//Cal for Forcasted Ratio
    	if(isForcastedRatio() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast7() != null && getForcastedFinancial().getFooterExpenseForcast7() != null &&
    			(getForcastedFinancial().getFooterForcast7().compareTo(BigDecimal.ZERO) > 0 )){
    		forcast = getForcastedFinancial().getFooterExpenseForcast7().divide(getForcastedFinancial().getFooterForcast7(),4, RoundingMode.HALF_UP);
    		forcast = forcast.multiply(new BigDecimal(100));
    	}
    	//Debt Service
    	if(isDebtService() && getDebtService()!= null ){
    		forcast = getDebtService();
    	} 
    	//CashFlow Index
    	if(isCashFlowBeforeTax() && getDebtService()!= null && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast1() != null && getForcastedFinancial().getFooterExpenseForcast1() != null){
    		forcast = getForcastedFinancial().getFooterForcast7().subtract(getForcastedFinancial().getFooterExpenseForcast7());
    		forcast = forcast.subtract(getDebtService());
    	}
    	//Mortage Balance
    	if(isMortageBalance() && getMortageBalance(new BigDecimal(83)) != null){
    		forcast = getMortageBalance(new BigDecimal(83));
    	}
    	//Pricinpal Paid
    	if(isPrincipalPaid() && getLoanAmount() != null && getMortageBalance(new BigDecimal(83)) != null){
    		forcast = getMortageBalance(new BigDecimal(71)).subtract(getMortageBalance(new BigDecimal(83)));
    	}
    	//Estimated Interest Paid
    	if(isEstInterestPaid() && getDebtService()!= null  && getLoanAmount() != null && getMortageBalance(new BigDecimal(83)) != null){
    		forcast = getDebtService().subtract(getMortageBalance(new BigDecimal(71)).subtract(getMortageBalance(new BigDecimal(83))));
    	}
    	//Depreciation
    	if(isDepreciation() && getDepreciation() != null){
    		forcast = getDepreciation();
    	}
    	//IncomeTax
    	if(isIncometax() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast7() != null && getForcastedFinancial().getFooterExpenseForcast7() != null &&
    			getDebtService()!= null && getMortageBalance(new BigDecimal(83)) != null && getDepreciation() != null && getForcastedFinancial().getTaxRate() != null){
        		
    		forcast = getForcastedFinancial().getFooterForcast7().subtract(getForcastedFinancial().getFooterExpenseForcast7());
        	forcast = forcast.subtract(getDebtService().subtract(getMortageBalance(new BigDecimal(71)).subtract(getMortageBalance(new BigDecimal(83)))));
        	forcast = forcast.subtract(getDepreciation());
        	forcast = forcast.multiply(getForcastedFinancial().getTaxRate());
        	forcast = forcast.divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
    	}
    	//CashFlow after Tax
    	if(isCashFlowAfterTax() && getDebtService()!= null && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast7() != null && 
    			getForcastedFinancial().getFooterExpenseForcast7() != null  && getMortageBalance(new BigDecimal(83)) != null && getDepreciation() != null && getForcastedFinancial().getTaxRate() != null ){
    		
    		forcast = getForcastedFinancial().getFooterForcast7().subtract(getForcastedFinancial().getFooterExpenseForcast7());
    		forcast = forcast.subtract(getDebtService());
			
			BigDecimal incomeTax = getForcastedFinancial().getFooterForcast7().subtract(getForcastedFinancial().getFooterExpenseForcast7());
        	incomeTax = incomeTax.subtract(getDebtService().subtract(getMortageBalance(new BigDecimal(71)).subtract(getMortageBalance(new BigDecimal(83)))));
        	incomeTax = incomeTax.subtract(getDepreciation());
        	incomeTax = incomeTax.multiply(getForcastedFinancial().getTaxRate());
        	incomeTax = incomeTax.divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
			
			forcast = forcast.subtract(incomeTax);
    	}
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    public BigDecimal getForcast8(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	//Calulation for NOI
    	if(isNoi() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast8() != null && getForcastedFinancial().getFooterExpenseForcast8() != null){
    		forcast = getForcastedFinancial().getFooterForcast8().subtract(getForcastedFinancial().getFooterExpenseForcast8());
    	}
    	//Cal for Forcasted Ratio
    	if(isForcastedRatio() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast8() != null && getForcastedFinancial().getFooterExpenseForcast8() != null &&
    			(getForcastedFinancial().getFooterForcast8().compareTo(BigDecimal.ZERO) > 0 )){
    		forcast = getForcastedFinancial().getFooterExpenseForcast8().divide(getForcastedFinancial().getFooterForcast8(),4, RoundingMode.HALF_UP);
    		forcast = forcast.multiply(new BigDecimal(100));
    	}
    	//Debt Service
    	if(isDebtService() && getDebtService()!= null ){
    		forcast = getDebtService();
    	} 
    	//CashFlow Index
    	if(isCashFlowBeforeTax() && getDebtService()!= null && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast1() != null && getForcastedFinancial().getFooterExpenseForcast1() != null){
    		forcast = getForcastedFinancial().getFooterForcast8().subtract(getForcastedFinancial().getFooterExpenseForcast8());
    		forcast = forcast.subtract(getDebtService());
    	}
    	//Mortage Balance
    	if(isMortageBalance() && getMortageBalance(new BigDecimal(95)) != null){
    		forcast = getMortageBalance(new BigDecimal(95));
    	}
    	//Pricinpal Paid
    	if(isPrincipalPaid() && getLoanAmount() != null && getMortageBalance(new BigDecimal(95)) != null){
    		forcast = getMortageBalance(new BigDecimal(83)).subtract(getMortageBalance(new BigDecimal(95)));
    	}
    	//Estimated Interest Paid
    	if(isEstInterestPaid() && getDebtService()!= null  && getLoanAmount() != null && getMortageBalance(new BigDecimal(95)) != null){
    		forcast = getDebtService().subtract(getMortageBalance(new BigDecimal(83)).subtract(getMortageBalance(new BigDecimal(95))));
    	}
    	//Depreciation
    	if(isDepreciation() && getDepreciation() != null){
    		forcast = getDepreciation();
    	}
    	//IncomeTax
    	if(isIncometax() && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast8() != null && getForcastedFinancial().getFooterExpenseForcast8() != null &&
    			getDebtService()!= null && getMortageBalance(new BigDecimal(95)) != null && getDepreciation() != null && getForcastedFinancial().getTaxRate() != null){
        		
    		forcast = getForcastedFinancial().getFooterForcast8().subtract(getForcastedFinancial().getFooterExpenseForcast8());
        	forcast = forcast.subtract(getDebtService().subtract(getMortageBalance(new BigDecimal(83)).subtract(getMortageBalance(new BigDecimal(95)))));
        	forcast = forcast.subtract(getDepreciation());
        	forcast = forcast.multiply(getForcastedFinancial().getTaxRate());
        	forcast = forcast.divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
    	}
    	//CashFlow after Tax
    	if(isCashFlowAfterTax() && getDebtService()!= null && getForcastedFinancial() != null && getForcastedFinancial().getFooterForcast8() != null && 
    			getForcastedFinancial().getFooterExpenseForcast8() != null  && getMortageBalance(new BigDecimal(95)) != null && getDepreciation() != null && getForcastedFinancial().getTaxRate() != null ){
    		
    		forcast = getForcastedFinancial().getFooterForcast8().subtract(getForcastedFinancial().getFooterExpenseForcast8());
    		forcast = forcast.subtract(getDebtService());
			
			BigDecimal incomeTax = getForcastedFinancial().getFooterForcast8().subtract(getForcastedFinancial().getFooterExpenseForcast8());
        	incomeTax = incomeTax.subtract(getDebtService().subtract(getMortageBalance(new BigDecimal(83)).subtract(getMortageBalance(new BigDecimal(95)))));
        	incomeTax = incomeTax.subtract(getDepreciation());
        	incomeTax = incomeTax.multiply(getForcastedFinancial().getTaxRate());
        	incomeTax = incomeTax.divide(new BigDecimal(100),2,RoundingMode.HALF_UP);
			
			forcast = forcast.subtract(incomeTax);
    	}
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
}