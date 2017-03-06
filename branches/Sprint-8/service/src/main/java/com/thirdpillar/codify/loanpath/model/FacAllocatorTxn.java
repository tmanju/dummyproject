package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;

import com.thirdpillar.codify.loanpath.util.Utility;
import com.thirdpillar.foundation.model.BaseDataObject;

public class FacAllocatorTxn extends BaseDataObject{

	public BigDecimal getTotalPaidToCp(){
		BigDecimal paidInterest = this.getPaidInterest() != null ? this.getPaidInterest() : BigDecimal.ZERO;
		BigDecimal paidUnusedFee = this.getPaidUnusedFee() != null ? this.getPaidUnusedFee() : BigDecimal.ZERO;
		BigDecimal paidServicingFee = this.getPaidServicingFee() != null ? this.getPaidServicingFee() : BigDecimal.ZERO;
		if(!this.getNetOff()){
			return Utility.add(paidInterest, paidUnusedFee, 3);
		}else if(this.getNetOff()){
			BigDecimal temp = Utility.add(paidInterest, paidUnusedFee, 3);
			return Utility.subtract(temp, paidServicingFee, 3);
		}
		return BigDecimal.ZERO;
	}
}
