@echo off

rem ###########################################################################
rem
rem   Batch file which installs the schema
rem
rem   The script accepts the following arguments:
rem
rem     1. Database Name
rem     2. Database Administrator Logon Name
rem     3. Database Administrator Password
rem     4. Target Schema User Name
rem     5. Target Schema Password
rem     6. Log Directory
rem
rem   Author: Gurvinder Pal Singh
rem
rem ###########################################################################

SET DB_HOST=%1
SET DB_PORT=%2
SET DB_NAME=%3
SET DB_USER=%4
SET DB_PASSWORD=%5
SET DB_SCHEMA_NAME=%6
SET DB_TABLESPACE=%7
SET DB_INDEXSPACE=%8
SET DB_LOG=%9
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SET DDL_FOLDER=%1


if (%DB_HOST%) == () goto usage
if (%DB_PORT%) == () goto usage
if (%DB_NAME%) == () goto usage
if (%DB_USER%) == () goto usage
if (%DB_PASSWORD%) == () goto usage
if (%DB_SCHEMA_NAME%) == () goto usage
if (%DB_TABLESPACE%) == () goto usage
if (%DB_INDEXSPACE%) == () goto usage
if (%DB_LOG%) == () goto usage
if (%DDL_FOLDER%) == () goto usage

echo Install Schema: Installing schema '%DB_SCHEMA_NAME%' on database '%DB_NAME%'...
set PGPASSWORD=%DB_PASSWORD%

echo Install Schema: Creating schema '%DB_SCHEMA_NAME%'
psql  -h %DB_HOST% -p %DB_PORT% -d %DB_NAME% --pset pager=off -X -q -a -l -v DB_SCHEMA_NAME=%DB_SCHEMA_NAME% -v DB_NAME=%DB_NAME% -f createschema.sql -o %DB_LOG%\createschema_%DB_SCHEMA_NAME%.log -U %DB_USER% 

echo Install Schema: Creating tables '%DB_SCHEMA_NAME%'
psql  -h %DB_HOST% -p %DB_PORT% -d %DB_NAME% --pset pager=off -X -q -a -l -v DB_SCHEMA_NAME=%DB_SCHEMA_NAME% -v DB_NAME=%DB_NAME% -f %DDL_FOLDER%/tables.sql -o %DB_LOG%\tables_%DB_SCHEMA_NAME%.log -U %DB_USER% 

echo Install Schema: Creating sequences '%DB_SCHEMA_NAME%'
psql  -h %DB_HOST% -p %DB_PORT% -d %DB_NAME% --pset pager=off -X -q -a -l -v DB_SCHEMA_NAME=%DB_SCHEMA_NAME% -v DB_NAME=%DB_NAME% -f %DDL_FOLDER%/sequences.sql -o %DB_LOG%\sequences_%DB_SCHEMA_NAME%.log -U %DB_USER% 

echo Install Schema: Creating sequences '%DB_SCHEMA_NAME%'
psql  -h %DB_HOST% -p %DB_PORT% -d %DB_NAME% --pset pager=off -X -q -a -l -v DB_SCHEMA_NAME=%DB_SCHEMA_NAME% -v DB_NAME=%DB_NAME% -f %DDL_FOLDER%/quartz/tables_postgres.sql -o %DB_LOG%\quartz_tables_%DB_SCHEMA_NAME%.log -U %DB_USER% 


echo Install Schema: Done!
goto leave

:usage
echo ------------------------------------------------------------------------------------------
echo Usage: installschema db_host db_port db_name db_user db_password schema_name tablespace indexspace log_dir ddl_folder
echo   - db_name:         database name to install into
echo   - admin_name:      database administrator logon name
echo   - admin_password:  database administrator logon password
echo   - schema_name:     target schema user logon name
echo   - schema_password: target schema user logon password
echo   - log_dir:         directory for log files            
echo Example: installschema 127.0.0.1 5432 testdb devuser password lp_oob_ab logs ddl
echo ------------------------------------------------------------------------------------------

:leave
