package com.thirdpillar.codify.loanpath.service.request;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.codify.loanpath.model.AttributeChoice;
import com.thirdpillar.codify.loanpath.model.BureauReport;
import com.thirdpillar.codify.loanpath.model.CreditScoreFactor;
import com.thirdpillar.codify.loanpath.model.Customer;
import com.thirdpillar.codify.loanpath.model.DebtorCustomer;
import com.thirdpillar.codify.loanpath.model.FacScoring;
import com.thirdpillar.codify.loanpath.model.Facility;
import com.thirdpillar.codify.loanpath.model.FacilityCustomerRole;
import com.thirdpillar.codify.loanpath.model.FacilityRecomm;
import com.thirdpillar.codify.loanpath.model.PartyScoring;
import com.thirdpillar.codify.loanpath.model.Request;
import com.thirdpillar.codify.loanpath.model.ScorecardDetails;
import com.thirdpillar.codify.loanpath.model.Scoring;
import com.thirdpillar.codify.loanpath.model.ScoringRecommRule;
import com.thirdpillar.codify.loanpath.model.ScrFactorSeed;
import com.thirdpillar.codify.loanpath.model.ScrRskMod;
import com.thirdpillar.codify.loanpath.util.DetailedBureuReportHelper;
import com.thirdpillar.codify.loanpath.util.ScoreFactorMapper;
import com.thirdpillar.codify.loanpath.util.Utility;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;

@Component
@Configurable
public class CalculateScroing {

	@Autowired
	private EntityService entityService;

	private static final Log LOG = LogFactory.getLog(CalculateScroing.class);
	Map<Long, Object> magnumReports = new HashMap<Long, Object>();

	public void calcScoring(BaseDataObject entity) {
		if (LOG.isTraceEnabled()) {
			LOG.trace(">> Entering calcScoring()");
		}
		Request request = (Request) entity;
		if (LOG.isDebugEnabled()) {
			LOG.debug("-- Inside calcScoring() > Got Request: "
					+ request.getId().toString());
		}
		Scoring scoring;
		if (entityService != null) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("-- Inside calcScoring() > Creating an Scoring Object for Request: "
						+ request.getId().toString());
			}
			scoring = (Scoring) entityService.createNew(Scoring.class);
			AttributeChoice racExceptionAuthSCO = (AttributeChoice) LookupService
					.getResult("AttributeChoice.byKey", "key",
							"RAC_EXCEPTION_AUTHORITY_SCO");

			scoring.setEvalDttm(new Date());
			scoring.setFinalDecisionDttm(new Date());
			scoring.setRequest(request);
			scoring.setExceptionAuthority(racExceptionAuthSCO);

			/*
			 * 
			 * Calculate Customer level scorings
			 */
			List<Customer> parties = fetchAllCustomers(entity);
			if (LOG.isDebugEnabled()) {
				LOG.debug("-- Inside calcScoring() > Scored Parties List Size is: ");
			}
			if (parties != null && !parties.isEmpty()) {
				for (Customer cst : parties) {
					PartyScoring partyScoring = (PartyScoring) entityService
							.createNew(PartyScoring.class);
					partyScoring.setParty(cst);
					partyScoring.setScoring(scoring);
					if (cst.isNonIndividual()) {
						AttributeChoice attributeChoice = (AttributeChoice) LookupService
								.getResult(
										"AttributeChoice.byKey",
										"key",
										Constants.CUSTOMER_TYPE_NON_INDIVIDUAL_KEY);
						partyScoring.setCustomerType(attributeChoice);
					} else if (cst.isIndividual()) {
						AttributeChoice attributeChoice = (AttributeChoice) LookupService
								.getResult("AttributeChoice.byKey", "key",
										Constants.CUSTOMER_TYPE_INDIVIDUAL_KEY);
						partyScoring.setCustomerType(attributeChoice);
					} else {
					}
					partyScoring.initScoring();
					scoring.addToPartyScorings(partyScoring);

				}
			}

