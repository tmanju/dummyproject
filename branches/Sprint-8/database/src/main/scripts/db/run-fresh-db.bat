@echo off

setlocal 
FOR /F "tokens=*" %%i in ('type database.properties') do SET %%i 
IF EXIST .\logs\NUL GOTO fresh
mkdir logs

:fresh
call fresh.bat %database.name% %database.admin.username% %database.admin.password% %database.host% %database.port% %database.username% %database.password% %database.schema% %database.user.tablespace% %database.user.indexspace% .\logs %database.user.profile% ddl %database.vendor%
echo "Database Schmea Creation Complete!! Check ./logs folder for errors!!"
endlocal