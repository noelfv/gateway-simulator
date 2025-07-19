@echo off
title Gateway Simulator

REM Crear directorio logs si no existe
if not exist "logs" mkdir logs

REM Ejecutar la aplicación
java -Xmx2g -Xms512m -Dlog4j2.configurationFile=config/log4j2.xml -jar gateway-simulator-1.0-SNAPSHOT.jar

pause