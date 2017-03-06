-----CREATE table----
	--LP_HUMAN_TASK
	create table int_uat.LP_HUMAN_TASK (
        LP_HUMAN_TASK_ID int8 not null,
        ACT_TAKEN varchar(255),
        ASSIGNED_DT timestamp,
        ASYNCHRONOUS bool,
        COMPLETED_DT timestamp,
        DAYS_FOR_DUE_DT varchar(255),
        DUE_DT timestamp,
        ESCALATED_FLAG bool,
        EVENT_NAME varchar(255),
        CREATED_AT timestamp,
        LAST_MODIFIED_AT timestamp,
        PARENT_TASK varchar(255),
        PROCESS_INSTANCE_ID int8,
        REASSIGN_REASON_CMT varchar(4000),
        REF_NUM varchar(255),
        SESSION_ID int8,
        SKIP_REASON varchar(4000),
        SKIPPABLE bool,
        SOFT_DELETED_ON timestamp,
        STRT_DT timestamp,
        SUSPENDED_DT timestamp,
        SUSPENDED_REASON varchar(4000),
        SWIMLANE varchar(255),
        SYS_TASK bool,
        TASK_ACTIONS varchar(255),
        TASK_DESC varchar(4000),
        TASK_NAME varchar(255),
        TASK_SESSION_ID int8,
        TASK_SUM varchar(4000),
        VERSION int4,
        WRK_ITM_ID int8,
        WRK_ITM_PROCESS_ID varchar(255),
        WRK_ITM_PROCESS_INSTANCE_ID int8,
        CMT_GRP_ID int8,
        ESCALATION_ID int8,
        CREATED_BY_USER_ID int8,
        LAST_MODIFIED_BY_USER_ID int8,
        NTF_CNFG_ID int8,
        OWNER_ID int8,
        PREV_WF_STATUS_ID int8,
        REASSIGN_OWNER_ID int8,
        REASSIGN_REASONS_ID int8,
        REASSIGN_STRATEGY_ID int8,
        SUSPEND_REASONS_ID int8,
        TASK_MANAGER_ID int8,
        TASK_PRIORITY_ID int8,
        VALIDATING_PAYLOAD_ID int8,
        WF_STATUS_ID int8,
        WRK_ITM_PAYLOAD_ID int8,
        primary key (LP_HUMAN_TASK_ID)
    );
	
	--LP_HUMAN_TASK_
    create table int_uat.LP_HUMAN_TASK_ (
        LP_HUMAN_TASK_ID int8 not null,
        REV int8 not null,
        REVTYPE int2,
        ACT_TAKEN varchar(255),
        ASSIGNED_DT timestamp,
        ASYNCHRONOUS bool,
        COMPLETED_DT timestamp,
        DAYS_FOR_DUE_DT varchar(255),
        DUE_DT timestamp,
        ESCALATED_FLAG bool,
        EVENT_NAME varchar(255),
        CREATED_AT timestamp,
        LAST_MODIFIED_AT timestamp,
        PARENT_TASK varchar(255),
        PROCESS_INSTANCE_ID int8,
        REASSIGN_REASON_CMT varchar(4000),
        REF_NUM varchar(255),
        SESSION_ID int8,
        SKIP_REASON varchar(4000),
        SKIPPABLE bool,
        SOFT_DELETED_ON timestamp,
        STRT_DT timestamp,
        SUSPENDED_DT timestamp,
        SUSPENDED_REASON varchar(4000),
        SWIMLANE varchar(255),
        SYS_TASK bool,
        TASK_ACTIONS varchar(255),
        TASK_DESC varchar(4000),
        TASK_NAME varchar(255),
        TASK_SESSION_ID int8,
        TASK_SUM varchar(4000),
        WRK_ITM_ID int8,
        WRK_ITM_PROCESS_ID varchar(255),
        WRK_ITM_PROCESS_INSTANCE_ID int8,
        CMT_GRP_ID int8,
        ESCALATION_ID int8,
        CREATED_BY_USER_ID int8,
        LAST_MODIFIED_BY_USER_ID int8,
        NTF_CNFG_ID int8,
        OWNER_ID int8,
        PREV_WF_STATUS_ID int8,
        REASSIGN_OWNER_ID int8,
        REASSIGN_REASONS_ID int8,
        REASSIGN_STRATEGY_ID int8,
        SUSPEND_REASONS_ID int8,
        TASK_MANAGER_ID int8,
        TASK_PRIORITY_ID int8,
        VALIDATING_PAYLOAD_ID int8,
        WF_STATUS_ID int8,
        WRK_ITM_PAYLOAD_ID int8,
        primary key (LP_HUMAN_TASK_ID, REV)
    );
	
	create table int_uat.LP_ESCALATION (
        LP_ESCALATION_ID int8 not null,
        DUE_DT_DELAY_TIME varchar(255),
        ESCALATION_ON date,
        EXP_TIME varchar(255),
        SOFT_DELETED_ON timestamp,
        VERSION int4,
        ESCALATION_TO_ID int8,
        primary key (LP_ESCALATION_ID)
    );

    create table int_uat.LP_ESCALATION_ (
        LP_ESCALATION_ID int8 not null,
        REV int8 not null,
        REVTYPE int2,
        DUE_DT_DELAY_TIME varchar(255),
        ESCALATION_ON date,
        EXP_TIME varchar(255),
        SOFT_DELETED_ON timestamp,
        ESCALATION_TO_ID int8,
        primary key (LP_ESCALATION_ID, REV)
    );
	
	--LP_HUMAN_TASK_BIZ_ADMS
    create table int_uat.LP_HUMAN_TASK_BIZ_ADMS (
        LP_HUMAN_TASK_ID int8 not null,
        LP_USER_ID int8 not null
    );

	--LP_HUMAN_TASK_BIZ_ADMS_
    create table int_uat.LP_HUMAN_TASK_BIZ_ADMS_ (
        REV int8 not null,
        LP_HUMAN_TASK_ID int8 not null,
        LP_USER_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_HUMAN_TASK_ID, LP_USER_ID)
    );

	--LP_HUMAN_TASK_ESC_TMS
    create table int_uat.LP_HUMAN_TASK_ESC_TMS (
        LP_HUMAN_TASK_ID int8 not null,
        LP_TEAM_ID int8 not null
    );

	--LP_HUMAN_TASK_ESC_TMS_
    create table int_uat.LP_HUMAN_TASK_ESC_TMS_ (
        REV int8 not null,
        LP_HUMAN_TASK_ID int8 not null,
        LP_TEAM_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_HUMAN_TASK_ID, LP_TEAM_ID)
    );

	--LP_HUMAN_TASK_ESC_USRS
    create table int_uat.LP_HUMAN_TASK_ESC_USRS (
        LP_HUMAN_TASK_ID int8 not null,
        LP_USER_ID int8 not null
    );
	
	--LP_HUMAN_TASK_ESC_USRS_
    create table int_uat.LP_HUMAN_TASK_ESC_USRS_ (
        REV int8 not null,
        LP_HUMAN_TASK_ID int8 not null,
        LP_USER_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_HUMAN_TASK_ID, LP_USER_ID)
    );
	
	--LP_HUMAN_TASK_POT_TMS
    create table int_uat.LP_HUMAN_TASK_POT_TMS (
        LP_HUMAN_TASK_ID int8 not null,
        LP_TEAM_ID int8 not null
    );

	--LP_HUMAN_TASK_POT_TMS_
    create table int_uat.LP_HUMAN_TASK_POT_TMS_ (
        REV int8 not null,
        LP_HUMAN_TASK_ID int8 not null,
        LP_TEAM_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_HUMAN_TASK_ID, LP_TEAM_ID)
    );

	--LP_HUMAN_TASK_POT_USRS
    create table int_uat.LP_HUMAN_TASK_POT_USRS (
        LP_HUMAN_TASK_ID int8 not null,
        LP_USER_ID int8 not null
    );

	--LP_HUMAN_TASK_POT_USRS_
    create table int_uat.LP_HUMAN_TASK_POT_USRS_ (
        REV int8 not null,
        LP_HUMAN_TASK_ID int8 not null,
        LP_USER_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_HUMAN_TASK_ID, LP_USER_ID)
    );

	--LP_HUMAN_TASK_SUB_TASKS
    create table int_uat.LP_HUMAN_TASK_SUB_TASKS (
        LP_HUMAN_TASK_ID int8 not null,
        LP_HUMAN_TASK_OTHER_ID int8 not null,
        unique (LP_HUMAN_TASK_OTHER_ID)
    );

	--LP_HUMAN_TASK_SUB_TASKS_
    create table int_uat.LP_HUMAN_TASK_SUB_TASKS_ (
        REV int8 not null,
        LP_HUMAN_TASK_ID int8 not null,
        LP_HUMAN_TASK_OTHER_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_HUMAN_TASK_ID, LP_HUMAN_TASK_OTHER_ID)
    );
	
	--LP_PAYLOAD_TP
	create table int_uat.LP_PAYLOAD_TP (
        LP_PAYLOAD_TP_ID int8 not null,
        PAYLOAD_ENTITY_ID varchar(255),
        PAYLOAD_ENTITY_TP varchar(255),
        SOFT_DELETED_ON timestamp,
        VERSION int4,
        primary key (LP_PAYLOAD_TP_ID)
    );

	--LP_PAYLOAD_TP_
    create table int_uat.LP_PAYLOAD_TP_ (
        LP_PAYLOAD_TP_ID int8 not null,
        REV int8 not null,
        REVTYPE int2,
        PAYLOAD_ENTITY_ID varchar(255),
        PAYLOAD_ENTITY_TP varchar(255),
        SOFT_DELETED_ON timestamp,
        primary key (LP_PAYLOAD_TP_ID, REV)
    );

	--LP_ASYNCH_WRK_QUEUE	
	create table int_uat.LP_ASYNCH_WRK_QUEUE (
        LP_ASYNCH_WRK_QUEUE_ID int8 not null,
        COMPLETED_AT timestamp,
        CREATED_AT timestamp,
        DISPLAY_NAME varchar(255),
        ENTITY_ID int8,
        ENTITY_NAME varchar(255),
        ERROR_MESSAGE varchar(255),
        LAST_EXECUTED_AT timestamp,
        MAX_NUM_OF_TRIES int4,
        NUM_OF_TRIES int4,
        OPERATION_NAME varchar(255),
        PAYLOAD text,
        PHASE varchar(255),
        PROCESS_AT timestamp,
        QUEUED_AT timestamp,
        REF_NUM varchar(255),
        SOFT_DELETED_ON timestamp,
        STACK_TRACE text,
        STARTED_AT timestamp,
        STATUS varchar(255),
        USER_NAME varchar(255),
        VERSION int4,
        primary key (LP_ASYNCH_WRK_QUEUE_ID)
    );

	--LP_ASYNCH_WRK_QUEUE_
    create table int_uat.LP_ASYNCH_WRK_QUEUE_ (
        LP_ASYNCH_WRK_QUEUE_ID int8 not null,
        REV int8 not null,
        REVTYPE int2,
        COMPLETED_AT timestamp,
        CREATED_AT timestamp,
        DISPLAY_NAME varchar(255),
        ENTITY_ID int8,
        ENTITY_NAME varchar(255),
        ERROR_MESSAGE varchar(255),
        LAST_EXECUTED_AT timestamp,
        MAX_NUM_OF_TRIES int4,
        NUM_OF_TRIES int4,
        OPERATION_NAME varchar(255),
        PAYLOAD text,
        PHASE varchar(255),
        PROCESS_AT timestamp,
        QUEUED_AT timestamp,
        REF_NUM varchar(255),
        SOFT_DELETED_ON timestamp,
        STACK_TRACE text,
        STARTED_AT timestamp,
        STATUS varchar(255),
        USER_NAME varchar(255),
        primary key (LP_ASYNCH_WRK_QUEUE_ID, REV)
    );
	
	--LP_TASK_CNFG_POT_TMS
	create table int_uat.LP_TASK_CNFG_POT_TMS (
        LP_TASK_CNFG_ID int8 not null,
        LP_TEAM_ID int8 not null
    );

	--LP_TASK_CNFG_POT_TMS_
    create table int_uat.LP_TASK_CNFG_POT_TMS_ (
        REV int8 not null,
        LP_TASK_CNFG_ID int8 not null,
        LP_TEAM_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_TASK_CNFG_ID, LP_TEAM_ID)
    );

	--LP_TASK_CNFG_POT_USRS
    create table int_uat.LP_TASK_CNFG_POT_USRS (
        LP_TASK_CNFG_ID int8 not null,
        LP_USER_ID int8 not null
    );

	--LP_TASK_CNFG_POT_USRS_
    create table int_uat.LP_TASK_CNFG_POT_USRS_ (
        REV int8 not null,
        LP_TASK_CNFG_ID int8 not null,
        LP_USER_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_TASK_CNFG_ID, LP_USER_ID)
    );
	
	--LP_TASK_CNFG_BIZ_ADMS	
	create table int_uat.LP_TASK_CNFG_BIZ_ADMS (
        LP_TASK_CNFG_ID int8 not null,
        LP_USER_ID int8 not null
    );

	--LP_TASK_CNFG_BIZ_ADMS_
    create table int_uat.LP_TASK_CNFG_BIZ_ADMS_ (
        REV int8 not null,
        LP_TASK_CNFG_ID int8 not null,
        LP_USER_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_TASK_CNFG_ID, LP_USER_ID)
    );

	--LP_TASK_CNFG_ESC_TMS
    create table int_uat.LP_TASK_CNFG_ESC_TMS (
        LP_TASK_CNFG_ID int8 not null,
        LP_TEAM_ID int8 not null
    );

	--LP_TASK_CNFG_ESC_TMS_
    create table int_uat.LP_TASK_CNFG_ESC_TMS_ (
        REV int8 not null,
        LP_TASK_CNFG_ID int8 not null,
        LP_TEAM_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_TASK_CNFG_ID, LP_TEAM_ID)
    );

	--LP_TASK_CNFG_ESC_USRS
    create table int_uat.LP_TASK_CNFG_ESC_USRS (
        LP_TASK_CNFG_ID int8 not null,
        LP_USER_ID int8 not null
    );

	--LP_TASK_CNFG_ESC_USRS_
    create table int_uat.LP_TASK_CNFG_ESC_USRS_ (
        REV int8 not null,
        LP_TASK_CNFG_ID int8 not null,
        LP_USER_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_TASK_CNFG_ID, LP_USER_ID)
    );


	--LP_ADDRESS_USE
	create table int_uat.LP_ADDRESS_USE (
			LP_ADDRESS_ID int8 not null,
			LP_ATTRB_CHC_ID int8 not null
		);
		
	--LP_ADDRESS_USE_
	create table int_uat.LP_ADDRESS_USE_ (
			REV int8 not null,
			LP_ADDRESS_ID int8 not null,
			LP_ATTRB_CHC_ID int8 not null,
			REVTYPE int2,
			primary key (REV, LP_ADDRESS_ID, LP_ATTRB_CHC_ID)
	);