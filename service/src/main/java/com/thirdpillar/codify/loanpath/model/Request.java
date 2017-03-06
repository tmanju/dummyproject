/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.codify.loanpath.service.DroolsUtil;
import com.thirdpillar.codify.loanpath.util.BureauReportComparator;
import com.thirdpillar.codify.loanpath.util.CoaComparator;
import com.thirdpillar.codify.loanpath.util.ComplianceComparator;
import com.thirdpillar.codify.loanpath.util.UserExceptionAuthorityLevelComparator;
import com.thirdpillar.codify.loanpath.util.Utility;
import com.thirdpillar.foundation.component.CodifyMessage;
import com.thirdpillar.foundation.integration.IntegrationExchange;
import com.thirdpillar.foundation.integration.IntegrationResult;
import com.thirdpillar.foundation.model.*;
import com.thirdpillar.foundation.service.ContextHolder;
import com.thirdpillar.foundation.service.EntityService;

/**
 * Model Class for Request
 * 
 * @author SHIVA NAND SINGH
 * @version 1.0
 * @since 1.0
 */
public class Request extends BaseDataObject {

	// ~ Static fields/initializers
	// -------------------------------------------------------------------------------------
	private static final Logger LOGGER = Logger.getLogger(Request.class);
	
	/** Use serialVersionUID for interoperability. */
	private static final long serialVersionUID = -2616358184370623909L;
	
	// ~ Methods
	// --------------------------------------------------------------------------------------------------------

	/**
	 * @return the list of facilities for which COA needs to be implemented
	 */
	public List<Facility> getCoaFacilities() {

		List<Facility> list = new ArrayList<Facility>();
		for (Facility fac : getAllFacilities()) {

			if (!fac.inWfStatus("FACILITY_STATUS_WITHDRAWN",
					"FACILITY_STATUS_DECLINED")) {
				if (fac.getCoasetDefinition() != null
						&& fac.getCoasetDefinition().getCoaEvls() != null) {
					java.util.Collections
							.sort(fac.getCoasetDefinition().getCoaEvls(),
									new com.thirdpillar.codify.loanpath.util.CoaComparator());
				}
				list.add(fac);
			}
		}
		return list;
	}

	/**
	 * @return the list of facilities for which COA needs to be implemented
	 */
	public List<CoaEvaluation> getCoaEvalFacilitiesDer() {

		List<CoaEvaluation> list = new LinkedList<CoaEvaluation>();
		if(!getCoaFacilities().isEmpty()){
			CoasetDefinition coasetDefinition = getCoaFacilities().get(0).getCoasetDefinition();
			if(coasetDefinition !=null){
				List<CoaEvaluation> coaEvals = coasetDefinition.getCoaEvls();
				if(!coaEvals.isEmpty()){
					for (CoaEvaluation coaEval : coaEvals) {
						list.add(coaEval);
					}
				}
			}	
			
		}
		
		Collections.sort(list, new CoaComparator());
		return list;
	}
	
	
	/**
	 * @return the list of facilities for which COA needs to be implemented
	 */
	public List<Facility> getDecisionFacilities() {
		List<Facility> list = new ArrayList<Facility>();
		for (Facility fac : getAllFacilities()) {
			if (!fac.inWfStatus("FACILITY_STATUS_WITHDRAWN")) {
				list.add(fac);
			}
		}
		return list;
	}

	/**
	 * @return the list of facilities for which COA needs to be implemented
	 */
	public List<Facility> getSrcFacilities() {
		List<Facility> list = new ArrayList<Facility>();
		for (Facility fac : getAllFacilities()) {
			if (!fac.inWfStatus("FACILITY_STATUS_WITHDRAWN")) {
				list.add(fac);
			}
		}
		return list;
	}

	public List<Facility> getAllActiveFacilities() {
		List<Facility> list = new ArrayList<Facility>();
		for (Facility fac : getAllFacilities()) {
			if (!fac.inWfStatus("FACILITY_STATUS_WITHDRAWN")) {
				list.add(fac);
			}
		}
		return list;
	}

	public List<Facility> getAllApprovedFacilities() {
		List<Facility> list = new ArrayList<Facility>();
		for (Facility fac : getAllFacilities()) {
			if (fac.inWfStatus("FACILITY_STATUS_APPROVED")) {
				list.add(fac);
			}
		}
		return list;
	}

	public List<Facility> getAllPendingFacilities() {
		List<Facility> list = new ArrayList<Facility>();
		for (Facility fac : getAllFacilities()) {
			if (fac.inWfStatus("FACILITY_STATUS_PENDING")) {
				list.add(fac);
			}
		}
		return list;
	}

	/**
	 * Return RAC Exception Authority
	 */
	public String getRacExceptionAuth() {
		String racExceptionAuth = null;

		List<String> ls = new ArrayList<String>();
		for (RacsetEvaluation racSetEval : getAllRACSetEvaluations()) {
			if (racSetEval.getExceptionAuthorityValue() != null) {
				ls.add(racSetEval.getExceptionAuthorityValue());
			}
		}

		if (!ls.isEmpty()) {
			Collections.sort(ls);
			racExceptionAuth = ls.get(ls.size() - 1);
		}

		return racExceptionAuth;
	}

