# Gateway Simulator - Guia para Claude Code

## Resumen del Proyecto

Simulador de gateway de pagos para redes **Visa** (PEER01) y **Mastercard** (PEER02).
Aplicacion de escritorio (Swing + Spring Boot 3.3.4) que parsea, mapea y convierte mensajes **ISO8583** a **ISO20022**.

## Stack Tecnologico

- **Java 17** | **Spring Boot 3.3.4** | **Maven**
- **GUI:** Swing (MyDoggy, TableLayout)
- **Mapeo:** MapStruct 1.5.5, Jackson
- **Logging:** Log4j2, SLF4J
- **Utilidades:** Guava 33.2.1, Apache Commons Lang3

## Compilacion y Ejecucion

```bash
# Compilar
mvn clean package

# Ejecutar (REQUIERE perfil gw)
mvn spring-boot:run "-Dspring-boot.run.profiles=gw"

# Alternativa con JAR
java -Dspring.profiles.active=gw -jar target/dist/gateway-simulator-2.0.0-SNAPSHOT.jar
```

> **IMPORTANTE:** Sin el perfil `gw` la aplicacion falla con error de binding de `BusinessData`.
> El perfil `gw` activa los sub-perfiles: `data`, `datalocal`, `sensitivedata`.

## Clase Principal

`com.bbva.SwingApplication` - Punto de entrada Spring Boot que inicializa la GUI Swing.

## Arquitectura

### Estructura de Paquetes

```
com.bbva/
├── SwingApplication.java          # Punto de entrada
├── gui/                           # Interfaz grafica Swing
│   ├── ParserGUIMain.java         # Frame principal (MDI)
│   ├── panels/v2/                 # Paneles actuales (usar v2, no v1)
│   ├── components/                # Componentes UI reutilizables
│   └── spring/                    # Integracion Spring-Swing
├── orchestrator/                  # Logica de negocio
│   ├── configuration/             # Cache de datos (ApplicationDataCache)
│   └── core/
│       ├── parser/factory/        # ParserFactory + Delegates por red
│       ├── mapper/factory/        # MapperFactory + Delegates por red
│       ├── logic/factory/         # FieldLogicFactory + Delegates por red
│       ├── fields/                # Definicion de 128 campos ISO8583 (enums)
│       ├── network/visa/          # Procesadores Visa
│       ├── network/mastercard/    # Procesadores Mastercard
│       ├── builders/              # ISO8583Builder, MonitoringBuilder
│       ├── dto/                   # DTOs internos
│       ├── enums/                 # Enumeraciones (ISODataType, etc.)
│       ├── exception/             # Excepciones de dominio
│       └── utils/                 # ISOUtil, ParserUtil, FieldUtil
└── gateway/                       # DTOs y utilidades del gateway
    ├── dto/iso20022/              # 50+ DTOs ISO20022
    ├── interceptors/              # GrpcHeadersInfo
    └── utils/                     # Utilidades generales
```

### Patrones de Diseno

- **Factory Pattern:** `ParserFactory`, `MapperFactory`, `FieldLogicFactory` - seleccionan implementacion segun red
- **Strategy Pattern:** `FieldParserStrategy` - diferentes parsers por tipo de dato
- **Delegate Pattern:** `ISO8583DelegateParser`, `ISO20022DelegateMapper` - delegan a implementaciones por red
- **Enumeracion de Campos:** `VisaISOField`, `MastercardISOField` - 128 campos ISO8583 declarativos

### Flujo Principal

```
Mensaje ISO8583 (hex) → ParserFactory → DelegateParser → ProcessField
    → Map<String, String> (128 campos)
    → MapperFactory → DelegateMapper → SectionMappingStrategies
    → ISO20022 DTO
```

### Redes Soportadas

| Red         | PeerId | Parser                    | Campos            | Procesador             |
|-------------|--------|---------------------------|-------------------|------------------------|
| Visa        | PEER01 | VisaDelegateParser        | VisaISOField      | VisaProcessField       |
| Mastercard  | PEER02 | MastercardDelegateParser  | MastercardISOField| MastercardProcessField |

