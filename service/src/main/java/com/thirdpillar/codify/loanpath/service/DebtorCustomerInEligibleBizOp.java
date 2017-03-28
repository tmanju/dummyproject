package com.thirdpillar.codify.loanpath.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thirdpillar.codify.loanpath.model.DebtorCustomer;
import com.thirdpillar.codify.loanpath.model.Request;
import com.thirdpillar.codify.loanpath.model.WorkflowStatus;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;
import com.thirdpillar.foundation.service.AbstractBusinessOperation;

public class DebtorCustomerInEligibleBizOp  extends AbstractBusinessOperation {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    private static final Log LOG = LogFactory.getLog(DebtorCustomerInEligibleBizOp.class);

   
    //~ Methods --------------------------------------------------------------------------------------------------------

    @Override public Object execute(BaseDataObject entity, String operation, Object... params) {
    	List<DebtorCustomer> debtorCustomers;
    	EntityService es = new EntityService();
    	 if ((params != null) && (params.length > 0)) {
    		 debtorCustomers = (List<DebtorCustomer>) ((Map<String, Object>) params[0]).get("selectedEntities");
	    	 for(DebtorCustomer debtCust : debtorCustomers){
	    		 WorkflowStatus debtStatus=(WorkflowStatus)LookupService.getResult("WorkflowStatus.byStatusKey", "statusKey", "DEBTOR_STATUS_INELIGIBLE");
	    		 debtCust.setWfStatus(debtStatus);
	    		 es.saveOrUpdate(debtCust);
	    	 }
	    	 es.flush();
    	 }
		return null;
	 }
}
