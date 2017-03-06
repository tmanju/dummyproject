/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import java.util.ArrayList;
import java.util.List;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.util.StringUtils;


/**
 * Model class for Partner
 *
 * @author   PRATIBHA PURSWANI
 * @version  1.0
 * @since    1.0
 */
public class Partner extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 6567957769133498996L;

    //~ Methods --------------------------------------------------------------------------------------------------------
       public Boolean isPartnerType(Collateral collateral){
    	boolean selectedPartner = false;
    	for(ThirdPartyRequest thirdParty : collateral.getAssessmentCollateral().getThrdPtyReqts()){
    		if(thirdParty.getPartner() == null){
    			if(this.getPartnerType().contains(thirdParty.getThirdPartyRequestType())){
    				return true;
    			}
    		}
    	}
    	return selectedPartner;
    }
}
