/*
 * Copyright (c) 2005-2013 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
/**
 *
 */
package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.model.DocumentAware;
import com.thirdpillar.foundation.model.Money;
import com.thirdpillar.codify.loanpath.model.*;
import com.thirdpillar.foundation.model.WorkflowAware;
import com.thirdpillar.codify.loanpath.model.Address;
import com.thirdpillar.foundation.service.ContextHolder;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;
import com.thirdpillar.foundation.util.StringUtils;
import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;
import com.thirdpillar.codify.loanpath.constants.Constants;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Transient;
import javax.swing.text.MaskFormatter;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Sachin.Sharma
 */
@XStreamLookupCollectionByOGNL.List(
	    {
	        @XStreamLookupCollectionByOGNL(
	            name = "byExternalIdentifier",
	            keys = { "externalIdentifier" }
	        )/*,
	    	@XStreamLookupCollectionByOGNL(
	           name = "byServicingIdentifier",
	           keys = { "servicingIdentifier" }
	        )*/,
	        @XStreamLookupCollectionByOGNL(
	           name = "byFein",
	           keys = { "customerFEIN" }
	        ),
	        @XStreamLookupCollectionByOGNL(
	 	           name = "bySsn",
	 	           keys = { "customerSSN" }
	 	        ),
		        @XStreamLookupCollectionByOGNL(
			 	           name = "byRefNumber",
			 	           keys = { "refNumber" }
			 	        )
	    }
	)
public class Customer {

	@Transient
	private boolean isDebtor;
	@Transient
	private boolean isAccount;
	@Transient
	private String duplicateCustomer="";
	
	@Transient
	private String fieldName="";

	// ~ Methods
	// --------------------------------------------------------------------------------------------------------

	/**
	 * @return all generated documents associated to a customer (party) in a
	 *         request
	 */
	public List<Document> getAllDocuments() {
		String reqClass = Request.class.getName();
		EntityService<Document> docES = new EntityService<Document>();
		List<Document> docList = docES
				.find("select request.documentGroup.generatedDocuments from "
						+ reqClass
						+ " request join request.relationship.allRelationshipParties parties where parties.customer.id="
						+ this.getId());
		return docList;
	}

	/**
	 * @return String containing "lastName, FirstName" if party is individual
	 *         and legalName otherwise.
	 */
	public String getCustomerName() {

		if ((getCustomerType() != null)
				&& StringUtils.equals("CUSTOMER_TYPE_INDIVIDUAL",
						getCustomerType().getKey())) {
			return getLastName() + ", " + getFirstName();
		}
			return getLegalName();
	}

	public boolean isCustomerType(String key) {

		if ((getCustomerType() != null)
				&& key.equalsIgnoreCase(getCustomerType().getKey())) {
			return true;
		}
			return false;
	}

	public boolean isCompanyType(String key) {
		if ((getCompanyType() != null)
				&& key.equalsIgnoreCase(getCompanyType().getKey())) {
			return true;
		}
			return false;
	}

	/**
	 * @return all generated docs associated to a customer (party) in a request
	 *         where the documents require esignature and not wetSignature and
	 *         has document status of approved ("DOCUMENT_STATUS_APPROVED") and
	 *         this signer has not yet signed the document (i.e.
	 *         documentSigner.getDocsignedFlag is false)
	 */
	public List<Document> getEsignatureRqdDocs() {

		// Filtering out wetSignature docs
		// TODO: try to do the filtering in a query so we don't have to process
		// the doc list
		List<Document> docList = getAllDocuments();

		if (docList != null) {
			Set<Document> esignDocs = new LinkedHashSet<Document>();

			for (Document doc : docList) {
				boolean isApprovedDocument = (doc.getDocumentStatus() != null) ? "DOCUMENT_STATUS_APPROVED"
						.equals(doc.getDocumentStatus().getKey()) : false;
				boolean isDocumentSigner = false;
				boolean isSigningComplete = false;

				for (DocumentSigner signer : doc.getDocumentSigners()) {

					// Use Customer refNumber for associating document signer to
					// document
					String externalIdentifier = this.getRefNumber();

					if ((externalIdentifier != null)
							&& externalIdentifier.equals(signer
									.getSignerRefId())) {
						isDocumentSigner = true;
						isSigningComplete = signer.getDocSignedFlag();

						break;
					}
				}

				// Removed check for doc.isShared()
				boolean include = isDocumentSigner && !isSigningComplete
						&& doc.getEsignatureRqd() && !doc.getWetSignature()
						&& isApprovedDocument;

				if (include) {
					esignDocs.add(doc);
				}
			}

			docList = new ArrayList<Document>(esignDocs);
		}

		return docList;
	}

