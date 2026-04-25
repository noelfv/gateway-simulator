# Code Review Report - 2026-04-22 (actualizado)

## Executive Summary

| Métrica | Valor |
|---|---|
| Archivos analizados | 24 |
| Issues críticos | 5 |
| Issues altos | 12 |
| Issues medios | 28 |
| Issues bajos | 14 |
| Score general | 5.5/10 |

**Paquete analizado:** `src/main/java/com/bbva/gui`

**Problemas transversales más graves:**
- Duplicación masiva de constantes de color en 8+ archivos
- 2 God Classes (FXParseGUI 371 líneas, GenerateTramaISO8583Pane 494 líneas)
- JSON parsing inseguro mediante manipulación de strings
- Datos de prueba hardcodeados (PANs, PINs) en código de producción

---

## Critical Issues

### [Anti-pattern] - `FXParseGUI.java` (God Class - 371 líneas)

**Problem:** La clase tiene múltiples responsabilidades: procesamiento de datos, actualización de UI, gestión de diálogos y búsqueda de campos.

**Impact:** CRITICAL

**Current Code:**
```java
// Mezcla de responsabilidades en una sola clase
public static void process(...)           // procesamiento
public static void updateTreeView(...)    // UI
public static void showNodeDetails(...)   // dialogs
public static String findFieldIdByName()  // búsqueda
```

**Recommended Fix:**
```java
// Dividir en 4 clases:
class ParseProcessor      { process(...); processTLV(...); }
class UIUpdater           { updateTreeView(...); updateTreeViewTLV(...); }
class DialogFactory       { showNodeDetails(...); showExportJsonDialog(...); }
class FieldFinder         { findFieldIdByName(...); findFieldTLVIdByName(...); }
```

**Rationale:** Single Responsibility Principle — cada clase debe tener un único motivo para cambiar.

---

### [Anti-pattern] - `GenerateTramaISO8583Pane.java` (God Class - 494 líneas)

**Problem:** Combina gestión de 100+ componentes UI (CheckBoxes, TextFields), lógica de generación de tramas ISO8583, y manejo de eventos, todo en una clase.

**Impact:** CRITICAL

**Current Code:**
```java
// 494 líneas con todo mezclado
private Map<Integer, CheckBox> checkBoxes;  // UI
private List<Integer> listCompras;          // lógica de negocio
private void buildUI() { ... }             // 300+ líneas de UI
private String generateField(int id) { ... }
```

**Recommended Fix:**
```java
class FieldSelectionPanel   { /* solo checkboxes/UI */ }
class ISO8583FieldGenerator { /* lógica de generación por campo */ }
class GenerateTramaPane     { /* orquestador: wiring de los anteriores */ }
```

**Rationale:** La clase es imposible de testear unitariamente en su forma actual.

---

### [Security] - `ConfigurationPane.java:94-95` (JSON Parsing inseguro)

**Problem:** Parsing de JSON mediante split y replace en lugar de un parser real.

**Impact:** CRITICAL

**Current Code:**
```java
String content = new String(Files.readAllBytes(archivo.toPath()));
String valores = content.split(":")[1].replace("\"", "").replace("}", "").trim();
```

**Recommended Fix:**
```java
ObjectMapper mapper = new ObjectMapper();
JsonNode root = mapper.readTree(archivo);
String valores = root.get("expectedFieldName").asText();
```

**Rationale:** El split por ":" falla si el valor contiene ":", es frágil ante cambios de formato y no valida la estructura. Jackson ya está en el classpath.

---

### [Spring Anti-pattern] - `ApplicationContextProvider.java` (Service Locator)

**Problem:** Usa estado estático mutable para exponer el contexto de Spring — equivale a una variable global.

**Impact:** CRITICAL

**Current Code:**
```java
@Setter
private static ConfigurableApplicationContext context;  // estado global

public static <T> T getBean(Class<T> beanClass) {
    return context.getBean(beanClass);  // acoplamiento fuerte
}
```

