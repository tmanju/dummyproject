/*
 * Copyright (c) 2005-2012 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thirdpillar.codify.loanpath.model.Request;
import com.thirdpillar.foundation.service.AbstractBusinessOperation;
import com.thirdpillar.foundation.service.CodifyThreadExecutor;
import com.thirdpillar.foundation.service.CodifyThreadTemplate;
import com.thirdpillar.foundation.service.ContextHolder;
import com.thirdpillar.foundation.service.EntityService;


/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
public class NoiaSchedulerBizOp extends AbstractBusinessOperation {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    private static final Log LOG = LogFactory.getLog(NoiaSchedulerBizOp.class);

    //~ Methods --------------------------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    public Object execute(String operation, Object... params) {

        // --------------------------------------------------------------------------------------
        LOG.info("NOIA Scheduler Operation is triggered");

        EntityService<Request> entityService = new EntityService<Request>();

        // Fetching all the requests/Applications that comes under given
        // statuses.
        String[] statusEligibleForNoia = new String[] {
                "REQUEST_STATUS_PENDING_SALES", "REQUEST_STATUS_PENDING_SUBMITTED_TO_CREDIT",
                "REQUEST_STATUS_PENDING_ANALYSIS", "REQUEST_STATUS_PENDING_DECISION",
                "REQUEST_STATUS_APPROVED_MODIFICATION", "REQUEST_STATUS_PENDING_RETURN_TO_SALES"
            };
        String[] queryParams = new String[] { "noiaStatuses", "regbDttm" };
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -20);

        Date regbDate = calendar.getTime();
        Object[] queryParamValues = new Object[] { statusEligibleForNoia, regbDate };
        List<Request> applicationList = entityService.findByNamedQueryAndNamedParam(
                "com.thirdpillar.codify.loanpath.model.Request.forNOIAProcess", queryParams, queryParamValues);
        LOG.info("Found # of requests eligible for NOIA Process - " +
            ((applicationList == null) ? 0 : applicationList.size()));

        //Now iterate over filtered applications and send these application to workflow by generating events.
        LOG.debug("Processing NOIA # of requests - " + applicationList.size());

        final EntityService entityService2 = entityService;
        for (Request request : applicationList) {
			final Long requestId = request.getId();
            final String refNumber = request.getRefNumber();
            Thread t = new Thread(Thread.currentThread().getName() + "_Child_" + requestId) {
            	public void run() {
            		try {
	            		CodifyThreadTemplate template = new CodifyThreadTemplate();
	            		template.executeInTransactionAs(new CodifyThreadExecutor() {
							
							public Object doExecute() {
								Request req = (Request) entityService2.get(Request.class, requestId);
					            LOG.info("Raising event NOIA on request -  " + refNumber + " id - " + requestId);
								entityService2.doOperation(req, "NOIASchedulerFlow");
								return null;
							}
						}, "SYSTEM" );
            		} catch (Exception t) {
			            LOG.error("NOIASchedulerFlow operation failed for - " + refNumber,t);
            		}
            	}
            };
            
            try {
            	t.start();
            	t.join();
            } catch (InterruptedException ie) {
            	// ignore shouldn't happen
            	LOG.error("Error occured at execute",ie);
            }
            
        }
        
        LOG.info("NOIA Scheduler Operation completed");

        return null;
    }
}