	public String getFormattedTaxIDNum() throws ParseException {
		String finalStr = "";
		String taxIdNumber = "";

		if (getCustomerType() != null) {
			MaskFormatter mf = null;
			if (isIndividual()) {
				mf = new MaskFormatter("***-**-****");
				taxIdNumber = getSsn();
			} else if (isNonIndividual() || isCounterParty()) {
				mf = new MaskFormatter("**-*******");
				taxIdNumber = getFederalTaxId();
			}

			mf.setValueContainsLiteralCharacters(false);

			if (StringUtils.isNotEmpty(taxIdNumber)) {
				StringBuffer formattedString = new StringBuffer(
						mf.valueToString(new StringBuffer(taxIdNumber).replace(
								0, taxIdNumber.length() - 4, "*****")));

				finalStr = formattedString.toString();
			}
		}

		return finalStr;
	}

	public boolean isIndividual() {
		return isCustomerType("CUSTOMER_TYPE_INDIVIDUAL");
	}

	public boolean isNonIndividual() {
		return isCustomerType("CUSTOMER_TYPE_NON_INDIVIDUAL");
	}

	public boolean isCounterParty() {
		return isCustomerType("CUSTOMER_TYPE_COUNTER_PARTY");
	}

	public boolean isBank() {
		return isCompanyType("COMPANY_TYPE_BANK");
	}

	public AttributeChoice getOrr() {
			if (isNonIndividual() && this.getRiskRatings() != null) {
				for (RiskRating riskRating : this.getRiskRatings()) {
					return riskRating.getObligorRR();
				}
			}

		return null;
	}

	public Money getTotalExposureAmt() {
		List<CustomerExposureLimit> custExposureLimits = getCustomerExposureLimits();
		Money total = new Money(0);
		for (CustomerExposureLimit exposureLimits : custExposureLimits) {
			if (exposureLimits.getExposureAmt() != null) {
				total = total.add(exposureLimits.getExposureAmt());
			}
		}
		return total;
	}

	public Money getTotalExposureLimitAmt() {
		List<CustomerExposureLimit> custExposureLimits = getCustomerExposureLimits();
		Money total = new Money(0);
		for (CustomerExposureLimit exposureLimits : custExposureLimits) {
			if (exposureLimits.getExposureLimitAmt() != null) {
				total = total.add(exposureLimits.getExposureLimitAmt());
			}
		}
		return total;
	}

	public CustomerExposureLimit getSecuritiesLendingExposure() {
		return getCustomerExposureLimitByProductType("EXPOSURE_LIMIT_FACILITY_TYPE_SECURITIES_LENDING");
	}

	public CustomerExposureLimit getReposExposure() {
		return getCustomerExposureLimitByProductType("EXPOSURE_LIMIT_FACILITY_TYPE_REPOS");
	}

	public CustomerExposureLimit getReverseReposExposure() {
		return getCustomerExposureLimitByProductType("EXPOSURE_LIMIT_FACILITY_TYPE_REVERSE_REPOS");
	}

	public CustomerExposureLimit getPlacementsExposure() {
		return getCustomerExposureLimitByProductType("EXPOSURE_LIMIT_FACILITY_TYPE_PLACEMENTS");
	}

	public CustomerExposureLimit getFedFundsExposure() {
		return getCustomerExposureLimitByProductType("EXPOSURE_LIMIT_FACILITY_TYPE_FED_FUNDS");
	}

	public CustomerExposureLimit getSettlementExposure() {
		return getCustomerExposureLimitByProductType("EXPOSURE_LIMIT_FACILITY_TYPE_FX_SETTLEMENT_AND_FX_PRE_SETTLEMENT");
	}

	public CustomerExposureLimit getSafekeepingExposure() {
		return getCustomerExposureLimitByProductType("EXPOSURE_LIMIT_FACILITY_TYPE_SAFEKEEPING");
	}

