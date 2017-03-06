drop index if exists int_uat.idx_qrtz_j_req_recovery;
drop index if exists int_uat.idx_qrtz_t_next_fire_time;
drop index if exists int_uat.idx_qrtz_t_state;
drop index if exists int_uat.idx_qrtz_t_nft_stl;
drop index if exists int_uat.idx_qrtz_t_volatile;
drop index if exists int_uat.idx_qrtz_ft_trig_name;
drop index if exists int_uat.idx_qrtz_ft_trig_group;
drop index if exists int_uat.idx_qrtz_ft_trig_nm_gp;
drop index if exists int_uat.idx_qrtz_ft_trig_volatile;
drop index if exists int_uat.idx_qrtz_ft_trig_inst_name;
drop index if exists int_uat.idx_qrtz_ft_job_name;
drop index if exists int_uat.idx_qrtz_ft_job_group;
drop index if exists int_uat.idx_qrtz_ft_job_stateful;
drop index if exists int_uat.idx_qrtz_ft_job_req_recovery;

--explicit indexes for quartz job triggers
create index idx_qrtz_j_req_recovery on int_uat.qrtz_job_details(SCHED_NAME,REQUESTS_RECOVERY);
create index idx_qrtz_j_grp on int_uat.qrtz_job_details(SCHED_NAME,JOB_GROUP);

