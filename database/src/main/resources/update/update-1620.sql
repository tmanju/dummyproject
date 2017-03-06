create table int_dev.LP_FILE_IMPORT (
        LP_FILE_IMPORT_ID int8 not null,
        CREATED_AT timestamp,
        CREATED_BY varchar(255),
        FILE_CONTENT_SIZE int4,
        FILE_NAME varchar(255),
        FILE_TP varchar(255),
        PROCESSED_AT timestamp,
        SOFT_DELETED_ON timestamp,
        STATUS varchar(255),
        TMPL varchar(255),
        VERSION int4,
        FILE_CONTENT_LOB_ID int8,
        primary key (LP_FILE_IMPORT_ID)
    );

    create table int_dev.LP_FILE_IMPORT_ (
        LP_FILE_IMPORT_ID int8 not null,
        REV int8 not null,
        REVTYPE int2,
        CREATED_AT timestamp,
        CREATED_BY varchar(255),
        FILE_CONTENT_SIZE int4,
        FILE_NAME varchar(255),
        FILE_TP varchar(255),
        PROCESSED_AT timestamp,
        SOFT_DELETED_ON timestamp,
        STATUS varchar(255),
        TMPL varchar(255),
        FILE_CONTENT_LOB_ID int8,
        primary key (LP_FILE_IMPORT_ID, REV)
    );
	
	
	create index IDX_LP_FILE_IMPORT_SF_DEL on int_dev.LP_FILE_IMPORT (SOFT_DELETED_ON);

    create index IDX_LP_FILE_IMPORT_FILE_LOB on int_dev.LP_FILE_IMPORT (FILE_CONTENT_LOB_ID);

    alter table int_dev.LP_FILE_IMPORT 
        add constraint FKLPFILEIMPORTLOB 
        foreign key (FILE_CONTENT_LOB_ID) 
        references int_dev.CORE_LOB_CONTENT;

    alter table int_dev.LP_FILE_IMPORT_ 
        add constraint FKLPFILEIMPORTREV 
        foreign key (REV) 
        references int_dev.REV_INFO;