	public CustomerExposureLimit getNostroBalanceExposure() {
		return getCustomerExposureLimitByProductType("EXPOSURE_LIMIT_FACILITY_TYPE_NOSTRO_BALANCES_DUE_FROM");
	}

	public CustomerExposureLimit getInterestRateDerivativesExposure() {
		return getCustomerExposureLimitByProductType("EXPOSURE_LIMIT_FACILITY_TYPE_INTEREST_RATE_DERIVATIVES");
	}

	public CustomerExposureLimit getCreditDefaultSwapsExposure() {
		return getCustomerExposureLimitByProductType("EXPOSURE_LIMIT_FACILITY_TYPE_CREDIT_DEFAULT_SWAPS");
	}

	public CustomerExposureLimit getCoveredBondsExposure() {
		return getCustomerExposureLimitByProductType("EXPOSURE_LIMIT_FACILITY_TYPE_COVERED_BONDS");
	}

	public CustomerExposureLimit getCorporateDebtExposure() {
		return getCustomerExposureLimitByProductType("EXPOSURE_LIMIT_FACILITY_TYPE_CORPORATE_DEBT");
	}

	public CustomerExposureLimit getFxInOutSwapsExposure() {
		return getCustomerExposureLimitByProductType("EXPOSURE_LIMIT_FACILITY_TYPE_FX_IN_OUT_SWAPS");
	}

	public CustomerExposureLimit getUnallocatedLimitsExposure() {
		return getCustomerExposureLimitByProductType("EXPOSURE_LIMIT_FACILITY_TYPE_UNALLOCATED_LIMITS");
	}

	public CustomerExposureLimit getCountryLimitsExposure() {
		return getCustomerExposureLimitByProductType("EXPOSURE_LIMIT_FACILITY_TYPE_COUNTRY_LIMITS");
	}

	public Money getUnusedCreditLines() {
		Money value = new Money(0);
		if (getTotalExposureLimitAmt() != null) {
			value = value.add(getTotalExposureLimitAmt());
		}
		if (getTotalExposureAmt() != null) {
			value = value.subtract(getTotalExposureAmt());
		}
		return value;
	}

	public Money getTotalEligibleCollateral() {
		Money value = new Money(0);
		if (getEligibleGuarantees() != null) {
			value = value.add(getEligibleGuarantees());
		}
		if (getEligibleEquityCreditDer() != null) {
			value = value.add(getEligibleEquityCreditDer());
		}
		if (getOtherEligigbleHedges() != null) {
			value = value.add(getOtherEligigbleHedges());
		}
		if (getEffectOfBilateralAgreements() != null) {
			value = value.add(getEffectOfBilateralAgreements());
		}
		if (getTotalNegotiableSecurities() != null) {
			value = value.add(getTotalNegotiableSecurities());
		}
		return value;
	}

	public Money getNetExposure() {
		Money value = new Money(0);
		if (getTotalExposureAmt() != null) {
			value = value.add(getTotalExposureAmt());
		}
		if (getTotalEligibleCollateral() != null) {
			value = value.subtract(getTotalEligibleCollateral());
		}
		return value;
	}

	public CustomerExposureLimit getCustomerExposureLimitByProductType(
			String productType) {

		if (getCustomerExposureLimits() != null) {
			for (CustomerExposureLimit exposureLimit : getCustomerExposureLimits()) {
				if (exposureLimit.getProductType() != null
						&& StringUtils.equals(exposureLimit.getProductType()
								.getKey(), productType)) {
					return exposureLimit;
				}
			}
		}
		return null;
	}

	public Money getTotalNegotiableSecurities() {
		List<NegotiableSecurity> securities = getNegotiableSecurities();
		Money total = new Money(0);
		for (NegotiableSecurity security : securities) {
			if (security.getAssetValue() != null) {
				total = total.add(security.getAssetValue());
			}
		}
		return total;
	}

