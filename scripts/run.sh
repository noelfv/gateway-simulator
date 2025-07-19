#!/bin/bash

echo "===================================="
echo "  Gateway Simulator"
echo "===================================="

# Configurar variables
APP_NAME="gateway-simulator"
JAR_FILE="${APP_NAME}-1.0-SNAPSHOT.jar"
LOG_CONFIG="config/log4j2.xml"
JAVA_OPTS="-Xmx2g -Xms512m"

# Verificar que existe Java
if ! command -v java &> /dev/null; then
    echo "ERROR: Java no está instalado o no está en el PATH"
    echo "Instala Java desde: https://adoptium.net/"
    exit 1
fi

# Verificar que existe el JAR
if [ ! -f "$JAR_FILE" ]; then
    echo "ERROR: No se encuentra el archivo $JAR_FILE"
    echo "Ejecuta 'mvn clean package' para generar el JAR"
    exit 1
fi

# Verificar que existe la configuración de log
if [ ! -f "$LOG_CONFIG" ]; then
    echo "ADVERTENCIA: No se encuentra $LOG_CONFIG"
    echo "Se usará la configuración por defecto"
    LOG_CONFIG_PARAM=""
else
    LOG_CONFIG_PARAM="-Dlog4j2.configurationFile=$LOG_CONFIG"
fi

# Crear directorio logs si no existe
mkdir -p logs

# Mostrar información del sistema
echo "Información del sistema:"
echo "- Java Version:"
java -version
echo "- Memoria disponible: $JAVA_OPTS"
echo "- Archivo JAR: $JAR_FILE"
echo "- Configuración Log: $LOG_CONFIG"
echo "- Directorio actual: $(pwd)"
echo ""

# Ejecutar la aplicación
echo "Iniciando aplicación..."
echo ""
java $JAVA_OPTS $LOG_CONFIG_PARAM -jar "$JAR_FILE"

# Verificar el código de salida
if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: La aplicación terminó con código de error $?"
    exit $?
fi

echo ""
echo "Aplicación terminada correctamente"