



package com.thirdpillar.codify.loanpath.model;

import org.apache.commons.lang.StringUtils;
import org.hibernate.search.annotations.Field;
import javax.persistence.GeneratedValue;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.DocumentId;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.validation.Unique;
import org.hibernate.search.annotations.Store;
import com.thirdpillar.foundation.model.Metafied;
import javax.persistence.Table;
import javax.persistence.Column;
import com.thirdpillar.codify.loanpath.model.helper.UccJurisdictionStateHelper;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.util.ArrayList;
import javax.persistence.CascadeType;
import java.util.List;
import javax.validation.constraints.Size;
import javax.persistence.FetchType;
import org.hibernate.annotations.Cache;
import javax.persistence.OneToMany;
import com.thirdpillar.foundation.search.Filter;
import com.thirdpillar.foundation.model.DataObjectHelper;
import org.hibernate.annotations.Index;
import com.thirdpillar.foundation.xstream.lookup.XStreamNamedQueryLookup;
import org.hibernate.envers.Audited;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import org.hibernate.annotations.Cascade;
import javax.persistence.Id;
import javax.persistence.Entity;


public  class UccJurisdictionState {
	
	public UccJurisdictionCounty getDefaultJurisdictionCounty() {
		for (UccJurisdictionCounty county : getJurisdictionCounties()) {
			if (StringUtils.equals(county.getCountyCode(), "0")) {
				return county;
			}
		}
		return null;
	}

}