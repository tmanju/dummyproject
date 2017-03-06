-- Core_Lob_Content

create table CORE_LOB_CONTENT (
	CORE_LOB_CONTENT_ID number(19,0) not null,
	CONTENT blob,
	VERSION number(10,0),
	primary key (CORE_LOB_CONTENT_ID)
);

create table CORE_LOB_CONTENT_ (
	CORE_LOB_CONTENT_ID number(19,0) not null,
	REV number(19,0) not null,
	REVTYPE number(3,0),
	CONTENT blob,
	primary key (CORE_LOB_CONTENT_ID, REV)
);

alter table CORE_LOB_CONTENT_ 
	add constraint FK2CA144E6A5CA84FE 
	foreign key (REV) 
	references REV_INFO;

----------------------------------------------------------------------
-- LP_ATTACHMET
----------------------------------------------------------------------

ALTER TABLE LP_ATTACHMENT ADD ATTACHMENTURL_LOB_ID number(19,0);

ALTER TABLE LP_ATTACHMENT_ ADD ATTACHMENTURL_LOB_ID number(19,0);

create index IDX_LP_ATTACHMENTURL_LOB_ID on LP_ATTACHMENT (ATTACHMENTURL_LOB_ID);

alter table LP_ATTACHMENT 
	add constraint FK_ATTACHMENTURL_LOB_ID 
	foreign key (ATTACHMENTURL_LOB_ID) 
	references CORE_LOB_CONTENT;
	
ALTER TABLE LP_ATTACHMENT
disable CONSTRAINT FK_ATTACHMENTURL_LOB_ID;

----------------------------------------------------------------------
-- LP_BUREAU_RPT
----------------------------------------------------------------------

ALTER TABLE LP_BUREAU_RPT ADD RESPONSE_PDF_LOB_ID number(19,0);

ALTER TABLE LP_BUREAU_RPT_ ADD RESPONSE_PDF_LOB_ID number(19,0);

create index IDX_BR_RESPONSE_PDF_LOB_ID on LP_BUREAU_RPT (RESPONSE_PDF_LOB_ID);

alter table LP_BUREAU_RPT 
	add constraint FK_BR_RESPONSE_PDF_LOB_ID 
	foreign key (RESPONSE_PDF_LOB_ID) 
	references CORE_LOB_CONTENT;
	
ALTER TABLE LP_BUREAU_RPT
disable CONSTRAINT FK_BR_RESPONSE_PDF_LOB_ID;

----------------------------------------------------------------------
-- LP_DOC.ARCHIVE_STREAM
----------------------------------------------------------------------

ALTER TABLE LP_DOC ADD ARCHIVE_STREAM_LOB_ID number(19,0);

ALTER TABLE LP_DOC_ ADD ARCHIVE_STREAM_LOB_ID number(19,0);

create index IDX_LP_DOC_ARCHIVE_STREAM on LP_DOC (ARCHIVE_STREAM_LOB_ID);

alter table LP_DOC 
	add constraint FK_LP_DOC_ARCHIVE_STREAM 
	foreign key (ARCHIVE_STREAM_LOB_ID) 
	references CORE_LOB_CONTENT;

ALTER TABLE LP_DOC
disable CONSTRAINT FK_LP_DOC_ARCHIVE_STREAM;

----------------------------------------------------------------------
-- LP_DOC.SIGNING_METADATA
----------------------------------------------------------------------

ALTER TABLE LP_DOC ADD SIGNING_METADATA_LOB_ID number(19,0);

ALTER TABLE LP_DOC_ ADD SIGNING_METADATA_LOB_ID number(19,0);

create index IDX_LP_DOC_SIGNING_METADATA on LP_DOC (SIGNING_METADATA_LOB_ID);

alter table LP_DOC 
	add constraint FK_LP_DOC_SIGNING_METADATA 
	foreign key (SIGNING_METADATA_LOB_ID) 
	references CORE_LOB_CONTENT;

ALTER TABLE LP_DOC
disable CONSTRAINT FK_LP_DOC_SIGNING_METADATA;

----------------------------------------------------------------------
-- LP_DOC.UPLOADED_STREAM
----------------------------------------------------------------------

ALTER TABLE LP_DOC ADD UPLOADED_STREAM_LOB_ID number(19,0);

ALTER TABLE LP_DOC_ ADD UPLOADED_STREAM_LOB_ID number(19,0);

create index IDX_LP_DOC_UPLOADED_STREAM on LP_DOC (UPLOADED_STREAM_LOB_ID);

alter table LP_DOC 
	add constraint FK_LP_DOC_UPLOADED_STREAM 
	foreign key (UPLOADED_STREAM_LOB_ID) 
	references CORE_LOB_CONTENT;

ALTER TABLE LP_DOC
disable CONSTRAINT FK_LP_DOC_UPLOADED_STREAM;

----------------------------------------------------------------------
-- LP_ICON_LIBRARY.ICON_FILE
----------------------------------------------------------------------

ALTER TABLE LP_ICON_LIBRARY ADD ICON_FILE_LOB_ID number(19,0);

ALTER TABLE LP_ICON_LIBRARY_ ADD ICON_FILE_LOB_ID number(19,0);

create index IDX_ICON_LIBRARY_ICON_FILE on LP_ICON_LIBRARY (ICON_FILE_LOB_ID);

alter table LP_ICON_LIBRARY 
	add constraint FK_ICON_LIBRARY_ICON_FILE 
	foreign key (ICON_FILE_LOB_ID) 
	references CORE_LOB_CONTENT;

