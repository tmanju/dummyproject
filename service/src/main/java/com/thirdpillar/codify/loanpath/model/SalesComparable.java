package com.thirdpillar.codify.loanpath.model;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.thirdpillar.foundation.model.BaseDataObject;

public class SalesComparable extends BaseDataObject{
	 
	public BigDecimal getSalesPricePerUnit(){
		BigDecimal perUnit = BigDecimal.ZERO;
		if(getNoOfUnit() != null && getNoOfUnit().compareTo(BigDecimal.ZERO) > 0 && getSalesPrice() != null) {
			perUnit = getSalesPrice().divide(getNoOfUnit(), 2,RoundingMode.HALF_UP);
		}
		return perUnit.equals( BigDecimal.ZERO)? null : perUnit;
	}
	public String getAddressDer(){
		StringBuilder address = new StringBuilder();
		if(getAddress() != null){
			address.append(getAddress());
		}
		if(getLocation() != null){
			if(getLocation().getCity() != null){
				address.append(", ");
				address.append(getLocation().getCity());
			}
			if(getLocation().getStateProvince() != null){
				address.append(", ");
				address.append(getLocation().getStateProvince().getName());
			}
			if(getLocation().getCountry() != null){
				address.append(", ");
				address.append(getLocation().getCountry().getName());
			}
			if(getLocation().getPostalCode() != null){
				address.append(", ");
				address.append(getLocation().getPostalCode());
			}
		}
		return address.toString();
	}
	

}
