package com.thirdpillar.codify.loanpath.model;


import com.thirdpillar.foundation.model.BaseDataObject;

public class ThirdPartyRequest extends BaseDataObject{
	
	public boolean isThirdPartyRequestType(String key) {
    	if ((getThirdPartyRequestType() != null) && key.equalsIgnoreCase(getThirdPartyRequestType().getKey())) {
            return true;
        }
            return false;
    }
	
	public boolean isAppraisal() {
        return isThirdPartyRequestType("THIRD_PARTY_REQUEST_TYPE_APPRAISAL");
    }
	public boolean isInsurance() {
        return isThirdPartyRequestType("THIRD_PARTY_REQUEST_TYPE_INSURANCE");
    }
	public boolean isInspection() {
        return isThirdPartyRequestType("THIRD_PARTY_REQUEST_TYPE_INSPECTION");
    }
	public String getStatusDer(){
		StringBuilder sb = new StringBuilder();
		if(isAppraisal() && getAppraisal() != null && getAppraisal().getWfStatus() != null){
			sb = sb.append(getAppraisal().getWfStatus().getValue());
		}else if(isInsurance() && getInsurance() != null && getInsurance().getWfStatus() != null){
			sb = sb.append(getInsurance().getWfStatus().getValue());
		}else if(isInspection() && getInspection() != null && getInspection().getWfStatus() != null){
			sb = sb.append(getInspection().getWfStatus().getValue());
		}
		return sb.toString();
	}
	
	public Appraisal getAppraisalDer(){
		if(getAppraisal() != null){
			return getAppraisal(); 
		}
		return null;
	}
	
}
