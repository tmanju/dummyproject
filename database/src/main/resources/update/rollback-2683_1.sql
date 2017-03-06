-----------------------------------------------
-- ROLLBACK LP_ATTACHMENT
-----------------------------------------------
ALTER TABLE LP_ATTACHMENT ADD ATTACHMENTURL BLOB;
ALTER TABLE LP_ATTACHMENT_ ADD ATTACHMENTURL BLOB;

-----------------------------------------------
-- ROLLBACK LP_BUREAU_RPT
-----------------------------------------------
ALTER TABLE LP_BUREAU_RPT ADD RESPONSE_PDF BLOB;
ALTER TABLE LP_BUREAU_RPT_ ADD RESPONSE_PDF BLOB;

-----------------------------------------------
-- ROLLBACK LP_DOC.ARCHIVE_STREAM
-----------------------------------------------
ALTER TABLE LP_DOC ADD ARCHIVE_STREAM BLOB;
ALTER TABLE LP_DOC_ ADD ARCHIVE_STREAM BLOB;

-----------------------------------------------
-- ROLLBACK LP_DOC.SIGNING_METADATA
-----------------------------------------------
ALTER TABLE LP_DOC ADD SIGNING_METADATA BLOB;
ALTER TABLE LP_DOC_ ADD SIGNING_METADATA BLOB;

-----------------------------------------------
-- ROLLBACK LP_DOC.UPLOADED_STREAM
-----------------------------------------------
ALTER TABLE LP_DOC ADD UPLOADED_STREAM BLOB;
ALTER TABLE LP_DOC_ ADD UPLOADED_STREAM BLOB;

-----------------------------------------------
-- ROLLBACK LP_ICON_LIBRARY.ICON_FILE
-----------------------------------------------
ALTER TABLE LP_ICON_LIBRARY ADD ICON_FILE BLOB;
ALTER TABLE LP_ICON_LIBRARY_ ADD ICON_FILE BLOB;

-----------------------------------------------
-- ROLLBACK LP_INTEGRATION_TASK_LOG.XML_PAYLOAD
-----------------------------------------------
ALTER TABLE LP_INTEGRATION_TASK_LOG ADD XML_PAYLOAD BLOB;
ALTER TABLE LP_INTEGRATION_TASK_LOG_ ADD XML_PAYLOAD BLOB;

-----------------------------------------------
-- ROLLBACK LP_MOODY_PARTY_FINC_INFO.FINC_SUM
-----------------------------------------------
ALTER TABLE LP_MOODY_PARTY_FINC_INFO ADD FINC_SUM BLOB;
ALTER TABLE LP_MOODY_PARTY_FINC_INFO_ ADD FINC_SUM BLOB;

-----------------------------------------------
-- ROLLBACK LP_PAGE_AS_IMAGE.IMAGE
-----------------------------------------------
ALTER TABLE LP_PAGE_AS_IMAGE ADD IMAGE BLOB;
ALTER TABLE LP_PAGE_AS_IMAGE_ ADD IMAGE BLOB;

-----------------------------------------------
-- ROLLBACK LP_SCRCRD_DTL.AUDIT_RESP_XML
-----------------------------------------------
ALTER TABLE LP_SCRCRD_DTL ADD AUDIT_RESP_XML BLOB;
ALTER TABLE LP_SCRCRD_DTL_ ADD AUDIT_RESP_XML BLOB;

-----------------------------------------------
-- ROLLBACK LP_TEST_ENTITY.IMAGE
-----------------------------------------------
ALTER TABLE LP_TEST_ENTITY ADD IMAGE BLOB;
ALTER TABLE LP_TEST_ENTITY_ ADD IMAGE BLOB;

-----------------------------------------------
-- ROLLBACK LP_UCC_ATTACHMENT.ATTACHMENT_BIN_VAL
-----------------------------------------------
ALTER TABLE LP_UCC_ATTACHMENT ADD ATTACHMENT_BIN_VAL BLOB;
ALTER TABLE LP_UCC_ATTACHMENT_ ADD ATTACHMENT_BIN_VAL BLOB;



