package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.thirdpillar.foundation.model.BaseDataObject;

public class Appraisal extends BaseDataObject{

	public BigDecimal getAmountPU(){
		BigDecimal amount = BigDecimal.ZERO;
		if(getFinalAmount() != null && getTotalUnit().compareTo(BigDecimal.ZERO) > 0 ){
			amount = getFinalAmount().divide(getTotalUnit(),2, RoundingMode.HALF_UP);
		}
		return amount;
    }
	public BigDecimal getDirectCapPU(){
		BigDecimal directCapPU = BigDecimal.ZERO;
		if(getDirectCapApproach() != null && getTotalUnit().compareTo(BigDecimal.ZERO) > 0 ){
			directCapPU = getDirectCapApproach().divide(getTotalUnit(),2, RoundingMode.HALF_UP);
		}
		return directCapPU;
    }
	public BigDecimal getCostPU(){
		BigDecimal costPU = BigDecimal.ZERO;
		if(getCostApproach() != null && getTotalUnit().compareTo(BigDecimal.ZERO) > 0 ){
			costPU = getCostApproach().divide(getTotalUnit(),2, RoundingMode.HALF_UP);
		}
		return costPU;
    }
	public BigDecimal getSalesApproachPU(){
		BigDecimal salesPU = BigDecimal.ZERO;
		if(getSalesApproach() != null && getTotalUnit().compareTo(BigDecimal.ZERO) > 0 ){
			salesPU = getSalesApproach().divide(getTotalUnit(),2, RoundingMode.HALF_UP);
		}
		return salesPU;
    }
	public BigDecimal getInsurableApproachPU(){
		BigDecimal insurablePU = BigDecimal.ZERO;
		if(getInsurableApproach() != null && getTotalUnit().compareTo(BigDecimal.ZERO) > 0 ){
			insurablePU = getInsurableApproach().divide(getTotalUnit(),2, RoundingMode.HALF_UP);
		}
		return insurablePU;
    }

}
