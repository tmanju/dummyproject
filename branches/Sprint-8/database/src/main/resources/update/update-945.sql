create table int_dev.LP_DEAL_SRV_MESSAGES (
        LP_DEAL_ID int8 not null,
        LP_SRV_MESSAGE_ID int8 not null,
        unique (LP_SRV_MESSAGE_ID)
    );

create table int_dev.LP_DEAL_SRV_MESSAGES_ (
        REV int8 not null,
        LP_DEAL_ID int8 not null,
        LP_SRV_MESSAGE_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_DEAL_ID, LP_SRV_MESSAGE_ID)
    );
	
alter table int_dev.LP_DEAL_SRV_MESSAGES 
        add constraint FKELP_SRV_MESSAGE_ID 
        foreign key (LP_SRV_MESSAGE_ID) 
        references int_dev.LP_SRV_MESSAGE;

alter table int_dev.LP_DEAL_SRV_MESSAGES 
        add constraint FKELP_DEAL_SRV_MESSAGES_FLP_DEAL_ID 
        foreign key (LP_DEAL_ID) 
        references int_dev.LP_DEAL;
		
alter table int_dev.LP_DEAL_SRV_MESSAGES_ 
        add constraint FKFLP_DEAL_SRV_MESSAGES_REV
        foreign key (REV) 
        references int_dev.REV_INFO;

create index IDX_LP_DEAL_SRV_MESSAGES_2_LP_SRV_MESSAGE_ID on int_dev.LP_DEAL_SRV_MESSAGES (LP_SRV_MESSAGE_ID);
	
		create index IDX_LP_DEAL_SRV_MESSAGES_1_LP_DEAL_ID on int_dev.LP_DEAL_SRV_MESSAGES (LP_DEAL_ID);
		
		
create table int_dev.LP_FAC_BE_SRV_MESSAGES (
        LP_FAC_BE_ID int8 not null,
        LP_SRV_MESSAGE_ID int8 not null,
        unique (LP_SRV_MESSAGE_ID)
    );

create table int_dev.LP_FAC_BE_SRV_MESSAGES_ (
        REV int8 not null,
        LP_FAC_BE_ID int8 not null,
        LP_SRV_MESSAGE_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_FAC_BE_ID, LP_SRV_MESSAGE_ID)
    );
	
alter table int_dev.LP_FAC_BE_SRV_MESSAGES 
        add constraint FKB2LP_FAC_BE_SRV_MESSAGES 
        foreign key (LP_FAC_BE_ID) 
        references int_dev.LP_FAC_BE;

 alter table int_dev.LP_FAC_BE_SRV_MESSAGES 
        add constraint FKB2LP_FAC_BE_SRV_MESSAGES_LP_SRV_MESSAGE_ID 
        foreign key (LP_SRV_MESSAGE_ID) 
        references int_dev.LP_SRV_MESSAGE;
		
alter table int_dev.LP_FAC_BE_SRV_MESSAGES_ 
        add constraint FKALP_FAC_BE_SRV_MESSAGES_
        foreign key (REV) 
        references int_dev.REV_INFO;
		
create index IDX_LP_FAC_BE_SRV_MESSAGES_2_LP_FAC_BE_SRV_MESSAGES on int_dev.LP_FAC_BE_SRV_MESSAGES (LP_SRV_MESSAGE_ID);
		
create index IDX_LP_FAC_BE_SRV_MESSAGES_1_LP_FAC_BE_ID on int_dev.LP_FAC_BE_SRV_MESSAGES (LP_FAC_BE_ID);