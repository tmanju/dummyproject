package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Transient;

import org.apache.log4j.Logger;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.codify.loanpath.model.CapitalProvider;
import com.thirdpillar.codify.loanpath.util.Utility;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.model.Money;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;

@XStreamLookupCollectionByOGNL.List(
	    {
	        @XStreamLookupCollectionByOGNL(
	        	name = "byServicingIdentifier",
	        	keys = { "servicingIdentifier"}
	        )
	    }
	)

public class FacilityBE {
	
	private static final Logger logger = Logger.getLogger(FacilityBE.class);
	
	@Transient
	private int scale = 3;
	@Transient
	MathContext context = new MathContext(5, RoundingMode.HALF_EVEN);
	
	public java.util.List<FacilityBE> getSelectedFacilityBE() {
		List<FacilityBE> deals = new ArrayList<FacilityBE>();
		deals.add(this);
		return deals;

	}
	public List<CapitalProvider> getCapitalProviders() {
		EntityService entityService = new EntityService();
		 List<CapitalProvider> capitalProviders = entityService.findByNamedQueryAndNamedParam(
	                "com.thirdpillar.codify.loanpath.model.CapitalProvider.fetchAllCapitalProvider", "ids", existingCPs());
		 List<CapitalProvider> capableCPs =new ArrayList<CapitalProvider>();//capable in term of ava balance
		 if(this.getCreditFacAmt()==null){
			 return capableCPs;
		 }
		 if(capitalProviders != null){
			 for (CapitalProvider cp : capitalProviders) {
				 if(this.getCreditFacAmt().getAmount() !=null && cp.getCapacityToFund().compareTo(this.getCreditFacAmt().getAmount())>=0){
					 capableCPs.add(cp);
				 }
			}
		 }
		return capableCPs;
	}

	public BigDecimal getSumAllocatedAmt() {
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal fundAmt = BigDecimal.ZERO;
		List<FacAllocator> facAllocators = this.getFacAllocators();
		for (FacAllocator facAllocator : facAllocators) {
			if (facAllocator.getFundAmt() != null  && facAllocator.getWfStatus() != null 
					&&  ( ! facAllocator.getWfStatus().getStatusKey().equals(Constants.DEALS_STATUS_REJECTED_KEY))  ) {
				fundAmt = facAllocator.getFundAmt();
				fundAmt = fundAmt.setScale(scale,context.getRoundingMode());
				total = Utility.add(total,fundAmt,scale);
				//grossAR = facAllocator.getFacilityBE().getGrossAR();
				 //grossAR = grossAR.setScale(scale,context.getRoundingMode());
				 
				//total = Utility.add(total,facAllocator.getFundAmt(),);
			}
		}
		return total;
	}

	public BigDecimal getSumAllocatedPer() {
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal fundPer = BigDecimal.ZERO;
		List<FacAllocator> facAllocators = this.getFacAllocators();
		for (FacAllocator facAllocator : facAllocators) {
			if (facAllocator.getFundPer() != null  && facAllocator.getWfStatus() != null 
					&&  ( ! facAllocator.getWfStatus().getStatusKey().equals(Constants.DEALS_STATUS_REJECTED_KEY)) ){
				
				//total = total.add(facAllocator.getFundPer());
				fundPer = facAllocator.getFundPer();
				fundPer = fundPer.setScale(scale,context.getRoundingMode());
				total = Utility.add(total,fundPer,scale);
			}
		}
		return total;
	}
	public String getStateCountry(){
	StringBuffer stateCountry=new StringBuffer("");
		 if(getCustomer() != null){
			 Address add= getCustomer().getLegalAddressInfo();
			 if(add != null && add.getStateProvince() != null){
				 stateCountry.append(add.getStateProvince().getName());
				 if(add.getCountry() !=null ){
					 stateCountry.append(",");
					 stateCountry.append(add.getCountry().getName());
				 }
			 }
		 }
		return stateCountry.toString();
	}
	public BigDecimal getAvailableCashDer(){
		BigDecimal creditFacAmt = getCreditFacAmt() == null? BigDecimal.ZERO: getCreditFacAmt().getAmount();
		if (creditFacAmt != null && getSumAllocatedAmt() !=null
				&& (creditFacAmt.compareTo(BigDecimal.ZERO) > 0) ) {
			return creditFacAmt.subtract(getSumAllocatedAmt());
		}
		return BigDecimal.ZERO;
	}

