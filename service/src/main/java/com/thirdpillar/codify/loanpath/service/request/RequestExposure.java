package com.thirdpillar.codify.loanpath.service.request;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.codify.loanpath.model.AttributeChoice;
import com.thirdpillar.codify.loanpath.model.Customer;
import com.thirdpillar.codify.loanpath.model.DebtorCustomer;
import com.thirdpillar.codify.loanpath.model.Exposure;
import com.thirdpillar.codify.loanpath.model.ExposureSummary;
import com.thirdpillar.codify.loanpath.model.Facility;
import com.thirdpillar.codify.loanpath.model.FacilityCustomerRole;
import com.thirdpillar.codify.loanpath.model.FacilityDelinquencyInfo;
import com.thirdpillar.codify.loanpath.model.FacilityExposure;
import com.thirdpillar.codify.loanpath.model.RelationshipParty;
import com.thirdpillar.codify.loanpath.model.Request;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.AbstractBusinessOperation;
import com.thirdpillar.foundation.service.BusinessOperation;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;

@Component @Configurable
public class RequestExposure {
	
	private static final Log LOG = LogFactory.getLog(RequestExposure.class);
	
	private static final String[] DIRECT_ROLES = {Constants.PARTY_ROLE_TYPE_CLIENT_KEY};
	private static final String[] RELATED_ROLES = {Constants.PARTY_ROLE_TYPE_GUARANTOR_KEY, Constants.PARTY_ROLE_TYPE_DEBTOR_KEY};
	private static final String[] EXPOSURE_ROLES = {Constants.PARTY_ROLE_TYPE_CLIENT_KEY,Constants.PARTY_ROLE_TYPE_GUARANTOR_KEY,Constants.PARTY_ROLE_TYPE_DEBTOR_KEY};
	
	
	@Autowired 
	private EntityService entityService;
	
	
	public void updateFacilityExposure(BaseDataObject entity) {
		
		if (!(entity instanceof Request)) {
			// TODO throw expcetion, need entity to be isntance of requests
		}
		Request request = (Request) entity;
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("Inside Facility Exposure...");
		}
		
		
		List<FacilityCustomerRole> allRoles = request.getAllCustomerRoles();
		if (allRoles == null || allRoles.isEmpty()) {
			// relationship is null, clear all existing exposures 
			clearFacilityExposures(request);			
			// nothing else to do
			return;
		}
		
		// 1. get all relationship parties from request.
		List<FacilityCustomerRole> accountParties = new ArrayList<FacilityCustomerRole>();
		List<FacilityCustomerRole> relatedParties = new ArrayList<FacilityCustomerRole>();
		List<Long> customerIds = new ArrayList<Long>();
			
		for (FacilityCustomerRole customerRole: allRoles) {
			if (customerRole.getPartyRole() != null && customerRole.getCustomer() != null) {
				if (partyInRole(customerRole, DIRECT_ROLES)) {
					accountParties.add(customerRole);
					customerIds.add(customerRole.getCustomer().getId());
				} else if (partyInRole(customerRole, RELATED_ROLES)) {
					relatedParties.add(customerRole);
					customerIds.add(customerRole.getCustomer().getId());
				}
			}
		}
		String[] notInStatuses = {"FACILITY_STATUS_DECLINED","FACILITY_STATUS_WITHDRAWN", "FACILITY_STATUS_EXPIRED", "FACILITY_STATUS_APPROVED_PENDING_RENEWAL", };
		String[] keys = EXPOSURE_ROLES;
		Long[] customers = customerIds.toArray(new Long[0]);
				
		List<Facility> facilities = entityService.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.Facility.byCustomerAndRoleAndNotInStatuses", new String[]{"notInStatuses","roles","customerIds"}, new Object[]{notInStatuses,keys,customers});
		if (LOG.isDebugEnabled()) {
			LOG.debug("Number of facililties found : " + facilities.size());			
		}
			
		// Clear all facility exposures		
		clearFacilityExposures(request);
		
