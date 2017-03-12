/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.Days;

import javax.persistence.Transient;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.codify.loanpath.model.CustomerFacilityExposure;
import com.thirdpillar.codify.loanpath.util.Utility;
import com.thirdpillar.foundation.codify.constants.CodifyConstants;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.model.WorkflowAware;
import com.thirdpillar.foundation.service.ContextHolder;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;
import com.thirdpillar.foundation.util.StringUtils;
import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

/**
 * Model class for Facility
 *
 * @author   Harmit Mann
 * @version  1.0
 * @since    1.0
 */


@XStreamLookupCollectionByOGNL.List(
    {
        @XStreamLookupCollectionByOGNL(
            name = "byExternalIdentifier",
            keys = { "externalIdentifier" }
        ),
        	@XStreamLookupCollectionByOGNL(
                    name = "byRefNumber",
                    keys = { "refNumber"}
                )
    }
)
public class Facility extends BaseDataObject{

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 1010356666987116092L;
    private static final Logger LOG = Logger.getLogger(Facility.class);

    
    //~ Instance fields ------------------------------------------------------------------------------------------------

    @Transient private int facilityRank;
    @Transient private Date maturityDtTemp;



    public int getfacilityRank() {
        return facilityRank;
    }
    
    public void setfacilityRank(int facilityRank) {
        this.facilityRank = facilityRank;
    }
    
    public List<Customer> getAccountCustomers(){
    	List<Customer> customers = new ArrayList<Customer>();

    	for(FacilityCustomerRole facilityCustomerRole : getFacilityCustomerRoles()){
    		if(facilityCustomerRole.getPartyRole() != null && "PARTY_ROLE_TYPE_BORROWER".equals(facilityCustomerRole.getPartyRole().getKey())){
    			customers.add(facilityCustomerRole.getCustomer());
    		}
    	}
    	return customers;
    }

    public List<Customer> getIndividualCustomers(){
    	List<Customer> customers = new ArrayList<Customer>();

    	for(FacilityCustomerRole facilityCustomerRole : getFacilityCustomerRoles()){
    		if(facilityCustomerRole.getCustomer() != null && facilityCustomerRole.getCustomer().isIndividual()){
    			customers.add(facilityCustomerRole.getCustomer());
    		}
    	}

    	return customers;
    }
    
    public List<Collateral> getCustomersCollaterals(){
    	List<Collateral> collateralList = new ArrayList<Collateral>();
    	for(FacilityCustomerRole facilityCustomerRole : getFacilityCustomerRoles()){
    		if(facilityCustomerRole.getCustomer() != null){
    			Customer customer=facilityCustomerRole.getCustomer();
    			if(customer!=null){
    				if(customer.getCollaterals()!=null){
    					for(Collateral collateral : customer.getCollaterals()){
    						collateralList.add(collateral);
    					}
    				}
    				
    			}
    		}
    	}

    	return collateralList;
    }
    


    public Customer getFirstAccountCustomer(){
    	Customer customer = null ;

    	if ( getAccountCustomers() != null && getAccountCustomers().size() >0) {
    			customer = getAccountCustomers().get(0);
    		}
    	return customer;
    }



    public BigDecimal getRequestedLoanAmtDer(){
		if(getRequestedLoanAmt() != null && !StringUtils.isEmpty(getRequestedLoanAmt().toString())){
			return Utility.formatBigDecimal(getRequestedLoanAmt());
		}
		return getRequestedLoanAmt();
    }

    public BigDecimal getApprovedLoanAmtDer(){
    	return getApprovedLoanAmt();
    }
    public Date getApprovalExpirationDateDer(){
    	return getApprovalExpirationDate();
    }
    
    public Integer getRequestedTermDer(){
    	return getRequestedTerm();
    }
    
    public Integer getApprovedTermDer(){
    	return getApprovedTerm();
    }
    
    public Customer getPrimaryBorrower() {
    	for(FacilityCustomerRole facilityCustomerRole : getFacilityCustomerRoles()){
    		if(facilityCustomerRole.getPartyRole() != null 
    				&& (Constants.PARTY_ROLE_TYPE_CLIENT_KEY.equals(facilityCustomerRole.getPartyRole().getKey())
    						|| "PARTY_ROLE_TYPE_CORPORATE_BORROWER".equals(facilityCustomerRole.getPartyRole().getKey()))
    				&& facilityCustomerRole.getPrimaryBorrower()){
    			return facilityCustomerRole.getCustomer();
    		}
    	}
    	return null;
    }
    
