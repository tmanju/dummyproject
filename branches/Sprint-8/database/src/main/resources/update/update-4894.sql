alter table LP_PERMISSION drop column DFLT;
alter table LP_PERMISSION drop column CMPT_ID;
alter table LP_PERMISSION drop column DFLT;
alter table LP_PERMISSION drop column PERMISSION_TP_ID;

alter table LP_PERMISSION_ drop column DFLT;
alter table LP_PERMISSION_ drop column CMPT_ID;
alter table LP_PERMISSION_ drop column DFLT;
alter table LP_PERMISSION_ drop column PERMISSION_TP_ID;

alter table LP_PERMISSION ADD (
        CREATE_ varchar(4000),
        DFLT_EXPR varchar(4000),
        DELETE_ varchar(4000)
)

alter table LP_PERMISSION_ ADD (
        CREATE_ varchar(4000),
        DFLT_EXPR varchar(4000),
        DELETE_ varchar(4000)
)
		