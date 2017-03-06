package com.thirdpillar.codify.loanpath;
/**
 * This interface holds inner interfaces, each representing a valid ValidationGroup/Scenario.
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
public interface ValidationGroup {
	
	public interface FacilityBEValidateConfirm {
    }
	
	interface AssignNewCapitalProviderValidationGroup {
	}

  
	 /**
     * Customer
     */
    interface CustomerSubmit {
    }
    
	 /**
     * Create Request
     */
    interface CreateRequest {
    }
        
    
	 /**
     * Pending Sales
     */
    interface PendingSales {
    }
    
	 /**
     * Pending Submitted To Credit
     */
    interface PendingSubmittedToCredit {
    }
    
	 /**
     * Customer
     */
    interface PendingAnalysis {
    }
    
    
    interface PendingDecision {
    }
    
    interface CreditAnalystPendingDecision {
    }
    
    interface PendingNoia {
    }
    
    interface PendingReturnToSales {
    }    
    
    interface Approved {
    }
    
    interface Declined {
    }
    
    // Others
	interface  EsignatureGroup {
	}	
	
	interface  GenerateCoa {
	}	
	
    
    
    

    // ***********************************************************
    // Below this line, all unsorted ones are listed. Eventually we delete every underneath this
    //*************************************************************
	 /**
     * Request Submit Validation Group
     */
    interface SubmitForDecision {
    }
    
    

	interface SubmitToLP{
    }
	
	/**
	 * Application Submitted to Credit Group
	 */
	interface SubmittedToCredit{
    } 
	
	/**
	 * Bureau Report manually pulled from LandSafe 
	 */
	interface SubmitToLandsafe{
    }
    
    /**
	 * Mods workflow Validation group 
	 */
	interface ModsFlow{		
	}
	
	interface SubmitToDocs{		
	}
 
	interface RequestDeclineValidationsGroup {
	}
	
	interface RequestModificationGroup {
	}

	interface FacilityDeclineValidationsGroup {
	}
	
	interface FacilityNoiaValidationsGroup{
	}
	
	interface FacilityWithdrawValidationsGroup{
	}
	
	interface CustomerDeclineValidationsGroup {
	}
	
	interface RelationshipDeclineValidationsGroup {
	}
	
	interface  DocumentDeclineValidationsGroup {
	}   
	
	
	interface RequestApproveValidationsGroup {
	}
	
	interface FacilityApproveValidationsGroup {
	}
	
	interface CustomerApproveValidationsGroup {
	}
	
	interface RelationshipApproveValidationsGroup {
	}
	
	interface  DocumentApproveValidationsGroup {
	}   
	
	
	interface  SubmitForNoia {
	}  
	
	interface  DocumentStatusValidationsGroup {
	}
	
	interface  AmlVerificationGroup {
	}
	
	interface  SubmitFilingGroup {
	}
	
	interface FacilityCounterOfferValidationsGroup {
	}
	interface  FacilityModificationGroup {
	}
	
	interface DobVerificationGroup {
	}
	interface NameVerificationGroup {
	}
	interface AddressVerificationGroup {
	}
	
	interface StatusValidationGroup {
	}
	interface StatusValidationForAppSent {
	}
	
	interface CustomerExposureLimit{
		
	}
	
	interface SelectOption{
		
	}
	interface CalculateOption{
		
	}
	interface CalculateOptionForFacility{
		
	}
	interface PartnerSubmit{
		
	}
	interface ProgramSubmit{
		
	}
	interface UploadedDocumentFinalStatus{
		
	}
	
	interface GeneratedDocumentFinalStatus{
		
	}
	
	interface UploadedDocumentPreValidation{
		
	}
	
	interface GeneratedDocumentPreValidation{
		
	}
	
	interface OneStopAppValidation{
		
	}
	
	interface OneStopExposureValidation{
		
	}

	interface SubmitForReview{
		
	}
	
	interface RequestSubmit{
		
	}
	
	interface ManualBureauPullCustomer{
		
	}
	
	interface ManualBureauPullBureau{
		
	}
	
	interface WithdrawApplicationForRequest{
		
	}
	
	interface WithdrawApplicationForFacility{
		
	}
	
	interface WithdrawQuote{
		
	}
	
	interface FundSubmit{
		
	}
	
	interface ReplacedFundSubmit{
		
	}
	
	interface FacilityPortionSumbit{
		
	}
	
	interface ApproveDebtorValidationsGroup{
		
	}
	
	interface PendingCompleted{
		
	}
	
	interface FacilityAcceptPortionSumbit{
		
	}
	
	interface FundedByCPSubmit{
		
	}
	
	interface CPPaidInterestSubmit{
		
	}
	
	interface CoaCompleted{
	}

	interface PortfolioSubmit{
		
	}
	
	interface DebtorDecision{}
	
	interface DebtorDecisionEligible{
		
	}
}
