-----------------------------------------------
-- ROLLBACK LP_ATTACHMENT
-----------------------------------------------
ALTER TABLE LP_ATTACHMENT DROP COLUMN ATTACHMENTURL_LOB_ID;
ALTER TABLE LP_ATTACHMENT_ DROP COLUMN ATTACHMENTURL_LOB_ID;

-----------------------------------------------
-- ROLLBACK LP_BUREAU_RPT
-----------------------------------------------
ALTER TABLE LP_BUREAU_RPT DROP COLUMN RESPONSE_PDF_LOB_ID;
ALTER TABLE LP_BUREAU_RPT_ DROP COLUMN RESPONSE_PDF_LOB_ID;

-----------------------------------------------
-- ROLLBACK LP_DOC.ARCHIVE_STREAM
-----------------------------------------------
ALTER TABLE LP_DOC DROP COLUMN ARCHIVE_STREAM_LOB_ID;
ALTER TABLE LP_DOC_ DROP COLUMN ARCHIVE_STREAM_LOB_ID;

-----------------------------------------------
-- ROLLBACK LP_DOC.SIGNING_METADATA
-----------------------------------------------
ALTER TABLE LP_DOC DROP COLUMN SIGNING_METADATA_LOB_ID;
ALTER TABLE LP_DOC_ DROP COLUMN SIGNING_METADATA_LOB_ID;

-----------------------------------------------
-- ROLLBACK LP_DOC.UPLOADED_STREAM
-----------------------------------------------
ALTER TABLE LP_DOC DROP COLUMN UPLOADED_STREAM_LOB_ID;
ALTER TABLE LP_DOC_ DROP COLUMN UPLOADED_STREAM_LOB_ID;

-----------------------------------------------
-- ROLLBACK LP_ICON_LIBRARY.ICON_FILE
-----------------------------------------------
ALTER TABLE LP_ICON_LIBRARY DROP COLUMN ICON_FILE_LOB_ID;
ALTER TABLE LP_ICON_LIBRARY_ DROP COLUMN ICON_FILE_LOB_ID;

-----------------------------------------------
-- ROLLBACK LP_INTEGRATION_TASK_LOG.XML_PAYLOAD
-----------------------------------------------
ALTER TABLE LP_INTEGRATION_TASK_LOG DROP COLUMN XML_PAYLOAD_LOB_ID;
ALTER TABLE LP_INTEGRATION_TASK_LOG_ DROP COLUMN XML_PAYLOAD_LOB_ID;

-----------------------------------------------
-- ROLLBACK LP_MOODY_PARTY_FINC_INFO.FINC_SUM
-----------------------------------------------
ALTER TABLE LP_MOODY_PARTY_FINC_INFO DROP COLUMN FINC_SUM_LOB_ID;
ALTER TABLE LP_MOODY_PARTY_FINC_INFO_ DROP COLUMN FINC_SUM_LOB_ID;

-----------------------------------------------
-- ROLLBACK LP_PAGE_AS_IMAG.IMAGE
-----------------------------------------------
ALTER TABLE LP_PAGE_AS_IMAGE DROP COLUMN IMAGE_LOB_ID;
ALTER TABLE LP_PAGE_AS_IMAGE_ DROP COLUMN IMAGE_LOB_ID;

-----------------------------------------------
-- ROLLBACK LP_SCRCRD_DTL.AUDIT_RESP_XML
-----------------------------------------------
ALTER TABLE LP_SCRCRD_DTL DROP COLUMN AUDIT_RESP_XML_LOB_ID;
ALTER TABLE LP_SCRCRD_DTL_ DROP COLUMN AUDIT_RESP_XML_LOB_ID;

-----------------------------------------------
-- ROLLBACK LP_TEST_ENTITY.IMAGE
-----------------------------------------------
ALTER TABLE LP_TEST_ENTITY DROP COLUMN IMAGE_LOB_ID;
ALTER TABLE LP_TEST_ENTITY_ DROP COLUMN IMAGE_LOB_ID;

-----------------------------------------------
-- ROLLBACK LP_UCC_ATTACHMENT.ATTACHMENT_BIN_VAL
-----------------------------------------------
ALTER TABLE LP_UCC_ATTACHMENT DROP COLUMN ATTACHMENT_VAL_LOB_ID;
ALTER TABLE LP_UCC_ATTACHMENT_ DROP COLUMN ATTACHMENT_VAL_LOB_ID;

-----------------------------------------------
-- DROP TABLE CORE_LOB_CONTENT
-----------------------------------------------
DROP TABLE CORE_LOB_CONTENT;
DROP TABLE CORE_LOB_CONTENT_;



