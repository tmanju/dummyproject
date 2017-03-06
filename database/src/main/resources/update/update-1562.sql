	alter table int_dev.lp_cust add column adj_risk_score_id bigint;
	alter table int_dev.lp_cust_ add column adj_risk_score_id bigint;
	
	alter table int_dev.lp_cust add column risk_score_id bigint;
	alter table int_dev.lp_cust_ add column risk_score_id bigint;
	
	alter table int_dev.lp_cust add column risk_score_dttm timestamp without time zone;
	alter table int_dev.lp_cust_ add column risk_score_dttm timestamp without time zone;


	create table int_dev.LP_BORWR_RATING (
        LP_BORWR_RATING_ID int8 not null,
        ADJ_FRAUD_SCORE int4,
        FRAUD_SCORE int4,
        FRAUD_SCORE_DTTM timestamp,
        RISK_SCORE_DTTM timestamp,
        SOFT_DELETED_ON timestamp,
        VERSION int4,
        ADJ_RISK_SCORE_ID int8,
        RISK_SCORE_ID int8,
        primary key (LP_BORWR_RATING_ID)
    );

    create table int_dev.LP_BORWR_RATING_ (
        LP_BORWR_RATING_ID int8 not null,
        REV int8 not null,
        REVTYPE int2,
        ADJ_FRAUD_SCORE int4,
        FRAUD_SCORE int4,
        FRAUD_SCORE_DTTM timestamp,
        RISK_SCORE_DTTM timestamp,
        SOFT_DELETED_ON timestamp,
        ADJ_RISK_SCORE_ID int8,
        RISK_SCORE_ID int8,
        primary key (LP_BORWR_RATING_ID, REV)
    );


   create table int_dev.LP_CUST_BORWR_RATINGS (
        LP_CUST_ID int8 not null,
        LP_BORWR_RATING_ID int8 not null,
        unique (LP_BORWR_RATING_ID)
    );

    create table int_dev.LP_CUST_BORWR_RATINGS_ (
        REV int8 not null,
        LP_CUST_ID int8 not null,
        LP_BORWR_RATING_ID int8 not null,
        REVTYPE int2,
        primary key (REV, LP_CUST_ID, LP_BORWR_RATING_ID)
    );
	
    alter table int_dev.LP_BORWR_RATING 
        add constraint FKCD72BA578390C6BF 
        foreign key (ADJ_RISK_SCORE_ID) 
        references int_dev.LP_ATTRB_CHC;

    alter table int_dev.LP_BORWR_RATING 
        add constraint FKCD72BA57D0112C57 
        foreign key (RISK_SCORE_ID) 
        references int_dev.LP_ATTRB_CHC;

    alter table int_dev.LP_BORWR_RATING_ 
        add constraint FKE0E490E8A5CA84FE 
        foreign key (REV) 
        references int_dev.REV_INFO;
		
    alter table int_dev.LP_CUST_BORWR_RATINGS 
        add constraint FK201A56062441BD48 
        foreign key (LP_CUST_ID) 
        references int_dev.LP_CUST;

    alter table int_dev.LP_CUST_BORWR_RATINGS 
        add constraint FK201A5606E70D1DE0 
        foreign key (LP_BORWR_RATING_ID) 
        references int_dev.LP_BORWR_RATING;

    alter table int_dev.LP_CUST_BORWR_RATINGS_ 
        add constraint FKE3306B19A5CA84FE 
        foreign key (REV) 
        references int_dev.REV_INFO;
