alter table int_dev.lp_cust ADD column duns_lookup varchar(255);
alter table int_dev.lp_cust_ ADD column duns_lookup varchar(255);

update int_dev.lp_cust set duns_lookup=duns;
update int_dev.lp_cust_ set duns_lookup=duns;