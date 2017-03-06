@echo off
call setenv.bat
%JRE_HOME%\bin\java -Xms1024m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=256m -DconfigLocations=appContext-dataexecutor-EntityService.xml -cp %cp% com.thirdpillar.dataexec.executor.DataExecutor import_operations.xml true