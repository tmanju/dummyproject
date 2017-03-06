ALTER TABLE int_dev.lp_address ADD COLUMN servicing_id character varying(36);

ALTER TABLE int_dev.lp_address_ ADD COLUMN servicing_id character varying(36);

create index IDX_100901 on int_dev.LP_ADDRESS (SERVICING_ID);
create index IDX_1009552 on int_dev.LP_CNTCT (SERVICING_ID);
create index IDX_1000552 on int_dev.LP_ACT (SERVICING_ID);
create index IDX_1002552 on int_dev.LP_FAC (SERVICING_ID);
create index IDX_1003552 on int_dev.LP_DEBTOR_CUST (SERVICING_ID);
create index IDX_1004552 on int_dev.LP_BANK_TRD (SERVICING_ID);
create index IDX_1005552 on int_dev.LP_FAC_PMT (SERVICING_ID);
create index IDX_1006552 on int_dev.LP_DEAL (SERVICING_ID);
create index IDX_1007552 on int_dev.LP_FAC_BE (SERVICING_ID);
create index IDX_100552 on int_dev.LP_CUST (SERVICING_ID);