    /**
     * Checks product/facility is about to expire in supplied days
     * @param days
     * @return true if facility is going to expire in supplied days.
     */
    public boolean isExpiring(int days){
    	if(getApprovalExpirationDate() == null){
    		return false;
    	}
    	
    	DateTime expirationDate = new DateTime(getApprovalExpirationDate());
    	return Days.daysBetween(new DateTime(),expirationDate).getDays() <= days; 
    }
    
    /**
     * Checks product/facility is due for renewal in supplied days
     * @param days
     * @return true if facility is due for renewal in supplied days.
     */
    public boolean isRenewalDue(int days){
    	if(getRenewalDate() == null) {
    		return false;
    	}
    	
    	DateTime renewalDate = new DateTime(getRenewalDate());
    	return Days.daysBetween(new DateTime(),renewalDate).getDays() <= days; 
    }

    public boolean isFacilityType(String key) {

        if ((getFacilityType() != null) && key.equalsIgnoreCase(getFacilityType().getKey())) {
            return true;
        }
            return false;
    }
    
    public boolean isExposureFacilityType(String key) {

        if ((getExposureLimitFacilityType() != null) && key.equalsIgnoreCase(getExposureLimitFacilityType().getKey())) {
            return true;
        }
            return false;
    }
    
    public AttributeChoice getEffectiveProductType() {
    	if (getFacilityType() != null) {
    		return getFacilityType();
    	} else{
    		return getExposureLimitFacilityType();
    	}
    }

	public BigDecimal getTotalSyndicatedAmt() {
		BigDecimal total = BigDecimal.ZERO;
		for (Tranche tranche : getFacilityTranches()) {
			if (tranche.getTotalTrancheAmt() != null) {
				total = total.add(tranche.getTotalTrancheAmt());
			}
		}
		return total;
	}

	public BigDecimal getRemainingSyndicatedAmt() {
		return (getRequestedLoanAmt() != null) ? getRequestedLoanAmt().subtract(getTotalSyndicatedAmt()) : BigDecimal.ZERO;
	}
	
	public BigDecimal getFeeApportionPct(FeeAllocation feeAllocation) {
		
		List<Syndication> syndications = getAllSyndications();
		
		BigDecimal totalAmt = BigDecimal.ZERO;
		BigDecimal apportionedPct = BigDecimal.ZERO;
		BigDecimal totalFixedFeePct = BigDecimal.ZERO;
		BigDecimal syndicationAmt = BigDecimal.ZERO;
		if (feeAllocation.getSyndication() != null) {
			syndicationAmt = feeAllocation.getSyndication().getSyndicationAmount();
		}
		syndicationAmt =  (syndicationAmt != null) ? syndicationAmt : BigDecimal.ZERO;
		
		for (Syndication syndication : syndications) {
			FeeAllocation syndicationFeeAlloc = syndication.getFeeAllocation(feeAllocation.getFee());
			
			if (syndicationFeeAlloc != null && syndicationFeeAlloc.getFeePct() != null) {
				totalFixedFeePct = totalFixedFeePct.add(syndicationFeeAlloc.getFeePct());
			} else if (syndication.getSyndicationAmount() != null){
				totalAmt = totalAmt.add(syndication.getSyndicationAmount());
			}
		}
		
		BigDecimal remainingPct = new BigDecimal(100).subtract(totalFixedFeePct);
		
		if (totalAmt.doubleValue() > 0) {
			apportionedPct = syndicationAmt.divide(totalAmt,2,RoundingMode.HALF_UP).multiply(new BigDecimal(100));
		} 
		
		apportionedPct = apportionedPct.multiply(remainingPct).divide(new BigDecimal(100));
		return apportionedPct;
	}
	
	public List<Syndication> getAllSyndications() {
		List<Syndication> syndications = new ArrayList<Syndication>();
		if (getFacilityTranches() != null) {
			for (Tranche tranche : getFacilityTranches()) {
				if (tranche.getSyndications() != null && tranche.getSyndications().size() > 0) {
					syndications.addAll(tranche.getSyndications());
				}
			}
		}
		return syndications;
	}
	
