alter table internex5.LP_DEBTOR_CUST add INELIGIBILITY_REASON_ID int8;
alter table internex5.LP_DEBTOR_CUST_ add INELIGIBILITY_REASON_ID int8;


create index IDX_INELIGIBILITY_REASON_ID on internex5.LP_DEBTOR_CUST (INELIGIBILITY_REASON_ID);

alter table internex5.LP_DEBTOR_CUST 
        add constraint FK_INELIGIBILITY_REASON_ID
        foreign key (INELIGIBILITY_REASON_ID) 
        references internex5.LP_ATTRB_CHC;
