----------------------------------------------------------------------
-- LP_ATTACHMET
----------------------------------------------------------------------

ALTER TABLE LP_ATTACHMENT
enable CONSTRAINT FK_ATTACHMENTURL_LOB_ID;

ALTER TABLE LP_ATTACHMENT DROP COLUMN ATTACHMENTURL;
ALTER TABLE LP_ATTACHMENT_ DROP COLUMN ATTACHMENTURL;

----------------------------------------------------------------------
-- LP_BUREAU_RPT
----------------------------------------------------------------------

ALTER TABLE LP_BUREAU_RPT
enable CONSTRAINT FK_BR_RESPONSE_PDF_LOB_ID;	

ALTER TABLE LP_BUREAU_RPT DROP COLUMN  RESPONSE_PDF;
ALTER TABLE LP_BUREAU_RPT_ DROP COLUMN  RESPONSE_PDF;

----------------------------------------------------------------------
-- LP_DOC.ARCHIVE_STREAM
----------------------------------------------------------------------

ALTER TABLE LP_DOC
enable CONSTRAINT FK_LP_DOC_ARCHIVE_STREAM;	

ALTER TABLE LP_DOC DROP COLUMN  ARCHIVE_STREAM;
ALTER TABLE LP_DOC_ DROP COLUMN  ARCHIVE_STREAM;

----------------------------------------------------------------------
-- LP_DOC.SIGNING_METADATA
----------------------------------------------------------------------

ALTER TABLE LP_DOC
enable CONSTRAINT FK_LP_DOC_SIGNING_METADATA;	

ALTER TABLE LP_DOC DROP COLUMN  SIGNING_METADATA;
ALTER TABLE LP_DOC_ DROP COLUMN  SIGNING_METADATA;

----------------------------------------------------------------------
-- LP_DOC.UPLOADED_STREAM
----------------------------------------------------------------------

ALTER TABLE LP_DOC
enable CONSTRAINT FK_LP_DOC_UPLOADED_STREAM;	

ALTER TABLE LP_DOC DROP COLUMN  UPLOADED_STREAM;
ALTER TABLE LP_DOC_ DROP COLUMN  UPLOADED_STREAM;

----------------------------------------------------------------------
-- LP_ICON_LIBRARY.ICON_FILE
----------------------------------------------------------------------

ALTER TABLE LP_ICON_LIBRARY
enable CONSTRAINT FK_ICON_LIBRARY_ICON_FILE;	

ALTER TABLE LP_ICON_LIBRARY DROP COLUMN  ICON_FILE;
ALTER TABLE LP_ICON_LIBRARY_ DROP COLUMN  ICON_FILE;

----------------------------------------------------------------------
-- LP_INTEGRATION_TASK_LOG.XML_PAYLOAD
----------------------------------------------------------------------

ALTER TABLE LP_INTEGRATION_TASK_LOG
enable CONSTRAINT FK_INTGTASK_LOG_XML_PAYLOAD;	

ALTER TABLE LP_INTEGRATION_TASK_LOG DROP COLUMN  XML_PAYLOAD;
ALTER TABLE LP_INTEGRATION_TASK_LOG_ DROP COLUMN  XML_PAYLOAD;

----------------------------------------------------------------------
-- LP_INTEGRATION_TASK_LOG.XML_PAYLOAD
----------------------------------------------------------------------

ALTER TABLE LP_MOODY_PARTY_FINC_INFO
enable CONSTRAINT FK_MOODY_FINC_SUM;	

ALTER TABLE LP_MOODY_PARTY_FINC_INFO DROP COLUMN  FINC_SUM;
ALTER TABLE LP_MOODY_PARTY_FINC_INFO_ DROP COLUMN  FINC_SUM;

----------------------------------------------------------------------
-- LP_PAGE_AS_IMAG.IMAGE
----------------------------------------------------------------------

ALTER TABLE LP_PAGE_AS_IMAGE
enable CONSTRAINT FK_PAGE_AS_IMG_LOB;	

ALTER TABLE LP_PAGE_AS_IMAGE DROP COLUMN  IMAGE;
ALTER TABLE LP_PAGE_AS_IMAGE_ DROP COLUMN  IMAGE;

----------------------------------------------------------------------
-- LP_SCRCRD_DTL.AUDIT_RESP_XML
----------------------------------------------------------------------

ALTER TABLE LP_SCRCRD_DTL
enable CONSTRAINT FK_SCRCRD_DTL_AUDIT_RESP_XML;	

ALTER TABLE LP_SCRCRD_DTL DROP COLUMN  AUDIT_RESP_XML;
ALTER TABLE LP_SCRCRD_DTL_ DROP COLUMN  AUDIT_RESP_XML;

----------------------------------------------------------------------
-- LP_TEST_ENTITY.IMAGE
----------------------------------------------------------------------

ALTER TABLE LP_TEST_ENTITY
enable CONSTRAINT FK_LP_TEST_ENTITY_IMAGE;	

ALTER TABLE LP_TEST_ENTITY DROP COLUMN  IMAGE;
ALTER TABLE LP_TEST_ENTITY_ DROP COLUMN  IMAGE;

----------------------------------------------------------------------
-- LP_UCC_ATTACHMENT.ATTACHMENT_BIN_VAL
----------------------------------------------------------------------

ALTER TABLE LP_UCC_ATTACHMENT
enable CONSTRAINT FK_LP_UCC_ATTACHMENT_VAL;	

ALTER TABLE LP_UCC_ATTACHMENT DROP COLUMN  ATTACHMENT_BIN_VAL;
ALTER TABLE LP_UCC_ATTACHMENT_ DROP COLUMN  ATTACHMENT_BIN_VAL;
