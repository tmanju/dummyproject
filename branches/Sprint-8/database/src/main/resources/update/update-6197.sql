alter table lp_real_estate_coll add num_of_unt_csf number(38,4);
alter table lp_real_estate_coll_ add num_of_unt_csf number(38,4);

insert into lp_workflow_status(LP_WORKFLOW_STATUS_ID,TRANSIENT,SEQUENCE,STATUS_KEY,VAL,VERSION,WORKFLOW_ENTITY) values
((select max(LP_WORKFLOW_STATUS_ID)+1 from lp_workflow_status) ,0,29,'REQUEST_STATUS_PENDING_APPLICATION_REVIEW','Pending - Application Review',0,'WORKFLOW_ENTITY_REQUEST');

insert into lp_workflow_status_ (LP_WORKFLOW_STATUS_ID,REV,REVTYPE,TRANSIENT,SEQUENCE,STATUS_KEY,VAL,WORKFLOW_ENTITY) values
((select max(LP_WORKFLOW_STATUS_ID)+1 from lp_workflow_status) ,100007,0,0,29,'REQUEST_STATUS_PENDING_APPLICATION_REVIEW','Pending - Application Review','WORKFLOW_ENTITY_REQUEST');