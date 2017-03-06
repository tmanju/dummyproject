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
rem 6. ddl folder
rem
rem   Author: 
rem
rem ###########################################################################

SET DB_HOST=%1
SET DB_PORT=%2
SET DB_NAME=%3
SET DB_USER=%4
SET DB_PASSWORD=%5
SET DB_SCHEMA_NAME=%6
SET DB_LOG=%7


if (%DB_HOST%) == () goto usage
if (%DB_PORT%) == () goto usage
if (%DB_NAME%) == () goto usage
if (%DB_USER%) == () goto usage
if (%DB_PASSWORD%) == () goto usage
if (%DB_LOG%) == () goto usage

echo Clear Database: Clearing schema '%DB_SCHEMA_NAME%' on database '%DB_NAME%'...
set PGPASSWORD=%DB_PASSWORD%
psql  -h %DB_HOST% -p %DB_PORT% -d %DB_NAME% -v ON_ERROR_STOP=1 --pset pager=off -X -q -a -l -v DB_SCHEMA_NAME=%DB_SCHEMA_NAME% -v DB_NAME=%DB_NAME% -f cleardatabase.sql -o %DB_LOG%\cleardatabase_%DB_SCHEMA_NAME%.log -U %DB_USER% 
echo Clear Database: Done!
goto leave

:usage
echo --------------------------------------------------------------------------
echo Usage: cleardatabase db_host db_port db_name db_user db_password schema_name log_dir
echo   - db_host:         name of database to clear
echo   - db_port:         name of database to clear
echo   - db_name:         name of database to clear
echo   - db_user:         database user name
echo   - db_password:  database password
echo   - schema_name:     target schema user logon name
echo   - log_dir:         directory for log files
echo Example: cleardatabase 127.0.0.1 5432 testdb devuser password lp_oob_ab logs
echo --------------------------------------------------------------------------

:leave
