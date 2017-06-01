alter table internex5.LP_DEBTOR_CUST add SYNC_TO_AKRITIV_ID int8;
alter table internex5.LP_DEBTOR_CUST_ add SYNC_TO_AKRITIV_ID int8;

create index IDX_SYNC_TO_AKRITIV_ID on internex5.LP_DEBTOR_CUST (SYNC_TO_AKRITIV_ID);

alter table internex5.LP_DEBTOR_CUST 
        add constraint FK_SYNC_TO_AKRITIV_ID 
        foreign key (SYNC_TO_AKRITIV_ID) 
        references internex5.LP_ATTRB_CHC;


update internex5.LP_DEBTOR_CUST set SYNC_TO_AKRITIV_ID =(select lp_attrb_chc_id from internex5.lp_attrb_chc where key_ ='YES_OR_NO_YES' ) where servicing_id is not null;
update internex5.LP_DEBTOR_CUST set SYNC_TO_AKRITIV_ID =(select lp_attrb_chc_id from internex5.lp_attrb_chc where key_ ='YES_OR_NO_NO' ) where servicing_id is null;