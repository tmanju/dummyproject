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
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;

import com.thirdpillar.codify.loanpath.model.PricingOption;
import com.thirdpillar.codify.loanpath.model.Program;
import com.thirdpillar.foundation.service.ContextHolder;
import com.thirdpillar.foundation.service.ContextImpl;
import com.thirdpillar.foundation.service.LookupService;
import com.thirdpillar.codify.loanpath.model.AttributeChoice;

/**
 * DOCUMENT ME!
 * 
 * @author Ankush.Bhardwaj
 * @version 1.0
 * @since 1.0
 */
public class IncomeExpense extends BaseDataObject {

	// ~ Static fields/initializers
	// -------------------------------------------------------------------------------------

	/**  */
	private static final long serialVersionUID = 1L;

	// ~ Methods
	// --------------------------------------------------------------------------------------------------------
	public BigDecimal getTotalRevenue() {
		BigDecimal total = BigDecimal.ZERO;
		if (getRevenueLines() != null) {
			for (RevenueLine revenueLine : getRevenueLinesDer()) {
				if (revenueLine.getAmountProRata() != null && revenueLine.getRevenueLegend() != null ){
					if (!("REVENUE_LEGEND_TYPE_VACANCY_LOSS".equals(revenueLine.getRevenueLegend().getKey()) 
							|| "REVENUE_LEGEND_TYPE_CONCESSIONS".equals(revenueLine.getRevenueLegend().getKey()) 
							|| "REVENUE_LEGEND_TYPE_BAD_DEBTS".equals(revenueLine.getRevenueLegend().getKey()))){
						total = total.add(revenueLine.getTotalAmountProRata());
					}else{
						total = total.subtract(revenueLine.getTotalAmountProRata());
					}
					
				}
			}
		}
		return total.equals( BigDecimal.ZERO)? null : total;
	}

	public BigDecimal getTotalExpense() {
		BigDecimal total = BigDecimal.ZERO;
		if (getExpenseLines() != null) {
			for (ExpenseLine expenseLine : getExpenseLinesDer()) {
				if (expenseLine.getAmountProRata() != null){
					total = total.add(expenseLine.getTotalAmountProRata());
				}
			}
		}
		return total.equals( BigDecimal.ZERO)? null : total;
	}

	public BigDecimal getRevenueExpenseRatio() {
		BigDecimal ratio = BigDecimal.ZERO;
		if (getTotalExpense() != null && getTotalRevenue() != null
				&& getTotalRevenue().compareTo(BigDecimal.ZERO) > 0
				&& getTotalExpense().compareTo(BigDecimal.ZERO) > 0) {
			ratio = getTotalRevenue().divide(getTotalExpense(), 2,
					RoundingMode.HALF_UP);

		}
		return ratio.equals( BigDecimal.ZERO)? null : ratio;
	}

	public BigDecimal getNoi() {
		BigDecimal noi = BigDecimal.ZERO;
		if (getTotalExpense() != null && getTotalRevenue() != null) {
			noi = getTotalRevenue().subtract(getTotalExpense());
		}
		return  noi.equals( BigDecimal.ZERO)? null : noi;
	}

	public List<ExpenseLine> getExpenseLinesDer() {
		BigDecimal totalAmount = BigDecimal.ZERO;
		ExpenseLine expenseLine = new ExpenseLine();
		Map<AttributeChoice, ExpenseLine> map = new HashMap<AttributeChoice, ExpenseLine>();
		for (ExpenseLine line : getExpenseLines()) {
			if (line.getExpenseLegend() != null && line.getAmount() != null) {
				totalAmount = line.getAmount();
				expenseLine = map.put(line.getExpenseLegend(), line);
				if (expenseLine != null) {
					if (line.getTotalAmount() != null) {
						totalAmount = totalAmount.add(expenseLine
								.getTotalAmount());
					} else {
						totalAmount = totalAmount.add(expenseLine.getAmount());
					}
					line.setTotalAmount(totalAmount);
				} else {
					line.setTotalAmount(totalAmount);
				}
				map.put(line.getExpenseLegend(), line);
			}
		}
		return new ArrayList<ExpenseLine>(map.values());
	}

	public List<RevenueLine> getRevenueLinesDer() {
		BigDecimal totalAmount = BigDecimal.ZERO;
		RevenueLine revenueLine = new RevenueLine();
		Map<AttributeChoice, RevenueLine> map = new HashMap<AttributeChoice, RevenueLine>();
		for (RevenueLine line : getRevenueLines()) {
			if (line.getRevenueLegend() != null && line.getAmount() != null) {
				totalAmount = line.getAmount();
				revenueLine = map.put(line.getRevenueLegend(), line);
				if (revenueLine != null) {
					if (line.getTotalAmount() != null) {
						totalAmount = totalAmount.add(revenueLine
								.getTotalAmount());
					} else {
						totalAmount = totalAmount.add(revenueLine.getAmount());
					}
					line.setTotalAmount(totalAmount);
				} else {
					line.setTotalAmount(totalAmount);
				}
				map.put(line.getRevenueLegend(), line);
			}
		}
		return new ArrayList<RevenueLine>(map.values());
	}

	public List<AttributeChoice> getListOfExpenseAttCh(
			List<ExpenseLine> listExpenseLine) {
		List<AttributeChoice> attributeChoiceList = new ArrayList<AttributeChoice>();
		for (ExpenseLine line : listExpenseLine) {
			attributeChoiceList.add(line.getExpenseLegend());
		}
		return attributeChoiceList;
	}

	public String getDetailsDer() {
		String detailType = "";
		if (getDetails() != null) {
			detailType = getDetails().getValue();
		}
		return detailType;
	}
}