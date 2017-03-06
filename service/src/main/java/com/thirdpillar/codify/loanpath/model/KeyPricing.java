/*
 * Copyright (c) 2005-2015 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.thirdpillar.foundation.model.BaseDataObject;



public class KeyPricing extends BaseDataObject { 

    //~ Methods --------------------------------------------------------------------------------------------------------
    public BigDecimal getLoanToValue(){
		BigDecimal loanValue = BigDecimal.ZERO;
		if(getCapRate() != null && getCapRate().compareTo(BigDecimal.ZERO) > 0 && getTotalNoi() != null && getTotalNoi().compareTo(BigDecimal.ZERO) > 0){
			BigDecimal appraisalValue = getTotalNoi().divide(getCapRate(),10, RoundingMode.HALF_UP);
			loanValue = getLoanAmount().divide(appraisalValue,2, RoundingMode.HALF_UP);
		}
		
		return loanValue.equals( BigDecimal.ZERO)? null : loanValue;
	}
    
    public BigDecimal getDscr(){
		BigDecimal dscr = BigDecimal.ZERO;
		
		if(getInterestRate() != null && getRequestedTerm() != null && getLoanAmount() != null && getTotalNoi() != null){
			double principal = getLoanAmount().doubleValue();
			double yearlyRate = getInterestRate().doubleValue();
			yearlyRate = yearlyRate/12;
			yearlyRate = yearlyRate/100;
			double termYears = getRequestedTerm().doubleValue();
			double pValue = principal / ((1- Math.pow(1 + yearlyRate, -termYears))/ yearlyRate)*12;
			BigDecimal debtService = new BigDecimal(pValue);
			debtService = debtService.divide(BigDecimal.ONE, 4, RoundingMode.HALF_UP);
			dscr = getTotalNoi().divide(debtService,2, RoundingMode.HALF_UP);
		}
		
		return dscr.equals( BigDecimal.ZERO)? null : dscr;
	}
	public BigDecimal getDebtService(){
		BigDecimal dscr = BigDecimal.ZERO;
		if(getInterestRate() != null && getRequestedTerm() != null && getLoanAmount() != null){
			double principal = getLoanAmount().doubleValue();
			double yearlyRate = getInterestRate().doubleValue();
			yearlyRate = yearlyRate/12;
			yearlyRate = yearlyRate/100;
			double termYears = getRequestedTerm().doubleValue();
			double pValue = principal / ((1- Math.pow(1 + yearlyRate, -termYears))/ yearlyRate)*12;
			BigDecimal debtService = new BigDecimal(pValue);
			debtService = debtService.divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP);
			dscr = debtService;
		}
		
		return dscr.equals( BigDecimal.ZERO)? null : dscr;
	}
	public BigDecimal getDebtYield(){
		BigDecimal debtYield = BigDecimal.ZERO;
		if(getLoanAmount() != null && getLoanAmount().compareTo(BigDecimal.ZERO) > 0){
			debtYield = getTotalNoi().divide(getLoanAmount(),4, RoundingMode.HALF_UP);
			debtYield = debtYield.multiply(new BigDecimal(100));
			debtYield = debtYield.divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP);
		}
		
		return debtYield.equals( BigDecimal.ZERO)? null : debtYield;
	}
	public String getInterestRateDer(){
		String interestRate = "";
		if(getInterestRate() != null){
			interestRate = getInterestRate().divide(BigDecimal.ONE,2, RoundingMode.HALF_UP).toString()+"%";
		}
		return interestRate;
	}
	 public BigDecimal getPreQualValue(){
		BigDecimal appraisalValue = BigDecimal.ZERO;
		if(getCapRate() != null && getCapRate().compareTo(BigDecimal.ZERO) > 0 && getTotalNoi() != null && getTotalNoi().compareTo(BigDecimal.ZERO) > 0){
			appraisalValue = getTotalNoi().divide(getCapRate(),2, RoundingMode.HALF_UP);
			appraisalValue = appraisalValue.multiply(new BigDecimal(100));
		}
		return appraisalValue.equals( BigDecimal.ZERO)? null : appraisalValue;
	}	
	 //Dervied Field values with "%" in the end
	 public String getLoanToValueDer(){
			String loanToValue = "";
			if(getLoanToValue() != null){
				loanToValue = getLoanToValue().divide(BigDecimal.ONE,2, RoundingMode.HALF_UP).toString()+"%";
			}
			return loanToValue;
	 }
	 public String getDscrDer(){
			String dscr = "";
			if(getDscr() != null){
				dscr = getDscr().divide(BigDecimal.ONE,2, RoundingMode.HALF_UP).toString();
			}
			return dscr;
	 }
	 public String getDebtYieldDer(){
			String debtYield = "";
			if(getDebtYield() != null){
				debtYield = getDebtYield().divide(BigDecimal.ONE,2, RoundingMode.HALF_UP).toString()+"%";
			}
			return debtYield;
	 }
}
