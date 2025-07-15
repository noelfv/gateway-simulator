@echo off
echo ====================================
echo  Gateway Simulator
echo ====================================

REM Configurar variables
set APP_NAME=gateway-simulator
set JAR_FILE=%APP_NAME%-1.0-SNAPSHOT.jar
set LOG_CONFIG=config/log4j2.xml
set JAVA_OPTS=-Xmx2g -Xms512m

REM Verificar que existe Java
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java no está instalado o no está en el PATH
    echo Descarga Java desde: https://adoptium.net/
    pause
    exit /b 1
)

REM Verificar que existe el JAR
if not exist "%JAR_FILE%" (
    echo ERROR: No se encuentra el archivo %JAR_FILE%
    echo Ejecuta 'mvn clean package' para generar el JAR
    pause
    exit /b 1
)

REM Verificar que existe la configuración de log
if not exist "%LOG_CONFIG%" (
    echo ADVERTENCIA: No se encuentra %LOG_CONFIG%
    echo Se usará la configuración por defecto
    set LOG_CONFIG_PARAM=
) else (
    set LOG_CONFIG_PARAM=-Dlog4j2.configurationFile=%LOG_CONFIG%
)

REM Crear directorio logs si no existe
if not exist "logs" mkdir logs

REM Mostrar información del sistema
echo Información del sistema:
echo - Java Version:
java -version
echo - Memoria disponible: %JAVA_OPTS%
echo - Archivo JAR: %JAR_FILE%
echo - Configuración Log: %LOG_CONFIG%
echo - Directorio actual: %CD%
echo.

REM Ejecutar la aplicación
echo Iniciando aplicación...
echo.
java %JAVA_OPTS% %LOG_CONFIG_PARAM% -jar "%JAR_FILE%"

REM Verificar el código de salida
if %errorlevel% neq 0 (
    echo.
    echo ERROR: La aplicación terminó con código de error %errorlevel%
    pause
    exit /b %errorlevel%
)

echo.
echo Aplicación terminada correctamente
pause