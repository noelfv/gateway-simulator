@echo off
setlocal enabledelayedexpansion

title Gateway Simulator - Build y Ejecucion

set DIST=target\dist
set JAR=gateway-simulator-2.0.0-SNAPSHOT.jar

echo.
echo ================================================================
echo   Gateway Simulator  ^|  Build y Ejecucion
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

if not exist "%DIST%\logs" (
    mkdir "%DIST%\logs"
    echo [INFO] Carpeta %DIST%\logs\ creada.
)

echo [OK] Estructura lista:
echo      %DIST%\libs\    dependencias
echo      %DIST%\config\  configuracion externa
echo      %DIST%\logs\    trazabilidad Log4j2
echo.

:: ── Paso 3: Lanzar la aplicacion ─────────────────────────────────
echo [3/3] Iniciando Gateway Simulator...
echo.

cd "%DIST%"

java -Xms256m -Xmx512m ^
     -Dspring.profiles.active=gw ^
     -Dspring.config.additional-location=config/ ^
     -Dlog4j2.configurationFile=config/log4j2.xml ^
     -jar %JAR%

if %errorlevel% neq 0 (
    echo.
    echo [ERROR] La aplicacion termino con codigo %errorlevel%.
    echo         Revisa logs\gateway-simulator-errors.log para el detalle.
    echo.
    pause
)

endlocal