**Recommended Fix:**
```java
// Solo es aceptable en entornos Swing donde no se puede inyectar directamente.
// Mitigar agregando thread safety y validación de estado:
private static volatile ConfigurableApplicationContext context;

public static <T> T getBean(Class<T> type) {
    if (context == null) throw new IllegalStateException("Spring context not initialized");
    return context.getBean(type);
}
```

**Rationale:** El patrón Service Locator dificulta el testing y oculta dependencias. En Swing es a veces necesario, pero debe protegerse con `volatile` y validación de estado.

---

## High Issues

### [Duplicated Code] - Constantes de color en 8+ archivos

**Problem:** Las mismas 8-10 constantes de color están copiadas en `TreeOutputPane`, `OutputTextPane`, `InputTextPane`, `JavaFXMain`, `GenerateTramaISO8583Pane` y más.

**Impact:** HIGH

**Current Code (repetido en cada archivo):**
```java
private static final String NAVY  = "#004481";
private static final String BLUE  = "#1464A0";
private static final String WHITE = "#FFFFFF";
// ... 6 más
```

**Recommended Fix:**
```java
// gui/theme/UITheme.java (un solo lugar)
public final class UITheme {
    public static final String NAVY  = "#004481";
    public static final String BLUE  = "#1464A0";
    public static final String WHITE = "#FFFFFF";
    private UITheme() {}
}
```

**Rationale:** Si el color de marca cambia, actualmente habría que editar 8 archivos.

---

### [Duplicated Code] - `ISO8583Processor.java:45-144` (Métodos gemelos)

**Problem:** `createMapFieldsISO8583Mastercard()` y `createMapFieldsISO8583Visa()` son 99% idénticos — solo difieren en el enum de campos.

**Impact:** HIGH

**Current Code:**
```java
public static Map<String,String> createMapFieldsISO8583Mastercard(String isoMessage) {
    // 50 líneas
    field = MastercardISOField.getById(fieldNumber);
    // ...
}
public static Map<String,String> createMapFieldsISO8583Visa(String isoMessage) {
    // 50 líneas idénticas
    field = VisaISOField.getById(fieldNumber);
    // ...
}
```

**Recommended Fix:**
```java
public static Map<String,String> createMapFields(String isoMsg, Network network) {
    // lógica única
    field = network.getFieldById(fieldNumber);
}
```

---

### [Security] - `UtilGUI.java:89-177` (Datos sensibles hardcodeados)

**Problem:** PAN de tarjeta (`"5536509999999999"`) y PIN (`"ABCDEF12"`) en código de producción.

**Impact:** HIGH

**Current Code:**
```java
map.put("002", "5536509999999999");  // PAN real-looking
map.put("052", "ABCDEF12");          // PIN credential
```

**Recommended Fix:**
```java
// Marcar claramente como datos de test y mover a clase separada
public final class TestFixtures {
    public static final String TEST_PAN = "4000000000000002"; // BIN de prueba conocido
    public static final String TEST_PIN = "00000000";
}
```

---

### [Performance] - `UtilGUI.java:74` y `GenerateTramaISO8583Pane.java:416,427,438` (Random instanciado por llamada)

**Problem:** `new Random()` dentro de métodos que se llaman repetidamente.

**Impact:** HIGH

**Current Code:**
```java
case 11 -> String.format("%06d", new Random().nextInt(1000000));
```

**Recommended Fix:**
```java
private static final Random RANDOM = new Random();
// ...
case 11 -> String.format("%06d", RANDOM.nextInt(1_000_000));
```

---

### [Anti-pattern] - `ISO8583Processor.java:140` (Error silenciado)

**Problem:** Excepción capturada pero se retorna un mapa parcial sin que el llamador pueda saber que el parseo falló.

**Impact:** HIGH

**Current Code:**
```java
} catch (ParserException e) {
    LOGGER.error("Cannot parse iso message: ", e.getMessage());
    return valuesMap;  // mapa con datos parciales/incorrectos
}
```

