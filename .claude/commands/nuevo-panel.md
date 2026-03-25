# Crear nuevo panel GUI

Crea un nuevo panel Swing en `gui/panels/v2/`.

Solicita al usuario:
1. Nombre del panel (ej: `MiNuevoPanel`)
2. Funcion principal del panel
3. Si necesita inputs (campos de texto, botones, etc.)

Pasos:
1. Leer un panel existente en `gui/panels/v2/` como referencia (ej: `ParseViewerPanel`)
2. Crear `{Nombre}Panel.java` en `gui/panels/v2/`
3. Usar `@Component` + `@Slf4j` de Lombok
4. Inyectar dependencias necesarias via constructor
5. Registrar el panel en `ParserGUIMain.java`

Convenciones GUI:
- Layout: TableLayout o GridBagLayout
- Botones: `JButton` con ActionListener lambda
- Texto: `JTextArea` con `JScrollPane`
- Logging: usar `log.info()` / `log.error()` no `System.out`
