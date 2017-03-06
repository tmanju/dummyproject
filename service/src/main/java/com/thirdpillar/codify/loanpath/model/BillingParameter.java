package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;

public class BillingParameter {
	public BigDecimal getRoutingNumberDer(){
		if(getAccountBorrower() != null){
			if(!getAccountBorrower().getCustomer().getBankTrades().isEmpty()){
				return new BigDecimal(getAccountBorrower().getCustomer().getBankTrades().get(0).getRoutingNumber());
			}
		}
    	return null;
    }
	public BigDecimal getBankAccountNumberDer(){
		if(getAccountBorrower() != null){
			if(!getAccountBorrower().getCustomer().getBankTrades().isEmpty()){
				return new BigDecimal(getAccountBorrower().getCustomer().getBankTrades().get(0).getBankAccountNumber());
			}
			
		}
    	return null;
    }
}
