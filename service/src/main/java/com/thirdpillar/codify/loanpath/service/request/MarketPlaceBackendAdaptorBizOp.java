package com.thirdpillar.codify.loanpath.service.request;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.codify.loanpath.model.Address;
import com.thirdpillar.codify.loanpath.model.AttributeChoice;
import com.thirdpillar.codify.loanpath.model.CustomerSite;
import com.thirdpillar.codify.loanpath.model.Deal;
import com.thirdpillar.codify.loanpath.model.Facility;
import com.thirdpillar.codify.loanpath.model.FacilityBE;
import com.thirdpillar.codify.loanpath.model.PricingOption;
import com.thirdpillar.codify.loanpath.model.Request;
import com.thirdpillar.codify.loanpath.model.WorkflowStatus;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.model.Money;
import com.thirdpillar.foundation.service.AbstractBusinessOperation;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;

@Component
@Configurable
public class MarketPlaceBackendAdaptorBizOp {

	private static final Log LOG = LogFactory
			.getLog(MarketPlaceBackendAdaptorBizOp.class);

	@Autowired
	private EntityService entityService;

	public void exportToMarketplace(BaseDataObject baseDataObject) {
		Request request = (Request) baseDataObject;
		LOG.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Exporting request " + request
				+ " to MarketPlace~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		for (Facility facility : request.getAllFacilities()) {

			FacilityBE facBE = generateNewFacility(facility, request);
			try {
				entityService.saveOrUpdate(facBE);
				entityService.flush();

				if (facility.getDrawRequest() != null) {
					LOG.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Generating Facility BE "
							+ facility
							+ " completed~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
					Deal deal = generateNewOrder(facility, request);
					deal.setFacilityBE(facBE);
					entityService.saveOrUpdate(deal);
					entityService.flush();
					LOG.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Generating Order for Facility "
							+ facility
							+ " completed~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
				}
				entityService.saveOrUpdate(facBE);
				entityService.flush();
			} catch (Exception e) {
				LOG.error("Error: {}", e);
			}
		}
		LOG.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Exporting request " + request
				+ " to MarketPlace completed~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}

	public Deal generateNewOrder(Facility facility, Request request) {
		LOG.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Generating Order for Facility "
				+ facility + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		Deal deal = (Deal) entityService.createNew(Deal.class);
		deal.setYearsUnderOwnership(request.getAccount().getCustomer()
				.getYearsInOwnership());
		deal.setProductType(facility.getFacilityType().getValue());
		Money fundingReq = new Money();
		fundingReq.setAmount(facility.getDrawRequest());
		deal.setInitialFundingReq(fundingReq);
		if (facility.getRequestedTerm() != null) {
			deal.setTerm(facility.getRequestedTerm().toString());
		}
		if (facility.getAdditionalOptions() != null) {
			for (PricingOption option : facility.getAdditionalOptions()) {
				if (option.getStructure() != null) {
					if ("RATE_TYPE_FLOATING".equals(option.getStructure().getRateType().getKey())
							&& option.getStructure().getCustomerRate() != null) {
						deal.setEstimatedYield(option.getStructure()
								.getCustomerRate().toString());
					}
				}
			}
		}
		deal.setMaturityDt(facility.getPricingOption().getStructure()
				.getMaturityDate());
		deal.setCreditFacAmt(facility.getApprovedLoanAmt());
		WorkflowStatus status = (WorkflowStatus) LookupService.getResult(
				"WorkflowStatus.byStatusKey", "statusKey",
				"FUNDING_REQUEST_STATUS_UNALLOCATED");
		deal.setWfStatus(status);

		return deal;
	}

	public FacilityBE generateNewFacility(Facility facility, Request request) {
		LOG.info("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~Generating Facility BE "
				+ facility + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		FacilityBE facBE = (FacilityBE) entityService
				.createNew(FacilityBE.class);

		facBE.setCustomer(request.getAccount().getCustomer());
		facBE.setYearsInBusiness(request.getAccount().getCustomer()
				.getYearsInOwnership());
		facBE.setProductType(facility.getFacilityType());
		if (facility.getRequestedTerm() != null) {
			facBE.setTerm((AttributeChoice) LookupService.getResult(
					"AttributeChoice.byKey", "key", facility.getRequestedTerm()
							.toString()));
		}
		if (facility.getAdditionalOptions() != null) {
			for (PricingOption option : facility.getAdditionalOptions()) {
				if (option.getStructure() != null) {
					if ("RATE_TYPE_FLOATING".equals(option.getStructure().getRateType().getKey())
							&& option.getStructure().getCustomerRate() != null) {
						facBE.setEstimatedYield(option.getStructure()
								.getCustomerRate().toString());
					} else if ("RATE_TYPE_FIXED".equals(option.getStructure().getRateType().getKey())
							&& option.getStructure().getInterestRate() != null) {
						facBE.setUnusedLineRate(option.getStructure()
								.getInterestRate());
					}
				}
			}
		}
		facBE.setMaturityDt(facility.getPricingOption().getStructure()
				.getMaturityDate());
		facBE.setCreditFacAmt(new Money(facility.getApprovedLoanAmt()));
		facBE.setMinUtilizationAmt((facility.getApprovedLoanAmt()
				.multiply(facility.getPricingOption().getStructure()
						.getMinUtilizationRate())).divide(new BigDecimal(100)));
		facBE.setServicingIdentifier(facility.getServicingIdentifier());
		facBE.setExternalIdentifier(request.getExternalIdentifier());
		return facBE;
	}
}
