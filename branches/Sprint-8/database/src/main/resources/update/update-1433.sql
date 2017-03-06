ALTER TABLE int_dev.lp_rq ADD COLUMN prt_draw_fee_commission_pct numeric(38,4);
ALTER TABLE int_dev.lp_rq_ ADD COLUMN prt_draw_fee_commission_pct numeric(38,4);

ALTER TABLE int_dev.lp_rq ADD COLUMN prt_org_fee_commission_pct numeric(38,4);
ALTER TABLE int_dev.lp_rq_ ADD COLUMN prt_org_fee_commission_pct numeric(38,4);


ALTER TABLE int_dev.lp_rq ADD COLUMN prt_id character varying(255);
ALTER TABLE int_dev.lp_rq_ ADD COLUMN prt_id character varying(255);
