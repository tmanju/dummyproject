    alter table int_dev.LP_BORWR_RATING 
        drop constraint FKCD72BA578390C6BF;

    alter table int_dev.LP_BORWR_RATING 
        drop constraint FKCD72BA57D0112C57 ;

    alter table int_dev.LP_BORWR_RATING_ 
        drop constraint FKE0E490E8A5CA84FE ;
		
    alter table int_dev.LP_CUST_BORWR_RATINGS 
        drop constraint FK201A56062441BD48 ;

    alter table int_dev.LP_CUST_BORWR_RATINGS 
        drop constraint FK201A5606E70D1DE0 ;

    alter table int_dev.LP_CUST_BORWR_RATINGS_ 
        drop constraint FKE3306B19A5CA84FE ;
		
	alter table int_dev.lp_cust drop column adj_risk_score_id ;
	alter table int_dev.lp_cust_ drop column adj_risk_score_id ;
	
	alter table int_dev.lp_cust drop column risk_score_id ;
	alter table int_dev.lp_cust_ drop column risk_score_id ;
	
	alter table int_dev.lp_cust drop column risk_score_dttm;
	alter table int_dev.lp_cust_ drop column risk_score_dttm;

	drop table int_dev.LP_CUST_BORWR_RATINGS;
	drop table int_dev.LP_CUST_BORWR_RATINGS_;
	
	drop table int_dev.LP_BORWR_RATING;
	drop table int_dev.LP_BORWR_RATING_;
