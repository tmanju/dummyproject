package com.thirdpillar.codify.loanpath.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.thirdpillar.foundation.integration.IntegrationClientManager;
import com.thirdpillar.foundation.integration.IntegrationExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.thirdpillar.codify.loanpath.model.Facility;
import com.thirdpillar.codify.loanpath.model.PricingOption;
import com.thirdpillar.codify.loanpath.model.PricingPolicy;
import com.thirdpillar.foundation.fe.FormulaEngine;
import com.thirdpillar.foundation.fe.Valuatable;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.AbstractBusinessOperation;
import com.thirdpillar.foundation.service.EntityService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalculatePricingBizOp extends AbstractBusinessOperation {
	
	    //~ Static fields/initializers -------------------------------------------------------------------------------------
	
	   private static final Log LOG = LogFactory.getLog(CalculatePricingBizOp.class);
	
	    //~ Instance fields ------------------------------------------------------------------------------------------------
	
	    @Autowired private FormulaEngine fe;
	
	    //~ Methods --------------------------------------------------------------------------------------------------------
	
	    @Override public Object execute(BaseDataObject entity, String operation, Object... params) {
	        LOG.info("CalculatePricingBizOp.execute()...");
	
	        final Facility product = (Facility) entity;
	       List<PricingOption> additionalOptions;
	
	        if ((params != null) && (params.length > 0)) {
	
	            // from UI selections
	            LOG.info("selected: " + params.length);
	            additionalOptions = (List<PricingOption>) ((Map<String, Object>) params[0]).get("selectedEntities");
	        } else {
	
	            // from pipeline - process all additionalOption from the facility
	            additionalOptions = product.getAdditionalOptions();
	        }
	
	        LOG.info("additionalOptions: " + ((additionalOptions != null) ? additionalOptions.size() : null));
	
	        PricingPolicy p = null;
	
	        if (product.getPricingPolicy() != null) {
	            p = product.getPricingPolicy();
	        } else {
	
	            //Get default pricing policy in case nothing is associated to the product(facility)
	            EntityService es = new EntityService();
	            List<PricingPolicy> pps = es.findByNamedQueryAndNamedParam(
	                    "com.thirdpillar.codify.loanpath.model.PricingPolicy.byNameCaseInsensitive", "name",
	                    "CRE General Policy");
	
	            if ((pps != null) && (!pps.isEmpty())) {
	                p = pps.get(0);
	            }
	        }
	
	        final PricingPolicy policy = p;
	        LOG.info("Evaluating PricingPolicy: " + p.getName());
	
	        for (PricingOption o : additionalOptions) {
	            final PricingOption pricingOption = o;
	            LOG.info("rateType: " +
	                (((pricingOption.getStructure() != null) && (pricingOption.getStructure().getRateType() != null))
	                    ? pricingOption.getStructure().getRateType().getKey() : null));
	            LOG.info("index: " +
	                (((pricingOption.getStructure() != null) && (pricingOption.getStructure().getPricingIndex() != null))
	                    ? pricingOption.getStructure().getPricingIndex().getKey() : null));
	
	            Valuatable val = new Valuatable() {
	                    public String getFormula() {
	                        return policy.getFormula();
	                    }
	
	                    public String getValidationFormula() {
	                        return null;
	                    }
	
	                    public String getExceptionFormula() {
	                        return null;
	                    }
	                };
	
	            Map<String, Object> vars = new HashMap<String, Object>();
	            vars.put("pricingOption", pricingOption);
	            vars.put("facility", product);
	            fe.eval(val, vars);
	            // clear the output values before calling wintip
	            resetOutputData(pricingOption);
	            // call wintip integration. Need to move this from java to a bpmn
	            List<PricingOption> optionInput = new ArrayList<PricingOption>();
	            optionInput.add(pricingOption);
	
	            if(pricingOption.getId() != null){
		            IntegrationExchange exchange = new IntegrationExchange("pricingClient", optionInput, null,
		                    pricingOption.getId().toString());
		            exchange.setExchangeDataByKey("PRICING_MODE", "LENDING");
		            IntegrationClientManager.callService(exchange, false);
	            }
	        }
	
	        return null;
	    }
	    
	    private void resetOutputData(PricingOption option){
	    	// clear out the existing payment summary schedules and amortization schedules
	    	if(option != null && option.getStructure() != null){
	    		if(option.getStructure().getPaymentSummarySchedules() != null){
	    			option.getStructure().getPaymentSummarySchedules().clear();
	    		}
	    		if(option.getStructure().getAmortizationSchedules() != null){
	    			option.getStructure().getAmortizationSchedules().clear();
	   		}
	    }
	 }
}