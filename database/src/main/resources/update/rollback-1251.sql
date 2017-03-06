
ALTER TABLE internex.lp_debtor_cust DROP CONSTRAINT lp_debtor_cust_cmt_grp_id;
ALTER TABLE internex.lp_debtor_cust DROP COLUMN cmt_grp_id;
ALTER TABLE internex.lp_debtor_cust DROP COLUMN next_review_dt;

--history--
ALTER TABLE internex.lp_debtor_cust_ DROP COLUMN cmt_grp_id;
ALTER TABLE internex.lp_debtor_cust_ DROP COLUMN next_review_dt;