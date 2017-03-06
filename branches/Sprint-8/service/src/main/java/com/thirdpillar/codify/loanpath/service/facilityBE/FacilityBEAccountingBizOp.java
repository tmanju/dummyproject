package com.thirdpillar.codify.loanpath.service.facilityBE;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thirdpillar.codify.loanpath.constants.Constants;
import com.thirdpillar.codify.loanpath.model.FacAllocator;
import com.thirdpillar.codify.loanpath.model.FacilityBE;
import com.thirdpillar.codify.loanpath.model.FacilityPayment;
import com.thirdpillar.codify.loanpath.model.WorkflowStatus;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.ContextHolder;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;
import com.thirdpillar.codify.loanpath.model.*;
import com.thirdpillar.codify.loanpath.util.Utility;

@Component
public class FacilityBEAccountingBizOp {
	
	EntityService entityService;

private static final Log LOG = LogFactory.getLog(FacilityBEAccountingBizOp.class);
	
	public void processInterimBalSettlement(BaseDataObject baseDataObject) {
		entityService = new EntityService();
		FacilityBE facilityBE = (FacilityBE) baseDataObject;
		LOG.info("****************Processing Facility Settlement Started*************************");
		if(facilityBE.getInterimBalance() != null){
			BigDecimal interimAmt = facilityBE.getInterimBalance();
			facilityBE.setInterimBalance(BigDecimal.ZERO);
			if(facilityBE.getSettlementAmt() == null){
				facilityBE.setSettlementAmt(BigDecimal.ZERO);
			}
			for(FacAllocator facAllocator : facilityBE.getFacAllocators()){
				LOG.info("****************Processing Facility Allocator Settlement for "+facAllocator+" *************************");
				if(facAllocator.getPercentOrAmt() != null && facAllocator.getPercentOrAmt().getKey().equals(Constants.AMOUNT_OR_PERCENT_PERCENT_KEY)){
					BigDecimal settlementAmt = interimAmt.multiply(facAllocator.getFundPer()).divide(new BigDecimal(100));
					facAllocator = facAllocator.calculateCpSettlementAmt(settlementAmt);
					facilityBE.calculateSettlementAmt(settlementAmt);
				}
			}
			try{
				entityService.saveOrUpdate(facilityBE);
			}catch(Exception e){
				LOG.fatal("****************Error Processing Facility Settlement*************************",e);
			}
		}
		LOG.info("****************Processing Facility Settlement Completed*************************");
	}
	
	public void processFacAllocatorSettlement(BaseDataObject baseDataObject) {
		entityService = new EntityService();
		FacAllocator facAllocator = (FacAllocator) baseDataObject;
		FacilityBE facilityBE = (FacilityBE) ContextHolder.getContext().getNamedContext().get("root");
		if(facAllocator.getCpBalance() != null && facAllocator.getCpSettlementAmt() != null){
			
			BigDecimal settlementAmt = facAllocator.getCpSettlementAmt();
			
			BigDecimal cpBal = Utility.subtract(facAllocator.getCpBalance(), settlementAmt, 3);
			
			facAllocator.setCpBalance(cpBal);
			
			BigDecimal facSettlementAmt = Utility.subtract(facilityBE.getSettlementAmt(), settlementAmt, 3);
			
			BigDecimal totalCPBalance = Utility.subtract(facilityBE.getTotalCpBalance(), settlementAmt, 3);
			
			facilityBE.setTotalCpBalance(totalCPBalance);
			
			facilityBE.setSettlementAmt(facSettlementAmt);
			
			facAllocator.setCpSettlementAmt(BigDecimal.ZERO);
			
			/*Maintain history of settlement*/
			FacAllocatorHistory facAllocatorHistory = new FacAllocatorHistory();
			facAllocatorHistory.setCpBalance(facAllocator.getCpBalance());
			facAllocatorHistory.setSettlementAmt(settlementAmt);
			facAllocatorHistory.setFacAllocator(facAllocator);
			
			try{
				entityService.saveOrUpdate(facilityBE);
				entityService.saveOrUpdate(facAllocatorHistory);
			}catch(Exception e){
				LOG.fatal("****************Error Processing Facility Settlement*************************",e);
			}
		}
	}
	
	public void updateFacilityTotalCPBal(BaseDataObject baseDataObject){
		entityService = new EntityService();
		FacAllocator facAllocator = (FacAllocator) baseDataObject;
		FacilityBE facility = (FacilityBE) ContextHolder.getContext().getNamedContext().get("root");
		
		if(facility.getTotalCpBalance() == null){
			facility.setTotalCpBalance(BigDecimal.ZERO);
		}

		if(facAllocator.getCpBalance() != null){
			facility.setTotalCpBalance(facility.getTotalCpBalance().add(facAllocator.getCpBalance()));
		}
		/*Maintain history of CP Balance*/
		FacAllocatorHistory facAllocatorHistory = new FacAllocatorHistory();
		facAllocatorHistory.setCpBalance(facAllocator.getCpBalance());
		facAllocatorHistory.setFacAllocator(facAllocator);
		try{
			entityService.saveOrUpdate(facAllocatorHistory);
			entityService.saveOrUpdate(facility);
		}catch(Exception e){
			LOG.fatal("****************Error Processing Facility Settlement*************************",e);
		}
	}
}
