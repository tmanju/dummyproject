package com.thirdpillar.codify.loanpath.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.thirdpillar.codify.loanpath.model.Attribute;
import com.thirdpillar.codify.loanpath.model.AttributeChoice;
import com.thirdpillar.codify.loanpath.model.Customer;
import com.thirdpillar.codify.loanpath.model.CustomerExposureLimit;
import com.thirdpillar.codify.loanpath.model.Exposure;
import com.thirdpillar.codify.loanpath.model.ExposureSummary;
import com.thirdpillar.codify.loanpath.model.Facility;
import com.thirdpillar.codify.loanpath.model.FacilityCustomerRole;
import com.thirdpillar.codify.loanpath.model.FacilityDelinquencyInfo;
import com.thirdpillar.codify.loanpath.model.FacilityExposure;
import com.thirdpillar.codify.loanpath.model.Relationship;
import com.thirdpillar.codify.loanpath.model.RelationshipParty;
import com.thirdpillar.codify.loanpath.model.Request;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.AbstractBusinessOperation;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;

public class CalculateRelationshipExposure extends AbstractBusinessOperation {
	
	private static final Log LOG = LogFactory.getLog(CalculateRelationshipExposure.class);
	
	private static final String[] DIRECT_ROLES = {"PARTY_ROLE_TYPE_BORROWER"};
	private static final String[] RELATED_ROLES = {"PARTY_ROLE_TYPE_GUARANTOR", "PARTY_ROLE_TYPE_OWNER"};
	private static final String[] EXPOSURE_ROLES = {"PARTY_ROLE_TYPE_BORROWER","PARTY_ROLE_TYPE_GUARANTOR", "PARTY_ROLE_TYPE_OWNER"};
	
	@Autowired 
	private EntityService entityService;
	
	public Object execute(BaseDataObject entity, String operation, Object... params) {
		if (!(entity instanceof Relationship)) {
			// error
			return null;
		}
		
		Relationship relationship = (Relationship) entity;
		
		if (relationship.getExposure() == null) {
			relationship.setExposure((Exposure) entityService.createNew(Exposure.class));
		}
		
		updateProductExposure(relationship);
		
		updateExposureSummary(relationship);
		
		entityService.flush();
		
		return null;
	}
	
