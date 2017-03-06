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
public class OperatingIncome extends BaseDataObject {
	 //~ Static fields/initializers -------------------------------------------------------------------------------------

    /**  */
    private static final long serialVersionUID = 1L;
    //Method to get whether Operating Income is default or it will be called from Financials
    public Boolean isRevenueCalledOperating(){
    	boolean operatingRev = true;
    	if(getRevenueLegend() != null && ("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(getRevenueLegend().getKey()) 
    	    	|| "REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(getRevenueLegend().getKey())
    	   		|| "REVENUE_LEGEND_TYPE_CONCESSIONS".equals(getRevenueLegend().getKey()) 
    	    	|| "REVENUE_LEGEND_TYPE_BAD_DEBTS".equals(getRevenueLegend().getKey()))){
    	   		operatingRev = false;
    	   	}
       	return operatingRev;
        }
    //Method to get one by one Default Operating income 
    public Boolean isGPR(){
    	boolean operatingRev = false;
    	if(getRevenueLegend() != null && ("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(getRevenueLegend().getKey()))){
    	   		operatingRev = true;
    	   	}
       	return operatingRev;
    }
    public Boolean isVacancyLoss(){
    	boolean operatingRev = false;
    	if(getRevenueLegend() != null && ("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(getRevenueLegend().getKey()))){
    	   		operatingRev = true;
    	   	}
       	return operatingRev;
    }
    public Boolean isConcession(){
    	boolean operatingRev = false;
    	if(getRevenueLegend() != null && ("REVENUE_LEGEND_TYPE_CONCESSIONS".equals(getRevenueLegend().getKey()))){
    	   		operatingRev = true;
    	   	}
       	return operatingRev;
    }
    public Boolean isBadDebt(){
    	boolean operatingRev = false;
    	if(getRevenueLegend() != null && ("REVENUE_LEGEND_TYPE_BAD_DEBTS".equals(getRevenueLegend().getKey()))){
    	   		operatingRev = true;
    	   	}
       	return operatingRev;
    }
    //Calculate Forcast for 15 years we used forcast1-forcast15
    //year 1
    public BigDecimal getForcast1(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	//forcast for all other incomes expect GPR,VC,BD,Concession
		if(getTotalAmount() != null && isRevenueCalledOperating() && getForcastedFinancial() != null && getForcastedFinancial().getAnnualIncIncr() != null){
			forcast = getTotalAmount().multiply(getForcastedFinancial().getAnnualIncIncr());
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getTotalAmount());
		}else if(getTotalAmount() != null && isGPR() && getForcastedFinancial() != null && getForcastedFinancial().getIncreaseRent() != null){
			forcast = getTotalAmount().multiply(getForcastedFinancial().getIncreaseRent()); //GPR Calculation
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getTotalAmount());
		}else if(getTotalAmount() != null && isVacancyLoss() && getForcastedFinancial() != null && getForcastedFinancial().getEstVancanyRate() != null){
			forcast = getTotalAmount().multiply(getForcastedFinancial().getEstVancanyRate()); //VC calculation
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getTotalAmount());
		}else if(isBadDebt() && getForcastedFinancial() != null && getForcastedFinancial().getBadDebt() != null){
			for(OperatingIncome income :getForcastedFinancial().getOperatingIncomes()){ //Bad Debts
				if("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(income.getRevenueLegend().getKey()) && income.getForcast1() != null){
					forcast = income.getForcast1();
				}if("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(income.getRevenueLegend().getKey()) && income.getForcast1() !=null){
					forcast = forcast.subtract(income.getForcast1());
					forcast = forcast.multiply(getForcastedFinancial().getBadDebt());
					forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
				}
			}
		}else if(isConcession() && getForcastedFinancial() != null && getForcastedFinancial().getConcession() != null){
			//Now Calculate Concession
			for(OperatingIncome income :getForcastedFinancial().getOperatingIncomes()){
				if("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(income.getRevenueLegend().getKey()) && income.getForcast1() != null){
					forcast = income.getForcast1();
				}if("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(income.getRevenueLegend().getKey()) && income.getForcast1() != null){
					forcast = forcast.subtract(income.getForcast1());
				}if("REVENUE_LEGEND_TYPE_BAD_DEBTS".equals(income.getRevenueLegend().getKey()) && income.getForcast1() != null){
					forcast = forcast.subtract(income.getForcast1());
					forcast=forcast.multiply(getForcastedFinancial().getConcession());
					forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
				}
			}
		}
		return forcast.equals(BigDecimal.ZERO)? null : forcast;
    	
    }
    //year 2
    public BigDecimal getForcast2(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	//forcast for all other incomes expect GPR,VC,BD,Concession
		if(getForcast1() != null && isRevenueCalledOperating() && getForcastedFinancial() != null && getForcastedFinancial().getAnnualIncIncr() != null){
			forcast = getForcast1().multiply(getForcastedFinancial().getAnnualIncIncr());
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast1());
		}else if(getForcast1() != null && isGPR() && getForcastedFinancial() != null && getForcastedFinancial().getIncreaseRent() != null){
			forcast = getForcast1().multiply(getForcastedFinancial().getIncreaseRent()); //GPR Calculation
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast1());
		}else if(getForcast1() != null && isVacancyLoss() && getForcastedFinancial() != null && getForcastedFinancial().getEstVancanyRate() != null){
			forcast = getForcast1().multiply(getForcastedFinancial().getEstVancanyRate()); //VC calculation
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast1());
		}else if(isBadDebt() && getForcastedFinancial() != null && getForcastedFinancial().getBadDebt() != null){
			for(OperatingIncome income :getForcastedFinancial().getOperatingIncomes()){
				if("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(income.getRevenueLegend().getKey()) && income.getForcast2() != null){
					forcast = income.getForcast2(); //Bad Debts
				}if("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(income.getRevenueLegend().getKey()) && income.getForcast2() !=null){
					forcast = forcast.subtract(income.getForcast2());
					forcast = forcast.multiply(getForcastedFinancial().getBadDebt());
					forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
				}
			}
		}else if(isConcession() && getForcastedFinancial() != null && getForcastedFinancial().getConcession() != null){
			//Concession Calculation 
			for(OperatingIncome income :getForcastedFinancial().getOperatingIncomes()){
				if("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(income.getRevenueLegend().getKey()) && income.getForcast2() != null){
					forcast = income.getForcast2();
				}if("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(income.getRevenueLegend().getKey()) && income.getForcast2() != null){
					forcast = forcast.subtract(income.getForcast2());
				}if("REVENUE_LEGEND_TYPE_BAD_DEBTS".equals(income.getRevenueLegend().getKey()) && income.getForcast2() != null){
					forcast = forcast.subtract(income.getForcast2());
					forcast=forcast.multiply(getForcastedFinancial().getConcession());
					forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
				}
			}
		}
		return forcast.equals(BigDecimal.ZERO)? null : forcast;
    	
    }
   //Year 3
    public BigDecimal getForcast3(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	//forcast for all other incomes expect GPR,VC,BD,Concession
		if(getForcast2() != null && isRevenueCalledOperating() && getForcastedFinancial() != null && getForcastedFinancial().getAnnualIncIncr() != null){
			forcast = getForcast2().multiply(getForcastedFinancial().getAnnualIncIncr());
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast2());
		}else if(getForcast2() != null && isGPR() && getForcastedFinancial() != null && getForcastedFinancial().getIncreaseRent() != null){
			forcast = getForcast2().multiply(getForcastedFinancial().getIncreaseRent()); //GPR Calculation
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast2());
		}else if(getForcast2() != null && isVacancyLoss() && getForcastedFinancial() != null && getForcastedFinancial().getEstVancanyRate() != null){
			forcast = getForcast2().multiply(getForcastedFinancial().getEstVancanyRate()); //VC calculation
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast2());
		}else if(isBadDebt() && getForcastedFinancial() != null && getForcastedFinancial().getBadDebt() != null){
			for(OperatingIncome income :getForcastedFinancial().getOperatingIncomes()){
				if("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(income.getRevenueLegend().getKey()) && income.getForcast3() != null){
					forcast = income.getForcast3(); //Bad Debts
				}if("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(income.getRevenueLegend().getKey()) && income.getForcast3() !=null){
					forcast = forcast.subtract(income.getForcast3());
					forcast = forcast.multiply(getForcastedFinancial().getBadDebt());
					forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
				}
			}
		}else if(isConcession() && getForcastedFinancial() != null && getForcastedFinancial().getConcession() != null){
			//Now Calculate Concession
			for(OperatingIncome income :getForcastedFinancial().getOperatingIncomes()){
				if("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(income.getRevenueLegend().getKey()) && income.getForcast3() != null){
					forcast = income.getForcast3();
				}if("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(income.getRevenueLegend().getKey()) && income.getForcast3() != null){
					forcast = forcast.subtract(income.getForcast3());
				}if("REVENUE_LEGEND_TYPE_BAD_DEBTS".equals(income.getRevenueLegend().getKey()) && income.getForcast3() != null){
					forcast = forcast.subtract(income.getForcast3());
					forcast=forcast.multiply(getForcastedFinancial().getConcession());
					forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
				}
			}
		}
		return forcast.equals(BigDecimal.ZERO)? null : forcast;
    	
    }
    //year 4
    public BigDecimal getForcast4(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	//forcast for all other incomes expect GPR,VC,BD,Concession
		if(getForcast3() != null && isRevenueCalledOperating() && getForcastedFinancial() != null && getForcastedFinancial().getAnnualIncIncr() != null){
			forcast = getForcast3().multiply(getForcastedFinancial().getAnnualIncIncr());
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast3());
		}else if(getForcast3() != null && isGPR() && getForcastedFinancial() != null && getForcastedFinancial().getIncreaseRent() != null){
			forcast = getForcast3().multiply(getForcastedFinancial().getIncreaseRent()); //GPR Calculation
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast3());
		}else if(getForcast3() != null && isVacancyLoss() && getForcastedFinancial() != null && getForcastedFinancial().getEstVancanyRate() != null){
			forcast = getForcast3().multiply(getForcastedFinancial().getEstVancanyRate()); //VC calculation
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast3());
		}else if(isBadDebt() && getForcastedFinancial() != null && getForcastedFinancial().getBadDebt() != null){
			for(OperatingIncome income :getForcastedFinancial().getOperatingIncomes()){
				if("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(income.getRevenueLegend().getKey()) && income.getForcast4() != null){
					forcast = income.getForcast4(); //Bad Debts
				}if("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(income.getRevenueLegend().getKey()) && income.getForcast4() !=null){
					forcast = forcast.subtract(income.getForcast4());
					forcast = forcast.multiply(getForcastedFinancial().getBadDebt());
					forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
				}
			}
		}else if(isConcession() && getForcastedFinancial() != null && getForcastedFinancial().getConcession() != null){
			//Now Calculate Concession
			for(OperatingIncome income :getForcastedFinancial().getOperatingIncomes()){
				if("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(income.getRevenueLegend().getKey()) && income.getForcast4() != null){
					forcast = income.getForcast4();
				}if("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(income.getRevenueLegend().getKey()) && income.getForcast4() != null){
					forcast = forcast.subtract(income.getForcast4());
				}if("REVENUE_LEGEND_TYPE_BAD_DEBTS".equals(income.getRevenueLegend().getKey()) && income.getForcast4() != null){
					forcast = forcast.subtract(income.getForcast4());
					forcast=forcast.multiply(getForcastedFinancial().getConcession());
					forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
				}
			}
		}
		return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    //year 5
    public BigDecimal getForcast5(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	//forcast for all other incomes expect GPR,VC,BD,Concession
		if(getForcast4() != null && isRevenueCalledOperating() && getForcastedFinancial() != null && getForcastedFinancial().getAnnualIncIncr() != null){
			forcast = getForcast4().multiply(getForcastedFinancial().getAnnualIncIncr());
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast4());
		}else if(getForcast4() != null && isGPR() && getForcastedFinancial() != null && getForcastedFinancial().getIncreaseRent() != null){
			forcast = getForcast4().multiply(getForcastedFinancial().getIncreaseRent()); //GPR Calculation
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast4());
		}else if(getForcast4() != null && isVacancyLoss() && getForcastedFinancial() != null && getForcastedFinancial().getEstVancanyRate() != null){
			forcast = getForcast4().multiply(getForcastedFinancial().getEstVancanyRate()); //VC calculation
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast4());
		}else if(isBadDebt() && getForcastedFinancial() != null && getForcastedFinancial().getBadDebt() != null){
			for(OperatingIncome income :getForcastedFinancial().getOperatingIncomes()){
				if("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(income.getRevenueLegend().getKey()) && income.getForcast5() != null){
					forcast = income.getForcast5();  //Bad Debts
				}if("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(income.getRevenueLegend().getKey()) && income.getForcast5() !=null){
					forcast = forcast.subtract(income.getForcast5());
					forcast = forcast.multiply(getForcastedFinancial().getBadDebt());
					forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
				}
			}
		}else if(isConcession() && getForcastedFinancial() != null && getForcastedFinancial().getConcession() != null){
			//Now Calculate Concession
			for(OperatingIncome income :getForcastedFinancial().getOperatingIncomes()){
				if("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(income.getRevenueLegend().getKey()) && income.getForcast5() != null){
					forcast = income.getForcast5();
				}if("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(income.getRevenueLegend().getKey()) && income.getForcast5() != null){
					forcast = forcast.subtract(income.getForcast5());
				}if("REVENUE_LEGEND_TYPE_BAD_DEBTS".equals(income.getRevenueLegend().getKey()) && income.getForcast5() != null){
					forcast = forcast.subtract(income.getForcast5());
					forcast=forcast.multiply(getForcastedFinancial().getConcession());
					forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
				}
			}
		}
		return forcast.equals(BigDecimal.ZERO)? null : forcast;		
    	
    }
    //year 6
    public BigDecimal getForcast6(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	//forcast for all other incomes expect GPR,VC,BD,Concession
		if(getForcast5() != null && isRevenueCalledOperating() && getForcastedFinancial() != null && getForcastedFinancial().getAnnualIncIncr() != null){
			forcast = getForcast5().multiply(getForcastedFinancial().getAnnualIncIncr());
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast5());
		}else if(getForcast5() != null && isGPR() && getForcastedFinancial() != null && getForcastedFinancial().getIncreaseRent() != null){
			forcast = getForcast5().multiply(getForcastedFinancial().getIncreaseRent()); //GPR Calculation
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast5());
		}else if(getForcast5() != null && isVacancyLoss() && getForcastedFinancial() != null && getForcastedFinancial().getEstVancanyRate() != null){
			forcast = getForcast5().multiply(getForcastedFinancial().getEstVancanyRate()); //VC calculation
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast5());
		}else if(isBadDebt() && getForcastedFinancial() != null && getForcastedFinancial().getBadDebt() != null){
			for(OperatingIncome income :getForcastedFinancial().getOperatingIncomes()){
				if("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(income.getRevenueLegend().getKey()) && income.getForcast6() != null){
					forcast = income.getForcast6();  //Bad Debts
				}if("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(income.getRevenueLegend().getKey()) && income.getForcast6() !=null){
					forcast = forcast.subtract(income.getForcast6());
					forcast = forcast.multiply(getForcastedFinancial().getBadDebt());
					forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
				}
			}
		}else if(isConcession() && getForcastedFinancial() != null && getForcastedFinancial().getConcession() != null){
			//Now Calculate Concession
			for(OperatingIncome income :getForcastedFinancial().getOperatingIncomes()){
				if("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(income.getRevenueLegend().getKey()) && income.getForcast6() != null){
					forcast = income.getForcast6();
				}if("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(income.getRevenueLegend().getKey()) && income.getForcast6() != null){
					forcast = forcast.subtract(income.getForcast6());
				}if("REVENUE_LEGEND_TYPE_BAD_DEBTS".equals(income.getRevenueLegend().getKey()) && income.getForcast6() != null){
					forcast = forcast.subtract(income.getForcast6());
					forcast=forcast.multiply(getForcastedFinancial().getConcession());
					forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
				}
			}
		}
		return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
    //year 7
    public BigDecimal getForcast7(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	//forcast for all other incomes expect GPR,VC,BD,Concession
		if(getForcast6() != null && isRevenueCalledOperating() && getForcastedFinancial() != null && getForcastedFinancial().getAnnualIncIncr() != null){
			forcast = getForcast6().multiply(getForcastedFinancial().getAnnualIncIncr());
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast6());
		}else if(getForcast6() != null && isGPR() && getForcastedFinancial() != null && getForcastedFinancial().getIncreaseRent() != null){
			forcast = getForcast6().multiply(getForcastedFinancial().getIncreaseRent()); //GPR Calculation
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast6());
		}else if(getForcast6() != null && isVacancyLoss() && getForcastedFinancial() != null && getForcastedFinancial().getEstVancanyRate() != null){
			forcast = getForcast6().multiply(getForcastedFinancial().getEstVancanyRate());  //VC calculation
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast6());
		}else if(isBadDebt() && getForcastedFinancial() != null && getForcastedFinancial().getBadDebt() != null){
			for(OperatingIncome income :getForcastedFinancial().getOperatingIncomes()){
				if("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(income.getRevenueLegend().getKey()) && income.getForcast7() != null){
					forcast = income.getForcast7();  //Bad Debts
				} if("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(income.getRevenueLegend().getKey()) && income.getForcast7() !=null){
					forcast = forcast.subtract(income.getForcast7());
					forcast = forcast.multiply(getForcastedFinancial().getBadDebt());
					forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
				}
			}
		}else if(isConcession() && getForcastedFinancial() != null && getForcastedFinancial().getConcession() != null){
			//Now Calculate Concession
			for(OperatingIncome income :getForcastedFinancial().getOperatingIncomes()){
				if("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(income.getRevenueLegend().getKey()) && income.getForcast7() != null){
					forcast = income.getForcast7();
				} if("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(income.getRevenueLegend().getKey()) && income.getForcast7() != null){
					forcast = forcast.subtract(income.getForcast7());
				} if("REVENUE_LEGEND_TYPE_BAD_DEBTS".equals(income.getRevenueLegend().getKey()) && income.getForcast7() != null){
					forcast = forcast.subtract(income.getForcast7());
					forcast=forcast.multiply(getForcastedFinancial().getConcession());
					forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
				}
			}
		}
		return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
   //year 8
    public BigDecimal getForcast8(){
    	BigDecimal forcast = BigDecimal.ZERO;
    	//forcast for all other incomes expect GPR,VC,BD,Concession
		if(getForcast7() != null && isRevenueCalledOperating() && getForcastedFinancial() != null && getForcastedFinancial().getAnnualIncIncr() != null){
			forcast = getForcast7().multiply(getForcastedFinancial().getAnnualIncIncr());
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast7());
		}else if(getForcast7() != null && isGPR() && getForcastedFinancial() != null && getForcastedFinancial().getIncreaseRent() != null){
			forcast = getForcast7().multiply(getForcastedFinancial().getIncreaseRent()); //GPR Calculation
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast7());
		}else if(getForcast7() != null && isVacancyLoss() && getForcastedFinancial() != null && getForcastedFinancial().getEstVancanyRate() != null){
			forcast = getForcast7().multiply(getForcastedFinancial().getEstVancanyRate());  //VC calculation
			forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
			forcast =  forcast.add(getForcast7());
		}else if(isBadDebt() && getForcastedFinancial() != null && getForcastedFinancial().getBadDebt() != null){
			for(OperatingIncome income :getForcastedFinancial().getOperatingIncomes()){
				if("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(income.getRevenueLegend().getKey()) && income.getForcast8() != null){
					forcast = income.getForcast8();  //Bad Debts
				} if("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(income.getRevenueLegend().getKey()) && income.getForcast8() !=null){
					forcast = forcast.subtract(income.getForcast8());
					forcast = forcast.multiply(getForcastedFinancial().getBadDebt());
					forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
				}
			}
		}else if(isConcession() && getForcastedFinancial() != null && getForcastedFinancial().getConcession() != null){
			//Now Calculate Concession
			for(OperatingIncome income :getForcastedFinancial().getOperatingIncomes()){
				if("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(income.getRevenueLegend().getKey()) && income.getForcast8() != null){
					forcast = income.getForcast8();
				} if("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(income.getRevenueLegend().getKey()) && income.getForcast8() != null){
					forcast = forcast.subtract(income.getForcast8());
				} if("REVENUE_LEGEND_TYPE_BAD_DEBTS".equals(income.getRevenueLegend().getKey()) && income.getForcast8() != null){
					forcast = forcast.subtract(income.getForcast8());
					forcast=forcast.multiply(getForcastedFinancial().getConcession());
					forcast = forcast.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
				}
			}
		}
		return forcast.equals(BigDecimal.ZERO)? null : forcast;
    }
  }
