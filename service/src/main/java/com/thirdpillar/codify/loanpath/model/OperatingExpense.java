/*
 * Copyright (c) 2005-2015 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.thirdpillar.foundation.model.BaseDataObject;


/**
 * DOCUMENT ME!
 *
 * @author   Ankush.Bhardwaj
 * @version  1.0
 * @since    1.0
 */
public class OperatingExpense extends BaseDataObject {
	 //~ Static fields/initializers -------------------------------------------------------------------------------------

    /**  */
    private static final long serialVersionUID = 1L;
    //Mgmt Fee or not
    public Boolean isMgmtFee(){
    	boolean operatingRev = false;
    	if(getExpenseLegend() != null && ("EXPENSE_LEGEND_TYPE_MANAGEMENT_FEES".equals(getExpenseLegend().getKey()))){
    	   	operatingRev = true;
    	}
       	return operatingRev;
    }
    //GrowthBasis list and value
    public int getGrowthBasis(){
    	int basis = 0;
    	if(getForcastedFinancial() != null && getForcastedFinancial().getBasisExpenseGwt() != null){
    		if("BASIS_EXPENSE_GROWTH_QUARTERLY".equals(getForcastedFinancial().getBasisExpenseGwt().getKey())){
    			basis = 4;
    		}if("BASIS_EXPENSE_GROWTH_SEMI_ANNUAL".equals(getForcastedFinancial().getBasisExpenseGwt().getKey())){
    			basis = 2;
    		}if("BASIS_EXPENSE_GROWTH_ANNUAL".equals(getForcastedFinancial().getBasisExpenseGwt().getKey())){
    			basis = 1;
    		}
    	}
    	return basis;
    }
    //Total Ammount Calculation
    public BigDecimal getTotalAmountDer(){
    	BigDecimal total = BigDecimal.ZERO;
    	if(isMgmtFee() && getForcastedFinancial() != null && getForcastedFinancial().getMgmtFees() != null){
    		total = getForcastedFinancial().getMgmtFees();
    	}else if(!isMgmtFee() && getTotalAmount() != null){
    		total = getTotalAmount();
    	}
    	return total.equals(BigDecimal.ZERO)? null : total;
    }
    //Method to get one by one Default Operating Expense 
    public BigDecimal getForcast1(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	if(getTotalAmountDer() != null && getForcastedFinancial() != null && getForcastedFinancial().getExpIncr() != null){
    		BigDecimal inc= BigDecimal.ZERO;
    		forcast = getTotalAmountDer();
    		int i;
    		//repeat cal based on GrowthBasis
    		for(i=0 ; i<getGrowthBasis();i++){
    			inc = forcast.multiply(getForcastedFinancial().getExpIncr());
    			inc = inc.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
    			forcast = inc.add(forcast);
    		}
    	}
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    public BigDecimal getForcast2(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	if(getForcast1() != null  && getForcastedFinancial() != null && getForcastedFinancial().getExpIncr() != null){
    		BigDecimal inc= BigDecimal.ZERO;
    		forcast = getForcast1();
    		int i;
    		for(i=0 ; i<getGrowthBasis();i++){
    			inc = forcast.multiply(getForcastedFinancial().getExpIncr());
    			inc = inc.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
    			forcast = inc.add(forcast);
    		}
    	}
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    public BigDecimal getForcast3(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	if(getForcast2() != null  && getForcastedFinancial() != null && getForcastedFinancial().getExpIncr() != null){
    		BigDecimal inc= BigDecimal.ZERO;
    		forcast = getForcast2();
    		int i;
    		for(i=0 ; i<getGrowthBasis();i++){
    			inc = forcast.multiply(getForcastedFinancial().getExpIncr());
    			inc = inc.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
    			forcast = inc.add(forcast);
    		}
    	}
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    public BigDecimal getForcast4(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	if(getForcast3() != null  && getForcastedFinancial() != null && getForcastedFinancial().getExpIncr() != null){
    		BigDecimal inc= BigDecimal.ZERO;
    		forcast = getForcast3();
    		int i;
    		for(i=0 ; i<getGrowthBasis();i++){
    			inc = forcast.multiply(getForcastedFinancial().getExpIncr());
    			inc = inc.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
    			forcast = inc.add(forcast);
    		}
    	}
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    public BigDecimal getForcast5(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	if(getForcast4() != null  && getForcastedFinancial() != null && getForcastedFinancial().getExpIncr() != null){
    		BigDecimal inc= BigDecimal.ZERO;
    		int i;
    		forcast = getForcast4();
    		for(i=0 ; i<getGrowthBasis();i++){
    			inc = forcast.multiply(getForcastedFinancial().getExpIncr());
    			inc = inc.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
    			forcast = inc.add(forcast);
    		}
    	}
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    public BigDecimal getForcast6(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	if(getForcast5() != null  && getForcastedFinancial() != null && getForcastedFinancial().getExpIncr() != null){
    		BigDecimal inc= BigDecimal.ZERO;
    		forcast = getForcast5();
    		int i;
    		for(i=0 ; i<getGrowthBasis();i++){
    			inc = forcast.multiply(getForcastedFinancial().getExpIncr());
    			inc = inc.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
    			forcast = inc.add(forcast);
    		}
    	}
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    public BigDecimal getForcast7(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	if(getForcast6() != null  && getForcastedFinancial() != null && getForcastedFinancial().getExpIncr() != null){
    		BigDecimal inc= BigDecimal.ZERO;
    		forcast = getForcast6();
    		int i;
    		for(i=0 ; i<getGrowthBasis();i++){
    			inc = forcast.multiply(getForcastedFinancial().getExpIncr());
    			inc = inc.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
    			forcast = inc.add(forcast);
    		}
    	}
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    public BigDecimal getForcast8(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	if(getForcast7() != null  && getForcastedFinancial() != null && getForcastedFinancial().getExpIncr() != null){
    		BigDecimal inc= BigDecimal.ZERO;
    		forcast = getForcast7();
    		int i;
    		for(i=0 ; i<getGrowthBasis();i++){
    			inc = forcast.multiply(getForcastedFinancial().getExpIncr());
    			inc = inc.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
    			forcast = inc.add(forcast);
    		}
    	}
    	return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    
}
