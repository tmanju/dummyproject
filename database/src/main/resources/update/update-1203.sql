ALTER TABLE int_dev.lp_fac_ctr ADD COLUMN rnw_appr_dt timestamp without time zone;
ALTER TABLE int_dev.lp_fac_ctr_ ADD COLUMN rnw_appr_dt timestamp without time zone;

ALTER TABLE int_dev.lp_fac ADD COLUMN rnw_appr_dt_updated boolean;
ALTER TABLE int_dev.lp_fac_ ADD COLUMN rnw_appr_dt_updated boolean;

UPDATE int_dev.lp_fac set rnw_appr_dt_updated=false;
UPDATE int_dev.lp_fac_ set rnw_appr_dt_updated=false;