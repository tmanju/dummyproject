alter table int_qc.lp_human_task add COLUMN jbpm_task_id BIGINT;
alter table int_qc.lp_human_task_ add COLUMN jbpm_task_id BIGINT;

create table int_qc.LP_MAP_STATUS_WFSTATUS (
  mapping_status_text varchar(255),
  mapping_status_code BIGINT,
  Mapping_WF_STATUS_ID BIGINT not null,
  foreign key(Mapping_WF_STATUS_ID) references int_qc.LP_WORKFLOW_STATUS(LP_WORKFLOW_STATUS_ID) 
);

insert into int_qc.lp_map_status_wfstatus values ('Created', 0, (select lp_workflow_status_id from int_qc.lp_workflow_status where status_key='TASK_STATUS_NEW'));
insert into int_qc.lp_map_status_wfstatus values ('InProgress', 3, (select lp_workflow_status_id from int_qc.lp_workflow_status where status_key='TASK_STATUS_INPROGRESS'));
insert into int_qc.lp_map_status_wfstatus values ('Ready', 1, (select lp_workflow_status_id from int_qc.lp_workflow_status where status_key='TASK_STATUS_READY'));
insert into int_qc.lp_map_status_wfstatus values ('Reserved', 2, (select lp_workflow_status_id from int_qc.lp_workflow_status where status_key='TASK_STATUS_RESERVED'));
insert into int_qc.lp_map_status_wfstatus values ('Suspended', 4, (select lp_workflow_status_id from int_qc.lp_workflow_status where status_key='TASK_STATUS_SUSPENDED'));
insert into int_qc.lp_map_status_wfstatus values ('Completed', 5, (select lp_workflow_status_id from int_qc.lp_workflow_status where status_key='TASK_STATUS_COMPLETED'));
insert into int_qc.lp_map_status_wfstatus values ('Obsolete', 9,  (select lp_workflow_status_id from int_qc.lp_workflow_status where status_key='TASK_STATUS_OBSOLETE'));


create table int_qc.LP_MAP_PRIORITY_PCHOICE (
  priority BIGINT,
  priority_choice_id BIGINT not null,
  foreign key(priority_choice_id) references int_qc.LP_ATTRB_CHC(LP_ATTRB_CHC_ID) 
);

insert into int_qc.LP_MAP_PRIORITY_PCHOICE values (1, (select lp_attrb_chc_id from int_qc.lp_attrb_chc where key_='TASK_PRIORITY_URGENT'));
insert into int_qc.LP_MAP_PRIORITY_PCHOICE values (2, (select lp_attrb_chc_id from int_qc.lp_attrb_chc where key_='TASK_PRIORITY_HIGH'));
insert into int_qc.LP_MAP_PRIORITY_PCHOICE values (3, (select lp_attrb_chc_id from int_qc.lp_attrb_chc where key_='TASK_PRIORITY_MEDIUM'));
insert into int_qc.LP_MAP_PRIORITY_PCHOICE values (4, (select lp_attrb_chc_id from int_qc.lp_attrb_chc where key_='TASK_PRIORITY_LOW'));


create table int_qc.LP_MAP_USER_TEAM_ORG_ENTITY (
  organizationEntityName varchar(255),
  Mapping_User_Id BIGINT,
  Mapping_Team_Id BIGINT, 
  foreign key(Mapping_User_ID) references int_qc.LP_USER(LP_USER_ID),
  foreign key(Mapping_Team_ID) references int_qc.LP_Team(LP_Team_ID)
  
);

alter table int_qc.LP_PAYLOAD_TP ADD COLUMN JBPM_TASK_MAPPING_ID_WRKITM BIGINT, ADD COLUMN JBPM_TASK_MAPPING_ID_VAL BIGINT;

insert into int_qc.LP_MAP_USER_TEAM_ORG_ENTITY(organizationEntityName, mapping_user_id) 
select ot.ID, lu.lp_user_id from int_qc.organizationalentity ot, int_qc.lp_user lu
where ot.id = lu.username and ot.dtype='User';


insert into int_qc.LP_MAP_USER_TEAM_ORG_ENTITY(organizationEntityName, mapping_team_id) 
select ot.ID, lt.lp_team_id from int_qc.organizationalentity ot, int_qc.lp_team lt
where ot.id = lt.name and ot.dtype='Group';

insert into int_qc.lp_payload_tp (lp_payload_tp_id, payload_entity_id, payload_entity_tp, JBPM_TASK_MAPPING_ID_WRKITM) 
select nextval('int_qc.hibernate_sequence'), workitem_payload_entity_id, workitem_payload_entity_type, task_mapping_id from int_qc.core_task_mapping;

insert into int_qc.lp_payload_tp (lp_payload_tp_id, payload_entity_id, payload_entity_tp, JBPM_TASK_MAPPING_ID_VAL) 
select nextval('int_qc.hibernate_sequence'), validating_payload_entity_id, validating_payload_entity_type, task_mapping_id from int_qc.core_task_mapping;
