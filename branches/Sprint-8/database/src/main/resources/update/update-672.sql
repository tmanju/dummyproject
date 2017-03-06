----- Adding column lp_fac_placement_audit -----
ALTER TABLE int_dev.lp_fac_placement_audit ADD COLUMN wf_status_id bigint;
ALTER TABLE int_dev.lp_fac_placement_audit ADD COLUMN process_instance_id bigint;
ALTER TABLE int_dev.lp_fac_placement_audit ADD COLUMN session_id bigint;

ALTER TABLE int_dev.lp_fac_placement_audit_ ADD COLUMN wf_status_id bigint;
ALTER TABLE int_dev.lp_fac_placement_audit_ ADD COLUMN process_instance_id bigint;
ALTER TABLE int_dev.lp_fac_placement_audit_ ADD COLUMN session_id bigint;

ALTER TABLE int_dev.lp_fund_allocator_hist ADD COLUMN wf_status_id bigint;
ALTER TABLE int_dev.lp_fund_allocator_hist ADD COLUMN process_instance_id bigint;
ALTER TABLE int_dev.lp_fund_allocator_hist ADD COLUMN session_id bigint;

ALTER TABLE int_dev.lp_fund_allocator_hist_ ADD COLUMN wf_status_id bigint;
ALTER TABLE int_dev.lp_fund_allocator_hist_ ADD COLUMN process_instance_id bigint;
ALTER TABLE int_dev.lp_fund_allocator_hist_ ADD COLUMN session_id bigint;


------- lp_deal -------------
UPDATE  int_dev.lp_deal set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'FUNDING_REQUEST_STATUS_UNALLOCATED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'FUNDING_REQUEST_STATUS_UNALLOCATED');

UPDATE  int_dev.lp_deal set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'FUNDING_REQUEST_STATUS_ALLOCATED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'FUNDING_REQUEST_STATUS_ALLOCATED');

UPDATE  int_dev.lp_deal set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'FUNDING_REQUEST_STATUS_PARTIALLY_FUNDED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'FUNDING_REQUEST_STATUS_PARTIALLY_FUNDED');

UPDATE  int_dev.lp_deal set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'FUNDING_REQUEST_STATUS_FULLY_FUNDED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'FUNDING_REQUEST_STATUS_FULLY_FUNDED');

------- lp_deal_ -------------
UPDATE  int_dev.lp_deal_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'FUNDING_REQUEST_STATUS_UNALLOCATED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'FUNDING_REQUEST_STATUS_UNALLOCATED');

UPDATE  int_dev.lp_deal_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'FUNDING_REQUEST_STATUS_ALLOCATED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'FUNDING_REQUEST_STATUS_ALLOCATED');

UPDATE  int_dev.lp_deal_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'FUNDING_REQUEST_STATUS_PARTIALLY_FUNDED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'FUNDING_REQUEST_STATUS_PARTIALLY_FUNDED');

UPDATE  int_dev.lp_deal_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'FUNDING_REQUEST_STATUS_FULLY_FUNDED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'FUNDING_REQUEST_STATUS_FULLY_FUNDED');


------- lp_fund_allocator -------------
UPDATE  int_dev.lp_fund_allocator set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'FUNDING_PLACEMENT_STATUS_NEW')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'FUNDING_PLACEMENT_STATUS_NEW');

UPDATE  int_dev.lp_fund_allocator set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'FUNDING_PLACEMENT_STATUS_REQUESTED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'FUNDING_PLACEMENT_STATUS_REQUESTED');

UPDATE  int_dev.lp_fund_allocator set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'FUNDING_PLACEMENT_STATUS_FAILED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'FUNDING_PLACEMENT_STATUS_FAILED');

UPDATE  int_dev.lp_fund_allocator set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'FUNDING_PLACEMENT_STATUS_FUNDED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'FUNDING_PLACEMENT_STATUS_FUNDED');

UPDATE  int_dev.lp_fund_allocator set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'FUNDING_PLACEMENT_STATUS_REPLACED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'FUNDING_PLACEMENT_STATUS_REPLACED');

------- lp_fund_allocator_ -------------
UPDATE  int_dev.lp_fund_allocator_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'FUNDING_PLACEMENT_STATUS_NEW')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'FUNDING_PLACEMENT_STATUS_NEW');

