/*
 * Copyright (c) 2005-2013 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
/**
 *
 */
package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import org.apache.log4j.Logger;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.util.StringUtils;

/**
 * @author
 */
public class CustomerBackend extends BaseDataObject {
	private int scale = 3;
	
	private static final Logger LOG = Logger.getLogger(CustomerBackend.class);
	
	public boolean isFpType(String key) {
		if ((getFpType() != null) && key.equalsIgnoreCase(getFpType().getKey())) {
			return true;
		}
			return false;
	}

	public boolean isFpTypeBank() {
		return isFpType("FP_TYPE_BANK");
	}

	public boolean isFpTypeFund() {
		return isFpType("FP_TYPE_FUND");
	}

	public Address getLegalAddressInfo() {
		if (this.getPrimarySite() != null
				|| this.getPrimarySite().getSiteDetails() != null) {
			List<Address> addresses = this.getPrimarySite().getSiteDetails()
					.getAddresses();
			if ((addresses != null) && (!addresses.isEmpty())) {
				for (Address addObj : addresses) {
					if (addObj.getUsage() != null) {
						for (AttributeChoice useType : addObj.getUsage()) {
							if (StringUtils.equals(
									Constants.ADDRESS_USE_TYPE_LEGAL,
									useType.getKey())) {
								return addObj;
							}
						}
					}
				}
			}
		}
		return null;
	}

	
	public String getAccValue(){
		LOG.info("*******************CUSTOMERBACKEND GET accvALUE()***************");

		if (getPledgedAmt()== null ){
			LOG.info("*******************CUSTOMERBACKEND GET accvALUE() Null null***************");
			return "0";
		}
		return getPledgedAmt().getAmount() +" "+ getPledgedAmt().getCurrency() ;
	}
	
	public boolean businessStartDate() {
		boolean match = true;
		LOG.info("Inside businessStartDate");
		java.util.Date date = new java.util.Date();
		if (this.getBusinessStartDttm() != null) {
			if (this.getBusinessStartDttm().after(date)) {
				match = false;
			}
		}
		return match;
	}
	
	public BigDecimal getInterestBalance(){
		BigDecimal result = BigDecimal.ZERO;
		BigDecimal intEarnedLTD = getInterestEarnedLTD();
		BigDecimal intPaidLTD = getInterestPaidLTD();
		if(intEarnedLTD == null){
			intEarnedLTD = BigDecimal.ZERO;
		}if(intPaidLTD == null){
			intPaidLTD = BigDecimal.ZERO;
		}
		if(intEarnedLTD != null && intPaidLTD != null){
			result = subtract(intEarnedLTD, intPaidLTD, scale);
		}
		return result;
	}
	
	public BigDecimal getUnusedFeeBalance(){
		BigDecimal result = BigDecimal.ZERO;
		BigDecimal unusedFeeLTD = getUnusedLineFeeLTD();
		BigDecimal unusedFeePaidLTD = getUnusedFeePaidLTD();
		if(unusedFeeLTD == null){
			unusedFeeLTD = BigDecimal.ZERO;
		}if(unusedFeePaidLTD == null){
			unusedFeePaidLTD = BigDecimal.ZERO;
		}
		if(unusedFeeLTD != null && unusedFeePaidLTD != null){
			result = subtract(unusedFeeLTD, unusedFeePaidLTD, scale);
		}
		return result;
	}
	
	public BigDecimal getServicingFeeBalance(){
		BigDecimal result = BigDecimal.ZERO;
		BigDecimal servicingFeeFeeLTD = getServicingFeeCaLTD();
		BigDecimal servicingFeeFeePaidLTD = getServicingFeeCpLTD();
		if(servicingFeeFeeLTD == null){
			servicingFeeFeeLTD = BigDecimal.ZERO;
		}if(servicingFeeFeePaidLTD == null){
			servicingFeeFeePaidLTD = BigDecimal.ZERO;
		}
		if(servicingFeeFeeLTD != null && servicingFeeFeePaidLTD != null){
			result = subtract(servicingFeeFeeLTD, servicingFeeFeePaidLTD, scale);
		}
		return result;
	}
	
	private BigDecimal subtract(BigDecimal lhs, BigDecimal subtrahend, int scale){
		 MathContext context = new MathContext(5, RoundingMode.HALF_EVEN);
		 BigDecimal result = BigDecimal.ZERO;
		 lhs.setScale(scale,context.getRoundingMode());
		 subtrahend.setScale(scale,context.getRoundingMode());
		 result = lhs.subtract(subtrahend,context);
		 return result;
	 }
}