			/*
			 * 
			 * Calculate facility scorings
			 */
			// Calculating and Assigning ranks for each facility that are
			// applicable for Ranking...
			List<Facility> allFacsForRanking = request
					.getAllFacilitiesApplicableForRanking();

			// Collections.sort(allFacsForRanking,
			for (int i = 0; i < allFacsForRanking.size(); i++) {
				int rank = i + 1;
				allFacsForRanking.get(i).setfacilityRank(rank);
			}
			// Fac Scoring...
			List<Facility> allFacilities = request.getAllFacilities();
			if (LOG.isDebugEnabled()) {
				LOG.debug("-- Inside calcScoring() > Facility List Size is: "
						+ allFacilities.size());
			}
			if (allFacilities != null && !allFacilities.isEmpty()) {
				for (Facility facility : allFacilities) {
					FacScoring facScoring = (FacScoring) entityService
							.createNew(FacScoring.class);
					facScoring.setFacility(facility);
					facScoring.setScoring(scoring);
					facScoring.initScoring();
					scoring.addToFacScorings(facScoring);
				}
			}

			/*
			 * 
			 * Calculate application scorings
			 */
			scoring.initScoring();
			request.addToAppScorings(scoring);
		} else {
			LOG.error("entity service is null.. Unable to initalize the Scoring Object..");
			LOG.error("=====Scoring Caluclation Failed====");
		}
		if (LOG.isTraceEnabled()) {
			LOG.trace("<< Existing calcScoring()");
		}
	}

	public void setFacilityRecommendation(BaseDataObject entity) {

		Request request = (Request) entity;
		Scoring scoring = request.getLatestAppScoring();
		if (scoring != null && scoring.getFacScorings() != null) {
			for (FacScoring facScoring : scoring.getFacScorings()) {

				List<ScoringRecommRule> lstRules = new ArrayList<ScoringRecommRule>();
				lstRules.addAll(facScoring.getScoringRecommRules());
				lstRules.addAll(scoring.getScoringRecommRules());
				for (PartyScoring partyScoring : scoring.getPartyScorings()) {
					lstRules.addAll(partyScoring.getScoringRecommRules());
				}

				ScoringRecommRule failedRule = scoring
						.getFailedRuleWithMaxRecommLevel(lstRules,
								Constants.RECOMMENDATION_TYPE_FACILITY_KEY);
				AttributeChoice recommId, recommCategory;
				if (failedRule == null) {
					recommId = (AttributeChoice) LookupService.getResult(
							"AttributeChoice.byKey", "key",
							"RECOMMENDATION_DECISION_APPROVE");
					recommCategory = (AttributeChoice) LookupService.getResult(
							Constants.ATTRIBUTECHOICEBYKEY,
							Constants.ATTRIBUTECHOICEBYKEYVAL,
							"RECOMMENDATION_CATEGORY_APPROVE_KEY");

				} else {
					recommId = failedRule.getRecommendationDecision();
					recommCategory = failedRule.getRecommCategory();

				}
				FacilityRecomm facilityRecomm = (FacilityRecomm) entityService
						.createNew(FacilityRecomm.class);
				facilityRecomm.setFacility(facScoring.getFacility());

				facilityRecomm.setRecommId(recommId);
				facilityRecomm.setRecommCategory(recommCategory);
				facScoring.setFacilityRecomm(facilityRecomm);
			}
		}
	}

	public void setPartyScoreCard(BaseDataObject entity) {
		if (LOG.isTraceEnabled()) {
			LOG.trace(">> Entering setPartyScoreCard()");
		}
		Request request = (Request) entity;
		if (LOG.isDebugEnabled()) {
			LOG.debug("-- Inside setPartyScoreCard() > Got Request: "
					+ request.getId().toString());
		}
		Scoring scoring = request.getLatestAppScoring();
		if (scoring != null && scoring.getPartyScorings() != null) {
			for (PartyScoring partySoring : scoring.getPartyScorings()) {

				ScorecardDetails scorecardDetails = (ScorecardDetails) entityService
						.createNew(ScorecardDetails.class);
				if (partySoring.getParty().isIndividual()) {
					scorecardDetails
							.setScorecardModel("Banking Consumer Scoring 1.0");
					if (LOG.isDebugEnabled()) {
						LOG.debug("-- Inside setPartyScoreCard() > created a ScoreCard For Individual Customer");
					}
				} else {
					scorecardDetails
							.setScorecardModel("Banking Commercial Scoring 1.0");
					if (LOG.isDebugEnabled()) {
						LOG.debug("-- Inside setPartyScoreCard() > created a ScoreCard For Non Individual Customer");
					}
				}
				scorecardDetails.setCustomer(partySoring.getParty());

				// Setting default empty string for these fields.
				scorecardDetails.setScorecardType("");
				scorecardDetails.setRawScore("");
				scorecardDetails.setFicoScore("");
				scorecardDetails.setBaseLimitExp("");
				scorecardDetails.setRecomm("");
				partySoring.setScorecardDetail(scorecardDetails);

			}
		}
		if (LOG.isTraceEnabled()) {
			LOG.trace("<< Existing setPartyScoreCard()");
		}
	}

	public void updatePartyScoreCard(BaseDataObject entity) {

		Request request = (Request) entity;
		Scoring scoring = request.getLatestAppScoring();
		if (scoring != null && scoring.getPartyScorings() != null) {
			for (PartyScoring partySoring : scoring.getPartyScorings()) {
				List<ScoringRecommRule> lstRules = new ArrayList<ScoringRecommRule>();
				lstRules.addAll(partySoring.getScoringRecommRules());

				ScoringRecommRule failedRule = scoring
						.getFailedRuleWithMaxRecommLevel(lstRules,
								Constants.RECOMMENDATION_TYPE_PARTY_KEY);
				AttributeChoice recommId = null, recommCategory = null, exceptionAuthority = null;
				if (failedRule == null) {
					recommId = (AttributeChoice) LookupService.getResult(
							"AttributeChoice.byKey", "key",
							"RECOMMENDATION_DECISION_APPROVE");
					recommCategory = (AttributeChoice) LookupService.getResult(
							Constants.ATTRIBUTECHOICEBYKEY,
							Constants.ATTRIBUTECHOICEBYKEYVAL,
							"RECOMMENDATION_CATEGORY_APPROVE_KEY");

				} else {
					recommId = failedRule.getRecommendationDecision();
					recommCategory = failedRule.getRecommCategory();
					exceptionAuthority = failedRule.getExceptionAuthority();
				}

				ScorecardDetails scorecardDetails = partySoring
						.getScorecardDetail();
				scorecardDetails.setRecommId(recommId);
				scorecardDetails.setRecommCategory(recommCategory);
				scorecardDetails.setExceptionAuthority(exceptionAuthority);
			}
		}
	}

	public void setFinalExceptionAuthority(BaseDataObject entity) {

		Request request = (Request) entity;
		Scoring scoring = request.getLatestAppScoring();
		if (scoring != null) {
			scoring.setFinalExceptionAuthority();
		}
	}

	public List<Customer> fetchAllCustomers(BaseDataObject entity) {
		List<Customer> customers = new ArrayList<Customer>();
		for (Facility facility : getAllActiveFacilities(entity)) {
			for (FacilityCustomerRole customerRole : facility
					.getFacilityCustomerRoles()) {
				if (customerRole.getCustomer() != null
						&& !customers.contains(customerRole.getCustomer())) {
					customers.add(customerRole.getCustomer());
				}
			}
		}
		return customers;
	}

	public List<Facility> getAllActiveFacilities(BaseDataObject entity) {
		Request request = (Request) entity;
		List<Facility> list = new ArrayList<Facility>();
		for (Facility fac : request.getAllFacilities()) {
			if (!fac.inWfStatus("FACILITY_STATUS_WITHDRAWN")) {
				list.add(fac);
			}
		}
		return list;
	}
}