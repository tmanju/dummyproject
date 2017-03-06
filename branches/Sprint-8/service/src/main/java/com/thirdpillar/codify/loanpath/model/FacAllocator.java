package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.thirdpillar.codify.loanpath.model.CapitalProvider;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.model.Money;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;

@Component
@Configurable
public class FacAllocator extends BaseDataObject {

	// @Transient

	public void changeStatus(BaseDataObject entity, String wfStatusKey) {
		WorkflowStatus status = (WorkflowStatus) LookupService.getResult(
				"WorkflowStatus.byStatusKey", "statusKey", wfStatusKey);
		this.setWfStatus(status);
	}

	public void acceptFacAllocation(BaseDataObject entity,
			Map<Long, BaseDataObject> map) {
		changeStatus(entity, "DEALS_STATUS_ACCEPTED");
	}

	public void declineFacAllocation(BaseDataObject entity,
			Map<Long, BaseDataObject> map) {
		changeStatus(entity, "DEALS_STATUS_REJECTED");
	}

	public void fundedFacAllocation(BaseDataObject entity,
			Map<Long, BaseDataObject> map) {
		changeStatus(entity, "DEALS_STATUS_FUNDED");
	}

	public BigDecimal getMinUtilizationPerForFacility() {
		BigDecimal base = getFacilityBE().getCreditFacAmt().getAmount();
		BigDecimal newAmt = getFacilityBE().getMinUtilizationAmt();
		if (base == null || newAmt == null){
			return BigDecimal.ZERO;
		}
		return getNewPercent(base, newAmt);
	}

	public BigDecimal percentage(BigDecimal base, BigDecimal pct) {
		return (base.multiply(pct)).divide(new BigDecimal(100), 2,
				RoundingMode.DOWN);
	}

	public BigDecimal getNewPercent(BigDecimal base, BigDecimal newAmt) {
		return (newAmt.multiply(new BigDecimal(100))).divide(base, 2,
				RoundingMode.DOWN);
	}

	public String getDbid() {
		return getId() + "";
	}

	public List<Object> getOldCPs() {
		EntityService entityService = new EntityService();
		List<Object> capitalProviders = entityService
				.findByNamedQueryAndNamedParam(
						"com.thirdpillar.codify.loanpath.model.FacPlacementAudit.fetchAllByFacAllocatroId",
						"id", this.getId());
		return capitalProviders;
	}

	public FacAllocator calculateCpSettlementAmt(BigDecimal amt) {
		if (getCpSettlementAmt() == null) {
			setCpSettlementAmt(BigDecimal.ZERO);
		}
		setCpSettlementAmt(getCpSettlementAmt().add(amt));
		return this;
	}

	public FacAllocator calculateCpBalance(BigDecimal amt) {
		if (getCpBalance() == null) {
			setCpBalance(BigDecimal.ZERO);
		}
		setCpBalance(getCpBalance().add(amt));
		return this;
	}

	public BigDecimal getPrinciple() {
		BigDecimal minUtilization = getMinUtilizationAmt();
		BigDecimal cpBal = getCpBalance();
		if (minUtilization != null && cpBal != null) {
			if (minUtilization.compareTo(cpBal) >= 0) {
				return minUtilization;
			} else {
				return cpBal;
			}
		}
		return BigDecimal.ZERO;
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