	public void updateProductExposure(Relationship relationship) {
		
		List<RelationshipParty> allRoles = relationship.getAllRelationshipParties();
		
		if (allRoles == null || allRoles.isEmpty()) {
			clearFacilityExposures(relationship);
			return;
		}
		
		List<RelationshipParty> directParties = new ArrayList<RelationshipParty>();
		List<RelationshipParty> relatedParties = new ArrayList<RelationshipParty>();
		List<Long> customerIds = new ArrayList<Long>();
		List<Customer> counterParties = new ArrayList<Customer>();
			
		for (RelationshipParty relationshipParty : allRoles) {
			if (relationshipParty.getPartyRole() != null && relationshipParty.getCustomer() != null) {
				if (partyInRole(relationshipParty, DIRECT_ROLES)) {
					directParties.add(relationshipParty);
				} else if (partyInRole(relationshipParty, RELATED_ROLES)) {
					relatedParties.add(relationshipParty);
				}
				
				if (relationshipParty.getCustomer().isCounterParty()) {
					counterParties.add(relationshipParty.getCustomer());
				} else {
					customerIds.add(relationshipParty.getCustomer().getId());
				}
			}
		}
		
		String[] notInStatuses = {"FACILITY_STATUS_DECLINED","FACILITY_STATUS_WITHDRAWN", "FACILITY_STATUS_EXPIRED", "FACILITY_STATUS_APPROVED_PENDING_RENEWAL", };
		String[] keys = EXPOSURE_ROLES;
		Long[] customers = customerIds.toArray(new Long[0]);		
		
		List<Facility> facilities = null;
		if (customers.length > 0) {
			facilities = entityService.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.Facility.byCustomerAndRoleAndNotInStatuses", new String[]{"notInStatuses","roles","customerIds"}, new Object[]{notInStatuses,keys,customers});
			if (LOG.isDebugEnabled()) {
				LOG.debug("Number of facililties found : " + facilities.size());			
			}
		}
		
		// Clear all facility exposures		
		clearFacilityExposures(relationship);
		
		if (facilities != null && !facilities.isEmpty()) {
		
			for (Facility facility : facilities) {		
				
				if (facility.isFacilityType("CREDIT_FACILITY_TYPE_EXPOSURE_LIMITS")) {
				}
				
				LOG.debug("Found facility : " + facility.getRefNumber());
				List<Customer> directCustomers = new ArrayList<Customer>();
				List<Customer> relatedCustomers = new ArrayList<Customer>();
				boolean setupFacilityExposure = false;
				
				// For each facility exposure, determine, who gets added as an account and relatedParty
				for (RelationshipParty accountParty : directParties) {
					for (FacilityCustomerRole facilityParty : facility.getFacilityCustomerRoles()) {
						if (partyInRole(facilityParty, DIRECT_ROLES)
								&& customerMatches(accountParty.getCustomer(), facilityParty.getCustomer())) {
							// 	facility and accountParty
							directCustomers.add(accountParty.getCustomer());
							setupFacilityExposure = true;
						}					
					}
				}
				for (RelationshipParty relatedParty : relatedParties) {
					for (FacilityCustomerRole facilityParty : facility.getFacilityCustomerRoles()) {
						if (partyInRole(facilityParty, RELATED_ROLES)
								&& customerMatches(relatedParty.getCustomer(), facilityParty.getCustomer())) {
							// 	facility and accountParty
							relatedCustomers.add(relatedParty.getCustomer());
							setupFacilityExposure = true;
						}					
					}
				}			
				
				if (setupFacilityExposure) {
					LOG.debug("Setting Facility Exposure for facility - " + facility.getRefNumber());
					setupFacilityExposure(relationship, facility, directCustomers, relatedCustomers);
				}
			}	
		}
			
		for (Customer counterParty : counterParties) {
			setupFacilityExposure(relationship, counterParty);
		}
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("Number of facility exposures setup - " + relationship.getExposure().getFacilityExposures().size());
		}			
		
	}
	
	public void updateExposureSummary(Relationship relationship) {
		
		if (LOG.isDebugEnabled()) {
			LOG.debug("Inside updateExposureSummary ... ");
		}
		
		List<RelationshipParty> allRoles = relationship.getAllRelationshipParties();
		
		if (allRoles == null || allRoles.isEmpty()) {
			clearFacilityExposures(relationship);
			return;
		}
		
		List<Customer> directCustomers = new ArrayList<Customer>();
		List<Customer> relatedCustomers = new ArrayList<Customer>();
		List<Customer> counterParties = new ArrayList<Customer>();
			
		for (RelationshipParty relationshipParty : allRoles) {
			if (relationshipParty.getPartyRole() != null && relationshipParty.getCustomer() != null) {
				if (partyInRole(relationshipParty, DIRECT_ROLES)) {
					if (relationshipParty.getCustomer().isCounterParty()) {
						counterParties.add(relationshipParty.getCustomer());
					} else {
						directCustomers.add(relationshipParty.getCustomer());
					}
				} else if (partyInRole(relationshipParty, RELATED_ROLES)) {
					if (relationshipParty.getCustomer().isCounterParty()) {
						counterParties.add(relationshipParty.getCustomer());
					}else {
						relatedCustomers.add(relationshipParty.getCustomer());
					}
				}
				
			}
		}
		
		List<Facility> facilitiesIncludedInSummary = new ArrayList<Facility>();
		List<String> counterProductIncludedInSummary = new ArrayList<String>();
		
		// Calcualting borrower exposures
		Exposure exposure = relationship.getExposure();
		// Borrower Sec + Unsec
		BigDecimal  borrowerRequested = BigDecimal.ZERO;
		BigDecimal  borrowerOutstanding = BigDecimal.ZERO;
		BigDecimal  borrowerRequestedUnsecured = BigDecimal.ZERO;
		BigDecimal  borrowerOutstandingUnsecured = BigDecimal.ZERO;
		
		for (Customer customer : directCustomers) {
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
						if (facilityExposure.getNatureOfSecurity() != null 
								&& StringUtils.equals("NATURE_OF_SECURITY_UNSECURED", facilityExposure.getNatureOfSecurity().getKey())) {
							if (LOG.isDebugEnabled()) {
								LOG.debug("Adding to requested exposure as borrowerRequestedUnsecured - " + facilityExposure.getRequestedExposure()); 
							}													
							borrowerRequestedUnsecured = add(borrowerRequestedUnsecured,facilityExposure.getRequestedExposure());							
						}
						facilitiesIncludedInSummary.add(facilityExposure.getFacility());
					}
	
					if (facilityExposure.getOutstandingExposure() != null && facilityExposure.getOutstandingExposure().doubleValue() > 0) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("Adding to outstanding exposure as borrowerOutstanding - " + facilityExposure.getOutstandingExposure()); 
						}						
						borrowerOutstanding = add(borrowerOutstanding,facilityExposure.getOutstandingExposure());
						if (facilityExposure.getNatureOfSecurity() != null 
								&& StringUtils.equals("NATURE_OF_SECURITY_UNSECURED", facilityExposure.getNatureOfSecurity().getKey())) {
							if (LOG.isDebugEnabled()) {
								LOG.debug("Adding to outstanding exposure as borrowerOutstandingUnsecured - " + facilityExposure.getOutstandingExposure()); 
							}						
							borrowerOutstandingUnsecured = add(borrowerOutstandingUnsecured,facilityExposure.getOutstandingExposure());							
						}
						facilitiesIncludedInSummary.add(facilityExposure.getFacility());
					}
				}
			}
		}
		
		Attribute exposureLimitFacilityTypeAttr = (Attribute) LookupService.getResult("Attribute.byKey", "key", "EXPOSURE_LIMIT_FACILITY_TYPE");
		
		if (exposureLimitFacilityTypeAttr != null 
				&& exposureLimitFacilityTypeAttr.getAttributeChoices() != null
				&& exposureLimitFacilityTypeAttr.getAttributeChoices().size() > 0) { 
		
			for (Customer customer : counterParties) {
				
				for (AttributeChoice exposureLimitProductType : exposureLimitFacilityTypeAttr.getAttributeChoices()) {
					FacilityExposure facilityExposure = exposure.findFacilityExposure(customer, exposureLimitProductType.getKey());
					
					if (facilityExposure == null) {
						continue;
					}
					if (LOG.isDebugEnabled()) {
						LOG.debug("Found facility exposure for counter Party - " + customer.getHelper().getDisplayValue() + ", Product  - " + exposureLimitProductType.getKey() + "");
					}			
					
					if ( !counterProductIncludedInSummary.contains(facilityExposure.getCounterPartyProductTypeId())) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("Including facilityExposure for summary - " + facilityExposure);
						}
						
						if (facilityExposure.getRequestedExposure() != null && facilityExposure.getRequestedExposure().doubleValue() > 0) {
							if (LOG.isDebugEnabled()) {
								LOG.debug("Adding to requested exposure as borrowerRequested - " + facilityExposure.getRequestedExposure()); 
							}												
							borrowerRequested = add(borrowerRequested,facilityExposure.getRequestedExposure());
							if (facilityExposure.getNatureOfSecurity() != null 
									&& StringUtils.equals("NATURE_OF_SECURITY_UNSECURED", facilityExposure.getNatureOfSecurity().getKey())) {
								if (LOG.isDebugEnabled()) {
									LOG.debug("Adding to requested exposure as borrowerRequestedUnsecured - " + facilityExposure.getRequestedExposure()); 
								}													
								borrowerRequestedUnsecured = add(borrowerRequestedUnsecured,facilityExposure.getRequestedExposure());							
							}
							counterProductIncludedInSummary.add(facilityExposure.getCounterPartyProductTypeId());
						}
		
						if (facilityExposure.getOutstandingExposure() != null && facilityExposure.getOutstandingExposure().doubleValue() > 0) {
							if (LOG.isDebugEnabled()) {
								LOG.debug("Adding to outstanding exposure as borrowerOutstanding - " + facilityExposure.getOutstandingExposure()); 
							}						
							borrowerOutstanding = add(borrowerOutstanding,facilityExposure.getOutstandingExposure());
							if (facilityExposure.getNatureOfSecurity() != null 
									&& StringUtils.equals("NATURE_OF_SECURITY_UNSECURED", facilityExposure.getNatureOfSecurity().getKey())) {
								if (LOG.isDebugEnabled()) {
									LOG.debug("Adding to outstanding exposure as borrowerOutstandingUnsecured - " + facilityExposure.getOutstandingExposure()); 
								}						
								borrowerOutstandingUnsecured = add(borrowerOutstandingUnsecured,facilityExposure.getOutstandingExposure());							
							}
							counterProductIncludedInSummary.add(facilityExposure.getCounterPartyProductTypeId());
						}
					}
				}
			}
		}
		
		setExposureSummary(exposure, Exposure.TOTAL_BORROWER_REQUESTED_EXPOSURE, borrowerRequested);
		setExposureSummary(exposure, Exposure.TOTAL_BORROWER_REQUESTED_UNSECURED_EXPOSURE, borrowerRequestedUnsecured);
		setExposureSummary(exposure, Exposure.TOTAL_BORROWER_OUTSTANDING_EXPOSURE, borrowerOutstanding);
		setExposureSummary(exposure, Exposure.TOTAL_BORROWER_OUTSTANDING_UNSECURED_EXPOSURE, borrowerOutstandingUnsecured);
		
		BigDecimal  contingentRequested = BigDecimal.ZERO;
		BigDecimal  contingentOutstanding = BigDecimal.ZERO;
		BigDecimal  contingentRequestedUnsecured = BigDecimal.ZERO;
		BigDecimal  contingentOutstandingUnsecured = BigDecimal.ZERO;
		
		for (Customer customer : relatedCustomers) {
			
			List<FacilityExposure> facilityExposures = exposure.findFacilityExposures(customer, false);

			if (LOG.isDebugEnabled()) {
				LOG.debug("Found facility exposure for related party - " + customer.getHelper().getDisplayValue() + ", Size - " + facilityExposures.size());
			}			
			
			for (FacilityExposure facilityExposure : facilityExposures) {
			
				if ( !containsFacility(facilitiesIncludedInSummary,facilityExposure.getFacility())) {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Including facilityExposure for summary - " + facilityExposure);
					}
								
					if (facilityExposure.getRequestedExposure() != null && facilityExposure.getRequestedExposure().doubleValue() > 0) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("Adding to requested exposure as contingentRequested - " + facilityExposure.getRequestedExposure()); 
						}
						contingentRequested = add(contingentRequested,facilityExposure.getRequestedExposure());
						if (facilityExposure.getNatureOfSecurity() != null 
								&& StringUtils.equals("NATURE_OF_SECURITY_UNSECURED", facilityExposure.getNatureOfSecurity().getKey())) {
							if (LOG.isDebugEnabled()) {
								LOG.debug("Adding to requested exposure as contingentRequestedUnsecured - " + facilityExposure.getRequestedExposure()); 
							}
							contingentRequestedUnsecured = add(contingentRequestedUnsecured,facilityExposure.getRequestedExposure());						
						}
						facilitiesIncludedInSummary.add(facilityExposure.getFacility());
					}
	
					if (facilityExposure.getOutstandingExposure() != null && facilityExposure.getOutstandingExposure().doubleValue() > 0) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("Adding to outstanding exposure as contingentOutstanding - " + facilityExposure.getOutstandingExposure()); 
						}
						contingentOutstanding = add(contingentOutstanding,facilityExposure.getOutstandingExposure());
						if (facilityExposure.getNatureOfSecurity() != null 
								&& StringUtils.equals("NATURE_OF_SECURITY_UNSECURED", facilityExposure.getNatureOfSecurity().getKey())) {
							if (LOG.isDebugEnabled()) {
								LOG.debug("Adding to outstanding exposure as contingentOutstandingUnsecured - " + facilityExposure.getOutstandingExposure()); 
							}
							
							contingentOutstandingUnsecured = add(contingentOutstandingUnsecured,facilityExposure.getOutstandingExposure());							
						}
						facilitiesIncludedInSummary.add(facilityExposure.getFacility());
					}
				}
			}
		}

		setExposureSummary(exposure, Exposure.TOTAL_CONTINGENT_REQUESTED_EXPOSURE, contingentRequested);
		setExposureSummary(exposure, Exposure.TOTAL_CONTINGENT_REQUESTED_UNSECURED_EXPOSURE, contingentRequestedUnsecured);
		setExposureSummary(exposure, Exposure.TOTAL_CONTINGENT_OUTSTANDING_EXPOSURE, contingentOutstanding);
		setExposureSummary(exposure, Exposure.TOTAL_CONTINGENT_OUTSTANDING_UNSECURED_EXPOSURE, contingentOutstandingUnsecured);
		
		// totals calculations
		// col 3 (sec + Unsec)
		setExposureSummary(exposure, Exposure.TOTAL_BORROWER_EXPOSURE, add(borrowerRequested, borrowerOutstanding));
		setExposureSummary(exposure, Exposure.TOTAL_CONTINGENT_EXPOSURE, add(contingentRequested, contingentOutstanding));
		
		// col 6 (Unsecured)
		setExposureSummary(exposure, Exposure.TOTAL_BORROWER_UNSECURED_EXPOSURE, add(borrowerRequestedUnsecured, borrowerOutstandingUnsecured));
		setExposureSummary(exposure, Exposure.TOTAL_CONTINGENT_UNSECURED_EXPOSURE, add(contingentRequestedUnsecured, contingentOutstandingUnsecured));		
		
		// row 3 (Sec + Unsec)
		setExposureSummary(exposure, Exposure.TOTAL_CALCULATED_REQUESTED_EXPOSURE, add(borrowerRequested, contingentRequested));
		setExposureSummary(exposure, Exposure.TOTAL_CALCULATED_OUTSTANDING_EXPOSURE, add(borrowerOutstanding, contingentOutstanding));
		setExposureSummary(exposure, Exposure.TOTAL_CALCULATED_EXPOSURE, add(borrowerRequested, contingentRequested, borrowerOutstanding, contingentOutstanding));
		
		// row 3 (Unsec)		
		setExposureSummary(exposure, Exposure.TOTAL_CALCULATED_REQUESTED_UNSECURED_EXPOSURE, add(borrowerRequestedUnsecured, contingentRequestedUnsecured));		
		setExposureSummary(exposure, Exposure.TOTAL_CALCULATED_OUTSTANDING_UNSECURED_EXPOSURE, add(borrowerOutstandingUnsecured, contingentOutstandingUnsecured));
		setExposureSummary(exposure, Exposure.TOTAL_CALCULATED_UNSECURED_EXPOSURE, add(borrowerRequestedUnsecured, contingentRequestedUnsecured, borrowerOutstandingUnsecured, contingentOutstandingUnsecured));
		
		// row 4 and row 4 are computed dynamically
		// TODO, need to implement this once we get rid of wordarounds becuase of codify		 
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
	
	private void setupFacilityExposure(Relationship relationship, Facility facility, List<Customer> directParties, List<Customer> relatedParties) {
		FacilityExposure facilityExposure = relationship.getExposure().findFacilityExposure(facility);
		if (facilityExposure == null) {
			facilityExposure =(FacilityExposure) entityService.createNew(FacilityExposure.class);
			// back reference
			facilityExposure.setExposure(relationship.getExposure());
			facilityExposure.setFacility(facility);
			relationship.getExposure().addToFacilityExposures(facilityExposure);

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
		
		facilityExposure.setFacilityNumber(new Long(facility.getRefNumber()));
		facilityExposure.setFacilityType(facility.getFacilityType());
		facilityExposure.setNatureOfSecurity(facility.getNatureOfSecurity());		
		facilityExposure.setFacilityRR(facility.getFacilityRR());
		facilityExposure.setBorrower(facility.getPrimaryBorrower());
		
		for (Customer directParty : directParties) {
			
			if (!containsCustomer(facilityExposure.getAccountCustomers(),directParty)) {
				facilityExposure.addToAccountCustomers(directParty);
			}
		}
		for (Customer relatedParty : relatedParties) {
			if (!containsCustomer(facilityExposure.getRelatedCustomers(),relatedParty)) {
				facilityExposure.addToRelatedCustomers(relatedParty);
			}
		}				
	}	
	
	private void setupFacilityExposure(Relationship relationship, Customer counterParty) {
		
		if (!counterParty.isCounterParty()) {
			return;		
		}
		for (CustomerExposureLimit exposureLimit : counterParty.getCustomerExposureLimits()) {
			if (exposureLimit.getProductType() == null) {
				return;
			}
			FacilityExposure facilityExposure = relationship.getExposure().findFacilityExposure(counterParty, exposureLimit.getProductType().getKey());
			if (facilityExposure == null) {
				facilityExposure =(FacilityExposure) entityService.createNew(FacilityExposure.class);
				// back reference
				facilityExposure.setExposure(relationship.getExposure());
				facilityExposure.setCounterParty(counterParty);
				facilityExposure.setFacilityType(exposureLimit.getProductType());
				relationship.getExposure().addToFacilityExposures(facilityExposure);

			}			
			
			facilityExposure.setRequestedExposure(exposureLimit.getExposureAmt().getValue());
			facilityExposure.setBorrower(counterParty);
		}
		
	}
	
	private void clearFacilityExposures(Relationship relationship) {
		if (relationship.getExposure().getFacilityExposures() != null) {
			relationship.getExposure().getFacilityExposures().clear();
		}
	}
	
	private void clearExposureSummaries(Relationship relationship) {
		if (relationship.getExposure().getExposureSummaries() != null) {
			relationship.getExposure().getExposureSummaries().clear();
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
	
	private boolean partyInRole(RelationshipParty partyRole, String... roleKeys) {
		if (partyRole != null && partyRole.getPartyRole() != null) {
			for (String roleKey : roleKeys) {
				if (StringUtils.equals(partyRole.getPartyRole().getKey(),roleKey )) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean customerMatches(Customer customer1, Customer customer2) {
		if (customer1 != null && customer2 != null) {
			return customer1.getId() == customer2.getId();
		}
		
		return false;
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
			total = total.add(value);
		}
		return total;
	}	
	
}
