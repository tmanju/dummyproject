
-------ADD COLUMNS/ALTER COLUMNS------------
	--qrtz_job_details
	ALTER TABLE int_uat.qrtz_job_details RENAME COLUMN IS_VOLATILE TO IS_NONCONCURRENT;
	ALTER TABLE int_uat.qrtz_job_details RENAME COLUMN IS_STATEFUL TO IS_UPDATE_DATA;

	--qrtz_triggers
	ALTER TABLE int_uat.qrtz_triggers DROP COLUMN IS_VOLATILE;

	--qrtz_fired_triggers
	ALTER TABLE int_uat.qrtz_fired_triggers RENAME COLUMN IS_VOLATILE TO IS_NONCONCURRENT;
	ALTER TABLE int_uat.qrtz_fired_triggers DROP COLUMN IS_STATEFUL;
	
	ALTER TABLE int_uat.qrtz_fired_triggers ADD COLUMN sched_time bigint;
	UPDATE int_uat.qrtz_fired_triggers SET sched_time = 0;
	ALTER TABLE int_uat.qrtz_fired_triggers ALTER COLUMN sched_time SET NOT NULL;

	--Application Level DDLs

	ALTER TABLE int_uat.lp_cmt RENAME COLUMN NUM_INDEX TO INDEX_NUM ;
	ALTER TABLE int_uat.lp_cmt_ RENAME COLUMN NUM_INDEX TO INDEX_NUM ;

	--qrtz_job_details
	ALTER TABLE int_uat.qrtz_job_details ADD COLUMN SCHED_NAME character varying(120);
	UPDATE int_uat.qrtz_job_details SET SCHED_NAME = 'DefaultLPScheduler';
	ALTER TABLE int_uat.qrtz_job_details ALTER COLUMN SCHED_NAME SET NOT NULL;

	--qrtz_triggers

	ALTER TABLE int_uat.qrtz_triggers ADD COLUMN SCHED_NAME character varying(120);
	UPDATE int_uat.qrtz_triggers SET SCHED_NAME = 'DefaultLPScheduler';
	ALTER TABLE int_uat.qrtz_triggers ALTER COLUMN SCHED_NAME SET NOT NULL;
	
	--qrtz_simple_triggers
	ALTER TABLE int_uat.qrtz_simple_triggers ADD COLUMN SCHED_NAME character varying(120);
	UPDATE int_uat.qrtz_simple_triggers SET SCHED_NAME = 'DefaultLPScheduler';
	ALTER TABLE int_uat.qrtz_simple_triggers ALTER COLUMN SCHED_NAME SET NOT NULL;
	
	--qrtz_cron_triggers
	ALTER TABLE int_uat.qrtz_cron_triggers ADD COLUMN SCHED_NAME character varying(120);
	UPDATE int_uat.qrtz_cron_triggers SET SCHED_NAME = 'DefaultLPScheduler';
	ALTER TABLE int_uat.qrtz_cron_triggers ALTER COLUMN SCHED_NAME SET NOT NULL;
	
	--qrtz_blob_triggers
	ALTER TABLE int_uat.qrtz_blob_triggers ADD COLUMN SCHED_NAME character varying(120);
	UPDATE int_uat.qrtz_blob_triggers SET SCHED_NAME = 'DefaultLPScheduler';
	ALTER TABLE int_uat.qrtz_blob_triggers ALTER COLUMN SCHED_NAME SET NOT NULL;
	
	--qrtz_calendars
	ALTER TABLE int_uat.qrtz_calendars ADD COLUMN SCHED_NAME character varying(120);
	UPDATE int_uat.qrtz_calendars SET SCHED_NAME = 'DefaultLPScheduler';
	ALTER TABLE int_uat.qrtz_calendars ALTER COLUMN SCHED_NAME SET NOT NULL;
	
	--qrtz_paused_trigger_grps
	ALTER TABLE int_uat.qrtz_paused_trigger_grps ADD COLUMN SCHED_NAME character varying(120);
	UPDATE int_uat.qrtz_paused_trigger_grps SET SCHED_NAME = 'DefaultLPScheduler';
	ALTER TABLE int_uat.qrtz_paused_trigger_grps ALTER COLUMN SCHED_NAME SET NOT NULL;
	
	--qrtz_fired_triggers
	ALTER TABLE int_uat.qrtz_fired_triggers ADD COLUMN SCHED_NAME character varying(120);
	UPDATE int_uat.qrtz_fired_triggers SET SCHED_NAME = 'DefaultLPScheduler';
	ALTER TABLE int_uat.qrtz_fired_triggers ALTER COLUMN SCHED_NAME SET NOT NULL;
	
	--qrtz_scheduler_state
	ALTER TABLE int_uat.qrtz_scheduler_state ADD COLUMN SCHED_NAME character varying(120);
	UPDATE int_uat.qrtz_scheduler_state SET SCHED_NAME = 'DefaultLPScheduler';
	ALTER TABLE int_uat.qrtz_scheduler_state ALTER COLUMN SCHED_NAME SET NOT NULL;
	
	 --qrtz_locks
	ALTER TABLE int_uat.qrtz_locks ADD COLUMN SCHED_NAME character varying(120);
	UPDATE int_uat.qrtz_locks SET SCHED_NAME = 'DefaultLPScheduler';
	ALTER TABLE int_uat.qrtz_locks ALTER COLUMN SCHED_NAME SET NOT NULL;


