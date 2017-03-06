/*
 * Copyright (c) 2005-2015 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.persistence.Transient;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mvel2.MVEL;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.codify.loanpath.util.Utility;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;

/**
 * DOCUMENT ME!
 * 
 * @author ENTER YOUR FULL NAME
 * @version 1.0
 * @since 1.0
 */

public class Scoring extends BaseDataObject {

	// ~ Static fields/initializers
	// -------------------------------------------------------------------------------------

	/** Use serialVersionUID for interoperability. */
	private static final long serialVersionUID = 6236857041232097916L;

	private static final Log LOG = LogFactory.getLog(Scoring.class);
	// ~ Methods
	// --------------------------------------------------------------------------------------------------------

	@Transient
	private ScrRcmRuleSeed scrRecomRuleSeed;

	@Transient
	private ScrAttributeSeed scrAttributeSeed;

	public String getAverageBCPUtilization() {
		BigDecimal averageBCPUtilization = BigDecimal.ZERO;
		LOG.debug("Inside Scoring : getAverageBCPUtilization() from MVEL..");
		if (isBCPCriteriaMatches()
				&& isExposureCriteriaMatches(Constants.BUSINESS_CHECKING_PLUS_KEY)) {

			BigDecimal sumAverageBCPOutstandings = getAverageOutstandings(Constants.BUSINESS_CHECKING_PLUS_KEY);
			BigDecimal sumOfBCPExposure = new BigDecimal(
					getExistingExposureAmountBCP());
			averageBCPUtilization = sumAverageBCPOutstandings.divide(
					sumOfBCPExposure, 3, RoundingMode.HALF_DOWN);
			return String.valueOf(averageBCPUtilization);
		} else {
			return String.valueOf(averageBCPUtilization);
		}
	}

	public String getAverageBCAUtilization() {
		BigDecimal averageBCAUtilization = BigDecimal.ZERO;
		LOG.debug("Inside Scoring : getAverageBCPUtilization() from MVEL..");
		if (isBCACriteriaMatches()
				&& isExposureCriteriaMatches(Constants.BUSINESS_CREDIT_ACCOUNT_KEY)) {
			BigDecimal sumAverageBCAOutstandings = getAverageOutstandings(Constants.BUSINESS_CREDIT_ACCOUNT_KEY);
			BigDecimal sumOfBCAExposure = new BigDecimal(
					getExistingExposureAmountBCA());

			averageBCAUtilization = sumAverageBCAOutstandings.divide(
					sumOfBCAExposure, 3, RoundingMode.HALF_DOWN);
			return String.valueOf(averageBCAUtilization);
		} else {
			return String.valueOf(averageBCAUtilization);
		}
	}

	public BigDecimal getAverageOutstandings(String facilityType) {
		BigDecimal sumAverageUtilization = BigDecimal.ZERO;
		if (getRequest() != null && getRequest().getExposure() != null) {

			Exposure exposure = getRequest().getExposure();
			List<CustomerFacilityExposure> directExposures = exposure
					.getDirectFacilityExposures();
			for (CustomerFacilityExposure directExposure : directExposures) {
				for (FacilityExposure facilityExposure : directExposure
						.getFacilityExposures()) {
					if (facilityExposure.getFacilityType() != null
							&& StringUtils.equals(facilityExposure
									.getFacilityType().getKey(), facilityType)) {
						sumAverageUtilization = sumAverageUtilization
								.add(facilityExposure.getAvgMoBal() == null ? BigDecimal.ZERO
										: facilityExposure.getAvgMoBal());
					}
				}
			}
		}
		return sumAverageUtilization;
	}

	public boolean isExposureCriteriaMatches(String facilityType) {
		LOG.debug("Inside Scoring : isExposureCriteriaMatches()");
		String tmp = "0";
		if (StringUtils.equals(facilityType,
				Constants.BUSINESS_CHECKING_PLUS_KEY)) {
			tmp = getExistingExposureAmountBCP();
		} else if (StringUtils.equals(facilityType,
				Constants.BUSINESS_CREDIT_ACCOUNT_KEY)) {
			tmp = getExistingExposureAmountBCA();
		}

		BigDecimal existingExposureBCP = BigDecimal.ZERO;
		if (tmp != null) {
			existingExposureBCP = new BigDecimal(tmp);
		}
		if (existingExposureBCP.compareTo(BigDecimal.ZERO) > 0) {
			return true;
		}
		return false;
	}

	public boolean isBCPCriteriaMatches() {
		LOG.debug("Inside Scoring : isBCPCriteriaMatches()");
		List<CustomerFacilityExposure> cstfaclityExposures = getRequest()
				.getExposure().getCustomerFacilityExposures();
		for (CustomerFacilityExposure cstfaclityExposure : cstfaclityExposures) {
			RelationshipParty party = cstfaclityExposure.getRelParty();
			if (party.getCustomer().isNonIndividual()) {
				for (FacilityExposure facilityExposure : cstfaclityExposure
						.getFacilityExposures()) {
					if (facilityExposure.getFacilityType() != null
							&& StringUtils.equals(facilityExposure
									.getFacilityType().getKey(),
									Constants.BUSINESS_CHECKING_PLUS_KEY)
							&& (Constants.FACILITY_EXPOSURE_TYPE_PIPELINE
									.equalsIgnoreCase(facilityExposure
											.getType()) || facilityExposure
									.getAccountNumber() != null)) {
						return true;

					}
				}
			}

		}
		return false;
	}

