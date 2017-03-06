package com.thirdpillar.codify.loanpath.model;

import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.thirdpillar.foundation.model.BaseDataObject;

public class AssessmentCollateral extends BaseDataObject{
	 
	public List<ThirdPartyRequest> getThrdPtyReqtsDer(){
	    	List<ThirdPartyRequest> thirdParties = new ArrayList<ThirdPartyRequest>();

	    	for(ThirdPartyRequest thirdParty : getThrdPtyReqts()){
	    		if(thirdParty.isAppraisal()){
	    			thirdParties.add(thirdParty);
	    		}
	    	}
	    	return thirdParties;
	    }
	public BigDecimal getParkingRatio(){
		BigDecimal ratio = BigDecimal.ZERO;
		if(getUnitArea() != null && getNoOfParking() != null && getUnitArea().compareTo(BigDecimal.ZERO) > 0 ) {
			ratio = getNoOfParking().divide(getUnitArea(), 2,RoundingMode.HALF_UP);
		}
		return ratio.equals( BigDecimal.ZERO)? null : ratio;
	}
	public String getUnitAreaBasisDer(){
		StringBuilder basis = new StringBuilder();
		if(getUnitAreaBasis() != null){
			basis = basis.append(getUnitAreaBasis().getValue());
		}
		return basis.toString();
	}

}
