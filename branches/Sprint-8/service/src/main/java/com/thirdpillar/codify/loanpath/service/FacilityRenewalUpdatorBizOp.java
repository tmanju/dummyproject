package com.thirdpillar.codify.loanpath.service;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thirdpillar.codify.loanpath.model.Facility;
import com.thirdpillar.foundation.integration.IntegrationClientManager;
import com.thirdpillar.foundation.integration.IntegrationExchange;
import com.thirdpillar.foundation.integration.exception.IntegrationException;
import com.thirdpillar.foundation.service.AbstractBusinessOperation;
import com.thirdpillar.foundation.service.EntityService;

public class FacilityRenewalUpdatorBizOp extends AbstractBusinessOperation{
	
	private static final Log LOG = LogFactory.getLog(FacilityRenewalUpdatorBizOp.class);

	 public Object execute(String operation, Object... params) {
		 LOG.info("**************************Starting business operation FacilityRenewalUpdatorBizOp");
		 EntityService es = new EntityService();
		 List<Facility> facilities = (List<Facility>)es.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.Facility.byRenewalApprDtUpdated", "renewalApprDtUpdated", true);
		 List<Facility> integrationFacilities = new ArrayList<Facility>();
		 for(Facility facility : facilities){
			 if(facility.getServicingIdentifier() != null){
				 LOG.info("**************************Sending Facility "+facility.getRefNumber());
				 Map<String,Object> inputMap = new HashMap<String,Object>();
			        inputMap.put("entity",facility);
			        inputMap.put("entityOut",facility);
			        inputMap.put("entityName","clientaccount");
			        inputMap.put("clientId","akritiv-internex");
			        inputMap.put("action","update");
			        
			        IntegrationExchange exchange = new IntegrationExchange("akritivService", facility, facility, facility.getRefNumber());
			         
			         exchange.getExchangeData().put("inputMap", inputMap);
			         exchange.getExchangeData().putAll(inputMap);
			         try{
			        	 IntegrationClientManager.callService(exchange, Boolean.TRUE);
			        	 if(exchange.getIntegrationResult() != null && "ERROR".equals(exchange.getIntegrationResult().getReturnStatus())){
			        		 System.out.println("error in updating facility "+facility.getRefNumber());
			        	 }else{
			        		 facility.setRenewalApprDtUpdated(false);
			        	 }
			        	 es.update(facility);
			        	 es.flush();
			         }
			 catch (IntegrationException e) {
				 LOG.error("**************************Starting business operation FacilityRenewalUpdatorBizOp");
				 e.printStackTrace();
			}
			 }
		 }
		 LOG.info("**************************Ending Execution business operation FacilityRenewalUpdatorBizOp");
		 
	 
		return null;
	 }
}
