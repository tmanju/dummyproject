ALTER TABLE int_dev.lp_fac_be ADD COLUMN cp_committed_flag boolean DEFAULT FALSE;
ALTER TABLE int_dev.lp_fac_be_ ADD COLUMN cp_committed_flag boolean DEFAULT FALSE;
ALTER TABLE int_dev.lp_fac_be ADD COLUMN external_id character varying(255);
ALTER TABLE int_dev.lp_fac_be_ ADD COLUMN external_id character varying(255);