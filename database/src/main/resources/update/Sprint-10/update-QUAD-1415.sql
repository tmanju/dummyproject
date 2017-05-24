alter table internex5.LP_DEBTOR_CUST add DELETED_ID int8;
alter table internex5.LP_DEBTOR_CUST_ add DELETED_ID int8;

create index IDX_LP_DEBTOR_CUST_DELETED_ID on internex5.LP_DEBTOR_CUST (DELETED_ID);

alter table internex5.LP_DEBTOR_CUST 
        add constraint FK_LP_DEBTOR_CUST_DELETED_ID
        foreign key (DELETED_ID) 
        references internex5.LP_ATTRB_CHC;

update internex5.LP_DEBTOR_CUST set DELETED_ID =(select lp_attrb_chc_id from internex5.lp_attrb_chc where key_ ='YES_OR_NO_NO' );
