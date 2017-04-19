-- Table: int_dev.lp_fac_srv_messages

-- DROP TABLE int_dev.lp_fac_srv_messages;

CREATE TABLE int_dev.lp_fac_srv_messages
(
  lp_fac_id bigint NOT NULL,
  lp_srv_message_id bigint NOT NULL,
  CONSTRAINT fk60a017f925735366 FOREIGN KEY (lp_srv_message_id)
      REFERENCES int_dev.lp_srv_message (lp_srv_message_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk60a017f9913784d1 FOREIGN KEY (lp_fac_id)
      REFERENCES int_dev.lp_fac (lp_fac_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT lp_fac_srv_messages_lp_srv_message_id_key UNIQUE (lp_srv_message_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE int_dev.lp_fac_srv_messages
  OWNER TO tps_dev_master;

-- Index: int_dev.idx_lp_fac_srv_messages_1

-- DROP INDEX int_dev.idx_lp_fac_srv_messages_1;

CREATE INDEX idx_lp_fac_srv_messages_1
  ON int_dev.lp_fac_srv_messages
  USING btree
  (lp_fac_id);

-- Index: int_dev.idx_lp_fac_srv_messages_2

-- DROP INDEX int_dev.idx_lp_fac_srv_messages_2;

CREATE INDEX idx_lp_fac_srv_messages_2
  ON int_dev.lp_fac_srv_messages
  USING btree
  (lp_srv_message_id);


  
-- Table: int_dev.lp_fac_srv_messages_

-- DROP TABLE int_dev.lp_fac_srv_messages_;

CREATE TABLE int_dev.lp_fac_srv_messages_
(
  rev bigint NOT NULL,
  lp_fac_id bigint NOT NULL,
  lp_srv_message_id bigint NOT NULL,
  revtype smallint,
  CONSTRAINT lp_fac_srv_messages__pkey PRIMARY KEY (rev, lp_fac_id, lp_srv_message_id),
  CONSTRAINT fkb362e786a5ca84fe FOREIGN KEY (rev)
      REFERENCES int_dev.rev_info (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE int_dev.lp_fac_srv_messages_
  OWNER TO tps_dev_master;

ALTER TABLE int_dev.lp_cust ADD COLUMN synced_to_akritiv boolean;
ALTER TABLE int_dev.lp_cust_ ADD COLUMN synced_to_akritiv boolean;
update int_dev.lp_cust set synced_to_akritiv= true;
update int_dev.lp_cust_ set synced_to_akritiv= true;