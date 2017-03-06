
ALTER TABLE lp_user ADD ACT_EXPIRY_DT date;
ALTER TABLE lp_user ADD LAST_PASSWORD_RST_DT date;
ALTER TABLE lp_user ADD LOCKED number(1,0);
ALTER TABLE lp_user ADD WF_STATUS_ID number(19,0);
ALTER TABLE lp_user ADD SESSION_ID number(19,0);
ALTER TABLE lp_user ADD PROCESS_INSTANCE_ID number(19,0);
ALTER TABLE lp_user MODIFY USERNAME varchar2(255 char) not null;
ALTER TABLE lp_user DROP COLUMN STATUS_ID;

ALTER TABLE lp_user_ ADD ACT_EXPIRY_DT date;
ALTER TABLE lp_user_ ADD LAST_PASSWORD_RST_DT date;
ALTER TABLE lp_user_ ADD LOCKED number(1,0);
ALTER TABLE lp_user_ ADD WF_STATUS_ID number(19,0);
ALTER TABLE lp_user_ ADD SESSION_ID number(19,0);
ALTER TABLE lp_user_ ADD PROCESS_INSTANCE_ID number(19,0);
ALTER TABLE lp_user_ MODIFY USERNAME varchar2(255 char) not null;
ALTER TABLE lp_user_ DROP COLUMN STATUS_ID;

create table LP_LOGIN_HIST (
    LP_LOGIN_HIST_ID number(19,0) not null,
    IP_ADDRESS varchar2(255 char),
    LOGIN_DTTM timestamp,
    LOGOUT_DTTM timestamp,
    VERSION number(10,0),
    primary key (LP_LOGIN_HIST_ID)
);

create table LP_LOGIN_HIST_ (
    LP_LOGIN_HIST_ID number(19,0) not null,
    REV number(19,0) not null,
    REVTYPE number(3,0),
    IP_ADDRESS varchar2(255 char),
    LOGIN_DTTM timestamp,
    LOGOUT_DTTM timestamp,
    primary key (LP_LOGIN_HIST_ID, REV)
);

create table LP_USER_LOGIN_HIST (
    LP_USER_ID number(19,0) not null,
    LP_LOGIN_HIST_ID number(19,0) not null
);

create table LP_USER_LOGIN_HIST_ (
    REV number(19,0) not null,
    LP_USER_ID number(19,0) not null,
    LP_LOGIN_HIST_ID number(19,0) not null,
    REVTYPE number(3,0),
    primary key (REV, LP_USER_ID, LP_LOGIN_HIST_ID)
);
    
alter table LP_LOGIN_HIST_ 
    add constraint FK8AD14ECCA5CA84FE 
    foreign key (REV) 
    references REV_INFO;
        

DROP INDEX IDX_100005;
create index IDX_100005 on LP_USER (WF_STATUS_ID);

alter table LP_USER 
    add constraint FK423FE5A67D5497C7 
    foreign key (WF_STATUS_ID) 
    references LP_WORKFLOW_STATUS;

alter table LP_USER_LOGIN_HIST 
    add constraint FKDDB4A916FF6FBD 
    foreign key (LP_USER_ID) 
    references LP_USER;

alter table LP_USER_LOGIN_HIST 
    add constraint FKDDB4A918B49E990 
    foreign key (LP_LOGIN_HIST_ID) 
    references LP_LOGIN_HIST;

alter table LP_USER_LOGIN_HIST_ 
    add constraint FKAD8E07EEA5CA84FE 
    foreign key (REV) 
    references REV_INFO;

update lp_USER set locked = 0;
update lp_USER_ set locked = 0;

commit;