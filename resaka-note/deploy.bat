@echo off
echo ========================================
echo    RESAKA NOTE - Build and Deploy
echo ========================================
echo.

REM 
set TOMCAT_HOME=C:\xampp\tomcat
set WAR_NAME=resaka-note
set PROJECT_DIR=%~dp0

REM 
echo [1/4] Build Maven clean package...
cd /d "%PROJECT_DIR%"
call mvn clean package -q
if %ERRORLEVEL% neq 0 (
    echo.
    echo ERREUR : Le build Maven a echoue !
    pause
    exit /b 1
)
echo       Build reussi !
echo.

REM 
echo [2/4] Suppression de l'ancien deploiement...
if exist "%TOMCAT_HOME%\webapps\%WAR_NAME%.war" (
    del /f "%TOMCAT_HOME%\webapps\%WAR_NAME%.war"
    echo       Ancien WAR supprime.
)
if exist "%TOMCAT_HOME%\webapps\%WAR_NAME%" (
    rmdir /s /q "%TOMCAT_HOME%\webapps\%WAR_NAME%"
    echo       Ancien dossier supprime.
)
echo.

REM 
echo [3/4] Copie du nouveau WAR vers Tomcat...
copy /y "%PROJECT_DIR%target\%WAR_NAME%.war" "%TOMCAT_HOME%\webapps\" >nul
if %ERRORLEVEL% neq 0 (
    echo.
    echo ERREUR : Impossible de copier le WAR !
    pause
    exit /b 1
)
echo       WAR copie avec succes !
echo.

REM 
echo [4/4] Deploiement termine !
echo ========================================
echo   Le WAR a ete deploye dans :
echo   %TOMCAT_HOME%\webapps\%WAR_NAME%.war
echo ========================================
echo.
echo   Si Tomcat est en cours d'execution, 
echo   il va deployer automatiquement.
echo   Sinon, demarrez Tomcat via XAMPP.
echo.
pause
