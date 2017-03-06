    create table int_dev.LP_PASSWORD_RST_HIST (
        LP_PASSWORD_RST_HIST_ID int8 not null,
        CREATED_AT timestamp,
        LAST_MODIFIED_AT timestamp,
        PASSWORD_ varchar(255),
        REF_NUM varchar(255),
        SOFT_DELETED_ON timestamp,
        USER_NAME varchar(255),
        VERSION int4,
        CREATED_BY_USER_ID int8,
        LAST_MODIFIED_BY_USER_ID int8,
        primary key (LP_PASSWORD_RST_HIST_ID)
    );

    create table int_dev.LP_PASSWORD_RST_HIST_ (
        LP_PASSWORD_RST_HIST_ID int8 not null,
        REV int8 not null,
        REVTYPE int2,
        CREATED_AT timestamp,
        LAST_MODIFIED_AT timestamp,
        PASSWORD_ varchar(255),
        REF_NUM varchar(255),
        SOFT_DELETED_ON timestamp,
        USER_NAME varchar(255),
        CREATED_BY_USER_ID int8,
        LAST_MODIFIED_BY_USER_ID int8,
        primary key (LP_PASSWORD_RST_HIST_ID, REV)
    );
	
	
	
    create index IDX_LP_PASSWORD_RST_HIST_CREATED_BY_USER_ID on int_dev.LP_PASSWORD_RST_HIST (CREATED_BY_USER_ID);

    create index IDX_LP_PASSWORD_RST_HIST_LAST_MODIFIED_BY_USER_ID on int_dev.LP_PASSWORD_RST_HIST (LAST_MODIFIED_BY_USER_ID);

    create index IDX_LP_PASSWORD_RST_HIST_SOFT_DELETED_ON on int_dev.LP_PASSWORD_RST_HIST (SOFT_DELETED_ON);

    alter table int_dev.LP_PASSWORD_RST_HIST 
        add constraint FK_CREATED_BY_USER_ID 
        foreign key (CREATED_BY_USER_ID) 
        references int_dev.LP_USER;

    alter table int_dev.LP_PASSWORD_RST_HIST 
        add constraint FK_LP_PASSWORD_RST_HIST
        foreign key (LAST_MODIFIED_BY_USER_ID) 
        references int_dev.LP_USER;

    alter table int_dev.LP_PASSWORD_RST_HIST_ 
        add constraint FK_REV 
        foreign key (REV) 
        references int_dev.REV_INFO;