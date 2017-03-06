ALTER TABLE LP_UCC RENAME COLUMN BILL_CODE TO REF1;
ALTER TABLE LP_UCC MODIFY REF1 VARCHAR2(255) NULL;

ALTER TABLE LP_UCC_ RENAME COLUMN BILL_CODE TO REF1;

ALTER TABLE LP_UCC RENAME COLUMN LENDER_CODE TO REF2;
ALTER TABLE LP_UCC_ RENAME COLUMN LENDER_CODE TO REF2;

ALTER TABLE LP_UCC RENAME COLUMN CUST_NAME TO REF3;
ALTER TABLE LP_UCC_ RENAME COLUMN CUST_NAME TO REF3;

ALTER TABLE LP_UCC ADD REF4 VARCHAR2(255);
ALTER TABLE LP_UCC_ ADD REF4 VARCHAR2(255);

ALTER TABLE LP_UCC ADD REF5 VARCHAR2(255);
ALTER TABLE LP_UCC_ ADD REF5 VARCHAR2(255);

ALTER TABLE LP_UCC RENAME COLUMN FLEX_FIELD6 TO REF6;
ALTER TABLE LP_UCC_ RENAME COLUMN FLEX_FIELD6 TO REF6;

ALTER TABLE LP_UCC RENAME COLUMN FLEX_FIELD7 TO REF7;
ALTER TABLE LP_UCC_ RENAME COLUMN FLEX_FIELD7 TO REF7;

ALTER TABLE LP_UCC ADD NO_OF_ATTACHMENTS NUMBER(38,4);
ALTER TABLE LP_UCC_ ADD NO_OF_ATTACHMENTS NUMBER(38,4);
