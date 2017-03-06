ALTER TABLE Deadline MODIFY escalated number (5,0) null;
    

ALTER TABLE I18NText ADD shortText varchar2(255 char);
            
            
ALTER TABLE Notification_email_header 
        RENAME COLUMN emailHeaders_KEY to mapkey;
        
ALTER TABLE Notification_email_header MODIFY mapkey varchar2(255 char) not null;
         

ALTER TABLE ProcessInstanceLog ADD outcome varchar2(255 char);
ALTER TABLE ProcessInstanceLog ADD parentProcessInstanceId number(19,0);
ALTER TABLE ProcessInstanceLog ADD status number(10,0);


ALTER TABLE Task ADD archived number(5,0);
ALTER TABLE Task ADD completedOn timestamp;
ALTER TABLE Task ADD OPTLOCK number(5,0);
       
        
create table TaskEvent (
    type varchar2(31 char) not null,
    id number(19,0) not null,
    eventTime timestamp,
    sessionId number(10,0),
    taskId number(19,0) not null,
    userId varchar2(255 char),
    primary key (id)
);

declare
    l_new_seq INTEGER;
begin
   select case when COUNT(*) = 0 then 1 else MAX(ID) + 1 end into l_new_seq  from ATTACHMENT;
   execute immediate 'create sequence ATTACHMENT_ID_SEQ start with ' || l_new_seq;
end;
/

declare
    l_new_seq INTEGER;
begin
   select case when COUNT(*) = 0 then 1 else MAX(ID) + 1 end into l_new_seq  from BOOLEANEXPRESSION;
   execute immediate 'create sequence BOOLEANEXPR_ID_SEQ start with ' || l_new_seq;
end;
/

declare
    l_new_seq INTEGER;
begin
   select case when COUNT(*) = 0 then 1 else MAX(ID) + 1 end into l_new_seq  from TASK_COMMENT;
   execute immediate 'create sequence COMMENT_ID_SEQ start with ' || l_new_seq;
end;
/

declare
    l_new_seq INTEGER;
begin
   select case when COUNT(*) = 0 then 1 else MAX(ID) + 1 end into l_new_seq  from CONTENT;
   execute immediate 'create sequence CONTENT_ID_SEQ start with ' || l_new_seq;
end;
/

declare
    l_new_seq INTEGER;
begin
   select case when COUNT(*) = 0 then 1 else MAX(ID) + 1 end into l_new_seq  from DEADLINE;
   execute immediate 'create sequence DEADLINE_ID_SEQ start with ' || l_new_seq;
end;
/

declare
    l_new_seq INTEGER;
begin
   select case when COUNT(*) = 0 then 1 else MAX(ID) + 1 end into l_new_seq  from email_header;
   execute immediate 'create sequence EMAILNOTIFHEAD_ID_SEQ start with ' || l_new_seq;
end;
/

declare
    l_new_seq INTEGER;
begin
   select case when COUNT(*) = 0 then 1 else MAX(ID) + 1 end into l_new_seq  from ESCALATION;
   execute immediate 'create sequence ESCALATION_ID_SEQ start with ' || l_new_seq;
end;
/

declare
    l_new_seq INTEGER;
begin
   select case when COUNT(*) = 0 then 1 else MAX(ID) + 1 end into l_new_seq  from I18NText;
   execute immediate 'create sequence I18NTEXT_ID_SEQ start with ' || l_new_seq;
end;
/

declare
    l_new_seq INTEGER;
begin
   select case when COUNT(*) = 0 then 1 else MAX(ID) + 1 end into l_new_seq  from NodeInstanceLog;
   execute immediate 'create sequence NODE_INST_LOG_ID_SEQ start with ' || l_new_seq;
end;
/

declare
    l_new_seq INTEGER;
begin
   select case when COUNT(*) = 0 then 1 else MAX(ID) + 1 end into l_new_seq  from Notification;
   execute immediate 'create sequence NOTIFICATION_ID_SEQ start with ' || l_new_seq;
end;
/

declare
    l_new_seq INTEGER;
begin
   select case when COUNT(*) = 0 then 1 else MAX(ID) + 1 end into l_new_seq  from ProcessInstanceEventInfo;
   execute immediate 'create sequence PROC_INST_EVENT_ID_SEQ start with ' || l_new_seq;
end;
/

declare
    l_new_seq INTEGER;
begin
   select case when COUNT(*) = 0 then 1 else MAX(ID) + 1 end into l_new_seq  from ProcessInstanceLog;
   execute immediate 'create sequence PROC_INST_LOG_ID_SEQ start with ' || l_new_seq;
end;
/

declare
    l_new_seq INTEGER;
begin
   select case when COUNT(*) = 0 then 1 else MAX(ID) + 1 end into l_new_seq  from Reassignment;
   execute immediate 'create sequence REASSIGNMENT_ID_SEQ start with ' || l_new_seq;
end;
/

create sequence TASK_EVENT_ID_SEQ;    

declare
    l_new_seq INTEGER;
begin
   select case when COUNT(*) = 0 then 1 else MAX(ID) + 1 end into l_new_seq  from VariableInstanceLog;
   execute immediate 'create sequence VAR_INST_LOG_ID_SEQ start with ' || l_new_seq;
end;
/

update task set optlock = 1;
update task set archived = 0;
commit;
