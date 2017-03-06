package com.thirdpillar.codify.loanpath.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.thirdpillar.foundation.model.IScheduledEventOccurence;
import com.thirdpillar.foundation.model.ScheduledEventAware;
import com.thirdpillar.foundation.rules.RuleService;
import com.thirdpillar.foundation.service.BaseScheduledEventJob;
import com.thirdpillar.foundation.service.EntityService;
import com.thirdpillar.foundation.service.LookupService;
import com.thirdpillar.foundation.util.StringUtils;
import com.thirdpillar.codify.loanpath.model.CovenantEvaluation;
import com.thirdpillar.codify.loanpath.model.Covenant;
import com.thirdpillar.codify.loanpath.model.AttributeChoice;
import com.thirdpillar.codify.loanpath.model.Questionnaire;

public class CovenantJob extends BaseScheduledEventJob {
	
	@Override
	public IScheduledEventOccurence createOccurence(
			ScheduledEventAware eventAware) {
		// TODO Auto-generated method stub
		EntityService es = new EntityService();
		CovenantEvaluation evaluation = (CovenantEvaluation) es.createNew(CovenantEvaluation.class);
		
		Covenant covenant = (Covenant) eventAware;
		
		// copy defautl from covenant to evaluation
		evaluation.setCovenant(covenant);
		
		if (covenant.getQuestions() != null) {
			for (Questionnaire questionnaire : covenant.getQuestions()) {
				Questionnaire qCopy = (Questionnaire) es.createNew(Questionnaire.class);
				qCopy.setQuestion(questionnaire.getQuestion());
				evaluation.addToQuestions(qCopy);				
			}
		}
		evaluation.setRequest(covenant.getRequest());
		evaluation.setTriggeredAt(new Date());
		
		
		AttributeChoice evalSatus = (AttributeChoice) LookupService.getResult("AttributeChoice.byKey", "key", "COVENANT_EVALUATION_STATUS_PENDING");
		evaluation.setCovenantEvalStatus(evalSatus);
		
		return evaluation;
	}

	@Override
	public void executeJob(ScheduledEventAware eventAware,
			IScheduledEventOccurence occurence) {
		// TODO Auto-generated method stub
		CovenantEvaluation evaluation = (CovenantEvaluation) occurence;
				
		String operationName = "CovenantEvaluation";
		
		EntityService es = new EntityService();
		Map<String, Object> paramFacts = new HashMap<String, Object>();
		es.doOperation(evaluation, operationName,paramFacts);

	}

}
