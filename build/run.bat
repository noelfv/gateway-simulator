@echo off
setlocal enabledelayedexpansion

title Gateway Simulator v2.0.0

:: ================================================================
::  Gateway Simulator - Launcher Portable
::  Uso: Doble clic o ejecutar desde la carpeta build\
:: ================================================================

set "APP_HOME=%~dp0"
set "APP_HOME=%APP_HOME:~0,-1%"

set "JAR=%APP_HOME%\gateway-simulator-2.0.0-SNAPSHOT.jar"
set "CONFIG_DIR=%APP_HOME%\config"
set "LOG_DIR=%APP_HOME%\log"
set "LIBS_DIR=%APP_HOME%\libs"

echo.
echo ================================================================
echo   Gateway Simulator  v2.0.0
echo   Redes: Visa ^(PEER01^)  ^|  Mastercard ^(PEER02^)
echo ================================================================
echo.

:: ── Validar JAR ──────────────────────────────────────────────────
if not exist "%JAR%" (
    echo [ERROR] JAR no encontrado:
    echo         %JAR%
    echo.
    echo         Ejecute desde la raiz del proyecto:
    echo         mvn clean package
    echo.
    pause
    exit /b 1
)

:: ── Crear carpeta log si no existe ───────────────────────────────
if not exist "%LOG_DIR%" (
    mkdir "%LOG_DIR%"
    echo [INFO] Carpeta log\ creada.
)

:: ── Detectar Java ────────────────────────────────────────────────
if defined JAVA_HOME (
    set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVA_CMD=java"
)

"%JAVA_CMD%" -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java no encontrado.
    echo         - Configure la variable JAVA_HOME, o
    echo         - Agregue java al PATH del sistema.
    echo         Descarga Java 17: https://adoptium.net/
    echo.
    pause
    exit /b 1
)

:: ── Info de arranque ─────────────────────────────────────────────
echo  APP_HOME  : %APP_HOME%
echo  Config    : %CONFIG_DIR%
echo  Logs      : %LOG_DIR%
echo  Libs      : %LIBS_DIR%
echo.
echo  Iniciando...
echo.

:: ── Ejecutar desde build\ para que las rutas relativas funcionen ─
cd /d "%APP_HOME%"

"%JAVA_CMD%" ^
    -Xms256m ^
    -Xmx512m ^
    -Dspring.profiles.active=gw ^
    -Dspring.config.additional-location=config/ ^
    -Dlog4j2.configurationFile=config/log4j2.xml ^
    -DlogDir=log ^
    -Djava.awt.headless=false ^
    -jar "%JAR%"

if errorlevel 1 (
    echo.
    echo ================================================================
    echo  [ERROR] La aplicacion termino con codigo %errorlevel%
    echo  Revisa: %LOG_DIR%\gateway-simulator-errors.log
    echo ================================================================
    echo.
    pause
)

endlocal
