ALTER TABLE int_dev.lp_fac_allocator ADD COLUMN coll_base numeric(38,4);
ALTER TABLE int_dev.lp_fac_allocator_ ADD COLUMN coll_base numeric(38,4);

ALTER TABLE int_dev.lp_cp_int_rcd ADD COLUMN coll_base numeric(38,4);
ALTER TABLE int_dev.lp_cp_int_rcd_ ADD COLUMN coll_base numeric(38,4);

create table int_dev.LP_FAC_ALLOCATOR_INT_RCD (
        LP_FAC_ALLOCATOR_INT_RCD_ID int8 not null,
        COLL_BASE numeric(38, 4),
        CP_BLNC numeric(38, 4),
        EARNED_INT numeric(38, 4),
        CREATED_AT timestamp,
        LAST_MODIFIED_AT timestamp,
        REFNUMBER varchar(255),
        SERVICING_FEE numeric(38, 4),
        SOFT_DELETED_ON timestamp,
        UN_USED_LNE_FEE numeric(38, 4),
        VERSION int4,
        FAC_ALLOCATOR_ID int8,
        CREATED_BY_USER_ID int8,
        LAST_MODIFIED_BY_USER_ID int8,
        primary key (LP_FAC_ALLOCATOR_INT_RCD_ID)
    );

    create table int_dev.LP_FAC_ALLOCATOR_INT_RCD_ (
        LP_FAC_ALLOCATOR_INT_RCD_ID int8 not null,
        REV int8 not null,
        REVTYPE int2,
        COLL_BASE numeric(38, 4),
        CP_BLNC numeric(38, 4),
        EARNED_INT numeric(38, 4),
        CREATED_AT timestamp,
        LAST_MODIFIED_AT timestamp,
        REFNUMBER varchar(255),
        SERVICING_FEE numeric(38, 4),
        SOFT_DELETED_ON timestamp,
        UN_USED_LNE_FEE numeric(38, 4),
        FAC_ALLOCATOR_ID int8,
        CREATED_BY_USER_ID int8,
        LAST_MODIFIED_BY_USER_ID int8,
        primary key (LP_FAC_ALLOCATOR_INT_RCD_ID, REV)
    );
	
ALTER TABLE int_dev.LP_FAC_ALLOCATOR_INT_RCD  ADD FAC_FIU numeric(38, 4),ADD GROSS_AR numeric(38, 4),ADD SERVICEFEE_RATE numeric(7, 4), ADD UNUSED_LNE_RATE numeric(7, 4);
ALTER TABLE int_dev.LP_FAC_ALLOCATOR_INT_RCD_  ADD FAC_FIU numeric(38, 4),ADD GROSS_AR numeric(38, 4),ADD SERVICEFEE_RATE numeric(7, 4), ADD UNUSED_LNE_RATE numeric(7, 4);

ALTER TABLE int_dev.LP_FAC_ALLOCATOR_INT_RCD  ADD PLACEMENT_RATE numeric(7, 4);
ALTER TABLE int_dev.LP_FAC_ALLOCATOR_INT_RCD_  ADD PLACEMENT_RATE numeric(7, 4);

ALTER TABLE int_dev.LP_FAC_ALLOCATOR_INT_RCD  ADD INT_CALC_DT timestamp;
ALTER TABLE int_dev.LP_FAC_ALLOCATOR_INT_RCD_  ADD INT_CALC_DT timestamp;