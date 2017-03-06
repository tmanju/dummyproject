/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.thirdpillar.codify.loanpath.model.Address;
import com.thirdpillar.codify.loanpath.model.BureauReport;
import com.thirdpillar.codify.loanpath.model.Compliance;
import com.thirdpillar.codify.loanpath.model.Customer;
import com.thirdpillar.codify.loanpath.model.Facility;
import com.thirdpillar.codify.loanpath.model.FacilityCustomerRole;
import com.thirdpillar.codify.loanpath.model.Request;
import com.thirdpillar.foundation.codify.exception.ValidationException;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.rules.RuleLogger;
import com.thirdpillar.foundation.rules.RuleService;
import com.thirdpillar.foundation.service.AbstractBusinessOperation;
import com.thirdpillar.foundation.service.EntityService;

/**
 * DOCUMENT ME!
 * 
 * @author ENTER YOUR FULL NAME
 * @version 1.0
 * @since 1.0
 */

public class DeDupBizOp extends AbstractBusinessOperation {

	// ~ Static fields/initializers
	// -------------------------------------------------------------------------------------

	private static final Log LOG = LogFactory.getLog(DeDupBizOp.class);

	// ~ Instance fields
	// ------------------------------------------------------------------------------------------------

	@Autowired
	EntityService entityService;

	// ~ Methods
	// --------------------------------------------------------------------------------------------------------

