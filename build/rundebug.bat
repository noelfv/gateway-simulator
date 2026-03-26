@echo off
setlocal enabledelayedexpansion

title Gateway Simulator v2.0.0 [DEBUG]

:: ================================================================
::  Gateway Simulator - Launcher DEBUG
::  Abre una segunda ventana con tail en tiempo real del log diario
:: ================================================================

set "APP_HOME=%~dp0"
set "APP_HOME=%APP_HOME:~0,-1%"

set "JAR=%APP_HOME%\gateway-simulator-2.0.0-SNAPSHOT.jar"
set "CONFIG_DIR=%APP_HOME%\config"
set "LOG_DIR=%APP_HOME%\log"
set "LIBS_DIR=%APP_HOME%\libs"
set "LOG_DAILY=%LOG_DIR%\gateway-simulator-daily.log"
set "LOG_ERRORS=%LOG_DIR%\gateway-simulator-errors.log"

echo.
echo ================================================================
echo   Gateway Simulator  v2.0.0  [MODO DEBUG]
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
if not exist "%LOG_DIR%" mkdir "%LOG_DIR%"

:: ── Detectar Java ────────────────────────────────────────────────
if defined JAVA_HOME (
    set "JAVA_CMD=%JAVA_HOME%\bin\java.exe"
) else (
    set "JAVA_CMD=java"
)

"%JAVA_CMD%" -version >nul 2>&1
if errorlevel 1 (
    echo [ERROR] Java no encontrado.
    echo         Configure JAVA_HOME o agregue java al PATH.
    echo         Descarga Java 17: https://adoptium.net/
    echo.
    pause
    exit /b 1
)

:: ── Abrir consola de trazabilidad en nueva ventana ───────────────
echo [1/2] Abriendo consola de trazabilidad...

start "Gateway Simulator - Trazabilidad [DEBUG]" powershell -NoProfile -NoExit -Command ^
    "& { $logFile='%LOG_DAILY:\=\\%'; $Host.UI.RawUI.BackgroundColor='Black'; $Host.UI.RawUI.ForegroundColor='Gray'; Clear-Host; Write-Host '================================================================' -ForegroundColor Cyan; Write-Host '  Gateway Simulator v2.0.0  |  Trazabilidad [DEBUG]' -ForegroundColor Cyan; Write-Host '================================================================' -ForegroundColor Cyan; Write-Host ''; Write-Host ('  Log diario : ' + $logFile) -ForegroundColor DarkGray; Write-Host '  Estado     : Esperando inicio de la aplicacion...' -ForegroundColor Yellow; Write-Host ''; while (-not (Test-Path $logFile)) { Start-Sleep -Milliseconds 400 }; Write-Host '[OK] Conectado. Logs en tiempo real:' -ForegroundColor Green; Write-Host ''; Get-Content -Path $logFile -Wait -Tail 200 }"

echo.
echo ================================================================
echo   [2/2] Iniciando Gateway Simulator [DEBUG]
echo.
echo   Logs generados en:
echo     log\gateway-simulator-daily.log   (trazabilidad completa)
echo     log\gateway-simulator-errors.log  (solo errores)
echo ================================================================
echo.

:: ── Ejecutar desde build\ ────────────────────────────────────────
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
