echo on

# ###########################################################################
#
#   Batch file which installs the schema
#
#   The script accepts the following arguments:
#
#     1. Database Name
#     2. Database Administrator Logon Name
#     3. Database Administrator Password
#     4. Target Schema User Name
#     5. Target Schema Password
#     6. Log Directory
#
#   Author: Gurvinder Pal Singh
#
# ###########################################################################

ARGNO=8

if [ $# -lt "$ARGNO" ] 

then

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

else

echo Install Schema: Installing schema $1 $2 $3 $4 $5 $6 $7 $8 $9 ${10} on database $1...

sqlplus "$2/$3@$1 as sysdba" @installschema.txt $1 $4 $5 $6 $7 $9 >$8/installschema_$4.log ${10}

echo Install Schema: Done!

fi
