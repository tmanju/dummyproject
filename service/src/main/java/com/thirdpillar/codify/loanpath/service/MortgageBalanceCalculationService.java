/*
 * Copyright (c) 2005-2015 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;


/**
 * DOCUMENT ME!
 *
 * @author   Ankush.Bhardwaj
 * @version  1.0
 * @since    1.0
 */
/*
 * calculate the mortgage payment amount given the 
 * amount of the mortgage, the term of the mortgage, and the 
 * interest rate of the mortgage.
 */
@Component @Configurable public class MortgageBalanceCalculationService {

    //~ Methods --------------------------------------------------------------------------------------------------------
	
	public List<BigDecimal> getMortgageBalance(BigDecimal loanAmount, 
			BigDecimal termRate, BigDecimal interestRate){
		List<BigDecimal> balanceList = new ArrayList<BigDecimal>();
		
		double principal = loanAmount.doubleValue();
		double rate = interestRate.doubleValue();
		double term = termRate.doubleValue();
		
		// Monthly intertest rate
	      rate = rate/100/12; 
	    
	     //Calculate Monthly Payment
	    double payment = (principal * rate) / (1 - Math.pow(1 + rate, -term));
		
	     // round to two decimals
	    payment = (double)Math.round(payment * 100) / 100;
	    
	    // Loop through the term of the loan tracking the balance
	    for (int i=0; i<term; i++) {
	        // Add interest to the balance
	    	 principal += (principal * rate);
	         // Subtract the monthly payment
	    	 principal -= payment;
	    	 BigDecimal b = new BigDecimal(principal);
	    	 b= b.divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP);
	         // Display running balance
	          if(i < 181){
	        	  balanceList.add(b);
	          }
	       }
	      
		return balanceList;
		
	}

   

}
