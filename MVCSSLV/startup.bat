set HOMEDIR=D:\WORK\Local_GIT_Hub\TEAMSSLV\MVCSSLV
set BINDIR=D:\WORK\apache-tomcat-7.0.37\bin
set EXECUTABLE=D:\WORK\apache-tomcat-7.0.37\bin\startup.bat
set STOPPABLE=D:\WORK\apache-tomcat-7.0.37\bin\shutdown.bat
set WARDIR=D:\WORK\apache-tomcat-7.0.37\webapps
set WARFILENEW=D:\WORK\Local_GIT_Hub\TEAMSSLV\MVCSSLV\target\java2.war

mvn clean install

cd %BINDIR%
call %STOPPABLE%

rmdir /S /Q %WARDIR%
mkdir %WARDIR%

copy %WARFILENEW% %WARDIR%

call %EXECUTABLE%

cd %HOMEDIR%
