package com.thirdpillar.codify.loanpath.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.thirdpillar.codify.loanpath.model.Customer;
import com.thirdpillar.codify.loanpath.model.RelationshipParty;
import com.thirdpillar.codify.loanpath.model.RiskRating;
import com.thirdpillar.foundation.util.StringUtils;

public class DroolsUtil {
	private static final Logger LOGGER = Logger.getLogger(DroolsUtil.class);
	
	public static int getValue(){
		return 12;
	}
	public static float getWorstRiskRating(List<RelationshipParty> relationshipParties){
		float worstRiskRating=0;
		LOGGER.info("Inside getWorstRiskRating!!!!!");
		if (relationshipParties != null) {
			for (RelationshipParty relationshipParty : relationshipParties) {
				LOGGER.info("RelationshipParty : " + relationshipParty);
				if(relationshipParty.getCustomer()!=null ){
					Customer customer = relationshipParty.getCustomer();
					if(customer.getRiskRatings()!=null){
						List<RiskRating> riskRatings = customer.getRiskRatings();
						float maxRR;
						maxRR = getIntValueOfMaxRR(riskRatings);
						if (maxRR > worstRiskRating) {
		
							worstRiskRating = maxRR;
						}
					}
				}
			}
		}
		LOGGER.info("worstRiskRating   : "+worstRiskRating);
		return worstRiskRating;
	}
	public static float getIntValueOfMaxRR(List<RiskRating> riskRatings){
		RiskRating riskRating;
		float currentRR = 0; 
		if(riskRatings!=null && !riskRatings.isEmpty()){
			riskRating = riskRatings.get(0);		
			LOGGER.info("RiskRating Source  :"
						+ riskRating.getSource().getKey());
				
			LOGGER.info("RiskRating  :" + riskRating);
			LOGGER.info("RiskRating Key : "
							+ riskRating.getObligorRR().getKey());
			LOGGER.info("RiskRating Value : "
							+ riskRating.getObligorRR().getValue());
					String rrString = riskRating.getObligorRR().getValue();
					currentRR = getFloatValueRR(rrString);
		}
		LOGGER.info("maxRR   ::"+currentRR);
		return currentRR;
	}
	public static float getFloatValueRR(String rrString){
		String temp=rrString;
		if(temp.contains("+") || temp.contains("-")){
			temp=temp.substring(0, temp.length()-1);
		}
		float currentRR=Integer.parseInt(temp);
		if(rrString.contains("+")){
			currentRR=currentRR-0.1f;
		}
		if(rrString.contains("-")){
			currentRR=currentRR+0.1f;
		}
		return currentRR;
	}
	public static int getIntValueCreditAuthorityLevel(String creditAuthLevel){
		if (StringUtils.isNotEmpty(creditAuthLevel)) {
			if(StringUtils.equals(creditAuthLevel,"Level 1")) {
				return 1;				
			} else if(StringUtils.equals(creditAuthLevel,"Level 2")) {
				return 2;				
			} else if(StringUtils.equals(creditAuthLevel,"Level 3")) {
				return 3;				
			} else if(StringUtils.equals(creditAuthLevel,"Level 4")) {
				return 4;				
			} else if(StringUtils.equals(creditAuthLevel,"Level 5")) {
				return 5;				
			} else if(StringUtils.equals(creditAuthLevel,"Level 6")) {
				return 6;				
			} else if(StringUtils.equals(creditAuthLevel,"Level 7")) {
				return 7;				
			} else if(StringUtils.equals(creditAuthLevel,"Level 8")) {
				return 8;				
			} else if(StringUtils.equals(creditAuthLevel,"Level 9 (Governance & Control)")) {
				return 9;				
			} 
		}
		return 0;
	}
}
