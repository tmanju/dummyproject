ALTER TABLE LOANPATH_OOB_DEV.LP_SYNDICATION ADD APPORTIONED bool;
ALTER TABLE LOANPATH_OOB_DEV.LP_SYNDICATION_ ADD APPORTIONED bool;

ALTER TABLE LOANPATH_OOB_DEV.LP_SYNDICATION ADD SHRG_PCT numeric(38, 2);
ALTER TABLE LOANPATH_OOB_DEV.LP_SYNDICATION_ ADD SHRG_PCT numeric(38, 2);

ALTER TABLE LOANPATH_OOB_DEV.LP_TRANCHE ADD MAX_AMT numeric(38, 4);
ALTER TABLE LOANPATH_OOB_DEV.LP_TRANCHE_ ADD MAX_AMT numeric(38, 4);

ALTER TABLE LOANPATH_OOB_DEV.LP_TRANCHE ADD MIN_AMT numeric(38, 4);
ALTER TABLE LOANPATH_OOB_DEV.LP_TRANCHE_ ADD MIN_AMT numeric(38, 4);

ALTER TABLE LOANPATH_OOB_DEV.LP_TRANCHE DROP COLUMN AMT;
ALTER TABLE LOANPATH_OOB_DEV.LP_TRANCHE_ DROP COLUMN AMT;

