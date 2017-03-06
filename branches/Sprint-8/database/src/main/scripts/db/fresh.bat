setlocal
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
rem 6. Table Tablespace
rem     7. Index Tablespace
rem     8. Error log directory
rem 9. Database User Profile
rem 10. ddl folder location
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
if (%9) == () goto usage


SET DB_NAME=%1
SET DB_ADMIN_LOGIN=%2
SET DB_ADMIN_PASSWORD=%3
set DB_HOST=%4
set DB_PORT=%5
SET DB_USER=%6
SET DB_PASSWORD=%7
SET DB_SCHEMA_NAME=%8
SET DB_TABLESPACE=%9
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SHIFT
SET DB_INDEXSPACE=%1
SET DB_LOG=%2
SET DB_USER_PROFILE=%3
SET DDL_FOLDER=%4
SET DB_VENDOR=%5

echo NOTE: It is strongly recommended that the target schema DB user be disconnected
echo       from database. Check %DB_LOG% for all logs related to this goal.

if %DB_VENDOR% == oracle goto oracle
if %DB_VENDOR% == postgres goto postgres

echo Unsupported database %DB_VENDOR%, only supported databases are oracle/postgres
goto leave

:oracle
echo installing schema for oracle database
cd %DB_VENDOR%
call cleardatabase.bat %DB_NAME% %DB_ADMIN_LOGIN% %DB_ADMIN_PASSWORD% %DB_USER% %DB_LOG% %DDL_FOLDER%
call installschema.bat %DB_NAME% %DB_ADMIN_LOGIN% %DB_ADMIN_PASSWORD% %DB_USER% %DB_PASSWORD% %DB_TABLESPACE% %DB_INDEXSPACE% %DB_LOG% %DB_USER_PROFILE% %DDL_FOLDER%
goto leave

:postgres
echo installing schema for postgres database
cd %DB_VENDOR%
call cleardatabase.bat %DB_HOST% %DB_PORT% %DB_NAME% %DB_USER% %DB_PASSWORD% %DB_SCHEMA_NAME% %DB_LOG% 
call installschema.bat %DB_HOST% %DB_PORT% %DB_NAME% %DB_USER% %DB_PASSWORD% %DB_SCHEMA_NAME% %DB_TABLESPACE% %DB_INDEXSPACE% %DB_LOG% %DDL_FOLDER%
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
echo   - schema_Index:     Index Table Space
echo   - log_dir:         directory for SQL Loader log files
echo Example: fresh beacondevdb sys wordpass beacon_user beacon_pass c:/logs/db dev
echo ---------------------------------------------------------------------------------------------

:leave
endlocal