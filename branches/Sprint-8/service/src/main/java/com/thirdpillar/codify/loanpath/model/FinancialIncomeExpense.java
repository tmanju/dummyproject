



package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.BaseDataObject;

import org.apache.commons.lang.StringUtils;
import org.hibernate.search.annotations.Indexed;
import javax.persistence.GeneratedValue;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.DocumentId;
import com.thirdpillar.foundation.validation.Unique;
import org.hibernate.search.annotations.Store;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.Column;
import com.thirdpillar.foundation.model.DataObjectHelper;
import com.thirdpillar.foundation.model.Money;
import com.thirdpillar.foundation.search.Filter;
import org.hibernate.envers.Audited;
import com.thirdpillar.foundation.xstream.lookup.XStreamNamedQueryLookup;
import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;

import javax.validation.Valid;
import org.hibernate.annotations.Cascade;
import javax.persistence.Id;

import java.util.ArrayList;
import javax.persistence.CascadeType;
import java.util.List;
import com.thirdpillar.codify.loanpath.model.helper.FinancialIncomeExpenseHelper;
import javax.persistence.Version;
import javax.persistence.Entity;

public  class FinancialIncomeExpense {
	
	public Money getTotalSalaryGrossIncome() {
		List<FinancialIncomeSalary> salaries = getSalaries();
		Money total = new Money(0);
		for (FinancialIncomeSalary salary : salaries) {
			if (salary.getGrossIncome() != null) {
				total = total.add(salary.getGrossIncome());
			}
		}
		return total;
	}
	
	public Money getTotalSalaryNetIncome() {
		List<FinancialIncomeSalary> salaries = getSalaries();
		Money total = new Money(0);
		for (FinancialIncomeSalary salary : salaries) {
			if (salary.getNetIncome() != null) {
				total = total.add(salary.getNetIncome());
			}
		}
		return total;
	}	
	
	public Money getTotalDividendIncome() {
		List<FinancialIncomeDividend> dividends = getDividends();
		Money total = new Money(0);
		for (FinancialIncomeDividend dividend : dividends) {
			if (dividend.getNetIncome() != null) {
				total = total.add(dividend.getNetIncome());
			}
		}
		return total;
	}	
	
	public Money getTotalRentIncome() {
		List<FinancialIncomeRental> rentalIncomes = getRentalIncomes();
		Money total = new Money(0);
		for (FinancialIncomeRental rentalIncome : rentalIncomes) {
			if (rentalIncome.getRentPaid() != null) {
				total = total.add(rentalIncome.getRentPaid());
			}
		}
		return total;
	}	
	
	public Money getTotalGovtIncome() {
		List<FinancialIncomeGovt> governmentalIncomes = getGovernmentalIncomes();
		Money total = new Money(0);
		for (FinancialIncomeGovt governmentalIncome : governmentalIncomes) {
			if (governmentalIncome.getNetIncome() != null) {
				total = total.add(governmentalIncome.getNetIncome());
			}
		}
		return total;
	}	
	
	public Money getTotalIncome() {
		Money total = new Money(0);
		total = total.add(getTotalSalaryNetIncome());
		total = total.add(getTotalDividendIncome());
		total = total.add(getTotalRentIncome());
		total = total.add(getTotalGovtIncome());
		return total;		
	}
	
	public Money getTotalMortgagePayment() {
		List<FinancialExpenseMortgage> mortgages = getMortgages();
		Money total = new Money(0);
		for (FinancialExpenseMortgage mortgage : mortgages) {
			if (mortgage.getMonthlyPayment() != null) {
				total = total.add(mortgage.getMonthlyPayment());
			}
		}
		return total;
	}
	
	public Money getTotalLoanPayment() {
		List<FinancialExpenseLoan> loans = getLoans();
		Money total = new Money(0);
		for (FinancialExpenseLoan loan : loans) {
			if (loan.getBalancePayment() != null) {
				total = total.add(loan.getBalancePayment());
			}
		}
		return total;
	}	

	public Money getTotalMiscPayment() {
		List<FinancialExpenseMisc> miscExpenses = getMiscExpenses();
		Money total = new Money(0);
		for (FinancialExpenseMisc miscExpense : miscExpenses) {
			if (miscExpense.getBalancePayment() != null) {
				total = total.add(miscExpense.getBalancePayment());
			}
		}
		return total;
	}	

	public Money getTotalExpense() {
		Money total = new Money(0);
		total = total.add(getTotalMortgagePayment());
		total = total.add(getTotalLoanPayment());
		total = total.add(getTotalMiscPayment());
		return total;		
	}
	
	
	
}