	public void setExposureLimit(String exposureProductType, Money limit,
			Date expiryDate) {

		CustomerExposureLimit exposureLimit = getCustomerExposureLimitByProductType(exposureProductType);
		if (exposureLimit == null) {
			EntityService es = new EntityService();
			exposureLimit = (CustomerExposureLimit) es
					.createNew(CustomerExposureLimit.class);
			exposureLimit.setProductType((AttributeChoice) LookupService
					.getResult("AttributeChoice.byKey", "key",
							exposureProductType));
			exposureLimit.setCustomer(this);
			getCustomerExposureLimits().add(exposureLimit);
		}

		exposureLimit.setExposureLimitAmt(limit);
		exposureLimit.setExpiryDate(expiryDate);
	}

	public String getObligorNumberDer() {
		return getObligorNumber();
	}

	public String getGuarantrAddress() {
		StringBuilder addressNew = new StringBuilder();
		if (this.getPrimarySite() != null
				&& this.getPrimarySite().getSiteDetails() != null
				&& this.getPrimarySite().getSiteDetails().getAddresses() != null
				&& !this.getPrimarySite().getSiteDetails().getAddresses()
						.isEmpty()) {
			Address address = getPrimarySite().getSiteDetails().getAddresses()
					.get(0);
			addressNew.append(address.getAddress1());
			addressNew.append(" ");
			addressNew.append(address.getAddress2());
		}
		return addressNew.toString();
	}

	public String getSosWebsite() {
		State state = (State) getStateIncorporated();
		if (state != null) {
			String iso = state.getIsoCode();
			if ("CA".equalsIgnoreCase(iso)) {
				return "http://www.ss.ca.gov/";
			} else if ("NY".equalsIgnoreCase(iso)) {
				return "http://www.dos.state.ny.us/";
			} else if ("NJ".equalsIgnoreCase(iso)) {
				return "http://www.state.nj.us/state/";
			} else if ("DC".equalsIgnoreCase(iso)) {
				return "http://www.dc.gov/";
			} else if ("TX".equalsIgnoreCase(iso)) {
				return "http://www.sos.state.tx.us/";
			} else if ("IL".equalsIgnoreCase(iso)) {
				return "http://www.cyberdriveillinois.com/";
			}
		}
		return null;

	}

	public Boolean getSsnVerified() {
		Boolean match = true;
		// Get all customers from database with provided SSN number.
		EntityService entityService = new EntityService();
		List<Customer> existingCustomers = entityService
				.findByNamedQueryAndNamedParam(
						"com.thirdpillar.codify.loanpath.model.Customer.bySsn",
						"customerSSN", this.getSsn());
		// should only have one, lets only take the first one in the list
		Customer existingCustomer = null;
		for (Customer c : existingCustomers) {
			if (!(this.getId() != null && this.getId().equals(c.getId()))) {
				existingCustomer = c;
				break;
			}
		}
			// 1. Will check the DOB with Existing customer & will ignore if DOB
			// not provided
			if (existingCustomer != null && this.getBirthDate() != null && existingCustomer.getBirthDate() != null && !this.getBirthDate().equals(existingCustomer.getBirthDate())) {
					match = false;
			}
		return match;
	}

	/**
	 * Method to check valid Naics Code
	 * @return false if Naics Code is invalid.
	 */
//	public boolean isValidNaicsCode() {
//		boolean match = false;
//		// Get all customers from database with provided NAICS CODE.
//		EntityService entityService = new EntityService();
//		if(this.getNaicsCode() !=null && this.getNaicsCode().getKey() !=null){
//			AttributeChoice existingNaicsCode = (AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key",this.getNaicsCode().getKey());
//			// should only have one, lets only take the first one in the list
//			if(existingNaicsCode != null){
//				match = true;
//			}
//		}
//		return match;
//	}
	
	/**
	 * Method to get no. of years in Business.
	 * 
	 * @return yearsInBusiness
	 */

	/**
	 * Method to compare years of ownership from Years in Business in
	 * Counterparty.
	 */
	public boolean compareYearsOfOwnership() {
		boolean match = true;
		if (getYearsInOwnership() != null && getYearsInBusiness() != null) {
			String yearsInOwn = getYearsInOwnership();
			int yearsInOwnership = Integer.parseInt(yearsInOwn);
			int yearsInBusiness = getYearsInBusiness();
			if (yearsInOwnership > yearsInBusiness) {
				match = false;
			}
		}
		return match;

	}

