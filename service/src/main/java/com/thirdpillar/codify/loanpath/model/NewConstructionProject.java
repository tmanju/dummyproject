



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
import com.thirdpillar.codify.loanpath.model.helper.NewConstructionProjectHelper;
import javax.persistence.JoinColumn;
import javax.validation.constraints.Max;
import java.math.BigDecimal;
import org.hibernate.annotations.CascadeType;
import javax.persistence.Version;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import javax.persistence.FetchType;
import com.thirdpillar.foundation.search.Filter;
import com.thirdpillar.foundation.model.DataObjectHelper;
import org.hibernate.annotations.Index;
import com.thirdpillar.foundation.xstream.lookup.XStreamNamedQueryLookup;
import org.hibernate.envers.Audited;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Cascade;
import javax.persistence.Id;
import com.thirdpillar.codify.loanpath.lookup.AttributeChoiceLookup;
import javax.persistence.Entity;

public  class NewConstructionProject {
	
	public BigDecimal getNetIncomeYr(){
		
		BigDecimal netIncome = BigDecimal.ZERO;
		if(getGrossIncomeYr()!= null && getExpensesYr() !=null){
			netIncome = getGrossIncomeYr().subtract(getExpensesYr());
		}
		return netIncome;
	}
	

}