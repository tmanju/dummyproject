
ALTER TABLE internex.lp_fac_allocator ADD COLUMN INX_MISC_FEE numeric(38,4);
ALTER TABLE internex.lp_fac_allocator_ ADD COLUMN INX_MISC_FEE numeric(38,4);

ALTER TABLE internex.LP_FAC_PLACEMENT_AUDIT ADD COLUMN INX_MISC_FEE numeric(38,4);
ALTER TABLE internex.LP_FAC_PLACEMENT_AUDIT_ ADD COLUMN INX_MISC_FEE numeric(38,4);

--------------  INX Unused Line Fee spread = Facility Unused Line Rate - CP Unused Line Rate

ALTER TABLE internex.lp_fac_be ADD COLUMN UNUSED_LNE_RATE numeric(7,4);
ALTER TABLE internex.lp_fac_be_ ADD COLUMN UNUSED_LNE_RATE numeric(7,4);

ALTER TABLE internex.LP_FAC_ALLOCATOR ADD COLUMN UNUSED_LNE_FEE_SPR numeric(38,4);
ALTER TABLE internex.LP_FAC_ALLOCATOR_ ADD COLUMN UNUSED_LNE_FEE_SPR numeric(38,4);

ALTER TABLE internex.LP_FAC_PLACEMENT_AUDIT ADD COLUMN UNUSED_LNE_FEE_SPR numeric(38,4);
ALTER TABLE internex.LP_FAC_PLACEMENT_AUDIT_ ADD COLUMN UNUSED_LNE_FEE_SPR numeric(38,4);








