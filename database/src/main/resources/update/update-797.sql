create table int_dev.LP_FAC_ALLOCATOR_TRANSACTIONS (
        LP_FAC_ALLOCATOR_ID int8 not null,
        LP_FAC_ALLOCATOR_TXN_ID int8 not null,
        unique (LP_FAC_ALLOCATOR_TXN_ID)
    );

    create table int_dev.LP_FAC_ALLOCATOR_TRANSACTIONS_ (
        REV int8 not null,
        LP_FAC_ALLOCATOR_ID int8 not null,
        LP_FAC_ALLOCATOR_TXN_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_FAC_ALLOCATOR_ID, LP_FAC_ALLOCATOR_TXN_ID)
    );

    create table int_dev.LP_FAC_ALLOCATOR_TXN (
        LP_FAC_ALLOCATOR_TXN_ID int8 not null,
        CREATED_AT timestamp,
        LAST_MODIFIED_AT timestamp,
        PAID_INT numeric(38, 4),
        PAID_SERVICING_FEE numeric(38, 4),
        PAID_UNUSED_FEE numeric(38, 4),
        PROCESS_INSTANCE_ID int8,
        REF_NUM varchar(255),
        SESSION_ID int8,
        SOFT_DELETED_ON timestamp,
        VERSION int4,
        FAC_ALLOCATOR_ID int8,
        CREATED_BY_USER_ID int8,
        LAST_MODIFIED_BY_USER_ID int8,
        WF_STATUS_ID int8,
        primary key (LP_FAC_ALLOCATOR_TXN_ID)
    );

    create table int_dev.LP_FAC_ALLOCATOR_TXN_ (
        LP_FAC_ALLOCATOR_TXN_ID int8 not null,
        REV int8 not null,
        REVTYPE int2,
        CREATED_AT timestamp,
        LAST_MODIFIED_AT timestamp,
        PAID_INT numeric(38, 4),
        PAID_SERVICING_FEE numeric(38, 4),
        PAID_UNUSED_FEE numeric(38, 4),
        PROCESS_INSTANCE_ID int8,
        REF_NUM varchar(255),
        SESSION_ID int8,
        SOFT_DELETED_ON timestamp,
        FAC_ALLOCATOR_ID int8,
        CREATED_BY_USER_ID int8,
        LAST_MODIFIED_BY_USER_ID int8,
        WF_STATUS_ID int8,
        primary key (LP_FAC_ALLOCATOR_TXN_ID, REV)
    );
	
	alter table int_dev.LP_FAC_ALLOCATOR_TRANSACTIONS 
        add constraint LP_FAC_ALLOCATOR_TRANSACTIONS_TXN_ID 
        foreign key (LP_FAC_ALLOCATOR_TXN_ID) 
        references int_dev.LP_FAC_ALLOCATOR_TXN;
		
	create index LP_FAC_ALLOCATOR_TXN_CBU on int_dev.LP_FAC_ALLOCATOR_TXN (CREATED_BY_USER_ID);

    create index LP_FAC_ALLOCATOR_TXN_LMU on int_dev.LP_FAC_ALLOCATOR_TXN (LAST_MODIFIED_BY_USER_ID);

    create index LP_FAC_ALLOCATOR_TXN_FID on int_dev.LP_FAC_ALLOCATOR_TXN (FAC_ALLOCATOR_ID);

    create index LP_FAC_ALLOCATOR_TXN_ST on int_dev.LP_FAC_ALLOCATOR_TXN (WF_STATUS_ID);

    create index LP_FAC_ALLOCATOR_TXN_SD on int_dev.LP_FAC_ALLOCATOR_TXN (SOFT_DELETED_ON);

    alter table int_dev.LP_FAC_ALLOCATOR_TXN 
        add constraint LP_FAC_ALLOCATOR_TXN_CR 
        foreign key (CREATED_BY_USER_ID) 
        references int_dev.LP_USER;

    alter table int_dev.LP_FAC_ALLOCATOR_TXN 
        add constraint LP_FAC_ALLOCATOR_TXN_MOD 
        foreign key (LAST_MODIFIED_BY_USER_ID) 
        references int_dev.LP_USER;

    alter table int_dev.LP_FAC_ALLOCATOR_TXN 
        add constraint LP_FAC_ALLOCATOR_TXN_WF_STATUS_ID 
        foreign key (WF_STATUS_ID) 
        references int_dev.LP_WORKFLOW_STATUS;

    alter table int_dev.LP_FAC_ALLOCATOR_TXN 
        add constraint LP_FAC_ALLOCATOR_TXN_FAC_ALLOCATOR_ID 
        foreign key (FAC_ALLOCATOR_ID) 
        references int_dev.LP_FAC_ALLOCATOR;

    alter table int_dev.LP_FAC_ALLOCATOR_TXN_ 
        add constraint LP_FAC_ALLOCATOR_TXN_REV_INFO 
        foreign key (REV) 
        references int_dev.REV_INFO;
		
	create index IDX_LP_FAC_ALLOCATOR_TRANSACTIONS_2 on int_dev.LP_FAC_ALLOCATOR_TRANSACTIONS (LP_FAC_ALLOCATOR_TXN_ID);
	
	ALTER TABLE int_dev.LP_CUST_BACKEND ADD INT_EARNED numeric(38, 4), ADD SERVICING_FEE_ACCURED numeric(38, 4), ADD UNUSED_LNE_FEE_ACCURED numeric(38, 4), ADD INT_PAID_MTD numeric(38, 4), ADD INT_PAID_YTD numeric(38, 4), ADD INT_PAID_LTD numeric(38, 4),ADD UNUSED_FEE_PAID_MTD numeric(38, 4), ADD UNUSED_FEE_PAID_YTD numeric(38, 4),ADD UNUSED_FEE_PAID_LTD numeric(38, 4);
	
	ALTER TABLE int_dev.LP_CUST_BACKEND_ ADD INT_EARNED numeric(38, 4), ADD SERVICING_FEE_ACCURED numeric(38, 4), ADD UNUSED_LNE_FEE_ACCURED numeric(38, 4), ADD INT_PAID_MTD numeric(38, 4), ADD INT_PAID_YTD numeric(38, 4), ADD INT_PAID_LTD numeric(38, 4),ADD UNUSED_FEE_PAID_MTD numeric(38, 4), ADD UNUSED_FEE_PAID_YTD numeric(38, 4),ADD UNUSED_FEE_PAID_LTD numeric(38, 4);
	
	