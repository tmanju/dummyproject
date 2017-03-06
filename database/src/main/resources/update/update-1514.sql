alter table int_dev.lp_doc add column generated_stream_lob_id int8;
alter table int_dev.lp_doc_ add column generated_stream_lob_id int8;


alter table int_dev.lp_doc add column generated_stream_size int4;
alter table int_dev.lp_doc_ add column generated_stream_size int4;

alter table int_dev.lp_doc add column generated_dttm timestamp;
alter table int_dev.lp_doc_ add column generated_dttm timestamp;

alter table int_dev.lp_doc add column generated_stream_by_id int8;
alter table int_dev.lp_doc_ add column generated_stream_by_id int8;


update int_dev.lp_doc d set generated_stream_lob_id = uploaded_stream_lob_id;
update int_dev.lp_doc_ d set generated_stream_lob_id = uploaded_stream_lob_id;

update int_dev.lp_doc d set generated_stream_size = uploaded_stream_size;
update int_dev.lp_doc_ d set generated_stream_size = uploaded_stream_size;


update int_dev.lp_doc d set generated_dttm = upload_dttm;
update int_dev.lp_doc_ d set generated_dttm = upload_dttm;

update int_dev.lp_doc d set generated_stream_by_id = uploaded_stream_by_id;
update int_dev.lp_doc_ d set generated_stream_by_id = uploaded_stream_by_id;
