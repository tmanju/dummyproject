package com.thirdpillar.codify.loanpath.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.mvel2.MVEL;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;
import org.apache.commons.lang.StringUtils;

public class PartyScoring extends BaseDataObject {

	/**
	 * @return String containing "lastName, FirstName" if party is individual
	 *         and legalName otherwise.
	 */
	private static final Log LOG = LogFactory.getLog(PartyScoring.class);

	public String getCustomerName() {

		if ((getCustomerType() != null)
				&& StringUtils.equals("CUSTOMER_TYPE_INDIVIDUAL",
						getCustomerType().getKey())) {
			return getCustomerLastName() + ", " + getCustomerFirstName();
		} else {
			return getCustomerLegalName();
		}
	}

	/**
	 * Method will give the BureauReport object from its ScorecardDetails.
	 * 
	 * @param String
	 *            name
	 * 
	 * @return {@link BureauReport}
	 */
	public BureauReport getBureauReportByName(String name) {
		BureauReport bureau = null;
		if (this.getScorecardDetail() != null) {
			for (BureauReport bur : this.getScorecardDetail()
					.getScrBureauRprts()) {
				if (StringUtils.equalsIgnoreCase(name, bur.getBureauName())) {
					bureau = bur;
					break;
				}
			}
		}
		return bureau;
	}

	public void initScoring() {
		if (LOG.isTraceEnabled()) {
			LOG.trace(">> Entering initScoring()");
		}
		List<BaseDataObject> scrAttributeChoices = new ArrayList<BaseDataObject>();
		List<BaseDataObject> scrRecomRules = new ArrayList<BaseDataObject>();
		EntityService es = new EntityService();

		if (this.getParty().isIndividual()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("-- Inside initScoring() > Customer is Individual");
			}
			scrAttributeChoices = LookupService
					.getResults(
							Constants.SCORE_ATTRIBUTE_CHOICE_BY_APPLICABILITY_NAMED_QUERY,
							Constants.APPLICABILITY_TYPE_KEY,
							Constants.APPLICABILITY_TYPE_INDIVIUAL_KEY);

			scrRecomRules = LookupService.getResults(
					Constants.SCORE_RECOM_RULES_BY_APPLICABILITY_NAMED_QUERY,
					Constants.APPLICABILITY_TYPE_KEY,
					Constants.APPLICABILITY_TYPE_INDIVIUAL_KEY);

		}
		if (this.getParty().isNonIndividual()) {
			if (LOG.isDebugEnabled()) {
				LOG.debug("-- Inside initScoring() > Customer is Non-Individual");
			}
			scrAttributeChoices = LookupService
					.getResults(
							Constants.SCORE_ATTRIBUTE_CHOICE_BY_APPLICABILITY_NAMED_QUERY,
							Constants.APPLICABILITY_TYPE_KEY,
							Constants.APPLICABILITY_TYPE_NON_INDIVIUAL_KEY);

			scrRecomRules = LookupService.getResults(
					Constants.SCORE_RECOM_RULES_BY_APPLICABILITY_NAMED_QUERY,
					Constants.APPLICABILITY_TYPE_KEY,
					Constants.APPLICABILITY_TYPE_NON_INDIVIUAL_KEY);
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("-- Inside initScoring() > scrAttributeChoices list size is: "
					+ scrAttributeChoices.size());
		}
		for (BaseDataObject baseDataObject : scrAttributeChoices) {
			ScrAttributeSeed scrAttributeChoiceSeed = (ScrAttributeSeed) baseDataObject;
			ScoringAttribute scoringAttribute = (ScoringAttribute) es
					.createNew(ScoringAttribute.class);
			scoringAttribute.setAttributeName(scrAttributeChoiceSeed.getName());
			scoringAttribute.setAttributeKey(scrAttributeChoiceSeed.getKey());
			scoringAttribute.setAttributeDescription(scrAttributeChoiceSeed
					.getDescription());

			scoringAttribute.setPartyScoring(this);
			scoringAttribute.setCustomer(this.getParty());
			this.addToScoringAttributes(scoringAttribute);
			if (LOG.isDebugEnabled()) {
				LOG.debug("-- Inside initScoring() > Created a Scoring Attribute Named: "
						+ scrAttributeChoiceSeed.getName());
			}
		}

		if (LOG.isDebugEnabled()) {
			LOG.debug("-- Inside initScoring() > scrRecomRules list size is: "
					+ scrRecomRules.size());
		}
		for (BaseDataObject baseDataObject : scrRecomRules) {

			ScrRcmRuleSeed scrRecomRule = (ScrRcmRuleSeed) baseDataObject;
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
			scoringRecommRule.setPartyScoring(this);
			scoringRecommRule.setCustomer(this.getParty());
			this.addToScoringRecommRules(scoringRecommRule);
			if (LOG.isDebugEnabled()) {
				LOG.debug("-- Inside initScoring() > Created a Scoring RecommRule Named: "
						+ scrRecomRule.getName());
			}
		}
		if (LOG.isTraceEnabled()) {
			LOG.trace("<< Existing initScoring()");
		}
	}

	public ScoringAttribute findAttributeByKey(String attributeKey) {
		LOG.debug("Inside PartyScoring: findAttributeByKey()");
		for (ScoringAttribute attr : this.getScoringAttributes()) {
			if (attr.getAttributeKey().equals(attributeKey)) {
				LOG.debug("==== Attribute found ====");
				return attr;
			}
		}
		return null;
	}

	public ScoringRecommRule findRecommRuleByKey(String ruleKey) {
		LOG.debug("Inside Scoring: findRecommRuleByKey()");
		for (ScoringRecommRule rule : this.getScoringRecommRules()) {
			if (rule.getRuleKey().equals(ruleKey)) {
				LOG.debug("=== RecommRule found ===");
				return rule;
			}
		}
		return null;

	}

	public boolean otherRulesFailureWithReviewCriteira() {
		boolean flag = false;
		for (ScoringRecommRule rule : this.getScoringRecommRules()) {
			if (StringUtils.equals(Constants.SCORE_RECOM_RULES_RESULT_FAIL,
					rule.getResult().getKey())
					&& !"NON_INDIVIDUAL_RULE_HIGH_RISK_INDUSTRY_NEED_REVIEW".equals(rule.getRuleKey())) {
				LOG.debug("=== Failed RecommRule found that is not realted to High Risk ===");
				if (StringUtils
						.equals(rule.getRecommendationDecision().getKey(),
								Constants.RECOMMENDATION_DECISION_REVIEW_KEY)) {
					flag = true;
					break;
				}
			}
		}
		return flag;
	}
}
