



package com.thirdpillar.codify.loanpath.model;

import org.apache.commons.lang.StringUtils;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;


@XStreamLookupCollectionByOGNL.List(
    {
        @XStreamLookupCollectionByOGNL(
            name = "byKey",
            keys = { "key" }
        )
    }
)
public  class AttributeChoice  extends BaseDataObject   {
	
	
	public String[] getGroupAsArray() {
		String groupCsv = getGroup();
		if (groupCsv != null) {
			return groupCsv.split(",");
		} else {
			return new String[0];
		}
	}
	
	public boolean isInGroup(String group) {
		String[] groupAsArray = getGroupAsArray();
		for (String g : groupAsArray ) {
			if (StringUtils.equals(g, group)) {
				return true;
			}
		}
		return false;
	}

	/*
	  *   Method will be used to display value NAICS_CODE - ExludeIndustry on order screen 
	  */
	 public String getDisplayValue() {
	  String displayformat="";
	  Attribute attribute=getAttribute();
	  
	  if(attribute!=null && (attribute.getKey().equals(Constants.NAICS_CODE_KEY))) {
	   displayformat=getCode();
	  } else {
	   displayformat=getValue();
	  }
	  return displayformat;
	 }
}