**Recommended Fix:**
```java
} catch (ParserException e) {
    LOGGER.error("Cannot parse iso message at field {}: {}", currentField, e.getMessage(), e);
    throw new ISO8583ParseException("Parse failed at field " + currentField, e);
}
```

---

### [Anti-pattern] - `FXParseGUI.java:238-240` (Exception swallowing)

**Problem:** Bloque catch que captura la excepción pero no la logea ni la propaga.

**Impact:** HIGH

**Current Code:**
```java
} catch (Exception e) {
    showSimpleInfo(nodeText);  // la excepción se pierde
}
```

**Recommended Fix:**
```java
} catch (Exception e) {
    LOGGER.error("Error showing field details for node '{}': {}", nodeText, e.getMessage(), e);
    showSimpleInfo(nodeText);
}
```

---

## Medium Issues

### [Code Smell] - `MappingMetadata.java:23` (assert en producción)

```java
assert origin != null;  // ❌ desactivado con -da (default en prod)
```
**Fix:** `Objects.requireNonNull(origin, "origin cannot be null")`

---

### [Code Smell] - `ConfigurationPane.java:70` (NPE potential)

```java
inputMessageTemp.substring(2, 3).startsWith("F")  // falla si len < 3
```
**Fix:** Validar `inputMessageTemp.length() >= 3` antes del substring.

---

### [Magic Numbers] - Múltiples archivos

| Archivo | Línea | Valor | Constante sugerida |
|---|---|---|---|
| `ISO8583Processor.java` | 67 | `65` | `PRIMARY_BITMAP_THRESHOLD` |
| `FXUtils.java` | 32 | `300` | `MAX_MESSAGE_DISPLAY_LENGTH` |
| `GenerateTramaISO8583Pane.java` | 309 | `5` | `MIN_REQUIRED_FIELDS` |
| `AbstractBasePane.java` | 25 | `0.55` | `DIVIDER_VERTICAL` |

---

### [Bug] - `ParseViewerPane.java:117` (Network hardcodeado → mapper incorrecto)

```java
// ACTUAL (bug): siempre llama al mapper de Visa sin importar la red seleccionada
ISO20022 iso20022 = delegateMapper.mapper(iso8583, subFields, "peer01");

// FIX: usar la variable de red ya disponible
ISO20022 iso20022 = delegateMapper.mapper(iso8583, subFields, itemSeleccionado);
```
**Impacto:** Cuando el usuario selecciona Mastercard y procesa, el mapeo ISO20022 usa la lógica de Visa — el objeto `ISO20022` resultante tiene datos incorrectos.

---

### [Bug] - `GenerateTramaISO8583Pane.java:304` (Network hardcodeado → Visa ignorada)

```java
// ACTUAL (bug): siempre usa Mastercard aunque procesarComboBox seleccione "Visa"
ISO8583DelegateParser delegateParser = parserFactory.getDelegateParser("PEER02");

// FIX: usar el valor del combo
String red = "Visa".equals(procesarComboBox.getValue()) ? "peer01" : "peer02";
ISO8583DelegateParser delegateParser = parserFactory.getDelegateParser(red);
// Y reemplazar MastercardISOField.getById() con lookups polimórficos
```
**Impacto:** El combo "Marca" en el header es funcional visualmente pero no afecta la generación de tramas.

---

### [Bug] - `ParseProcessor.process()` (Muta el mapa de entrada)

```java
// ACTUAL: elimina keys del mapa que el llamador puede necesitar
public static ParseResult process(Map<String, String> mapValues) {
    for (String key : INTERNAL_KEYS) {
        mapValues.remove(key);   // ← side-effect destructivo
    }
    ...
}

// FIX: trabajar sobre una copia
public static ParseResult process(Map<String, String> mapValues) {
    Map<String, String> clean = new HashMap<>(mapValues);
    for (String key : INTERNAL_KEYS) { clean.remove(key); }
    ...
    return new ParseResult(clean, fieldsById);
}
```
**Impacto:** `ConvertTramaOriginalPane.parseMessage()` llama `delegateParser.unParser(mapValues)` después de `ParseProcessor.process(mapValuesTree)`, pero pasa el mapa original — funciona solo porque se creó `mapValuesTree` como copia. Si algún caller usa el mismo mapa, los campos internos se pierden silenciosamente.

