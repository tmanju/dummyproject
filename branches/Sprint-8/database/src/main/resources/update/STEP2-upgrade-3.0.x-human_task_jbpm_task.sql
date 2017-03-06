insert into int_qc.lp_human_task (LP_HUMAN_TASK_ID, ACT_TAKEN, COMPLETED_DT, DUE_DT, EVENT_NAME, CREATED_AT, LAST_MODIFIED_AT, DAYS_FOR_DUE_DT, REF_NUM, SESSION_ID, TASK_ACTIONS, TASK_DESC, TASK_NAME, TASK_SESSION_ID, TASK_SUM, WRK_ITM_ID, WRK_ITM_PROCESS_ID, WRK_ITM_PROCESS_INSTANCE_ID, jbpm_task_id, wf_status_id, TASK_PRIORITY_ID, asynchronous, escalated_flag, skippable,sys_task, version) 
select nextval('int_qc.hibernate_sequence'), ctm.ACTION_TAKEN, ctm.COMPLETED_DATE, ctm.DUE_DATE, ctm.EVENT_NAME, ctm.CREATED_DATE, ctm.UPDATED_DATE, ctm.DUE_DATE_DELAY_TIME, nextval('int_qc.LP_HUMAN_TASK_REF_NUM_SEQ'),ctm.SESSION_ID, ctm.TASK_ACTIONS, ctm.TASK_DESC, ctm.TASK_NAME, ctm.SESSION_ID, ctm.SUBJECT, ctm.WORK_ITEM_ID, ctm.PROCESS_KBASE_ID, ctm.PROCESS_INSTANCE_ID, ctm.task_mapping_id, 
lmsw.mapping_wf_status_id, 
acp.priority_choice_id, false, false, false, true, 0
from int_qc.core_task_mapping ctm, int_qc.lp_map_status_wfstatus lmsw, int_qc.LP_MAP_PRIORITY_PCHOICE acp, int_qc.task tsk
where    
tsk.status =lmsw.mapping_status_text and ctm.priority= acp.priority and ctm.work_item_id=tsk.workitemid;


update int_qc.lp_human_task ly set prev_wf_status_id=(
select lmsw.mapping_wf_status_id from int_qc.lp_map_status_wfstatus as lmsw,  
int_qc.task tsk, 
int_qc.core_task_mapping ctm
where ly.jbpm_task_id=ctm.task_mapping_id and 
tsk.previousstatus=lmsw.mapping_status_code and 
ctm.work_item_id=tsk.workitemid);

update int_qc.lp_human_task ly set created_by_user_id= (
select mut.mapping_user_id from int_qc.lp_map_user_team_org_entity mut, 
int_qc.core_task_mapping ctm, 
int_qc.lp_human_task as lht
where lht.jbpm_task_id = ctm.task_mapping_id and ctm.created_by = mut.organizationentityname
and ly.lp_human_task_id=lht.lp_human_task_id);

update int_qc.lp_human_task ly set last_modified_by_user_id= (
select mut.mapping_user_id from int_qc.lp_map_user_team_org_entity mut, 
int_qc.core_task_mapping ctm,
int_qc.lp_human_task as lht
where lht.jbpm_task_id = ctm.task_mapping_id and 
ctm.updated_by = mut.organizationentityname  
and ly.lp_human_task_id=lht.lp_human_task_id
);

update int_qc.lp_human_task lht set owner_id= (
select mut.mapping_user_id from int_qc.lp_map_user_team_org_entity mut 
join int_qc.task t ON t.actualowner_id = mut.organizationentityname
JOIN int_qc.core_task_mapping ctm
On t.workitemid=ctm.work_item_id
where lht.jbpm_task_id=ctm.task_mapping_id and t.workitemid=ctm.work_item_id
);

update int_qc.lp_human_task ly set validating_payload_id = 
(select pt.lp_payload_tp_id from int_qc.lp_payload_tp pt,  int_qc.lp_human_task lht
where
lht.jbpm_task_id= pt.JBPM_TASK_MAPPING_ID_WRKITM
and ly.lp_human_task_id=lht.lp_human_task_id);

update int_qc.lp_human_task ly set wrk_itm_payload_id = 
(select lp_payload_tp_id from int_qc.lp_payload_tp pt,int_qc.lp_human_task lht
where 
lht.jbpm_task_id= pt.JBPM_TASK_MAPPING_ID_VAL
and ly.lp_human_task_id=lht.lp_human_task_id);
			   
insert into int_qc.lp_human_task_pot_tms (LP_HUMAN_TASK_ID, LP_TEAM_ID) 
select ht.lp_human_task_id, lpt.lp_team_id from int_qc.peopleassignments_potowners ppo ,int_qc.lp_human_task ht, int_qc.organizationalentity oe, int_qc.lp_team lpt,int_qc.core_task_mapping ctm, int_qc.task task
               where ht.jbpm_task_id = ctm.task_mapping_id
               and ctm.work_item_id = task.workitemid
               and ppo.task_id= task.id
               and oe.id = ppo.entity_id and lpt.name = oe.id and oe.dtype = 'Group';
			   
insert into int_qc.lp_human_task_pot_usrs (LP_HUMAN_TASK_ID, LP_USER_ID) 
select ht.lp_human_task_id, lpu.lp_user_id from int_qc.peopleassignments_potowners ppo ,int_qc.lp_human_task ht, int_qc.organizationalentity oe, int_qc.lp_user lpu,int_qc.core_task_mapping ctm, int_qc.task task
               where ht.jbpm_task_id = ctm.task_mapping_id
               and ctm.work_item_id = task.workitemid
               and ppo.task_id= task.id
               and oe.id = ppo.entity_id and lpu.username = oe.id and oe.dtype = 'User';


insert into int_qc.lp_human_task_(lp_human_task_id,act_taken,assigned_dt,asynchronous,completed_dt,days_for_due_dt,due_dt,escalated_flag,event_name,created_at,last_modified_at,parent_task,process_instance_id,reassign_reason_cmt,ref_num,session_id,skip_reason,skippable,soft_deleted_on,strt_dt,suspended_dt,suspended_reason,swimlane,sys_task,task_actions,task_desc,task_name,task_session_id,task_sum,wrk_itm_id,wrk_itm_process_id,wrk_itm_process_instance_id,cmt_grp_id,escalation_id,created_by_user_id,last_modified_by_user_id,ntf_cnfg_id,owner_id,prev_wf_status_id,reassign_owner_id,reassign_reasons_id,reassign_strategy_id,suspend_reasons_id,task_manager_id,task_priority_id,validating_payload_id,wf_status_id,wrk_itm_payload_id,jbpm_task_id,rev) 
select lp_human_task_id,act_taken,assigned_dt,asynchronous,completed_dt,days_for_due_dt,due_dt,escalated_flag,event_name,created_at,last_modified_at,parent_task,process_instance_id,reassign_reason_cmt,ref_num,session_id,skip_reason,skippable,soft_deleted_on,strt_dt,suspended_dt,suspended_reason,swimlane,sys_task,task_actions,task_desc,task_name,task_session_id,task_sum,wrk_itm_id,wrk_itm_process_id,wrk_itm_process_instance_id,cmt_grp_id,escalation_id,created_by_user_id,last_modified_by_user_id,ntf_cnfg_id,owner_id,prev_wf_status_id,reassign_owner_id,reassign_reasons_id,reassign_strategy_id,suspend_reasons_id,task_manager_id,task_priority_id,validating_payload_id,wf_status_id,wrk_itm_payload_id,jbpm_task_id,CAST(coalesce(ref_num,'') AS integer) from int_qc.lp_human_task;
