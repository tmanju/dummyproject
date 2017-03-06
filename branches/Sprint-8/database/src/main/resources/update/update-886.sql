ALTER TABLE int_dev.lp_fac_allocator_txn ADD COLUMN net_off boolean;
ALTER TABLE int_dev.lp_fac_allocator_txn_ ADD COLUMN net_off boolean;

ALTER TABLE int_dev.lp_fac_allocator_txn ADD COLUMN txn_dt date;
ALTER TABLE int_dev.lp_fac_allocator_txn_ ADD COLUMN txn_dt date;

ALTER TABLE int_dev.lp_fac_allocator_txn ADD COLUMN net_paid_to_cp numeric(38,4);
ALTER TABLE int_dev.lp_fac_allocator_txn_ ADD COLUMN net_paid_to_cp numeric(38,4);