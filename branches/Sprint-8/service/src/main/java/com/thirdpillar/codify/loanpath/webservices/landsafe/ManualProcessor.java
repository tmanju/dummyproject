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


public class ManualProcessor implements CustomTaskProcessor{
	
	
	
	private static final Log LOG = LogFactory.getLog(ManualProcessor.class);
	
	public void onError(IntegrationExchange integrationExchange) {
		LOG.debug("Some error has occured");
	}
	
	
	public void onSucess(IntegrationExchange integrationExchange) {
		UserDetails userdtl = (UserDetails) ApplicationContext.getContext().getContextUser();
		String userName = userdtl.getUsername();
		User user = (User)LookupService.getResult("User.byUsername", "username", userName);		
		integrationExchange.setExchangeDataByKey("user", user);
					
		
	}
		
		
		
		
		
}