--constraints for LP HUMAN TASK
    alter table int_uat.LP_HUMAN_TASK 
        add constraint FK9E540F12D9C0CF9D 
        foreign key (LAST_MODIFIED_BY_USER_ID) 
        references int_uat.LP_USER;

    alter table int_uat.LP_HUMAN_TASK 
        add constraint FK9E540F1277B20362 
        foreign key (VALIDATING_PAYLOAD_ID) 
        references int_uat.LP_PAYLOAD_TP;

    alter table int_uat.LP_HUMAN_TASK 
        add constraint FK9E540F12E7996CDC 
        foreign key (NTF_CNFG_ID) 
        references int_uat.LP_NTF_CNFG;

    alter table int_uat.LP_HUMAN_TASK 
        add constraint FK9E540F1246DD5B27 
        foreign key (REASSIGN_REASONS_ID) 
        references int_uat.LP_ATTRB_CHC;

    alter table int_uat.LP_HUMAN_TASK 
        add constraint FK9E540F1296310889 
        foreign key (REASSIGN_STRATEGY_ID) 
        references int_uat.LP_ATTRB_CHC;

    alter table int_uat.LP_HUMAN_TASK 
        add constraint FK9E540F128030DEE2 
        foreign key (WRK_ITM_PAYLOAD_ID) 
        references int_uat.LP_PAYLOAD_TP;

    alter table int_uat.LP_HUMAN_TASK 
        add constraint FK9E540F124D05CA53 
        foreign key (PREV_WF_STATUS_ID) 
        references int_uat.LP_WORKFLOW_STATUS;

    alter table int_uat.LP_HUMAN_TASK 
        add constraint FK9E540F1261BBE147 
        foreign key (CREATED_BY_USER_ID) 
        references int_uat.LP_USER;

    alter table int_uat.LP_HUMAN_TASK 
        add constraint FK9E540F1214712350 
        foreign key (TASK_MANAGER_ID) 
        references int_uat.LP_USER;

    alter table int_uat.LP_HUMAN_TASK 
        add constraint FK9E540F124E3F64B8 
        foreign key (ESCALATION_ID) 
        references int_uat.LP_ESCALATION;

    alter table int_uat.LP_HUMAN_TASK 
        add constraint FK9E540F127D5497C7 
        foreign key (WF_STATUS_ID) 
        references int_uat.LP_WORKFLOW_STATUS;

    alter table int_uat.LP_HUMAN_TASK 
        add constraint FK9E540F12E04A34DB 
        foreign key (TASK_PRIORITY_ID) 
        references int_uat.LP_ATTRB_CHC;

    alter table int_uat.LP_HUMAN_TASK 
        add constraint FK9E540F12C432B248 
        foreign key (CMT_GRP_ID) 
        references int_uat.LP_CMT_GRP;

    alter table int_uat.LP_HUMAN_TASK 
        add constraint FK9E540F1261D8DB8D 
        foreign key (SUSPEND_REASONS_ID) 
        references int_uat.LP_ATTRB_CHC;

    alter table int_uat.LP_HUMAN_TASK 
        add constraint FK9E540F12F96A2F90 
        foreign key (OWNER_ID) 
        references int_uat.LP_USER;

    alter table int_uat.LP_HUMAN_TASK 
        add constraint FK9E540F129810E24D 
        foreign key (REASSIGN_OWNER_ID) 
        references int_uat.LP_USER;

    alter table int_uat.LP_HUMAN_TASK_ 
        add constraint FK2C2DD38DA5CA84FE 
        foreign key (REV) 
        references int_uat.REV_INFO;

    alter table int_uat.LP_HUMAN_TASK_BIZ_ADMS 
        add constraint FK3CFDE3626FF6FBD 
        foreign key (LP_USER_ID) 
        references int_uat.LP_USER;

    alter table int_uat.LP_HUMAN_TASK_BIZ_ADMS 
        add constraint FK3CFDE362A110DEFC 
        foreign key (LP_HUMAN_TASK_ID) 
        references int_uat.LP_HUMAN_TASK;

    alter table int_uat.LP_HUMAN_TASK_BIZ_ADMS_ 
        add constraint FK62BE893DA5CA84FE 
        foreign key (REV) 
        references int_uat.REV_INFO;

    alter table int_uat.LP_HUMAN_TASK_ESC_TMS 
        add constraint FKD9C152E3A110DEFC 
        foreign key (LP_HUMAN_TASK_ID) 
        references int_uat.LP_HUMAN_TASK;

    alter table int_uat.LP_HUMAN_TASK_ESC_TMS 
        add constraint FKD9C152E3B9FA0E7D 
        foreign key (LP_TEAM_ID) 
        references int_uat.LP_TEAM;

    alter table int_uat.LP_HUMAN_TASK_ESC_TMS_ 
        add constraint FK5E6909DCA5CA84FE 
        foreign key (REV) 
        references int_uat.REV_INFO;

    alter table int_uat.LP_HUMAN_TASK_ESC_USRS 
        add constraint FK5E6994966FF6FBD 
        foreign key (LP_USER_ID) 
        references int_uat.LP_USER;

    alter table int_uat.LP_HUMAN_TASK_ESC_USRS 
        add constraint FK5E699496A110DEFC 
        foreign key (LP_HUMAN_TASK_ID) 
        references int_uat.LP_HUMAN_TASK;

    alter table int_uat.LP_HUMAN_TASK_ESC_USRS_ 
        add constraint FK6EC8FE89A5CA84FE 
        foreign key (REV) 
        references int_uat.REV_INFO;

    alter table int_uat.LP_HUMAN_TASK_POT_TMS 
        add constraint FK19C20BC3A110DEFC 
        foreign key (LP_HUMAN_TASK_ID) 
        references int_uat.LP_HUMAN_TASK;

    alter table int_uat.LP_HUMAN_TASK_POT_TMS 
        add constraint FK19C20BC3B9FA0E7D 
        foreign key (LP_TEAM_ID) 
        references int_uat.LP_TEAM;

    alter table int_uat.LP_HUMAN_TASK_POT_TMS_ 
        add constraint FK1E7F6CFCA5CA84FE 
        foreign key (REV) 
        references int_uat.REV_INFO;

    alter table int_uat.LP_HUMAN_TASK_POT_USRS 
        add constraint FK1E7FF7B66FF6FBD 
        foreign key (LP_USER_ID) 
        references int_uat.LP_USER;

    alter table int_uat.LP_HUMAN_TASK_POT_USRS 
        add constraint FK1E7FF7B6A110DEFC 
        foreign key (LP_HUMAN_TASK_ID) 
        references int_uat.LP_HUMAN_TASK;

    alter table int_uat.LP_HUMAN_TASK_POT_USRS_ 
        add constraint FKB17EFF69A5CA84FE 
        foreign key (REV) 
        references int_uat.REV_INFO;

    alter table int_uat.LP_HUMAN_TASK_SUB_TASKS 
        add constraint FK254CEBA2A66FE4CB 
        foreign key (LP_HUMAN_TASK_OTHER_ID) 
        references int_uat.LP_HUMAN_TASK;

    alter table int_uat.LP_HUMAN_TASK_SUB_TASKS 
        add constraint FK254CEBA2A110DEFC 
        foreign key (LP_HUMAN_TASK_ID) 
        references int_uat.LP_HUMAN_TASK;

    alter table int_uat.LP_HUMAN_TASK_SUB_TASKS_ 
        add constraint FK845088FDA5CA84FE 
        foreign key (REV) 
        references int_uat.REV_INFO;

--New sequances added
create sequence int_uat.SEQ_100011 start 100000 increment 1;

create sequence int_uat.LP_CMT_INDEX_NUM_SEQ start 100000 increment 1;

create sequence int_uat.LP_HUMAN_TASK_REF_NUM_SEQ start 100000 increment 1;

create sequence int_uat.LP_DCSN_TABLE_META_REF_NUM_SEQ start 100000 increment 1;