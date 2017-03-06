/*
 * Copyright (c) 2005-2015 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.constants;

/**
 * 
 * @author Sajan Monga
 * @version 1.0
 * @since 1.0
 */
public class Constants {
	
	private Constants(){
		
	}

	// ~ Static fields/initializers
	// -------------------------------------------------------------------------------------

	// ~ Static fields/initializers
	// -------------------------------------------------------------------------------------
	public static final String SCORING_RECOMM_RESULT_FAIL_KEY = "SCORING_RECOMM_RESULT_FAIL";
	public static final String CUSTOMER_TYPE_INDIVIDUAL_KEY = "CUSTOMER_TYPE_INDIVIDUAL";
	public static final String RECOMMENDATION_TYPE_FACILITY_KEY = "FACILITY_RECOMMENDATION";
	public static final String BUSINESS_BANKING_BIL_KEY = "BUSINESS_BANKING_BIL";
	public static final String BUSINESS_INSTALLMENT_LOAN_KEY = "BUSINESS_INSTALLMENT_LOAN";
	public static final String BUSINESS_INSTALLMENT_LOAN_6MONTH_KEY = "BUSINESS_INSTALLMENT_LOAN_6MONTH";
	public static final String BUSINESS_CREDIT_ACCOUNT_KEY = "BUSINESS_CREDIT_ACCOUNT";
	public static final String SCORE_ATTRIBUTE_CHOICE_BY_APPLICABILITY_NAMED_QUERY = "ScrAttributeSeed.byApplicability";
	public static final String APPLICABILITY_TYPE_KEY = "applicability_key";
	public static final String APPLICABILITY_TYPE_FACILITY_KEY = "APPLICABILITY_TYPE_FACILITY";
	public static final String BUSINESS_CHECKING_PLUS_KEY = "BUSINESS_CHECKING_PLUS";
	public static final String SCORE_RECOM_RULES_BY_APPLICABILITY_NAMED_QUERY = "ScrRcmRuleSeed.byApplicability";
	public static final String RECOMMENDATION_DECISION_REVIEW_KEY = "RECOMMENDATION_DECISION_REVIEW";
	public static final String RECOMMENDATION_DECISION_DECLINE_KEY = "RECOMMENDATION_DECISION_DECLINE";
	public static final String RECOMMENDATION_DECISION_APPROVE_KEY = "RECOMMENDATION_DECISION_APPROVE";
	public static final String CUSTOMER_TYPE_NON_INDIVIDUAL_KEY = "CUSTOMER_TYPE_NON_INDIVIDUAL";
	public static final String APPLICABILITY_TYPE_INDIVIUAL_KEY = "APPLICABILITY_TYPE_INDIVIDUAL";
	public static final String APPLICABILITY_TYPE_NON_INDIVIUAL_KEY = "APPLICABILITY_TYPE_NONINDIVIDUAL";
	public static final String SCORE_RECOM_RULES_RESULT_PASS = "Pass";
	public static final String SCORE_RECOM_RULES_RESULT_FAIL = "Fail";
	public static final String FACILITY_EXPOSURE_TYPE_PIPELINE = "P";
	public static final String RECOMMENDATION_TYPE_PARTY_KEY = "PARTY_RECOMMENDATION";
	public static final String SCORING_RECOMM_RESULT_PASS_KEY = "SCORING_RECOMM_RESULT_PASS";
	public static final String RECOMMENDATION_CATEGORY_PRE_DECLINE_REVIEW_KEY = "RECOMMENDATION_CATEGORY_PRE_DECLINE_REVIEW";
	public static final String PARTY_ROLE_TYPE_PRIMARY_BORROWER_KEY = "PARTY_ROLE_TYPE_PRIMARY_BORROWER";
	public static final String EMPTY_STRING = "";
	public static final String APPLICABILITY_TYPE_APPLICATION_KEY = "APPLICABILITY_TYPE_APPLICATION";
	public static final String EXISTING_AVERAGE_BCP_UTILIZATION_KEY_1 = "EXISTING_AVERAGE_BCP_UTILIZATION_1";
	public static final String EXISTING_AVERAGE_BCA_UTILIZATION_KEY_1 = "EXISTING_AVERAGE_BCA_UTILIZATION_1";
	public static final String APPLICATION_RULE_RAC_ANALYSIS_REQUIRED_KEY_1 = "APPLICATION_RULE_RAC_ANALYSIS_REQUIRED_1";
	public static final String BANK_ACCOUNT_TYPE_DDA_KEY = "BANK_ACCOUNT_TYPE_DDA";
	public static final String EXISTING_CITI_CUSTOMER = "Existing Citi Customer";
	public static final String EXISTING_CITI_CUSTOMER_EXPOSURE_KEY_1 = "EXISTING_CITI_CUSTOMER_EXPOSURE_1";
	public static final String NEW_TOBANK_EXPOSURE_KEY_1 = "NEW_TOBANK_EXPOSURE_1";
	public static final String AVERAGE_FICO_CUTOFF_KEY_1 = "AVERAGE_FICO_CUTOFF_1";
	public static final String LEGAL_STRUCTURE_NOT_FOR_PROFIT_KEY = "LEGAL_STRUCTURE_NOT_FOR_PROFIT";
	public static final String CITI_CURRENT_BALANCE_KEY_1 = "CITI_CURRENT_BALANCE_1";
    public static final String CITI_DEPOSIT_TENURE_KEY_1 = "CITI_DEPOSIT_TENURE_1";
    public static final String NEW_CITI_CUSTOMER = "New To Bank";
    public static final String CITI_CREDIT_EXPOSURE_EXCEEDS_KEY_1 = "CITI_CREDIT_EXPOSURE_EXCEEDS_1";
    
