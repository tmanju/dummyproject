package com.thirdpillar.codify.loanpath.model;

import java.text.SimpleDateFormat;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.codify.loanpath.constants.Constants;

public class Comment extends BaseDataObject {
	
	//~ Static fields/initializers -------------------------------------------------------------------------------------

    private static final long serialVersionUID = 3154597001876570709L;
    
    // ~ Methods--------------------------------------------------------------------------------------------------------

    public String getTime() {
	SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.TIME_FORMAT);
	String time = null;
	if (getMetaInfo().getCreatedAt() != null) {
	    time = dateFormat.format(getMetaInfo().getCreatedAt());
	}
	return time;
    }

    public String getDerDate() {
    	SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT);
    	String derDate = null;
    	if (getMetaInfo().getCreatedAt() != null) {
    		derDate = dateFormat.format(getMetaInfo().getCreatedAt());
    	}
    	return derDate;
    }
}