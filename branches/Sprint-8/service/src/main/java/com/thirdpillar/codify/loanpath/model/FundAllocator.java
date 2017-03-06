package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.codify.loanpath.model.CapitalProvider;
import com.thirdpillar.codify.loanpath.util.Utility;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;

@Component @Configurable
public class FundAllocator extends BaseDataObject {

	public String getDbid(){
		return getId()+"";
	}

	public List<Object> getOldCPs() {
		EntityService entityService = new EntityService();
		 List<Object> capitalProviders = entityService.findByNamedQueryAndNamedParam(
	                "com.thirdpillar.codify.loanpath.model.FundAllocatorHistory.fetchAllByFundAllocatorId", "id", this.getId());
		return capitalProviders;
	}

	public BigDecimal percentage(BigDecimal base, BigDecimal pct) {
		return base.multiply(pct).divide(new BigDecimal(100),2,RoundingMode.DOWN);
	}

	public void changeStatus(BaseDataObject entity, String wfStatusKey){
		
		FundAllocator fundAloc = (FundAllocator) entity;
		WorkflowStatus status=(WorkflowStatus)LookupService.getResult("WorkflowStatus.byStatusKey", "statusKey", wfStatusKey);
		fundAloc.setWfStatus(status);

	}
	public void failedFundAllocation(BaseDataObject entity, Map<Long, BaseDataObject> map) {
		changeStatus(entity,	"FUNDING_PLACEMENT_STATUS_FAILED");
	}
	public void replacedFundAllocation(BaseDataObject entity, Map<Long, BaseDataObject> map) {
		changeStatus(entity,	"FUNDING_PLACEMENT_STATUS_REPLACED");
	}
	
	public void fundedFundAllocation(BaseDataObject entity, Map<Long, BaseDataObject> map) {
		changeStatus(entity,	"FUNDING_PLACEMENT_STATUS_FUNDED");
	}
	
	public BigDecimal getInxTempFundAmt() {
		EntityService entityService = new EntityService();
		List<FundAllocatorHistory> fundAllocatorsHistory = new ArrayList<FundAllocatorHistory>();
		
		if(this.getCapitalProvider()!=null && this.getId() !=null){
			
			fundAllocatorsHistory = entityService.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.FundAllocatorHistory.fetchAllByFundAllocatorInx",new String[]{"id"},
		             new Object[]{this.getId()});
		}
		
		BigDecimal tempFundAmt = BigDecimal.ZERO;
		if(fundAllocatorsHistory !=null){
			for(FundAllocatorHistory fundAllocatorHistory : fundAllocatorsHistory){
				if(fundAllocatorHistory.getCapitalProvider() !=null && fundAllocatorHistory.getNewCP() != null && fundAllocatorHistory.getWfStatus() != null && "FUNDING_PLACEMENT_STATUS_REPLACED".equals(fundAllocatorHistory.getWfStatus().getStatusKey()) && fundAllocatorHistory.getTemp() !=null && "FUNDING_PLACEMENT_TEMP_YES".equals(fundAllocatorHistory.getTemp().getKey())){
					tempFundAmt = (BigDecimal)fundAllocatorHistory.getFundAmt();
				}
			}
		}
		
		return tempFundAmt;
	}
	
	public String getInxTempCp() {
		EntityService entityService = new EntityService();
		List<FundAllocatorHistory> fundAllocatorsHistory = new ArrayList<FundAllocatorHistory>();
		
		if(this.getCapitalProvider()!=null && this.getId() !=null){
			
			fundAllocatorsHistory = entityService.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.FundAllocatorHistory.fetchAllByFundAllocatorInx",new String[]{"id"},
		             new Object[]{this.getId()});
		}
		
		String tempCp = "";
		if(fundAllocatorsHistory !=null){
			for(FundAllocatorHistory fundAllocatorHistory : fundAllocatorsHistory){
				if(fundAllocatorHistory.getCapitalProvider() !=null && fundAllocatorHistory.getNewCP() != null && fundAllocatorHistory.getWfStatus() != null && "FUNDING_PLACEMENT_STATUS_REPLACED".equals(fundAllocatorHistory.getWfStatus().getStatusKey()) && fundAllocatorHistory.getTemp() !=null && "FUNDING_PLACEMENT_TEMP_YES".equals(fundAllocatorHistory.getTemp().getKey())){
					tempCp = fundAllocatorHistory.getCapitalProvider().toString();
				}
			}
		}
		
		return tempCp;
	}
	
	public boolean getReplacedCheck() {
		boolean match=true;
		if(this.getDeal() !=null){
			java.math.BigDecimal fullPer = new java.math.BigDecimal("100");
			if(this.getDeal() !=null && this.getDeal().getSumAllocatedPer() !=null && this.getFundPer() !=null){
				if((this.getDeal().getSumAllocatedPer().compareTo(fullPer))>0 && (fullPer.subtract(this.getDeal().getSumAllocatedPer()).compareTo(this.getFundPer()) > 0)){
					match=false;
				} 
			}
		}
		return match;
	}
	
}
