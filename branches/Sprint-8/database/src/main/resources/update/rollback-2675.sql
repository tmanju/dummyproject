ALTER TABLE LP_ATTACHMENT
    DROP COLUMN ATTACHMENTURL_SIZE;
    
ALTER TABLE LP_ATTACHMENT_
    DROP COLUMN ATTACHMENTURL_SIZE;    

ALTER TABLE LP_BUREAU_RPT
    DROP COLUMN  RESPONSE_PDF_SIZE;
    
ALTER TABLE LP_BUREAU_RPT_
    DROP COLUMN RESPONSE_PDF_SIZE;  
    
ALTER TABLE LP_DOC
    DROP (
        ARCHIVE_STREAM_SIZE,
        SIGNING_METADATA_SIZE,
        UPLOADED_STREAM_SIZE
    );
    
    
ALTER TABLE LP_DOC_
    DROP (
        ARCHIVE_STREAM_SIZE,
        SIGNING_METADATA_SIZE,
        UPLOADED_STREAM_SIZE
    );
    
ALTER TABLE LP_ICON_LIBRARY
    DROP COLUMN ICON_FILE_SIZE;
    
ALTER TABLE LP_ICON_LIBRARY_
    DROP COLUMN ICON_FILE_SIZE;
    
ALTER TABLE LP_INTEGRATION_TASK_LOG
    DROP COLUMN XML_PAYLOAD_SIZE;
    
ALTER TABLE LP_INTEGRATION_TASK_LOG_
    DROP COLUMN XML_PAYLOAD_SIZE;
      
ALTER TABLE LP_MOODY_PARTY_FINC_INFO
    DROP COLUMN  FINC_SUM_SIZE;
    
ALTER TABLE LP_MOODY_PARTY_FINC_INFO_
    DROP COLUMN  FINC_SUM_SIZE;
    
ALTER TABLE LP_PAGE_AS_IMAGE
    DROP COLUMN IMAGE_SIZE;
    
ALTER TABLE LP_PAGE_AS_IMAGE_
    DROP COLUMN IMAGE_SIZE;
    
ALTER TABLE LP_SCRCRD_DTL
    DROP COLUMN AUDIT_RESP_XML_SIZE;
    
ALTER TABLE LP_SCRCRD_DTL_
    DROP COLUMN AUDIT_RESP_XML_SIZE;
    
ALTER TABLE LP_TEST_ENTITY
    DROP COLUMN IMAGE_SIZE;
    
ALTER TABLE LP_TEST_ENTITY_
    DROP COLUMN IMAGE_SIZE;
    
ALTER TABLE LP_UCC_ATTACHMENT
    DROP COLUMN ATTACHMENT_BIN_VAL_SIZE;
    
ALTER TABLE LP_UCC_ATTACHMENT_
    DROP COLUMN ATTACHMENT_BIN_VAL_SIZE;
