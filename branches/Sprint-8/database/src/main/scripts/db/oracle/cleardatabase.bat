@echo off

rem ###########################################################################
rem
rem   Batch file which removes the schema
rem
rem   The script accepts the following arguments:
rem
rem     1. Database Name
rem     2. Database Administrator Logon Name
rem     3. Database Administrator Password
rem     4. Target Schema User Name
rem     5. Log Directory
rem	6. ddl folder
rem
rem   Author: 
rem
rem ###########################################################################

SET DB_NAME=%1
SET DB_ADMIN_LOGIN=%2
SET DB_ADMIN_PASSWORD=%3
SET DB_USER=%4
SET DB_LOG=%5
SET DDL_FOLDER=%6


if (%DB_NAME%) == () goto usage
if (%DB_ADMIN_LOGIN%) == () goto usage
if (%DB_ADMIN_PASSWORD%) == () goto usage
if (%DB_USER%) == () goto usage
if (%DB_LOG%) == () goto usage
if (%DDL_FOLDER%) == () goto usage

echo Clear Database: Clearing schema '%DB_USER%' on database '%DB_NAME%'...
sqlplus "%DB_ADMIN_LOGIN%/%DB_ADMIN_PASSWORD%@%DB_NAME% as sysdba" @cleardatabase.txt %DB_USER% %DDL_FOLDER% > %DB_LOG%\cleardatabase_%DB_USER%.log
echo Clear Database: Done!
goto leave

:usage
echo --------------------------------------------------------------------------
echo Usage: cleardatabase db_name admin_name admin_password schema_name log_dir
echo   - db_name:         name of database to clear
echo   - admin_name:      database administrator logon name
echo   - admin_password:  database administrator logon password
echo   - schema_name:     target schema user logon name
echo   - log_dir:         directory for log files
echo Example: cleardatabase beacondevdb sys wordpass beacon_user c:/logs/db
echo --------------------------------------------------------------------------

:leave
