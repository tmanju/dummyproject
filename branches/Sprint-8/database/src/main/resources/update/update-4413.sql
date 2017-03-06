alter table loanpath_oob_dev.LP_QUOTE
    add RQ_ID int8;

alter table loanpath_oob_dev.LP_QUOTE_
    add RQ_ID int8;

alter table loanpath_oob_dev.LP_RQ
    add QUOTE_ID int8;

alter table loanpath_oob_dev.LP_RQ
    add QUOTE_PRIC_OPT_TO_REQ_TRAC_ID int8;

alter table loanpath_oob_dev.LP_RQ_
    add QUOTE_ID int8;

alter table loanpath_oob_dev.LP_RQ_
    add QUOTE_PRIC_OPT_TO_REQ_TRAC_ID int8;

alter table loanpath_oob_dev.LP_QUOTE 
    add constraint FK5857FE1F762474C 
    foreign key (RQ_ID) 
    references loanpath_oob_dev.LP_RQ;

alter table loanpath_oob_dev.LP_RQ 
    add constraint FK454C6DAFE2C1E5C 
    foreign key (QUOTE_ID) 
    references loanpath_oob_dev.LP_QUOTE;

alter table loanpath_oob_dev.LP_RQ 
    add constraint FK454C6DA281B16EB 
    foreign key (QUOTE_PRIC_OPT_TO_REQ_TRAC_ID) 
    references loanpath_oob_dev.LP_QUOTE_PRIC_OPT_TO_REQ_TRAC;

create table loanpath_oob_dev.LP_AST_PARTNERS (
    LP_AST_ID int8 not null,
    LP_PRT_ID int8 not null
);

create table loanpath_oob_dev.LP_AST_PARTNERS_ (
    REV int8 not null,
    LP_AST_ID int8 not null,
    LP_PRT_ID int8 not null,
    REVTYPE int2,
    primary key (REV, LP_AST_ID, LP_PRT_ID)
);


alter table loanpath_oob_dev.LP_AST_PARTNERS 
    add constraint FKA255E4C37E933C25 
    foreign key (LP_AST_ID) 
    references loanpath_oob_dev.LP_AST;

alter table loanpath_oob_dev.LP_AST_PARTNERS 
    add constraint FKA255E4C3E6C2C74D 
    foreign key (LP_PRT_ID) 
    references loanpath_oob_dev.LP_PRT;

alter table loanpath_oob_dev.LP_AST_PARTNERS_ 
    add constraint FKA866B3FCA5CA84FE 
    foreign key (REV) 
    references loanpath_oob_dev.REV_INFO;

