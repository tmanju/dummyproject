ALTER TABLE int_dev.lp_cust ADD COLUMN PUBLIC_CMP boolean default false;
ALTER TABLE int_dev.lp_cust_ ADD COLUMN PUBLIC_CMP boolean default false;


create table int_dev.LP_BUREAU_DATA (
        LP_BUREAU_DATA_ID int8 not null,
        CMP_DBT int4,
        DNB_CDT_SCORE int4,
        DNB_DATA_DEPTH_IND varchar(255),
        DNB_VIABILITY_RATING int4,
        IDST_DBT int4,
        SOFT_DELETED_ON timestamp,
        VERSION int4,
        AGENCY_RATING_ID int8,
        primary key (LP_BUREAU_DATA_ID)
    );

    create table int_dev.LP_BUREAU_DATA_ (
        LP_BUREAU_DATA_ID int8 not null,
        REV int8 not null,
        REVTYPE int2,
        CMP_DBT int4,
        DNB_CDT_SCORE int4,
        DNB_DATA_DEPTH_IND varchar(255),
        DNB_VIABILITY_RATING int4,
        IDST_DBT int4,
        SOFT_DELETED_ON timestamp,
        AGENCY_RATING_ID int8,
        primary key (LP_BUREAU_DATA_ID, REV)
    );
	
	create table int_dev.LP_CUST_BUREAU_DATA (
        LP_CUST_ID int8 not null,
        LP_BUREAU_DATA_ID int8 not null,
        unique (LP_BUREAU_DATA_ID)
    );

    create table int_dev.LP_CUST_BUREAU_DATA_ (
        REV int8 not null,
        LP_CUST_ID int8 not null,
        LP_BUREAU_DATA_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_CUST_ID, LP_BUREAU_DATA_ID)
    );
	
	    alter table int_dev.LP_BUREAU_DATA 
        add constraint FK73872614D9E7EE42 
        foreign key (AGENCY_RATING_ID) 
        references int_dev.LP_ATTRB_CHC;

    alter table int_dev.LP_BUREAU_DATA_ 
        add constraint FKFD5D9CCBA5CA84FE 
        foreign key (REV) 
        references int_dev.REV_INFO;