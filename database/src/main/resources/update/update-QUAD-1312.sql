ALTER TABLE int_dev.lp_cust ADD COLUMN generate_duns boolean;
ALTER TABLE int_dev.lp_cust_ ADD COLUMN generate_duns boolean;
update int_dev.lp_cust set generate_duns= false;
update int_dev.lp_cust_ set generate_duns= false;