/*
 * Copyright (c) 2005-2012 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;

import com.thirdpillar.codify.loanpath.model.Customer;
import com.thirdpillar.codify.loanpath.model.CustomerExposureLimit;
import com.thirdpillar.codify.loanpath.model.Request;
import com.thirdpillar.foundation.service.AbstractBusinessOperation;
import com.thirdpillar.foundation.service.CodifyThreadExecutor;
import com.thirdpillar.foundation.service.CodifyThreadTemplate;
import com.thirdpillar.foundation.service.EntityService;


/**
 * Job which periodically checks Customer Exposure Limits for threshold breach.
 * If any exposure for customer is breached we trigger a human task in work flow.
 * 
 * @author gaurav.bagga
 *
 */
public class ExposureLimitExpiryMonitoringBizOp extends AbstractBusinessOperation {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    private static final Log LOG = LogFactory.getLog(ExposureLimitExpiryMonitoringBizOp.class);

    //~ Methods --------------------------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    public Object execute(String operation, Object... params) {

        // --------------------------------------------------------------------------------------
        LOG.info("Exposure Limit Monitoring Scheduler Operation is triggered");
                
        EntityService<Customer> entityService = new EntityService<Customer>();
        DateTime fiveDaysFromNow = new DateTime().plusDays(5);
        
        List<Customer> customerExposureList = entityService.findByNamedQueryAndNamedParam(
                "com.thirdpillar.codify.loanpath.model.Customer.byExposureAmountAboutToExpire", new String[]{"startdate","enddate"}, new Object[]{new Date(),fiveDaysFromNow.toDate()});
        
        LOG.info("Customers exposure list size found as " + customerExposureList.size());
        
        final EntityService entityService2 = entityService;
        for (Customer customer : customerExposureList) {
			final Long customerId = customer.getId();
            
            Thread t = new Thread(Thread.currentThread().getName() + "_Child_" + customerId) {
            	public void run() {
            		try {
	            		CodifyThreadTemplate template = new CodifyThreadTemplate();
	            		template.executeInTransactionAs(new CodifyThreadExecutor() {
							
							public Object doExecute() {
								Customer customer = (Customer) entityService2.get(Customer.class, customerId);
					            LOG.info("Raising event threshold breached on customer id -  " + customerId);
								entityService2.doOperation(customer, "ExposureLimitExpired");
								return null;
							}
						}, "SYSTEM" );
            		} catch (Exception t) {
			            LOG.error("ExposureAmtBreached operation failed for customer id - " + customerId,t);
            		}
            	}
            };
            
            try {
            	t.start();
            	t.join();
            } catch (InterruptedException ie) {
            	 LOG.error("Interrupted Exception ",ie);
            	// ignore shouldn't happen
            }
            
        }
        
        //Now iterate over filtered applications and send these application to workflow by generating events.
        return null;
    }
}
