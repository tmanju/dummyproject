------------------------------------

ALTER TABLE internex.LP_CUST_BACKEND ADD COLUMN PLEDGED_AMT_AMT numeric(19,2);
ALTER TABLE internex.LP_CUST_BACKEND_ ADD COLUMN PLEDGED_AMT_AMT numeric(19,2);

ALTER TABLE internex.LP_CUST_BACKEND ADD COLUMN PLEDGED_AMT_FRX character varying(255);
ALTER TABLE internex.LP_CUST_BACKEND_ ADD COLUMN PLEDGED_AMT_FRX character varying(255);

ALTER TABLE internex.LP_CUST_BACKEND ADD COLUMN PLEDGED_AMT_TDATE date;
ALTER TABLE internex.LP_CUST_BACKEND_ ADD COLUMN PLEDGED_AMT_TDATE date;

----ACC_VAL in Customerbackend will be removed.

ALTER TABLE internex.LP_CUST_BACKEND drop COLUMN acc_val;
ALTER TABLE internex.LP_CUST_BACKEND_ drop COLUMN acc_val;

----ACC_VAL in Customerbackend will be removed. 
--PLEDGED_AMT_AMT, PLEDGED_AMT_FRX will take its place but will be filled by the value 
--of corrosponding capital provider's field called ACC_VAL_AMT,ACC_VAL_FRX

--Remove from capital provider----
ALTER TABLE internex.lp_capital_provider drop COLUMN ACC_VAL_AMT;
ALTER TABLE internex.lp_capital_provider_ drop COLUMN ACC_VAL_AMT;
ALTER TABLE internex.lp_capital_provider drop COLUMN ACC_VAL_FRX;
ALTER TABLE internex.lp_capital_provider_ drop COLUMN ACC_VAL_FRX;
ALTER TABLE internex.lp_capital_provider drop COLUMN ACC_VAL_TDATE;
ALTER TABLE internex.lp_capital_provider_ drop COLUMN ACC_VAL_TDATE;

commit;
------------

 