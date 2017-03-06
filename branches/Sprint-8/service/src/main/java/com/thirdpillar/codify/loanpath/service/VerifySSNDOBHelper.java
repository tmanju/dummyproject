package com.thirdpillar.codify.loanpath.service;

import java.util.List;

import com.thirdpillar.codify.loanpath.model.Customer;
import com.thirdpillar.foundation.codify.exception.ValidationException;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.EntityService;

public class VerifySSNDOBHelper {
	
	public static boolean verifySSNDOB(Customer customer) {

		String[] queryParams = new String[] { "customerSSN"};
        
        // Creating query parameter values.
        String ssn=customer.getSsn();
        Object[] queryValues=new Object[]{ssn};
        
        //Get all customers from database with provided SSN number.
        EntityService entityService = new EntityService();
        List<Customer> allCustomers=entityService.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.Customer.bySsn",queryParams,queryValues);
		if(allCustomers!=null && !allCustomers.isEmpty()) {

			//iterating on existing customers fetched from database. 
			for (Customer existingCustomer : allCustomers) {
				
				if (customer.getId() != null && customer.getId().equals(existingCustomer.getId())) {
					// same customer skip
					continue;
				}
					
				// if SSN number is same then check whether they have same DOB.
			   if(!customer.getBirthDate().equals(existingCustomer.getBirthDate())) {
				   return false;
			   }
			}
		}

        return true;		
	}

}
