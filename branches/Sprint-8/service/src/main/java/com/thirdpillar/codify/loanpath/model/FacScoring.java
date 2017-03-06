/*
 * Copyright (c) 2005-2015 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.lang.NumberUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;
/**
 * DOCUMENT ME!
 *
 * @author   Sajan Monga
 * @version  1.0
 * @since    1.0
 */
public class FacScoring extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /**  */
    private static final long serialVersionUID = -1311881684201033268L;
    private static final Log LOG = LogFactory.getLog(FacScoring.class);
    //~ Methods --------------------------------------------------------------------------------------------------------
    
	@Transient
    private BigDecimal incrementalAmt;
    
    
	public BigDecimal getIncrementalAmt() {
		return incrementalAmt;
	}

	public void setIncrementalAmt(BigDecimal incrementalAmt) {
		this.incrementalAmt = incrementalAmt;
	}

	public Facility getFacilityDer() {
        return getFacility();
    }

    
    public String getIsEligibleForSBAExpress(){
    	LOG.debug("Inside FacScoring : getIsEligibleForSBAExpress() from Mvel...");
    	
    	if("SBA".equalsIgnoreCase(getProgramValue()) && SBAExpressCriteriaMatches()){
    		return "Yes";
    	}else{
    		return "No";
    	}
    	
    }
    private boolean SBAExpressCriteriaMatches(){
		if(appLevelRuleResult() && individualLevelRuleResult()
				&& nonindividualLevelRuleResult() && facilitylevelRuleResult()
				&& exisitingDirectExposureRuleResult() && facilityTypeRuleResult() && fullySecureRuleresult()){
			return true;
		}
			return false;
	}

	private boolean fullySecureRuleresult(){
		LOG.debug("Inside FacScoring : fullySecureRuleresult()");
		boolean flag = false;
		if(getScoring() !=null && getScoring().getCustomerFullySecureRuleResult()){
			flag = true;
		}
		return flag;
	}

	private boolean facilityTypeRuleResult(){
		LOG.debug("Inside FacScoring : facilityTypeRuleResult()");
		boolean flag = false;
		if(getFacility() != null && getFacility().getFacilityType() != null){
			
			if(StringUtils.equals(getFacility().getFacilityType().getKey(), Constants.BUSINESS_CHECKING_PLUS_KEY)
					|| StringUtils.equals(getFacility().getFacilityType().getKey(), Constants.BUSINESS_CREDIT_ACCOUNT_KEY)){				
				flag = true;
			}
		}
		return flag;
	}

	private boolean exisitingDirectExposureRuleResult(){
		LOG.debug("Inside FacScoring : exisitingDirectExposureRuleResult()");
		boolean flag = false;
		if(getDirectExistingExposureAmount(getFacility()).compareTo(BigDecimal.ZERO) == 0){
			flag = true;
		}
		return flag;
	}

	private boolean facilitylevelRuleResult(){
		LOG.debug("Inside FacScoring : facilitylevelRuleResult()");
		boolean flag = false;
		
		if(getBcaOrLineIncreasedRuleResult() && getCreditScoreRuleResult()){
			flag = true;
		}
		return flag;
	}

	private boolean nonindividualLevelRuleResult(){
		LOG.debug("Inside FacScoring : nonindividualLevelRuleResult()");
		boolean flag = true;
		
		if(getScoring() !=null && getScoring().getPartyScorings() != null && getScoring().getPartyScorings().size() > 0){
			for(PartyScoring partyScoring : getScoring().getPartyScorings()){
				if(partyScoring.getCustomerType() != null &&  StringUtils.equals(partyScoring.getCustomerType().getKey(),Constants.CUSTOMER_TYPE_NON_INDIVIDUAL_KEY)){
					//none of them should has a value of fail..
					ScoringRecommRule recommRuleBureauBankcruptcy = partyScoring.findRecommRuleByKey("NON_INDIVIDUAL_RULE_CREDIT_BUREAU_BANKRUPTCY");
					ScoringRecommRule recommRulePriorLoss = partyScoring.findRecommRuleByKey("NON_INDIVIDUAL_RULE_CITI_PRIOR_LOSS");
					ScoringRecommRule recommRulePastDue = partyScoring.findRecommRuleByKey("NON_INDIVIDUAL_RULE_PAST_DUE_CITI");
					ScoringRecommRule recommRuleOsLegalItems = partyScoring.findRecommRuleByKey("NON_INDIVIDUAL_RULE_SUBSTANTIAL_OS_LEGAL_ITEMS");
					ScoringRecommRule recommRuleIndustryProhibited = partyScoring.findRecommRuleByKey("NON_INDIVIDUAL_RULE_INDUSTRY_POLICY_EXCEPTION");
					ScoringRecommRule recommRuleYears = partyScoring.findRecommRuleByKey("NON_INDIVIDUAL_RULE_BUSINESS_YEARS");
					ScoringRecommRule recommRuleYearsBcp = partyScoring.findRecommRuleByKey("NON_INDIVIDUAL_RULE_BUSINESS_YEARS_BCP");
					
					if(recommRuleIndustryProhibited !=null && recommRuleIndustryProhibited.getResult() !=null && recommRuleIndustryProhibited.getResult().equals(Constants.SCORING_RECOMM_RESULT_FAIL_KEY) ){
						flag = false;
						break;
					}
					if(recommRuleYears !=null && recommRuleYears.getResult() !=null && recommRuleYears.getResult().equals(Constants.SCORING_RECOMM_RESULT_FAIL_KEY) ){
						flag = false;
						break;
					}
					if(recommRuleYearsBcp !=null && recommRuleYearsBcp.getResult() !=null && recommRuleYearsBcp.getResult().equals(Constants.SCORING_RECOMM_RESULT_FAIL_KEY) ){
						flag = false;
						break;
					}
					if(recommRuleBureauBankcruptcy !=null && recommRuleBureauBankcruptcy.getResult() !=null && recommRuleBureauBankcruptcy.getResult().equals(Constants.SCORING_RECOMM_RESULT_FAIL_KEY) ){
						flag = false;
						break;
					}
					if(recommRulePriorLoss !=null && recommRulePriorLoss.getResult() !=null && recommRulePriorLoss.getResult().equals(Constants.SCORING_RECOMM_RESULT_FAIL_KEY) ){
						flag = false;
						break;
					}
					if(recommRulePastDue !=null && recommRulePastDue.getResult() !=null && recommRulePastDue.getResult().equals(Constants.SCORING_RECOMM_RESULT_FAIL_KEY) ){
						flag = false;
						break;
					}
					if(recommRuleOsLegalItems !=null && recommRuleOsLegalItems.getResult() !=null && recommRuleOsLegalItems.getResult().equals(Constants.SCORING_RECOMM_RESULT_FAIL_KEY) ){
						flag = false;
						break;
					}
				}		
			}	
		}
		return flag;
	}

	private boolean individualLevelRuleResult(){
		LOG.debug("Inside FacScoring : individualLevelRuleResult()");
		boolean flag = true;
		
		if(getScoring() !=null && getScoring().getPartyScorings() != null && getScoring().getPartyScorings().size() > 0){
			for(PartyScoring partyScoring : getScoring().getPartyScorings()){
				if(partyScoring.getCustomerType() != null &&  StringUtils.equals(partyScoring.getCustomerType().getKey(),Constants.CUSTOMER_TYPE_INDIVIDUAL_KEY)){
					//none of the below has a value of FAIL..
					ScoringRecommRule recommRulebureau = partyScoring.findRecommRuleByKey("INDIVIDUAL_RULE_CREDIT_BUREAU_BANKRUPTCY");
					ScoringRecommRule recommRuleSubstantialOutstandings = partyScoring.findRecommRuleByKey("INDIVIDUAL_RULE_SUBSTANTIAL_OUTSTANDINGS_ITEMS");
					ScoringRecommRule recommRulePriorLoss = partyScoring.findRecommRuleByKey("INDIVIDUAL_RULE_PRIOR_CITI_LOSS");
					if(recommRulebureau !=null && recommRulebureau.getResult() !=null && recommRulebureau.getResult().equals(Constants.SCORING_RECOMM_RESULT_FAIL_KEY) ){
						flag = false;
						break;
					}
					if(recommRuleSubstantialOutstandings !=null && recommRuleSubstantialOutstandings.getResult() !=null && recommRuleSubstantialOutstandings.getResult().equals(Constants.SCORING_RECOMM_RESULT_FAIL_KEY) ){
						flag = false;
						break;
					}
					if(recommRulePriorLoss !=null && recommRulePriorLoss.getResult() !=null && recommRulePriorLoss.getResult().equals(Constants.SCORING_RECOMM_RESULT_FAIL_KEY) ){
						flag = false;
						break;
					}	
				}	
			}	
		}
		return flag;
	}

	private boolean appLevelRuleResult(){
		LOG.debug("Inside FacScoring : appLevelRuleResult()");
		boolean flag = false;
		if(getScoring() != null && getScoring().getMissingFicoOrMissingCustomRuleResult()){
			flag = true;
		}
		return flag;
	}

	public String getFailedRuleHighestRecomLevel(){
    	LOG.debug("Inside FacScoring : getFailedRuleHighestRecomLevel() from Mvel...");
    	String val="";
    	List<ScoringRecommRule> lstRules = new ArrayList<ScoringRecommRule>();
    	lstRules.addAll(getScoringRecommRules());
    	if(getScoring()!=null){
    		ScoringRecommRule recommRule = getScoring().getFailedRuleWithMaxRecommLevel(lstRules, Constants.RECOMMENDATION_TYPE_FACILITY_KEY);
    		if(recommRule != null){
    			val = recommRule.getRuleName();
    		}
    	}
    	return val;
    }
    public String getScoringResultDecline(){
    	LOG.debug("Inside FacScoring : getScoringResultDecline() from Mvel...");
    	return null;
    }
    public String getFacilityType(){
    	LOG.debug("Inside FacScoring : getFacilityType() from Mvel...");
    	return getFacilityTypeAttributeValue();
    }
    private String getFacilityTypeAttributeValue(){
    	LOG.debug("Inside FacScoring : getFacilityTypeAttributeValue()");
    	String value="";
		if(getFacility() != null && getFacility().getFacilityType() != null){
			if(StringUtils.equals(getFacility().getFacilityType().getKey(), Constants.BUSINESS_BANKING_BIL_KEY)
				|| StringUtils.equals(getFacility().getFacilityType().getKey(), Constants.BUSINESS_INSTALLMENT_LOAN_KEY)
				|| StringUtils.equals(getFacility().getFacilityType().getKey(), Constants.BUSINESS_INSTALLMENT_LOAN_6MONTH_KEY)){
				
				value = "BIL";					
			}else if (StringUtils.equals(getFacility().getFacilityType().getKey(), Constants.BUSINESS_CHECKING_PLUS_KEY)){
				value = "BCP";
			}else if (StringUtils.equals(getFacility().getFacilityType().getKey(), Constants.BUSINESS_CREDIT_ACCOUNT_KEY)) {
				value = "BCA";
			}else{
				value = "Others";
			}		
		}
		return value;
	}

	public String getProgramValue(){
    	LOG.debug("Inside FacScoring : getProgramValue() from Mvel...");
    	String value="";
    	if(facilityTypePass() && nonIndividualCriteriaPass()){
    		value = "SBA";
    	}else{
    		value = String.valueOf(BigDecimal.ZERO);
    	}
    	return value;
    }
	
    private boolean facilityTypePass(){
    	LOG.debug("Inside FacScoring : facilityTypePass()");
    	boolean flag = false;
		if(getFacility() != null && getFacility().getFacilityType() != null){
			if(StringUtils.equals(getFacility().getFacilityType().getKey(), Constants.BUSINESS_BANKING_BIL_KEY)
				|| StringUtils.equals(getFacility().getFacilityType().getKey(), Constants.BUSINESS_CREDIT_ACCOUNT_KEY)){
				
				flag = true;					
			}		
		}
		return flag;
	}

	private boolean nonIndividualCriteriaPass(){
    	boolean flag = false;
    	
    	if(getScoring() !=null && getScoring().getPartyScorings() != null && getScoring().getPartyScorings().size() > 0){
			for(PartyScoring partyScoring : getScoring().getPartyScorings()){
				if(partyScoring.getCustomerType() != null &&  StringUtils.equals(partyScoring.getCustomerType().getKey(),Constants.CUSTOMER_TYPE_NON_INDIVIDUAL_KEY)){
					ScoringRecommRule recommRuleYearsBCA = partyScoring.findRecommRuleByKey("NON_INDIVIDUAL_RULE_BUSINESS_YEARS_BCA");
					ScoringRecommRule recommRuleYearsBIL = partyScoring.findRecommRuleByKey("NON_INDIVIDUAL_RULE_BUSINESS_YEARS_BIL");
					ScoringRecommRule recommRuleYearsHighRisk = partyScoring.findRecommRuleByKey("NON_INDIVIDUAL_RULE_HIGH_RISK_INDUSTRY_LIMITED_YEARS_BUSINESS");
					if(recommRuleYearsBCA !=null && recommRuleYearsBCA.getResult() !=null && recommRuleYearsBCA.getResult().equals(Constants.SCORING_RECOMM_RESULT_FAIL_KEY) ){
						flag = true;
						break;
					}
					if(recommRuleYearsBIL !=null && recommRuleYearsBIL.getResult() !=null && recommRuleYearsBIL.getResult().equals(Constants.SCORING_RECOMM_RESULT_FAIL_KEY) ){
						flag = true;
						break;
					}
					if(recommRuleYearsHighRisk !=null && recommRuleYearsHighRisk.getResult() !=null && recommRuleYearsHighRisk.getResult().equals(Constants.SCORING_RECOMM_RESULT_FAIL_KEY) ){
						flag = true;
						break;
					}	
				}		
			}	
		}
		return flag;
	}

	public String getOfferType(){
    	LOG.debug("Inside FacScoring : getOfferType() from Mvel...");
    	
    	if(StringUtils.equals(getIsEligibleForSBAExpress(), "YES"))	{
    		return "SBA Only";
    	}else{
    		return "Conventional";
    	}
    }
    public String getTotalRequestedExposureFacility(){
    	LOG.debug("Inside FacScoring : getTotalRequestedExposureFacility() from Mvel...");
    	BigDecimal total = BigDecimal.ZERO;
    	
    	BigDecimal directExistingExposureAmount = getDirectExistingExposureAmount(getFacility());
    	BigDecimal directRequestedExposureAmount = getDirectRequestedExposureAmount(getFacility());
    	total = directExistingExposureAmount.add(directRequestedExposureAmount);
    	
    	return String.valueOf(total);
    	
    }
    public String getRequestedAmountFacility(){
    	LOG.debug("Inside FacScoring : getRequestedAmountFacility() from Mvel...");
    	return String.valueOf(getDirectRequestedExposureAmount(getFacility()));
    }
    public String getExistingExposureFacility(){
    	LOG.debug("Inside FacScoring : getExistingExposureFacility() from Mvel...");
    	return String.valueOf(getDirectExistingExposureAmount(getFacility()));
    }
    public String getExposureAdjustmentFacility(){
    	LOG.debug("Inside FacScoring : getExposureAdjustmentFacility() from Mvel...");
    	return String.valueOf(getDirectExposureAdjustments(getFacility()));
    }
    public String getCommercialCustomScore(){
    	LOG.debug("Inside FacScoring : getCommercialCustomScore() from Mvel...");
    	return null;
    }
    public String getValidityCommercialCustomScore(){
    	LOG.debug("Inside FacScoring : getValidityCommercialCustomScore() from Mvel...");
    	return null;
    }
    public String getPartyAssociatedCommercialCustomScore(){
    	LOG.debug("Inside FacScoring : getPartyAssociatedCommercialCustomScore() from Mvel...");
    	return null;
    }
    public String getConsumerCustomScore(){
    	LOG.debug("Inside FacScoring : getConsumerCustomScore() from Mvel...");
    	return null;
    }
    public String getValidityConsumerCustomScore(){
    	LOG.debug("Inside FacScoring : getValidityConsumerCustomScore() from Mvel...");
    	return null;
    }
    public String getCreditBureauAssociatedWithConsumer(){
    	LOG.debug("Inside FacScoring : getCreditBureauAssociatedWithConsumer() from Mvel...");
    	return null;
    }
    public String getPartyAssociatedConsumerCustomScore(){
    	LOG.debug("Inside FacScoring : getPartyAssociatedConsumerCustomScore() from Mvel...");
    	return null;
    }
    public String getFacilityRank(){
    	LOG.debug("Inside FacScoring : getFacilityRank() from Mvel...");
    	return String.valueOf(this.getFacilityDer().getfacilityRank());
    }
    public String getLineAdjustmentFactor(){
    	LOG.debug("Inside FacScoring : getLineAdjustmentFactor() from Mvel...");
    	
    	BigDecimal existingExposureDirect =  getDirectExistingExposureAmount(getFacility());
    	BigDecimal requestedExposureDirect =  getDirectRequestedExposureAmount(getFacility());
    	double totalRequestedExposure = existingExposureDirect.add(requestedExposureDirect).doubleValue();
    	String value="";
    	if(totalRequestedExposure <= 5000){
    		value="0.2";
    	}else if( totalRequestedExposure > 5000 && totalRequestedExposure <= 25000){
    		value="0.1";
    		
    	}else if( totalRequestedExposure > 25000 && totalRequestedExposure <= 100000){
    		value="0.05";
    	}else if( totalRequestedExposure > 100000 && totalRequestedExposure <= 250000){
    		value="0.02";
    	}
    	return value;
    }
    
    //API that will give the Direct ExistingExposureAmount for a Particular facility.
    private BigDecimal getDirectExistingExposureAmount(Facility facility){
		BigDecimal total = BigDecimal.ZERO;
		
		if(getScoring()!=null && getScoring().getRequest() != null && getScoring().getRequest().getExposure() != null ){
			Exposure exposure =  getScoring().getRequest().getExposure();
			
			List<CustomerFacilityExposure>  directExposures = exposure.getDirectFacilityExposures();
			for(CustomerFacilityExposure directExposure : directExposures){
				if(directExposure.getExistingExposurePerFacility(facility) != null){		
					total = directExposure.getExistingExposurePerFacility(facility);
					return total;
				}
			}
		}		
		return total;
	}
    
    //API that will give the Direct RequestedExposureAmount for a Particular facility.
    private BigDecimal getDirectRequestedExposureAmount(Facility facility){
		BigDecimal total = BigDecimal.ZERO;
		
		if(getScoring()!=null && getScoring().getRequest() != null && getScoring().getRequest().getExposure() != null ){
			Exposure exposure =  getScoring().getRequest().getExposure();
			
			List<CustomerFacilityExposure>  directExposures = exposure.getDirectFacilityExposures();
			for(CustomerFacilityExposure directExposure : directExposures){
				if(directExposure.getRequestedExposurePerFacility(facility) != null){		
					total = directExposure.getRequestedExposurePerFacility(facility);
					return total;
				}
			}
		}		
		return total;
	}

  //API that will give the Direct ExposureAdjustments for a Particular facility.
    private BigDecimal getDirectExposureAdjustments(Facility facility){
		BigDecimal total = BigDecimal.ZERO;
		
		if(getScoring()!=null && getScoring().getRequest() != null && getScoring().getRequest().getExposure() != null ){
			Exposure exposure =  getScoring().getRequest().getExposure();
			
			List<CustomerFacilityExposure>  directExposures = exposure.getDirectFacilityExposures();
			for(CustomerFacilityExposure directExposure : directExposures){
				if(directExposure.getExposureAdjustmentsPerFacility(facility) != null){		
					total = directExposure.getExposureAdjustmentsPerFacility(facility);
					return total;
				}
			}
		}		
		return total;
	}

	public String getMaximumLineAdjustmentAmount(){
    	LOG.debug("Inside FacScoring : getMaximumLineAdjustmentAmount() from Mvel...");
    	
    	BigDecimal existingExposureDirect =  getDirectExistingExposureAmount(getFacility());
    	BigDecimal requestedExposureDirect =  getDirectRequestedExposureAmount(getFacility());
    	BigDecimal totalRequestedExposure = existingExposureDirect.add(requestedExposureDirect);
    	BigDecimal ajustmentFactor =BigDecimal.ZERO;
    	if(getLineAdjustmentFactor() != null && NumberUtils.isNumber(getLineAdjustmentFactor()) ){
         ajustmentFactor = new BigDecimal(getLineAdjustmentFactor());
    	}
    	
    	return String.valueOf(totalRequestedExposure.multiply(ajustmentFactor));
    }
    public String getTotalIncrementalAmountAssignedFacility(){
    	LOG.debug("Inside FacScoring : getTotalIncrementalAmountAssignedFacility() from Mvel...");
    	BigDecimal total = BigDecimal.ZERO;
    	
    	return String.valueOf(total);
    }
    public String getTotalRecommendedAmountAssignedFacility(){
    	LOG.debug("Inside FacScoring : getTotalRecommendedAmountAssignedFacility() from Mvel...");
    	BigDecimal value = BigDecimal.ZERO;
    	BigDecimal totalIncrementalAmount = new BigDecimal(getTotalIncrementalAmountAssignedFacility());
    	BigDecimal exposureAmount =  getDirectExistingExposureAmount(getFacility()).add(getDirectRequestedExposureAmount(getFacility())); 
    	BigDecimal exposureAdjustments = getDirectExposureAdjustments(getFacility());
    	
    	value = totalIncrementalAmount.add(exposureAmount).add(exposureAdjustments);
    
    	return String.valueOf(value);
    }
    public String getLineAssignmentSatisfiesAmountFacility(){
    	LOG.debug("Inside FacScoring : getLineAssignmentSatisfiesAmountFacility() from Mvel...");
    	BigDecimal totalIncrementalAmount = new BigDecimal(getTotalIncrementalAmountAssignedFacility());
    	BigDecimal newRequestedAmount = getDirectRequestedExposureAmount(getFacility());
    	if(totalIncrementalAmount.compareTo(newRequestedAmount) >= 0){
    		return "YES";	
    	}else{
    		return "NO";
    	}
    }
    
    public boolean getCreditScoreRuleResult(){
    	LOG.debug("Inside FacScoring : getCreditScoreRuleResult() from Mvel...");
    	return false;
    }
    public boolean getBcaOrLineIncreasedRuleResult(){
    	LOG.debug("Inside FacScoring : getBcaOrLineIncreasedRuleResult() from Mvel...");
    	boolean flag = true;
   
    	return flag;
    }
    public boolean getFacilityTypeRuleResult(){
    	LOG.debug("Inside FacScoring : getFacilityTypeRuleResult() from Mvel...");
    	boolean flag = false;
    	
    	return flag;
    }

    public String getTestOgnl() {
    	return null;
    }
    public boolean getTestOgnlRcmRule() {
    	return false;
    }
	public void initScoring(){
		
		LOG.debug("Inside FacScoring: initScoring()");
		List<BaseDataObject> scrAttributeChoices = LookupService.getResults(
				Constants.SCORE_ATTRIBUTE_CHOICE_BY_APPLICABILITY_NAMED_QUERY,
				Constants.APPLICABILITY_TYPE_KEY,
				Constants.APPLICABILITY_TYPE_FACILITY_KEY );
		
		List<BaseDataObject> scrRecomRules = LookupService.getResults(
                Constants.SCORE_RECOM_RULES_BY_APPLICABILITY_NAMED_QUERY,
                Constants.APPLICABILITY_TYPE_KEY,
                Constants.APPLICABILITY_TYPE_FACILITY_KEY );
		
		EntityService es = new EntityService();

		LOG.debug("scrAttributeChoices list size fetched from DB is:- " + scrAttributeChoices.size());
		for (BaseDataObject baseDataObject : scrAttributeChoices){
			ScrAttributeSeed scrAttributeChoiceSeed = (ScrAttributeSeed) baseDataObject;
            ScoringAttribute scoringAttribute = (ScoringAttribute) es.createNew(ScoringAttribute.class);
            scoringAttribute.setAttributeKey(scrAttributeChoiceSeed.getKey());
            scoringAttribute.setAttributeName(scrAttributeChoiceSeed.getName());
            scoringAttribute.setAttributeDescription(scrAttributeChoiceSeed.getDescription());
            
            scoringAttribute.setFacility(this.getFacility());
            scoringAttribute.setFacScoring(this);
            this.addToScoringAttributes(scoringAttribute);
            
	    }
		
		LOG.debug("scrRecomRules list size fetched from DB is:- " + scrRecomRules.size());
		for(BaseDataObject baseDataObject : scrRecomRules){

			ScrRcmRuleSeed scrRecomRule = (ScrRcmRuleSeed) baseDataObject;
			ScoringRecommRule scoringRecommRule = (ScoringRecommRule) es.createNew(ScoringRecommRule.class);
			scoringRecommRule.setRuleKey(scrRecomRule.getKey());
			scoringRecommRule.setRuleName(scrRecomRule.getName());
			scoringRecommRule.setDescription(scrRecomRule.getDescription());

			AttributeChoice attributeChoiceScoreResult = (AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", Constants.SCORING_RECOMM_RESULT_FAIL_KEY);
			scoringRecommRule.setResult(attributeChoiceScoreResult);
			scoringRecommRule.setRecommCategory(scrRecomRule.getRecommendationCategory());
			scoringRecommRule.setRecommendationDecision(scrRecomRule.getRecommendation());
			scoringRecommRule.setReasonCode((scrRecomRule.getRecommendationReasonCode() == null)? null : scrRecomRule.getRecommendationReasonCode().getRecomCode().toString());
			scoringRecommRule.setReasonLevel((scrRecomRule.getReasonLevel() == null) ? null : scrRecomRule.getReasonLevel().getValue());
			scoringRecommRule.setRecommendationLevel((scrRecomRule.getRecommendationLevel() == null)? null : scrRecomRule.getRecommendationLevel().getValue());
			scoringRecommRule.setExceptionAuthority(scrRecomRule.getExceptionAuthority());

			scoringRecommRule.setFacility(this.getFacility());
			scoringRecommRule.setFacScoring(this);
			this.addToScoringRecommRules(scoringRecommRule);
		}    
	}
	
	public ScoringAttribute findAttributeByKey(String attributeKey){
		LOG.debug("Inside FacScoring: findAttributeByKey()");
		for(ScoringAttribute attr: this.getScoringAttributes()){
			if(attr.getAttributeKey().equals(attributeKey)){
				LOG.debug("==== Attribute found ====");		
				return attr;
			}
		}
		return null;
	}

	public ScoringRecommRule findRecommRuleByKey(String ruleKey){
		LOG.debug("Inside FacScoring: findRecommRuleByKey()");
		for(ScoringRecommRule rule: this.getScoringRecommRules()){
			if(rule.getRuleKey().equals(ruleKey)){
				LOG.debug("=== RecommRule found ===");		
				return rule;
			}
		}
		return null;

	}
	public List<FacRecomRsn> getFacRecomRsnsDer(){
		List<FacRecomRsn> derFacRecomRsns = new ArrayList<FacRecomRsn>();
		for(FacRecomRsn facRcm : getFacRecomRsns()){
			if(facRcm !=null && facRcm.getRecommendationReason()!=null){
				derFacRecomRsns.add(facRcm);
			}
		}
		return derFacRecomRsns;
	}
	public void removeRecomRule(String ruleKey){
		if(getScoringRecommRules() !=null && getScoringRecommRules().size() >0){
			List<ScoringRecommRule> lstRules = getScoringRecommRules();
			Iterator<ScoringRecommRule> iter=lstRules.iterator();
			while (iter.hasNext()){
				ScoringRecommRule sr = iter.next();
				if(sr!=null && sr.getRuleKey().equals(ruleKey)){
					iter.remove();
				}			
			}
		}
	}
}

