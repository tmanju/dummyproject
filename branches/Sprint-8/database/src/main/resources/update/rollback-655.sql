ALTER TABLE lp_fac_be DROP COLUMN trm_id;
ALTER TABLE lp_fac_be_ DROP COLUMN trm_id;

ALTER TABLE lp_fac_be DROP COLUMN prd_tp_id;
ALTER TABLE lp_fac_be_ DROP COLUMN prd_tp_id;

ALTER TABLE lp_fac_allocator DROP COLUMN offered_dt;
ALTER TABLE lp_fac_allocator_ DROP COLUMN offered_dt;

ALTER TABLE lp_fac_allocator DROP COLUMN last_update;
ALTER TABLE lp_fac_allocator_ DROP COLUMN last_update;

ALTER TABLE lp_fac_be ADD COLUMN trm character varying(255);
ALTER TABLE lp_fac_be_ ADD COLUMN trm character varying(255);

ALTER TABLE lp_fac_be ADD COLUMN prd_tp character varying(255);
ALTER TABLE lp_fac_be_ ADD COLUMN prd_tp character varying(255);