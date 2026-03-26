@echo off
setlocal enabledelayedexpansion

title Gateway Simulator - Build y Ejecucion [DEBUG]

set DIST=build
set JAR=gateway-simulator-2.0.0-SNAPSHOT.jar

echo.
echo ================================================================
echo   Gateway Simulator  ^|  Build y Ejecucion [MODO DEBUG]
echo ================================================================
echo.

:: ── Paso 1: Empaquetar ──────────────────────────────────────────
echo [1/3] Empaquetando proyecto (mvn clean package)...
echo.
call mvn clean package -DskipTests -B

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] Fallo el empaquetado. Revisa los errores anteriores.
    echo.
    pause
    exit /b 1
)

echo.
echo [OK] Empaquetado exitoso.
echo.

:: ── Paso 2: Preparar carpetas en target\dist ─────────────────────
echo [2/3] Preparando estructura de distribucion...

if not exist "%DIST%\log" (
    mkdir "%DIST%\log"
    echo [INFO] Carpeta %DIST%\log\ creada.
)

echo [OK] Estructura lista:
echo      %DIST%\libs\    dependencias
echo      %DIST%\config\  configuracion externa
echo      %DIST%\log\     trazabilidad Log4j2
echo.

:: Moverse a dist y capturar ruta absoluta del log
cd "%DIST%"
set LOG_PATH=%CD%\log\gateway-simulator-daily.log

:: ── Paso 3: Abrir consola de trazabilidad en nueva ventana ───────
echo [3/3] Abriendo consola de trazabilidad...

start "Gateway Simulator - Trazabilidad [DEBUG]" powershell -NoProfile -NoExit -Command ^
    "& { $f='%LOG_PATH%'; $Host.UI.RawUI.BackgroundColor='Black'; $Host.UI.RawUI.ForegroundColor='Gray'; Clear-Host; Write-Host '================================================================' -ForegroundColor Cyan; Write-Host '  Gateway Simulator  |  Trazabilidad en tiempo real [DEBUG]' -ForegroundColor Cyan; Write-Host '================================================================' -ForegroundColor Cyan; Write-Host ''; Write-Host ('Archivo : ' + $f) -ForegroundColor DarkGray; Write-Host 'Estado  : Esperando inicio de la aplicacion...' -ForegroundColor Yellow; Write-Host ''; while (-not (Test-Path $f)) { Start-Sleep -Milliseconds 300 }; Write-Host '[OK] Conectado. Mostrando logs en tiempo real:' -ForegroundColor Green; Write-Host ''; Get-Content -Path $f -Wait -Tail 100 }"

echo.
echo ================================================================
echo   Iniciando Gateway Simulator [MODO DEBUG]
echo   Logs guardados en:
echo   - log\gateway-simulator-daily.log  (trazabilidad completa)
echo   - log\gateway-simulator-errors.log (solo errores)
echo ================================================================
echo.

:: ── Paso 4: Lanzar aplicacion ────────────────────────────────────
java -Xms256m -Xmx512m ^
     -Dspring.profiles.active=gw ^
     -Dspring.config.additional-location=config/ ^
     -Dlog4j2.configurationFile=config/log4j2.xml ^
     -jar %JAR%

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] La aplicacion termino con codigo %errorlevel%.
    echo         Revisa log\gateway-simulator-errors.log para el detalle.
    echo.
    pause
)

endlocal
