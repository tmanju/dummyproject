@echo off

rem ###########################################################################
rem
rem   Batch file which clears an existing database and reinstalls.
rem
rem   The script accepts the following arguments:
rem
rem     1. Database Name
rem     2. Database Administrator Logon Name
rem     3. Database Administrator Password
rem     4. Target Schema User Name
rem     5. Target Schema Password
rem	6. Table Tablespace
rem 	7. Index Tablespace
rem     8. Error log directory
rem     9. Target Environment (dev, test etc.)
rem
rem     Author: Gurvinder Pal Singh
rem
rem ###########################################################################

if (%1) == () goto usage
if (%2) == () goto usage
if (%3) == () goto usage
if (%4) == () goto usage
if (%5) == () goto usage
if (%6) == () goto usage
if (%7) == () goto usage
if (%8) == () goto usage


SET ONE=%1
SET TWO=%2
SET THREE=%3
SET FOUR=%4
SET FIVE=%5
SET SIX=%6
SET SEVEN=%7
SET EIGHT=%8
SET NINE=%9
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SET TEN=%1

echo NOTE: It is strongly recommended that the target schema DB user be disconnected
echo       from Oracle. Check %6 for all logs related to this goal.


call cleardatabase.bat %ONE% %TWO% %THREE% %FOUR% %EIGHT%
call installschema.bat %ONE% %TWO% %THREE% %FOUR% %FIVE% %SIX% %SEVEN% %EIGHT% %NINE%
goto leave

:usage
echo ---------------------------------------------------------------------------------------------
echo Usage: fresh db_name admin_name admin_password schema_name schema_password log_dir target_env
echo   - db_name:         name of database to refresh
echo   - admin_name:      database administrator logon name
echo   - admin_password:  database administrator logon password
echo   - schema_name:     target schema user logon name
echo   - schema_password: target schema user logon password
echo   - schema_tablespace: Table Table Space
echo   - schema_Index:	   Index Table Space
echo   - log_dir:         directory for SQL Loader log files
echo   - target_env:      dev, test etc.; to load the proper seed data
echo Example: fresh beacondevdb sys wordpass beacon_user beacon_pass c:/logs/db dev
echo ---------------------------------------------------------------------------------------------

:leave
