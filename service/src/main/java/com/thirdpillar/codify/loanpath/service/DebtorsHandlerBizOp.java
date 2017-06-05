package com.thirdpillar.codify.loanpath.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.thirdpillar.codify.loanpath.model.Customer;
import com.thirdpillar.codify.loanpath.model.DebtorCustomer;
import com.thirdpillar.codify.loanpath.model.Facility;
import com.thirdpillar.codify.loanpath.model.ServiceMessage;
import com.thirdpillar.foundation.component.CodifyMessage;
import com.thirdpillar.foundation.integration.IntegrationClientManager;
import com.thirdpillar.foundation.integration.IntegrationExchange;
import com.thirdpillar.foundation.integration.IntegrationResult;
import com.thirdpillar.foundation.integration.exception.IntegrationException;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.rules.MvelExecutor;
import com.thirdpillar.foundation.service.AbstractBusinessOperation;
import com.thirdpillar.foundation.service.ContextHolder;
import com.thirdpillar.foundation.service.EntityService;

@Component
public class DebtorsHandlerBizOp extends AbstractBusinessOperation{

	 private static final Log LOG = LogFactory.getLog(DebtorsHandlerBizOp.class);
	 
	 @Override public Object execute(BaseDataObject entity, String operation, Object... params) {
		 //ContextHolder.getContext().getNamedContext().put("debtors", params);
		 EntityService entityService = new EntityService();
		 List<DebtorCustomer> debtorCustomers = new ArrayList<>();
		 debtorCustomers.addAll(debtorCustomers);
		 Facility f = (Facility)entity;
		 //ContextHolder.getContext().getNamedContext().put("entity", entity);
		 for(Object object : params){
			 if(object instanceof Map){
				 Map<Object, Object> pars = (Map<Object, Object>) object;
				 List<Object> objs = (List<Object>) pars.get("selectedEntities");
				 for(Object o : objs){
					 debtorCustomers.add((DebtorCustomer)o);
				 }
			 }
		 }
		 
		 List<Object> outList = new LinkedList();
		 //outList.add(f);
		 outList.addAll(debtorCustomers);
		 for(DebtorCustomer d : debtorCustomers){
			 outList.add(d.getFacilityCustomerRole().getCustomer());
		 }
		 
		 IntegrationExchange exchange = new IntegrationExchange("sforceDbaService",debtorCustomers,outList, null);
         //exchange.getExchangeData().put(MvelExecutor.CONTEXT, ContextHolder.getContext());
         exchange.setExchangeDataByKey("contextEntity", entity);
         exchange.setExchangeDataByKey("entityOut",outList);
         exchange.setExchangeDataByKey("entityIn", debtorCustomers);
         exchange.setExchangeDataByKey("entityName", "debtor__c");
         exchange.setExchangeDataByKey("objectType", "debtor__c");
         exchange.setExchangeDataByKey("action","update");
         try{
        	 List<ServiceMessage> serviceMessages = new ArrayList<>();
        	 IntegrationClientManager.callService(exchange, Boolean.TRUE);
        	 IntegrationResult result = exchange.getIntegrationResult();
        		 for(Object o : outList){
        			 if(o instanceof Facility){
        				 Facility fac = (Facility)o;
        				 serviceMessages = fac.getServiceMessages();
        				 //entityService.update(fac);
        				 break;
        			 }
        		 }
        	 if(!serviceMessages.isEmpty()){
        		 StringBuffer buffer = new StringBuffer();
				for(ServiceMessage msg : serviceMessages){
					buffer.append(msg.getMessage());
					buffer.append("\n");
				}
					
        		 if(buffer.toString().length()>0){
						buffer = buffer.delete(buffer.length()-1, buffer.length());
						CodifyMessage.addMessage("ERR_EXEC_INT_SERVICE",CodifyMessage.Severity.ERROR, new String[]{"Akritiv service",buffer.toString()});
					}
        	 }else{
        		 for(Object o : outList){
        			 if(o instanceof DebtorCustomer){
        				 DebtorCustomer d = (DebtorCustomer)o;
        				 d.setServicingIdentifier(d.getServicingIdentifierLookup());
        				 entityService.update(d);
        			 }else if(o instanceof Customer){
        				 entityService.update((BaseDataObject)o);
        			 }
        		 }
        		 entityService.flush();
        	 }
         }catch(IntegrationException integrationException){
        	 integrationException.printStackTrace();
         }
		 return null;
	 }
}
