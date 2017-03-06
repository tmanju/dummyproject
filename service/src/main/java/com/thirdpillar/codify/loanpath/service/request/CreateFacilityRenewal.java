package com.thirdpillar.codify.loanpath.service.request;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.thirdpillar.codify.loanpath.model.Approval;
import com.thirdpillar.codify.loanpath.model.ApprovalLevel;
import com.thirdpillar.codify.loanpath.model.AttributeChoice;
import com.thirdpillar.codify.loanpath.model.Collateral;
import com.thirdpillar.codify.loanpath.model.Facility;
import com.thirdpillar.codify.loanpath.model.FacilityCustomerRole;
import com.thirdpillar.codify.loanpath.model.FacilityRewriteInfo;
import com.thirdpillar.codify.loanpath.model.ProductLimits;
import com.thirdpillar.codify.loanpath.model.Request;
import com.thirdpillar.codify.loanpath.model.WorkflowStatus;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;

/**
 * 
 * @author gaurav.bagga
 *
 */
@Component
public class CreateFacilityRenewal {

	public void createRenewals(BaseDataObject entity){
		
		if (!(entity instanceof Request)) {
			// TODO throw expcetion, need entity to be isntance of requests
		}
		Request request = (Request) entity;
		
		Facility renewedFacility = null;
		Facility facility = null;
		ListIterator<Facility> facilityListIterator = request.getAllFacilities().listIterator();
		EntityService es = new EntityService();
		
		while(facilityListIterator.hasNext()){
			
			facility = facilityListIterator.next();
			
			//TODO :: how to know current instance is already renewed
			if(facility.inWfStatus("FACILITY_STATUS_APPROVED") && facility.isRenewalDue(60)){
								
				
				renewedFacility = (Facility) es.createNew(Facility.class);
				
				renewedFacility.setWithCheckAccess(facility.getWithCheckAccess());
				renewedFacility.setFundingStatus(facility.getFundingStatus());
				renewedFacility.setModWithdrawReason(facility.getModWithdrawReason());
				renewedFacility.setDescOfFinancingNeeds(facility.getDescOfFinancingNeeds());
				renewedFacility.setRequestedLoanAmt(facility.getRequestedLoanAmt());
				renewedFacility.setModNoiaReason(facility.getModNoiaReason());
				renewedFacility.setWithdrawReason(facility.getWithdrawReason());
				renewedFacility.setExternalIdentifier(facility.getExternalIdentifier());
				renewedFacility.setHierarchy(facility.getHierarchy());
				renewedFacility.setFacilityType(facility.getFacilityType());
				renewedFacility.setFacilityContract(facility.getFacilityContract());
				renewedFacility.setSbaSourceCode(facility.getSbaSourceCode());
				renewedFacility.setNatureOfSecurity(facility.getNatureOfSecurity());
				renewedFacility.setProjectManager(facility.getProjectManager());
				renewedFacility.setRewriteReason(facility.getRewriteReason());
				renewedFacility.setRequest(facility.getRequest());
				renewedFacility.setModificationStatus(facility.getModificationStatus());
				renewedFacility.setSalesAssistant(facility.getSalesAssistant());
				renewedFacility.setPurposeCode(facility.getPurposeCode());
				renewedFacility.setFacilityPracticeInformation(facility.getFacilityPracticeInformation());
				renewedFacility.setCommentGroup(facility.getCommentGroup());
				renewedFacility.setFunding(facility.getFunding());
				renewedFacility.setContract(facility.getContract());
				renewedFacility.setFacilityCategory(facility.getFacilityCategory());
				renewedFacility.setRelationshipSalesManager(facility.getRelationshipSalesManager());
				renewedFacility.setFacilityRR(facility.getFacilityRR());
				renewedFacility.setAccountManager(facility.getAccountManager());
				renewedFacility.setCoasetDefinition(facility.getCoasetDefinition());
				renewedFacility.setPricingOption(facility.getPricingOption());
				renewedFacility.setFacilityRRSource(facility.getFacilityRRSource());
				renewedFacility.setProgram(facility.getProgram());
				renewedFacility.setBusinessTypeFacility(facility.getBusinessTypeFacility());
				renewedFacility.setSbaType(facility.getSbaType());
				renewedFacility.setPreDocTaskOwner(facility.getPreDocTaskOwner());
				renewedFacility.setRenewalDate(null);

				renewedFacility.setModCreditDenialReason(new ArrayList<AttributeChoice>(facility.getModCreditDenialReason()));
				renewedFacility.setCreditDenialReason(new ArrayList<AttributeChoice>(facility.getCreditDenialReason()));
				renewedFacility.setProductLimits(new ArrayList<ProductLimits>(facility.getProductLimits()));
				
				
				//product roles
				if(facility.getFacilityCustomerRoles() != null){
					
					renewedFacility.setFacilityCustomerRoles(new ArrayList<FacilityCustomerRole>());
					
					Map<Long,FacilityCustomerRole> roleIdMappings = new HashMap<Long, FacilityCustomerRole>();
					
					for (FacilityCustomerRole  facilityCustomerRole : facility.getFacilityCustomerRoles()) {
						FacilityCustomerRole  renewalFacilityCustomerRole = (FacilityCustomerRole) es.createNew(FacilityCustomerRole.class);
						roleIdMappings.put(facilityCustomerRole.getId(), renewalFacilityCustomerRole);

						
						renewalFacilityCustomerRole.setPrimaryContact(facilityCustomerRole.getPrimaryContact());
						renewalFacilityCustomerRole.setExternalIdentifier(facilityCustomerRole.getExternalIdentifier());
						renewalFacilityCustomerRole.setPrimaryBorrower(facilityCustomerRole.getPrimaryBorrower());
						renewalFacilityCustomerRole.setPctOwned(facilityCustomerRole.getPctOwned());
						renewalFacilityCustomerRole.setParentRole(facilityCustomerRole.getParentRole());
						renewalFacilityCustomerRole.setPartyRole(facilityCustomerRole.getPartyRole());
						renewalFacilityCustomerRole.setPercentGuaranteed(facilityCustomerRole.getPercentGuaranteed());
						renewalFacilityCustomerRole.setCustomer(facilityCustomerRole.getCustomer());
						
						renewalFacilityCustomerRole.setFacility(renewedFacility);
						renewedFacility.getFacilityCustomerRoles().add(renewalFacilityCustomerRole);
						
					}
					
					// reset parent roles
					for (FacilityCustomerRole  renewalFacilityCustomerRole : renewedFacility.getFacilityCustomerRoles()) {
						if (renewalFacilityCustomerRole.getParentRole() != null) {
							renewalFacilityCustomerRole.setParentRole(roleIdMappings.get(renewalFacilityCustomerRole.getParentRole().getId()));							
						}
					}
				}
				
				//facility rewrite info
				FacilityRewriteInfo renewalFacilityRewriteInfo = null;
				if(facility.getFacilityRewrites() != null){
					
					renewedFacility.setFacilityRewrites(new ArrayList<FacilityRewriteInfo>());
					
					for (FacilityRewriteInfo  facilityRewriteInfo : facility.getFacilityRewrites()) {
						
						renewalFacilityRewriteInfo = (FacilityRewriteInfo) es.createNew(FacilityRewriteInfo.class);
						
						renewalFacilityRewriteInfo.setOriginalLoanAccount(facilityRewriteInfo.getOriginalLoanAccount());
						renewalFacilityRewriteInfo.setOriginalLoanSystem(facilityRewriteInfo.getOriginalLoanSystem());
						
						renewedFacility.getFacilityRewrites().add(renewalFacilityRewriteInfo);
					}
					
					
				} 
				
				
				if(facility.getCollaterals() != null){
					
					renewedFacility.setCollaterals(new ArrayList<Collateral>());
					
					for (Collateral  collateral : facility.getCollaterals()) {
						renewedFacility.getCollaterals().add(collateral);
					}
					
					
				}
				renewedFacility.setRenewalOf(facility);
				
				facilityListIterator.add(renewedFacility);
				
				//status changes
				
				 WorkflowStatus status=(WorkflowStatus)LookupService.getResult("WorkflowStatus.byStatusKey", "statusKey", "REQUEST_STATUS_PENDING_RENEWAL_SAVED"); 
				 request.setWfStatus(status);
				 
				 status=(WorkflowStatus)LookupService.getResult("WorkflowStatus.byStatusKey", "statusKey", "FACILITY_STATUS_APPROVED_PENDING_RENEWAL"); 
				 facility.setWfStatus(status);
				 
				 status=(WorkflowStatus)LookupService.getResult("WorkflowStatus.byStatusKey", "statusKey", "FACILITY_STATUS_PENDING"); 
				 renewedFacility.setWfStatus(status);
				 
				
			}
		}
		
	}
}
