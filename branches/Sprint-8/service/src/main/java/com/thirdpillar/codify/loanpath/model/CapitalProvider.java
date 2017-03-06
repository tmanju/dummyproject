package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.EntityService;


public class CapitalProvider extends BaseDataObject{

	public BigDecimal getAvailableCashDer() {
		BigDecimal availCash = BigDecimal.ZERO;
		if(this.getCustomerAssociated()== null){
			return availCash;
		}
		BigDecimal pending = getPending();
		BigDecimal outstandingPrincipal = getOutstandingPrincipal();
		BigDecimal accVal = (this.getCustomerAssociated().getPledgedAmt() == null) ? BigDecimal.ZERO
				: this.getCustomerAssociated().getPledgedAmt().getAmount();
		if (accVal != null) {
			pending = (pending == null) ? BigDecimal.ZERO : pending;
			outstandingPrincipal = (outstandingPrincipal == null) ? BigDecimal.ZERO
					: outstandingPrincipal;
			availCash = accVal.subtract(pending.add(outstandingPrincipal));
		}
		return availCash;
	}

	public String getHasExIndustry() {

		String isExluded = "No";
		if (this.getCustomerAssociated() == null) {
			return "";
		}
		List<AttributeChoice> exIndustry = this.getCustomerAssociated()
				.getExIndustry();
		if (exIndustry != null && !exIndustry.isEmpty()) {
			isExluded = "Yes";
		}
		return isExluded;
	}

	public BigDecimal getCapacityToFund() {
		if (this.getCustomerAssociated() == null || this.getCustomerAssociated().getPledgedAmt() == null 
				|| this.getCustomerAssociated().getPledgedAmt().getAmount() == null) {
			return BigDecimal.ZERO;
		}
		if (this.getCustomerAssociated().getCommittedFacAmt() == null
				|| this.getCustomerAssociated().getOfferedFacAmt() == null) {
			return this.getCustomerAssociated().getPledgedAmt().getAmount();
		}
		BigDecimal commitfacAmt = this.getCustomerAssociated()
				.getCommittedFacAmt();
		BigDecimal offeredFacAmt = this.getCustomerAssociated()
				.getOfferedFacAmt();
		BigDecimal capacity = this.getCustomerAssociated().getPledgedAmt().getAmount().subtract(
				commitfacAmt.add(offeredFacAmt));

		return capacity;

	}
	public boolean participatingDate() {
		boolean match = true;
		java.util.Date date = new java.util.Date();
		if (this.getParticipatingInMPsince() != null) {
			if (this.getLastDealDt() != null) {
				if (this.getParticipatingInMPsince()
						.after(this.getLastDealDt())) {
					match = false;
				}
			} else {
				if (this.getParticipatingInMPsince().after(date)) {
					match = false;
				}
			}
		}
		return match;
	}

	public List<State> getRegion() {
		if (this.getCustomerAssociated() == null) {
			return new ArrayList<State>();
		}
		return this.getCustomerAssociated().getRegion();
	}
	public List<AttributeChoice> getExIndustry() {
		if (this.getCustomerAssociated() == null) {
			return new ArrayList<AttributeChoice>();
		}
		return this.getCustomerAssociated().getExIndustry();
	}
	public boolean isExistingProfile() {
		EntityService entityService = new EntityService();
		List<CapitalProvider> capitalProviders = entityService
				.findByNamedQueryAndNamedParam(
						"com.thirdpillar.codify.loanpath.model.CapitalProvider.byAssociatedCustomerId",
						"id", this.getCustomerAssociated().getId());
		if (capitalProviders == null) {
			return false;
		}
		for(CapitalProvider cp :capitalProviders){
			if(this.getId()==cp.getId()){
				continue;
			}
			return true;
		}
		return false;
	}
}
