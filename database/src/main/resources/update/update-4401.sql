    create table loanpath_oob_dev.LP_QUOTE_PARTNERS (
        LP_QUOTE_ID int8 not null,
        LP_PRT_ID int8 not null
    );

    create table loanpath_oob_dev.LP_QUOTE_PARTNERS_ (
        REV int8 not null,
        LP_QUOTE_ID int8 not null,
        LP_PRT_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_QUOTE_ID, LP_PRT_ID)
    );

    alter table loanpath_oob_dev.LP_QUOTE_PARTNERS 
        add constraint FKFEAFAB49B42E36B7 
        foreign key (LP_QUOTE_ID) 
        references loanpath_oob_dev.LP_QUOTE;

    alter table loanpath_oob_dev.LP_QUOTE_PARTNERS 
        add constraint FKFEAFAB49E6C2C74D 
        foreign key (LP_PRT_ID) 
        references loanpath_oob_dev.LP_PRT;

    alter table loanpath_oob_dev.LP_QUOTE_PARTNERS_ 
        add constraint FKD745BE36A5CA84FE 
        foreign key (REV) 
        references loanpath_oob_dev.REV_INFO;
        