	public Object execute(BaseDataObject entity, String operation,
			Object... params) {

		Request request = (Request) entity;

		// Get all customers from request.
		List<Customer> requestCustomers = request.getAllCustomers();

		String[] queryParams = new String[] { "customerSSN" };

		List<Customer> customerRemovalList = new ArrayList<Customer>();
		List<Customer> customerSaveList = new ArrayList<Customer>();

		if (requestCustomers != null && !requestCustomers.isEmpty()) {

			// iterating on request customers
			for (Customer requestCustomer : requestCustomers) {

				if (requestCustomer.getVerifiedForDeDup()) {
					// skip dedup for this customer
					continue;
				}
				if (requestCustomer.getBirthDate() == null) {
					// skip deduping
					continue;
				}
				// Creating query parameter values.
				String ssn = requestCustomer.getSsn();
				Object[] queryValues = new Object[] { ssn };

				// Get all customers from database with provided SSN number.
				List<Customer> existingCustomers = entityService
						.findByNamedQueryAndNamedParam(
								"com.thirdpillar.codify.loanpath.model.Customer.bySsn",
								queryParams, queryValues);

				// should only have one, lets only take the first one in the
				// list
				Customer existingCustomer = null;

				// ideally should only have to find max of one customer (plus
				// current request if save), take the first customer that isn't
				// the same instance as request customer
				for (Customer c : existingCustomers) {
					if (requestCustomer.getId() != null
							&& requestCustomer.getId().equals(c.getId())) {
						// same customer skip
						continue;
					} else {
						existingCustomer = c;
						break;
					}
				}

				if (existingCustomer != null) {
					// 1. if no then throw validation exception.
					// 2. If yes then run De-Dupe Process.
					if (requestCustomer.getBirthDate() == null
							|| existingCustomer.getBirthDate() == null) {
						// dob not provided on incoming or customer in database.
						// lets totally avoid deduping with this guy
						continue;
					} else if (!requestCustomer.getBirthDate().equals(
							existingCustomer.getBirthDate())) {

						// TODO: Message added for testing only.Need to add
						// appropriate message.
						LOG.error("SSN & DOB doesnot match with an existing customer, ideally this case shouldnot exist as it should have been caught in validation phase, "
								+ "Offending customers "
								+ requestCustomer.getId()
								+ " matching with"
								+ existingCustomer.getId());
						throw new ValidationException(
								" The SSN and the Date of Birth provided doesnot match with the existing records.");
					} else {

						// --------------- Executing De-Dupe Process
						// ---------------------.
						LOG.info("Executing Dedup for Customer - "
								+ requestCustomer.getId() + ", duplciate of - "
								+ existingCustomer.getId());

						// Marking duplicate customer which will be deleted from
						// Request.
						customerRemovalList.add(requestCustomer);

						// Merging data from existing customer to request
						// customer.
						existingCustomer.setFirstName(requestCustomer
								.getFirstName());
						existingCustomer.setLastName(requestCustomer
								.getLastName());
						existingCustomer.setSsn(requestCustomer.getSsn());
						existingCustomer.setBirthDate(requestCustomer
								.getBirthDate());
						existingCustomer.setDisclosureMethod(requestCustomer
								.getDisclosureMethod());
						existingCustomer.setDisclosuresDate(requestCustomer
								.getDisclosuresDate());
						existingCustomer
								.getPrimarySite()
								.getSiteDetails()
								.setEmail(requestCustomer.getPrimarySite()
												.getSiteDetails().getEmail());
						existingCustomer
								.getPrimarySite()
								.getSiteDetails()
								.setHomePhone(requestCustomer.getPrimarySite()
												.getSiteDetails()
												.getHomePhone());
						existingCustomer
								.getPrimarySite()
								.getSiteDetails()
								.setMobilePhone(requestCustomer.getPrimarySite()
												.getSiteDetails()
												.getMobilePhone());

						List<Address> requestCustomerAddresses = requestCustomer
								.getPrimarySite().getSiteDetails()
								.getAddresses();
						Address requestCustomerAddress = null;

						List<Address> exsitingCustomerAddresses = existingCustomer
								.getPrimarySite().getSiteDetails()
								.getAddresses();
						Address existingCustomerAddress = null;

						if (requestCustomerAddresses != null
								&& !requestCustomerAddresses.isEmpty()) {
							requestCustomerAddress = requestCustomerAddresses
									.get(0);
							if (exsitingCustomerAddresses != null
									&& !exsitingCustomerAddresses.isEmpty()) {
								existingCustomerAddress = exsitingCustomerAddresses
										.get(0);

								// ?? do we need to check for type, is it
								// necessary?
								// Check if address type of requestCustomer and
								// existingCustomer is Installation
								if (existingCustomerAddress.getUse() != null
										&& "ADDRESS_USE_TYPE_INSTALLATION".equals(existingCustomerAddress
														.getUse().get(0)
														.getKey())
										&& requestCustomerAddress.getUse() != null
										&& "ADDRESS_USE_TYPE_INSTALLATION".equals(requestCustomerAddress
														.getUse().get(0)
														.getKey())) {
									// --- Merge fields from requestCustomer to
									// existingCustomer

									existingCustomerAddress
											.setAddress1(requestCustomerAddress
													.getAddress1());
									existingCustomerAddress
											.setAddress2(requestCustomerAddress
													.getAddress2());
									existingCustomerAddress
											.setCity(requestCustomerAddress
													.getCity());
									existingCustomerAddress
											.setCounty(requestCustomerAddress
													.getCounty());
									existingCustomerAddress
											.setCountry(requestCustomerAddress
													.getCountry());
									existingCustomerAddress
											.setStateProvince(requestCustomerAddress
													.getStateProvince());
									existingCustomerAddress
											.setPostalCode(requestCustomerAddress
													.getPostalCode());
									existingCustomerAddress
											.setPostalCodePlus4(requestCustomerAddress
													.getPostalCodePlus4());
									existingCustomerAddress
											.setUse(requestCustomerAddress
													.getUse());
									existingCustomerAddress
											.setExternalIdentifier(requestCustomerAddress
													.getExternalIdentifier());
								}
							}
						}

						// Now Iterate on All Facilities associated to Request
						// and fetch FacilityCustomerRole and
						// replace the customer reference inside each
						// FacilityCustomerRole Object with existingCustomer.

						List<Facility> allFacilities = request
								.getAllFacilities();

						if (allFacilities != null && !allFacilities.isEmpty()) {
							for (Facility facility : allFacilities) {
								if (facility.getFacilityCustomerRoles() != null
										&& facility.getFacilityCustomerRoles()
												.size() > 0) {

									for (FacilityCustomerRole facilityCustomerRole : facility
											.getFacilityCustomerRoles()) {
										if (facilityCustomerRole.getCustomer()
												.getId() == requestCustomer
												.getId()) {
											facilityCustomerRole
													.setCustomer(existingCustomer);
										}
									}
								}
							}
						}

						// Now Iterate on All BureauReports associated to
						// Request and replace the customer reference with
						// existingCustomer having same SSN.

						List<BureauReport> bureauReports = request
								.getBureauReports();
						if (bureauReports != null && !bureauReports.isEmpty()) {
							for (BureauReport bureauReport : bureauReports) {
								if (bureauReport.getCustomer().getId() == requestCustomer
										.getId()) {
									bureauReport.setCustomer(existingCustomer);
								}
							}
						}

						List<Compliance> compliances = request.getCompliances();
						if (compliances != null && !compliances.isEmpty()) {
							for (Compliance compliance : compliances) {
								if (compliance.getCustomer().getId() == requestCustomer
										.getId()) {
									compliance.setCustomer(existingCustomer);
								}
							}
						}

						// Setting verification flag of existing customer.
						existingCustomer.setVerifiedForDeDup(true);

						// Marking existing customer which will be added to
						// Request after removal of duplicate customer.
						customerSaveList.add(existingCustomer);
					}
				} else {
					// If no customer found with matching SSN then set
					// VerifiedForDeDup=true .
					requestCustomer.setVerifiedForDeDup(true);
					customerSaveList.add(requestCustomer);
				}
			}
		}

		// remove customer that need to be remvoed
		if (!customerRemovalList.isEmpty()) {
			entityService.deleteAll(customerRemovalList);
		}

		for (Customer customer : customerSaveList) {
			entityService.saveOrUpdate(customer);
		}

		// Saving Request.
		entityService.saveOrUpdate(request);

		return "";
	}
}
