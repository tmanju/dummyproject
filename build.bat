@echo off
:setlocal
set PROJECT_HOME=%CD%
set M2_HOME=%PROJECT_HOME%\tools\maven-2.0.10
set MAVEN_OPTS=-Xms1024m -Xmx1024m -XX:PermSize=256m -XX:MaxPermSize=256m
%M2_HOME%\bin\mvn.bat %*
:endlocal
