package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.foundation.component.CodifyMessage;
import com.thirdpillar.foundation.integration.IntegrationExchange;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;
import com.thirdpillar.xstream.ext.lookup.XStreamLookupCollectionByOGNL;

@XStreamLookupCollectionByOGNL.List(
	    {
	        @XStreamLookupCollectionByOGNL(
	        	name = "byServicingIdentifier",
	        	keys = { "servicingIdentifier"}
	        )
	    }
	)

public class Deal {
	private static final Logger LOG = Logger.getLogger(Deal.class);
	
	public java.util.List<Deal> getSelectedDeal() {
		List<Deal> deals = new ArrayList<Deal>();
		deals.add(this);
		return deals;

	}

	public BigDecimal getSumAllocatedAmt() {
		BigDecimal total = BigDecimal.ZERO;
		List<FundAllocator> fundAllocators = this.getFundAllocators();
		for (FundAllocator fundAllocator : fundAllocators) {
			if (fundAllocator.getFundAmt() != null  && fundAllocator.getWfStatus() != null 
					&&  ( ! fundAllocator.getWfStatus().getStatusKey().equals(Constants.DEALS_STATUS_REJECTED_KEY))  ) {
				total = total.add(fundAllocator.getFundAmt());
			}
		}
		return total;
	}

	public BigDecimal getSumAllocatedPer() {
		BigDecimal total = BigDecimal.ZERO;
		List<FundAllocator> fundAllocators = this.getFundAllocators();
		for (FundAllocator fundAllocator : fundAllocators) {
			if (fundAllocator.getFundPer() != null  && fundAllocator.getWfStatus() != null 
					&&  ( ! fundAllocator.getWfStatus().getStatusKey().equals(Constants.DEALS_STATUS_REJECTED_KEY)) ){
				total = total.add(fundAllocator.getFundPer());
			}
		}
		return total;
	}
	public String getStateCountry(){
	StringBuffer stateCountry=new StringBuffer("");
		 if(getFacilityBE() != null && getFacilityBE().getCustomer() != null){
			 Address add= getFacilityBE().getCustomer().getLegalAddressInfo();
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
		BigDecimal iniFundingReq = getInitialFundingReq() == null? BigDecimal.ZERO: getInitialFundingReq().getAmount();
		if (iniFundingReq != null && getSumAllocatedAmt() !=null
				&& (iniFundingReq.compareTo(BigDecimal.ZERO) > 0) ) {
			return iniFundingReq.subtract(getSumAllocatedAmt());
		}
		return BigDecimal.ZERO;
	}
	
	public boolean allFundAssignmentStatusSame(){
		boolean same=false;
	  	if(this.getWfStatus() == null || this.getFundAllocators()== null || this.getFundAllocators().get(0).getWfStatus()==null){
	  		LOG.info("Found null "+this.getWfStatus()+" this.getWfStatus() "+ this.getFundAllocators() +"    "+this.getFundAllocators());
		  	return false;
		}
  		String firstStatusKey=this.getFundAllocators().get(0).getWfStatus().getStatusKey();
	  	for( FundAllocator fundAllocator : this.getFundAllocators()){
	  		if(fundAllocator.getWfStatus() != null &&   fundAllocator.getWfStatus().getStatusKey().equals(firstStatusKey)){
	  				  same=true;
	  			}else{
	  				return false;
	  				}
	  	}
	  	return same;
	}
	public String getFirstFundAssignmentStatusKey(){
	  	if(this.getWfStatus() == null || this.getFundAllocators()== null || this.getFundAllocators().get(0).getWfStatus()==null){
	  		LOG.info("getFirstFundAssignmentStatusKey()  Found null this.getWfStatus()"+ this.getWfStatus() +" this.getFundAllocators()" +"    "+this.getFundAllocators());
		  	return "FUNDING_PLACEMENT_STATUS_NEW";
		}
	  	return this.getFundAllocators().get(0).getWfStatus().getStatusKey();
	}
	
	
	public List<CapitalProvider> getInxCapitalProvider() {
		EntityService entityService = new EntityService();
		List<CapitalProvider> capitalProviders = new ArrayList<CapitalProvider>();

		boolean isInxCpVal = true;
		if(getFacilityBE() != null && this.getFacilityBE().getCustomer()!=null && this.getFacilityBE().getCustomer().getLegalName()!=null && this.getProductType()!=null ){
			capitalProviders = entityService.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.CapitalProvider.fetchAllInxCapitalProvider",new String[]{"isInxCp_key"},
	             new Object[]{isInxCpVal});
		}	
		return capitalProviders;
	}
	
