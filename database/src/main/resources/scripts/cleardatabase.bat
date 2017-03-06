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
rem
rem   Author: 
rem
rem ###########################################################################

if (%1) == () goto usage
if (%2) == () goto usage
if (%3) == () goto usage
if (%4) == () goto usage
if (%5) == () goto usage

echo Clear Database: Clearing schema '%4' on database '%1'...
sqlplus "%2/%3@%1 as sysdba" @cleardatabase.txt %4 > %5\cleardatabase_%4.log
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