	public void setupFeeAllocation() {
		if (getPricingOption() != null && getPricingOption().getStructure() != null && getPricingOption().getStructure().getFees() != null) {
			for (Fee fee : getPricingOption().getStructure().getFees()) {
				setupFeeAllocation(fee);
			}
		}
	}
    public String getNullSafeNameFromRequestDer(){
    	String requestName="";
    	if(getRequest() != null){
    		requestName = getRequest().getRequestName();
    	}
    	return requestName;
	}
	public void setupFeeAllocation(Fee fee) {
		if (getFacilityTranches() != null) {
			for (Tranche tranche : getFacilityTranches()) {
				if (tranche.getSyndications() != null && tranche.getSyndications().size() > 0) {
					for (Syndication syndication : tranche.getSyndications()) {						
						FeeAllocation feeAllocation = syndication.getFeeAllocation(fee);
						if (feeAllocation == null) {
							// create new feeAllocation
							EntityService es = new EntityService<BaseDataObject>();
							feeAllocation = (FeeAllocation) es.createNew(FeeAllocation.class);
							feeAllocation.setSyndication(syndication);
							feeAllocation.setFee(fee);
							syndication.addToFeeAllocations(feeAllocation);
						}
					}
				}
			}
		}		
	}
	
	public Boolean isShowSolveFor(){
		
		Boolean isVisible = false;
		if((getRequestedLoanAmt() != null && getApprovedLoanAmt() != null && getRequestedTerm() != null & getApprovedTerm() != null ) && (getRequestedLoanAmt().compareTo(getApprovedLoanAmt()) != 0) || (getRequestedTerm().compareTo(getApprovedTerm()) != 0)){
			isVisible = true;
		}
		return isVisible;
	}
	
	public Boolean isFinancedAmtValid(){
		if(getPricingOption() != null && getPricingOption().getStructure() != null && getPricingOption().getStructure().getFinancedAmount() != null && !getPricingOption().getStructure().getFees().isEmpty()){
			BigDecimal financedAmount = getPricingOption().getStructure().getFinancedAmount();
			for (Fee fee : getPricingOption().getStructure().getFees()) {
				if(fee.getFeePolicy() != null && fee.getFeePct() == null ){
					FeePolicy feepolicy = fee.getFeePolicy();
					if(feepolicy.getMinimumAmt()!= null){
						if(financedAmount.compareTo(feepolicy.getMinimumAmt()) != 1){
							return true;
						}
					}
					if(feepolicy.getMaximumAmt()!= null){
						if(financedAmount.compareTo(feepolicy.getMaximumAmt()) != -1){
							return true;
						}
					}
				}	
			}
		}
		return false;
	}
	
	 public String getFacilityProductCode() {
	        AttributeChoice afsProductTypeAttr = null;
	        String afsProductTypeCode = null;
	        String afsProductTypeKey = null;
	        afsProductTypeAttr = (AttributeChoice) getAfsProductType();

	        if (afsProductTypeAttr != null) {
	            afsProductTypeCode = afsProductTypeAttr.getCode();
	            afsProductTypeKey = afsProductTypeAttr.getKey();
	            LOG.debug("Facility Product Code :  " + afsProductTypeCode + " and Key:" + afsProductTypeKey);
	        }

	        return afsProductTypeCode;
	    }
	 public BigDecimal getAmountFinanced() {

	        if (getApprovedLoanAmt() != null){
	            return getApprovedLoanAmt();
	        }

	        return getRequestedLoanAmt();
	    }
	 
	 /**
	  * Validates the Debtors.
	  * Once a Debtor is added it should not be same as a PB or Guarantor
	  * @return
	  */
	 public boolean validateDebtors(){
		 boolean flag = true;
		 List<DebtorCustomer> debtors = getDebtors();
		 if(!debtors.isEmpty()){
			 Customer primaryBorrower = getPrimaryBorrower();
			 List<FacilityCustomerRole> guarantors = getGuarators();
			 //Check for if Debtor is same as PB
			 for(DebtorCustomer debtor : debtors){
				 if(debtor.getFacilityCustomerRole().getCustomer().getRefNumber().equals(primaryBorrower.getRefNumber())){
					 flag = false;
					 break;
				 }
				 for(FacilityCustomerRole guarantor : guarantors){
					 if(guarantor.getCustomer().getRefNumber().equals(debtor.getFacilityCustomerRole().getCustomer().getRefNumber())){
						 flag = false;
						 break;
					 }
				 }
			 }
		 }
		 return flag;
		 
	 }
	 