	public List<CapitalProvider> getFacilityCapitalProviders(){
		List<CapitalProvider> finalCPs = new ArrayList<CapitalProvider>();
		if(this.getFacilityBE() !=null && this.getFacilityBE().getWfStatus() != null &&   this.getFacilityBE().getWfStatus().getStatusKey().equals(Constants.DEALS_STATUS_ACCEPTED_KEY)){
			List<String> cp = new ArrayList<String>();
			if(this.getFacilityBE().getFacAllocators()!=null && this.getFacilityBE().getFacAllocators().size()>0){
				for(FacAllocator facObj : this.getFacilityBE().getFacAllocators()){
					if(!cp.contains(facObj.getCapitalProvider().getCustomerAssociated().getLegalName())){
						finalCPs.add(facObj.getCapitalProvider());
						cp.add(facObj.getCapitalProvider().getCustomerAssociated().getLegalName());
					}
				}
			}
		}
		
		List<FundAllocator> fundAllocs = this.getFundAllocators();
		if(fundAllocs !=null){
			List<CapitalProvider> capproviders = new ArrayList<CapitalProvider>();
			List<FundAllocatorHistory> fundAllocatorHistoryRec = new ArrayList<FundAllocatorHistory>();
			for(FundAllocator fundAloc : fundAllocs){
				if(fundAloc.getId() !=null && fundAloc.getCapitalProvider() !=null && fundAloc.getCapitalProvider().getCustomerAssociated() != null){
					EntityService entityService = new EntityService();
					CapitalProvider cp = (CapitalProvider)fundAloc.getCapitalProvider();
					if(cp !=null){
						Long cpid = (Long) cp.getId();
						fundAllocatorHistoryRec = entityService.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.FundAllocatorHistory.fetchAllByFundAllocatorReplaced",new String[]{"id","newCP"},
					             new Object[]{fundAloc.getId(),cpid});
						if(fundAllocatorHistoryRec !=null && fundAllocatorHistoryRec.isEmpty()){
							for(FundAllocatorHistory fundAllocHist : fundAllocatorHistoryRec){
								if(fundAllocHist.getCapitalProvider() !=null && fundAllocHist.getNewCP() !=null && "FUNDING_PLACEMENT_STATUS_REPLACED".equals(fundAllocHist.getWfStatus().getStatusKey())){
									capproviders.add((CapitalProvider)fundAllocHist.getCapitalProvider());
								}
								finalCPs.removeAll(capproviders);
							}
						}
					}
					capproviders.add((CapitalProvider)fundAloc.getCapitalProvider());
				}

			}

			finalCPs.removeAll(capproviders);
			
			//to remove duplicate INX CPs on Funding Request
			if(this.getFacilityBE().getFacAllocators()!=null && this.getFacilityBE().getFacAllocators().size()>0){
				for(FacAllocator facObj : this.getFacilityBE().getFacAllocators()){
					if(facObj.getCapitalProvider() !=null && facObj.getCapitalProvider().getCustomerAssociated() != null && facObj.getCapitalProvider() !=null ){
						if(getInxCapitalProvider()!=null){
							for(CapitalProvider cap :getInxCapitalProvider()){
								CapitalProvider cp = (CapitalProvider)facObj.getCapitalProvider();
								if(cp.equals(cap)){
									finalCPs.remove(cap);
								}
							}
						}
					}
				}
			}
		}
		