    //NAICS IndustryCategory
    public static final String NAICS_RISK_PROHIBITED_KEY = "NAICS_RISK_PROHIBITED";
    public static final String NAICS_RISK_SPECILIZED_KEY = "NAICS_RISK_SPECILIZED";
    public static final String NAICS_RISK_HIGH_RISK_KEY = "NAICS_RISK_HIGH_RISK";
    public static final String NAICS_GRP_CONST_KEY = "NAICS_GRP_CONST";
    public static final String BANK_ACCOUNT_TYPE_SAVINGS_KEY = "BANK_ACCOUNT_TYPE_SAVINGS";
    
    //scoring Attribute Params Keys
    public static final String CITI_RELATIONSHIP_KEY_1 = "APPLICTION_ATTR_CITI_RELATIONSHIP_1";
    public static final String CITI_RELATIONSHIP_KEY_2 = "APPLICTION_ATTR_CITI_RELATIONSHIP_2";
    
    public static final String BANK_ACCOUNT_TYPE_CHECKING_KEY = "BANK_ACCOUNT_TYPE_CHECKING";
    
    // Facility Exposure Type
    public static final String NO_LOAN_EXPOSURE_FROM_AFS_IMPACS = "No Loan Exposure from AFS / IMPACS";
    
    //HumanTask
    public static final String WEBAPP_SERVER_BASEURL = "webapp.server.baseurl";
    public static final String CRUD = "/crud/";
    public static final String VIEW_CANVAS_CRUD = "/view.seam?canvasType=crud&eid=";
    
    // Application Type constants
    public static final String APPLICATION_TYPE_SMALL_TICKET_KEY = "APPLICATION_TYPE_SMALL_TICKET";
    public static final String APPLICATION_TYPE_STANDARD_TICKET_KEY = "APPLICATION_TYPE_STANDARD";

    // Party Level Keys values
    public static final String PARTY_ROLE_TYPE_BORROWER_KEY = "PARTY_ROLE_TYPE_BORROWER";
    public static final String PARTY_ROLE_TYPE_CLIENT_KEY = "PARTY_ROLE_TYPE_CLIENT";
    public static final String PARTY_ROLE_TYPE_DEBTOR_KEY = "PARTY_ROLE_TYPE_DEBTOR";
    public static final String DEBTOR_PENDING_STATUS_KEY = "DEBTOR_STATUS_PENDING";
    public static final String PARTY_ROLE_TYPE_GUARANTOR_KEY = "PARTY_ROLE_TYPE_GUARANTOR";
    public static final String REQUEST_STATUS_PORTFOLIO_MANAGMENT_KEY = "REQUEST_STATUS_PORTFOLIO_MANAGMENT";
    
