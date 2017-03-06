    create table CORE_SEARCH_FILTER (
        SAVED_SEARCH_ID number(19,0) not null,
        ENTITY_CLASS_NAME varchar2(255 char),
        FILTER_NAME varchar2(255 char),
        GLOBAL_FLAG number(1,0),
        GROUP_NAME varchar2(255 char),
        OWNER varchar2(255 char),
        SEARCH_CRITERIA clob,
        primary key (SAVED_SEARCH_ID)
    );

    create table CORE_SEARCH_FILTER_ (
        SAVED_SEARCH_ID number(19,0) not null,
        REV number(19,0) not null,
        REVTYPE number(3,0),
        ENTITY_CLASS_NAME varchar2(255 char),
        FILTER_NAME varchar2(255 char),
        GLOBAL_FLAG number(1,0),
        GROUP_NAME varchar2(255 char),
        OWNER varchar2(255 char),
        SEARCH_CRITERIA clob,
        primary key (SAVED_SEARCH_ID, REV)
    );

    alter table CORE_SEARCH_FILTER_ 
        add constraint FKC83BBCF0A5CA84FE 
        foreign key (REV) 
        references REV_INFO;