	 public List<FacilityCustomerRole> getGuarators(){
		 List<FacilityCustomerRole> guarantors = new ArrayList<FacilityCustomerRole>();
		 for(FacilityCustomerRole facilityCustomerRole : getFacilityCustomerRoles()){
	    		if(facilityCustomerRole.getPartyRole() != null 
	    				&& Constants.PARTY_ROLE_TYPE_GUARANTOR_KEY.equals(facilityCustomerRole.getPartyRole().getKey())){
	    			guarantors.add(facilityCustomerRole);
	    		}
	    	}
		 return guarantors;
	 }
	 
	 public List<FacilityCustomerRole> getAllDebtors(){
		 List<FacilityCustomerRole> debtors = new ArrayList<FacilityCustomerRole>();
		 for(FacilityCustomerRole facilityCustomerRole : getFacilityCustomerRoles()){
	    		if(facilityCustomerRole.getPartyRole() != null 
	    				&& Constants.PARTY_ROLE_TYPE_DEBTOR_KEY.equals(facilityCustomerRole.getPartyRole().getKey())){
	    			debtors.add(facilityCustomerRole);
	    		}
	    	}
		 return debtors;
	 }
	 
	 public List<FacilityCustomerRole> getFacilityCustomerRolesDer(){
		 List<FacilityCustomerRole> list = new ArrayList<FacilityCustomerRole>();
		 for(FacilityCustomerRole facilityCustomerRole : getFacilityCustomerRoles()){
	    		if(facilityCustomerRole.getPartyRole() != null 
	    				&& !Constants.PARTY_ROLE_TYPE_DEBTOR_KEY.equals(facilityCustomerRole.getPartyRole().getKey())){
	    			list.add(facilityCustomerRole);
	    		}
	    	}
		 return list;
	 }
	 
	/**
	 * Validates the Client requested loan amount
	 * based on client location and type of facility
	 * @return flag
	 */
	public boolean validateRequestedAmount() {
		boolean flag = false;
		AttributeChoice productType = getFacilityType();
		if (productType == null) {
			return true;
		}	
		if(getPrimaryBorrower() != null && getPrimaryBorrower().getLegalAddressInfo() != null && getPrimaryBorrower().getLegalAddressInfo().getStateProvince() != null){
			Customer customer = getPrimaryBorrower();
			Address address = customer.getLegalAddressInfo();
				String state = address.getStateProvince().getName();
				if (state != null) {
					FacilityRange facilityRange = (FacilityRange) LookupService
							.getResult("FacilityRange.byState", "stateProvince",state);
					BigDecimal reqLoanAmtMin = facilityRange.getReqLoanAmtMin();
					BigDecimal reqMaxAmt = null;
					if (productType.getKey().equals(Constants.CREDIT_FACILITY_TYPE_LINE_OF_CREDIT_KEY)) {
						reqMaxAmt = facilityRange.getLocReqMaxAmt();
					} else if (productType.getKey()
							.equals(Constants.CREDIT_FACILITY_TYPE_LOAN_KEY)) {
						reqMaxAmt = facilityRange.getReqLoanAmtMax();
					}
	
					if (reqLoanAmtMin == null && reqMaxAmt != null) {
						if (compareAmounts(reqMaxAmt,getRequestedLoanAmt()) >= 0) {
							flag = true;
						}
					} else if (reqLoanAmtMin != null && reqMaxAmt == null) {
						if (compareAmounts(getRequestedLoanAmt(),
								reqLoanAmtMin) >= 0) {
							flag = true;
						}
					} else if (reqLoanAmtMin != null && reqMaxAmt != null) {
						if (compareAmounts(reqMaxAmt,
								getRequestedLoanAmt()) >= 0
								&& compareAmounts(getRequestedLoanAmt(),
										reqLoanAmtMin) >= 0) {
							flag = true;
						}
					}else {
						flag = true;
					}
				}
		}else {
			flag=true;
		}
		return flag;
	}

	public int compareAmounts(BigDecimal a, BigDecimal b) {
		return a.compareTo(b);
	}
	
	public boolean uniqueDebtor(){
		boolean match = true;
		List<String> refNums = new ArrayList<String>();
	    List<DebtorCustomer> debtors = getDebtors();
		 if(!debtors.isEmpty()){
			 for(DebtorCustomer debtor : debtors){
				 if (debtor.getFacilityCustomerRole().getCustomer() !=null) {
					 refNums.add(debtor.getFacilityCustomerRole().getCustomer().getRefNumber());
				 }
			 }	 
	   }
		Set<String> refNumSet = new HashSet<String>(refNums);
		if(refNumSet.size() < refNums.size()){
			match=false;			
		}
        return match;
		
	}
	
