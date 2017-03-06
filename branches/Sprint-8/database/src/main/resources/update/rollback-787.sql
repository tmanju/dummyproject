ALTER TABLE internex.LP_BANK_AND_TRD ALTER COLUMN bank_act_num TYPE numeric(38,4) USING (bank_act_num::numeric);
ALTER TABLE internex.LP_BANK_AND_TRD_ ALTER COLUMN bank_act_num TYPE numeric(38,4) USING (bank_act_num::numeric);

ALTER TABLE internex.LP_BANK_AND_TRD ALTER COLUMN routing_num TYPE numeric(38,4) USING (bank_act_num::numeric);
ALTER TABLE internex.LP_BANK_AND_TRD_ ALTER COLUMN routing_num TYPE numeric(38,4) USING (bank_act_num::numeric);