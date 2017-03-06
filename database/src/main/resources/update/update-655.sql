
ALTER TABLE lp_fac_be ADD COLUMN trm_id bigint;
ALTER TABLE lp_fac_be_ ADD COLUMN trm_id bigint;

ALTER TABLE lp_fac_be ADD COLUMN prd_tp_id bigint;
ALTER TABLE lp_fac_be_ ADD COLUMN prd_tp_id bigint;

ALTER TABLE lp_fac_allocator ADD COLUMN offered_dt date;
ALTER TABLE lp_fac_allocator_ ADD COLUMN offered_dt date;

ALTER TABLE lp_fac_allocator ADD COLUMN last_update date;
ALTER TABLE lp_fac_allocator_ ADD COLUMN last_update date;



