create table int_dev.LP_SRV_MESSAGE (
        LP_SRV_MESSAGE_ID int8 not null,
        MESSAGE varchar(255),
        SERVICING_ID varchar(255),
        SOFT_DELETED_ON timestamp,
        SOURCE_SYS varchar(255),
        STATUS varchar(255),
        VERSION int4,
        primary key (LP_SRV_MESSAGE_ID)
    );

    create table int_dev.LP_SRV_MESSAGE_ (
        LP_SRV_MESSAGE_ID int8 not null,
        REV int8 not null,
        REVTYPE int2,
        MESSAGE varchar(255),
        SERVICING_ID varchar(255),
        SOFT_DELETED_ON timestamp,
        SOURCE_SYS varchar(255),
        STATUS varchar(255),
        primary key (LP_SRV_MESSAGE_ID, REV)
    );

    create index IDX_SRV_MESSAGES_ID_ on int_dev.LP_SRV_MESSAGE (SOFT_DELETED_ON);
    alter table int_dev.LP_SRV_MESSAGE_ 
        add constraint FK1SRV_MESSAGES_ID_ 
        foreign key (REV) 
        references int_dev.REV_INFO;


create table int_dev.LP_RQ_SRV_MESSAGES (
        LP_RQ_ID int8 not null,
        LP_SRV_MESSAGE_ID int8 not null,
        unique (LP_SRV_MESSAGE_ID)
    );

    create table int_dev.LP_RQ_SRV_MESSAGES_ (
        REV int8 not null,
        LP_RQ_ID int8 not null,
        LP_SRV_MESSAGE_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_RQ_ID, LP_SRV_MESSAGE_ID)
    );

     alter table int_dev.LP_RQ_SRV_MESSAGES 
        add constraint FKLP_SRV_MESSAGE_ID 
        foreign key (LP_SRV_MESSAGE_ID) 
        references int_dev.LP_SRV_MESSAGE;

    alter table int_dev.LP_RQ_SRV_MESSAGES 
        add constraint FKLP_RQ_ID 
        foreign key (LP_RQ_ID) 
        references int_dev.LP_RQ;

alter table int_dev.LP_RQ_SRV_MESSAGES_ 
        add constraint FKREV_INFO 
        foreign key (REV) 
        references int_dev.REV_INFO;
        

create index IDX_LP_RQ_SRV_MESSAGES_1 on int_dev.LP_RQ_SRV_MESSAGES (LP_RQ_ID);
create index IDX_LP_RQ_SRV_MESSAGES_2 on int_dev.LP_RQ_SRV_MESSAGES (LP_SRV_MESSAGE_ID);