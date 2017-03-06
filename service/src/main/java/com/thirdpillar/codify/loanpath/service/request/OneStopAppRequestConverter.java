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

import com.thirdpillar.codify.loanpath.model.*;
import com.thirdpillar.codify.loanpath.model.Address;
import com.thirdpillar.codify.loanpath.model.AttributeChoice;
import com.thirdpillar.codify.loanpath.model.Collateral;
import com.thirdpillar.codify.loanpath.model.Contact;
import com.thirdpillar.codify.loanpath.model.Customer;
import com.thirdpillar.codify.loanpath.model.CustomerSite;
import com.thirdpillar.codify.loanpath.model.Exposure;
import com.thirdpillar.codify.loanpath.model.ExposureSummary;
import com.thirdpillar.codify.loanpath.model.Facility;
import com.thirdpillar.codify.loanpath.model.Structure;
import com.thirdpillar.codify.loanpath.model.PricingOption;
import com.thirdpillar.codify.loanpath.model.FacilityCustomerRole;
import com.thirdpillar.codify.loanpath.model.FacilityDelinquencyInfo;
import com.thirdpillar.codify.loanpath.model.FacilityExposure;
import com.thirdpillar.codify.loanpath.model.OneStopApp;
import com.thirdpillar.codify.loanpath.model.KeyPricing;
import com.thirdpillar.codify.loanpath.model.RelationshipParty;
import com.thirdpillar.codify.loanpath.model.Request;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.AbstractBusinessOperation;
import com.thirdpillar.foundation.service.BusinessOperation;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;

@Component @Configurable
public class OneStopAppRequestConverter {

