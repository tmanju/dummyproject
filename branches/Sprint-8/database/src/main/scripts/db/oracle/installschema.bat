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

SET DB_NAME=%1
SET DB_ADMIN_LOGIN=%2
SET DB_ADMIN_PASSWORD=%3
SET DB_USER=%4
SET DB_PASSWORD=%5
SET DB_TABLESPACE=%6
SET DB_INDEXSPACE=%7
SET DB_LOG=%8
SET DB_USER_PROFILE=%9
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

echo Install Schema: Installing schema '%DB_USER%' on database '%DB_NAME%'...


sqlplus "%DB_ADMIN_LOGIN%/%DB_ADMIN_PASSWORD%@%DB_NAME% as sysdba" @installschema.txt %DB_NAME% %DB_USER% %DB_PASSWORD% %DB_TABLESPACE% %DB_INDEXSPACE% %DB_USER_PROFILE% %DDL_FOLDER%>> %DB_LOG%\installschema_%DB_USER%.log
echo Install Schema: Done!
goto leave

:usage
echo ------------------------------------------------------------------------------------------
echo Usage: installschema db_name admin_name admin_password schema_name schema_password log_dir
echo   - db_name:         database name to install into
echo   - admin_name:      database administrator logon name
echo   - admin_password:  database administrator logon password
echo   - schema_name:     target schema user logon name
echo   - schema_password: target schema user logon password
echo   - log_dir:         directory for log files
echo Example: installschema beaccondevdb sys wordpass beacon_user beacon_pass c:/logs/db
echo ------------------------------------------------------------------------------------------

:leave
