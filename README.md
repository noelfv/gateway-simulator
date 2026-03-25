# Gateway Simulator

Simulador de gateway de pagos que permite parsear, mapear y convertir mensajes **ISO8583** hacia **ISO20022** para las redes **Visa** y **Mastercard**.

## Requisitos

- **Java 17**
- **Maven 3.8+**

## Compilacion

```bash
mvn clean package
```

Esto genera la estructura de distribucion en `target/dist/`.

## Ejecucion

### Con Maven (desarrollo)

```bash
mvn spring-boot:run "-Dspring-boot.run.profiles=gw"
```

### Con JAR (produccion)

```bash
java -Dspring.profiles.active=gw -jar target/dist/gateway-simulator-2.0.0-SNAPSHOT.jar
```

> El perfil `gw` es **obligatorio**. Activa los sub-perfiles: `data`, `datalocal`, `sensitivedata`.

## Funcionalidades

| Funcionalidad               | Descripcion                                              |
|-----------------------------|----------------------------------------------------------|
| Parsear mensaje ISO8583     | Convierte tramas hexadecimales en campos legibles        |
| Parsear mensaje claro       | Parsea mensajes en texto claro                           |
| Convertir trama             | Convierte tramas entre formatos                          |
| Transformar a ISO20022      | Mapea mensajes ISO8583 a estructura ISO20022             |
| Generar trama ISO8583       | Genera tramas ISO8583 a partir de campos individuales    |
| Parser TLV (Campo 48)       | Analiza campos TLV del campo 48                          |

## Redes Soportadas

- **Visa** (PEER01)
- **Mastercard** (PEER02)

## Arquitectura

Aplicacion Spring Boot con interfaz grafica Swing. Utiliza el patron Factory + Delegate para soportar multiples redes de pago con sus respectivos parsers, mappers y logica de campos.

```
SwingApplication
    ├── ParserFactory    → Visa/Mastercard DelegateParser
    ├── MapperFactory    → Visa/Mastercard DelegateMapper
    └── FieldLogicFactory → Visa/Mastercard DelegateFieldLogic
```

## Estructura del Proyecto

```
src/main/java/com/bbva/
├── SwingApplication.java       # Punto de entrada
├── gui/                        # Interfaz grafica (Swing)
├── orchestrator/               # Logica de negocio (parsers, mappers, campos)
└── gateway/                    # DTOs ISO20022 y utilidades
```

## Configuracion

| Archivo                      | Contenido                                    |
|------------------------------|----------------------------------------------|
| `application.yml`            | Configuracion principal (puerto 8083)        |
| `application-data.yml`       | Monedas, BINs, MCCs, bancos P2P              |
| `application-datalocal.yml`  | Datos locales                                |
| `log4j2.xml`                 | Configuracion de logging                     |
