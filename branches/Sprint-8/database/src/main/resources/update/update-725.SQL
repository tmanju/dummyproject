ALTER TABLE int_dev.lp_cust ADD COLUMN salutation character varying(255);
ALTER TABLE int_dev.lp_cust_ ADD COLUMN salutation character varying(255);

----lp_cust-----
UPDATE  int_dev.lp_cust set salutation = (Select val from int_dev.lp_attrb_chc a,int_dev.lp_cust b where b.salutation_id = a.lp_attrb_chc_id) 
where salutation_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'SALUTATION_KEY_MS');

UPDATE  int_dev.lp_cust set salutation = (Select val from int_dev.lp_attrb_chc a,int_dev.lp_cust b where b.salutation_id = a.lp_attrb_chc_id) 
where salutation_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'SALUTATION_KEY_MISS');

UPDATE  int_dev.lp_cust set salutation = (Select val from int_dev.lp_attrb_chc a,int_dev.lp_cust b where b.salutation_id = a.lp_attrb_chc_id) 
where salutation_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'SALUTATION_KEY_MRS');

UPDATE  int_dev.lp_cust set salutation = (Select val from int_dev.lp_attrb_chc a,int_dev.lp_cust b where b.salutation_id = a.lp_attrb_chc_id) 
where salutation_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'SALUTATION_KEY_MR');

UPDATE  int_dev.lp_cust set salutation = (Select val from int_dev.lp_attrb_chc a,int_dev.lp_cust b where b.salutation_id = a.lp_attrb_chc_id) 
where salutation_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc where key_ = 'SALUTATION_KEY_DR');

----lp_cust_-----
UPDATE  int_dev.lp_cust_ set salutation = (Select val from int_dev.lp_attrb_chc_ a,int_dev.lp_cust_ b where b.salutation_id = a.lp_attrb_chc_id) 
where salutation_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'SALUTATION_KEY_MS');

UPDATE  int_dev.lp_cust_ set salutation = (Select val from int_dev.lp_attrb_chc_ a,int_dev.lp_cust_ b where b.salutation_id = a.lp_attrb_chc_id) 
where salutation_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'SALUTATION_KEY_MISS');

UPDATE  int_dev.lp_cust_ set salutation = (Select val from int_dev.lp_attrb_chc_ a,int_dev.lp_cust_ b where b.salutation_id = a.lp_attrb_chc_id) 
where salutation_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'SALUTATION_KEY_MRS');

UPDATE  int_dev.lp_cust_ set salutation = (Select val from int_dev.lp_attrb_chc_ a,int_dev.lp_cust_ b where b.salutation_id = a.lp_attrb_chc_id) 
where salutation_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'SALUTATION_KEY_MR');

UPDATE  int_dev.lp_cust_ set salutation = (Select val from int_dev.lp_attrb_chc_ a,int_dev.lp_cust_ b where b.salutation_id = a.lp_attrb_chc_id) 
where salutation_id = (Select lp_attrb_chc_id from int_dev.lp_attrb_chc_ where key_ = 'SALUTATION_KEY_DR');


----- drop column salutation_id --------
ALTER TABLE int_dev.lp_cust DROP COLUMN salutation_id;
ALTER TABLE int_dev.lp_cust_ DROP COLUMN salutation_id;