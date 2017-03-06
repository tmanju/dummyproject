



package com.thirdpillar.codify.loanpath.model;

import org.hibernate.search.annotations.Field;

import javax.persistence.GeneratedValue;

import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.DocumentId;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.model.Money;
import com.thirdpillar.foundation.validation.Unique;

import org.hibernate.search.annotations.Store;

import javax.persistence.Temporal;
import javax.persistence.Table;
import javax.persistence.Column;

import java.util.Date;
import java.math.BigDecimal;

import javax.persistence.JoinColumn;
import javax.persistence.TemporalType;

import com.thirdpillar.codify.loanpath.model.helper.NegotiableSecurityHelper;


import org.hibernate.annotations.CascadeType;

import javax.persistence.Version;
import javax.validation.constraints.Size;

import org.hibernate.annotations.NamedQuery;

import javax.persistence.FetchType;

import com.thirdpillar.foundation.search.Filter;
import com.thirdpillar.foundation.model.DataObjectHelper;

import org.hibernate.annotations.Index;

import com.thirdpillar.foundation.xstream.lookup.XStreamNamedQueryLookup;

import org.hibernate.envers.Audited;

import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;

import javax.persistence.Id;

import org.hibernate.annotations.NamedQueries;

import com.thirdpillar.codify.loanpath.lookup.AttributeChoiceLookup;

import javax.persistence.Entity;

import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;


@XStreamLookupCollectionByOGNL.List(
	    { @XStreamLookupCollectionByOGNL(
	            name = "byNegotiableSecurityType",
	            keys = { "negotiableSecurityType.key" }
	        ) }
	)

public  class NegotiableSecurity {
	
	public Money getAssetValue() {
		Money assetValue = null;
		if (getQuantity() != null && getValue() != null) {
		assetValue = getValue().multiply(new BigDecimal(getQuantity()));
		}
		
		if (assetValue != null && getHaircut() != null) {
			assetValue = (new Money(1)).subtract(assetValue.multiply(getHaircut()));
		}
		return assetValue;
	}

}