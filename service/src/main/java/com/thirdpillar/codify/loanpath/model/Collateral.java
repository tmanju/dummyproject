package com.thirdpillar.codify.loanpath.model;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.ContextHolder;

public class Collateral extends BaseDataObject{
	
	public CashFlows getCashFlowsDer(){
		if(getCashFlows() != null){
			return getCashFlows(); 
		}
		return null;
	}
	public Boolean isRevenueLineYearOne(){
		boolean isVisible=false;
		if(getCashFlows() != null  && getCashFlows().getFinancial() != null){
			for(FinancialAssessmentRevenue revenue : getCashFlows().getFinancial().getFinAssttRevDer()){
				if(revenue.getYearOne() != null){
					return true;
				}
			}
		}
		return isVisible;
	}
	public Boolean isRevenueLineYearAppraisal(){
		boolean isVisible=false;
		if(getCashFlows() != null  && getCashFlows().getFinancial() != null){
			for(FinancialAssessmentRevenue revenue : getCashFlows().getFinancial().getFinAssttRevDer()){
				if(revenue.getYearAppraisal() != null){
					return true;
				}
			}
		}
		return isVisible;
	}
	public Boolean isRevenueLineYearTwo(){
		boolean isVisible=false;
		if(getCashFlows() != null  && getCashFlows().getFinancial() != null){
			for(FinancialAssessmentRevenue revenue : getCashFlows().getFinancial().getFinAssttRevDer()){
				if(revenue.getYearTwo() != null){
					return true;
				}
			}
		}
		return isVisible;
	}
	public Boolean isRevenueLineYearThree(){
		boolean isVisible=false;
		if(getCashFlows() != null  && getCashFlows().getFinancial() != null){
			for(FinancialAssessmentRevenue revenue : getCashFlows().getFinancial().getFinAssttRevDer()){
				if(revenue.getYearThree() != null){
					return true;
				}
			}
		}
		return isVisible;
	}
	public Boolean isExpenseLineYearOne(){
		boolean isVisible=false;
		if(getCashFlows() != null  && getCashFlows().getFinancial() != null){
			for(FinancialAssessmentExpense expense : getCashFlows().getFinancial().getFinAssttExpDer()){
				if(expense.getYearOne() != null){
					return true;
				}
			}
		}
		return isVisible;
	}
	public Boolean isExpenseLineYearTwo(){
		boolean isVisible=false;
		if(getCashFlows() != null  && getCashFlows().getFinancial() != null){
			for(FinancialAssessmentExpense expense : getCashFlows().getFinancial().getFinAssttExpDer()){
				if(expense.getYearTwo() != null){
					return true;
				}
			}
		}
		return isVisible;
	}
	public Boolean isExpenseLineYearThree(){
		boolean isVisible=false;
		if(getCashFlows() != null  && getCashFlows().getFinancial() != null){
			for(FinancialAssessmentExpense expense : getCashFlows().getFinancial().getFinAssttExpDer()){
				if(expense.getYearThree() != null){
					return true;
				}
			}
		}
		return isVisible;
	}
	public Boolean isExpenseLineYearApprisal(){
		boolean isVisible=false;
		if(getCashFlows() != null  && getCashFlows().getFinancial() != null){
			for(FinancialAssessmentExpense expense : getCashFlows().getFinancial().getFinAssttExpDer()){
				if(expense.getYearAppraisal() != null){
					return true;
				}
			}
		}
		return isVisible;
	}
	public Boolean isGrossNoiUnderwrting(){
		boolean isVisible=false;
		if(getCashFlows() != null  && getCashFlows().getFinancial() != null){
			for(FinancialCRE financial : getCashFlows().getFinancial().getHeadersNoi()){
				if(financial.getGrossNoiUnderWriting() != null){
					return true;
				}
			}
		}
		return isVisible;
	}
	
