alter table int_dev.lp_cust alter column max_cdt_period type integer using max_cdt_period::integer;
alter table int_dev.lp_cust_ alter column max_cdt_period type integer using max_cdt_period::integer;

alter table int_dev.lp_cust alter column DAYS_BEYOND_TERMS type integer using DAYS_BEYOND_TERMS::integer;
alter table int_dev.lp_cust_ alter column DAYS_BEYOND_TERMS type integer using DAYS_BEYOND_TERMS::integer;

alter table int_dev.lp_bureau_data add column agency_rating_dt timestamp without time zone;
alter table int_dev.lp_bureau_data_ add column agency_rating_dt timestamp without time zone;

alter table int_dev.lp_bureau_data add column dnb_rpt_dt timestamp without time zone;
alter table int_dev.lp_bureau_data_ add column dnb_rpt_dt timestamp without time zone;

alter table int_dev.lp_bureau_data add column paydex_trend_id bigint;
alter table int_dev.lp_bureau_data_ add column paydex_trend_id bigint;

alter table int_dev.lp_rq add column rcmnd character varying(4000);
alter table int_dev.lp_rq_ add column rcmnd character varying(4000);