	/**
	 * Method to get no. of years in Business as a text.
	 * 
	 * @return yearsInBusiness
	 */
	public String getYearsInBusinessDer() {
		int yearsInBusiness = 0;
		if (getYearsInBusiness() != null) {
			yearsInBusiness = getYearsInBusiness();
		}

		if (yearsInBusiness > 0) {
			return yearsInBusiness + " years";
		}
		return "";
	}

	public Integer getAgeofCustomer() {
		int age = 0;
		Calendar cal = Calendar.getInstance();
		if (getBirthDate() != null) {
			cal.setTime(getBirthDate());
			int yearofBirth = cal.get(Calendar.YEAR);
			cal.setTime(new Date());
			int currentYear = cal.get(Calendar.YEAR);
			age = currentYear - yearofBirth;
		}
		return age;
	}

	public List<Collateral> getAllCollaterals() {
		List<Collateral> list = new ArrayList<Collateral>();
		for (Collateral col : getCollaterals()) {
			list.add(col);
		}
		return list;
	}

	/**
	 * This method returns the Sum of CurrentBalance of all CITI Bank's DDA AC
	 * that a particular Party has.
	 * 
	 * @return sum of Current Balance.
	 */
	public Money getSumOfCurrentBalanceDDA() {
		Money total = new Money(0);
		
		List<BankAndTrade> lstBankTrades = this.getBankTrades();

		for (BankAndTrade banktrade : lstBankTrades) {
			String bankName = "", accountTypeKey = "";
			AttributeChoice accountType = (AttributeChoice) banktrade
					.getAccountType();
			bankName = banktrade.getName();

			if ((accountType != null) && (accountType.getKey() != null)) {
				accountTypeKey = accountType.getKey();
			}

			if (Constants.BANK_ACCOUNT_TYPE_DDA_KEY
					.equalsIgnoreCase(accountTypeKey)
					&& "CITI".equalsIgnoreCase(bankName.trim())) {
				Money currentBalance = banktrade.getPresentBalance();
				currentBalance = (currentBalance == null) ? new Money(0)
						: currentBalance;
				total = total.add(currentBalance);
			} else {
			}
		}

		return total;
	}

	/**
	 * This method returns the Sum of Average Balance of all CITI Bank's DDA AC
	 * that a particular Party has.
	 * 
	 * @return sum of Average Balance for DDA AC.
	 */
	public Money getSumOfAverageBalanceDDA() {
		Money total = new Money(0);
		
		List<BankAndTrade> lstBankTrades = this.getBankTrades();

		for (BankAndTrade banktrade : lstBankTrades) {
			String bankName = "", accountTypeKey = "";
			AttributeChoice accountType = (AttributeChoice) banktrade
					.getAccountType();
			bankName = banktrade.getName();

			if ((accountType != null) && (accountType.getKey() != null)) {
				accountTypeKey = accountType.getKey();
			}

			if (Constants.BANK_ACCOUNT_TYPE_DDA_KEY
					.equalsIgnoreCase(accountTypeKey)
					&& "CITI".equalsIgnoreCase(bankName.trim())) {
				Money averageBalance = banktrade.getAverageBalance();
				averageBalance = (averageBalance == null) ? new Money(0)
						: averageBalance;
				total = total.add(averageBalance);
			} else {
			}
		}

		return total;
	}

	/**
	 * This method returns the AnnualSales for a Party.
	 * 
	 * @return Annual Sales for a Particular party.
	 */
	public Money getAnuualSales() {
		Money total = new Money(0);
		
		if (this.getBusinessDetail() != null) {
			total = ((this.getBusinessDetail().getAnnualRevenue()) == null) ? new Money(
					0) : this.getBusinessDetail().getAnnualRevenue();
		}

		return total;
	}