	public boolean matchExpirationDate(){
		if (getRequest() != null && getRequest().getWfStatus() != null && "REQUEST_STATUS_PORTFOLIO_MANAGMENT".equals(getRequest().getWfStatus().getStatusKey())) 
		{
			return true;
		}
		else if(getApprovalExpirationDate() != null && getApprovalExpirationDate().before(new java.util.Date())){
			return false;
		}
		return true;
	}
	 
	public boolean addUpdateDebtorsAllowed(){
		boolean match = false;
		User user = (User)ContextHolder.getContext().getUser();
		List<Team> teams = user.getTeams();
		for(Team team : teams){
			if("credit analyst team".equals(team.getName().toLowerCase()) || "credit decision team".equals(team.getName().toLowerCase())){
				match = true;
				break;
			}
		}
		return match;
	}
	
	public boolean updateApprovedAmtAllowed(){
		boolean match = true;
		Request request = (Request) ContextHolder.getContext().getNamedContext().get("root");
		if(!Constants.REQUEST_STATUS_PORTFOLIO_MANAGMENT_KEY.equals(request.getWfStatus().getStatusKey())){
			return match;
		}
		match = false;
		User user = (User)ContextHolder.getContext().getUser();
		List<Team> teams = user.getTeams();
		for(Team team : teams){
			if(Constants.REQUEST_STATUS_PORTFOLIO_MANAGMENT_KEY.equals(request.getWfStatus().getStatusKey()) && "credit decision team".equals(team.getName().toLowerCase())){
				match = true;
				break;
			}
		}
		return match;
	}
	
	public boolean updateFeeAllowed(){
		boolean match = true;
		Request request = (Request) ContextHolder.getContext().getNamedContext().get("root");
		if(!Constants.REQUEST_STATUS_PORTFOLIO_MANAGMENT_KEY.equals(request.getWfStatus().getStatusKey())){
			return match;
		}
		match = false;
		User user = (User)ContextHolder.getContext().getUser();
		List<Team> teams = user.getTeams();
		List<Fee> fees = getPricingOption().getStructure().getFees();
		for(Team team : teams){
			for(Fee fee : fees){
				if(Constants.REQUEST_STATUS_PORTFOLIO_MANAGMENT_KEY.equals(request.getWfStatus().getStatusKey()) && "credit decision team".equals(team.getName().toLowerCase()) && ("origination fee".equals(fee.getFeePolicy().getName().toLowerCase()) || "draw fee".equals(fee.getFeePolicy().getName().toLowerCase()))){
					match = true;
					break;
				}
			}
		}
		return match;
	}
	
	public void updateValues(){
		/*FacilityContract contract =  getFacilityContract();
		if(contract != null){
			contract.setContractStartDate(new Date());
		}
		getPricingOption().getStructure().setMaturityDate(com.thirdpillar.codify.loanpath.util.Utility.addYearsToDate(contract.getContractStartDate(),1));*/
	}
	
	public void updateMaturityDt(Date renewalApprDt){
		/*Date maturityDt = getPricingOption().getStructure().getMaturityDate();
		if(maturityDt.after(renewalApprDt) && !getRenewalApprDtUpdated()){
			setRenewalDate(Utility.addDaysToDate(maturityDt, 1));
			getPricingOption().getStructure().setMaturityDate(Utility.addYearsToDate(maturityDt, 1));
			setRenewalApprDtUpdated(true);
		}*/
		
	}

	public Date getMaturityDtTemp() {
		return maturityDtTemp;
	}

	public void setMaturityDtTemp(Date maturityDtTemp) {
		this.maturityDtTemp = maturityDtTemp;
	}
	
	public boolean getMaturityRenewalDate() {
		boolean match = true;
		if(this.getPricingOption() != null && this.getPricingOption().getStructure() != null && this.getPricingOption().getStructure().getMaturityDate() != null && this.getRenewalDate() !=null){
			if(this.getPricingOption().getStructure().getMaturityDate().before(this.getRenewalDate())){
				match = false;
			}
		}
		return match;
	}
	
	public boolean getMaturityContractStartDate() {
		boolean match = true;
		if(this.getPricingOption() != null && this.getPricingOption().getStructure() != null && this.getPricingOption().getStructure().getMaturityDate() != null && this.getFacilityContract() !=null && this.getFacilityContract().getContractStartDate() !=null){
			if(this.getPricingOption().getStructure().getMaturityDate().before(this.getFacilityContract().getContractStartDate())){
				match = false;
			}
		}
		return match;
	}
	
}
