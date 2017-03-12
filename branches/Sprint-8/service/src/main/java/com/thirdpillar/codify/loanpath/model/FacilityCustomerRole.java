



package com.thirdpillar.codify.loanpath.model;

import org.hibernate.search.annotations.Field;

import javax.persistence.GeneratedValue;

import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.DocumentId;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.validation.Unique;

import org.hibernate.search.annotations.Store;

import javax.persistence.Table;

import com.thirdpillar.foundation.lookup.OGNLLookup;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.validation.constraints.Max;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CascadeType;

import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NamedQuery;

import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.thirdpillar.codify.loanpath.model.helper.FacilityCustomerRoleHelper;
import com.thirdpillar.foundation.search.Filter;
import com.thirdpillar.foundation.model.DataObjectHelper;

import org.hibernate.annotations.Index;

import com.thirdpillar.foundation.xstream.lookup.XStreamNamedQueryLookup;
import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;

import org.hibernate.envers.Audited;

import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;

import javax.persistence.Id;

import org.hibernate.annotations.NamedQueries;

import com.thirdpillar.codify.loanpath.lookup.AttributeChoiceLookup;

import javax.persistence.Entity;


@XStreamLookupCollectionByOGNL.List(
	    { @XStreamLookupCollectionByOGNL(
	            name = "byExternalIdentifier",
	            keys = { "externalIdentifier" }
	        ),
	        @XStreamLookupCollectionByOGNL(
		            name = "byServicingIdentifier",
		            keys = { "servicingIdentifier" }
		        )}
	)
public  class FacilityCustomerRole {
	
	public List<FacilityCustomerRole> getRelatedRoles() {
		List<FacilityCustomerRole> relatedRoles = new ArrayList<FacilityCustomerRole>();
		
		for (FacilityCustomerRole role : getFacility().getFacilityCustomerRoles()) {
			if (role == this) {
				continue;
			}
			
			if (role.getParentRole() != null && role.getParentRole() == this) {
				relatedRoles.add(role);
			}
			
		}
		return relatedRoles;
		
	}
	
	// Applicable for only non-individual customer
	public FacilityCustomerRole getPrimaryContactRole() {
		
		if (this.getCustomer() == null || !this.getCustomer().isNonIndividual()) {
			return null;
		}
		
		List<FacilityCustomerRole> relatedRoles = getRelatedRoles();
		for (FacilityCustomerRole role : relatedRoles) {
			if (role.getCustomer() != null && role.getCustomer().isIndividual() && role.getPrimaryContact()) {
				return role;
			}
		}
		return null;
	}
    
}