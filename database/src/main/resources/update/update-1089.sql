create table int_dev.CORE_EVENT_HISTORY (
        CORE_EVENT_HISTORY_ID int8 not null,
        CLIENT_IP varchar(255),
        EVENT varchar(255),
        LOGIN_DATE timestamp,
        LOGOUT_DATE timestamp,
        SERVER_NAME varchar(255),
        SESSION_ID varchar(255),
        USERNAME varchar(255),
        primary key (CORE_EVENT_HISTORY_ID)
    );

create table int_dev.CORE_EVENT_HISTORY_ (
        CORE_EVENT_HISTORY_ID int8 not null,
        REV int8 not null,
        REVTYPE int2,
        CLIENT_IP varchar(255),
        EVENT varchar(255),
        LOGIN_DATE timestamp,
        LOGOUT_DATE timestamp,
        SERVER_NAME varchar(255),
        SESSION_ID varchar(255),
        USERNAME varchar(255),
        primary key (CORE_EVENT_HISTORY_ID, REV)
    );

alter table int_dev.CORE_EVENT_HISTORY_ 
        add constraint FK_CORE_EVT_HIST
        foreign key (REV) 
        references int_dev.REV_INFO;
  
insert into int_dev.core_event_history(CORE_EVENT_HISTORY_ID,
        CLIENT_IP,
        LOGIN_DATE,
        SERVER_NAME,
        USERNAME) (select CORE_LOGIN_HISTORY_ID,
        CLIENT_IP,
        LOGIN_DATE,
        SERVER_NAME,
        USERNAME from int_dev.core_login_history); 

create table int_dev.CORE_NOTIF_ATTACHMENT (
        CORE_NOTIF_ID bigint not null,
        CONTENT oid,
        CONTENT_TYPE character varying(255),
        NAME character varying(255),
        NOTIFICATION_ID bigint,
        primary key (CORE_NOTIF_ID)
    );

create table int_dev.CORE_NOTIF_ATTACHMENT_ (
        CORE_NOTIF_ID bigint not null,
        REV bigint not null,
        REVTYPE smallint,
        CONTENT oid,
        CONTENT_TYPE character varying(255),
        NAME character varying(255),
        NOTIFICATION_ID bigint,
        primary key (CORE_NOTIF_ID, REV)
    );
 
 create index IDX_CORE_ATTACHMENT_NOTIF_ID on int_dev.CORE_NOTIF_ATTACHMENT (NOTIFICATION_ID);
 
 
 
 alter table int_dev.lp_attrb_chc ADD COLUMN name varchar(255);
alter table int_dev.lp_attrb_chc_ ADD COLUMN name varchar(255);