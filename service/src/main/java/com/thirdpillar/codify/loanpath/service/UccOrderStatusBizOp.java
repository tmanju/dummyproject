/*
 * Copyright (c) 2005-2012 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thirdpillar.codify.loanpath.model.UccSearchDetail;
import com.thirdpillar.foundation.service.AbstractBusinessOperation;
import com.thirdpillar.foundation.service.CodifyThreadExecutor;
import com.thirdpillar.foundation.service.CodifyThreadTemplate;
import com.thirdpillar.foundation.service.EntityService;


/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
public class UccOrderStatusBizOp extends AbstractBusinessOperation {

    //~ Static fields/initializers -------------------------------------------------------------------------------------

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------
    private static final Log LOG = LogFactory.getLog(UccOrderStatusBizOp.class);

    //~ Methods --------------------------------------------------------------------------------------------------------

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------
    public Object execute(String operation, Object... params) {

        // --------------------------------------------------------------------------------------
        LOG.info("UCC Order Status Scheduler Operation is triggered");

        EntityService<UccSearchDetail> entityService = new EntityService<UccSearchDetail>();

        // Fetching all the requests/Applications that comes under given
        // statuses.
        String queryString =
            "from com.thirdpillar.codify.loanpath.model.UccSearchDetail as detail where detail.orderItemNum is not null and (detail.orderStatus is null or detail.orderStatus.key != 'UCC_SEARCH_ORDER_STATUS_COMPLETED')";
        List<UccSearchDetail> uccSearchDetailList = entityService.find(queryString);
        LOG.info("result for ucc order search query - " +
            ((uccSearchDetailList == null) ? 0 : uccSearchDetailList.size()));

        Map<String, UccSearchDetail> uccSrchDetailByOrderNumMap = new HashMap<String, UccSearchDetail>();

        if ((uccSearchDetailList != null) && (!uccSearchDetailList.isEmpty())) {

            for (UccSearchDetail uccSearchDetail : uccSearchDetailList) {
                uccSrchDetailByOrderNumMap.put(uccSearchDetail.getOrderNum(), uccSearchDetail);
            }

            List<UccSearchDetail> filteredUccSearchDetailList = new ArrayList<UccSearchDetail>(
                    uccSrchDetailByOrderNumMap.values());
            LOG.info("Number of Unique Ucc Orders - " + filteredUccSearchDetailList.size());
            final EntityService entityService2 = entityService;

            for (UccSearchDetail uccSearchDetail : filteredUccSearchDetailList) {
            	final Long detailId = uccSearchDetail.getId();
            	final String orderNum = uccSearchDetail.getOrderNum();
                Thread t = new Thread(Thread.currentThread().getName() + "_Child" + detailId) {
                	public void run() {
                		try {
    	            		CodifyThreadTemplate template = new CodifyThreadTemplate();
    	            		template.executeInTransactionAs(new CodifyThreadExecutor() {
    							
    							public Object doExecute() {
    								UccSearchDetail detail = (UccSearchDetail) entityService2.get(UccSearchDetail.class, detailId);
    				                LOG.info("check ucc order status for  -  " + detail.getOrderNum());
    				                entityService2.doOperation(detail, "UCCSearchOrderStatus");
    								return null;
    							}
    						}, "SYSTEM" );
                		} catch (Exception t) {
    			            LOG.error("UCCSearchOrderStatus operation failed for - " + orderNum,t);
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
        }

        //Now iterate over filtered applications and send these application to workflow by generating events.
        return null;
    }
}
