alter table intg2.LP_PARTY_DBA add column DEBTOR_CUST_ID int8;
alter table intg2.LP_PARTY_DBA_ add column DEBTOR_CUST_ID int8;

ALTER TABLE intg2.LP_PARTY_DBA ADD COLUMN servicing_id character varying(255);
ALTER TABLE intg2.LP_PARTY_DBA_ ADD COLUMN servicing_id character varying(255);

ALTER TABLE intg2.LP_PARTY_DBA ADD COLUMN ref_num character varying(255);
ALTER TABLE intg2.LP_PARTY_DBA_ ADD COLUMN ref_num character varying(255);

create table intg2.LP_CUST_DBAS (
        LP_CUST_ID int8 not null,
        LP_PARTY_DBA_ID int8 not null
    );

    create table intg2.LP_CUST_DBAS_ (
        REV int8 not null,
        LP_CUST_ID int8 not null,
        LP_PARTY_DBA_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_CUST_ID, LP_PARTY_DBA_ID)
    );

-- Sequence for (entity.[method]):null
create sequence intg2.LP_PARTY_DBA_REF_NUM_SEQ start 100000 increment 1;