UPDATE  int_dev.lp_fund_allocator_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'FUNDING_PLACEMENT_STATUS_REQUESTED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'FUNDING_PLACEMENT_STATUS_REQUESTED');

UPDATE  int_dev.lp_fund_allocator_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'FUNDING_PLACEMENT_STATUS_FAILED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'FUNDING_PLACEMENT_STATUS_FAILED');

UPDATE  int_dev.lp_fund_allocator_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'FUNDING_PLACEMENT_STATUS_FUNDED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'FUNDING_PLACEMENT_STATUS_FUNDED');

UPDATE  int_dev.lp_fund_allocator_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'FUNDING_PLACEMENT_STATUS_REPLACED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'FUNDING_PLACEMENT_STATUS_REPLACED');


------- lp_fund_allocator_hist -------------
UPDATE  int_dev.lp_fund_allocator_hist set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'FUNDING_PLACEMENT_STATUS_NEW')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'FUNDING_PLACEMENT_STATUS_NEW');

UPDATE  int_dev.lp_fund_allocator_hist set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'FUNDING_PLACEMENT_STATUS_REQUESTED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'FUNDING_PLACEMENT_STATUS_REQUESTED');

UPDATE  int_dev.lp_fund_allocator_hist set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'FUNDING_PLACEMENT_STATUS_FAILED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'FUNDING_PLACEMENT_STATUS_FAILED');

UPDATE  int_dev.lp_fund_allocator_hist set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'FUNDING_PLACEMENT_STATUS_FUNDED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'FUNDING_PLACEMENT_STATUS_FUNDED');

UPDATE  int_dev.lp_fund_allocator_hist set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'FUNDING_PLACEMENT_STATUS_REPLACED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'FUNDING_PLACEMENT_STATUS_REPLACED');


------- lp_fund_allocator_hist_ -------------
UPDATE  int_dev.lp_fund_allocator_hist_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'FUNDING_PLACEMENT_STATUS_NEW')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'FUNDING_PLACEMENT_STATUS_NEW');

UPDATE  int_dev.lp_fund_allocator_hist_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'FUNDING_PLACEMENT_STATUS_REQUESTED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'FUNDING_PLACEMENT_STATUS_REQUESTED');

UPDATE  int_dev.lp_fund_allocator_hist_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'FUNDING_PLACEMENT_STATUS_FAILED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'FUNDING_PLACEMENT_STATUS_FAILED');

UPDATE  int_dev.lp_fund_allocator_hist_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'FUNDING_PLACEMENT_STATUS_FUNDED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'FUNDING_PLACEMENT_STATUS_FUNDED');

UPDATE  int_dev.lp_fund_allocator_hist_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'FUNDING_PLACEMENT_STATUS_REPLACED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'FUNDING_PLACEMENT_STATUS_REPLACED');



------- lp_fac_be -------------
UPDATE  int_dev.lp_fac_be set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'DEALS_STATUS_UNASSIGNED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'DEALS_STATUS_UNASSIGNED');

UPDATE  int_dev.lp_fac_be set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'DEALS_STATUS_ASSIGNED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'DEALS_STATUS_ASSIGNED');

UPDATE  int_dev.lp_fac_be set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'DEALS_STATUS_ACCEPTED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'DEALS_STATUS_ACCEPTED');

UPDATE  int_dev.lp_fac_be set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'DEALS_STATUS_FUNDED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'DEALS_STATUS_FUNDED');

UPDATE  int_dev.lp_fac_be set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'DEALS_STATUS_REJECTED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'DEALS_STATUS_REJECTED');

------- lp_fac_be_ -------------
UPDATE  int_dev.lp_fac_be_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'DEALS_STATUS_UNASSIGNED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'DEALS_STATUS_UNASSIGNED');

UPDATE  int_dev.lp_fac_be_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'DEALS_STATUS_ASSIGNED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'DEALS_STATUS_ASSIGNED');

UPDATE  int_dev.lp_fac_be_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'DEALS_STATUS_ACCEPTED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'DEALS_STATUS_ACCEPTED');

UPDATE  int_dev.lp_fac_be_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'DEALS_STATUS_FUNDED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'DEALS_STATUS_FUNDED');

UPDATE  int_dev.lp_fac_be_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'DEALS_STATUS_REJECTED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'DEALS_STATUS_REJECTED');

------- lp_fac_allocator -------------
UPDATE  int_dev.lp_fac_allocator set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'DEALS_STATUS_UNASSIGNED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'DEALS_STATUS_UNASSIGNED');

