



package com.thirdpillar.codify.loanpath.model;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;

import javax.persistence.GeneratedValue;

import org.hibernate.search.annotations.Indexed;

import com.thirdpillar.foundation.model.BaseDataObject;

import org.hibernate.search.annotations.Store;

import com.thirdpillar.foundation.validation.Unique;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.Transient;

import java.math.BigDecimal;

import javax.persistence.JoinTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Version;

import org.hibernate.annotations.CascadeType;

import com.thirdpillar.foundation.model.WorkflowAware;
import com.thirdpillar.codify.loanpath.model.helper.FacilityExposureHelper;

import javax.persistence.FetchType;
import javax.persistence.ManyToMany;

import com.thirdpillar.foundation.search.Filter;
import com.thirdpillar.foundation.service.ContextHolder;
import com.thirdpillar.foundation.model.DataObjectHelper;

import org.hibernate.annotations.Index;

import com.thirdpillar.foundation.xstream.lookup.XStreamNamedQueryLookup;

import org.hibernate.envers.Audited;

import javax.persistence.ManyToOne;
import javax.persistence.Id;

import org.hibernate.annotations.Cascade;

import com.thirdpillar.codify.loanpath.lookup.AttributeChoiceLookup;

import javax.persistence.Entity;

import com.thirdpillar.codify.loanpath.model.DebtorCustomer;


public  class FacilityExposure {
	
	@Transient
	private DebtorCustomer debtorCustomerDer;
	
	
	public String getCounterPartyProductTypeId() {
		StringBuffer sb = new StringBuffer();
		if (getCounterParty() != null) {
			sb = sb.append(getCounterParty().getId());
			if (getFacilityType() != null) {
				sb = sb.append("-").append(getFacilityType().getKey());
			}
		}
		return sb.toString();
	}


	public DebtorCustomer getDebtorCustomerDer() {
		return debtorCustomerDer;
	}


	public void setDebtorCustomerDer(DebtorCustomer debtorCustomerDer) {
		this.debtorCustomerDer = debtorCustomerDer;
	}




}