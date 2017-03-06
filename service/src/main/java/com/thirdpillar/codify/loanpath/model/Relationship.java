/*
 * Copyright (c) 2005-2012 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.model;

import com.thirdpillar.codify.loanpath.util.AssociatedPartyComparator;
import com.thirdpillar.foundation.model.BaseDataObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
public class Relationship extends BaseDataObject {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = -1369999899800810521L;

    //~ Methods --------------------------------------------------------------------------------------------------------

    public List<RelationshipParty> getAllRelationshipPartiesDer() {
        return (List) getAllRelationshipParties();
    }

    public List<Customer> getCustomers() {
        List<Customer> associatedCustomers = new ArrayList<Customer>();

        if ((getAllRelationshipParties() != null) && (getAllRelationshipParties().size() > 0)) {

            for (RelationshipParty relParty : getAllRelationshipParties()) {

                if (relParty.getCustomer() != null) {
                    associatedCustomers.add(relParty.getCustomer());
                }
            }
        }

        return associatedCustomers;
    }

    public List<RelationshipParty> getRelationshipPartiesByRole(String role) {
        List<RelationshipParty> parties = new ArrayList<RelationshipParty>();

        for (RelationshipParty relationshipParty : getAllRelationshipParties()) {

            if ((relationshipParty != null) && (relationshipParty.getPartyRole() != null) &&
                    role.equalsIgnoreCase(relationshipParty.getPartyRole().getKey())) {
                parties.add(relationshipParty);
            }
        }

        return parties;
    }

    public List<Customer> getUniqueCustomers() {
        List<Customer> associatedCustomers = getCustomers();
        Map<Long, Customer> uniqueCustomerMap = new HashMap<Long, Customer>();

        for (Customer customer : associatedCustomers) {
            uniqueCustomerMap.put(customer.getId(), customer);
        }

        return new ArrayList<Customer>(uniqueCustomerMap.values());
    }
    
  
    /**
     * Return a sorted Relationship Parties list
     */
    
    public List<RelationshipParty> getSortedRelationshipParties() {
    	List<RelationshipParty> accountRelatedParties = new ArrayList<RelationshipParty>();
    	Map<Long, List<RelationshipParty>> relatedPartyMap = new HashMap<Long, List<RelationshipParty>>();
    	List<RelationshipParty> finalList = new ArrayList<RelationshipParty>();
    	if(this.getAllRelationshipParties() !=null){
    		
    		for(RelationshipParty relationshipParty : this.getAllRelationshipParties()) {
    			if (relationshipParty.getParentRelationshipParty() == null) {
    				if (relatedPartyMap.get(relationshipParty.getId()) == null) {
    					relatedPartyMap.put(relationshipParty.getId(), new ArrayList<RelationshipParty>());
    				}
    				accountRelatedParties.add(relationshipParty);
    			} else {
    				List<RelationshipParty> relatedParties = relatedPartyMap.get(relationshipParty.getParentRelationshipParty().getId());
    				if (relatedParties == null) {
    					relatedParties = new ArrayList<RelationshipParty>();
    					relatedPartyMap.put(relationshipParty.getParentRelationshipParty().getId(), relatedParties);
    				}
    				relatedParties.add(relationshipParty);
    				
    			}
    		}
    		
    		Collections.sort(accountRelatedParties, new AssociatedPartyComparator());
    			
    		for(RelationshipParty accountParty : accountRelatedParties) {
    			finalList.add(accountParty);
    			setupChildParties(finalList, accountParty, relatedPartyMap);
    		}
    		
    	}
    	
    	return finalList;
    
   }	
    
   
   private void setupChildParties(List<RelationshipParty> finalList, RelationshipParty forParty, Map<Long, List<RelationshipParty>> relatedPartyMap) {
	   if (relatedPartyMap.get(forParty.getId()) != null) {
			List<RelationshipParty> parties = relatedPartyMap.get(forParty.getId());
			Collections.sort(parties, new AssociatedPartyComparator());
			for(RelationshipParty party : parties) {
				if (finalList.contains(party)) {
					// to stop recursive, shouldn't happen
					continue;
				}
				finalList.add(party);
				setupChildParties(finalList, party, relatedPartyMap);
			}
		} 
   }
   
   public RelationshipParty getPrimaryBorrower() {
       List<RelationshipParty> parties = getRelationshipPartyByRole("PARTY_ROLE_TYPE_PRIMARY_BORROWER");

       if ((parties != null) && (!parties.isEmpty())) {

           if (parties.size() > 1) {
               // ideally should n't happen, max of only one primary borrower is allowed in a realtionshp,
               // log but ignore others
           }

           return parties.get(0);
       } else {
           return null;
       }
   }
   
   public List<RelationshipParty> getRelationshipPartyByRole(String role) {
       List<RelationshipParty> parties = new ArrayList<RelationshipParty>();

       if (this.getRelatedParties() != null) {

           for (RelationshipParty relationshipParty : this.getRelatedParties()) {

               if (relationshipParty.hasBorrowerRole(role) || relationshipParty.hasRelatedRole(role)) {
                   parties.add(relationshipParty);
               }
           }
       }

       return parties;
   }
    
}