	/**
	 * This method returns the Sum of CurrentBalance of all CITI Bank's Savings
	 * OR DDA OR CHECKING AC that a particular Party has.
	 * 
	 * @return sum of Current Balance.
	 */
	public Money getSumOfCurrentBalanceSavingsDDAChecking() {
		Money total = new Money(0);
		
		List<BankAndTrade> lstBankTrades = this.getBankTrades();
		boolean flag = false;

		for (BankAndTrade banktrade : lstBankTrades) {
			String bankName = "", accountTypeKey = "";
			AttributeChoice accountType = (AttributeChoice) banktrade
					.getAccountType();
			bankName = banktrade.getName();

			if ((accountType != null) && (accountType.getKey() != null)) {
				accountTypeKey = accountType.getKey();
			}

			if ((Constants.BANK_ACCOUNT_TYPE_SAVINGS_KEY
					.equalsIgnoreCase(accountTypeKey)
					|| Constants.BANK_ACCOUNT_TYPE_DDA_KEY
							.equalsIgnoreCase(accountTypeKey) || Constants.BANK_ACCOUNT_TYPE_CHECKING_KEY
						.equalsIgnoreCase(accountTypeKey))
					&& "CITI".equalsIgnoreCase(bankName.trim())) {
				Money currentBalance = banktrade.getPresentBalance();
				currentBalance = (currentBalance == null) ? new Money(0)
						: currentBalance;
				total = total.add(currentBalance);
				flag = true;
			} else {
			}
		}

		if (!flag) {
			return new Money(-1);
		}

		return total;
	}

	/**
	 * This method returns the Sum of CurrentBalance of all CITI Bank's Savings
	 * OR DDA AC that a particular Party has.
	 * 
	 * @return sum of Current Balance.
	 */
	public Money getSumOfCurrentBalanceSavingsDDA() {
		Money total = new Money(0);
		
		List<BankAndTrade> lstBankTrades = this.getBankTrades();

		for (BankAndTrade banktrade : lstBankTrades) {
			String bankName = "", accountTypeKey = "";
			AttributeChoice accountType = (AttributeChoice) banktrade
					.getAccountType();
			bankName = banktrade.getName();

			if ((accountType != null) && (accountType.getKey() != null)) {
				accountTypeKey = accountType.getKey();
			}

			if ((Constants.BANK_ACCOUNT_TYPE_SAVINGS_KEY
					.equalsIgnoreCase(accountTypeKey) || Constants.BANK_ACCOUNT_TYPE_DDA_KEY
					.equalsIgnoreCase(accountTypeKey))
					&& "CITI".equalsIgnoreCase(bankName.trim())) {
				Money currentBalance = banktrade.getPresentBalance();
				currentBalance = (currentBalance == null) ? new Money(0)
						: currentBalance;
				total = total.add(currentBalance);
			} else {
			}
		}

		return total;
	}

	public Integer getTimeInbusiness() {

		int yearsInBusiness = 0;
		Calendar cal = Calendar.getInstance();
		if (getBusinessStartDttm() != null) {
			cal.setTime(getBusinessStartDttm());
			int businessStartYear = cal.get(Calendar.YEAR);
			int createdYear = 0;
			if (this.getMetaInfo() != null
					&& this.getMetaInfo().getCreatedAt() != null) {
				cal.setTime(this.getMetaInfo().getCreatedAt());
				createdYear = cal.get(Calendar.YEAR);
			}

			yearsInBusiness = createdYear - businessStartYear;
		}
		return yearsInBusiness;

	}

	public Address getLegalAddressInfo() {
		if (this.getPrimarySite() != null
				|| this.getPrimarySite().getSiteDetails() != null) {
			List<Address> addresses = this.getPrimarySite().getSiteDetails()
					.getAddresses();
			if ((addresses != null) && (addresses.isEmpty())) {
				for (Address addObj : addresses) {
					if (addObj.getUsage() != null) {
						for (AttributeChoice useType : addObj.getUsage()) {
							if (StringUtils.equals(
									Constants.ADDRESS_USE_TYPE_LEGAL,
									useType.getKey())) {
								return addObj;
							}
						}
					}
				}
			}
		}

		return null;
	}

	public boolean isNumeric(String str) {

		return str.matches("-?\\d+(\\.\\d+)?");
	}

	public boolean checkIndustryDBT() {
		boolean match = true;
		if (getIndustryDBT() != null && isNumeric(getIndustryDBT()) && Integer.parseInt(getIndustryDBT()) < 0) {
				match = false;
		}
		return match;
	}

	public boolean isDebtor() {
		return isDebtor;
	}

	public void setDebtor(boolean isDebtor) {
		this.isDebtor = isDebtor;
	}

	public boolean isAccount() {
		return isAccount;
	}

	public void setAccount(boolean isAccount) {
		this.isAccount = isAccount;
	}

