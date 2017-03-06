ALTER TABLE int_dev.lp_cust DROP COLUMN PUBLIC_CMP ;
ALTER TABLE int_dev.lp_cust_ DROP COLUMN PUBLIC_CMP ;

alter table int_dev.LP_BUREAU_DATA 
drop constraint FK73872614D9E7EE42 ;

alter table int_dev.LP_BUREAU_DATA_ 
drop constraint FKFD5D9CCBA5CA84FE;

drop table int_dev.LP_BUREAU_DATA;

drop table int_dev.LP_BUREAU_DATA_ ;

drop table int_dev.LP_CUST_BUREAU_DATA;

drop table int_dev.LP_CUST_BUREAU_DATA_ ;
	
