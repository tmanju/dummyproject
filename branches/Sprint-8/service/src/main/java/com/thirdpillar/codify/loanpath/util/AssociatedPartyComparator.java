
package com.thirdpillar.codify.loanpath.util;


import com.thirdpillar.codify.loanpath.model.Request;

import com.thirdpillar.codify.loanpath.model.RelationshipParty;



import com.thirdpillar.foundation.rules.RuleConstants;
import com.thirdpillar.foundation.rules.RuleService;
import com.thirdpillar.foundation.service.interceptors.BasicInterceptor;
import com.thirdpillar.foundation.service.interceptors.InterceptorContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.drools.runtime.StatefulKnowledgeSession;

import org.hibernate.event.EventSource;
import org.hibernate.event.PostInsertEvent;
import org.hibernate.event.PostInsertEventListener;
import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.hibernate.event.PreInsertEvent;
import org.hibernate.event.PreInsertEventListener;
import org.hibernate.event.PreUpdateEvent;
import org.hibernate.event.PreUpdateEventListener;

import org.hibernate.tuple.StandardProperty;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Comparator;
import java.io.Serializable;

/**
 * Util class to compare two parties based on their name
 *
 * @author   GAGANDEEP SINGH
 * @version  1.0
 * @since    1.0
 */

public class AssociatedPartyComparator implements Comparator, Serializable {
	
	public int compare(Object o1, Object o2) {
		
		RelationshipParty party1 = (RelationshipParty) o1;
		RelationshipParty party2 = (RelationshipParty) o2;
		
		 if ((party1 != null) && (party2 != null) &&
				 party1.getCustomer() != null && party2.getCustomer() != null && 
	                (party1.getCustomer().getCustomerName() != null) && (party2 != null) &&
	                (party2.getCustomer().getCustomerName() != null)) {
			 
			 int result = (party1.getCustomer().getCustomerName()).compareToIgnoreCase(party2.getCustomer().getCustomerName());
			 if((result < 0) || (result > 0)){
				 return result;
			 }else if(party1.getPartyRole() !=null && party2.getPartyRole() !=null) {
				 return party1.getPartyRole().getKey().compareToIgnoreCase(party2.getPartyRole().getKey());
			 }
		 }
		 
		 return 0;
		 
	}
	
}
