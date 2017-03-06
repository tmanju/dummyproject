/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */
package com.thirdpillar.codify.loanpath.lookup;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.thirdpillar.codify.loanpath.model.WorkflowStatus;
import com.thirdpillar.foundation.lookup.LookupProvider;
import com.thirdpillar.foundation.service.EntityService;


/**
 * DOCUMENT ME!
 *
 * @author   ENTER YOUR FULL NAME
 * @version  1.0
 * @since    1.0
 */
@Configurable public class WorkflowStatusLookupProvider
    implements LookupProvider<WorkflowStatusLookup, WorkflowStatus> {

    //~ Instance fields ------------------------------------------------------------------------------------------------

    @Autowired private EntityService<WorkflowStatus> service;
    private WorkflowStatusLookup workflowStatusLookup;

    //~ Methods --------------------------------------------------------------------------------------------------------

    public void initialize(WorkflowStatusLookup workflowStatusLookup) {
        this.workflowStatusLookup = workflowStatusLookup;

        // HACK aspectj doesn't work because of relcoations of spring fiels to WEB-INF
        service = new EntityService<WorkflowStatus>();
    }

    public List<WorkflowStatus> lookupList() {
        String workflowEntity = workflowStatusLookup.workflowEntity();
        return service.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.WorkflowStatus.byWorkflowEntity","workflowEntity",workflowEntity.toUpperCase());
    }

    public WorkflowStatus lookupValue(String... statusKeys) {
        String workflowEntity = workflowStatusLookup.workflowEntity();
        
        List<WorkflowStatus> wfStatuses = service.findByNamedQueryAndNamedParam("com.thirdpillar.codify.loanpath.model.AttributeChoice.byKeyAndAttributeKey",
        		new String[]{"workflowEntity","statusKey"},
    			new Object[]{workflowEntity.toUpperCase(),statusKeys[0].toUpperCase()});  
        if (wfStatuses.isEmpty()) {
            return wfStatuses.get(0);
        } else {
            return null;
        }
    }
}
