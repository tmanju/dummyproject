alter table int_uat.lp_human_task drop column jbpm_task_id;

drop table int_uat.LP_MAP_STATUS_WFSTATUS;

drop table int_uat.LP_MAP_PRIORITY_PCHOICE;

drop table int_uat.LP_MAP_USER_TEAM_ORG_ENTITY;

alter table int_uat.LP_PAYLOAD_TP DROP  COLUMN JBPM_TASK_MAPPING_ID_WRKITM, DROP COLUMN JBPM_TASK_MAPPING_ID_VAL;