	public Map<String, BigDecimal> getAllocatedPerGroupByStatus(){
		Map<String, BigDecimal> count = new HashMap<String, BigDecimal>();
		BigDecimal assigned=BigDecimal.ZERO,a=BigDecimal.ZERO,r=BigDecimal.ZERO;
		BigDecimal fundPer = BigDecimal.ZERO;
		for( FacAllocator facAllocator : this.getFacAllocators()){
	  		if(facAllocator.getWfStatus() != null && facAllocator.getFundPer() != null ){
				if(Constants.DEALS_STATUS_ASSIGNED_KEY.equals(facAllocator.getWfStatus().getStatusKey())){
					//assigned=assigned.add(facAllocator.getFundPer());
					
					fundPer = facAllocator.getFundPer();
					fundPer = fundPer.setScale(scale,context.getRoundingMode());
					assigned = Utility.add(assigned,fundPer,scale);
					
	  			 }
	  			if(Constants.DEALS_STATUS_ACCEPTED_KEY.equals(facAllocator.getWfStatus().getStatusKey())){
					//a=a.add(facAllocator.getFundPer());
					fundPer = facAllocator.getFundPer();
					fundPer = fundPer.setScale(scale,context.getRoundingMode());
					a = Utility.add(a,fundPer,scale);
	  			 }
	  			if(Constants.DEALS_STATUS_REJECTED_KEY.equals(facAllocator.getWfStatus().getStatusKey())){
					//r= r.add(facAllocator.getFundPer());
	  				fundPer = facAllocator.getFundPer();
					fundPer = fundPer.setScale(scale,context.getRoundingMode());
					r = Utility.add(r,fundPer,scale);
	  			 }
	  		}
	  	}
		count.put(Constants.DEALS_STATUS_ASSIGNED_KEY,assigned);
		count.put(Constants.DEALS_STATUS_ACCEPTED_KEY, a);
		count.put(Constants.DEALS_STATUS_REJECTED_KEY, r);
		logger.info("******getAllocatedPerGroupByStatus()=== UNASSIGNED_KEY "+assigned +"_ACCEPTED_KEY, "+ a+" REJECTED"+ r);
		return count;
	}
	public String getFacilityStatus(){
		final BigDecimal hundred= new BigDecimal(100);
		Map<String, BigDecimal> count =getAllocatedPerGroupByStatus();
		if(count== null){
			logger.info("nullllllllllllllllllllllllllllllllllllllllll");
			return Constants.DEALS_STATUS_UNASSIGNED_KEY;
		}
		if(hundred.compareTo(count.get(Constants.DEALS_STATUS_ASSIGNED_KEY))  == 0){
			return 	Constants.DEALS_STATUS_ASSIGNED_KEY;
		}
		if(hundred.compareTo(count.get(Constants.DEALS_STATUS_ACCEPTED_KEY))  == 0){
			return 	Constants.DEALS_STATUS_ACCEPTED_KEY;
		}
		if(hundred.compareTo(count.get(Constants.DEALS_STATUS_REJECTED_KEY))  == 0){
			return 	Constants.DEALS_STATUS_REJECTED_KEY;
		}
		return Constants.DEALS_STATUS_ASSIGNED_KEY;
	}
	
	
	public Object[] existingCPs(){
		List<Long> existingCPs =new ArrayList<Long>();
		existingCPs.add(new Long(-1));// will have always value , 
		List<FacAllocator> facAllocators = getFacAllocators();
		if(facAllocators != null  ){
			for(FacAllocator facAllocator : facAllocators){
				if( facAllocator.getWfStatus() != null && "DEALS_STATUS_ACCEPTED".equals(facAllocator.getWfStatus().getStatusKey())){
					existingCPs.add(facAllocator.getCapitalProvider().getId());
				}
			}
		}
		    return existingCPs.toArray();		
	}
	
	public boolean hasDuplicateCP() {
		boolean duplicate = false;
		List<FacAllocator> facAllocators = this.getFacAllocators();
		if(facAllocators== null){
			return false;
		}
		Set<Long> cps=new TreeSet<Long>();
		
		for (FacAllocator facAllocator : facAllocators) {
			if (facAllocator.getCapitalProvider() != null 
					&& (facAllocator.getWfStatus() != null  && 
						 ! "DEALS_STATUS_REJECTED".equals(facAllocator.getWfStatus().getStatusKey()))){
				duplicate= cps.add(facAllocator.getCapitalProvider().getId());
				if(!duplicate){
					return true;
				}
			}
		}
		return false;
	}
	
	public BigDecimal getinterimBal() {
		BigDecimal total = BigDecimal.ZERO;
		
		return total;
	}
	
	public FacilityBE calculateFacBalance(BigDecimal amt){
		if(getFacBalance() == null){
			setFacBalance(BigDecimal.ZERO);
		}
		setFacBalance(getFacBalance().add(amt));
		return this;
	}

	public FacilityBE calculateInterimBalance(BigDecimal amt){
		if(getInterimBalance() == null){
			setInterimBalance(BigDecimal.ZERO);
		}
		setInterimBalance(getInterimBalance().add(amt));
		return this;
	}
	
	public FacilityBE calculateSettlementAmt(BigDecimal amt){
		if(getSettlementAmt() == null){
			setSettlementAmt(BigDecimal.ZERO);
		}
		setSettlementAmt(getSettlementAmt().add(amt));
		return this;
	}
	
	public BigDecimal getUnusedAmtDer(){
		BigDecimal facility = getCreditFacAmt().getAmount();
		BigDecimal facBalance = getFacBalance();
		if(facBalance != null){
			return facility.subtract(facBalance);
		}
		return facility;
	}
	public String getMinUtilizationAmtDer(){
		String minUtilization="0";
		BigDecimal minUtilAmt=this.getMinUtilizationAmt()== null? BigDecimal.ZERO:this.getMinUtilizationAmt() ;
		minUtilization =String.format("%,.2f", minUtilAmt.setScale(2, RoundingMode.DOWN));
		minUtilization = minUtilization.substring(0, minUtilization.length()-3);
		Money creditFacAmt=this.getCreditFacAmt();
		if(creditFacAmt!= null){
			minUtilization= minUtilization +" "+ creditFacAmt.getCurrency();
		}
		return minUtilization ;
	}
}