	/**
	 * Method validating request Customers Applying DedupBizOp Checks
	 * 
	 * @return
	 */
	public boolean validatePrimaryEmailDup(String partyType) {

		EntityService entityService = new EntityService<BaseDataObject>();
		boolean flag = true;

		if (ContextHolder.getContext() != null
				&& ContextHolder.getContext().getNamedContext().get("root") != null
				&& !(ContextHolder.getContext().getNamedContext().get("root") instanceof Customer)) {
			return flag;
		}

		// Get all customers from database of type Non Individual.
		List<Customer> existingCustomers = null;
		if (getId() != null) {
			existingCustomers = (List<Customer>) entityService
					.findByNamedQueryAndNamedParam(
							"com.thirdpillar.codify.loanpath.model.Customer.byCustTypeAndId",
							new String[] { "customerType", "email", "id" },
							new Object[] {
									partyType,
									getPrimarySite().getSiteDetails()
											.getEmail(), getId() });
		} else {
			existingCustomers = (List<Customer>) entityService
					.findByNamedQueryAndNamedParam(
							"com.thirdpillar.codify.loanpath.model.Customer.byCustType",
							new String[] { "customerType", "email" },
							new Object[] {
									partyType,
									getPrimarySite().getSiteDetails()
											.getEmail() });
		}

		/**
		 * If constraint fired from request context skip next lines of code.
		 */
		if (!existingCustomers.isEmpty()) {
			flag = false;
		}
		return flag;
	}

	/**
	 * Method validating duns number duplication
	 */
	public boolean validateDUNSDedupe(String partyType){


		EntityService entityService = new EntityService<BaseDataObject>();
		boolean flag = true;

		// Get all customers from database of type Non Individual.
		List<Customer> existingCustomers = null;
		if ("CUSTOMER_TYPE_NON_INDIVIDUAL".equals(partyType) && StringUtils.isNotEmpty(getDuns())) {
			if (getId() != null) {
				existingCustomers = (List<Customer>) entityService
						.findByNamedQueryAndNamedParam(
								"com.thirdpillar.codify.loanpath.model.Customer.byDunsAndId",
								new String[] { "duns", "id" },
								new Object[] { getDuns(), getId() });
			} else {
				existingCustomers = (List<Customer>) entityService
						.findByNamedQueryAndNamedParam(
								"com.thirdpillar.codify.loanpath.model.Customer.byDunsLookup",
								new String[] { "dunsLookup" },
								new Object[] { getDuns() });
			}
		}

		/**
		 * If constraint fired from request context skip next lines of code.
		 */
		if (!existingCustomers.isEmpty()) {
			duplicateCustomer = existingCustomers.get(0).getCustomerName();
			flag = false;
		}
		return flag;
	
	}
	
	/**
	 * Method validating Customers TaxPayer ID Applying DedupBizOp Checks
	 * 
	 * @return
	 */
	public boolean validateSSNDup(String partyType) {

		EntityService entityService = new EntityService<BaseDataObject>();
		boolean flag = true;

		// Get all customers from database of type Non Individual.
		List<Customer> existingCustomers = null;

		if ("CUSTOMER_TYPE_INDIVIDUAL".equals(partyType)) {
			if (getId() != null) {
				existingCustomers = (List<Customer>) entityService
						.findByNamedQueryAndNamedParam(
								"com.thirdpillar.codify.loanpath.model.Customer.bySsnAndId",
								new String[] { "customerSSN", "id" },
								new Object[] { getSsn(), getId() });
			} else {
				existingCustomers = (List<Customer>) entityService
						.findByNamedQueryAndNamedParam(
								"com.thirdpillar.codify.loanpath.model.Customer.bySsn",
								new String[] { "customerSSN" },
								new Object[] { getSsn() });
			}
		} else if ("CUSTOMER_TYPE_NON_INDIVIDUAL".equals(partyType) && StringUtils.isNotEmpty(getFederalTaxId())) {
			if (getId() != null) {
				existingCustomers = (List<Customer>) entityService
						.findByNamedQueryAndNamedParam(
								"com.thirdpillar.codify.loanpath.model.Customer.byFeinAndId",
								new String[] { "customerFEIN", "id" },
								new Object[] { getFederalTaxId(), getId() });
			} else {
				existingCustomers = (List<Customer>) entityService
						.findByNamedQueryAndNamedParam(
								"com.thirdpillar.codify.loanpath.model.Customer.byFein",
								new String[] { "customerFEIN" },
								new Object[] { getFederalTaxId() });
			}
		}else if ("CUSTOMER_TYPE_NON_INDIVIDUAL".equals(partyType) && StringUtils.isNotEmpty(getDuns())) {
			if (getId() != null) {
				existingCustomers = (List<Customer>) entityService
						.findByNamedQueryAndNamedParam(
								"com.thirdpillar.codify.loanpath.model.Customer.byDunsAndId",
								new String[] { "duns", "id" },
								new Object[] { getFederalTaxId(), getId() });
			} else {
				existingCustomers = (List<Customer>) entityService
						.findByNamedQueryAndNamedParam(
								"com.thirdpillar.codify.loanpath.model.Customer.byDuns",
								new String[] { "duns" },
								new Object[] { getDuns() });
			}
		}

		/**
		 * If constraint fired from request context skip next lines of code.
		 */
		if (!existingCustomers.isEmpty()) {
			flag = false;
		}
		return flag;
	}