		if (facilities.isEmpty()) {
			// no facilities found				
			return;
		}
		
		// For each facility found setup facility exposures
		for (Facility facility : facilities) {			
			
			LOG.debug("Found facility : " + facility.getRefNumber());
			List<Customer> accountCustomers = new ArrayList<Customer>();
			List<Customer> relatedCustomers = new ArrayList<Customer>();
			List<DebtorCustomer> debtorCustomers = new ArrayList<DebtorCustomer>();
			boolean setupFacilityExposure = false;
			
			
			// For each facility exposure, determine, who gets added as an account and relatedParty
			for (FacilityCustomerRole accountParty : accountParties) {
				for (FacilityCustomerRole facilityParty : facility.getFacilityCustomerRoles()) {
					if (partyInRole(facilityParty, DIRECT_ROLES)
							&& customerMatches(accountParty, facilityParty)) {
						// 	facility and accountParty
						accountCustomers.add(accountParty.getCustomer());
						setupFacilityExposure = true;
					}					
				}
			}
			for (FacilityCustomerRole relatedParty : relatedParties) {
				for (FacilityCustomerRole facilityParty : facility.getFacilityCustomerRoles()) {
					if (partyInRole(facilityParty, new String[]{Constants.PARTY_ROLE_TYPE_GUARANTOR_KEY})
							&& customerMatches(relatedParty, facilityParty)) {
						
						// 	facility and accountParty
						relatedCustomers.add(relatedParty.getCustomer());
						setupFacilityExposure = true;
					}					
				}
			}
			
			for (FacilityCustomerRole relatedParty : relatedParties) {
				for (DebtorCustomer facilityParty : facility.getDebtors()) {
					if (partyInRole(facilityParty.getFacilityCustomerRole(), new String[]{Constants.PARTY_ROLE_TYPE_DEBTOR_KEY})
							&& customerMatches(relatedParty, facilityParty.getFacilityCustomerRole())) {
							debtorCustomers.add(facilityParty);
					}					
				}
			}
			if (setupFacilityExposure) {
				LOG.debug("Setting Facility Exposure for facility - " + facility.getRefNumber());
				setupFacilityExposure(request, facility, accountCustomers, relatedCustomers);
				if(!debtorCustomers.isEmpty()){
					setupDebtorExposure(request, facility, debtorCustomers);
				}
			}
		}			
			
