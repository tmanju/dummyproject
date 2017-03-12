



package com.thirdpillar.codify.loanpath.model;

import org.apache.commons.lang.StringUtils;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;

@XStreamLookupCollectionByOGNL.List(
	    {
	        @XStreamLookupCollectionByOGNL(
	            name = "byExternalIdentifier",
	            keys = { "externalIdentifier" }
	        ), @XStreamLookupCollectionByOGNL(
		            name = "byRefNumber",
		            keys = { "refNumber" }
		        )
	    }
	)

public  class Contact extends BaseDataObject{
	
	public String getDisplayName() {
		StringBuilder s = new StringBuilder();
		boolean addSpace = false;
		if (StringUtils.isNotEmpty(getFirstName())) {
			s= s.append(getFirstName());
			addSpace = true;
		}
		if (StringUtils.isNotEmpty(getLastName())) {
			if (addSpace) {
				s= s.append(" ");
			} else {
				addSpace = true;
			}
			s= s.append(getLastName());
		}
		return s.toString();
	}

    public String getDerLastNameFirstName() {
        StringBuilder s = new StringBuilder();
        boolean addSpace = false;

        if (StringUtils.isNotEmpty(getLastName())) {
            s = s.append(getLastName());
            addSpace = true;
        }

        if (StringUtils.isNotEmpty(getFirstName())) {

            if (addSpace) {
                s = s.append(Constants.COMMA);
            } else {
                addSpace = true;
            }

            s = s.append(getFirstName());
        }

        if (StringUtils.isNotEmpty(getMiddleName())) {

            if (addSpace) {
                s = s.append(Constants.BLANK);
            }

            s = s.append(getMiddleName());
        }

        return s.toString();
    }
}