    public static final String PARTY_ROLE_TYPE_PRIMARY_BORROWER_KEY_VAL = "Primary Borrower";
 // RAc Exception
    public static final String RAC_EXCEPTION_AUTHORITY_SCO_KEY = "RAC_EXCEPTION_AUTHORITY_SCO";
    public static final String RECOMMENDATION_CATEGORY_JUDGMENTAL_REVIEW_KEY =
        "RECOMMENDATION_CATEGORY_JUDGMENTAL_REVIEW";
    public static final String RECOMMENDATION_CATEGORY_AUTODECLINE_KEY = "RECOMMENDATION_CATEGORY_AUTODECLINE";
    public static final String RECOMMENDATION_CATEGORY_DECLINE_KEY = "RECOMMENDATION_CATEGORY_DECLINE";
    public static final String RECOMMENDATION_CATEGORY_APPROVE_KEY = "RECOMMENDATION_CATEGORY_APPROVE";
 // Attribute:
    //TODO Not as per coding Standard
    public static final String ATTRIBUTECHOICEBYKEY = "AttributeChoice.byKey";
    public static final String ATTRIBUTECHOICEBYKEYVAL = "key";
    public static final String ATTRIBUTE_CHOICE_BY_KEY = "AttributeChoice.byKey";
    public static final String ATTRIBUTE_CHOICE_KEY = "key";
    
    public static final String DOCUMENT_RELATED_TO_FACILITY_KEY = "DOCUMENT_RELATED_TO_FACILITY";
    public static final String DOCUMENT_RELATED_TO_PARTY_KEY = "DOCUMENT_RELATED_TO_PARTY";
    //customer
    //constant For EOD
    public static final String ADDRESS_USE_TYPE_LEGAL = "ADDRESS_USE_TYPE_LEGAL";
    public static final String ADDRESS_USE_TYPE_MAILING = "ADDRESS_USE_TYPE_MAILING";
    public static final String ADDRESS_USE_TYPE_HOME = "ADDRESS_USE_TYPE_HOME";
    
    //constant For NAICS_CODE
    public static final String NAICS_CODE_KEY = "NAICS_CODE";

    public static final String DEALS_STATUS_UNASSIGNED_KEY = "DEALS_STATUS_UNASSIGNED";
    public static final String DEALS_STATUS_REJECTED_KEY = "DEALS_STATUS_REJECTED";
    public static final String DEALS_STATUS_ACCEPTED_KEY = "DEALS_STATUS_ACCEPTED";
    public static final String FUNDING_REQUEST_STATUS_UNALLOCATED_KEY = "FUNDING_REQUEST_STATUS_UNALLOCATED";
    public static final String CREDIT_FACILITY_TYPE_LINE_OF_CREDIT_KEY = "CREDIT_FACILITY_TYPE_LINE_OF_CREDIT";
    public static final String CREDIT_FACILITY_TYPE_LOAN_KEY = "CREDIT_FACILITY_TYPE_LOAN";
    public static final String AMOUNT_OR_PERCENT_PERCENT_KEY = "AMOUNT_OR_PERCENT_PERCENT";
    public static final String DEALS_STATUS_ASSIGNED_KEY="DEALS_STATUS_ASSIGNED";   
    
    public static final String HumanTask_byWorkItem = "com.thirdpillar.codify.loanpath.model.HumanTask.byWorkItem";
    public static final String TASKBY_TASK_NAME = "com.thirdpillar.codify.loanpath.model.HumanTask.getTasksByTaskName";
    
    public static final String BRACKET = "]";
    public static final String LEFT_BRACKET = "[";
    
    public static final String COMMA = ", ";
    public static final String BLANK =" ";
    
    //Comment
    public static final String TIME_FORMAT= "hh:mm a";
    public static final String DATE_FORMAT = "MM/dd/yyyy";
}
