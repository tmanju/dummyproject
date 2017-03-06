ALTER TABLE Deadline MODIFY escalated number (1,0) not null;
    

ALTER TABLE I18NText DROP COLUMN shortText;
            
            
ALTER TABLE Notification_email_header 
        RENAME COLUMN mapkey to emailHeaders_KEY;
        
ALTER TABLE Notification_email_header MODIFY emailHeaders_KEY varchar2(255 char) null;
         
     
ALTER TABLE ProcessInstanceLog DROP COLUMN outcome;
ALTER TABLE ProcessInstanceLog DROP COLUMN parentProcessInstanceId;
ALTER TABLE ProcessInstanceLog DROP COLUMN status;


ALTER TABLE Task DROP COLUMN archived;
ALTER TABLE Task DROP COLUMN completedOn;
ALTER TABLE Task DROP COLUMN OPTLOCK;
       
        
DROP table TaskEvent;

DROP sequence ATTACHMENT_ID_SEQ;

DROP sequence BOOLEANEXPR_ID_SEQ;

DROP sequence COMMENT_ID_SEQ;

DROP sequence CONTENT_ID_SEQ;

DROP sequence DEADLINE_ID_SEQ;

DROP sequence EMAILNOTIFHEAD_ID_SEQ;

DROP sequence ESCALATION_ID_SEQ;

DROP sequence I18NTEXT_ID_SEQ;

DROP sequence NODE_INST_LOG_ID_SEQ;

DROP sequence NOTIFICATION_ID_SEQ;

DROP sequence PROC_INST_EVENT_ID_SEQ;

DROP sequence PROC_INST_LOG_ID_SEQ;

DROP sequence REASSIGNMENT_ID_SEQ;

DROP sequence TASK_EVENT_ID_SEQ;        

DROP sequence VAR_INST_LOG_ID_SEQ;
        