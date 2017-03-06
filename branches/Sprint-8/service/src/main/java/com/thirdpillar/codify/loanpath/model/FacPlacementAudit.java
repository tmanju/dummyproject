package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.thirdpillar.codify.loanpath.model.CapitalProvider;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.model.Money;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;

@Component
@Configurable
public class FacPlacementAudit extends BaseDataObject {
	public String getDbid() {
		if (this.getFacAllocator() != null) {
			return this.getFacAllocator().getId() + "";
		} else {
			return "";
		}

	}

	public String getMinUtilizationAmtDer() {
		String minUtilization = "0";
		BigDecimal minUtilAmt = this.getMinUtilizationAmt() == null ? BigDecimal.ZERO
				: this.getMinUtilizationAmt();
		minUtilization = String.format("%,.2f",
				minUtilAmt.setScale(2, RoundingMode.DOWN));
		Money creditFacAmt = this.getFacilityBE().getCreditFacAmt();
		if (creditFacAmt != null) {
			minUtilization = minUtilization + " " + creditFacAmt.getCurrency();
		}

		return minUtilization;
	}

	public String getFundAmtDer() {
		String fundAmtDer = "0";
		BigDecimal fundAmt = this.getFundAmt() == null ? BigDecimal.ZERO : this
				.getFundAmt();
		fundAmtDer = String.format("%,.2f",
				fundAmt.setScale(2, RoundingMode.DOWN));
		Money creditFacAmt = this.getFacilityBE().getCreditFacAmt();
		if (creditFacAmt != null) {
			fundAmtDer = fundAmtDer + " " + creditFacAmt.getCurrency();
		}
		return fundAmtDer;
	}
}
