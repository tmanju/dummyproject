if not "%JAVA_HOME%" == "" goto jdkHome
if not "%JRE_HOME%" == "" goto okHome
echo Neither the JAVA_HOME nor the JRE_HOME environment variable is defined
echo At least one of these environment variable is needed to run this program
goto end

:jdkHome
set JRE_HOME="%JAVA_HOME%"

:okHome
set cp="."
for %%i in (lib\*.jar) do call cp.bat %%i

:end