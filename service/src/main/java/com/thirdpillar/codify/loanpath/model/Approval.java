/*
 * Copyright (c) 2005-2011 Third Pillar Systems Inc. All Rights Reserved.
 * Third Pillar Proprietary and Confidential.
 *
 * $URL$
 * $Id$
 */

package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.thirdpillar.codify.loanpath.util.ApprovalLevelComparator;
import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;


public  class Approval extends BaseDataObject{
	
	 //~ Static fields/initializers -------------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 8958673609258371466L;

    //~ Methods --------------------------------------------------------------------------------------------------------
     
    public List<ApprovalLevel> getOrderedApprovalLevels() {
    	
    	if (getApprovalLevels() != null) {
    		List<ApprovalLevel> levels = new ArrayList<ApprovalLevel>();
    		levels.addAll(getApprovalLevels());
    		
    		Collections.sort(levels,new ApprovalLevelComparator());
    		return levels;
    	} else {
    		return getApprovalLevels();
    	}
    }
    
    public void addApproverByTeam(String levelName, String teamName) {
    	Team team = (Team) LookupService.getResult("Team.byName", "name", teamName);
    	addApprover(levelName,team);
    	/*if (team != null) {
    		for (User user : team.getUsers()) {
    			addApprover(levelName, user);
    		}
    	}*/
    }
    
    public void addApprover(String levelName, String userName) {

    	User approverUser = (User) LookupService.getResult("User.byUsername", "username", userName);
    	addApprover(levelName, approverUser);
    }    
    
    public void addApprover(String levelName, User approverUser) {
    	if (approverUser == null) {
    		return;
    	}
        // check if the level already exists (assuming level starts with 1)
        EntityService<ApprovalLevel> es = new EntityService<ApprovalLevel>();
        ApprovalLevel approvalLevel = getApprovalLevel(levelName);
        if (approvalLevel == null) {
            approvalLevel = es.createNew(ApprovalLevel.class);
            approvalLevel.setName(levelName);
            approvalLevel.setMinRespReqPct(new BigDecimal(100));
            approvalLevel.setApproval(this);
            this.addToApprovalLevels(approvalLevel);        	
        }
        
        approvalLevel.addApprover(approverUser);
    }
    
    public void addApprover(String levelName, Team team) {
    	if (team == null) {
    		return;
    	}
        // check if the level already exists (assuming level starts with 1)
        EntityService<ApprovalLevel> es = new EntityService<ApprovalLevel>();
        ApprovalLevel approvalLevel = getApprovalLevel(levelName);
        if (approvalLevel == null) {
            approvalLevel = es.createNew(ApprovalLevel.class);
            approvalLevel.setName(levelName);
            approvalLevel.setMinRespReqPct(new BigDecimal(100));
            approvalLevel.setApproval(this);
            this.addToApprovalLevels(approvalLevel);        	
        }
        
       approvalLevel.addApprover(team);
    }
    
    public ApprovalLevel getApprovalLevel(String levelName) {
    	List<ApprovalLevel> approvalLevels = this.getApprovalLevels();    	
    	if (approvalLevels != null) {
    		for (ApprovalLevel level : approvalLevels) {
    			
    			if (StringUtils.equals(levelName, level.getName())) {
    				return level;
    			}
    				    			
    		}
    	}
    	return null;
    }
}
