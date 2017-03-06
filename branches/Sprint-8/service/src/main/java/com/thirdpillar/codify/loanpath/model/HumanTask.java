package com.thirdpillar.codify.loanpath.model;

import org.apache.commons.lang.WordUtils;
import com.thirdpillar.foundation.model.URLData;
import com.thirdpillar.foundation.codify.constants.CodifyConstants;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.codify.loanpath.constants.Constants;

public class HumanTask extends BaseDataObject{
	
	public String getEntityRefNumber() {

        if (this.getWorkItemObj() == null) {
            return "";
        }

        if (this.getWorkItemObj() instanceof Request) {
            return String.valueOf(((Request) this.getWorkItemObj()).getRefNumber());
        }
        return null;
    }
	//Over riding BC project method to overcome the the LowerCase issue
	public URLData getWorkItemDetails() {

        if (this.getWorkItemObj() != null) {
            String workItemDetails = getWorkItemObj().getClass().getSimpleName() + ": " +
                getWorkItemObj().toString();
            
            //String url = CodifyConstants.CONFIG.getString("webapp.server.baseurl") + "/crud/" +
           //     getWorkItemObj().getClass().getSimpleName().toLowerCase() + "/view.seam?canvasType=crud&eid=" +
          //      getWorkItemObj().getId();
            String url = CodifyConstants.CONFIG.getString(Constants.WEBAPP_SERVER_BASEURL) + Constants.CRUD +
            	WordUtils.uncapitalize(getWorkItemObj().getClass().getSimpleName()) + Constants.VIEW_CANVAS_CRUD +
                getWorkItemObj().getId();
            workItemDetails = workItemDetails + Constants.LEFT_BRACKET+ url + Constants.BRACKET;

            return new URLData(workItemDetails);
        }

        return new URLData();
    }
	
}