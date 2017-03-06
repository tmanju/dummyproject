package com.thirdpillar.codify.loanpath.service.facAllocatorTxn;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.thirdpillar.codify.loanpath.model.CustomerBackend;
import com.thirdpillar.codify.loanpath.model.FacAllocatorTxn;
import com.thirdpillar.codify.loanpath.model.WorkflowStatus;
import com.thirdpillar.codify.loanpath.util.Utility;
import com.thirdpillar.foundation.service.LookupService;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.EntityService;

@Component
@Configurable
public class FacAllocatorTransactionBizOp {
	
	private static final Log LOG = LogFactory.getLog(FacAllocatorTransactionBizOp.class);

	public void doCPTxn(BaseDataObject baseDataObject){
		LOG.info("****************Processing Capital Provider Transaction*************************");
		FacAllocatorTxn transaction = (FacAllocatorTxn) baseDataObject;
		EntityService es = new EntityService();
		try{
			/**
			 * update interest paid for CP
			 */
			CustomerBackend customerBackend = transaction.getFacAllocator().getCapitalProvider().getCustomerAssociated();
			
			if(transaction.getPaidInterest() != null){
				BigDecimal intPaidMTD = customerBackend.getInterestPaidMTD() != null ? customerBackend.getInterestPaidMTD() : BigDecimal.ZERO;
				BigDecimal intPaidYTD = customerBackend.getInterestPaidYTD() != null ? customerBackend.getInterestPaidYTD() : BigDecimal.ZERO;
				BigDecimal intPaidLTD = customerBackend.getInterestPaidLTD() != null ? customerBackend.getInterestPaidLTD() : BigDecimal.ZERO;
				
				intPaidMTD = Utility.add(intPaidMTD, transaction.getPaidInterest(), 3);
				intPaidYTD = Utility.add(intPaidYTD, transaction.getPaidInterest(), 3);
				intPaidLTD = Utility.add(intPaidLTD, transaction.getPaidInterest(), 3);
				
				customerBackend.setInterestPaidMTD(intPaidMTD);
				customerBackend.setInterestPaidYTD(intPaidYTD);
				customerBackend.setInterestPaidLTD(intPaidLTD);
			}
			
			if(transaction.getPaidServicingFee() != null){
				BigDecimal servicingFeePaidMTD = customerBackend.getServicingFeeCpMTD() != null ? customerBackend.getServicingFeeCpMTD() : BigDecimal.ZERO;
				BigDecimal servicingFeePaidYTD = customerBackend.getServicingFeeCpYTD() != null ? customerBackend.getServicingFeeCpYTD() : BigDecimal.ZERO;
				BigDecimal servicingFeePaidLTD = customerBackend.getServicingFeeCpLTD() != null ? customerBackend.getServicingFeeCpLTD() : BigDecimal.ZERO;
				
				servicingFeePaidMTD = Utility.add(servicingFeePaidMTD, transaction.getPaidServicingFee(), 3);
				servicingFeePaidYTD = Utility.add(servicingFeePaidYTD, transaction.getPaidServicingFee(), 3);
				servicingFeePaidLTD = Utility.add(servicingFeePaidLTD, transaction.getPaidServicingFee(), 3);
				
				customerBackend.setServicingFeeCpMTD(servicingFeePaidMTD);
				customerBackend.setServicingFeeCpYTD(servicingFeePaidYTD);
				customerBackend.setServicingFeeCpLTD(servicingFeePaidLTD);
			}
			
			if(transaction.getPaidUnusedFee() != null){
				BigDecimal unusedFeePaidMTD = customerBackend.getUnusedFeePaidMTD() != null ? customerBackend.getUnusedFeePaidMTD() : BigDecimal.ZERO;
				BigDecimal unusedFeePaidYTD = customerBackend.getUnusedFeePaidYTD() != null ? customerBackend.getUnusedFeePaidYTD() : BigDecimal.ZERO;
				BigDecimal unusedFeePaidLTD = customerBackend.getUnusedFeePaidLTD() != null ? customerBackend.getUnusedFeePaidLTD() : BigDecimal.ZERO;
				
				unusedFeePaidMTD = Utility.add(unusedFeePaidMTD, transaction.getPaidUnusedFee(), 3);
				unusedFeePaidYTD = Utility.add(unusedFeePaidYTD, transaction.getPaidUnusedFee(), 3);
				unusedFeePaidLTD = Utility.add(unusedFeePaidLTD, transaction.getPaidUnusedFee(), 3);
				
				customerBackend.setUnusedFeePaidMTD(unusedFeePaidMTD);
				customerBackend.setUnusedFeePaidYTD(unusedFeePaidYTD);
				customerBackend.setUnusedFeePaidLTD(unusedFeePaidLTD);
			}
						
			WorkflowStatus status=(WorkflowStatus)LookupService.getResult("WorkflowStatus.byStatusKey", "statusKey", "TXN_STATUS_SUBMITTED");
			transaction.setWfStatus(status);
			transaction.setTxnDate(new Date());
			transaction.setNetPaidToCp(transaction.getTotalPaidToCp());
			
			es.update(customerBackend);
			es.saveOrUpdate(transaction);
			es.flush();
		}catch(Exception e){
			LOG.fatal("****************Error Processing Capital Provider Transaction*************************",e);	
		}
	}
}