	public String getDuplicateCustomer() {
		return duplicateCustomer;
	}

	public void setDuplicateCustomer(String duplicateCustomer) {
		this.duplicateCustomer = duplicateCustomer;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public String getNaicsDescDer(){
		if(getNaicsCode() != null){
			AttributeChoice attributeChoice = getNaicsCode();
			return attributeChoice.getValue();
		}
		return null;
	}
	
    public boolean borrowerRatingsCheck(){
    	boolean flag = true;
		
		if(StringUtils.equals("CUSTOMER_TYPE_NON_INDIVIDUAL",this.getCustomerType().getKey())){
			List<BorrowerRating> borrowerRatings = this.getBorrowerRatings();
			if(! borrowerRatings.isEmpty()){
				for(BorrowerRating borrowerRating : borrowerRatings){
					if(borrowerRating.getFraudScoreDttm() == null){
						this.setFieldName("Fraud Score Date");
						flag = false;
						return flag;
					}else if(borrowerRating.getFraudScore() == null){
						this.setFieldName("Fraud Score");
						flag = false;
						return flag;
					}else if(borrowerRating.getAdjRiskScore() == null){
						this.setFieldName("Adj Fraud Score");
						flag = false;
						return flag;
					}else if(borrowerRating.getRiskScoreDttm() == null){
						this.setFieldName("Risk Score Date");
						flag = false;
						return flag;
					}else if(borrowerRating.getRiskScore() == null || borrowerRating.getRiskScore().getKey() == null){
						this.setFieldName("Risk Score");
						flag = false;
						return flag;
					}else if(borrowerRating.getAdjRiskScore() == null || borrowerRating.getAdjRiskScore().getKey() == null){
						this.setFieldName("Adj Risk Score");
						flag = false;
						return flag;
					}
				}	
			}else{
				this.setFieldName("Borrower Risk Ratings");
				flag = false;
				return flag;
			}
		}
    	return flag;
    }
    
    public boolean riskScoreDttmCheck(){
    	boolean flag = true;
		
		if(StringUtils.equals("CUSTOMER_TYPE_NON_INDIVIDUAL",this.getCustomerType().getKey())){
			if(this.getRiskScoreDttm() == null){
				flag = false;
				return flag;
			}
		}
    	return flag;
    }
    
    public boolean riskScoreCheck(){
    	boolean flag = true;
		
		if(StringUtils.equals("CUSTOMER_TYPE_NON_INDIVIDUAL",this.getCustomerType().getKey())){
			if(this.getRiskScore() == null){
				flag = false;
				return flag;
			}
		}
    	return flag;
    }
    
    public boolean adjRiskScoreCheck(){
    	boolean flag = true;
		
		if(StringUtils.equals("CUSTOMER_TYPE_NON_INDIVIDUAL",this.getCustomerType().getKey())){
			if(this.getAdjRiskScore() == null){
				flag = false;
				return flag;
			}
		}
    	return flag;
    }
    
    public BureauData getDefaultBureauData(){
    	return (this.getBureauData() != null && !this.getBureauData().isEmpty()) ? this.getBureauData().get(0) : null;
    }
}
