package com.thirdpillar.codify.loanpath.service;

import org.drools.runtime.KnowledgeRuntime;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemManager;

import com.thirdpillar.codify.bc.service.LPUserTaskHandler;

public class ApproverWorkItemHandler extends LPUserTaskHandler{
	
	public ApproverWorkItemHandler(KnowledgeRuntime session, String eventName) {
		super(session, eventName);
		// TODO Auto-generated constructor stub
	}

	public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
		super.executeWorkItem(workItem, manager);
		// TODO 
	}
}
