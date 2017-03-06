package com.thirdpillar.codify.loanpath.webservices.landsafe;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.thirdpillar.foundation.integration.IntegrationExchange;
import com.thirdpillar.foundation.integration.custom.CustomTaskProcessor;
import com.thirdpillar.foundation.model.DocumentAware;
import com.thirdpillar.foundation.ApplicationContext;
import com.thirdpillar.codify.loanpath.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import com.thirdpillar.foundation.service.LookupService;


public class AutoProcessor implements CustomTaskProcessor{
	
	
	
	private static final Log LOG = LogFactory.getLog(AutoProcessor.class);
	
	public void onError(IntegrationExchange integrationExchange) {
		LOG.debug("Some error has occured");
	}
	
	
	public void onSucess(IntegrationExchange integrationExchange) {
		
		User user = (User)LookupService.getResult("User.byUsername", "username", "SYSTEM");		
		integrationExchange.setExchangeDataByKey("user", user);
					
		
	}
		
		
		
		
		
}