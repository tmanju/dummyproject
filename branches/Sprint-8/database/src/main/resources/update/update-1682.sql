ALTER TABLE int_dev.lp_cust ADD COLUMN servicing_id_lookup character varying(255);
ALTER TABLE int_dev.lp_cust_ ADD COLUMN servicing_id_lookup character varying(255);

ALTER TABLE int_dev.lp_fac ADD COLUMN servicing_id_lookup character varying(255);
ALTER TABLE int_dev.lp_fac_ ADD COLUMN servicing_id_lookup character varying(255);

ALTER TABLE int_dev.lp_debtor_cust ADD COLUMN servicing_id_lookup character varying(255);
ALTER TABLE int_dev.lp_debtor_cust_ ADD COLUMN servicing_id_lookup character varying(255);

ALTER TABLE int_dev.lp_party_dba ADD COLUMN servicing_id_lookup character varying(255);
ALTER TABLE int_dev.lp_party_dba_ ADD COLUMN servicing_id_lookup character varying(255);