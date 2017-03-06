###########################################################################
#
#   Script which removes the schema
#
#   The script accepts the following arguments:
#
#     1. Database Name
#     2. Database Administrator Logon Name
#     3. Database Administrator Password
#     4. Target Schema User Name
#     5. Log Directory
#     6. ddl folder
#
#   Author: Gurvinder Pal Singh
#
###########################################################################

ARGNO=4

if [ $# -lt "$ARGNO" ]   

then
echo --------------------------------------------------------------------------
echo Usage: cleardatabase db_name admin_name admin_password schema_name log_dir
echo   - db_name:         name of database to clear
echo   - admin_name:      database administrator logon name
echo   - admin_password:  database administrator logon password
echo   - schema_name:     target schema user logon name
echo   - log_dir:         directory for log files
echo Example: cleardatabase beacondevdb sys wordpass beacon_user /logs/db
echo --------------------------------------------------------------------------
else
echo Clear Database: Clearing schema $4 on database $1...
sqlplus "$2/$3@$1 as sysdba" @cleardatabase.txt $4 $6  > $5/cleardatabase_$4.log 

echo Clear Database: Done!
fi