		if(getInxCapitalProvider()!=null){
			List<CapitalProvider> inxCps = getInxCapitalProvider();
			finalCPs.addAll(inxCps);
		}
				
		return finalCPs;
	}
	
	public List<CapitalProvider> getFacilityNewCapitalProviders(){
		List<CapitalProvider> finalCPs = new ArrayList<CapitalProvider>();
	
		if(this.getFacilityBE() !=null && this.getFacilityBE().getWfStatus() != null &&   this.getFacilityBE().getWfStatus().getStatusKey().equals(Constants.DEALS_STATUS_ACCEPTED_KEY)){
			List<String> cp = new ArrayList<String>();
			if(this.getFacilityBE().getFacAllocators()!=null && this.getFacilityBE().getFacAllocators().size()>0){
				for(FacAllocator facObj : this.getFacilityBE().getFacAllocators()){
					if(!cp.contains(facObj.getCapitalProvider().getCustomerAssociated().getLegalName())){
						finalCPs.add(facObj.getCapitalProvider());
						cp.add(facObj.getCapitalProvider().getCustomerAssociated().getLegalName());
					}
				}
			}
		}
		

		
		List<FundAllocator> fundAllocs = this.getFundAllocators();
		if(fundAllocs !=null){
			List<CapitalProvider> capproviders = new ArrayList<CapitalProvider>();
			for(FundAllocator fundAloc : fundAllocs){
				if(fundAloc.getId() !=null && fundAloc.getCapitalProvider() !=null && fundAloc.getCapitalProvider().getCustomerAssociated() != null){
					capproviders.add((CapitalProvider)fundAloc.getCapitalProvider());
				}
			}

			finalCPs.removeAll(capproviders);
			
			//to remove duplicate INX CPs in replaced dropdown list.
			if(this.getFacilityBE().getFacAllocators()!=null && this.getFacilityBE().getFacAllocators().size()>0){
				for(FacAllocator facObj : this.getFacilityBE().getFacAllocators()){
					if(facObj.getCapitalProvider() !=null && facObj.getCapitalProvider().getCustomerAssociated() != null && facObj.getCapitalProvider() !=null ){
						if(getInxCapitalProvider()!=null){
							for(CapitalProvider cap :getInxCapitalProvider()){
								CapitalProvider cp = (CapitalProvider)facObj.getCapitalProvider();
								if(cp.equals(cap)){
									finalCPs.remove(cap);
								}
							}
						}
					}
				}
			}
		}
		
		if(getInxCapitalProvider()!=null){
			List<CapitalProvider> inxCps = getInxCapitalProvider();
			finalCPs.addAll(inxCps);
		}
				
		return finalCPs;
	}
	
	public List<CapitalProvider> getCapitalProviders(){
		List<CapitalProvider> finalCPs = new ArrayList<CapitalProvider>();
		if(this.getFacilityBE() !=null && this.getFacilityBE().getWfStatus() != null &&   this.getFacilityBE().getWfStatus().getStatusKey().equals(Constants.DEALS_STATUS_ACCEPTED_KEY)){
			List<String> cp = new ArrayList<String>();
			if(this.getFacilityBE().getFacAllocators()!=null && this.getFacilityBE().getFacAllocators().size()>0){
				for(FacAllocator facObj : this.getFacilityBE().getFacAllocators()){
					if(!cp.contains(facObj.getCapitalProvider().getCustomerAssociated().getLegalName())){
						finalCPs.add(facObj.getCapitalProvider());
						cp.add(facObj.getCapitalProvider().getCustomerAssociated().getLegalName());
					}
				}
			}
		}
		
		if(getInxCapitalProvider()!=null){
			List<CapitalProvider> inxCps = getInxCapitalProvider();
			finalCPs.addAll(inxCps);
		}
		
		return finalCPs;
	}
	
	public int getNoOfCps(){
		List<Deal> deals = getSelectedDeal();
		List<String> cp = new ArrayList<String>();
		int count = 0;
		for(Deal deal : deals){
			Set<FundAllocator> fundAllocatorsSet = new HashSet<FundAllocator>(deal.getFundAllocators());
			for(FundAllocator fundAlloc : fundAllocatorsSet){
				if(!cp.contains(fundAlloc.getCapitalProvider().getCustomerAssociated().getLegalName())){
					cp.add(fundAlloc.getCapitalProvider().getCustomerAssociated().getLegalName());
					count++;
				}
			}
		}
		return count;
	}
	
	public List<Object> getFundAllocatorHistory() {
		EntityService entityService = new EntityService();
		 List<Object> capitalProviders = entityService.findByNamedQueryAndNamedParam(
	                "com.thirdpillar.codify.loanpath.model.FundAllocatorHistory.fetchAllFundAllocatorHistory", "id", this.getId());
		return capitalProviders;
	}
	
	
	public void changeDealStatus(BaseDataObject entity, String wfStatusKey){
		Deal deal = (Deal)entity;
		WorkflowStatus status=(WorkflowStatus)LookupService.getResult("WorkflowStatus.byStatusKey", "statusKey", wfStatusKey);
		deal.setWfStatus(status);
	}
	
    public void mergeAkritivResponse(Deal deal, java.lang.String rStatus, IntegrationExchange integrationExchangeObj){
    	Object object = integrationExchangeObj.getTaskOutput();
    	EntityService es = new EntityService();
		if ("ERROR".equals(rStatus)) {
			LOG.error("******* Call Failed");
			 CodifyMessage.addMessage("ERR_INT_HTTP_RESP_ERROR",CodifyMessage.Severity.ERROR, new String[]{"Akritv Service"});
			 CodifyMessage.addMessage("ERR_INT_HTTP_REQ_FAIL",CodifyMessage.Severity.ERROR, new String[]{"Akritv Service"});
		 }else{
			if(object instanceof Deal){
				Deal responseObj = (Deal)object;
				deal.setServicingIdentifier(responseObj.getServicingIdentifier());
				if(deal.getFundAllocators() != null && responseObj.getFundAllocators() !=null){
					for(FundAllocator fundAlloc : responseObj.getFundAllocators()){
						for(FundAllocator dealFundAlloc : deal.getFundAllocators()){
							if(dealFundAlloc.getId() == fundAlloc.getId()){
								dealFundAlloc.setServicingIdentifier(fundAlloc.getServicingIdentifier());
							}
						}
					}
				}
				
				StringBuffer buffer = new StringBuffer();
				if(deal.getServiceMessages() != null){
					deal.getServiceMessages().clear();
				}
				if(deal.getServiceMessages() != null){
					deal.getServiceMessages().addAll(responseObj.getServiceMessages());
					for(ServiceMessage msg : responseObj.getServiceMessages()){
						buffer.append(msg.getMessage());
						buffer.append("\n");
					}
				}
				if(buffer.toString().length()>0){
					buffer = buffer.delete(buffer.length()-1, buffer.length());
					CodifyMessage.addMessage("ERR_EXEC_INT_SERVICE",CodifyMessage.Severity.ERROR, new String[]{"Akritiv service",buffer.toString()});
				}			
	 
			 	 if(deal.getServiceMessages() != null && deal.getServiceMessages().isEmpty()){
			 		deal.setSubmitFundingRequestFlag(false);
			 	 }

				LOG.info("**************Akritiv response saved Successfully************************");
				es.update(deal);
		 	}
		}
	}
    
    public Date getDrawRequestedDt() {
		return getMetaInfo().getCreatedAt();
	}
    
}
