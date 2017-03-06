	create table CORE_CHANGE_LOG (
        CHANGE_LOG_ID number(19,0) not null,
        ENTITY_CLASS_NAME varchar2(255 char) not null,
        ENTITY_ID number(19,0) not null,
        ENTITY_NAME varchar2(255 char) not null,
        LOG clob,
        revDate timestamp,
        username varchar2(255 char),
        primary key (CHANGE_LOG_ID)
    );

    create index IDX_CORE_CL_ENTITY_ID on CORE_CHANGE_LOG (ENTITY_ID);

    create index IDX_CORE_CL_CLASS_NAME on CORE_CHANGE_LOG (ENTITY_CLASS_NAME);

    create index IDX_CORE_CL_ENTITY_NAME on CORE_CHANGE_LOG (ENTITY_NAME);