	public List<CustomerFacilityExposure> getDirectFacilityExposures() {
		List<CustomerFacilityExposure> exposures = new ArrayList<CustomerFacilityExposure>();
		if (getExposure() != null) {
			for (FacilityCustomerRole facilityCustomerRole : getAllCustomerRoles()) {
				if (facilityCustomerRole.getPartyRole() != null
						&& (StringUtils.equals(facilityCustomerRole
								.getPartyRole().getKey(),
								Constants.PARTY_ROLE_TYPE_CLIENT_KEY))) {
					CustomerFacilityExposure customerFacilityExposure = new CustomerFacilityExposure();
					customerFacilityExposure
							.setCustomerRole(facilityCustomerRole);
					List<FacilityExposure> facilityExposures = getExposure()
							.findFacilityExposures(
									facilityCustomerRole.getCustomer(), true);
					if (facilityExposures != null) {
						for (FacilityExposure facilityExposure : facilityExposures) {
							customerFacilityExposure
									.addToFacilityExposures(facilityExposure);
						}
					}
					exposures.add(customerFacilityExposure);
				}
			}
		}
		return exposures;
	}

	
	public List<CustomerFacilityExposure> getGuarantorFacilityExposures() {

		List<CustomerFacilityExposure> exposures = new ArrayList<CustomerFacilityExposure>();
		if (getExposure() != null) {
			for (FacilityCustomerRole facilityCustomerRole : getAllCustomerRoles()) {
				if (facilityCustomerRole.getPartyRole() != null
						&& (facilityCustomerRole.getPartyRole() != null
								&& (StringUtils.equals(facilityCustomerRole
										.getPartyRole().getKey(),
										Constants.PARTY_ROLE_TYPE_GUARANTOR_KEY)))) {

					CustomerFacilityExposure customerFacilityExposure = new CustomerFacilityExposure();
					customerFacilityExposure
							.setCustomerRole(facilityCustomerRole);
					List<FacilityExposure> facilityExposures = getExposure()
							.findFacilityExposures(
									facilityCustomerRole.getCustomer(), false);
					if (facilityExposures != null) {
						for (FacilityExposure facilityExposure : facilityExposures) {
							customerFacilityExposure
									.addToFacilityExposures(facilityExposure);
						}
					}
					exposures.add(customerFacilityExposure);
				} 
			}
		}

		return exposures;

	}
	
	public List<CustomerFacilityExposure> getDebtorFacilityExposures() {

		List<CustomerFacilityExposure> exposures = new ArrayList<CustomerFacilityExposure>();
		if (getExposure() != null) {
			for (FacilityCustomerRole facilityCustomerRole : getAllCustomerRoles()) {
				if (facilityCustomerRole.getPartyRole() != null
						&& (facilityCustomerRole.getPartyRole() != null
								&& (StringUtils.equals(facilityCustomerRole
										.getPartyRole().getKey(),
										Constants.PARTY_ROLE_TYPE_DEBTOR_KEY)))) {
							CustomerFacilityExposure customerFacilityExposure = new CustomerFacilityExposure();
							customerFacilityExposure
									.setCustomerRole(facilityCustomerRole);
							customerFacilityExposure.setExposure(getExposure());
							List<FacilityExposure> facilityExposures = getExposure()
									.findFacilityExposures(
											facilityCustomerRole.getCustomer(), false);
							if (facilityExposures != null) {
								for (FacilityExposure facilityExposure : facilityExposures) {
									customerFacilityExposure.addToFacilityExposures(facilityExposure);
								}
							}
							exposures.add(customerFacilityExposure);
					}
			}
		}

		return exposures;

	}
	
