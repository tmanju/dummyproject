------DROP------- 
	-- qrtz_job_details
	ALTER TABLE int_uat.qrtz_job_details  DROP CONSTRAINT qrtz_job_details_pkey CASCADE;
	--qrtz_triggers
	ALTER TABLE int_uat.qrtz_triggers  DROP CONSTRAINT qrtz_triggers_pkey CASCADE;
	ALTER TABLE int_uat.qrtz_triggers DROP CONSTRAINT IF EXISTS  qrtz_triggers_job_name_fkey;
	--qrtz_simple_triggers
	ALTER TABLE int_uat.qrtz_simple_triggers DROP CONSTRAINT qrtz_simple_triggers_pkey;
	ALTER TABLE int_uat.qrtz_simple_triggers DROP CONSTRAINT IF EXISTS qrtz_simple_triggers_trigger_name_fkey;
	 --qrtz_cron_triggers
	ALTER TABLE int_uat.qrtz_cron_triggers DROP CONSTRAINT qrtz_cron_triggers_pkey;
	ALTER TABLE int_uat.qrtz_cron_triggers DROP CONSTRAINT IF EXISTS qrtz_cron_triggers_trigger_name_fkey;
	 
	--qrtz_blob_triggers
	ALTER TABLE int_uat.qrtz_blob_triggers DROP CONSTRAINT qrtz_blob_triggers_pkey;
	ALTER TABLE int_uat.qrtz_blob_triggers DROP CONSTRAINT IF EXISTS qrtz_blob_triggers_trigger_name_fkey;

	--qrtz_calendars
	ALTER TABLE int_uat.qrtz_calendars DROP CONSTRAINT qrtz_calendars_pkey;
	
	 --qrtz_paused_trigger_grps
	ALTER TABLE int_uat.qrtz_paused_trigger_grps DROP CONSTRAINT qrtz_paused_trigger_grps_pkey;
	
	 --qrtz_fired_triggers
	ALTER TABLE int_uat.qrtz_fired_triggers DROP CONSTRAINT qrtz_fired_triggers_pkey;
	
	 --qrtz_scheduler_state
	ALTER TABLE int_uat.qrtz_scheduler_state DROP CONSTRAINT qrtz_scheduler_state_pkey;
	
	 --qrtz_locks
	ALTER TABLE int_uat.qrtz_locks DROP CONSTRAINT qrtz_locks_pkey;

	--qrtz_simprop_triggers
	ALTER TABLE int_uat.qrtz_simprop_triggers DROP CONSTRAINT IF EXISTS qrtz_simprop_triggers_pkey;
	ALTER TABLE int_uat.qrtz_simprop_triggers DROP CONSTRAINT IF EXISTS qrtz_simprop_triggers_sched_name_fkey;
-------ADD--------	
	-- qrtz_job_details
	ALTER TABLE int_uat.qrtz_job_details ADD CONSTRAINT qrtz_job_details_pkey PRIMARY KEY (SCHED_NAME,JOB_NAME,JOB_GROUP);

	--qrtz_triggers
	ALTER TABLE int_uat.qrtz_triggers ADD CONSTRAINT qrtz_triggers_pkey PRIMARY KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
	ALTER TABLE int_uat.qrtz_triggers	ADD CONSTRAINT qrtz_triggers_job_name_fkey FOREIGN KEY (SCHED_NAME,JOB_NAME,JOB_GROUP)
      REFERENCES int_uat.qrtz_job_details (SCHED_NAME,JOB_NAME,JOB_GROUP) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
	--qrtz_simple_triggers
	ALTER TABLE int_uat.qrtz_simple_triggers ADD CONSTRAINT qrtz_simple_triggers_pkey PRIMARY KEY(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
	ALTER TABLE int_uat.qrtz_simple_triggers ADD CONSTRAINT qrtz_simple_triggers_trigger_name_fkey FOREIGN KEY  (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
		  REFERENCES int_uat.qrtz_triggers (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) MATCH SIMPLE
		  ON UPDATE NO ACTION ON DELETE NO ACTION;

	--qrtz_cron_triggers
	ALTER TABLE int_uat.qrtz_cron_triggers ADD CONSTRAINT qrtz_cron_triggers_pkey PRIMARY KEY(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);

	ALTER TABLE int_uat.qrtz_cron_triggers ADD CONSTRAINT qrtz_cron_triggers_trigger_name_fkey FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
      REFERENCES int_uat.qrtz_triggers (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
	--qrtz_blob_triggers
	ALTER TABLE int_uat.qrtz_blob_triggers ADD CONSTRAINT qrtz_blob_triggers_pkey PRIMARY KEY(SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP);
	
	ALTER TABLE int_uat.qrtz_blob_triggers ADD CONSTRAINT qrtz_blob_triggers_trigger_name_fkey FOREIGN KEY (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP)
      REFERENCES int_uat.qrtz_triggers (SCHED_NAME,TRIGGER_NAME,TRIGGER_GROUP) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

	--qrtz_calendars
	ALTER TABLE int_uat.qrtz_calendars ADD CONSTRAINT qrtz_calendars_pkey PRIMARY KEY(SCHED_NAME,CALENDAR_NAME);

	--qrtz_paused_trigger_grps
	ALTER TABLE int_uat.qrtz_paused_trigger_grps ADD CONSTRAINT qrtz_paused_trigger_grps_pkey PRIMARY KEY(SCHED_NAME,TRIGGER_GROUP);

	--qrtz_fired_triggers
	ALTER TABLE int_uat.qrtz_fired_triggers ADD CONSTRAINT qrtz_fired_triggers_pkey PRIMARY KEY(SCHED_NAME,ENTRY_ID);


	--qrtz_scheduler_state
	ALTER TABLE int_uat.qrtz_scheduler_state ADD CONSTRAINT qrtz_scheduler_state_pkey PRIMARY KEY(SCHED_NAME,INSTANCE_NAME);

	--qrtz_locks
	ALTER TABLE int_uat.qrtz_locks ADD CONSTRAINT qrtz_locks_pkey PRIMARY KEY(SCHED_NAME,LOCK_NAME);
	
	--qrtz_simprop_triggers
	ALTER TABLE int_uat.qrtz_simprop_triggers ADD CONSTRAINT qrtz_simprop_triggers_pkey PRIMARY KEY(sched_name, trigger_name, trigger_group);
	
	ALTER TABLE int_uat.qrtz_simprop_triggers ADD CONSTRAINT qrtz_simprop_triggers_sched_name_fkey FOREIGN KEY (sched_name, trigger_name, trigger_group)
      REFERENCES int_uat.qrtz_triggers (sched_name, trigger_name, trigger_group) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;