	@Autowired 
	private EntityService entityService;
	
	
	public void convertOneStopAppToRequest(BaseDataObject entity) {
		
		Request request = (Request) entity;
		OneStopApp oneStopApp = request.getOneStopApp();
		request.setRequestType((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "REQUEST_TYPE_CREDIT"));
		request.setRequestName(oneStopApp.getEntityName());

		Customer customer = oneStopApp.getNonIndividualCustomer();
		if (customer == null) {
			// Create Entity as Non-individual Customer
			customer = (Customer) entityService.createNew(Customer.class);
			customer.setCustomerType((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "CUSTOMER_TYPE_NON_INDIVIDUAL"));
			customer.setLegalEntityType(oneStopApp.getLegalEntityType());
			customer.setLegalName(oneStopApp.getEntityName());
			customer.setFederalTaxId(oneStopApp.getFederalTaxId());
	
			CustomerSite primarySite = (CustomerSite) entityService.createNew(CustomerSite.class);
			Contact siteDetails = (Contact) entityService.createNew(Contact.class);
			primarySite.setSiteDetails(siteDetails);
			customer.setPrimarySite(primarySite);
			siteDetails.setOtherPhone(oneStopApp.getContact().getOtherPhone());
			siteDetails.setEmail(oneStopApp.getContact().getEmail());
			
			if (oneStopApp.getContact().getAddresses() != null) {
				for (Address oneStopAddress : oneStopApp.getContact().getAddresses()) {
					Address address = (Address) entityService.createNew(Address.class);
					siteDetails.addToAddresses(address);
					address.setAddress1(oneStopAddress.getAddress1());
					address.setAddress2(oneStopAddress.getAddress1());
					address.setCity(oneStopAddress.getCity());
					address.setStateProvince(oneStopAddress.getStateProvince());
					address.setCountry(oneStopAddress.getCountry());
					address.setUse(oneStopAddress.getUse());			
				}
								
			}
			
			Contact ct = (Contact) entityService.createNew(Contact.class);
			ct.setFirstName(oneStopApp.getContact().getFirstName());
			ct.setLastName(oneStopApp.getContact().getLastName());
			ct.setFirstName(oneStopApp.getContact().getFirstName());
			ct.setMobilePhone(oneStopApp.getContact().getMobilePhone());
			ct.setOtherPhone(oneStopApp.getContact().getOtherPhone());
			ct.setEmail(oneStopApp.getContact().getEmail());
			primarySite.addToContacts(ct);
			
			
			entityService.saveOrUpdate(customer);
		}
		//Create Contact as Individual customer		
		Customer contactCustomer = oneStopApp.getContactCustomer();
		if (contactCustomer == null && !"CREDIT_FACILITY_TYPE_COMMERCIAL_REAL_ESTATE".equals(oneStopApp.getProductType().getKey())) {
			contactCustomer = (Customer) entityService.createNew(Customer.class);
			contactCustomer.setCustomerType((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "CUSTOMER_TYPE_INDIVIDUAL"));
			contactCustomer.setFirstName(oneStopApp.getContact().getFirstName());
			contactCustomer.setLastName(oneStopApp.getContact().getLastName());		
			
			CustomerSite bcPrimarySite = (CustomerSite) entityService.createNew(CustomerSite.class);
			Contact bcSiteDetails = (Contact) entityService.createNew(Contact.class);
			bcPrimarySite.setSiteDetails(bcSiteDetails);
			contactCustomer.setPrimarySite(bcPrimarySite);
			bcSiteDetails.setMobilePhone(oneStopApp.getContact().getMobilePhone());
			bcSiteDetails.setOtherPhone(oneStopApp.getContact().getOtherPhone());
			bcSiteDetails.setEmail(oneStopApp.getContact().getEmail());
					
			if (oneStopApp.getContact().getAddresses() != null) {
					for (Address oneStopAddress : oneStopApp.getContact().getAddresses()) {
						Address address = (Address) entityService.createNew(Address.class);
						bcSiteDetails.addToAddresses(address);
						address.setAddress1(oneStopAddress.getAddress1());
						address.setAddress2(oneStopAddress.getAddress2());
						address.setCity(oneStopAddress.getCity());
						address.setStateProvince(oneStopAddress.getStateProvince());
						address.setCountry(oneStopAddress.getCountry());
						address.setUse(oneStopAddress.getUse());			
					}
				}
			
				entityService.saveOrUpdate(contactCustomer);
			}
		//Update PrimaryContact info in case of CRE
		if ("CREDIT_FACILITY_TYPE_COMMERCIAL_REAL_ESTATE".equals(oneStopApp.getProductType().getKey())) {
			if(customer.getPrimarySite().getSiteDetails().getFirstName() == null){
				customer.getPrimarySite().getSiteDetails().setFirstName(oneStopApp.getContact().getFirstName());
			}
			if(customer.getPrimarySite().getSiteDetails().getLastName() == null){
				customer.getPrimarySite().getSiteDetails().setLastName(oneStopApp.getContact().getLastName());
			}
			if(customer.getPrimarySite().getSiteDetails().getOtherPhone() == null){
				customer.getPrimarySite().getSiteDetails().setOtherPhone(oneStopApp.getContact().getOtherPhone());
			}
			if(customer.getPrimarySite().getSiteDetails().getEmail() == null){
				customer.getPrimarySite().getSiteDetails().setEmail(oneStopApp.getContact().getEmail());
			}

		}
		// Create Guarantor as individual Customer
		Customer guarantorCustomer = oneStopApp.getGuarantorCustomer();
		if (guarantorCustomer == null && oneStopApp.getGuarantor() != null && StringUtils.isNotEmpty(oneStopApp.getGuarantor().getFirstName()) && StringUtils.isNotEmpty(oneStopApp.getGuarantor().getLastName())) {
			guarantorCustomer = (Customer) entityService.createNew(Customer.class);
			guarantorCustomer.setCustomerType((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "CUSTOMER_TYPE_INDIVIDUAL"));
			guarantorCustomer.setFirstName(oneStopApp.getGuarantor().getFirstName());
			guarantorCustomer.setLastName(oneStopApp.getGuarantor().getLastName());
			guarantorCustomer.setSsn(oneStopApp.getGuarantorSsn());
	
			CustomerSite gPrimarySite = (CustomerSite) entityService.createNew(CustomerSite.class);
			Contact gSiteDetails = (Contact) entityService.createNew(Contact.class);
			gPrimarySite.setSiteDetails(gSiteDetails);
			guarantorCustomer.setPrimarySite(gPrimarySite);
			gSiteDetails.setMobilePhone(oneStopApp.getContact().getMobilePhone());
			gSiteDetails.setOtherPhone(oneStopApp.getContact().getOtherPhone());
			gSiteDetails.setEmail(oneStopApp.getContact().getEmail());
			
			if (oneStopApp.getContact().getAddresses() != null) {
				for (Address oneStopAddress : oneStopApp.getContact().getAddresses()) {
					Address address = (Address) entityService.createNew(Address.class);
					gSiteDetails.addToAddresses(address);
					address.setAddress1(oneStopAddress.getAddress1());
					address.setAddress2(oneStopAddress.getAddress2());
					address.setCity(oneStopAddress.getCity());
					address.setStateProvince(oneStopAddress.getStateProvince());
					address.setCountry(oneStopAddress.getCountry());
					address.setUse(oneStopAddress.getUse());			
				}
			}
			entityService.saveOrUpdate(guarantorCustomer);
		}
		

		// Create Product
		Facility facility = (Facility) entityService.createNew(Facility.class);
		AttributeChoice productType = oneStopApp.getProductType();
		if (productType != null) {
			facility.setFacilityType(productType);
		} else {
			facility.setFacilityType((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "CREDIT_FACILITY_TYPE_COMMERCIAL_REAL_ESTATE"));
		}

		boolean cre = false;
		if (facility.getFacilityType() != null && "CREDIT_FACILITY_TYPE_COMMERCIAL_REAL_ESTATE".equals(facility.getFacilityType().getKey())) {
			cre = true;
		}

		if (cre) {
			facility.setFacilitySubType(oneStopApp.getFacilitySubType());
			facility.setProductPurpose(oneStopApp.getProductPurpose());
			facility.setNatureOfSecurity((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "NATURE_OF_SECURITY_SECURED"));
		} else {
			facility.setNatureOfSecurity((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "NATURE_OF_SECURITY_UNSECURED"));
		}		
		facility.setRequestedTerm(oneStopApp.getTerm());
		facility.setPurposeCode(oneStopApp.getPurposeCode());
		facility.getPricingOption().getStructure().setTerm(oneStopApp.getTerm());
		facility.getPricingOption().getStructure().setTermUnit((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "TERM_UNIT_MONTHS"));
		facility.getPricingOption().getStructure().setAmortization(oneStopApp.getAmortizationTerm());
		facility.getPricingOption().getStructure().setAmortizationUnit((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "TERM_UNIT_MONTHS"));

		facility.setRequestedLoanAmt(oneStopApp.getRequestedAmt());
		// Add primary borrower		
		FacilityCustomerRole pbRole = (FacilityCustomerRole) entityService.createNew(FacilityCustomerRole.class);
		pbRole.setFacility(facility);
		pbRole.setPartyRole((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "PARTY_ROLE_TYPE_BORROWER"));
		pbRole.setCustomer(customer);
		pbRole.setPrimaryBorrower(true);
		facility.addToFacilityCustomerRoles(pbRole);
		
		// Add primary contact
		if(!cre){
			FacilityCustomerRole pcRole = (FacilityCustomerRole) entityService.createNew(FacilityCustomerRole.class);
			pcRole.setFacility(facility);
			pcRole.setPartyRole((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "PARTY_ROLE_TYPE_AUTHORITY_SIGNER"));
			pcRole.setParentRole(pbRole);
			pcRole.setCustomer(contactCustomer);
			pcRole.setPrimaryContact(true);
			facility.addToFacilityCustomerRoles(pcRole);
		}
		facility.setRequest(request);
	
		request.addToAllFacilities(facility);
		// Add gurantor contact
		if (guarantorCustomer != null) {
			FacilityCustomerRole gRole = (FacilityCustomerRole) entityService.createNew(FacilityCustomerRole.class);
			gRole.setFacility(facility);
			gRole.setPartyRole((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "PARTY_ROLE_TYPE_GUARANTOR"));
			gRole.setParentRole(pbRole);
			gRole.setCustomer(guarantorCustomer);
			gRole.setPrimaryContact(false);
			facility.addToFacilityCustomerRoles(gRole);
		}
		//  Add Collateral
		BigDecimal totalNoi = BigDecimal.ZERO;
		if(!oneStopApp.getCollaterals().isEmpty()){
			for(Collateral collateral : oneStopApp.getCollaterals()){
				if(collateral.getNoi() != null){
					totalNoi = totalNoi.add(collateral.getNoi());
				}
				if(!collateral.getUseExistingCollateral()){
					Collateral newCollateral = (Collateral) entityService.createNew(Collateral.class);
					
					newCollateral.setCollateralName(collateral.getCollateralName());
					newCollateral.setCollateralCategory(collateral.getCollateralCategory());
					newCollateral.setCollateralType(collateral.getCollateralType());
					newCollateral.setCollateralDescription(collateral.getCollateralDescription());
					newCollateral.setLocationHeld(collateral.getLocationHeld());
					newCollateral.setNoi(collateral.getNoi());
					newCollateral.setCustomer(customer);
					Address location = (Address) entityService.createNew(Address.class);
					newCollateral.setLocation(location);
					location.setAddress1(collateral.getLocation().getAddress1());
					location.setAddress2(collateral.getLocation().getAddress2());
					location.setCity(collateral.getLocation().getCity());
					location.setStateProvince(collateral.getLocation().getStateProvince());
					location.setCountry(collateral.getLocation().getCountry());
					location.setCounty(collateral.getLocation().getCounty());
					location.setPostalCode(collateral.getLocation().getPostalCode());
					location.setPostalCodePlus4(collateral.getLocation().getPostalCodePlus4());
					
					facility.addToCollaterals(newCollateral);
					customer.addToCollaterals(newCollateral);
				}
			}
		}
		if (cre) {
			//key Pricing
			KeyPricing keyPricing = (KeyPricing) entityService.createNew(KeyPricing.class);
			keyPricing.setDayCountConvention(oneStopApp.getDayCountConvention());
			keyPricing.setAmortizationTerm(oneStopApp.getAmortizationTerm());
			keyPricing.setInterestRate(oneStopApp.getInterestRate());
			keyPricing.setCapRate(oneStopApp.getCapRate());
			keyPricing.setTotalNoi(totalNoi);
			keyPricing.setRequestedAmount(oneStopApp.getRequestedAmt());
			keyPricing.setRequestedTerm(oneStopApp.getTerm());
			keyPricing.setLoanAmount(oneStopApp.getRequestedAmt());
			request.setKeyPricing(keyPricing);
			if(customer.getKeyPricing() == null){
				customer.setKeyPricing(keyPricing);
			}
			//Product Pricing
			if(oneStopApp.getInterestRate() != null && oneStopApp.getCapRate() != null && oneStopApp.getDayCountConvention() != null ){
				PricingOption additionalOption = (PricingOption) entityService.createNew(PricingOption.class);
				Structure structure = (Structure) entityService.createNew(Structure.class);
				structure.setInterestRate(oneStopApp.getInterestRate());
				structure.setCapRate(oneStopApp.getCapRate());
				structure.setBasis(oneStopApp.getDayCountConvention());
				additionalOption.setStructure(structure);
				additionalOption.setFacility(facility);
				facility.addToAdditionalOptions(additionalOption);
			}
			
			
		}
		
		Account acc =  new Account();
		acc.setCustomer(customer);
		request.setAccount(acc);
		request.setOneStopApp(null);
		entityService.saveOrUpdate(request);
		entityService.flush();
		
	}
		
}
