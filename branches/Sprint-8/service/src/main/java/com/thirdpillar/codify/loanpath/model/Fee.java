package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.BaseDataObject;

import java.math.BigDecimal;
import java.util.Date;

import com.thirdpillar.foundation.service.ContextHolder;
import com.thirdpillar.foundation.service.ContextImpl;
import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;
import com.thirdpillar.codify.loanpath.model.PricingOption;
import com.thirdpillar.codify.loanpath.util.Utility;

@XStreamLookupCollectionByOGNL.List(
	    { @XStreamLookupCollectionByOGNL(
	            name = "byExternalRefId",
	            keys = { "externalRefId" }
	        ) }
	)
public class Fee extends BaseDataObject {
	
	/**
	 * DOCUMENT ME!
	 *
	 * @author   Sajan Monga
	 * @version  1.0
	 * @since    1.0
	 */
	private static final long serialVersionUID = 1L;
	
	private static final int NO_NEED_TO_CHECK_PRG_AND_TOTAL_AMT = 2;
	
	private static final int NEED_TO_CHECK_PRG_AND_TOTAL_AMT = 3;

		
  
  
  
  public Boolean isFeePctValid(){
		BigDecimal calculatedfinAmt = BigDecimal.ZERO;
		BigDecimal result = BigDecimal.ZERO;
		if(getStructure() != null && getStructure().getFinancedAmount() != null ){
			BigDecimal financedAmount = getStructure().getFinancedAmount();
				if(this.getFeePolicy() != null && this.getFeePolicy().getCalculationMethod() != null && "CALCULATION_METHOD_BASED_ON_FINANCED_AMOUNT".equals(this.getFeePolicy().getCalculationMethod().getKey())){
					FeePolicy feepolicy = this.getFeePolicy();
						calculatedfinAmt = getPercentage(financedAmount,this.getFeePct());
						if(feepolicy.getMinimumPct()!= null){
							result = getPercentage(financedAmount,feepolicy.getMinimumPct());
							if(calculatedfinAmt.compareTo(result) != 1){
								return true;
							}
						}
						if(feepolicy.getMaximumPct()!= null){
							result = getPercentage(financedAmount,feepolicy.getMaximumPct());
							if(calculatedfinAmt.compareTo(result) != -1){
								return true;
							}
						}
					}
				}	
			return false;
		}
  
  	public BigDecimal getPercentage(BigDecimal amount,BigDecimal multiplier){
		BigDecimal result = BigDecimal.ZERO;
		BigDecimal hundred = new BigDecimal("100");
		result = amount.multiply(multiplier);
		result = result.divide(hundred);
		return result;
	}
  	
//  	public static BigDecimal calcEffectiveFee(FeePolicy feePolicy, User user){
//  		BigDecimal eff = null;
//   		Long milis =Utility.convertDateToTimezone(new Date(),user.getTimeZone().getIsoCode());
//   		Date feeDate = feePolicy.getEffectiveFromDate();
//   		Long effMilis = feeDate.getTime();
//   		if(milis.compareTo(effMilis) >= 0 && feePolicy.getEffectiveDefaultFeePct() != null){
//   			eff = feePolicy.getEffectiveDefaultFeePct();
//   		}else{
//   			eff = feePolicy.getDefaultPct();
//   		}
//  		return eff;
//  	}
			
}
