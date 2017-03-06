--------------------------------------------------
---- Indexes on FK of Attachment
--------------------------------------------------

create index IDX_Attachment_1 on Attachment (attachedBy_id);

create index IDX_Attachment_2 on Attachment (TaskData_Attachments_Id);
	

--------------------------------------------------
---- Indexes on FK of BooleanExpression
--------------------------------------------------

create index IDX_BooleanExpression_1 on BooleanExpression (Escalation_Constraints_Id);
	
--------------------------------------------------
---- Indexes on FK of Deadline
--------------------------------------------------

create index IDX_Deadline_1 on Deadline (Deadlines_StartDeadLine_Id);

create index IDX_Deadline_2 on Deadline (Deadlines_EndDeadLine_Id);
	
--------------------------------------------------
---- Indexes on FK of Delegation_delegates
--------------------------------------------------

create index IDX_Delegation_delegates_1 on Delegation_delegates (entity_id);

create index IDX_Delegation_delegates_2 on Delegation_delegates (task_id);
	

--------------------------------------------------
---- Indexes on FK of Escalation
--------------------------------------------------        

create index IDX_Escalation_1 on Escalation (Deadline_Escalation_Id);
	
        
--------------------------------------------------
---- Indexes on FK of EventTypes
--------------------------------------------------        

create index IDX_EventTypes_1 on EventTypes (InstanceId);

        
--------------------------------------------------
---- Indexes on FK of I18NText
--------------------------------------------------        

create index IDX_I18NText_1 on I18NText (Notification_Descriptions_Id);

create index IDX_I18NText_2 on I18NText (Notification_Names_Id);
create index IDX_I18NText_3 on I18NText (Reassignment_Documentation_Id);
create index IDX_I18NText_4 on I18NText (Deadline_Documentation_Id);
create index IDX_I18NText_5 on I18NText (Notification_Documentation_Id);
create index IDX_I18NText_6 on I18NText (Task_Descriptions_Id);
create index IDX_I18NText_7 on I18NText (Task_Subjects_Id);
create index IDX_I18NText_8 on I18NText (Task_Names_Id);
create index IDX_I18NText_9 on I18NText (Notification_Subjects_Id);

--------------------------------------------------
---- Indexes on FK of Notification
--------------------------------------------------        

create index IDX_Notification_1 on Notification (Escalation_Notifications_Id);
	

--------------------------------------------------
---- Indexes on FK of Notification_BAs
--------------------------------------------------        


create index IDX_Notification_BAs_1 on Notification_BAs (entity_id);

create index IDX_Notification_BAs_2 on Notification_BAs (task_id);


--------------------------------------------------
---- Indexes on FK of Notification_Recipients
--------------------------------------------------        

create index IDX_Notification_Recipients_1 on Notification_Recipients (entity_id);

create index IDX_Notification_Recipients_2 on Notification_Recipients (task_id);

--------------------------------------------------        
---- Indexes on FK of Notification_Recipients
--------------------------------------------------        

create index IDX_Notification_emailH_2 on Notification_email_header (Notification_id);

--------------------------------------------------        
---- Indexes on FK of Notification_Recipients
--------------------------------------------------        

create index IDX_PeopleAssignments_BAs_1 on PeopleAssignments_BAs (entity_id);

create index IDX_PeopleAssignments_BAs_2 on PeopleAssignments_BAs (task_id);


--------------------------------------------------        
---- Indexes on FK of PeopleAssignments_ExclOwners
--------------------------------------------------        

create index IDX_PA_ExclOwners_1 on PeopleAssignments_ExclOwners (entity_id);

create index IDX_PA_ExclOwners_2 on PeopleAssignments_ExclOwners (task_id);


--------------------------------------------------        
---- Indexes on FK of PeopleAssignments_PotOwners
--------------------------------------------------        

create index IDX_PA_PotOwners_1 on PeopleAssignments_PotOwners (entity_id);

create index IDX_PA_PotOwners_2 on PeopleAssignments_PotOwners (task_id);


--------------------------------------------------        
---- Indexes on FK of PeopleAssignments_Recipients
--------------------------------------------------        

create index IDX_PA_Recipients_1 on PeopleAssignments_Recipients (entity_id);

create index IDX_PA_Recipients_2 on PeopleAssignments_Recipients (task_id);


--------------------------------------------------        
---- Indexes on FK of PeopleAssignments_Stakeholders
--------------------------------------------------        

create index IDX_PA_Stakeholders_1 on PeopleAssignments_Stakeholders (entity_id);

create index IDX_PA_Stakeholders_2 on PeopleAssignments_Stakeholders (task_id);

--------------------------------------------------        
---- Indexes on FK of Reassignment
--------------------------------------------------        

create index IDX_Reassignment_1 on Reassignment (Escalation_Reassignments_Id);

--------------------------------------------------        
---- Indexes on FK of Reassignment_potentialOwners
--------------------------------------------------        

create index IDX_Reassignment_PO_1 on Reassignment_potentialOwners (entity_id);

create index IDX_Reassignment_PO_2 on Reassignment_potentialOwners (task_id);
	

--------------------------------------------------        
---- Indexes on FK of SubTasksStrategy
--------------------------------------------------        

create index IDX_SubTasksStrategy_1 on SubTasksStrategy (Task_Id);

        
--------------------------------------------------        
---- Indexes on FK of Task
--------------------------------------------------        

create index IDX_Task_1 on Task (createdBy_id);
create index IDX_Task_2 on Task (actualOwner_id);
create index IDX_Task_3 on Task (taskInitiator_id);

--------------------------------------------------        
---- Indexes on FK of task_comment
--------------------------------------------------        

create index IDX_task_comment_1 on task_comment (TaskData_Comments_Id);
create index IDX_task_comment_2 on task_comment (addedBy_id);
