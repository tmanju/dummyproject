ALTER TABLE internex.lp_debtor_cust ADD COLUMN cmt_grp_id bigint;

ALTER TABLE internex.lp_debtor_cust
  ADD CONSTRAINT lp_debtor_cust_cmt_grp_id FOREIGN KEY (cmt_grp_id)
      REFERENCES internex.lp_cmt_grp (lp_cmt_grp_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE internex.lp_debtor_cust ADD COLUMN next_review_dt date;
	  
	  ---history-----
ALTER TABLE internex.lp_debtor_cust_ ADD COLUMN cmt_grp_id bigint;
	  
ALTER TABLE internex.lp_debtor_cust_ ADD COLUMN next_review_dt date;