## Archivos de Configuracion

| Archivo                    | Proposito                                           |
|----------------------------|-----------------------------------------------------|
| `application.yml`          | Config principal (puerto 8083, perfil gw)           |
| `application-data.yml`     | Datos maestros: monedas, BINs, MCCs, bancos P2P     |
| `application-datalocal.yml`| Datos locales                                       |
| `log4j2.xml`               | Logging: consola + rotacion diaria (30 dias)        |

## Paneles GUI (v2 - Version Actual)

| Panel                                | Funcion                              |
|--------------------------------------|--------------------------------------|
| `ParseViewerPanel`                   | Parsear mensajes ISO8583             |
| `ParseClearViewerPanel`              | Parsear en texto claro               |
| `ConvertTramaOriginalViewerPanel`    | Convertir trama original             |
| `ConvertTramaOriginalVisaViewerPanel`| Convertir trama Visa                 |
| `Transformer20022Panel`              | Transformacion a ISO20022            |
| `GenerateTramaISO8583Panel`          | Generar tramas ISO8583               |
| `TLVParseViewerPanel`               | Parser de campos TLV (Campo 48)      |

## Convenciones de Codigo

- Usar **Lombok** para reducir boilerplate (@Data, @Builder, @Slf4j)
- Nuevos paneles GUI deben ir en `gui/panels/v2/`
- Nuevos campos ISO se agregan en los enums `VisaISOField` / `MastercardISOField`
- Para nueva red: crear Delegate (Parser + Mapper + FieldLogic) + ProcessField + ISOField enum
- Excepciones de dominio extienden las clases en `orchestrator.core.exception`
- Tests con JUnit 5 + Mockito

## Campos Especiales ISO8583

- **Campo 48 (TLV):** Datos adicionales del minorista, parseable por separado
- **Campo 52 (PIN):** Datos PIN encriptados (8 bytes hex)
- **Campo 64 (MAC):** Message Authentication Code (8 bytes hex)
- **Campos 49, 50, 51:** Monedas - Visa agrega '0' al inicio y ajusta con substring(1,4)

## Estructura de Distribucion (post-build)

```
target/dist/
├── gateway-simulator-2.0.0-SNAPSHOT.jar
├── libs/          # Dependencias
├── config/        # log4j2.xml, *.properties
└── logs/          # Logs de ejecucion
```

## Comandos Personalizados Claude Code

Disponibles via `/` en Claude Code:

| Comando          | Descripcion                                      |
|------------------|--------------------------------------------------|
| `/build`         | Compilar el proyecto                             |
| `/nueva-red`     | Guia para agregar soporte a nueva red de pagos   |
| `/nuevo-panel`   | Crear nuevo panel GUI en v2                      |
| `/debug-parse`   | Diagnosticar problemas de parseo ISO8583         |

## Errores Comunes y Soluciones

| Error                                    | Causa                        | Solucion                              |
|------------------------------------------|------------------------------|---------------------------------------|
| `BindException: BusinessData`            | Perfil `gw` no activo        | Agregar `-Dspring.profiles.active=gw` |
| `NullPointerException` en parser         | Campo no definido en enum     | Agregar campo a `VisaISOField` o `MastercardISOField` |
| Panel no aparece en GUI                  | No registrado en `ParserGUIMain` | Agregar panel al frame principal   |
| Campo 48 mal parseado                    | TLV requiere parser especial  | Usar `TLVParseViewerPanel` o `ISOUtil.parseTLV` |

## Decisiones de Arquitectura (ADRs)

- **v2 sobre v1:** Los paneles v1 estan deprecados, toda nueva funcionalidad GUI va en `panels/v2/`
- **MapStruct sobre manual:** El mapeo ISO8583→ISO20022 usa MapStruct para reducir errores
- **Spring Boot + Swing:** Swing gestiona la GUI, Spring gestiona los beans de negocio (no usar `new` para servicios)
- **Perfiles obligatorios:** El perfil `gw` carga datos sensibles y de negocio; sin el la app no arranca
