package com.thirdpillar.codify.loanpath.model;

import org.apache.commons.lang.StringUtils;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;

@XStreamLookupCollectionByOGNL.List(
	    {
	        @XStreamLookupCollectionByOGNL(
	            name = "byExternalIdentifier",
	            keys = { "externalIdentifier"}
	        ),
	        @XStreamLookupCollectionByOGNL(
	        	name = "byServicingIdentifier",
	        	keys = { "servicingIdentifier"}
	        )
	    }
	)
public class Address extends BaseDataObject {
	
	public String getDisplayAddress() {
		StringBuilder s = new StringBuilder();
		s = s.append(getAddress1());
		if (StringUtils.isNotEmpty(getAddress2())) {
			s = s.append(", ").append(getAddress2());
		}
		if (StringUtils.isNotEmpty(getCity())) {
			s= s.append(", ").append(getCity());
		}
		if (getStateProvince() != null) {
			s= s.append(", ").append(getStateProvince().getName());
		}
		if (StringUtils.isNotEmpty(getPostalCode())) {
			s= s.append(" ").append(getPostalCode());
		}
		if (StringUtils.isNotEmpty(getPostalCodePlus4())) {
			s= s.append("-").append(getPostalCodePlus4());
		}
		if (getCountry() != null) {
			s= s.append(" ").append(getCountry().getName());
		}
		
		return s.toString();
	}
	
	
	public void leadingZeros(String s, int length) {
	     if (s.length() < length){ 
	    	 setPostalCode(String.format("%0" + (length-s.length()) + "d%s", 0, s));
	     }
	}
	
	public boolean isNumeric(String s) {  
	    return s.matches("[-+]?\\d*\\.?\\d+");  
	} 

}
