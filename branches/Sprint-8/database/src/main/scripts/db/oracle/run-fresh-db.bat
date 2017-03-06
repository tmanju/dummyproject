@echo off

setlocal 
FOR /F "tokens=*" %%i in ('type database.properties') do SET %%i 
IF EXIST .\logs\NUL GOTO fresh
mkdir logs

:fresh
call fresh.bat %database.tns.entry% %database.admin.username% %database.admin.password% %database.username% %database.password% %database.user.tablespace% %database.user.indexspace% .\logs %database.user.profile% ddl
echo "Database Schmea Creation Complete!! Check ./logs folder for errors!!"
endlocal