		if (LOG.isDebugEnabled()) {
			LOG.debug("Number of facility exposures setup - " + request.getExposure().getFacilityExposures().size());
		}		
		
	}
	
	
	public void updateExposureSummary(BaseDataObject entity) {
		
		if (!(entity instanceof Request)) {
			// TODO throw expcetion, need entity to be isntance of requests
		}
		Request request = (Request) entity;
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("Inside Facility Exposure... ");
		}
		
		List<FacilityCustomerRole> allRoles = request.getAllCustomerRoles();
			clearExposureSummaries(request);			
		
		// 1. get all relationship parties from request.
		List<Customer> accountCustomers = new ArrayList<Customer>();
		List<Customer> relatedCustomers = new ArrayList<Customer>();
		for (FacilityCustomerRole customerRole : allRoles) {
			if (customerRole.getPartyRole() != null && customerRole.getCustomer() != null) {
				if (partyInRole(customerRole, DIRECT_ROLES)) {
					accountCustomers.add(customerRole.getCustomer());
				} else if (partyInRole(customerRole, RELATED_ROLES)) {
					relatedCustomers.add(customerRole.getCustomer());
				}
			}
		}
		
		List<Facility> facilitiesIncludedInSummary = new ArrayList<Facility>();
		
		// Calcualting borrower exposures
		Exposure exposure = request.getExposure();
		// Borrower Sec + Unsec
		BigDecimal  borrowerRequested = BigDecimal.ZERO;
		BigDecimal  borrowerOutstanding = BigDecimal.ZERO;
		
		for (Customer customer : accountCustomers) {
			List<FacilityExposure> facilityExposures = exposure.findFacilityExposures(customer, true);
			
			if (LOG.isDebugEnabled()) {
				LOG.debug("Found facility exposure for account - " + customer.getHelper().getDisplayValue() + ", Size - " + facilityExposures.size());
			}			
			
			for (FacilityExposure facilityExposure : facilityExposures) {
			
				if ( !containsFacility(facilitiesIncludedInSummary,facilityExposure.getFacility())) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Including facilityExposure for summary - " + facilityExposure);
					}
					
					if (facilityExposure.getRequestedExposure() != null && facilityExposure.getRequestedExposure().doubleValue() > 0) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("Adding to requested exposure as borrowerRequested - " + facilityExposure.getRequestedExposure()); 
						}												
						borrowerRequested = add(borrowerRequested,facilityExposure.getRequestedExposure());
						facilitiesIncludedInSummary.add(facilityExposure.getFacility());
					}
	
					if (facilityExposure.getOutstandingExposure() != null && facilityExposure.getOutstandingExposure().doubleValue() > 0) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("Adding to outstanding exposure as borrowerOutstanding - " + facilityExposure.getOutstandingExposure()); 
						}						
						borrowerOutstanding = add(borrowerOutstanding,facilityExposure.getOutstandingExposure());
						facilitiesIncludedInSummary.add(facilityExposure.getFacility());
					}
				}
			}
		}
		
		setExposureSummary(exposure, Exposure.EXPOSURE_TYPE_TOTAL_DIRECT_EXPOSURE, borrowerRequested);
		
		BigDecimal  indirectExposure = BigDecimal.ZERO;
		facilitiesIncludedInSummary.clear();
		/**
		 * Guarantor Exposure
		 */
		for (Customer customer : relatedCustomers) {
			
			List<FacilityExposure> facilityExposures = exposure.findFacilityExposures(customer, false);

			if (LOG.isDebugEnabled()) {
				LOG.debug("Found facility exposure for related party - " + customer.getHelper().getDisplayValue() + ", Size - " + facilityExposures.size());
			}			
			
			for (FacilityExposure facilityExposure : facilityExposures) {
				FacilityCustomerRole fcr = (FacilityCustomerRole) LookupService.getResult("FacilityCustomerRole.byCustomerId", "customerId", customer.getId());
				if ( !containsFacility(facilitiesIncludedInSummary,facilityExposure.getFacility()) && (fcr != null && fcr.getPartyRole().getKey().equals(Constants.PARTY_ROLE_TYPE_GUARANTOR_KEY))) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Including facilityExposure for summary - " + facilityExposure);
					}
								
					if (facilityExposure.getRequestedExposure() != null && facilityExposure.getRequestedExposure().doubleValue() > 0) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("Adding to requested exposure as contingentRequested - " + facilityExposure.getRequestedExposure()); 
						}
						indirectExposure = add(indirectExposure,facilityExposure.getRequestedExposure());
						facilitiesIncludedInSummary.add(facilityExposure.getFacility());
					}
	
				}
			}
		}
		
		/**
		 * Debtor Exposure
		 */
		facilitiesIncludedInSummary.clear();
		for (Customer customer : relatedCustomers) {
			
			List<FacilityExposure> facilityExposures = exposure.findFacilityExposures(customer, false);

			if (LOG.isDebugEnabled()) {
				LOG.debug("Found facility exposure for related party - " + customer.getHelper().getDisplayValue() + ", Size - " + facilityExposures.size());
			}			
			
			for (FacilityExposure facilityExposure : facilityExposures) {
				FacilityCustomerRole fcr = (FacilityCustomerRole) LookupService.getResult("FacilityCustomerRole.byCustomerId", "customerId", customer.getId());
				if ( !containsFacility(facilitiesIncludedInSummary,facilityExposure.getFacility()) && (fcr != null && fcr.getPartyRole().getKey().equals(Constants.PARTY_ROLE_TYPE_DEBTOR_KEY))) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Including facilityExposure for summary - " + facilityExposure);
					}
								
					if (facilityExposure.getRequestedExposure() != null && facilityExposure.getRequestedExposure().doubleValue() > 0) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("Adding to requested exposure as contingentRequested - " + facilityExposure.getRequestedExposure()); 
						}
						indirectExposure = add(indirectExposure,facilityExposure.getRequestedDebtorExposure());
						facilitiesIncludedInSummary.add(facilityExposure.getFacility());
					}
	
				}
			}
		}

		setExposureSummary(exposure, Exposure.EXPOSURE_TYPE_TOTAL_INDIRECT_EXPOSURE, indirectExposure);
		setExposureSummary(exposure, Exposure.EXPOSURE_TYPE_TOTAL_DIRECT_INDIRECT_EXPOSURE, add(borrowerRequested, indirectExposure));
	}
	
	private void setExposureSummary(Exposure exposure, String exposureKey, BigDecimal value) {
		ExposureSummary exposureSummary = exposure.getExposureSummary(exposureKey);
		if (exposureSummary == null) {
			exposureSummary = (ExposureSummary)entityService.createNew(ExposureSummary.class);
			exposureSummary.setExposureType((AttributeChoice)LookupService.getResult("AttributeChoice.byKey", "key", exposureKey));
		}
		exposureSummary.setExposureValue(value);
		exposureSummary.setExposure(exposure);
		exposure.addToExposureSummaries(exposureSummary);
	
	}
	
	
	private void clearFacilityExposures(Request request) {
		if (request.getExposure().getFacilityExposures() != null) {
			request.getExposure().getFacilityExposures().clear();
		}
	}
	
	private void clearExposureSummaries(Request request) {
		if (request.getExposure().getExposureSummaries() != null) {
			request.getExposure().getExposureSummaries().clear();
		}
	}

	private boolean partyInRole(FacilityCustomerRole customerRole, String... roleKeys) {
		if (customerRole != null && customerRole.getPartyRole() != null) {
			for (String roleKey : roleKeys) {
				if (StringUtils.equals(customerRole.getPartyRole().getKey(),roleKey )) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean customerMatches(FacilityCustomerRole party1, FacilityCustomerRole party2) {
		if (party1 != null && party2 != null) {
			return customerMatches(party1.getCustomer(), party2.getCustomer());
		}
		
		return false;
	}
	
	private boolean customerMatches(Customer customer1, Customer customer2) {
		if (customer1 != null && customer2 != null) {
			return customer1.getId() == customer2.getId();
		}
		
		return false;
	}
	
	private void setupFacilityExposure(Request request, Facility facility, List<Customer> accountParties, List<Customer> relatedParties) {
		FacilityExposure facilityExposure = request.getExposure().findFacilityExposure(facility);
		if (facilityExposure == null) {
			facilityExposure =(FacilityExposure) entityService.createNew(FacilityExposure.class);
			// back reference
			facilityExposure.setExposure(request.getExposure());
			facilityExposure.setFacility(facility);
			request.getExposure().addToFacilityExposures(facilityExposure);

		}
		
		// exposure
		// ideally shouldn't happen except the current facility in question
		if (facility.getFacilityDecision() == null || !StringUtils.equals(facility.getFacilityDecision().getKey(),"FACILITY_DECISION_DECLINE")) {
			if (facility.getApprovedLoanAmt() != null) {
				facilityExposure.setRequestedExposure(facility.getApprovedLoanAmt());	
			} else {
				facilityExposure.setRequestedExposure(facility.getRequestedLoanAmt());
				
			}
		}
		
		
		// delinquency
		FacilityDelinquencyInfo facilityDelinquencyInfo = request.getExposure().getFacilityDelinquencyInfo(facility);
		if (facilityDelinquencyInfo != null) {
			facilityExposure.setOutstandingExposure(facilityDelinquencyInfo.getOutstanding());
			
			if (facilityExposure.getOutstandingExposure() != null && facilityExposure.getOutstandingExposure().doubleValue() > 0) {
				facilityExposure.setRequestedExposure(BigDecimal.ZERO); // setting requested exposure to 0 if outstanding is populated
			}
			facilityExposure.setThirtyDayArrears(facilityDelinquencyInfo.getThirtyDayArrears());
			facilityExposure.setSixtyDayArrears(facilityDelinquencyInfo.getSixtyDayArrears());
			facilityExposure.setNinetyDayArrears(facilityDelinquencyInfo.getNinetyDayArrears());
		} else {
			// set everyting to null
			facilityExposure.setOutstandingExposure(null);			
			facilityExposure.setThirtyDayArrears(null);
			facilityExposure.setSixtyDayArrears(null);
			facilityExposure.setNinetyDayArrears(null);		
		}
		
		
		
		facilityExposure.setFacilityNumber(new Long(facility.getRefNumber()));
		facilityExposure.setFacilityType(facility.getFacilityType());
		facilityExposure.setNatureOfSecurity(facility.getNatureOfSecurity());		
		facilityExposure.setFacilityRR(facility.getFacilityRR());
		facilityExposure.setBorrower(facility.getPrimaryBorrower());
		
		for (Customer accountParty : accountParties) {
			
			if (!containsCustomer(facilityExposure.getAccountCustomers(),accountParty)) {
				facilityExposure.addToAccountCustomers(accountParty);
			}
		}
		for (Customer relatedParty : relatedParties) {
			if (!containsCustomer(facilityExposure.getRelatedCustomers(),relatedParty)) {
				facilityExposure.addToRelatedCustomers(relatedParty);
			}
		}
	}
	
	private void setupDebtorExposure(Request request, Facility facility, List<DebtorCustomer> debtorParties) {
		FacilityExposure facilityExposure = request.getExposure().findFacilityExposure(facility);
		if (facilityExposure == null) {
			facilityExposure =(FacilityExposure) entityService.createNew(FacilityExposure.class);
			// back reference
			facilityExposure.setExposure(request.getExposure());
			facilityExposure.setFacility(facility);
			request.getExposure().addToFacilityExposures(facilityExposure);

		}
		BigDecimal debtorExposureValue = BigDecimal.ZERO;
		for (DebtorCustomer debtorParty : debtorParties) {
			debtorExposureValue = add(debtorExposureValue,debtorParty.getFundingLimit());
		}
		facilityExposure.setRequestedDebtorExposure(debtorExposureValue);
		facilityExposure.setFacilityNumber(new Long(facility.getRefNumber()));
		facilityExposure.setFacilityType(facility.getFacilityType());
		facilityExposure.setNatureOfSecurity(facility.getNatureOfSecurity());		
		facilityExposure.setFacilityRR(facility.getFacilityRR());
		facilityExposure.setBorrower(facility.getPrimaryBorrower());
			
		for (DebtorCustomer debtorParty : debtorParties) {
			if (!containsCustomer(facilityExposure.getRelatedCustomers(),debtorParty.getFacilityCustomerRole().getCustomer())) {
				facilityExposure.addToRelatedCustomers(debtorParty.getFacilityCustomerRole().getCustomer());
			}
		}
	}
	
	
	
	private boolean containsCustomer(List<Customer> customers, Customer lookupCustomer) {
		for (Customer customer : customers) {
			if (customerMatches(customer, lookupCustomer)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean containsFacility(List<Facility> facilities, Facility lookupFacility) {
		for (Facility facility : facilities) {
			if (facility != null && lookupFacility != null &&  (facility.getId() == lookupFacility.getId())) {
				return true;
			}
		}
		return false;
	}

	private BigDecimal add(BigDecimal... values) {
		BigDecimal total = BigDecimal.ZERO;
		for (BigDecimal value : values) {
			if (value != null) {
				total = total.add(value);
			}
		}
		return total;
	}	
}
