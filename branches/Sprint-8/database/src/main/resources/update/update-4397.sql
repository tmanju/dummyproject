create table lp_oob_suren.LP_QUOTE_PRIC_OPT_TO_REQ_TRAC (
    LP_QUOTE_PRIC_OPT_TO_REQ_TRAC_ID int8 not null,
    VERSION int4,
    PRC_OPTION_ID int8,
    QUOTE_ID int8,
    RQ_ID int8,
    primary key (LP_QUOTE_PRIC_OPT_TO_REQ_TRAC_ID)
);

create table lp_oob_suren.LP_QUOTE_PRIC_OPT_TO_REQ_TRAC_ (
    LP_QUOTE_PRIC_OPT_TO_REQ_TRAC_ID int8 not null,
    REV int8 not null,
    REVTYPE int2,
    PRC_OPTION_ID int8,
    QUOTE_ID int8,
    RQ_ID int8,
    primary key (LP_QUOTE_PRIC_OPT_TO_REQ_TRAC_ID, REV)
);

alter table lp_oob_suren.LP_QUOTE_PRIC_OPT_TO_REQ_TRAC 
    add constraint FK3AA44DD4F762474C 
    foreign key (RQ_ID) 
    references lp_oob_suren.LP_RQ;

alter table lp_oob_suren.LP_QUOTE_PRIC_OPT_TO_REQ_TRAC 
    add constraint FK3AA44DD4FE2C1E5C 
    foreign key (QUOTE_ID) 
    references lp_oob_suren.LP_QUOTE;

alter table lp_oob_suren.LP_QUOTE_PRIC_OPT_TO_REQ_TRAC 
    add constraint FK3AA44DD4D1B22504 
    foreign key (PRC_OPTION_ID) 
    references lp_oob_suren.LP_PRC_OPTION;

alter table lp_oob_suren.LP_QUOTE_PRIC_OPT_TO_REQ_TRAC_ 
    add constraint FK19E56D0BA5CA84FE 
    foreign key (REV) 
    references lp_oob_suren.REV_INFO;

COMMIT;