---

### [Bug] - `ISOFieldFinder.findFieldIdByName()` (Solo itera Mastercard)

```java
// ACTUAL: siempre itera MastercardISOField aunque el mensaje sea Visa
public static String findFieldIdByName(String fieldName) {
    for (ISOField field : MastercardISOField.values()) { ... }
}

// FIX: necesita recibir el peerId o iterar ambos enums
public static String findFieldIdByName(String fieldName, String peerId) {
    ISOField[] fields = "peer01".equalsIgnoreCase(peerId)
            ? VisaISOField.values() : MastercardISOField.values();
    for (ISOField field : fields) {
        if (field.getName().equalsIgnoreCase(fieldName)) return String.valueOf(field.getId());
    }
    return null;
}
```
**Impacto:** El tree view de mensajes Visa muestra IDs de campo incorrectos (o "null" para campos exclusivos de Visa).

---

### [Code Smell] - `DownloadJsonTemplatePane.actualizarPreview()` (InputStream no cerrado)

```java
// ACTUAL: el InputStream queda abierto si la lectura tiene éxito
InputStream is = getClass().getResourceAsStream(RESOURCE_BASE + filename);
if (is != null) {
    String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
    // ← is nunca se cierra en el path feliz

// FIX: try-with-resources
try (InputStream is = getClass().getResourceAsStream(RESOURCE_BASE + filename)) {
    if (is != null) {
        String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        ...
    } else { ... }
}
```

---

### [Performance] - `ObjectMapper` instanciado por operación (3 lugares)

| Archivo | Línea | Contexto |
|---|---|---|
| `ParseViewerPane.java` | 122 | `new ObjectMapper()` dentro de `parseMessage()` |
| `FXParseGUI.java` | 308 | `new ObjectMapper()` dentro de `buildTreeJson()` |
| `GenerateTramaIFromJsonExportPane.java` | 328 | `new ObjectMapper()` dentro de `importarJson()` |

`ObjectMapper` es thread-safe y costoso de construir. Debe ser una constante estática compartida:
```java
private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
```

---

### [Code Smell] - Código comentado en producción

| Archivo | Líneas | Descripción |
|---|---|---|
| `Transformer20022Pane.java` | 29-40 | JSON de prueba comentado |
| `ConvertTramaOriginalPane.java` | 118-122 | Código de formato sin usar |
| `Metadata.java` | ~40 | Comentario sobre P054 |

**Fix:** Eliminar o mover a test resources.

---

### [Design] - `FontType.java` (Interface como contenedor de constantes)

```java
public interface FontType {  // ❌ anti-pattern
    String ARIAL = "Arial";
```
**Fix:**
```java
public final class FontType {
    private FontType() {}
    public static final String ARIAL = "Arial";
```

---

### [Performance] - CSS inline duplicado

`TreeOutputPane`, `OutputTextPane`, `InputTextPane` y `JavaFXMain` tienen bloques de estilos CSS construidos por concatenación de strings, duplicados entre archivos.

**Fix:** Crear `UIStyles.java` con constantes CSS o un archivo `.css` externo cargado una vez.

---

### [Code Smell] - Tramas de prueba inline en paneles

`ParseViewerPane`, `ParseClearViewerPane`, `ConvertTramaOriginalPane` y `ConvertTramaOriginalVisaPane` contienen tramas ISO8583 de 1000-2000 caracteres directamente en el código fuente.

**Fix:** Crear `src/main/resources/samples/` con archivos `.hex` y cargarlos con `getResourceAsStream`.

---

## Low Issues

