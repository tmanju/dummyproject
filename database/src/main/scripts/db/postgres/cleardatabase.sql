--
-- Completely removes the schema
-- This script must be run when connected as a db administrator.
--

-- PROMPT Clear Schema : Killing any open sessions on schema ':DB_SCHEMA_NAME'
SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = ':DB_NAME';

commit;
-- PROMPT Clear Schema : pause for 15s to allow for sessions to terminate.
SELECT pg_sleep(15);

-- PROMPT Clear schema ':DB_SCHEMA_NAME'
DROP SCHEMA IF EXISTS :DB_SCHEMA_NAME CASCADE;