UPDATE  int_dev.lp_fac_allocator set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'DEALS_STATUS_ASSIGNED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'DEALS_STATUS_ASSIGNED');

UPDATE  int_dev.lp_fac_allocator set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'DEALS_STATUS_ACCEPTED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'DEALS_STATUS_ACCEPTED');

UPDATE  int_dev.lp_fac_allocator set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'DEALS_STATUS_FUNDED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'DEALS_STATUS_FUNDED');

UPDATE  int_dev.lp_fac_allocator set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'DEALS_STATUS_REJECTED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'DEALS_STATUS_REJECTED');

------- lp_fac_allocator_ -------------
UPDATE  int_dev.lp_fac_allocator_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'DEALS_STATUS_UNASSIGNED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'DEALS_STATUS_UNASSIGNED');

UPDATE  int_dev.lp_fac_allocator_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'DEALS_STATUS_ASSIGNED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'DEALS_STATUS_ASSIGNED');

UPDATE  int_dev.lp_fac_allocator_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'DEALS_STATUS_ACCEPTED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'DEALS_STATUS_ACCEPTED');

UPDATE  int_dev.lp_fac_allocator_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'DEALS_STATUS_FUNDED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'DEALS_STATUS_FUNDED');

UPDATE  int_dev.lp_fac_allocator_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'DEALS_STATUS_REJECTED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'DEALS_STATUS_REJECTED');

------- lp_fac_placement_audit -------------
UPDATE  int_dev.lp_fac_placement_audit set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'DEALS_STATUS_UNASSIGNED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'DEALS_STATUS_UNASSIGNED');

UPDATE  int_dev.lp_fac_placement_audit set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'DEALS_STATUS_ASSIGNED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'DEALS_STATUS_ASSIGNED');

UPDATE  int_dev.lp_fac_placement_audit set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'DEALS_STATUS_ACCEPTED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'DEALS_STATUS_ACCEPTED');

UPDATE  int_dev.lp_fac_placement_audit set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'DEALS_STATUS_FUNDED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'DEALS_STATUS_FUNDED');

UPDATE  int_dev.lp_fac_placement_audit set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status where status_key = 'DEALS_STATUS_REJECTED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'DEALS_STATUS_REJECTED');

------- lp_fac_placement_audit_ -------------
UPDATE  int_dev.lp_fac_placement_audit_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'DEALS_STATUS_UNASSIGNED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'DEALS_STATUS_UNASSIGNED');

UPDATE  int_dev.lp_fac_placement_audit_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'DEALS_STATUS_ASSIGNED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'DEALS_STATUS_ASSIGNED');

UPDATE  int_dev.lp_fac_placement_audit_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'DEALS_STATUS_ACCEPTED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'DEALS_STATUS_ACCEPTED');

UPDATE  int_dev.lp_fac_placement_audit_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'DEALS_STATUS_FUNDED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'DEALS_STATUS_FUNDED');

UPDATE  int_dev.lp_fac_placement_audit_ set wf_status_id = (Select lp_workflow_status_id from int_dev.lp_workflow_status_ where status_key = 'DEALS_STATUS_REJECTED')
WHERE status_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'DEALS_STATUS_REJECTED');

----- drop column status_id --------
ALTER TABLE int_dev.lp_deal DROP COLUMN status_id;
ALTER TABLE int_dev.lp_deal_ DROP COLUMN status_id;
ALTER TABLE int_dev.lp_fund_allocator DROP COLUMN status_id;
ALTER TABLE int_dev.lp_fund_allocator_ DROP COLUMN status_id;
ALTER TABLE int_dev.lp_fund_allocator_hist DROP COLUMN status_id;
ALTER TABLE int_dev.lp_fund_allocator_hist_ DROP COLUMN status_id;
ALTER TABLE int_dev.lp_fac_be DROP COLUMN status_id;
ALTER TABLE int_dev.lp_fac_be_ DROP COLUMN status_id;
ALTER TABLE int_dev.lp_fac_allocator DROP COLUMN status_id;
ALTER TABLE int_dev.lp_fac_allocator_ DROP COLUMN status_id;
ALTER TABLE int_dev.lp_fac_placement_audit DROP COLUMN status_id;
ALTER TABLE int_dev.lp_fac_placement_audit_ DROP COLUMN status_id;