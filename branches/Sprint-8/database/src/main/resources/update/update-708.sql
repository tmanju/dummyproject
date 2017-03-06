create table int_dev.LP_CP_INT_RCD (
        LP_CP_INT_RCD_ID int8 not null,
        DAILY_INT_ACCRUAL numeric(38, 4),
        EARNED_INT_LTD numeric(38, 4),
        EARNED_INT_MTD numeric(38, 4),
        EARNED_INT_YTD numeric(38, 4),
        CREATED_AT timestamp,
        LAST_MODIFIED_AT timestamp,
        SERVICING_FEE numeric(38, 4),
        SERVICING_FEE_LTD numeric(38, 4),
        SERVICING_FEE_MTD numeric(38, 4),
        SERVICING_FEE_YTD numeric(38, 4),
        SOFT_DELETED_ON timestamp,
        UNUSED_LNE_FEE numeric(38, 4),
        UNUSED_LNE_FEE_LTD numeric(38, 4),
        UNUSED_LNE_FEE_MTD numeric(38, 4),
        UNUSED_LNE_FEE_YTD numeric(38, 4),
        VERSION int4,
        CUST_BACKEND_ID int8,
        CREATED_BY_USER_ID int8,
        LAST_MODIFIED_BY_USER_ID int8,
        primary key (LP_CP_INT_RCD_ID)
    );

    create table int_dev.LP_CP_INT_RCD_ (
        LP_CP_INT_RCD_ID int8 not null,
        REV int8 not null,
        REVTYPE int2,
        DAILY_INT_ACCRUAL numeric(38, 4),
        EARNED_INT_LTD numeric(38, 4),
        EARNED_INT_MTD numeric(38, 4),
        EARNED_INT_YTD numeric(38, 4),
        CREATED_AT timestamp,
        LAST_MODIFIED_AT timestamp,
        SERVICING_FEE numeric(38, 4),
        SERVICING_FEE_LTD numeric(38, 4),
        SERVICING_FEE_MTD numeric(38, 4),
        SERVICING_FEE_YTD numeric(38, 4),
        SOFT_DELETED_ON timestamp,
        UNUSED_LNE_FEE numeric(38, 4),
        UNUSED_LNE_FEE_LTD numeric(38, 4),
        UNUSED_LNE_FEE_MTD numeric(38, 4),
        UNUSED_LNE_FEE_YTD numeric(38, 4),
        CUST_BACKEND_ID int8,
        CREATED_BY_USER_ID int8,
        LAST_MODIFIED_BY_USER_ID int8,
        primary key (LP_CP_INT_RCD_ID, REV)
    );
	
	
	create table int_dev.LP_FAC_ALLOCATOR_HIST (
        LP_FAC_ALLOCATOR_HIST_ID int8 not null,
        CP_BLNC numeric(38, 4),
        EARNED_INT numeric(38, 4),
        CREATED_AT timestamp,
        LAST_MODIFIED_AT timestamp,
        REF_NUM varchar(255),
        SERVICING_FEE numeric(38, 4),
        SOFT_DELETED_ON timestamp,
        UN_USED_LNE_FEE numeric(38, 4),
        VERSION int4,
        FAC_ALLOCATOR_ID int8,
        CREATED_BY_USER_ID int8,
        LAST_MODIFIED_BY_USER_ID int8,
        primary key (LP_FAC_ALLOCATOR_HIST_ID)
    );

    create table int_dev.LP_FAC_ALLOCATOR_HIST_ (
        LP_FAC_ALLOCATOR_HIST_ID int8 not null,
        REV int8 not null,
        REVTYPE int2,
        CP_BLNC numeric(38, 4),
        EARNED_INT numeric(38, 4),
        CREATED_AT timestamp,
        LAST_MODIFIED_AT timestamp,
        REF_NUM varchar(255),
        SERVICING_FEE numeric(38, 4),
        SOFT_DELETED_ON timestamp,
        UN_USED_LNE_FEE numeric(38, 4),
        FAC_ALLOCATOR_ID int8,
        CREATED_BY_USER_ID int8,
        LAST_MODIFIED_BY_USER_ID int8,
        primary key (LP_FAC_ALLOCATOR_HIST_ID, REV)
    );
	
	ALTER TABLE int_dev.LP_FAC_ALLOCATOR ADD COLUMN DAILY_INT_ACCRUAL numeric(38, 4);
	ALTER TABLE int_dev.LP_FAC_ALLOCATOR_ ADD COLUMN DAILY_INT_ACCRUAL numeric(38, 4);
	
	ALTER TABLE int_dev.LP_FAC_ALLOCATOR ADD COLUMN SERVICING_FEE_COLL numeric(38, 4);
	ALTER TABLE int_dev.LP_FAC_ALLOCATOR_ ADD COLUMN SERVICING_FEE_COLL numeric(38, 4);
		
	ALTER TABLE int_dev.LP_FAC_ALLOCATOR ADD COLUMN UNUSED_LNE_FEE numeric(38, 4);
	ALTER TABLE int_dev.LP_FAC_ALLOCATOR_ ADD COLUMN UNUSED_LNE_FEE numeric(38, 4);
	
	ALTER TABLE int_dev.LP_FAC_ALLOCATOR ADD COLUMN UNUSED_LNE_RATE numeric(7, 4);
	ALTER TABLE int_dev.LP_FAC_ALLOCATOR_ ADD COLUMN UNUSED_LNE_RATE numeric(7, 4);
	
	ALTER TABLE int_dev.lp_fac_allocator_ ADD COLUMN int_calc_dt date;
	ALTER TABLE int_dev.lp_fac_allocator ADD COLUMN int_calc_dt date;
	
	ALTER TABLE int_dev.LP_FAC_ALLOCATOR_HIST ADD STLM_AMT numeric(38, 4);
	ALTER TABLE int_dev.LP_FAC_ALLOCATOR_HIST_ ADD STLM_AMT numeric(38, 4);
	
	ALTER TABLE int_dev.lp_cust_backend ADD COLUMN int_calc_dt date;
	ALTER TABLE int_dev.lp_cust_backend_ ADD COLUMN int_calc_dt date;
	
	ALTER TABLE int_dev.lp_cp_int_rcd ADD COLUMN cust_backend_id bigint;
	ALTER TABLE int_dev.lp_cp_int_rcd_ ADD COLUMN cust_backend_id bigint;
	
	ALTER TABLE int_dev.LP_CP_INT_RCD ADD SERVICING_FEE numeric(38, 4), ADD SERVICING_FEE_LTD numeric(38, 4), ADD SERVICING_FEE_MTD numeric(38, 4), ADD SERVICING_FEE_YTD numeric(38, 4);
	ALTER TABLE int_dev.LP_CP_INT_RCD_ ADD SERVICING_FEE numeric(38, 4), ADD SERVICING_FEE_LTD numeric(38, 4), ADD SERVICING_FEE_MTD numeric(38, 4), ADD SERVICING_FEE_YTD numeric(38, 4);
	
	ALTER TABLE int_dev.LP_CP_INT_RCD ADD UNUSED_LNE_FEE numeric(38, 4), ADD UNUSED_LNE_FEE_LTD numeric(38, 4), ADD UNUSED_LNE_FEE_MTD numeric(38, 4), ADD UNUSED_LNE_FEE_YTD numeric(38, 4);
	ALTER TABLE int_dev.LP_CP_INT_RCD_ ADD UNUSED_LNE_FEE numeric(38, 4), ADD UNUSED_LNE_FEE_LTD numeric(38, 4), ADD UNUSED_LNE_FEE_MTD numeric(38, 4), ADD UNUSED_LNE_FEE_YTD numeric
	(38, 4);
	
	ALTER TABLE int_dev.LP_CP_INT_RCD ADD COLUMN int_calc_dt date;
	ALTER TABLE int_dev.LP_CP_INT_RCD_ ADD COLUMN int_calc_dt date;
	
	ALTER TABLE int_dev.lp_fac_be ADD COLUMN GROSS_AR numeric(38, 4);
	ALTER TABLE int_dev.lp_fac_be_ ADD COLUMN GROSS_AR numeric(38, 4);
	
		
	ALTER TABLE int_dev.lp_cust_backend ADD UNUSED_LNE_FEE_LTD numeric(38, 4), ADD UNUSED_LNE_FEE_MTD numeric(38, 4), ADD UNUSED_LNE_FEE_YTD numeric(38, 4);
	ALTER TABLE int_dev.lp_cust_backend_ ADD UNUSED_LNE_FEE_LTD numeric(38, 4),ADD UNUSED_LNE_FEE_MTD numeric(38, 4), ADD UNUSED_LNE_FEE_YTD numeric(38, 4);
	
	
	alter table int_dev.LP_CP_INT_RCD 
        add constraint LP_CP_INT_RCD_LP_CP 
        foreign key (CAPITAL_PROVIDER_ID) 
        references int_dev.LP_CAPITAL_PROVIDER;
		
	alter table int_dev.LP_CP_INT_RCD 
        add constraint LP_CP_INT_RCD_CUST_BACKEND_ID 
        foreign key (CUST_BACKEND_ID) 
        references int_dev.LP_CUST_BACKEND;
	
	create index LP_CP_INT_RCD_SFT_DE on int_dev.LP_CP_INT_RCD (SOFT_DELETED_ON);

    create index LP_CP_INT_RCD_CP on int_dev.LP_CP_INT_RCD (CAPITAL_PROVIDER_ID);

    create index LP_CP_INT_RCD_CB on int_dev.LP_CP_INT_RCD (CUST_BACKEND_ID);

    create index LP_CP_INT_RCD_MOD on int_dev.LP_CP_INT_RCD (LAST_MODIFIED_BY_USER_ID);

    create index LP_CP_INT_RCD_CR on int_dev.LP_CP_INT_RCD (CREATED_BY_USER_ID);

    create index LP_FAC_ALLOCATOR_HIST_MOD on int_dev.LP_FAC_ALLOCATOR_HIST (LAST_MODIFIED_BY_USER_ID);

    create index LP_FAC_ALLOCATOR_HIST_SD on int_dev.LP_FAC_ALLOCATOR_HIST (SOFT_DELETED_ON);

    create index CREATED_BY_USER_ID_CB on int_dev.LP_FAC_ALLOCATOR_HIST (CREATED_BY_USER_ID);

    create index LP_FAC_ALLOCATOR_HIST_FA on int_dev.LP_FAC_ALLOCATOR_HIST (FAC_ALLOCATOR_ID);
