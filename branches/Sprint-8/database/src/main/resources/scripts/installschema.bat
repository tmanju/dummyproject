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

if (%1) == () goto usage
if (%2) == () goto usage
if (%3) == () goto usage
if (%4) == () goto usage
if (%5) == () goto usage
if (%6) == () goto usage

echo Install Schema: Installing schema '%4' on database '%1'...


sqlplus "%2/%3@%1 as sysdba" @installschema.txt %1 %4 %5 %6 %7 %9> %8\installschema_%4.log
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