	public Boolean isSelectedIncomeExpenseType(){
		boolean selected = false;
		BigDecimal count = BigDecimal.ZERO;
		Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
		if(getCashFlows() != null){
			if(getCashFlows().getFinancial() != null){
				FinancialCRE financial = getCashFlows().getFinancial();
				for(IncomeExpense selectedIncomeExpense : financial.getIncomeExpenses()){
					if(selectedIncomeExpense.getDetails() != null){
						count = map.put(selectedIncomeExpense.getDetails().getKey(),BigDecimal.ONE);
						map.put(selectedIncomeExpense.getDetails().getKey(),BigDecimal.ONE);
						if(count != null){
							selected = true;
						}
					}
				}
			}
			
		}
		return selected;
	}
	public List<AttributeChoice> getExpenseLegendList(){
		List<AttributeChoice> choiceList = new ArrayList<AttributeChoice>();
		if(getCashFlows() != null){
			if(getCashFlows().getFinancial() != null){
				FinancialCRE financial = getCashFlows().getFinancial();
				for(IncomeExpense incomeExpense : financial.getIncomeExpenses()){
					if(!incomeExpense.getExpenseLinesDer().isEmpty()){
						for(ExpenseLine expenseLine : incomeExpense.getExpenseLinesDer()){
							choiceList.add(expenseLine.getExpenseLegend());
						}
					}
				}
			}
		} 
		return choiceList;	
	}
	public List<AttributeChoice> getRevenueLegendList(){
		List<AttributeChoice> choiceList = new ArrayList<AttributeChoice>();
		if(getCashFlows() != null){
			if(getCashFlows().getFinancial() != null){
				FinancialCRE financial = getCashFlows().getFinancial();
				for(IncomeExpense incomeExpense : financial.getIncomeExpenses()){
					if(!incomeExpense.getRevenueLinesDer().isEmpty()){
						for(RevenueLine revenueLine : incomeExpense.getRevenueLinesDer()){
							choiceList.add(revenueLine.getRevenueLegend());
						}
					}
				}
			}
		} 
		return choiceList;	
	}
	public List<Collateral> getAllCollateralDer(){
		Request request = (Request)ContextHolder.getContext().getNamedContext().get("root");
		OneStopApp oneStopApp = request.getOneStopApp();
		return oneStopApp.getAllCustomerCollateral();
	}
	public Boolean isCollateralTypeCRE(){
		boolean isVisible=false;
		if(getCollateralCategory() != null ){
			if(("COLLATE_CATEGORY_LEISURE".equalsIgnoreCase(getCollateralCategory().getKey())) || 
					("COLLATE_CATEGORY_RETAIL".equalsIgnoreCase(getCollateralCategory().getKey())) || 
					("COLLATE_CATEGORY_OFFICE".equalsIgnoreCase(getCollateralCategory().getKey())) || 
					("COLLATE_CATEGORY_INDUSTRIAL".equalsIgnoreCase(getCollateralCategory().getKey())) || 
					("COLLATE_CATEGORY_HEALTHCARE".equalsIgnoreCase(getCollateralCategory().getKey())) || 
					("COLLATE_CATEGORY_OTHER".equalsIgnoreCase(getCollateralCategory().getKey())) || 
					("COLLATE_CATEGORY_MULTIFAMILY_APARTMENTS".equalsIgnoreCase(getCollateralCategory().getKey())) || 
					("COLLATE_CATEGORY_MIXED_USE".equalsIgnoreCase(getCollateralCategory().getKey()))){
				isVisible =true;
			}
			
		}
		return isVisible;
	}
	//Google map url embedding
	public String getMapUrl(){
		
		StringBuilder map = new StringBuilder();
		map = map.append("https://www.google.co.in/maps/place/");
		
		if(getLocation() != null){
			if(getLocation().getAddress1() != null){
				String address1 = getLocation().getAddress1().replaceAll(" ","+");
				map.append(address1);
			}if(getLocation().getCity() != null){
				String city = getLocation().getCity().replaceAll(" ","+");
				map.append(",+");
				map.append(city);
			}if(getLocation().getStateProvince() != null){
				String state = getLocation().getStateProvince().getName().replaceAll(" ","+");
				map.append(",+");
				map.append(state);
			}if(getLocation().getCountry() != null){
				String country = getLocation().getCountry().getName().replaceAll(" ","+");
				map.append(",+");
				map.append(country);
			}
		}
		return map.toString();
	}

}