	public boolean hasFacilityWithType(String facilityType) {
		for (Facility fac : getAllFacilities()) {
			if (!fac.inWfStatus("FACILITY_STATUS_WITHDRAWN")) {
				if (fac.getFacilityType() != null
						&& StringUtils.equals(fac.getFacilityType().getKey(),
								facilityType)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @return
	 */
	public Approver getHighestRacExcepAuth() {
		List<ApprovalLevel> approvalLevels = getApproval().getApprovalLevels();
		List<Approver> highestAuthLevelApprovers = new ArrayList<Approver>();

		for (ApprovalLevel approvalLevel : approvalLevels) {
			List<Approver> approvers = new ArrayList<Approver>();
			if (approvalLevel.getApproverList() != null) {
				approvers.addAll(approvalLevel.getApproverList());
			}

			if (approvers.isEmpty()) {
				Collections.sort(approvers,
						new UserExceptionAuthorityLevelComparator());
				highestAuthLevelApprovers
						.add(approvers.get(approvers.size() - 1));
			}
		}

		Collections.sort(highestAuthLevelApprovers,
				new UserExceptionAuthorityLevelComparator());
		if (highestAuthLevelApprovers.isEmpty()) {
			return highestAuthLevelApprovers.get(highestAuthLevelApprovers
					.size() - 1);
		} else {
			return null;
		}
	}

	/**
	 * This method will check whether all the approvers in the Final Approval
	 * Level has a RacExceptionAuthority >= RacExceptionAuthority of
	 * Application.
	 * 
	 * @param appRacExceptionAuthority
	 * @return
	 */
	public boolean isFinalLevelApproversAuthorized(int appRacExceptionAuthority) {
		boolean result = true;

		Approval approval = getApproval();

		if (approval != null && approval.getOrderedApprovalLevels() != null
				&& approval.getOrderedApprovalLevels().size() > 0) {
			// getting final Approval Level.
			ApprovalLevel finalApprovalLevel = approval
					.getOrderedApprovalLevels().get(
							approval.getOrderedApprovalLevels().size() - 1);

			if (finalApprovalLevel != null
					&& finalApprovalLevel.getApproverList() != null
					&& finalApprovalLevel.getApproverList().size() > 0) {

				for (Approver approver : finalApprovalLevel.getApproverList()) {
					// Setting approverRacExceptionAuthority=0 which indicates
					// that Approver's RacExceptionAuthority is NULL.
					int approverRacExceptionAuthority = 0;

					// Checking whether user is having a valid
					// RacExceptionAuthority.
					if (approver.getUser() != null
							&& approver.getUser().getRacExceptionAuthority() != null) {
						if (approver.getUser().getRacExceptionAuthority()
								.getValue() != null
								&& !("".equals(approver.getUser()
												.getRacExceptionAuthority().getValue()))) {
							approverRacExceptionAuthority = Integer
									.parseInt(approver.getUser()
											.getRacExceptionAuthority()
											.getValue().split(" ")[1]);
						}
					}

					if (approverRacExceptionAuthority < appRacExceptionAuthority) {
						result = false;
						break;
					}
				}

			} else {
				// indicates that there are no approvers in the Final Approval
				// Level.
				result = false;
			}
		} else {
			// indicates that there are no Approval Levels.
			result = false;
		}

		return result;
	}

	public List<Customer> getAllCustomers() {
		List<Customer> customers = new ArrayList<Customer>();
		for (Facility facility : getAllActiveFacilities()) {
			for (FacilityCustomerRole customerRole : facility
					.getFacilityCustomerRoles()) {
				if (customerRole.getCustomer() != null
						&& !customers.contains(customerRole.getCustomer())) {
					customers.add(customerRole.getCustomer());
				}
				for(DebtorCustomer dc : facility.getDebtors()){
					if(dc.getFacilityCustomerRole() != null && dc.getFacilityCustomerRole().getCustomer() != null && !customers.contains(dc.getFacilityCustomerRole().getCustomer())){
						customers.add(dc.getFacilityCustomerRole().getCustomer());
					}
				}
			}
		}
		return customers;
	}
	
	public List<DebtorCustomer> getAllDebtorsDer(){

		List<DebtorCustomer> debtorCustomers = new ArrayList<DebtorCustomer>();
		for (Facility facility : getAllActiveFacilities()) {
				for(DebtorCustomer dc : facility.getDebtors()){
					debtorCustomers.add(dc);
				}
			}
		return debtorCustomers;
	}
	
	public List<FacilityCustomerRole> getAllFacilityCustomerRoles() {
		List<FacilityCustomerRole> customers = new ArrayList<FacilityCustomerRole>();
		for (Facility facility : getAllActiveFacilities()) {
			for (FacilityCustomerRole customerRole : facility
					.getFacilityCustomerRoles()) {
				if (customerRole.getCustomer() != null
						&& !customers.contains(customerRole.getCustomer())) {
					customers.add(customerRole);
				}
			}
				for(DebtorCustomer dc : facility.getDebtors()){
					if(dc.getFacilityCustomerRole() != null && dc.getFacilityCustomerRole().getCustomer() != null && !customers.contains(dc.getFacilityCustomerRole().getCustomer())){
						customers.add(dc.getFacilityCustomerRole());
					}
				}
			}
		
		return customers;
	}

	public List<FacilityCustomerRole> getAllCustomerRoles() {
		List<FacilityCustomerRole> customerRoles = new ArrayList<FacilityCustomerRole>();
		for (Facility facility : getAllActiveFacilities()) {
			for (FacilityCustomerRole customerRole : facility
					.getFacilityCustomerRoles()) {
				customerRoles.add(customerRole);
			}
			
			for (DebtorCustomer debtor : facility.getDebtors()) {
				customerRoles.add(debtor.getFacilityCustomerRole());
			}
		}
		return customerRoles;
	}

	public List<FacilityCustomerRole> getAllCustomerRolesInPendingFacilities() {
		List<FacilityCustomerRole> customerRoles = new ArrayList<FacilityCustomerRole>();
		for (Facility facility : getAllPendingFacilities()) {
			for (FacilityCustomerRole customerRole : facility
					.getFacilityCustomerRoles()) {
				customerRoles.add(customerRole);
			}
		}
		return customerRoles;
	}

	public List<FacilityCustomerRole> getAllCustomerRolesInRoles(
			String... roles) {
		List<FacilityCustomerRole> customerRoles = new ArrayList<FacilityCustomerRole>();
		for (Facility facility : getAllActiveFacilities()) {
			for (FacilityCustomerRole customerRole : facility
					.getFacilityCustomerRoles()) {
				if (customerRole.getPartyRole() != null
						&& customerRole.getCustomer() != null) {
					for (String role : roles) {
						if (StringUtils.equals(role, customerRole
								.getPartyRole().getKey())) {
							customerRoles.add(customerRole);
						}
					}
				}
			}
		}
		return customerRoles;
	}

	public Set<Customer> getAllCustomersInRoles(String... roles) {
		Set<Customer> customers = new HashSet<Customer>();
		for (FacilityCustomerRole customerRole : getAllCustomerRoles()) {
			if (customerRole.getPartyRole() != null
					&& customerRole.getCustomer() != null) {
				for (String role : roles) {
					if (StringUtils.equals(role, customerRole.getPartyRole()
							.getKey())) {
						customers.add(customerRole.getCustomer());
					}
				}
			}
		}
		return customers;
	}

	public List<Customer> getAllCustomersInRolesAsList(String... roles) {
		return new ArrayList<Customer>(getAllCustomersInRoles(roles));
	}

	public BigDecimal getTotalRequestedLoanAmt() {
		BigDecimal total = BigDecimal.ZERO;
		for (Facility facility : getAllActiveFacilities()) {
			if (facility.getRequestedLoanAmt() != null) {
				total = total.add(facility.getRequestedLoanAmt());
			}
		}
		return Utility.formatBigDecimal(total);
	}

	public List<BureauReport> getLatestBureauReports(Customer customer,
			String... bureauNames) {
		List<BureauReport> latestReports = new ArrayList<BureauReport>();
		for (String bureauName : bureauNames) {
			BureauReport report = getLatestBureauReport(customer, bureauName);
			if (report != null) {
				latestReports.add(report);
			}
		}
		return latestReports;
	}

	public BureauReport getLatestBureauReport(Customer customer,
			String bureauName) {
		List<BureauReport> reports = getBureauReports();
		List<BureauReport> orderedReports = new ArrayList<BureauReport>(reports);

		// Descending order of reports based on the pullTime
		Collections.sort(orderedReports, new BureauReportComparator());
		Collections.reverse(orderedReports);

		for (BureauReport report : orderedReports) {
			if (StringUtils.equals(bureauName, report.getBureauName())
					&& report.getCustomer().getId() == customer.getId()) {
				return report;
			}
		}
		return null;
	}

	public Map<String, BureauReport> getLatestBureauReports(Customer customer) {
		List<BureauReport> reports = getBureauReports();
		List<BureauReport> orderedReports = new ArrayList<BureauReport>(reports);

		// Descending order of reports based on the pullTime
		Collections.sort(orderedReports, new BureauReportComparator());
		Collections.reverse(orderedReports);

		Map<String, BureauReport> latestReports = new HashMap<String, BureauReport>();
		for (BureauReport report : orderedReports) {
			if (latestReports.get(report.getBureauName()) == null
					&& report.getCustomer().getId() == customer.getId()) {
				latestReports.put(report.getBureauName(), report);
			}
		}
		return latestReports;
	}

	public Map<String, Compliance> getRecentCompliances(Customer customer) {
		List<Compliance> compliances = getCompliances();
		Map<String, Compliance> recentCompliances = new HashMap<String, Compliance>();

		if (compliances != null && compliances.isEmpty()) {
			List<Compliance> orderedCompliances = new ArrayList<Compliance>(
					compliances);

			// Descending order of Compliances based on the complianceCheckDttm
			Collections.sort(orderedCompliances, new ComplianceComparator());
			Collections.reverse(orderedCompliances);

			for (Compliance compliance : orderedCompliances) {
				if (compliance.getCategory() != null
						&& compliance.getCategory().getKey() != null
						&& recentCompliances.get(compliance.getCategory()
								.getKey()) == null
						&& compliance.getCustomer().getId() == customer.getId()) {
					recentCompliances.put(compliance.getCategory().getKey(),
							compliance);
				}
			}
		}
		return recentCompliances;
	}

	/**
	 * This method will return true if bureau report of customer exist otherwise
	 * return false.
	 * 
	 * @param customer
	 *            : Customer.
	 * @return : boolean
	 */
	public boolean isBureauReportExist(Customer customer) {

		for (BureauReport report : getBureauReports()) {
			if (report.getCustomer() != null
					&& report.getCustomer().getId().equals(customer.getId())) {

				if (report.getReportStatus() != null
						&& ("BUREAU_REPORT_STATUS_HIT".equalsIgnoreCase(report.getReportStatus().getKey()) || 
								"BUREAU_REPORT_STATUS_REUSED".equalsIgnoreCase(report
										.getReportStatus().getKey()))) {
					return true;
				}
			}
		}

		return false;
	}

	public List<CovenantEvaluation> getCovenantEvaluations() {

		List<CovenantEvaluation> list = new ArrayList<CovenantEvaluation>();
		for (Covenant covenant : getCovenants()) {

			for (CovenantEvaluation evaluation : covenant
					.getCovenantEvaluations()) {
				list.add(evaluation);
			}
		}
		return list;
	}

	/**
	 * It checks if any of the facilities/products currently present in
	 * request/application is about to expire or not. <br />
	 * <b>Note: </b> Request has to be in work flow status Portfolio
	 * Managment(implies request has been proceesed completely and is managed in
	 * back office) and only those Facilities which are in Approved((as
	 * facilities in Approved status would only qualify for renewal check any
	 * other status would be considered as under process) status will be
	 * considered for renewal check .
	 * 
	 * @param days
	 * @return true if any one of the facilities is about to expire.
	 */
	public boolean isAnyFacilityRenewalExpected() {
		return getFacilitiesPendingRenewal().size() > 0;
	}

	public List getFacilitiesPendingRenewal() {

		List renewalFacilities = new ArrayList();

		if (inWfStatus("REQUEST_STATUS_PORTFOLIO_MANAGMENT")
				&& getAllActiveFacilities() != null) {
			for (Facility facility : getAllActiveFacilities()) {
				if (facility.inWfStatus("FACILITY_STATUS_APPROVED")
						&& facility.isRenewalDue(60)) {
					renewalFacilities.add(facility);
				}
			}
		}
		return renewalFacilities;
	}

	public boolean isRequestType(String key) {

		if ((getRequestType() != null)
				&& key.equalsIgnoreCase(getRequestType().getKey())) {
			return true;
		}
			return false;
	}

	public String getNullSafeNameFromAccountDer() {
		String customerName = "";
		if (getAccount() != null) {
			customerName = getAccount().getCustomer().getCustomerName();
		}
		return customerName;
	}

	public String getNullSafeNameFromRelationshipDer() {
		String relationshipName = "";
		if (getRelationship() != null) {
			relationshipName = getRelationship().getName();
		}
		return relationshipName;
	}

	public boolean isFacilityTypeCRE() {
		for (Facility facility : getAllFacilities()) {
			if ("CREDIT_FACILITY_TYPE_COMMERCIAL_REAL_ESTATE".equalsIgnoreCase(
							facility.getFacilityType().getKey())) {
				return true;
			}
		}
		return false;

	}

	public boolean isFacilityTypeLease() {
		for (Facility facility : getAllFacilities()) {
			if ("CREDIT_FACILITY_TYPE_LEASE".equalsIgnoreCase(facility.getFacilityType().getKey())) {
				return false;
			}
		}
		return true;

	}

	/**
	 * Method checks if application has a particular facility requested by the
	 * customer.
	 * 
	 * @paramter1 Facility Type to check if application has this facility
	 *            requested.
	 * 
	 * @return boolean as indicator
	 */
	public boolean isFacilityrequested(String facilityType) {
		boolean flag = false;

		for (Facility fac : getAllFacilities()) {

			if ((fac.getFacilityType() != null)
					&& StringUtils.equals(facilityType, fac.getFacilityType()
							.getKey())) {
				flag = true;

				break;
			}
		}

		return flag;
	}

	/**
	 * This method checks that application is small ticket or not.
	 * 
	 * @return boolean as true if application is small ticket otherwise false.
	 */
	public boolean isSmallTicket() {
		boolean flag = false;

		if ((getRequestType() != null)
				&& Constants.APPLICATION_TYPE_SMALL_TICKET_KEY
						.equalsIgnoreCase(getRequestType().getKey())) {
			flag = true;
		}

		return flag;
	}

	/**
	 * This method checks that application is standard ticket or not.
	 * 
	 * @return boolean as true if application is standard ticket otherwise
	 *         false.
	 */
	public boolean isStandardTicket() {
		boolean flag = false;

		if ((getRequestType() != null)
				&& Constants.APPLICATION_TYPE_STANDARD_TICKET_KEY
						.equalsIgnoreCase(getRequestType().getKey())) {
			flag = true;
		}

		return flag;
	}

	public AttributeChoice getRequestTypeDer() {

		return getRequestType();

	}

	public String getRequestNameDer() {

		return getRequestName();
	}

	public String getAccountDer() {
		String name = "";
		if (getAccount() != null){
			name = getAccount().getCustomer().getCustomerName();
		}
		return name;
	}

	public Date getReceivedDateDer() {

		return getReceivedDate();
	}

	public Date getModificationDateDer() {

		return getModificationDate();
	}

	public Date getRegbDttmDer() {

		return getRegbDttm();
	}

	public Date getExpectedCloseDateDer() {

		return getExpectedCloseDate();
	}

	public String getLastModifiedByUserDer() {
		String name = "";
		if (getMetaInfo() != null
				&& getMetaInfo().getLastModifiedByUser() != null
				&& getMetaInfo().getCreatedByUser().getContact() != null) {
			name = getFirstNameAndLastName(getMetaInfo()
					.getLastModifiedByUser().getContact());
		}
		return name;
	}

	public String getCreatedByUserDer() {

		String name = "";
		if (getMetaInfo() != null && getMetaInfo().getCreatedByUser() != null
				&& getMetaInfo().getCreatedByUser().getContact() != null) {
			name = getFirstNameAndLastName(getMetaInfo().getCreatedByUser()
					.getContact());
		}
		return name;

	}
	
	public String getFirstNameAndLastName(Contact contact) {
		return contact.getFirstName() + " " + contact.getLastName();
	}
    public Scoring getLatestAppScoring() {
        Scoring latestScoring = null;

        if (this.getAppScorings() != null) {
            List<Scoring> scorings = this.getAppScorings();

            for (Scoring scoring : scorings) {

                if (latestScoring == null) {
                    latestScoring = scoring;
                }

                if (latestScoring.getEvalDttm().compareTo(scoring.getEvalDttm()) < 0) {
                    latestScoring = scoring;
                }
            }

            return latestScoring;
        }

        return null;
    }
    public List<Facility> getAllFacilitiesApplicableForRanking() {
        List<Facility> list = new ArrayList<Facility>();

        for (Facility fac : getAllFacilities()) {

            if (fac.getFacilityType() != null){
                list.add(fac);
            }
        }

        return list;
    }
    public User getLoggedInUser(){
    	return (User)ContextHolder.getContext().getUser();
    }

    public Date getReceivedDateAddedSixtyDateDer(){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(getReceivedDate());
		calendar.add(Calendar.DATE,60);
		return calendar.getTime();
    }
    
    /**
     * Method validating the account field
     * Counter Party selected in account field should not
     * be same as any counter party acting as guarantor/debtor
     * in Application
     * @return
     */
    public boolean validateAccount(){
    	boolean flag = true;
    	if(getAccount().getCustomer() != null){
    		Customer customer = getAccount().getCustomer();
        	for(Facility facility : this.getAllFacilities()){
        		for(FacilityCustomerRole customerRole : facility.getFacilityCustomerRoles()){
        			if(!customerRole.getPartyRole().getKey().equals(Constants.PARTY_ROLE_TYPE_CLIENT_KEY) && customerRole.getCustomer() != null){
        				if(customer.getRefNumber().equals(customerRole.getCustomer().getRefNumber())){
        					flag = false;
        					break;
        				}
        			}
        		}
        	}
    	}
    	
    	return flag;
    }
    
    public Facility fetchCurrentFacility(){
    	return (Facility)ContextHolder.getContext().getNamedContext().get("root_allFacilities");
    }
    
    public void mergeAkritivResponse(Request request, java.lang.String rStatus, IntegrationExchange integrationExchangeObj){
		EntityService es = new EntityService();
		
		 if ("ERROR".equals(rStatus)) {
			 LOGGER.error("******* Call Failed");
			 CodifyMessage.addMessage("ERR_INT_HTTP_RESP_ERROR",CodifyMessage.Severity.ERROR, new String[]{"Akritiv service"});
			 CodifyMessage.addMessage("ERR_INT_HTTP_REQ_FAIL",CodifyMessage.Severity.ERROR, new String[]{"Akritiv service"});
			 
			 ServiceMessage serviceMessage = (ServiceMessage)es.createNew(ServiceMessage.class);
			 serviceMessage.setMessage("Integration call failed for Akritiv service.");
			 serviceMessage.setStatus(rStatus);
			 if(request.getServiceMessages() != null){
				 request.getServiceMessages().add(serviceMessage); 
			 }else{
				 List<ServiceMessage> serviceMessages = new ArrayList<ServiceMessage>();
				 serviceMessages.add(serviceMessage);
				 request.setServiceMessages(serviceMessages);
			 }
			 CodifyMessage.addMessage("ERR_EXEC_INT_SERVICE",CodifyMessage.Severity.ERROR, new String[]{"Akritiv service",serviceMessage.getMessage()});
		 }else{
			 Object object = integrationExchangeObj.getTaskOutput();
		List<Customer> customers = new ArrayList<Customer>();
		if(object instanceof Request){
			LOGGER.info("**************Accept Akritiv response************************");
			Request responseObj = (Request)object;
			/**
			 * Saving servicing identifier for account
			 */
			if(responseObj.getAccount() != null){
				request.getAccount().setServicingIdentifier(responseObj.getAccount().getServicingIdentifier());
			}
				for(Facility facility : responseObj.getAllFacilities()){
					for(Facility f : request.getAllFacilities()){
						
						if(f.getRefNumber().equals(facility.getRefNumber())){
							/**
							 * Saving servicing identifier for facility
							 */
							f.setServicingIdentifier(facility.getServicingIdentifier());
							
							/**
							 * Saving servicing identifier for facility customer roles
							 */
							for(FacilityCustomerRole facilitycustomerRole : facility.getFacilityCustomerRoles()){
								for(FacilityCustomerRole fcr : f.getFacilityCustomerRoles()){
									if(facilitycustomerRole.getCustomer() != null && facilitycustomerRole.getCustomer().getRefNumber() != null && facilitycustomerRole.getCustomer().getRefNumber().equals(fcr.getCustomer().getRefNumber())){
										fcr.getCustomer().setServicingIdentifier(facilitycustomerRole.getCustomer().getServicingIdentifier());
										if(fcr.getCustomer().getPrimarySite() != null && fcr.getCustomer().getPrimarySite().getSiteDetails() != null && facilitycustomerRole.getCustomer().getPrimarySite() != null && facilitycustomerRole.getCustomer().getPrimarySite().getSiteDetails() != null 
												&& fcr.getCustomer().getPrimarySite().getSiteDetails().getRefNumber().equals(facilitycustomerRole.getCustomer().getPrimarySite().getSiteDetails().getRefNumber())){
											fcr.getCustomer().getPrimarySite().getSiteDetails().setServicingIdentifier(facilitycustomerRole.getCustomer().getPrimarySite().getSiteDetails().getServicingIdentifier());
										}
										if(facilitycustomerRole.getCustomer().getBankTrades() != null && fcr.getCustomer().getBankTrades() != null){
											
											for(BankAndTrade bankAndTrade : facilitycustomerRole.getCustomer().getBankTrades()){
												
												for(BankAndTrade b : fcr.getCustomer().getBankTrades()){
													if(b.getRefNumber().equals(bankAndTrade.getRefNumber())){
														b.setServicingIdentifier(bankAndTrade.getServicingIdentifier());
													}
												}
											}
										}
										customers.add(fcr.getCustomer());
									}
									
									/**
									 * Saving for primary contact
									 */
									if(facilitycustomerRole.getPrimaryContact() && facilitycustomerRole.getCustomer() != null && fcr.getPrimaryContact()){
										if(facilitycustomerRole.getCustomer().getPrimarySite() != null && facilitycustomerRole.getCustomer().getPrimarySite().getSiteDetails() != null
											&& 	facilitycustomerRole.getCustomer().getPrimarySite().getSiteDetails().getRefNumber().equals(fcr.getCustomer().getPrimarySite().getSiteDetails().getRefNumber())){
											fcr.getCustomer().getPrimarySite().getSiteDetails().setServicingIdentifier(facilitycustomerRole.getCustomer().getPrimarySite().getSiteDetails().getServicingIdentifier());
										}										
									}
								}
								LOGGER.info(">>>>>>>>>Debtors size>>>>>>>>>>>>"+f.getDebtors());
								for(DebtorCustomer dc : f.getDebtors()){
									if(facilitycustomerRole != null){
										LOGGER.info(">>>>>>>>>facilitycustomerRole>>>>>>>>>>>>"+facilitycustomerRole);
									}
									if(facilitycustomerRole.getCustomer() != null){
										LOGGER.info(">>>>>>>>>facilitycustomerRole.getCustomer()>>>>>>>>>>>>"+facilitycustomerRole.getCustomer());
									}
									if(dc != null && dc.getFacilityCustomerRole() != null && facilitycustomerRole != null && facilitycustomerRole.getCustomer() != null && facilitycustomerRole.getCustomer().getRefNumber() != null && dc.getFacilityCustomerRole().getCustomer().getRefNumber().equals(facilitycustomerRole.getCustomer().getRefNumber())){
										dc.getFacilityCustomerRole().getCustomer().setServicingIdentifier(facilitycustomerRole.getCustomer().getServicingIdentifier());
										if(facilitycustomerRole.getCustomer().getPrimarySite() != null && facilitycustomerRole.getCustomer().getPrimarySite().getSiteDetails() != null){
											dc.getFacilityCustomerRole().getCustomer().getPrimarySite().getSiteDetails().setServicingIdentifier(facilitycustomerRole.getCustomer().getPrimarySite().getSiteDetails().getServicingIdentifier());
										}
										customers.add(dc.getFacilityCustomerRole().getCustomer());
									}
								}
								
							}
							
							/**
							 * Saving servicing identifier for DebtorCustomers
							 */
							LOGGER.info(">>>>>>>>>Response Debtors size>>>>>>>>>>>>"+facility.getDebtors());
							for(DebtorCustomer debtors : facility.getDebtors()){
									for(DebtorCustomer dc : f.getDebtors()){
										if(dc.getRefNumber().equals(debtors.getRefNumber())){
											for(PartyDba dba : dc.getDbas()){
												for(PartyDba partyDba : debtors.getDbas()){
													if(dba.getRefNumber().equals(partyDba.getRefNumber())){
														dba.setServicingIdentifier(partyDba.getServicingIdentifier());
													}
												}
											}
											dc.setServicingIdentifier(debtors.getServicingIdentifier());
										}
									}
								}
						}
					}
					
				}
			
				StringBuffer buffer = new StringBuffer();
				if(request.getServiceMessages() != null){
					request.getServiceMessages().clear();
				}
				if(responseObj.getServiceMessages() != null){
					request.getServiceMessages().addAll(responseObj.getServiceMessages());
					for(ServiceMessage msg : responseObj.getServiceMessages()){
						buffer.append(msg.getMessage());
						buffer.append("\n");
					}
				}
				
					es.saveOrUpdate(request);
					es.saveOrUpdateAll(customers);
					es.flush();
				
				if(buffer.toString().length()>0){
					buffer = buffer.delete(buffer.length()-1, buffer.length());
					CodifyMessage.addMessage("ERR_EXEC_INT_SERVICE",CodifyMessage.Severity.ERROR, new String[]{"Akritiv service",buffer.toString()});
				}
				LOGGER.info("**************Akritiv response saved Successfully************************");
		 }
		}

	}
    
     
    /**
     * Method validating request Customers
     * Applying DedupBizOp Checks
     * @return
     */
    public boolean validateDeDupCustomer(){
    	if(!inWfStatus("REQUEST_STATUS_NEW")){
    		return true;
    	}
    	EntityService entityService = new EntityService<BaseDataObject>();
    	boolean flag = true;
        List<Customer> requestCustomers=getAllCustomers();
        
        String[] queryParams = new String[] {"customerSSN"};
        
        if(requestCustomers != null && requestCustomers.isEmpty()) {
        	
        	//iterating on request customers
        	for (Customer requestCustomer : requestCustomers){
		        // Creating query parameter values.
		        String ssn=requestCustomer.getSsn();
		        Object[] queryValues=new Object[]{ssn};
		        
		        //Get all customers from database with provided SSN number.
		        List<Customer> existingCustomers=entityService.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.Customer.bySsn",queryParams,queryValues);
		
				// should only have one, lets only take the first one in the list        			
				Customer existingCustomer = null;
		    	
		    	for (Customer c : existingCustomers) {
					if (requestCustomer.getId() != null && requestCustomer.getId().equals(c.getId())) {
						// same customer skip
						continue;
					} else {
						existingCustomer = c;
						break;
					}	    				
				}
    	
				if (existingCustomer != null){							   
						if(requestCustomer.getBirthDate() ==null || existingCustomer.getBirthDate() == null) { 
						    continue;
						}else if(!requestCustomer.getBirthDate().equals(existingCustomer.getBirthDate())) { 
						   flag=false;
						}
				}
		
        	  }
        	}
    	return flag;
    }
    
	    public boolean clientBankInfo(){
	    	boolean flag = true;
	    	if(getAccount().getCustomer() != null){
	    	Customer customer = getAccount().getCustomer();
	        				if(customer.getBankTrades().size() == 0){
	        					flag= false;
	        				}
	        				
	    	}
	    	return flag;
	    }	
	    
	    public boolean coaCleared(){
	    	boolean match = false;
	    	for(Facility facility : getAllFacilities()){
	    		if(facility.getCoaCleared()){
	    			match = true;
	    		}else{
	    			match = false;
	    		}
	    	}
	    	return match;
	    }
	    
	    public boolean modifyDebtorsAllowed(){
			boolean match = false;
			User user = (User)ContextHolder.getContext().getUser();
			List<Team> teams = user.getTeams();
			for(Team team : teams){
				if("credit decision team".equals(team.getName().toLowerCase())){
					match = true;
					break;
				}
			}
			/*List<Role> roles = user.getRoles();
			for(Role role : roles){
				if("credit officer".equals(role.getName().toLowerCase()) || "underwriter".equals(role.getName().toLowerCase())){
					match = true;
					break;
				}
			}*/
			return match;
		}
	    
	    public boolean getCompletedCoaCheck(){
	    	boolean match = true;
			if(!getCoaEvalFacilitiesDer().isEmpty() ){
				List<CoaEvaluation> coaEvals = getCoaEvalFacilitiesDer();
				if(!coaEvals.isEmpty()){
					for (CoaEvaluation coaEval : coaEvals) {
						if(!coaEval.inWfStatus("COA_EVALUATION_COMPLETE",
								"COA_EVALUATION_SUSPEND")){
							match = false;
							return match;
						}
					}
				}
				
			}
			
			return match;
	    }
	    
	    public boolean customerDuplicateRolesCheck(){
	    	boolean match = true;
	    	int counter = 0;
			if(!getAllFacilities().isEmpty() ){
				List<Facility> facilities = getAllFacilities();
				if(!facilities.isEmpty()){
					for (Facility f : facilities) {
						if(f.getFacilityCustomerRoles() !=null){
							List<FacilityCustomerRole> uniqueSet = new ArrayList<FacilityCustomerRole>(f.getFacilityCustomerRoles());
							List<String> partyRoleCust = new ArrayList<String>();
							String partyRoleCustString=null;
							for(FacilityCustomerRole fcr : uniqueSet){
								if(fcr.getPartyRole() !=null && fcr.getPartyRole().getId() !=null){
									Long partyRole = (Long)fcr.getPartyRole().getId();
									AttributeChoice partyRoleAttribute = (AttributeChoice)fcr.getPartyRole();
									String partyRoleAttributeVal = partyRoleAttribute.getDisplayValue();
									Long cust = (Long)fcr.getCustomer().getId();
									partyRoleCustString = (partyRoleAttributeVal.toString()).concat(cust.toString());
									if(!partyRoleCust.contains(partyRoleCustString)){
										partyRoleCust.add(partyRoleCustString);
										//counter=0;
									}else{
										counter++;
									}
								}
							}
						}
					}
				}
			}
			
			if(counter >= 1){
				match = false;
				return match;
			}
			
			return match;
	    }
	    
	    //COA checklist should be editable only for the user of 'Credit Decision' team
		public boolean getCoaAction(){
			boolean match = false;
			User user = (User)ContextHolder.getContext().getUser();
			List<Team> teams = user.getTeams();
			for(Team team : teams){
				//System.out.println("=============Team Name========="+team.getName());
				if(team.getName() !=null && "credit decision team".equals(team.getName().toLowerCase())){
					match = true;
					break;
				}
			}
			return match;
		}
		
		public boolean updateApplicationInfoAllowed(){
			boolean match = false;
			User user = (User)ContextHolder.getContext().getUser();
			List<Team> teams = user.getTeams();
			for(Team team : teams){
				//System.out.println("=============Team Name========="+team.getName());
				if(team.getName() !=null && "credit decision team".equals(team.getName().toLowerCase()) && this.inWfStatus("REQUEST_STATUS_APPROVED_DOCUMENTATION","REQUEST_STATUS_PORTFOLIO_MANAGMENT")){
					match = true;
					break;
				}
			}
			return match;
		}
}