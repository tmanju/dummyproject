package com.thirdpillar.codify.loanpath.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.util.StringUtils;

public class ScorecardDetails extends BaseDataObject{
	
	public List<ScrRskMod> getRiskModelsDer(){
		List<ScrRskMod> lstRskModels = new ArrayList<ScrRskMod>();
		for(BureauReport bureauRpts : getScrBureauRprts()){
			for(ScrRskMod rskModel :  bureauRpts.getRiskModels()){
				lstRskModels.add(rskModel);
			}
		}
		return lstRskModels;		
	}
	public List<BureauReport> getScrBureauRprtsDer(){
		return getScrBureauRprts();
	}
	
	
	
}
