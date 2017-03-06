------------Three fields will be added

ALTER TABLE inx_dev.LP_FAC_BE ADD COLUMN CDT_FAC_AMT_AMT numeric(19,2);
ALTER TABLE inx_dev.LP_FAC_BE_ ADD COLUMN CDT_FAC_AMT_AMT numeric(19,2);

ALTER TABLE inx_dev.LP_FAC_BE ADD COLUMN CDT_FAC_AMT_FRX character varying(255);
ALTER TABLE inx_dev.LP_FAC_BE_ ADD COLUMN CDT_FAC_AMT_FRX character varying(255);

ALTER TABLE inx_dev.LP_FAC_BE ADD COLUMN CDT_FAC_AMT_TDATE date;
ALTER TABLE inx_dev.LP_FAC_BE_ ADD COLUMN CDT_FAC_AMT_TDATE date;

----CDT_FAC_AMT in facilitybe will be droped once its value is copied to above CDT_FAC_AMT_AMT fields and put "USD" IN  CDT_FAC_AMT_FRX.

ALTER TABLE inx_dev.LP_FAC_BE drop COLUMN CDT_FAC_AMT;
ALTER TABLE inx_dev.LP_FAC_BE_ drop COLUMN CDT_FAC_AMT;


commit;
------------

 