###########################################################################
#
#   Batch file which clears an existing database and reinstalls.
#
#   The script accepts the following arguments:
#
#     1. Database Name
#     2. Database Administrator Logon Name
#     3. Database Administrator Password
#     4. Target Schema User Name
#     5. Target Schema Password
#     6. Error log directory
#     7. Target Environment (dev, test etc.)
#
#     Author: Gurvinder Pal Singh
#
# ###########################################################################

echo starting fresh.sh

ARGNO=4

if [ $# -lt "$ARGNO" ]   

then
  
echo ---------------------------------------------------------------------------------------------
echo Usage: fresh db_name admin_name admin_password schema_name schema_password log_dir target_env
echo   - db_name:         name of database to refresh
echo   - admin_name:      database administrator logon name
echo   - admin_password:  database administrator logon password
echo   - schema_name:     target schema user logon name
echo   - schema_password: target schema user logon password
echo   - log_dir:         directory for SQL Loader log files
echo   - target_env:      dev, test etc.; to load the proper seed data
echo Example: fresh beacondevdb sys sys_pwd beacon_user beacon_pass /db dev
echo ---------------------------------------------------------------------------------------------
	 
else

echo NOTE: It is strongly recommended that the target schema DB user be disconnected
echo       from Oracle. Check  logs related to this goal.
./cleardatabase.sh $1 $2 $3 $4 $8 ${10}
./installschema.sh $1 $2 $3 $4 $5 $6 $7 $8 $9 ${10}

fi 