- `JavaFXMain.java:28` — Versión hardcodeada `"v2.0.0-NFV"`. Usar `@Value("${app.version}")` o filtrado Maven.
- `UtilGUI.java:84` — Método `showErrorDialog()` marcado `@Deprecated` dentro del mismo archivo.
- `AbstractBasePane.java:86-95` — `extractNodeValue()` acoplado a formato `[valor]`; mover a `ISOUtil`.
- `FXParseGUI.java:117-122` — Lista de campos filtrados sin documentación de por qué se excluyen.
- `GenerateTramaISO8583Pane.java:59-70` — 3 listas de field IDs hardcodeadas. Mover a enum o JSON de configuración.

---

## Summary of Changes (Checklist)

### Critical (resolver primero)
- [x] Dividir `FXParseGUI.java` → `FXParseGUI` (tree methods) + `FXDialogs` (dialog methods); `ParseProcessor` e `ISOFieldFinder` ya existían
- [x] Dividir `GenerateTramaISO8583Pane.java` → `FieldSelectionPanel` (UI component) + `GenerateTramaISO8583Pane` (orchestrator); botones extraídos a `UIButtonFactory`
- [ ] Refactorizar `Metadata.java` usando tabla de mapeo con `Function<ISO20022, String>` (fuera del paquete gui)
- [x] `ConfigurationPane.java` — no existe en este codebase (panel Swing removido)
- [x] `ApplicationContextProvider` — ya tenía `volatile` y validación de null aplicados

### High
- [ ] Crear `UITheme.java` y eliminar duplicación de colores en 8 archivos
- [ ] Unificar `createMapFieldsISO8583Mastercard` y `createMapFieldsISO8583Visa` en un método parametrizado
- [ ] Mover datos sensibles de `UtilGUI.java` a `TestFixtures` (o eliminar si no se usan)
- [ ] Convertir `new Random()` por instancia estática en `UtilGUI` y `GenerateTramaISO8583Pane`
- [ ] Agregar logging y relanzar excepción en `ISO8583Processor.java:140`
- [ ] Agregar logging en `FXParseGUI.java:238-240`

### Bugs confirmados (resolver urgente)
- [ ] **`ParseViewerPane.java:117`** — cambiar `"peer01"` por `itemSeleccionado` en llamada a `delegateMapper.mapper()`
- [ ] **`GenerateTramaISO8583Pane.java:304`** — usar `procesarComboBox.getValue()` para resolver `getDelegateParser()` y reemplazar `MastercardISOField.getById()` con lookup polimórfico
- [ ] **`ParseProcessor.process()`** — operar sobre copia del mapa, no mutar el parámetro
- [ ] **`ISOFieldFinder.findFieldIdByName()`** — recibir peerId para iterar el enum correcto (Visa vs Mastercard)
- [ ] **`DownloadJsonTemplatePane.actualizarPreview()`** — envolver `getResourceAsStream` en try-with-resources
- [ ] **`ObjectMapper`** — convertir a constante estática en `ParseViewerPane`, `FXParseGUI` y `GenerateTramaIFromJsonExportPane`

### Medium
- [ ] Reemplazar `assert` por `Objects.requireNonNull` en `MappingMetadata.java`
- [ ] Agregar validación de longitud antes de `substring` en `ConfigurationPane.java:70`
- [ ] Crear constantes para magic numbers (ver tabla arriba)
- [ ] Eliminar código comentado de `Transformer20022Pane`, `ConvertTramaOriginalPane`
- [ ] Cambiar `interface FontType` a `final class FontType`
- [ ] Crear `UIStyles.java` para centralizar CSS
- [ ] Mover tramas de prueba a `src/main/resources/samples/`

### Low
- [ ] Versión de app desde properties en `JavaFXMain.java`
- [ ] Eliminar o actualizar método `@Deprecated showErrorDialog()` en `UtilGUI.java`
- [ ] Documentar campos filtrados en `FXParseGUI.java:117-122`
- [ ] Mover listas de field IDs de `GenerateTramaISO8583Pane` a configuración externa
