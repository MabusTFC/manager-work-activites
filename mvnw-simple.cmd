@echo off
setlocal

REM Set JAVA_HOME if not set
if not defined JAVA_HOME (
    set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-25.0.2.10-hotspot"
)

REM Add Java to PATH
set "PATH=%JAVA_HOME%\bin;%PATH%"

REM Maven distribution URL from properties
set "MVN_VERSION=3.9.14"
set "MVN_HOME=%USERPROFILE%\.m2\wrapper\dists\apache-maven-%MVN_VERSION%-bin"

REM Check if Maven is already downloaded
for /d %%d in ("%MVN_HOME%\*") do (
    if exist "%%d\bin\mvn.cmd" (
        set "MVN_CMD=%%d\bin\mvn.cmd"
        goto :run_maven
    )
)

echo Maven not found. Please use IntelliJ IDEA to reload Maven project.
echo Or install Apache Maven manually and add it to PATH.
exit /b 1

:run_maven
call "%MVN_CMD%" %*