ALTER TABLE LP_ICON_LIBRARY
disable CONSTRAINT FK_ICON_LIBRARY_ICON_FILE;

----------------------------------------------------------------------
-- LP_INTEGRATION_TASK_LOG.XML_PAYLOAD
----------------------------------------------------------------------

ALTER TABLE LP_INTEGRATION_TASK_LOG ADD XML_PAYLOAD_LOB_ID number(19,0);

ALTER TABLE LP_INTEGRATION_TASK_LOG_ ADD XML_PAYLOAD_LOB_ID number(19,0);

create index IDX_INTGTASK_LOG_XML_PAYLOAD on LP_INTEGRATION_TASK_LOG (XML_PAYLOAD_LOB_ID);

alter table LP_INTEGRATION_TASK_LOG 
	add constraint FK_INTGTASK_LOG_XML_PAYLOAD 
	foreign key (XML_PAYLOAD_LOB_ID) 
	references CORE_LOB_CONTENT;

ALTER TABLE LP_INTEGRATION_TASK_LOG
disable CONSTRAINT FK_INTGTASK_LOG_XML_PAYLOAD;

----------------------------------------------------------------------
-- LP_MOODY_PARTY_FINC_INFO.FINC_SUM
----------------------------------------------------------------------

ALTER TABLE LP_MOODY_PARTY_FINC_INFO ADD FINC_SUM_LOB_ID number(19,0);

ALTER TABLE LP_MOODY_PARTY_FINC_INFO_ ADD FINC_SUM_LOB_ID number(19,0);

create index IDX_MOODY_FINC_SUM on LP_MOODY_PARTY_FINC_INFO (FINC_SUM_LOB_ID);

alter table LP_MOODY_PARTY_FINC_INFO 
	add constraint FK_MOODY_FINC_SUM 
	foreign key (FINC_SUM_LOB_ID) 
	references CORE_LOB_CONTENT;

ALTER TABLE LP_MOODY_PARTY_FINC_INFO
disable CONSTRAINT FK_MOODY_FINC_SUM;

----------------------------------------------------------------------
-- LP_PAGE_AS_IMAG.IMAGE
----------------------------------------------------------------------

ALTER TABLE LP_PAGE_AS_IMAGE ADD IMAGE_LOB_ID number(19,0);

ALTER TABLE LP_PAGE_AS_IMAGE_ ADD IMAGE_LOB_ID number(19,0);

create index IDX_PAGE_AS_IMG_LOB on LP_PAGE_AS_IMAGE (IMAGE_LOB_ID);

alter table LP_PAGE_AS_IMAGE
	add constraint FK_PAGE_AS_IMG_LOB 
	foreign key (IMAGE_LOB_ID) 
	references CORE_LOB_CONTENT;

ALTER TABLE LP_PAGE_AS_IMAGE
disable CONSTRAINT FK_PAGE_AS_IMG_LOB;

----------------------------------------------------------------------
-- LP_SCRCRD_DTL.AUDIT_RESP_XML
----------------------------------------------------------------------

ALTER TABLE LP_SCRCRD_DTL ADD AUDIT_RESP_XML_LOB_ID number(19,0);

ALTER TABLE LP_SCRCRD_DTL_ ADD AUDIT_RESP_XML_LOB_ID number(19,0);

create index IDX_SCRCRD_DTL_AUDIT_RESP_XML on LP_SCRCRD_DTL (AUDIT_RESP_XML_LOB_ID);

alter table LP_SCRCRD_DTL 
	add constraint FK_SCRCRD_DTL_AUDIT_RESP_XML 
	foreign key (AUDIT_RESP_XML_LOB_ID) 
	references CORE_LOB_CONTENT;

ALTER TABLE LP_SCRCRD_DTL
disable CONSTRAINT FK_SCRCRD_DTL_AUDIT_RESP_XML;

----------------------------------------------------------------------
-- LP_TEST_ENTITY.IMAGE
----------------------------------------------------------------------

ALTER TABLE LP_TEST_ENTITY ADD IMAGE_LOB_ID number(19,0);

ALTER TABLE LP_TEST_ENTITY_ ADD IMAGE_LOB_ID number(19,0);

create index IDX_LP_TEST_ENTITY_IMAGE on LP_TEST_ENTITY (IMAGE_LOB_ID);

alter table LP_TEST_ENTITY 
	add constraint FK_LP_TEST_ENTITY_IMAGE 
	foreign key (IMAGE_LOB_ID) 
	references CORE_LOB_CONTENT;

ALTER TABLE LP_TEST_ENTITY
disable CONSTRAINT FK_LP_TEST_ENTITY_IMAGE;

----------------------------------------------------------------------
-- LP_UCC_ATTACHMENT.ATTACHMENT_BIN_VAL
----------------------------------------------------------------------

ALTER TABLE LP_UCC_ATTACHMENT ADD ATTACHMENT_VAL_LOB_ID number(19,0);

ALTER TABLE LP_UCC_ATTACHMENT_ ADD ATTACHMENT_VAL_LOB_ID number(19,0);

create index IDX_LP_UCC_ATTACHMENT_VAL on LP_UCC_ATTACHMENT (ATTACHMENT_VAL_LOB_ID);

alter table LP_UCC_ATTACHMENT 
	add constraint FK_LP_UCC_ATTACHMENT_VAL 
	foreign key (ATTACHMENT_VAL_LOB_ID) 
	references CORE_LOB_CONTENT;

ALTER TABLE LP_UCC_ATTACHMENT
disable CONSTRAINT FK_LP_UCC_ATTACHMENT_VAL;