	public boolean isBCACriteriaMatches() {
		LOG.debug("Inside Scoring : isBCACriteriaMatches()");
		List<CustomerFacilityExposure> cstfaclityExposures = getRequest()
				.getExposure().getCustomerFacilityExposures();
		for (CustomerFacilityExposure cstfaclityExposure : cstfaclityExposures) {
			RelationshipParty party = cstfaclityExposure.getRelParty();
			if (party.getCustomer().isNonIndividual()) {
				for (FacilityExposure facilityExposure : cstfaclityExposure
						.getFacilityExposures()) {
					if (facilityExposure.getFacilityType() != null
							&& StringUtils.equals(facilityExposure
									.getFacilityType().getKey(),
									Constants.BUSINESS_CREDIT_ACCOUNT_KEY)
							&& (Constants.FACILITY_EXPOSURE_TYPE_PIPELINE
									.equalsIgnoreCase(facilityExposure
											.getType()) || facilityExposure
									.getAccountNumber() != null)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public String getExistingCustomerOrNew() {
		LOG.debug("Inside Scoring:getExistingCustomerOrNew() from MVEL..");
		if (isPrimaryBorrowerCriteriaCheck() || isGuarantorCriteriaCheck()) {
			return Constants.EXISTING_CITI_CUSTOMER;
		}
		return Constants.NEW_CITI_CUSTOMER;

	}

	public boolean isGuarantorCriteriaCheck() {
		BigDecimal sumOfOwnershipPercentage = BigDecimal.ZERO;
		int config = getScrAttrParamValue(Constants.CITI_RELATIONSHIP_KEY_2);
		if (config < 0) {
			return false;
		}
		if (getRequest() != null && getRequest().getRelationship() != null) {
			List<RelationshipParty> allGuarantors = getRequest()
					.getRelationship().getRelationshipPartyByRole(
							Constants.PARTY_ROLE_TYPE_GUARANTOR_KEY);
			List<RelationshipParty> guarantors = new ArrayList<RelationshipParty>();
			for (RelationshipParty guarantor : allGuarantors) {
				if (isBankAccountCriteriaCheck(guarantor)) {
					guarantors.add(guarantor);
				}
			}
			for (RelationshipParty guarantor : guarantors) {
				Customer customer = guarantor.getCustomer();
				BigDecimal ownershipValue = customer.getOwnershipValue();
				sumOfOwnershipPercentage = sumOfOwnershipPercentage
						.add(ownershipValue);
			}
			LOG.debug("Sum of All the Guarantors:-" + sumOfOwnershipPercentage);
			if (sumOfOwnershipPercentage.compareTo(BigDecimal.valueOf(Double
					.valueOf(config))) > 0) {
				return true;
			}
		}
		return false;
	}

	public String getValueOfCitiCurrentBalance() {
		LOG.debug("Inside Scoring:getValueOfCitiCurrentBalance() from MVEL..");
		BigDecimal sumOfCitiCurrentBalance = BigDecimal.ZERO;

		if (getRequest() != null
				&& getRequest().getRelationship() != null
				&& getRequest().getRelationship().getRelatedParties() != null
				&& getRequest().getRelationship().getRelatedParties().size() > 0) {
			List<RelationshipParty> parties = getRequest().getRelationship()
					.getRelatedParties();
			for (RelationshipParty party : parties) {
				sumOfCitiCurrentBalance = sumOfCitiCurrentBalance.add(party
						.getCustomer().getSumOfCurrentBalanceSavingsDDA()
						.getValue());
			}
		} else {
			// nothing can be done if there is no parties..
		}
		return String.valueOf(sumOfCitiCurrentBalance);
	}

	public boolean isPrimaryBorrowerCriteriaCheck() {
		if (getRequest() != null && getRequest().getRelationship() != null) {
			RelationshipParty primaryBorrower = getRequest().getRelationship()
					.getPrimaryBorrower();
			if (isBankAccountCriteriaCheck(primaryBorrower)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Added for Calculation of Facility Attribute.. check if customer is new or
	 * Existing..
	 * 
	 * @param relationshipParty
	 * 
	 * @return
	 */
	public boolean isExistingCustomer(RelationshipParty relationshipParty) {
		Customer customer = relationshipParty.getCustomer();
		LOG.debug("CustomerName is:-" + customer.getLegalName());
		int config = 365;
		List<BankAndTrade> lstBankTrades = customer.getBankTrades();
		for (BankAndTrade banktrade : lstBankTrades) {
			String bankName = "", accountTypeKey = "";
			AttributeChoice accountType = (AttributeChoice) banktrade
					.getAccountType();
			bankName = banktrade.getName();
			if ((accountType != null) && (accountType.getKey() != null)) {
				accountTypeKey = accountType.getKey();
			}

			Date accountOpenedDate = banktrade.getAccountOpenedDttm();
			Date appReceivedDate = getRequest().getReceivedDate();
			if ((Constants.BANK_ACCOUNT_TYPE_SAVINGS_KEY
					.equalsIgnoreCase(accountTypeKey) || Constants.BANK_ACCOUNT_TYPE_DDA_KEY
					.equalsIgnoreCase(accountTypeKey))
					&& "CITI".equalsIgnoreCase(bankName.trim())) {
				if (accountOpenedDate != null && appReceivedDate != null) {
					long diff = appReceivedDate.getTime()
							- accountOpenedDate.getTime();
					long diffDays = TimeUnit.DAYS.convert(diff,
							TimeUnit.MILLISECONDS);
					LOG.debug("App receiveddate - AccountOpenedDate is:-"
							+ diffDays);
					if (diffDays > Long.valueOf(config)) {
						return true;
					}
				}

			}
		}
		return false;
	}

	public long citidepositTenureDDAOrChecking(
			RelationshipParty relationshipParty) {
		long tenure = 0L;
		Customer customer = relationshipParty.getCustomer();
		LOG.debug("CustomerName is:-" + customer.getLegalName());
		List<BankAndTrade> lstBankTrades = customer.getBankTrades();
		for (BankAndTrade banktrade : lstBankTrades) {
			String bankName = "", accountTypeKey = "";
			AttributeChoice accountType = (AttributeChoice) banktrade
					.getAccountType();
			bankName = banktrade.getName();
			if ((accountType != null) && (accountType.getKey() != null)) {
				accountTypeKey = accountType.getKey();
			}

			Date accountOpenedDate = banktrade.getAccountOpenedDttm();
			Date appReceivedDate = getRequest().getReceivedDate();
			if ((Constants.BANK_ACCOUNT_TYPE_CHECKING_KEY
					.equalsIgnoreCase(accountTypeKey) || Constants.BANK_ACCOUNT_TYPE_DDA_KEY
					.equalsIgnoreCase(accountTypeKey))
					&& "CITI".equalsIgnoreCase(bankName.trim())) {
				if (accountOpenedDate != null && appReceivedDate != null) {
					long diff = appReceivedDate.getTime()
							- accountOpenedDate.getTime();
					long diffDays = TimeUnit.DAYS.convert(diff,
							TimeUnit.MILLISECONDS);
					LOG.debug("App receiveddate - AccountOpenedDate is:-"
							+ diffDays);
					if (diffDays > 0) {
						tenure = diffDays;
						break;
					}
				}
			}
		}
		return tenure;
	}

	public boolean isBankAccountCriteriaCheck(
			RelationshipParty relationshipParty) {
		Customer customer = relationshipParty.getCustomer();
		LOG.debug("CustomerName is:-" + customer.getLegalName());
		int config = getScrAttrParamValue(Constants.CITI_RELATIONSHIP_KEY_1);
		if (config < 0) {
			return false;
		}
		List<BankAndTrade> lstBankTrades = customer.getBankTrades();
		for (BankAndTrade banktrade : lstBankTrades) {
			String bankName = "", accountTypeKey = "";
			AttributeChoice accountType = (AttributeChoice) banktrade
					.getAccountType();
			bankName = banktrade.getName();
			if ((accountType != null) && (accountType.getKey() != null)) {
				accountTypeKey = accountType.getKey();
			}

			Date accountOpenedDate = banktrade.getAccountOpenedDttm();
			Date appReceivedDate = getRequest().getReceivedDate();
			if ((Constants.BANK_ACCOUNT_TYPE_SAVINGS_KEY
					.equalsIgnoreCase(accountTypeKey) || Constants.BANK_ACCOUNT_TYPE_DDA_KEY
					.equalsIgnoreCase(accountTypeKey))
					&& "CITI".equalsIgnoreCase(bankName.trim())) {
				if (accountOpenedDate != null && appReceivedDate != null) {
					long diff = appReceivedDate.getTime()
							- accountOpenedDate.getTime();
					long diffDays = TimeUnit.DAYS.convert(diff,
							TimeUnit.MILLISECONDS);
					LOG.debug("App receiveddate - AccountOpenedDate is:-"
							+ diffDays);
					if (diffDays > Long.valueOf(config)) {
						return true;
					}
				}

			}
		}
		return false;
	}

	public String getScoringDecisionDttm() {
		String format = "MM/dd/yyyy";
		DateFormat formatter = new SimpleDateFormat(format, new Locale("en",
				"US"));
		java.util.Date date = getEvalDttm();
		formatter.setLenient(false);

		if (date != null) {
			return formatter.format(date);
		} else {
			return null;
		}
	}

	public String getAverageFicoGuarantors() {
		LOG.debug("Inside Scoring: getAverageFicoGuarantors() from MVEL..");
		BigDecimal averageFicoAcrossGuarantors = BigDecimal.ZERO;
		BigDecimal sumOfFicoAcrossGuarantors = BigDecimal.ZERO;
		int count = 0;
		for (PartyScoring partyScoring : getPartyScorings()) {
			ScoringAttribute averageFico = partyScoring
					.findAttributeByKey("INDVIDUAL_ATTR_AVERAGE_FICO_INDIVIDUAL");
			if (averageFico != null
					&& NumberUtils.isNumber(averageFico.getValue())) {
				sumOfFicoAcrossGuarantors = sumOfFicoAcrossGuarantors
						.add(new BigDecimal(averageFico.getValue()));
				count++;
			}
		}
		if (count > 0
				&& sumOfFicoAcrossGuarantors.compareTo(BigDecimal.ZERO) > 0) {
			averageFicoAcrossGuarantors = sumOfFicoAcrossGuarantors.divide(
					new BigDecimal(count), 3, RoundingMode.HALF_DOWN);
		}
		return String.valueOf(averageFicoAcrossGuarantors);
	}

	public String getTotalRequestedExposureApplication() {
		LOG.debug("Inside Scoring: getTotalRequestedExposureApplication() from MVEL..");
		BigDecimal totalRequestedExposure = BigDecimal.ZERO;
		BigDecimal directRequestedExposure = BigDecimal.ZERO;
		BigDecimal inDirectRequestedExposure = BigDecimal.ZERO;

		if (getRequest() != null && getRequest().getExposure() != null) {

			Exposure exposure = getRequest().getExposure();

			// calculations of directRequestedExposure
			List<CustomerFacilityExposure> directExposures = exposure
					.getDirectFacilityExposures();
			for (CustomerFacilityExposure directExposure : directExposures) {
				if (directExposure.getTotalRequestedExposure() != null) {
					directRequestedExposure = directRequestedExposure
							.add(directExposure.getTotalRequestedExposure());
				}
			}
			// calculations of IndirectRequestedExposure
			List<CustomerFacilityExposure> indirectExposures = exposure
					.getIndirFacExposures();
			for (CustomerFacilityExposure indirectExposure : indirectExposures) {
				if (indirectExposure.getTotalRequestedExposure() != null) {
					inDirectRequestedExposure = inDirectRequestedExposure
							.add(indirectExposure.getTotalRequestedExposure());
				}
			}

			totalRequestedExposure = totalRequestedExposure
					.add(directRequestedExposure);
			totalRequestedExposure = totalRequestedExposure
					.add(inDirectRequestedExposure);

		}
		return String.valueOf(totalRequestedExposure);
	}

	public String getTotalDirectNewRequestedAmount() {
		LOG.debug("Inside Scoring: getTotalNewRequestedAmount() from MVEL..");
		BigDecimal totalNewRequestedAmtDirect = BigDecimal.ZERO;
		if (getRequest() != null && getRequest().getExposure() != null) {

			Exposure exposure = getRequest().getExposure();

			// calculations of directRequestedExposure
			List<CustomerFacilityExposure> directExposures = exposure
					.getDirectFacilityExposures();
			for (CustomerFacilityExposure directExposure : directExposures) {
				if (directExposure.getTotalRequestedExposure() != null) {
					totalNewRequestedAmtDirect = totalNewRequestedAmtDirect
							.add(directExposure.getTotalRequestedExposure());
				}
			}

		}

		return String.valueOf(totalNewRequestedAmtDirect);
	}

	public String getTotalDirectExistingExposureAmount() {

		LOG.debug("Inside Scoring: getTotalExistingExposureAmount() from MVEL..");
		BigDecimal totalExistingExposureDirect = BigDecimal.ZERO;
		if (getRequest() != null && getRequest().getExposure() != null) {

			Exposure exposure = getRequest().getExposure();

			// calculations of directRequestedExposure
			List<CustomerFacilityExposure> directExposures = exposure
					.getDirectFacilityExposures();
			for (CustomerFacilityExposure directExposure : directExposures) {
				if (directExposure.getTotalExistingExposure() != null) {
					totalExistingExposureDirect = totalExistingExposureDirect
							.add(directExposure.getTotalExistingExposure());
				}
			}
		}
		return String.valueOf(totalExistingExposureDirect);
	}

	public String getTotalPipelineAmountDirect() {
		LOG.debug("Inside Scoring: getTotalPipelineAmountDirect() from MVEL..");
		BigDecimal totalPipelineAmountDirect = BigDecimal.ZERO;
		if (getRequest() != null && getRequest().getExposure() != null) {

			Exposure exposure = getRequest().getExposure();

			// calculations of directRequestedExposure
			List<CustomerFacilityExposure> directExposures = exposure
					.getDirectFacilityExposures();
			for (CustomerFacilityExposure directExposure : directExposures) {
				if (directExposure.getTotalPipelineAmount() != null) {
					totalPipelineAmountDirect = totalPipelineAmountDirect
							.add(directExposure.getTotalPipelineAmount());
				}
			}
		}
		return String.valueOf(totalPipelineAmountDirect);
	}

	public String getTotalExposureAdjustmentsDirect() {
		LOG.debug("Inside Scoring: getTotalExposureAdjustmentsDirect() from MVEL..");
		BigDecimal totalExposureAdjustementDirect = BigDecimal.ZERO;
		if (getRequest() != null && getRequest().getExposure() != null) {

			Exposure exposure = getRequest().getExposure();

			// calculations of directRequestedExposure
			List<CustomerFacilityExposure> directExposures = exposure
					.getDirectFacilityExposures();
			for (CustomerFacilityExposure directExposure : directExposures) {
				if (directExposure.getTotalExposureAdjustments() != null) {
					totalExposureAdjustementDirect = totalExposureAdjustementDirect
							.add(directExposure.getTotalExposureAdjustments());
				}
			}
		}
		return String.valueOf(totalExposureAdjustementDirect);
	}

	public String getTotalExposureAdjustmentsInDirect() {
		LOG.debug("Inside Scoring: getTotalExposureAdjustmentsInDirect() from MVEL..");
		BigDecimal totalExposureAdjustementInDirect = BigDecimal.ZERO;
		if (getRequest() != null && getRequest().getExposure() != null) {

			Exposure exposure = getRequest().getExposure();

			// calculations of directRequestedExposure
			List<CustomerFacilityExposure> inDirectExposures = exposure
					.getIndirFacExposures();
			for (CustomerFacilityExposure inDirectExposure : inDirectExposures) {
				if (inDirectExposure.getTotalExposureAdjustments() != null) {
					totalExposureAdjustementInDirect = totalExposureAdjustementInDirect
							.add(inDirectExposure.getTotalExposureAdjustments());
				}
			}
		}
		return String.valueOf(totalExposureAdjustementInDirect);

	}

	public String getTotalExistingExposureAmountInDirect() {
		LOG.debug("Inside Scoring: getTotalExistingExposureAmountInDirect() from MVEL..");
		BigDecimal totalExistingExposureInDirect = BigDecimal.ZERO;
		if (getRequest() != null && getRequest().getExposure() != null) {

			Exposure exposure = getRequest().getExposure();

			// calculations of directRequestedExposure
			List<CustomerFacilityExposure> indirectExposures = exposure
					.getIndirFacExposures();
			for (CustomerFacilityExposure indirectExposure : indirectExposures) {
				if (indirectExposure.getTotalExistingExposure() != null) {
					totalExistingExposureInDirect = totalExistingExposureInDirect
							.add(indirectExposure.getTotalExistingExposure());
				}
			}
		}
		return String.valueOf(totalExistingExposureInDirect);
	}

	public String getTotalPipelineAmountIndirect() {
		LOG.debug("Inside Scoring: getTotalPipelineAmountIndirect() from MVEL..");
		BigDecimal totalPipelineAmountInDirect = BigDecimal.ZERO;
		if (getRequest() != null && getRequest().getExposure() != null) {

			Exposure exposure = getRequest().getExposure();
			List<CustomerFacilityExposure> indirectExposures = exposure
					.getIndirFacExposures();
			for (CustomerFacilityExposure indirectExposure : indirectExposures) {
				if (indirectExposure.getTotalPipelineAmount() != null) {
					totalPipelineAmountInDirect = totalPipelineAmountInDirect
							.add(indirectExposure.getTotalPipelineAmount());
				}
			}
		}
		return String.valueOf(totalPipelineAmountInDirect);
	}

	public String getOverallCustomerExposureLimnit() {
		LOG.debug("Inside Scoring: getOverallCustomerExposureLimnit() from MVEL..");
		// Per use case this value if fixed irrespective of all the facilities
		return String.valueOf(250000);

	}

	public String getSalesFactorRatio() {
		LOG.debug("Inside Scoring: getSalesFactorRatio() from MVEL..");
		String val = "";
		if (getRequest() != null && getRequest().getRelationship() != null
				&& getRequest().getRelationship().getPrimaryBorrower() != null) {
			RelationshipParty party = getRequest().getRelationship()
					.getPrimaryBorrower();
			if (party != null && party.getCustomer() != null
					&& party.getCustomer().getBusinessDetail() != null) {
				if (party
						.getCustomer()
						.getBusinessDetail()
						.hasIndustryCategory(Constants.NAICS_RISK_HIGH_RISK_KEY)
						&& party.getCustomer()
								.getBusinessDetail()
								.hasIndustryCategory(
										Constants.NAICS_RISK_PROHIBITED_KEY)
						&& party.getCustomer()
								.getBusinessDetail()
								.hasIndustryCategory(
										Constants.NAICS_RISK_SPECILIZED_KEY)) {
					val = "10";
				} else {
					val = "20";
				}
				return val;
			}
		}

		return null;
	}

	public String getExistingExposureAmountBCP() {
		LOG.debug("Inside Scoring: getExistingExposureAmountBCP() from MVEL..");
		if (getRequest() != null) {
			if (getRequest().isFacilityrequested(
					Constants.BUSINESS_CHECKING_PLUS_KEY)) {
				BigDecimal sumOfExistingExposureAmount = getSumofExistingExposureAmount(Constants.BUSINESS_CHECKING_PLUS_KEY);
				return String.valueOf(sumOfExistingExposureAmount);
			} else {
				// since there is no BCP requested in the Request.. no need to
				// calculate this attribute..
				return null;
			}
		}
		return null;
	}

	public String getPipelineAmountBCA() {
		LOG.debug("Inside Scoring: getExistingExposureAmountBCP() from MVEL..");
		if (getRequest() != null) {
			if (getRequest().isFacilityrequested(
					Constants.BUSINESS_CREDIT_ACCOUNT_KEY)) {
				BigDecimal sumOfExistingExposureAmount = getSumOfPipelineAmount(Constants.BUSINESS_CREDIT_ACCOUNT_KEY);
				return String.valueOf(sumOfExistingExposureAmount);
			} else {
				// since there is no BCP requested in the Request.. no need to
				// calculate this attribute..
				return null;
			}
		}
		return null;
	}

	public String getPipelineAmountBCP() {
		LOG.debug("Inside Scoring: getPipelineAmountBCP() from MVEL..");
		if (getRequest() != null) {
			if (getRequest().isFacilityrequested(
					Constants.BUSINESS_CHECKING_PLUS_KEY)) {
				BigDecimal sumOfExistingExposureAmount = getSumOfPipelineAmount(Constants.BUSINESS_CHECKING_PLUS_KEY);
				return String.valueOf(sumOfExistingExposureAmount);
			} else {
				// since there is no BCP requested in the Request.. no need to
				// calculate this attribute..
				return null;

			}
		}
		return null;
	}

	public String getPipelineAmountBIL() {
		LOG.debug("Inside Scoring: getPipelineAmountBCP() from MVEL..");
		if (getRequest() != null) {
			if (getRequest().isFacilityrequested(
					Constants.BUSINESS_INSTALLMENT_LOAN_KEY)) {
				BigDecimal sumOfExistingExposureAmount = getSumOfPipelineAmount(Constants.BUSINESS_INSTALLMENT_LOAN_KEY);
				return String.valueOf(sumOfExistingExposureAmount);
			} else {
				// since there is no BCP requested in the Request.. no need to
				// calculate this attribute..
				return null;
			}
		}
		return null;
	}

	public String getExistingExposureAmountBCA() {
		LOG.debug("Inside Scoring: getExistingExposureAmountBCA() from MVEL..");
		if (getRequest() != null) {
			if (getRequest().isFacilityrequested(
					Constants.BUSINESS_CREDIT_ACCOUNT_KEY)) {
				BigDecimal sumOfnewRequestedAmount = getSumofExistingExposureAmount(Constants.BUSINESS_CREDIT_ACCOUNT_KEY);
				return String.valueOf(sumOfnewRequestedAmount);
			} else {
				// since there is no BCP requested in the Request.. no need to
				// calculate this attribute..
				return null;
			}
		}
		return null;

	}

	public String getNewRequestedAmountBIL() {
		LOG.debug("Inside Scoring: getNewRequestedAmountBIL() from MVEL..");
		if (getRequest() != null) {
			if (getRequest().isFacilityrequested(
					Constants.BUSINESS_INSTALLMENT_LOAN_KEY)) {
				BigDecimal sumOfnewRequestedAmount = getSumofNewRequestedAmount(Constants.BUSINESS_INSTALLMENT_LOAN_KEY);
				return String.valueOf(sumOfnewRequestedAmount);
			} else {
				// since there is no BCP requested in the Request.. no need to
				// calculate this attribute..
				return null;
			}
		}
		return null;
	}

	public String getNewRequestedAmountBCP() {
		LOG.debug("Inside Scoring: getNewRequestedAmountBCP() from MVEL..");
		if (getRequest() != null) {
			if (getRequest().isFacilityrequested(
					Constants.BUSINESS_CHECKING_PLUS_KEY)) {
				BigDecimal sumOfnewRequestedAmount = getSumofNewRequestedAmount(Constants.BUSINESS_CHECKING_PLUS_KEY);
				return String.valueOf(sumOfnewRequestedAmount);
			} else {
				// since there is no BCP requested in the Request.. no need to
				// calculate this attribute..
				return null;
			}
		}
		return null;

	}

	public String getNewRequestedAmountBCA() {
		LOG.debug("Inside Scoring: getNewRequestedAmountBCA() from MVEL..");
		if (getRequest() != null) {
			if (getRequest().isFacilityrequested(
					Constants.BUSINESS_CREDIT_ACCOUNT_KEY)) {
				BigDecimal sumOfnewRequestedAmount = getSumofNewRequestedAmount(Constants.BUSINESS_CREDIT_ACCOUNT_KEY);
				return String.valueOf(sumOfnewRequestedAmount);
			} else {
				// since there is no BCA requested in the Request.. no need to
				// calculate this attribute..
				return null;
			}
		}
		return null;
	}

	public BigDecimal getSumofNewRequestedAmount(String facilityType) {
		BigDecimal total = BigDecimal.ZERO;

		if (getRequest() != null && getRequest().getExposure() != null) {
			Exposure exposure = getRequest().getExposure();

			List<CustomerFacilityExposure> directExposures = exposure
					.getDirectFacilityExposures();
			for (CustomerFacilityExposure directExposure : directExposures) {
				if (directExposure
						.getRequestedExposureByFacilityType(facilityType) != null) {
					total = total.add(directExposure
							.getRequestedExposureByFacilityType(facilityType));
				}
			}
		}
		return total;
	}

	private BigDecimal getSumOfPipelineAmount(String facilityType) {
		BigDecimal total = BigDecimal.ZERO;

		if (getRequest() != null && getRequest().getExposure() != null) {
			Exposure exposure = getRequest().getExposure();

			List<CustomerFacilityExposure> directExposures = exposure
					.getDirectFacilityExposures();
			for (CustomerFacilityExposure directExposure : directExposures) {
				if (directExposure
						.getPipelineAmountByFacilityType(facilityType) != null) {
					total = total.add(directExposure
							.getPipelineAmountByFacilityType(facilityType));
				}
			}
		}
		return total;
	}

	public BigDecimal getSumofExistingExposureAmount(String facilityType) {
		BigDecimal total = BigDecimal.ZERO;

		if (getRequest() != null && getRequest().getExposure() != null) {
			Exposure exposure = getRequest().getExposure();

			List<CustomerFacilityExposure> directExposures = exposure
					.getDirectFacilityExposures();
			for (CustomerFacilityExposure directExposure : directExposures) {
				if (directExposure
						.getExistingExposureByFacilityType(facilityType) != null) {
					total = total.add(directExposure
							.getExistingExposureByFacilityType(facilityType));
				}
			}
		}
		return total;
	}

	public BigDecimal getSumofExposureAdjustments(String facilityType) {

		BigDecimal total = BigDecimal.ZERO;

		if (getRequest() != null && getRequest().getExposure() != null) {
			Exposure exposure = getRequest().getExposure();

			List<CustomerFacilityExposure> directExposures = exposure
					.getDirectFacilityExposures();
			for (CustomerFacilityExposure directExposure : directExposures) {
				if (directExposure
						.getExposureAdjustmentsByFacilityType(facilityType) != null) {
					total = total
							.add(directExposure
									.getExposureAdjustmentsByFacilityType(facilityType));
				}
			}
		}
		return total;

	}

	public String getSalesRatioExposureLimit() {
		LOG.debug("Inside Scoring: getSalesRatioExposureLimit() from MVEL..");
		BigDecimal total = BigDecimal.ZERO;

		// 1 sales Fator Ratio...
		BigDecimal saleRatio = BigDecimal.ZERO;
		BigDecimal annualDeposit = BigDecimal.ZERO;
		BigDecimal annualSales = BigDecimal.ZERO;
		if (getSalesFactorRatio() != null
				&& NumberUtils.isNumber(getSalesFactorRatio())) {
			saleRatio = new BigDecimal(getSalesFactorRatio());
		}
		// 2 min of Primary Borrower Annual Sales or Primary Borrower Annualized
		// Deposits
		if (getRequest() != null && getRequest().getRelationship() != null
				&& getRequest().getRelationship().getPrimaryBorrower() != null) {
			RelationshipParty party = getRequest().getRelationship()
					.getPrimaryBorrower();
			annualDeposit = getAnnualizedDeposits(party.getCustomer());
			annualSales = party.getCustomer().getAnuualSales().getValue();
		}
		BigDecimal minOfSalesDeposit = annualDeposit.min(annualSales);

		total = saleRatio.multiply(minOfSalesDeposit);
		return String.valueOf(total);
	}

	public String getExistingExposureAmountBIL() {
		LOG.debug("Inside Scoring: getExistingExposureAmountBIL() from MVEL..");
		if (getRequest() != null) {
			if (getRequest().isFacilityrequested(
					Constants.BUSINESS_INSTALLMENT_LOAN_KEY)) {
				BigDecimal sumOfnewRequestedAmount = getSumofExistingExposureAmount(Constants.BUSINESS_INSTALLMENT_LOAN_KEY);
				return String.valueOf(sumOfnewRequestedAmount);
			} else {
				// since there is no BCP requested in the Request.. no need to
				// calculate this attribute..
				return null;
			}
		}
		return null;

	}

	public String getExposureAdjustmentsBCA() {
		LOG.debug("Inside Scoring: getExposureAdjustmentsBCA() from MVEL..");
		if (getRequest() != null) {
			if (getRequest().isFacilityrequested(
					Constants.BUSINESS_CREDIT_ACCOUNT_KEY)) {
				BigDecimal sumOfnewRequestedAmount = getSumofExposureAdjustments(Constants.BUSINESS_CREDIT_ACCOUNT_KEY);
				return String.valueOf(sumOfnewRequestedAmount);
			} else {
				// since there is no BCA requested in the Request.. no need to
				// calculate this attribute..
				return null;
			}
		}
		return null;
	}

	public String getExposureAdjustmentsBCP() {
		LOG.debug("Inside Scoring: getExposureAdjustmentsBCP() from MVEL..");
		if (getRequest() != null) {
			if (getRequest().isFacilityrequested(
					Constants.BUSINESS_CHECKING_PLUS_KEY)) {
				BigDecimal sumOfnewRequestedAmount = getSumofExposureAdjustments(Constants.BUSINESS_CHECKING_PLUS_KEY);
				return String.valueOf(sumOfnewRequestedAmount);
			} else {
				// since there is no BCP requested in the Request.. no need to
				// calculate this attribute..
				return null;
			}
		}
		return null;
	}

	public String getExposureAdjustmentsBIL() {
		LOG.debug("Inside Scoring: getsalesRatioExposureLimit() from MVEL..");
		if (getRequest() != null) {
			if (getRequest().isFacilityrequested(
					Constants.BUSINESS_INSTALLMENT_LOAN_KEY)) {
				BigDecimal sumOfnewRequestedAmount = getSumofExposureAdjustments(Constants.BUSINESS_INSTALLMENT_LOAN_KEY);
				return String.valueOf(sumOfnewRequestedAmount);
			} else {
				// since there is no BIL requested in the Request.. no need to
				// calculate this attribute..
				return null;
			}
		}
		return null;
	}

	public String getMaxValueBCA() {
		LOG.debug("Inside Scoring: getMaxValueBCA() from MVEL..");
		if (getRequest() != null) {
			if (getRequest().isFacilityrequested(
					Constants.BUSINESS_CREDIT_ACCOUNT_KEY)) {
				return String.valueOf("250000");
			} else {
				// since there is no BCA requested in the Request.. no need to
				// calculate this attribute..
				return null;
			}
		}
		return null;
	}

	public String getMaxValueBCP() {
		LOG.debug("Inside Scoring: getMaxValueBCP() from MVEL..");
		if (getRequest() != null) {
			if (getRequest().isFacilityrequested(
					Constants.BUSINESS_CHECKING_PLUS_KEY)) {
				return String.valueOf("20000");
			} else {
				// since there is no BCA requested in the Request.. no need to
				// calculate this attribute..
				return null;
			}
		}
		return null;
	}

	public String getMaxValueBIL() {
		LOG.debug("Inside Scoring: getMaxValueBIL() from MVEL..");
		if (getRequest() != null) {
			if (getRequest().isFacilityrequested(
					Constants.BUSINESS_BANKING_BIL_KEY)) {
				return String.valueOf("250000");
			} else {
				// since there is no BIL requested in the Request.. no need to
				// calculate this attribute..
				return null;
			}
		}
		return null;
	}

	public double getTotalRequestedExposureBCA() {
		BigDecimal total = BigDecimal.ZERO;
		total = total
				.add(getSumofNewRequestedAmount(Constants.BUSINESS_CREDIT_ACCOUNT_KEY));
		total = total
				.add(getSumofExistingExposureAmount(Constants.BUSINESS_CREDIT_ACCOUNT_KEY));
		return total.doubleValue();

	}

	public String getGridCapBCA() {
		LOG.debug("Inside Scoring: getGridCapBCA() from MVEL..");
		// once this method is defined the same content will also be in
		// ScoringCalcAttrApplication.drl in place of this method calling
		return null;
	}

	public String getGridCapBCP() {
		LOG.debug("Inside Scoring: getGridCapBCP() from MVEL..");
		// once this method is defined the same content will also be in
		// ScoringCalcAttrApplication.drl in place of this method calling

		return null;
	}

	public String getGridCapBIL() {
		LOG.debug("Inside Scoring: getGridCapBIL() from MVEL..");
		// once this method is defined the same content will also be in
		// ScoringCalcAttrApplication.drl in place of this method calling
		return null;
	}

	public String getTotalIncrementalAmountAssigned() {
		LOG.debug("Inside Scoring: getTotalIncrementalAmountAssigned() from MVEL..");
		BigDecimal total = BigDecimal.ZERO;
		return String.valueOf(total);
	}

	public String getTotalEXposureAmount() {
		LOG.debug("Inside Scoring: getTotalEXposureAmount() from MVEL..");
		BigDecimal total = BigDecimal.ZERO;

		// 1- Total Incremental Amount assigned
		total = total.add(new BigDecimal(getTotalIncrementalAmountAssigned()));

		// 2- Total Existing Exposure Amount (‘Direct- Existing’ + Pipeline)
		total = total
				.add(new BigDecimal(getTotalDirectExistingExposureAmount()));
		total = total.add(new BigDecimal(getTotalDirectNewRequestedAmount()));
		total = total.add(new BigDecimal(getTotalPipelineAmountDirect()));

		// 3Total Exposure Adjustments (Direct)
		total = total.add(new BigDecimal(getTotalExposureAdjustmentsDirect()));

		return String.valueOf(total);
	}

	public String getMaximumIncrementalAmount() {
		LOG.debug("Inside Scoring: getMmaximumIncrementalAmount() from MVEL..");
		BigDecimal total = BigDecimal.ZERO;

		total = new BigDecimal(getOverallCustomerMaxIncrementalAmount())
				.min(new BigDecimal(getSalesRatioMaximum()));
		return String.valueOf(total);
	}

	public String getMaximumBCAIncrementalAmount() {
		LOG.debug("Inside Scoring: getMaximumBCAIncrementalAmount() from MVEL..");
		BigDecimal total = BigDecimal.ZERO;

		// 1- Maximum Incremental Amount.
				

		// 2- BCA Facility Type Max
		BigDecimal bcaMax = BigDecimal.ZERO;
		bcaMax = bcaMax
				.add(getExistingExposureAmountBCA() == null ? BigDecimal.ZERO
						: new BigDecimal(getExistingExposureAmountBCA()));
		bcaMax = bcaMax.add(getPipelineAmountBCA() == null ? BigDecimal.ZERO
				: new BigDecimal(getPipelineAmountBCA()));
		bcaMax = bcaMax
				.add(getExposureAdjustmentsBCA() == null ? BigDecimal.ZERO
						: new BigDecimal(getExposureAdjustmentsBCA()));

		// 3
		// //////////////////...............................
		
		return String.valueOf(total);
	}

	public String getMaximumBCPIncrementalAmount() {
		LOG.debug("Inside Scoring: getMaximumBCPIncrementalAmount() from MVEL..");
		return null;
		// once this method is defined the same content will also be in
		// ScoringCalcAttrApplication.drl in place of this method calling

	}

	public String getMaximumBILIncrementalAmount() {
		LOG.debug("Inside Scoring: getMaximumBILIncrementalAmount() from MVEL..");
		// once this method is defined the same content will also be in
		// ScoringCalcAttrApplication.drl in place of this method calling
		return null;
	}

	public String getOverallCustomerMaxIncrementalAmount() {
		LOG.debug("Inside Scoring: getOverallCustomerMaxIncrementalAmount() from MVEL..");

		// To calculate as a result of this attribute
		BigDecimal overallCustomerMaximumIncrementalAmount = BigDecimal.ZERO;

		BigDecimal overallCustomerExposureLimit = new BigDecimal(
				getOverallCustomerExposureLimnit());

		BigDecimal totalDirectRequestedAmount = new BigDecimal(
				getTotalDirectNewRequestedAmount());
		BigDecimal totalDirectExistingAmount = new BigDecimal(
				getTotalDirectExistingExposureAmount());

		BigDecimal totalExposureAmountDirect = BigDecimal.ZERO;
		totalExposureAmountDirect = totalExposureAmountDirect
				.add(totalDirectRequestedAmount);
		totalExposureAmountDirect = totalExposureAmountDirect
				.add(totalDirectExistingAmount);

		BigDecimal totalExposureAdjustmentsDirect = new BigDecimal(
				getTotalExposureAdjustmentsDirect());

		BigDecimal tmp = totalExposureAmountDirect
				.add(totalExposureAdjustmentsDirect);
		overallCustomerMaximumIncrementalAmount = overallCustomerExposureLimit
				.subtract(tmp);

		return String.valueOf(overallCustomerMaximumIncrementalAmount);
	}

	public String getSalesRatioMaximum() {
		LOG.debug("Inside Scoring: getSalesRatioMaximum() from MVEL..");
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal salesRatioLimit = BigDecimal.ZERO;

		BigDecimal existingExposureAmount = BigDecimal.ZERO;
		BigDecimal pipelineAmount = BigDecimal.ZERO;
		BigDecimal exposureAdjustments = BigDecimal.ZERO;

		// 1. Sales Ratio Exposure
		if (getSalesRatioExposureLimit() != null
				&& NumberUtils.isNumber(getSalesRatioExposureLimit())) {
			salesRatioLimit = new BigDecimal(getSalesRatioExposureLimit());
		}
		// 2. Total Existing Exposure Amount (‘Direct – Existing’ + Pipeline) +
		// Total Exposure Adjustments (Direct)
		if (getTotalDirectExistingExposureAmount() != null
				&& NumberUtils.isNumber(getTotalDirectExistingExposureAmount())) {
			existingExposureAmount = new BigDecimal(
					getTotalDirectExistingExposureAmount());
		}
		if (getTotalPipelineAmountDirect() != null
				&& NumberUtils.isNumber(getTotalPipelineAmountDirect())) {
			pipelineAmount = new BigDecimal(getTotalPipelineAmountDirect());
		}
		if (getTotalExposureAdjustmentsDirect() != null
				&& NumberUtils.isNumber(getTotalExposureAdjustmentsDirect())) {
			exposureAdjustments = new BigDecimal(
					getTotalExposureAdjustmentsDirect());
		}
		total = total.add(existingExposureAmount).add(pipelineAmount)
				.add(exposureAdjustments);
		return String.valueOf(salesRatioLimit.min(total));
	}

	public boolean getNonProfitOragnizationRuleResult() {
		LOG.debug("Inside Scoring: getNonProfitOragnizationRuleResult() from MVEL..");
		boolean flag = true;
		if (legalEntityCriteriaMatches()) {
			flag = false;
		}
		return flag;
	}

	public boolean legalEntityCriteriaMatches() {
		boolean flag = false;
		if (getRequest() != null && getRequest().getRelationship() != null
				&& getRequest().getRelationship().getPrimaryBorrower() != null) {
			RelationshipParty primaryBorrower = getRequest().getRelationship()
					.getPrimaryBorrower();
			if (primaryBorrower
					.checkForLegalEntityType(Constants.LEGAL_STRUCTURE_NOT_FOR_PROFIT_KEY)) {
				flag = true;
			}
		}
		return flag;
	}

	public boolean getCustomerFullySecureRuleResult() {
		LOG.debug("Inside Scoring: getCustomerFullySecureRuleResult() from MVEL..");
		boolean flag = true;
		if (isCustomerFullySecure()) {
			flag = false;
		}
		return flag;
	}

	public boolean isCustomerFullySecure() {
		boolean flag = false;
		if (getRequest() != null) {
			if (getRequest().getFullySecure()) {
				flag = true;
			}
		}
		return flag;
	}

	public boolean getMissingFicoOrMissingCustomRuleResult() {
		LOG.debug("Inside Scoring: getMissingFicoOrMissingCustomRuleResult() from MVEL..");
		boolean flag = true;
		if (legalEntityCriteriaMatches()) {
			if (getAverageFicoGuarantors() != null
					&& NumberUtils.isNumber(getAverageFicoGuarantors())
					&& new BigDecimal(getAverageFicoGuarantors())
							.compareTo(BigDecimal.ZERO) == 0) {
				flag = false;
			}
		}
		return flag;
	}

	public boolean getCitiCreditExposureExceedRuleResult() {
		LOG.debug("Inside Scoring: getCitiCreditExposureExceedRuleResult() from MVEL..");
		boolean flag = true;

		BigDecimal citiCreditExposure = getcitiCreditExposureAmount();

		if (getRequest() != null
				&& getRequest().getRelationship() != null
				&& getRequest().getRelationship().getPrimaryBorrower() != null
				&& getRequest().getRelationship().getPrimaryBorrower()
						.getCustomer() != null) {
			BigDecimal annualSales = getRequest().getRelationship()
					.getPrimaryBorrower().getCustomer().getAnuualSales()
					.getValue();
			BigDecimal annualDeposit = getAnnualizedDeposits(getRequest()
					.getRelationship().getPrimaryBorrower().getCustomer());
			BigDecimal min = annualDeposit.min(annualSales);
			int percentage = getScrRcmParamValue(Constants.CITI_CREDIT_EXPOSURE_EXCEEDS_KEY_1);
			if (percentage > 0) {
				BigDecimal perOfMin = min.multiply(new BigDecimal(percentage))
						.divide(new BigDecimal(100));
				int res = citiCreditExposure.compareTo(perOfMin);
				if (res > 0) {
					flag = false;
				}
			} else {
				flag = false;
			}

		}
		return flag;
	}

	public BigDecimal getcitiCreditExposureAmount() {
		BigDecimal total = BigDecimal.ZERO;
		total = total
				.add(new BigDecimal(getTotalDirectExistingExposureAmount()));
		total = total.add(new BigDecimal(getTotalPipelineAmountDirect()));
		total = total.add(new BigDecimal(getTotalExposureAdjustmentsDirect()));
		total = total.add(new BigDecimal(
				getTotalExistingExposureAmountInDirect()));
		total = total.add(new BigDecimal(getTotalPipelineAmountIndirect()));
		total = total
				.add(new BigDecimal(getTotalExposureAdjustmentsInDirect()));
		return total;
	}

	/**
	 * This method returns the AnnualizedDeposits for a Party passed into the
	 * Parameter.
	 * 
	 * @return Annualized Deposits for a Particular party.
	 */

	public BigDecimal getAnnualizedDeposits(Customer customer) {

		BigDecimal total = BigDecimal.ZERO;
		if (customer != null) {
			List<BankAndTrade> lstBankTrades = customer.getBankTrades();
			for (BankAndTrade banktrade : lstBankTrades) {
				String bankName = "", accountTypeKey = "";
				AttributeChoice accountType = (AttributeChoice) banktrade
						.getAccountType();
				bankName = banktrade.getName();
				if ((accountType != null) && (accountType.getKey() != null)) {
					accountTypeKey = accountType.getKey();
					if (Constants.BANK_ACCOUNT_TYPE_DDA_KEY
							.equalsIgnoreCase(accountTypeKey)
							&& "CITI".equalsIgnoreCase(bankName.trim())) {
						Date accountOpenedDate = banktrade
								.getAccountOpenedDttm();
						Date appReceivedDate = getRequest().getReceivedDate();
						if (accountOpenedDate != null
								&& appReceivedDate != null) {
							
						}
					}
				}
			}
		}
		return total;
	}

	/**
	 * An custom method written for Scoring calcs.
	 * 
	 * 
	 * @param customer
	 */
	public BigDecimal getAverageCitiBalanceDDANonindAndIndGuarantor(
			Customer customer) {
		LOG.debug("Inside Scoring: getAverageCitiBalanceDDANonindAndIndGuarantor()..");
		BigDecimal averageBalanceNonIndividual = BigDecimal.ZERO;
		BigDecimal averageBalanceIndividual = BigDecimal.ZERO;
		if (customer != null) {
			averageBalanceNonIndividual = customer.getSumOfAverageBalanceDDA()
					.getValue();
		}
		List<RelationshipParty> parties = getRequest().getRelationship()
				.getRelatedParties();
		for (RelationshipParty party : parties) {
			if (party.isGuarantor() && party.getCustomer() != null
					&& party.getCustomer().isIndividual()) {
				averageBalanceIndividual = averageBalanceIndividual
						.add(customer.getSumOfAverageBalanceDDA().getValue());
			}
		}

		return averageBalanceIndividual.add(averageBalanceNonIndividual);
	}

	public boolean getAverageFicoCutOffRuleResult() {
		LOG.debug("Inside Scoring: getAverageFicoCutOffRuleResult() from MVEL..");
		boolean flag = true;

		// //average FICO is not missing or invalid and greater than 0...
		if (getAverageFicoGuarantors() != null
				&& NumberUtils.isNumber(getAverageFicoGuarantors())
				&& new BigDecimal(getAverageFicoGuarantors())
						.compareTo(BigDecimal.ZERO) > 0) {
			// average FICO is less than config value...
			double config = getScrRcmParamValue(Constants.AVERAGE_FICO_CUTOFF_KEY_1);
			if (config > 0
					&& new BigDecimal(getAverageFicoGuarantors())
							.compareTo(new BigDecimal(config)) < 0) {
				flag = false;
			}
		}
		return flag;
	}

	public boolean getNewToBankRuleResult() {
		LOG.debug("Inside Scoring: getNewToBankRuleResult() from MVEL..");
		boolean flag = true;

		String customerStatus = getExistingCustomerOrNew();
		double totalRequestedExposure = Double
				.parseDouble(getTotalRequestedExposureApplication());
		double config = getScrRcmParamValue(Constants.NEW_TOBANK_EXPOSURE_KEY_1);
		if (config > 0 && customerStatus.equals(Constants.NEW_CITI_CUSTOMER)
				&& totalRequestedExposure > config) {
			flag = false;
		}
		return flag;
	}

	public boolean getExistingCitiCustomerRuleResult() {
		LOG.debug("Inside Scoring: getExistingCitiCustomerRuleResult() from MVEL..");

		boolean flag = true;

		String customerStatus = getExistingCustomerOrNew();
		double totalRequestedExposure = Double
				.parseDouble(getTotalRequestedExposureApplication());
		double config = getScrRcmParamValue(Constants.EXISTING_CITI_CUSTOMER_EXPOSURE_KEY_1);
		if (config > 0
				&& customerStatus.equals(Constants.EXISTING_CITI_CUSTOMER)
				&& totalRequestedExposure > config) {
			flag = false;
		}
		return flag;
	}

	public boolean getCitiCurrentBalanceRuleResult() {
		LOG.debug("Inside Scoring: getCitiCurrentBalanceRuleResult() from MVEL..");
		boolean flag = true;

		BigDecimal sumOfCurrentBalancePrimaryBorrower = BigDecimal.ZERO;
		if (getRequest() != null && getRequest().getRelationship() != null) {
			RelationshipParty primaryBorrower = getRequest().getRelationship()
					.getPrimaryBorrower();
			sumOfCurrentBalancePrimaryBorrower = primaryBorrower.getCustomer()
					.getSumOfCurrentBalanceDDA().getValue();
			int config = getScrRcmParamValue(Constants.CITI_CURRENT_BALANCE_KEY_1);
			if (config > 0
					&& sumOfCurrentBalancePrimaryBorrower
							.compareTo(new BigDecimal(config)) < 0) {
				flag = false;
			}
		}
		return flag;
	}

	private int getScrRcmParamValue(String scrParamKey) {
		LOG.debug("Inside Scoring: getScrRcmParamValue()..");
		int param = 0;
		if (scrRecomRuleSeed != null) {
			ScrRuleParam scrRcmParam = (ScrRuleParam) LookupService.getResult(
					"ScrRuleParam.byParamKeyAndRuleSeedId", new String[] {
							"param_key", "scrRcmRuleSeed_key" }, new String[] {
							scrParamKey, scrRecomRuleSeed.getKey() });
			if (scrRcmParam != null && scrRcmParam.getParamValue() != null) {
				param = Integer.parseInt(scrRcmParam.getParamValue());
				LOG.debug("Param value for from DB:-" + param);
			} else {
				param = -1;
			}
		} else {
			param = -1;
		}
		return param;
	}

	private int getScrAttrParamValue(String scrParamKey) {
		LOG.debug("Inside Scoring: getScrAttrParamValue()..");
		int param = 0;
		if (scrAttributeSeed != null) {
			ScrAttrParam scrAttrParam = (ScrAttrParam) LookupService.getResult(
					"ScrAttrParam.byParamKeyAndAttrSeedId", new String[] {
							"param_key", "scrAttrSeed_key" }, new String[] {
							scrParamKey, scrAttributeSeed.getKey() });
			if (scrAttrParam != null && scrAttrParam.getParamValue() != null) {
				param = Integer.parseInt(scrAttrParam.getParamValue());
				LOG.debug("Param value for from DB:-" + param);
			} else {
				param = -1;
			}
		} else {
			param = -1;
		}
		return param;
	}

	/*
	 * This Method will give the value of Scoring Rule Parmas based on
	 * RecommRuleKey and Scr Param key
	 * 
	 * @parameter recommRuleKey is the key of ScrRecommRuleSeed
	 * 
	 * @parameter scrParamKey is the key of ScrRuleParam
	 * 
	 * @retun int value of rule Param.
	 */
	public int getScrRcmParamValueByRcmRule(String recommRuleKey,
			String scrParamKey) {
		LOG.debug("Inside Scoring: getScrRcmParamValueByRcmRule()..");
		int param = 0;

		ScrRuleParam scrRcmParam = (ScrRuleParam) LookupService.getResult(
				"ScrRuleParam.byParamKeyAndRuleSeedId", new String[] {
						"param_key", "scrRcmRuleSeed_key" }, new String[] {
						scrParamKey, recommRuleKey });
		if (scrRcmParam != null && scrRcmParam.getParamValue() != null) {
			param = Integer.parseInt(scrRcmParam.getParamValue());
			LOG.debug("Param value for from DB:-" + param);

		} else {
			param = -1;
		}
		return param;
	}

	private int getScrAttrParamValueByAttribute(String attributeKey,
			String scrParamKey) {
		LOG.debug("Inside Scoring: getScrAttrParamValue()..");
		int param = 0;
		ScrAttrParam scrAttrParam = (ScrAttrParam) LookupService.getResult(
				"ScrAttrParam.byParamKeyAndAttrSeedId", new String[] {
						"param_key", "scrAttrSeed_key" }, new String[] {
						scrParamKey, attributeKey });
		if (scrAttrParam != null && scrAttrParam.getParamValue() != null) {
			param = Integer.parseInt(scrAttrParam.getParamValue());
			LOG.debug("Param value for from DB:-" + param);
		} else {
			param = -1;
		}
		return param;
	}

	public boolean getCitiDepositTenureRuleResult() {
		LOG.debug("Inside Scoring: getCitiDepositTenureRuleResult() from MVEL..");
		boolean flag = true;
		int configDays = getScrRcmParamValue(Constants.CITI_DEPOSIT_TENURE_KEY_1);
		if (getRequest() != null && getRequest().getRelationship() != null) {
			RelationshipParty primaryBorrower = getRequest().getRelationship()
					.getPrimaryBorrower();
			int days = getCitiDepositTenure(primaryBorrower.getCustomer());

			if (configDays > 0 && days < configDays) {
				flag = false;
			}
		}
		return flag;
	}

	public int getCitiDepositTenure(Customer customer) {

		Date appReceivedDate = getRequest().getReceivedDate();
		Date oldestAcOpenDate = appReceivedDate;
		if (customer != null) {
			String bankName = "", accountTypeKey = "";
			List<BankAndTrade> lstBankTrades = customer.getBankTrades();

			// This below logic will give the oldest Account opened date amongst
			// all bank Name CITI and AC type DDA.
			for (BankAndTrade banktrade : lstBankTrades) {
				AttributeChoice accountType = (AttributeChoice) banktrade
						.getAccountType();
				bankName = banktrade.getName();

				if ((accountType != null) && (accountType.getKey() != null)) {
					accountTypeKey = accountType.getKey();
				}
				if (Constants.BANK_ACCOUNT_TYPE_DDA_KEY
						.equalsIgnoreCase(accountTypeKey)
						&& "CITI".equalsIgnoreCase(bankName.trim())) {
					Date accountOpenedDate = banktrade.getAccountOpenedDttm();
					if (accountOpenedDate.compareTo(oldestAcOpenDate) < 0) {
						oldestAcOpenDate = accountOpenedDate;
					}
				}
			}

		}
		return Utility.daysDifferenceInDates(appReceivedDate, oldestAcOpenDate);
	}

	public int getBusinessStartDays(Customer customer) {
		int days = 0;
		Date judgmentalStartDate = null, currentManagementStartDttm = null, appReceivedDate = null;
		Calendar calappReceivedDate = Calendar.getInstance();
		Calendar calBuisnessStartDate = Calendar.getInstance();

		if (customer != null && customer.getBusinessDetail() != null) {

			appReceivedDate = getRequest().getReceivedDate();
			judgmentalStartDate = customer.getBusinessDetail()
					.getJudgmentalStartDate();
			currentManagementStartDttm = customer.getBusinessDetail()
					.getCurrentManagementStartDttm();
			if (appReceivedDate != null && judgmentalStartDate != null) {
				calappReceivedDate.setTime(appReceivedDate);
				calBuisnessStartDate.setTime(judgmentalStartDate);
				days = Utility.daysDifferenceBetween(calappReceivedDate,
						calBuisnessStartDate);
			} else if (appReceivedDate != null
					&& currentManagementStartDttm != null) {
				calappReceivedDate.setTime(appReceivedDate);
				calBuisnessStartDate.setTime(currentManagementStartDttm);
				days = Utility.daysDifferenceBetween(calappReceivedDate,
						calBuisnessStartDate);
			} else {
			}
		} else {
			days = 0;
		}
		return days;
	}

	public boolean getRacAnalysisRequiredRuleResult() {
		LOG.debug("Inside Scoring: getRacAnalysisRequiredRuleResult() from MVEL..");
		int configAmount = getScrRcmParamValue(Constants.APPLICATION_RULE_RAC_ANALYSIS_REQUIRED_KEY_1);
		if (isCustomerFullySecure() || isOnlyBcpRequested()) {

			
		} else {
			if (getTotalRequestedExposureApplication() != null
					&& NumberUtils
							.isNumber(getTotalRequestedExposureApplication())
					&& new BigDecimal(getTotalRequestedExposureApplication())
							.compareTo(new BigDecimal(configAmount)) > 0) {
			}
		}

		return false;
	}

	public boolean isOnlyBcpRequested() {
		boolean flag = true;

		if (getRequest() != null
				&& getRequest().getAllActiveFacilities() != null) {
			for (Facility fac : getRequest().getAllActiveFacilities()) {
				if ((fac.getFacilityType() != null)
						&& !StringUtils.equals(fac.getFacilityType().getKey(),
								Constants.BUSINESS_CHECKING_PLUS_KEY)) {
					flag = false;
					break;
				}
			}
		}
		return flag;
	}

	public boolean getLineExceedSalesRevenueRuleResult() {
		LOG.debug("Inside Scoring: getLineExceedSalesRevenueRuleResult() from MVEL..");
		// once defined here. Same content should also be put in
		// scoringCalcRecommRuleApplication.drl
		return false;
	}

	public boolean getExistingAverageBCARuleResult() {
		LOG.debug("Inside Scoring: getExistingAverageBCARuleResult() from MVEL..");
		boolean flag = true;
		int configValue = getScrRcmParamValue(Constants.EXISTING_AVERAGE_BCA_UTILIZATION_KEY_1);

		BigDecimal averageBCAUtil = new BigDecimal(getAverageBCAUtilization());
		if (configValue > 0
				&& averageBCAUtil.compareTo(new BigDecimal(configValue)) > 0) {
			flag = false;
		}
		return flag;
	}

	public boolean getExistingAverageBCPRuleResult() {
		LOG.debug("Inside Scoring: getExistingAverageBCPRuleResult() from MVEL..");
		boolean flag = true;
		int configValue = getScrRcmParamValue(Constants.EXISTING_AVERAGE_BCP_UTILIZATION_KEY_1);

		BigDecimal averageBCPUtil = new BigDecimal(getAverageBCPUtilization());
		if (configValue > 0
				&& averageBCPUtil.compareTo(new BigDecimal(configValue)) > 0) {
			flag = false;
		}
		return flag;
	}

	/**
	 * Method is added for Custom attr calc :- cust_new_existing
	 * 
	 * @return
	 */
	public boolean isCustNewExistingCriteriaMeets() {
		LOG.debug("Inside Scoring : isCustNewExistingCriteriaMeets()");
		boolean flag = false;

		// 1..If the Primary Borrower has an Existing Credit Product
		Exposure exposure = getRequest().getExposure();
		List<CustomerFacilityExposure> directExposures = exposure
				.getDirectFacilityExposures();
		for (CustomerFacilityExposure directExposure : directExposures) {
			if (directExposure.getRelParty().isPrimaryBorrower()) {
				for (FacilityExposure facExposure : directExposure
						.getFacilityExposures()) {

					if (facExposure != null
							&& facExposure.getAccountNumber() != null
							&& !StringUtils.isBlank(facExposure
									.getAccountNumber())) {
						flag = true;
					}
				}
			}
		}
		// 2.. Current Citi Employee has been selected for any Individual party
		for (RelationshipParty party : getRequest().getRelationship()
				.getRelatedParties()) {
			if (party.getCustomer().isIndividual()
					&& party.getCustomer().getCurrCitiEmployee()) {
				flag = true;
			}
		}
		// 3. Other Citi Relationship in Loan Path is Smith Barney, CitiGold, or
		// Gemini
		// citiRelationship
		if (getRequest().getCitiRelationship() != null
				&& getRequest().getCitiRelationship().size() > 0) {
			flag = true;
		}
		return flag;
	}

	public List<PartyScoring> getIndividualPartyScorings() {
		List<PartyScoring> lst = new ArrayList<PartyScoring>();
		for (PartyScoring partySoring : getPartyScorings()) {
			if (partySoring.getParty().isIndividual()) {
				lst.add(partySoring);
			}
		}
		return lst;
	}

	public List<PartyScoring> getNonIndividualPartyScorings() {
		List<PartyScoring> lst = new ArrayList<PartyScoring>();
		for (PartyScoring partySoring : getPartyScorings()) {
			if (partySoring.getParty().isNonIndividual()) {
				lst.add(partySoring);
			}
		}
		return lst;
	}

	public boolean isAddtionalNonIndividualCriteiaPass() {
		LOG.debug("Inside Scoring : isAddtionalNonIndividualCriteiaPass()");
		boolean flag = false;
		List<RelationshipParty> parties = getRequest().getRelationship()
				.getRelatedParties();
		for (RelationshipParty party : parties) {
			if (party.getCustomer() != null
					&& party.getCustomer().isNonIndividual()) {
				if (!party.isPrimaryBorrower()) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	public void initScoring() {
		LOG.debug("Inside Scoring:initScoring()");

		List<BaseDataObject> scrAttributeChoices = LookupService.getResults(
				Constants.SCORE_ATTRIBUTE_CHOICE_BY_APPLICABILITY_NAMED_QUERY,
				Constants.APPLICABILITY_TYPE_KEY,
				Constants.APPLICABILITY_TYPE_APPLICATION_KEY);
		LOG.debug("scrAttributeChoices list size fetched from DB is:- "
				+ scrAttributeChoices.size());
		for (BaseDataObject baseDataObject : scrAttributeChoices) {
			ScrAttributeSeed scrAttributeChoiceSeed = (ScrAttributeSeed) baseDataObject;
			this.scrAttributeSeed = scrAttributeChoiceSeed;
			EntityService es = new EntityService();
			ScoringAttribute scoringAttribute = (ScoringAttribute) es
					.createNew(ScoringAttribute.class);
			scoringAttribute.setAttributeKey(scrAttributeChoiceSeed.getKey());
			scoringAttribute.setAttributeName(scrAttributeChoiceSeed.getName());
			scoringAttribute.setAttributeDescription(scrAttributeChoiceSeed
					.getDescription());

			scoringAttribute.setRequest(getRequest());
			scoringAttribute.setScoring(this);
			addToScoringAttributes(scoringAttribute);
		}

		List<BaseDataObject> scrRecomRules = LookupService.getResults(
				Constants.SCORE_RECOM_RULES_BY_APPLICABILITY_NAMED_QUERY,
				Constants.APPLICABILITY_TYPE_KEY,
				Constants.APPLICABILITY_TYPE_APPLICATION_KEY);
		LOG.debug("scrRecomRules list size fetched from DB is:- "
				+ scrRecomRules.size());
		for (BaseDataObject baseDataObject : scrRecomRules) {

			ScrRcmRuleSeed scrRecomRule = (ScrRcmRuleSeed) baseDataObject;
			this.scrRecomRuleSeed = scrRecomRule;
			EntityService es = new EntityService();
			ScoringRecommRule scoringRecommRule = (ScoringRecommRule) es
					.createNew(ScoringRecommRule.class);
			scoringRecommRule.setRuleKey(scrRecomRule.getKey());
			scoringRecommRule.setRuleName(scrRecomRule.getName());
			scoringRecommRule.setDescription(scrRecomRule.getDescription());

			AttributeChoice attributeChoiceScoreResult = (AttributeChoice) LookupService
					.getResult("AttributeChoice.byKey", "key",
							Constants.SCORING_RECOMM_RESULT_FAIL_KEY);
			scoringRecommRule.setResult(attributeChoiceScoreResult);
			scoringRecommRule.setRecommCategory(scrRecomRule
					.getRecommendationCategory());
			scoringRecommRule.setRecommendationDecision(scrRecomRule
					.getRecommendation());
			scoringRecommRule.setReasonCode((scrRecomRule
					.getRecommendationReasonCode() == null) ? null
					: scrRecomRule.getRecommendationReasonCode().getRecomCode()
							.toString());
			scoringRecommRule
					.setReasonLevel((scrRecomRule.getReasonLevel() == null) ? null
							: scrRecomRule.getReasonLevel().getValue());
			scoringRecommRule.setRecommendationLevel((scrRecomRule
					.getRecommendationLevel() == null) ? null : scrRecomRule
					.getRecommendationLevel().getValue());
			scoringRecommRule.setExceptionAuthority(scrRecomRule
					.getExceptionAuthority());

			scoringRecommRule.setRequest(getRequest());
			scoringRecommRule.setScoring(this);
			addToScoringRecommRules(scoringRecommRule);

		}

	}

	public void setRuleResult(ScoringRecommRule recommRule, boolean result) {
		LOG.debug("Inside Scoring: setRuleResult()");
		LOG.debug("Result is == " + result);
		if (recommRule != null) {
			if (result) {

				AttributeChoice attributeChoiceScoreResult = (AttributeChoice) LookupService
						.getResult("AttributeChoice.byKey", "key",
								Constants.SCORING_RECOMM_RESULT_PASS_KEY);
				recommRule.setResult(attributeChoiceScoreResult);
				recommRule.setRecommCategory(null);
				AttributeChoice attributeChoice = (AttributeChoice) LookupService
						.getResult("AttributeChoice.byKey", "key",
								Constants.RECOMMENDATION_DECISION_APPROVE_KEY);
				recommRule.setRecommendationDecision(attributeChoice);
				recommRule.setReasonCode(Constants.EMPTY_STRING);
				recommRule.setExceptionAuthority(null);
			} else {
				AttributeChoice attributeChoiceScoreResult = (AttributeChoice) LookupService
						.getResult("AttributeChoice.byKey", "key",
								Constants.SCORING_RECOMM_RESULT_FAIL_KEY);
				recommRule.setResult(attributeChoiceScoreResult);

			}
		}

	}

	private ScoringRecommRule getMaxFailedRuleForFacility(
			List<ScoringRecommRule> lstRule) {
		Iterator<ScoringRecommRule> iter = lstRule.iterator();
		// Filtering list on the basis of result
		while (iter.hasNext()) {
			ScoringRecommRule sr = iter.next();
			if (sr != null
					&& sr.getResult() != null
					&& StringUtils.equals(
							Constants.SCORING_RECOMM_RESULT_PASS_KEY, sr
									.getResult().getKey())) {
				iter.remove();
			}
		}
		if (lstRule == null || lstRule.isEmpty()) {
			return null;
		} else {
			// finding the max in all failed rules
			iter = lstRule.iterator();
			ScoringRecommRule max = lstRule.get(0);
			for (int i = 1; i < lstRule.size(); i++) {
				ScoringRecommRule toCom = lstRule.get(i);
				if (toCom.getRecommendationLevel() != null
						&& NumberUtils.isNumber(toCom.getRecommendationLevel())
						&& max.getRecommendationLevel() != null
						&& NumberUtils.isNumber(max.getRecommendationLevel())
						&& Double.parseDouble(toCom.getRecommendationLevel()) == Double
								.parseDouble(max.getRecommendationLevel())) {

					if (toCom.getRecommCategory() != null
							&& StringUtils
									.equals(Constants.RECOMMENDATION_CATEGORY_PRE_DECLINE_REVIEW_KEY,
											toCom.getRecommCategory().getKey())
							&& !StringUtils
									.equals(Constants.RECOMMENDATION_CATEGORY_PRE_DECLINE_REVIEW_KEY,
											max.getRecommCategory().getKey())) {
						max = toCom;
					} else {
					}
				} else if (toCom.getRecommendationLevel() != null
						&& NumberUtils.isNumber(toCom.getRecommendationLevel())
						&& max.getRecommendationLevel() != null
						&& NumberUtils.isNumber(max.getRecommendationLevel())
						&& Double.parseDouble(toCom.getRecommendationLevel()) > Double
								.parseDouble(max.getRecommendationLevel())) {

					max = toCom;
				}
			}
			return max;
		}
	}

	private ScoringRecommRule getMaxFailedRuleForParty(
			List<ScoringRecommRule> lstRule) {
		Iterator<ScoringRecommRule> iter = lstRule.iterator();
		// Filtering list on the basis of result
		while (iter.hasNext()) {
			ScoringRecommRule sr = iter.next();
			if (sr != null
					&& sr.getResult() != null
					&& StringUtils.equals(
							Constants.SCORING_RECOMM_RESULT_PASS_KEY, sr
									.getResult().getKey())) {
				iter.remove();
			}
		}
		if (lstRule == null || lstRule.isEmpty()) {
			return null;
		} else {
			// finding the max in all failed rules
			iter = lstRule.iterator();
			ScoringRecommRule max = lstRule.get(0);
			for (int i = 1; i < lstRule.size(); i++) {
				ScoringRecommRule toCom = lstRule.get(i);
				if (toCom.getRecommendationLevel() != null
						&& NumberUtils.isNumber(toCom.getRecommendationLevel())
						&& max.getRecommendationLevel() != null
						&& NumberUtils.isNumber(max.getRecommendationLevel())
						&& Double.parseDouble(toCom.getRecommendationLevel()) == Double
								.parseDouble(max.getRecommendationLevel())) {

					if (toCom.getRecommendationDecision() != null
							&& StringUtils
									.equals(Constants.RECOMMENDATION_DECISION_DECLINE_KEY,
											toCom.getRecommendationDecision()
													.getKey())
							&& StringUtils
									.equals(Constants.RECOMMENDATION_DECISION_REVIEW_KEY,
											max.getRecommendationDecision()
													.getKey())) {
						max = toCom;
					} else {
					}
				} else if (toCom.getRecommendationLevel() != null
						&& NumberUtils.isNumber(toCom.getRecommendationLevel())
						&& max.getRecommendationLevel() != null
						&& NumberUtils.isNumber(max.getRecommendationLevel())
						&& Double.parseDouble(toCom.getRecommendationLevel()) > Double
								.parseDouble(max.getRecommendationLevel())) {

					max = toCom;
				}
			}
			return max;
		}
	}

	public ScoringRecommRule getFailedRuleWithMaxRecommLevel(
			List<ScoringRecommRule> lstRule, String applicability) {
		if (StringUtils.equals(applicability,
				Constants.RECOMMENDATION_TYPE_FACILITY_KEY)) {
			return getMaxFailedRuleForFacility(lstRule);
		} else if (StringUtils.equals(applicability,
				Constants.RECOMMENDATION_TYPE_PARTY_KEY)) {
			return getMaxFailedRuleForParty(lstRule);
		} else{
			return null;
		}
	}

	public ScoringAttribute findAttributeByKey(String attributeKey) {
		LOG.debug("Inside Scoring: findAttributeByKey()");

		List<ScoringAttribute> allScoringAttribute = new ArrayList<ScoringAttribute>();
		allScoringAttribute.addAll(this.getScoringAttributes());
		for (FacScoring facScoring : this.getFacScorings()) {
			allScoringAttribute.addAll(facScoring.getScoringAttributes());
		}
		for (PartyScoring partyScoring : this.getPartyScorings()) {
			allScoringAttribute.addAll(partyScoring.getScoringAttributes());
		}
		for (ScoringAttribute attr : allScoringAttribute) {
			if (attr.getAttributeKey().equals(attributeKey)) {
				LOG.debug("Attribute found...");
				return attr;
			}
		}
		return null;

	}

	public ScoringRecommRule findRecommRuleByKey(String ruleKey) {
		LOG.debug("Inside Scoring: findRecommRuleByKey()");

		List<ScoringRecommRule> allScoringRecommRule = new ArrayList<ScoringRecommRule>();
		allScoringRecommRule.addAll(this.getScoringRecommRules());
		for (FacScoring facScoring : this.getFacScorings()) {
			allScoringRecommRule.addAll(facScoring.getScoringRecommRules());
		}
		for (PartyScoring partyScoring : this.getPartyScorings()) {
			allScoringRecommRule.addAll(partyScoring.getScoringRecommRules());
		}
		for (ScoringRecommRule rule : allScoringRecommRule) {
			if (rule.getRuleKey().equals(ruleKey)) {
				LOG.debug("RecommRule found...");
				return rule;
			}
		}
		return null;

	}

	public String getApplicationScoringDecision() {
		boolean approvedFlag = false;
		boolean declineFlag = false;
		boolean reviewFlag = false;
		String applicationScoringDecision = "";
		if (getFacScorings() != null) {
			for (FacScoring facScoring : getFacScorings()) {
				if (facScoring.getFacilityRecomm() != null
						&& facScoring.getFacilityRecomm().getRecommId() != null
						&& StringUtils.equals(facScoring.getFacilityRecomm()
								.getRecommId().getKey(),
								Constants.RECOMMENDATION_DECISION_APPROVE_KEY)) {
					approvedFlag = true;
				}
				if (facScoring.getFacilityRecomm() != null
						&& facScoring.getFacilityRecomm().getRecommId() != null
						&& StringUtils.equals(facScoring.getFacilityRecomm()
								.getRecommId().getKey(),
								Constants.RECOMMENDATION_DECISION_DECLINE_KEY)) {
					declineFlag = true;
				}
				if (facScoring.getFacilityRecomm() != null
						&& facScoring.getFacilityRecomm().getRecommId() != null
						&& StringUtils.equals(facScoring.getFacilityRecomm()
								.getRecommId().getKey(),
								Constants.RECOMMENDATION_DECISION_REVIEW_KEY)) {
					reviewFlag = true;
				}
			}
			if (approvedFlag && (!declineFlag) && (!reviewFlag)) {
				applicationScoringDecision = Constants.RECOMMENDATION_DECISION_APPROVE_KEY;
			} else if (declineFlag && (!approvedFlag) && (!reviewFlag)) {
				applicationScoringDecision = Constants.RECOMMENDATION_DECISION_DECLINE_KEY;
			} else {
				applicationScoringDecision = Constants.RECOMMENDATION_DECISION_REVIEW_KEY;
			}
		}
		return applicationScoringDecision;
	}

	public void setFinalExceptionAuthority() {
		LOG.debug("Inside Scoring : setFinalExceptionAuthority()");
		List<ScoringRecommRule> lstRules = new ArrayList<ScoringRecommRule>();
		if (this.getScoringRecommRules() != null) {
			lstRules.addAll(this.getScoringRecommRules());
		}
		for (PartyScoring partyScoring : this.getPartyScorings()) {
			if (partyScoring.getScoringRecommRules() != null) {
				lstRules.addAll(partyScoring.getScoringRecommRules());
			}
		}
		for (FacScoring facScoring : this.getFacScorings()) {
			if (facScoring.getScoringRecommRules() != null) {
				lstRules.addAll(facScoring.getScoringRecommRules());
			}
		}
		ScoringRecommRule highestFailedrecommRule = getFailedRuleWithMaxRecommLevel(
				lstRules, Constants.RECOMMENDATION_TYPE_FACILITY_KEY);
		if (highestFailedrecommRule == null) {
			this.setExceptionAuthority(null);

		} else {
			this.setExceptionAuthority(highestFailedrecommRule
					.getExceptionAuthority());
		}
	}

	// Existing Exposure Amount for Facility Type BCA(Existing + Pipeline) +
	// Exposure Adjustments for Facility Type BCA
	public BigDecimal calExistingExposureAmount_AdjustmentForBCA() {
		BigDecimal existingExposureAmountBCA = getExistingExposureAmountBCA() == null ? BigDecimal.ZERO
				: new BigDecimal(getExistingExposureAmountBCA());
		existingExposureAmountBCA = existingExposureAmountBCA
				.add(getPipelineAmountBCA() == null ? BigDecimal.ZERO
						: new BigDecimal(getPipelineAmountBCA()));
		existingExposureAmountBCA = existingExposureAmountBCA
				.add(getExposureAdjustmentsBCA() == null ? BigDecimal.ZERO
						: new BigDecimal(getExposureAdjustmentsBCA()));
		return existingExposureAmountBCA;
	}

	// Credit_and _Risk_Policy_Solution_v5.3 6.1.2.25 3rd
	public BigDecimal minimumGridCap_ExistingExposureAmount_Adjustment_BCA() {
		BigDecimal gridCapBCP = BigDecimal.ZERO;
		if (getGridCapBCA() != null && NumberUtils.isNumber(getGridCapBCA())){
			gridCapBCP = new BigDecimal(getGridCapBCA());
		}
		// 3.a
		BigDecimal gridCapFor = gridCapBCP
				.subtract(calExistingExposureAmount_AdjustmentForBCA());

		// 3.b
		BigDecimal bcaFacTypMax = BigDecimal.ZERO;
		if (getMaxValueBCA() != null && NumberUtils.isNumber(getMaxValueBCA())){
			bcaFacTypMax = new BigDecimal(getMaxValueBCA());
		}
		// 3.c
		BigDecimal newRequestedAmtBCA = BigDecimal.ZERO;
		if (getNewRequestedAmountBCA() != null
				&& NumberUtils.isNumber(getNewRequestedAmountBCA())){
			newRequestedAmtBCA = new BigDecimal(getNewRequestedAmountBCA());
		}

		BigDecimal smallest = gridCapFor.min(bcaFacTypMax);
		smallest = smallest.min(newRequestedAmtBCA);
		// 3 Note
		if (getMissingFicoOrMissingCustomRuleResult()) {
			smallest = smallest.divide(new BigDecimal(2));
		}
		return smallest;
	}

	// Existing Exposure Amount for Facility Type BCP(Existing + Pipeline) +
	// Exposure Adjustments for Facility Type BCP)
	public BigDecimal calExistingExposureAmount_AdjustmentForBCP() {
		BigDecimal existingExposureAmountBCP = getExistingExposureAmountBCP() == null ? BigDecimal.ZERO
				: new BigDecimal(getExistingExposureAmountBCP());
		existingExposureAmountBCP = existingExposureAmountBCP
				.add(getPipelineAmountBCP() == null ? BigDecimal.ZERO
						: new BigDecimal(getPipelineAmountBCP()));
		existingExposureAmountBCP = existingExposureAmountBCP
				.add(getExposureAdjustmentsBCP() == null ? BigDecimal.ZERO
						: new BigDecimal(getExposureAdjustmentsBCP()));
		return existingExposureAmountBCP;
	}

	public BigDecimal calExistingExposureAmount_AdjustmentForBIL() {
		BigDecimal existingExposureAmountBIL = getExistingExposureAmountBIL() == null ? BigDecimal.ZERO
				: new BigDecimal(getExistingExposureAmountBIL());
		existingExposureAmountBIL = existingExposureAmountBIL
				.add(getPipelineAmountBIL() == null ? BigDecimal.ZERO
						: new BigDecimal(getPipelineAmountBIL()));
		existingExposureAmountBIL = existingExposureAmountBIL
				.add(getExposureAdjustmentsBIL() == null ? BigDecimal.ZERO
						: new BigDecimal(getExposureAdjustmentsBIL()));
		return existingExposureAmountBIL;
	}

	public BigDecimal minimumGridCap_ExistingExposureAmount_Adjustment_BCP() {
		BigDecimal gridCapBCP = BigDecimal.ZERO;
		if (getGridCapBCP() != null && NumberUtils.isNumber(getGridCapBCP())){
			gridCapBCP = new BigDecimal(getGridCapBCP());
		}
		// 3.a
		BigDecimal gridCapFor = gridCapBCP
				.subtract(calExistingExposureAmount_AdjustmentForBCP());

		// 3.b
		BigDecimal bcpFacTypMax = BigDecimal.ZERO;
		if (getMaxValueBCP() != null && NumberUtils.isNumber(getMaxValueBCP())){
			bcpFacTypMax = new BigDecimal(getMaxValueBCP());
		}
		// 3.c
		BigDecimal newRequestedAmtBCP = BigDecimal.ZERO;
		if (getNewRequestedAmountBCP() != null
				&& NumberUtils.isNumber(getNewRequestedAmountBCP())){
			newRequestedAmtBCP = new BigDecimal(getNewRequestedAmountBCA());
		}

		BigDecimal smallest = gridCapFor.min(bcpFacTypMax);
		smallest = smallest.min(newRequestedAmtBCP);
		return smallest;
	}

	public BigDecimal minimumGridCap_ExistingExposureAmount_Adjustment_BIL() {
		BigDecimal gridCapBIL = BigDecimal.ZERO;
		if (getGridCapBIL() != null && NumberUtils.isNumber(getGridCapBIL())){
			gridCapBIL = new BigDecimal(getGridCapBIL());
		}
		// 3.a
		BigDecimal gridCapFor = gridCapBIL
				.subtract(calExistingExposureAmount_AdjustmentForBIL());

		// 3.b
		BigDecimal bilFacTypMax = BigDecimal.ZERO;
		if (getMaxValueBIL() != null && NumberUtils.isNumber(getMaxValueBIL())){
			bilFacTypMax = new BigDecimal(getMaxValueBIL());
		}
		// 3.c
		BigDecimal newRequestedAmtBIL = BigDecimal.ZERO;
		if (getNewRequestedAmountBIL() != null
				&& NumberUtils.isNumber(getNewRequestedAmountBIL())){
			newRequestedAmtBIL = new BigDecimal(getNewRequestedAmountBCA());
		}

		BigDecimal smallest = gridCapFor.min(bilFacTypMax);
		smallest = smallest.min(newRequestedAmtBIL);
		// 3 Note
		if (getMissingFicoOrMissingCustomRuleResult()) {
			smallest = smallest.divide(new BigDecimal(2));
		}
		return smallest;
	}

	public String getTestOgnl() {
		return null;
	}

	public boolean getTestOgnlRcmRule() {
		return false;
	}
}
