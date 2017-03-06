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
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.AbstractBusinessOperation;
import com.thirdpillar.foundation.service.BusinessOperation;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;

@Component @Configurable
public class OneStopExposureRequestConverter {

	@Autowired 
	private EntityService entityService;
	
	
	public void convertOneStopExposureToRequest(BaseDataObject entity) {
		
		Request request = (Request) entity;
		OneStopExposure oneStopExposure = request.getOneStopExposure();
		request.setRequestType((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "REQUEST_TYPE_COUNTERPARTY_EXPOSURE_LIMIT"));
		request.setRequestName(oneStopExposure.getEntityName());

		Customer customer = oneStopExposure.getCounterParty();
		if (customer == null) {
			// Create Entity as CounterParty
			customer = (Customer) entityService.createNew(Customer.class);
			customer.setCustomerType((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "CUSTOMER_TYPE_COUNTER_PARTY"));
			customer.setLegalName(oneStopExposure.getEntityName());
			customer.setFederalTaxId(oneStopExposure.getFederalTaxId());
	
			CustomerSite primarySite = (CustomerSite) entityService.createNew(CustomerSite.class);
			Contact siteDetails = (Contact) entityService.createNew(Contact.class);
			primarySite.setSiteDetails(siteDetails);
			customer.setPrimarySite(primarySite);
			siteDetails.setOtherPhone(oneStopExposure.getContact().getOtherPhone());
			siteDetails.setEmail(oneStopExposure.getContact().getEmail());
			
			if (oneStopExposure.getContact().getAddresses() != null) {
				for (Address oneStopAddress : oneStopExposure.getContact().getAddresses()) {
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
			ct.setFirstName(oneStopExposure.getContact().getFirstName());
			ct.setLastName(oneStopExposure.getContact().getLastName());
			ct.setFirstName(oneStopExposure.getContact().getFirstName());
			ct.setMobilePhone(oneStopExposure.getContact().getMobilePhone());
			ct.setOtherPhone(oneStopExposure.getContact().getOtherPhone());
			ct.setEmail(oneStopExposure.getContact().getEmail());
			primarySite.addToContacts(ct);
			
			
			entityService.saveOrUpdate(customer);
		}
		
		// Create Contact as Individual customer		
		Customer contactCustomer = oneStopExposure.getContactCustomer();
		if (contactCustomer == null) {
			contactCustomer = (Customer) entityService.createNew(Customer.class);
			contactCustomer.setCustomerType((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "CUSTOMER_TYPE_INDIVIDUAL"));
			contactCustomer.setFirstName(oneStopExposure.getContact().getFirstName());
			contactCustomer.setLastName(oneStopExposure.getContact().getLastName());		
	
			CustomerSite bcPrimarySite = (CustomerSite) entityService.createNew(CustomerSite.class);
			Contact bcSiteDetails = (Contact) entityService.createNew(Contact.class);
			bcPrimarySite.setSiteDetails(bcSiteDetails);
			contactCustomer.setPrimarySite(bcPrimarySite);
			bcSiteDetails.setMobilePhone(oneStopExposure.getContact().getMobilePhone());
			bcSiteDetails.setOtherPhone(oneStopExposure.getContact().getOtherPhone());
			bcSiteDetails.setEmail(oneStopExposure.getContact().getEmail());
			
			if (oneStopExposure.getContact().getAddresses() != null) {
				for (Address oneStopAddress : oneStopExposure.getContact().getAddresses()) {
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
		
		// Create Product
		Facility facility = (Facility) entityService.createNew(Facility.class);
		facility.setFacilityType((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "CREDIT_FACILITY_TYPE_EXPOSURE_LIMITS"));
		
		AttributeChoice productType = oneStopExposure.getProductType();
		if (productType != null) {
			facility.setExposureLimitFacilityType(productType);
		}
		

		facility.setNatureOfSecurity((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "NATURE_OF_SECURITY_UNSECURED"));
		facility.setRequestedLoanAmt(oneStopExposure.getRequestedAmt());
		
		// Add primary borrower		
		FacilityCustomerRole pbRole = (FacilityCustomerRole) entityService.createNew(FacilityCustomerRole.class);
		pbRole.setFacility(facility);
		pbRole.setPartyRole((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "PARTY_ROLE_TYPE_BORROWER"));
		pbRole.setCustomer(customer);
		pbRole.setPrimaryBorrower(true);
		facility.addToFacilityCustomerRoles(pbRole);
		
		// Add primary contact
		FacilityCustomerRole pcRole = (FacilityCustomerRole) entityService.createNew(FacilityCustomerRole.class);
		pcRole.setFacility(facility);
		pcRole.setPartyRole((AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "PARTY_ROLE_TYPE_AUTHORITY_SIGNER"));
		pcRole.setParentRole(pbRole);
		pcRole.setCustomer(contactCustomer);
		pcRole.setPrimaryContact(true);
		facility.addToFacilityCustomerRoles(pcRole);
		facility.setRequest(request);
				
		request.addToAllFacilities(facility);
		
		Account acc =  new Account();
		acc.setCustomer(customer);
		request.setAccount(acc);
		request.setOneStopExposure(null);
		entityService.saveOrUpdate(request);
		entityService.flush();
		
	}
		
}
