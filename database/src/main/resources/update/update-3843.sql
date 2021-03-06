ALTER TABLE LOANPATH_OOB_DEV.LP_FEE ADD FEE_DISTRIBUTION_ID int8;
ALTER TABLE LOANPATH_OOB_DEV.LP_FEE_ ADD FEE_DISTRIBUTION_ID int8;

create index IDX_1001720 on LOANPATH_OOB_DEV.LP_FEE (FEE_DISTRIBUTION_ID);

alter table LOANPATH_OOB_DEV.LP_FEE 
		add constraint FK8643E62B607954FC 
		foreign key (FEE_DISTRIBUTION_ID) 
		references LOANPATH_OOB_DEV.LP_ATTRB_CHC;


create table LOANPATH_OOB_DEV.LP_FEE_ALLOCATION (
		LP_FEE_ALLOCATION_ID int8 not null,
		FEE_AMT numeric(38, 4),
		FEE_PCT numeric(7, 4) ,
		VERSION int4,
		FEE_ID int8,
		SYNDICATION_ID int8,
		primary key (LP_FEE_ALLOCATION_ID)
);

create table LOANPATH_OOB_DEV.LP_FEE_ALLOCATION_ (
		LP_FEE_ALLOCATION_ID int8 not null,
		REV int8 not null,
		REVTYPE int2,
		FEE_AMT numeric(38, 4),
		FEE_PCT numeric(7, 4),
		FEE_ID int8,
		SYNDICATION_ID int8,
		primary key (LP_FEE_ALLOCATION_ID, REV)
);

create index IDX_1000680 on LOANPATH_OOB_DEV.LP_FEE_ALLOCATION (SYNDICATION_ID);

create index IDX_1000670 on LOANPATH_OOB_DEV.LP_FEE_ALLOCATION (FEE_ID);

alter table LOANPATH_OOB_DEV.LP_FEE_ALLOCATION 
		add constraint FKD311B3742861E9FC 
		foreign key (SYNDICATION_ID) 
		references LOANPATH_OOB_DEV.LP_SYNDICATION;

alter table LOANPATH_OOB_DEV.LP_FEE_ALLOCATION 
		add constraint FKD311B3742C42AA9C 
		foreign key (FEE_ID) 
		references LOANPATH_OOB_DEV.LP_FEE;

alter table LOANPATH_OOB_DEV.LP_FEE_ALLOCATION_ 
		add constraint FK8F24BB6BA5CA84FE 
		foreign key (REV) 
		references LOANPATH_OOB_DEV.REV_INFO;

