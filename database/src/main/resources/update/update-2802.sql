ALTER TABLE LP_RQ ADD RAC_CMT_GRP_ID NUMBER(19,0);

ALTER TABLE LP_RQ ADD CONSTRAINT FK454C6DAF32D3B53 FOREIGN KEY (RAC_CMT_GRP_ID) REFERENCES LP_CMT_GRP (LP_CMT_GRP_ID);

create index IDX_RacComment_1 on lp_rq (RAC_CMT_GRP_ID);

ALTER TABLE LP_RQ_ ADD RAC_CMT_GRP_ID NUMBER(19,0);