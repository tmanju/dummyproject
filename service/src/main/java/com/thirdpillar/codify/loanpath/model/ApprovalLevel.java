



package com.thirdpillar.codify.loanpath.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.thirdpillar.foundation.model.BaseDataObject;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;


public  class ApprovalLevel  extends BaseDataObject   {
	
	private static final long serialVersionUID = -7892669283767476747L;
	
	public String getLevelDecision(){
		String lvlDecision="";
		double approvedCount=0;
		List<Approver> approverList=getApproverList();
		if(approverList!=null){
		for(Approver approver:approverList){
			
				AttributeChoice attribChoice=approver.getDecision();
				if(attribChoice!=null){
					if("APPROVAL_DECISION_DECLINED".equalsIgnoreCase(attribChoice.getKey())){
						lvlDecision="Declined";
					}else if("APPROVAL_DECISION_APPROVED".equalsIgnoreCase(attribChoice.getKey())){
						approvedCount=approvedCount+1;
					}
				}
			}
			//Check if all the approvers decision is approved,Change the level decision to approved.
			if(approvedCount==approverList.size()){
				lvlDecision="Approved";
			}
			
		}
		return lvlDecision;
	}
	
	public String getUppercaseName() {
		return StringUtils.upperCase(getName());
	}
	
	public void addApprover(String userName) {
		User approverUser = (User) LookupService.getResult("User.byUsername", "username", userName);
		addApprover(approverUser);
	}
	
	public void addApprover(User approverUser) {

	    if (approverUser != null) {

		    // check if this user is already added as an approver to this level
		    Approver newApprover = null;
	
		    if (this.getApproverList() != null) {
	
		    	for (Approver approver : this.getApproverList()) {
	
		    		if (StringUtils.equals(approverUser.getUsername(),approver.getUser().getUsername())) {
		    			newApprover = approver;
		    			break;
		    		}
		    	}
		    }

            if (newApprover == null) {
                EntityService<Approver> es = new EntityService<Approver>();
                Approver approver = es.createNew(Approver.class);
                approver.setUser(approverUser);
                approver.setApprovalLevel(this);
                this.addToApproverList(approver);
            }
	    }
	}
	
	public void addApprover(Team approverTeam) {

	    if (approverTeam != null) {

		    // check if this user is already added as an approver to this level
		    Approver newApprover = null;
	
			    	for(Approver approver : getApproverList()){
			    		if (StringUtils.equals(approver.getTeam().getName(), approverTeam.getName())) {
			    			newApprover = approver;
			    			break;
			    		}
			    	}
            if (newApprover == null) {
                EntityService<Approver> es = new EntityService<Approver>();
                Approver approver = es.createNew(Approver.class);
                approver.setTeam(approverTeam);
                approver.setApprovalLevel(this);
                this.addToApproverList(approver);
            }
	    }
	}
	
	public String getApproverTeamList(){
		List<String> appList = null;
		for(Approver app : this.getApproverList()){
			if(appList == null){
				appList = new ArrayList<String>();
			}
			if(app.getTeam() != null){
				appList.add(app.getTeam().getName());
			}else if(app.getUser() != null){
				appList.add(app.getUser().getContact().getDisplayName());
			}
		}
		return appList.toString();
	}

}