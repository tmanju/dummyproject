DROP INDEX IDX_CORE_TASK_MAP_NOT_FC;

ALTER TABLE CORE_TASK_MAPPING drop constraint FKC5A6809426D4B027;

ALTER TABLE CORE_TASK_MAPPING 
	DROP (DUE_DATE,
	 DUE_DATE_DELAY_TIME,
	 ESCALATED_FLAG,
	 ESCALATION_GROUP,
	 ESCALATION_TO,
	 FROM_MAIL,
	 TO_RECIPIENTS,
	 NOTIFICATION_ID,
	 ESCALATION_ACTOR);
	 
ALTER TABLE CORE_TASK_MAPPING_ 
	DROP (DUE_DATE,
	 DUE_DATE_DELAY_TIME,
	 ESCALATED_FLAG,
	 ESCALATION_GROUP,
	 ESCALATION_TO,
	 FROM_MAIL,
	 TO_RECIPIENTS,
	 NOTIFICATION_ID,
	 ESCALATION_ACTOR);
	
	


COMMIT;
