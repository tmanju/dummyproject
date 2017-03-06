alter table lp_real_estate_coll drop column num_of_unt_csf;
alter table lp_real_estate_coll_ drop column num_of_unt_csf;

delete from lp_workflow_status where status_key='REQUEST_STATUS_PENDING_APPLICATION_REVIEW';
delete from lp_workflow_status_ where status_key='REQUEST_STATUS_PENDING_APPLICATION_REVIEW';