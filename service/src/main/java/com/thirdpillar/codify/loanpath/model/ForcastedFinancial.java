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
import java.util.ArrayList;
import java.util.List;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.util.StringUtils;
import com.thirdpillar.codify.loanpath.model.IncomeExpense;
import com.thirdpillar.codify.loanpath.model.Collateral;
import com.thirdpillar.foundation.service.LookupService;
import com.thirdpillar.codify.loanpath.constants.Constants;


/**
 * DOCUMENT ME!
 *
 * @author   Ankush.Bhardwaj
 * @version  1.0
 * @since    1.0
 */
public class ForcastedFinancial extends BaseDataObject {
	 //~ Static fields/initializers -------------------------------------------------------------------------------------

    /**  */
    private static final long serialVersionUID = 1L;
    
    //list of base years
	public List<IncomeExpense> getBaseYearList(){
		List<IncomeExpense> filteredExpense = new ArrayList<IncomeExpense>();
		if(getCollateral()!= null && getCollateral().getCashFlows() != null && getCollateral().getCashFlows().getFinancial() != null){
			for(IncomeExpense incomeExpense : getCollateral().getCashFlows().getFinancial().getIncomeExpenses()){
				if(incomeExpense.getDetails() != null && ("DETAILS_TYPE_YEAR_ONE".equalsIgnoreCase(incomeExpense.getDetails().getKey()) || "DETAILS_TYPE_APPRAISAL".equalsIgnoreCase(incomeExpense.getDetails().getKey()))){
					filteredExpense.add(incomeExpense);
				}
			}
		}
		return filteredExpense;
	}
	//default Revenues to be added everytime
	public List<AttributeChoice> getDefaultOperatingIncomeList(){
		List<AttributeChoice> listChoices = new ArrayList<AttributeChoice>();
		AttributeChoice attributeChoiceBadDebt = (AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "REVENUE_LEGEND_TYPE_BAD_DEBTS");
		AttributeChoice attributeChoiceConcession = (AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "REVENUE_LEGEND_TYPE_CONCESSIONS");
		AttributeChoice attributeChoiceGPR = (AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT");
		AttributeChoice attributeChoiceVancanyLoss = (AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "REVENUE_LEGEND_TYPE_VACANCY_LOSS");
		listChoices.add(attributeChoiceGPR);
		listChoices.add(attributeChoiceVancanyLoss);
		listChoices.add(attributeChoiceConcession);
		listChoices.add(attributeChoiceBadDebt);
		return listChoices;
	}
	//default expense to be added everytime
	public List<AttributeChoice> getDefaultOperatingExpenseList(){
		List<AttributeChoice> listChoices = new ArrayList<AttributeChoice>();
		AttributeChoice attributeChoice = (AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "EXPENSE_LEGEND_TYPE_MANAGEMENT_FEES");
		listChoices.add(attributeChoice);
		return listChoices;
	}
	//default ratio and schedule to be added everytime
	public List<AttributeChoice> getDefaultRatioSchedule(){
		List<AttributeChoice> listChoices = new ArrayList<AttributeChoice>();
		Attribute attribute = (Attribute) LookupService.getResult("Attribute.byKey", "key", "RATIO_SCHEDULE_TYPE");
		for(AttributeChoice attributeChoice : attribute.getAttributeChoices()){
			listChoices.add(attributeChoice);
		}
		return listChoices;
	}
	//default ProfitMeasure to be added everytime
		public List<AttributeChoice> getDefaultProfitMeasure(){
			List<AttributeChoice> listChoices = new ArrayList<AttributeChoice>();
			Attribute attribute = (Attribute) LookupService.getResult("Attribute.byKey", "key", "PROFIT_MEASURES_TYPE");
			for(AttributeChoice attributeChoice : attribute.getAttributeChoices()){
				listChoices.add(attributeChoice);
			}
			return listChoices;
		}
		//default FinancialRatio to be added everytime
		public List<AttributeChoice> getDefaultFinancialRatio(){
			List<AttributeChoice> listChoices = new ArrayList<AttributeChoice>();
			Attribute attribute = (Attribute) LookupService.getResult("Attribute.byKey", "key", "FINANCIAL_RATIO_TYPE");
			for(AttributeChoice attributeChoice : attribute.getAttributeChoices()){
				listChoices.add(attributeChoice);
			}
			return listChoices;
		}
		//default IncMultiMethod to be added everytime
		public List<AttributeChoice> getDefaultIncMultiMethod(){
			List<AttributeChoice> listChoices = new ArrayList<AttributeChoice>();
			Attribute attribute = (Attribute) LookupService.getResult("Attribute.byKey", "key", "INCOME_MULTIPLIER_METHOD_TYPE");
			for(AttributeChoice attributeChoice : attribute.getAttributeChoices()){
				listChoices.add(attributeChoice);
			}
			return listChoices;
		}
		//default IncomeMultiplier to be added everytime
		public List<AttributeChoice> getDefaultIncomeMultiplier(){
			List<AttributeChoice> listChoices = new ArrayList<AttributeChoice>();
			Attribute attribute = (Attribute) LookupService.getResult("Attribute.byKey", "key", "INCOME_MULTIPLIER_TYPE");
			for(AttributeChoice attributeChoice : attribute.getAttributeChoices()){
				listChoices.add(attributeChoice);
			}
			return listChoices;
		}
	//calculation of GPR
	public BigDecimal getGPR(){
    	BigDecimal total = BigDecimal.ZERO;
    	if(getCollateral() != null && getCollateral().getCashFlows() != null && getCollateral().getCashFlows().getRentRoll() != null && getIncreaseRent() != null){
    		total = getCollateral().getCashFlows().getRentRoll().getTotalRent();
    		BigDecimal increase = total.multiply(getIncreaseRent());
    		increase = increase.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
    		total = total.add(increase);
    		
    	}
		return total.equals(BigDecimal.ZERO)? null : total;
    	
    }
	//calculation of VC
	public BigDecimal getVacancyLoss(){
		BigDecimal total = BigDecimal.ZERO;
		if(getCollateral().getAssessmentCollateral() != null && getCollateral().getAssessmentCollateral().getOccupancy() != null && getGPR() != null){
			BigDecimal occupancy = getCollateral().getAssessmentCollateral().getOccupancy();
			occupancy = new BigDecimal(100).subtract(occupancy);
			total =  getGPR().multiply(occupancy);
			total = total.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	//calculation of BD
	public BigDecimal getBadDebts(){
		BigDecimal total = BigDecimal.ZERO;
		if(getGPR() != null && getVacancyLoss() != null && getBadDebt() != null ){
			total = getGPR().subtract(getVacancyLoss());
			total=total.multiply(getBadDebt());
			total = total.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	//calculation of Concession
	public BigDecimal getConcessions(){
		BigDecimal total = BigDecimal.ZERO;
		if(getGPR() != null && getVacancyLoss() != null && getConcession() != null && getBadDebts() != null){
			total = getGPR().subtract(getVacancyLoss());
			total=total.subtract(getBadDebts());
			total = total.multiply(getConcession());
			total = total.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	//calculation of Mgmt fee
	public BigDecimal getMgmtFees(){
		BigDecimal total = BigDecimal.ZERO;
		if(getMgmtFee() != null && getFooterTotalAmt() != null  ){
			total= getFooterTotalAmt().multiply(getMgmtFee());
			total = total.divide(new BigDecimal(100),2, RoundingMode.HALF_UP);
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	//Calculation default Income
	public BigDecimal getTotalAmountDefaultOperatingIncome(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingIncome operatingIncome : getOperatingIncomes()){
			if(getGPR() != null){
				if("REVENUE_LEGEND_TYPE_GROSS_POTENTIAL_RENT".equals(operatingIncome.getRevenueLegend().getKey())){
					total = getGPR();
				}else if("REVENUE_LEGEND_TYPE_BAD_DEBTS".equals(operatingIncome.getRevenueLegend().getKey())){
					total = getBadDebts();
				}else if("REVENUE_LEGEND_TYPE_CONCESSIONS".equals(operatingIncome.getRevenueLegend().getKey())){
					total = getConcessions();
				}else if("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(operatingIncome.getRevenueLegend().getKey())){
					total = getVacancyLoss();
				}
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	//Footers for Income
	public String getFooterName(){
		return "";
	}
	public BigDecimal getFooterTotalAmt(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingIncome operatingIncome : getOperatingIncomes()){
			if((operatingIncome.isVacancyLoss() || operatingIncome.isBadDebt() || operatingIncome.isConcession()) && operatingIncome.getTotalAmount() != null){
				total = total.subtract(operatingIncome.getTotalAmount());
			}else if(operatingIncome.getTotalAmount() != null){
					total = total.add(operatingIncome.getTotalAmount());
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	public BigDecimal getFooterForcast1(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingIncome operatingIncome : getOperatingIncomes()){
			if((operatingIncome.isVacancyLoss() || operatingIncome.isBadDebt() || operatingIncome.isConcession()) && operatingIncome.getForcast1() != null){
				total = total.subtract(operatingIncome.getForcast1());
			}else if(operatingIncome.getForcast1() != null){
				total = total.add(operatingIncome.getForcast1());
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	public BigDecimal getFooterForcast2(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingIncome operatingIncome : getOperatingIncomes()){
			if((operatingIncome.isVacancyLoss() || operatingIncome.isBadDebt() || operatingIncome.isConcession()) && operatingIncome.getForcast2() != null){
				total = total.subtract(operatingIncome.getForcast2());
			}else if(operatingIncome.getForcast2() != null){
				total = total.add(operatingIncome.getForcast2());
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	public BigDecimal getFooterForcast3(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingIncome operatingIncome : getOperatingIncomes()){
			if((operatingIncome.isVacancyLoss() || operatingIncome.isBadDebt() || operatingIncome.isConcession()) && operatingIncome.getForcast3() != null){
				total = total.subtract(operatingIncome.getForcast3());
			}else if(operatingIncome.getForcast3() != null){
				total = total.add(operatingIncome.getForcast3());
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	public BigDecimal getFooterForcast4(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingIncome operatingIncome : getOperatingIncomes()){
			if((operatingIncome.isVacancyLoss() || operatingIncome.isBadDebt() || operatingIncome.isConcession()) && operatingIncome.getForcast4() != null){
				total = total.subtract(operatingIncome.getForcast4());
			}else if(operatingIncome.getForcast4() != null){
				total = total.add(operatingIncome.getForcast4());
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	public BigDecimal getFooterForcast5(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingIncome operatingIncome : getOperatingIncomes()){
			if((operatingIncome.isVacancyLoss() || operatingIncome.isBadDebt() || operatingIncome.isConcession()) && operatingIncome.getForcast5() != null){
				total = total.subtract(operatingIncome.getForcast5());
			}else if(operatingIncome.getForcast5() != null){
				total = total.add(operatingIncome.getForcast5());
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	public BigDecimal getFooterForcast6(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingIncome operatingIncome : getOperatingIncomes()){
			if((operatingIncome.isVacancyLoss() || operatingIncome.isBadDebt() || operatingIncome.isConcession()) && operatingIncome.getForcast6() != null){
				total = total.subtract(operatingIncome.getForcast6());
			}else if(operatingIncome.getForcast6() != null){
				total = total.add(operatingIncome.getForcast6());
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	public BigDecimal getFooterForcast7(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingIncome operatingIncome : getOperatingIncomes()){
			if((operatingIncome.isVacancyLoss() || operatingIncome.isBadDebt() || operatingIncome.isConcession()) && operatingIncome.getForcast7() != null){
				total = total.subtract(operatingIncome.getForcast7());
			}else if(operatingIncome.getForcast7() != null){
				total = total.add(operatingIncome.getForcast7());
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	public BigDecimal getFooterForcast8(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingIncome operatingIncome : getOperatingIncomes()){
			if((operatingIncome.isVacancyLoss() || operatingIncome.isBadDebt() || operatingIncome.isConcession()) && operatingIncome.getForcast8() != null){
				total = total.subtract(operatingIncome.getForcast8());
			}else if(operatingIncome.getForcast8() != null){
				total = total.add(operatingIncome.getForcast8());
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	
	//footer for Expense
	public String getFooterExpenseName(){
		return "";
	}
	public BigDecimal getFooterTotalAmtExpense(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingExpense operatingExpense : getOperatingExpenses()){
			if(operatingExpense.getTotalAmountDer() != null){
				total = total.add(operatingExpense.getTotalAmountDer());
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	public BigDecimal getFooterExpenseForcast1(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingExpense operatingExpense : getOperatingExpenses()){
			if(operatingExpense.getForcast1() != null){
				total = total.add(operatingExpense.getForcast1());
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	public BigDecimal getFooterExpenseForcast2(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingExpense operatingExpense : getOperatingExpenses()){
			if(operatingExpense.getForcast2() != null){
				total = total.add(operatingExpense.getForcast2());
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	public BigDecimal getFooterExpenseForcast3(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingExpense operatingExpense : getOperatingExpenses()){
			if(operatingExpense.getForcast3() != null){
				total = total.add(operatingExpense.getForcast3());
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	public BigDecimal getFooterExpenseForcast4(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingExpense operatingExpense : getOperatingExpenses()){
			if(operatingExpense.getForcast4() != null){
				total = total.add(operatingExpense.getForcast4());
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	public BigDecimal getFooterExpenseForcast5(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingExpense operatingExpense : getOperatingExpenses()){
			if(operatingExpense.getForcast5() != null){
				total = total.add(operatingExpense.getForcast5());
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	public BigDecimal getFooterExpenseForcast6(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingExpense operatingExpense : getOperatingExpenses()){
			if(operatingExpense.getForcast6() != null){
				total = total.add(operatingExpense.getForcast6());
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	public BigDecimal getFooterExpenseForcast7(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingExpense operatingExpense : getOperatingExpenses()){
			if(operatingExpense.getForcast7() != null){
				total = total.add(operatingExpense.getForcast7());
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	public BigDecimal getFooterExpenseForcast8(){
		BigDecimal total = BigDecimal.ZERO;
		for(OperatingExpense operatingExpense : getOperatingExpenses()){
			if(operatingExpense.getForcast8() != null){
				total = total.add(operatingExpense.getForcast8());
			}
		}
		return total.equals(BigDecimal.ZERO)? null : total;
	}
	
}
