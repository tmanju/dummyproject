



package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.BaseDataObject;
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
import com.thirdpillar.codify.loanpath.model.helper.FinancialAssetLiabilityHelper;
import javax.validation.Valid;
import org.hibernate.annotations.Cascade;
import javax.persistence.Id;

import java.util.ArrayList;
import javax.persistence.CascadeType;
import java.util.List;
import javax.persistence.Version;
import javax.persistence.Entity;

public  class FinancialAssetLiability {
	
	public Money getTotalREValue() {
		List<FinancialAssetRE> realEstates = getAssetRealEstates();
		Money total = new Money(0);
		for (FinancialAssetRE realEstate : realEstates) {
			if (realEstate.getAssetValue() != null) {
				total = total.add(realEstate.getAssetValue());
			}
		}
		return total;
	}
	
	public Money getTotalSecuritiesValue() {
		List<FinancialAssetSecurities> securities = getAssetSecurities();
		Money total = new Money(0);
		for (FinancialAssetSecurities security : securities) {
			if (security.getAssetValue() != null) {
				total = total.add(security.getAssetValue());
			}
		}
		return total;
	}

	public Money getTotalMVValue() {
		List<FinancialAssetMV> motorVechiles = getAssetMotorVechiles();
		Money total = new Money(0);
		for (FinancialAssetMV motorVechile : motorVechiles) {
			if (motorVechile.getAssetValue() != null) {
				total = total.add(motorVechile.getAssetValue());
			}
		}
		return total;
	}
	
	public Money getTotalPPValue() {
		List<FinancialAssetPP> personalProperties = getAssetPersonalProperties();
		Money total = new Money(0);
		for (FinancialAssetPP personalProperty : personalProperties) {
			if (personalProperty.getAssetValue() != null) {
				total = total.add(personalProperty.getAssetValue());
			}
		}
		return total;
	}
	
	public Money getTotalAssetValue() {
		Money total = new Money(0);
		total = total.add(getTotalREValue());
		total = total.add(getTotalSecuritiesValue());
		total = total.add(getTotalMVValue());
		total = total.add(getTotalPPValue());
		return total;
	}
	
	public Money getTotalMortgageLimit() {
		List<FinancialLiabilityMortgage> mortgages = getLiabilityMortgages();
		Money total = new Money(0);
		for (FinancialLiabilityMortgage mortgage : mortgages) {
			if (mortgage.getLimit() != null) {
				total = total.add(mortgage.getLimit());
			}
		}
		return total;
	}
	
	public Money getTotalMortgageBalance() {
		List<FinancialLiabilityMortgage> mortgages = getLiabilityMortgages();
		Money total = new Money(0);
		for (FinancialLiabilityMortgage mortgage : mortgages) {
			if (mortgage.getBalance() != null) {
				total = total.add(mortgage.getBalance());
			}
		}
		return total;
	}	

	public Money getTotalLoanLimit() {
		List<FinancialLiabilityLoan> loans = getLiabilityLoans();
		Money total = new Money(0);
		for (FinancialLiabilityLoan loan : loans) {
			if (loan.getLimit() != null) {
				total = total.add(loan.getLimit());
			}
		}
		return total;
	}
	
	public Money getTotalLoanBalance() {
		List<FinancialLiabilityLoan> loans = getLiabilityLoans();
		Money total = new Money(0);
		for (FinancialLiabilityLoan loan : loans) {
			if (loan.getBalance() != null) {
				total = total.add(loan.getBalance());
			}
		}
		return total;
	}
	
	
	public Money getTotalLiabilityBalance() {
		Money total = new Money(0);
		total = total.add(getTotalMortgageBalance());
		total = total.add(getTotalLoanBalance());
		return total;
	}
	
	public Money getNetWorth() {
		Money total = new Money(0);
		total = total.add(getTotalAssetValue());
		total = total.subtract(getTotalLiabilityBalance());
		return total;
	}	
	
}