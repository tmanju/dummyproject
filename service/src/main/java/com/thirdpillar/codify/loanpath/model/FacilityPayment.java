package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;

@XStreamLookupCollectionByOGNL.List(
	    {
	        @XStreamLookupCollectionByOGNL(
	        	name = "byServicingIdentifier",
	        	keys = { "servicingIdentifier"}
	        )
	    }
	)
public class FacilityPayment {

	public List<FacilityPayment> getSelectedFacilityPayment(){
		List<FacilityPayment> payments = new ArrayList<FacilityPayment>();
		payments.add(this);
		return payments;
	}
	
	public BigDecimal getTotalDisbursedPercent(){
		BigDecimal totalPer = BigDecimal.ZERO;
		if(this.getFacilityPaymentPlacements() != null){
			for(FacilityPaymentPlacement facPlacement : this.getFacilityPaymentPlacements()){
				if(facPlacement.getFacAllocator() != null){
					totalPer = totalPer.add(facPlacement.getFacAllocator().getFundPer());
				}
			}
		}
		return totalPer;
	}
}
