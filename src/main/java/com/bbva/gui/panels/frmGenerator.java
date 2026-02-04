package com.bbva.gui.panels;

import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.orchestrator.core.fields.MastercardISOField;
import com.bbva.orchestrator.core.fields.VisaISOField;
import com.bbva.orchestrator.core.fields.definitions.ISODataType;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import com.bbva.orchestrator.core.utils.FieldUtil;
import info.clearthought.layout.TableLayout;

import org.noos.xing.mydoggy.DockedTypeDescriptor;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.stream.IntStream;


public class frmGenerator extends JPanel {

    // Usamos estructuras de datos para evitar declarar 192 variables manuales
    private final Map<Integer, JCheckBox> checkBoxes = new TreeMap<>();
    private final Map<Integer, JTextField> textFields = new TreeMap<>();
    private final Map<Integer, JRadioButton> radioButtons = new TreeMap<>();
    private final List<Integer> camposPermitidos = Arrays.asList(2, 3, 4, 5, 6, 7, 9, 11, 12, 13, 14);
    private final JButton procesarButton = new JButton("Procesar");
    private final JTextArea inputTextArea = new JTextArea(4, 40);
    private final ParserFactory parserFactory;
    // Paleta de colores oficial
    public static final Color BBVA_NAVY = new Color(0, 68, 129);  // Azul principal
    public static final Color BBVA_WHITE = new Color(255, 255, 255);
    public static final Color BBVA_LIGHT_GRAY = new Color(244, 244, 244);
    public static final Color BBVA_ACCENT_BLUE = new Color(18, 190, 255); // Azul brillante para acentos

    public frmGenerator(BeanProviderInstance beanProviderInstance) {
        this.parserFactory = beanProviderInstance.parserFactory();
        initializeComponents();
        setupEventHandlers();
        estiloBotonProcesar();
    }


    private void initializeComponents() {
        // 1. Crear el panel de campos usando TableLayout
        JPanel panelCampos = new JPanel(new GridLayout(1, 3, 10, 0));
        panelCampos.add(crearColumnaTableLayout("P02 - P23", 2, 23));
        panelCampos.add(crearColumnaTableLayout("P24 - P45", 24, 45));
        panelCampos.add(crearColumnaTableLayout("P46 - P65", 46, 65));

        // 2. Panel superior para acciones
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.add(procesarButton);
        procesarButton.setFont(new Font("SansSerif", Font.BOLD, 12));

        // 3. Unir todo en un contenedor intermedio
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.add(headerPanel, BorderLayout.NORTH);
        mainContent.add(new JScrollPane(panelCampos), BorderLayout.CENTER);

        // 4. Inyectar en MyDoggy para manejo de ventanas pro
        setupMyDoggy(mainContent);
    }


    private JPanel crearColumnaTableLayout(String titulo, int inicio, int fin) {

        // Calculamos cuántas filas necesitamos realmente basándonos en los permitidos
        long filasVisibles = IntStream.rangeClosed(inicio, fin)
                .filter(camposPermitidos::contains)
                .count();

        if (filasVisibles == 0) return new JPanel();

        // Definición de columnas: Check(35), Input(160), Radio(80)
        double[][] size = {
                {55, 160, 80},
                new double[(int) filasVisibles]
        };
        // Altura de fila de 30px
        Arrays.fill(size[1], 25);

        JPanel panel = new JPanel(new TableLayout(size));
        panel.setBackground(BBVA_WHITE);

        // Título con estilo BBVA
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BBVA_NAVY, 1), titulo);
        border.setTitleColor(BBVA_NAVY);
        border.setTitleFont(new Font("SansSerif", Font.BOLD, 12));
        panel.setBorder(border);


        int row = 0;
        for (int i = inicio; i <= fin; i++) {

            if (camposPermitidos.contains(i)) {

                String fieldName = String.format("P%02d", i);

                // Crear CheckBox
                JCheckBox chk = new JCheckBox(fieldName, false);
                chk.setBackground(BBVA_WHITE);
                chk.setForeground(BBVA_NAVY);
                checkBoxes.put(i, chk);

                // Crear TextField con valores por defecto específicos
                JTextField txt = new JTextField(getDefaultValue(i));
                txt.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                        BorderFactory.createEmptyBorder(2, 5, 2, 5)));
                txt.setDisabledTextColor(BBVA_NAVY);
                txt.setBackground(BBVA_ACCENT_BLUE);
                txt.setEnabled(false);
                txt.setEditable(false);
                textFields.put(i, txt);

                // Crear RadioButton (Toggle Editable)
                JRadioButton rb = new JRadioButton("Edit", true);
                rb.setBackground(BBVA_WHITE);
                rb.setFont(new Font("SansSerif", Font.PLAIN, 10));
                rb.setSelected(false);
                radioButtons.put(i, rb);

                // Evento para habilitar/deshabilitar edición
                rb.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (rb.isSelected()) {
                            txt.setEditable(true);
                            txt.setEnabled(true);
                        } else {
                            txt.setEditable(false);
                            txt.setEnabled(false);
                        }
                    }
                });

                panel.add(chk, "0, " + row);
                panel.add(txt, "1, " + row);
                panel.add(rb, "2, " + row);

                // Guardar en los mapas (textFields, checkBoxes, etc.)
                row++;
            }
        }

        return panel;
    }

    private void setupMyDoggy(JPanel mainContent) {
        MyDoggyToolWindowManager windowManager = new MyDoggyToolWindowManager();

        // Configuración del área de texto (Consola)
        inputTextArea.setBackground(new Color(30, 30, 30)); // Fondo oscuro tipo terminal
        // inputTextArea.setBackground(BBVA_WHITE); // Fondo oscuro tipo terminal
        inputTextArea.setForeground(BBVA_ACCENT_BLUE);      // Letras azul neón
        inputTextArea.setCaretColor(Color.WHITE);
        inputTextArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

        ToolWindow toolWindow = windowManager.registerToolWindow(
                "TRAMA_ISO", "Output Trama ISO-8583", null,
                new JScrollPane(inputTextArea), ToolWindowAnchor.BOTTOM
        );

        // toolWindow.getTypeDescriptor(DockedTypeDescriptor.class).setEnabled(true);
        DockedTypeDescriptor descriptor = (DockedTypeDescriptor) toolWindow.getTypeDescriptor(DockedTypeDescriptor.class);
        if (descriptor != null) {
            descriptor.setDockLength(450); // Altura del panel
            descriptor.setPopupMenuEnabled(true);
        }
        toolWindow.setActive(true);

        // El panel principal ahora se añade al ContentManager de MyDoggy
        windowManager.getContentManager().addContent("Generador", "Visa/Mastercard", null, mainContent);

        // Añadir el manager al panel principal
        this.setLayout(new BorderLayout());
        this.add(windowManager, BorderLayout.CENTER);

    }

    private void setupEventHandlers() {
        procesarButton.addActionListener(this::procesarTrama);
    }

    private void procesarTrama(ActionEvent e) {

        if (!validarSeleccionMinima()) {
            return; // Detenemos el proceso si no hay suficientes campos
        }

        // 1. Obtener el parser específico
        ISO8583DelegateParser delegateParser = parserFactory.getDelegateParser("PEER02");

        // 2. Crear el mapa que contendrá SOLO los campos seleccionados
        Map<String, String> isoDataMap = new HashMap<>();

        // 3. Recorrer dinámicamente nuestra colección de checkboxes
        checkBoxes.forEach((id, chk) -> {
            // LÓGICA CLAVE: Solo si el checkbox está marcado se procesa el campo
            if (chk.isSelected()) {

                // Intentamos obtener la definición desde VisaISOField
                MastercardISOField fieldDef = MastercardISOField.getById(id);
                String value = textFields.get(id).getText();

                if (fieldDef != null) {
                    // Si existe en el Enum, usamos su nombre (ej. "processingCode")
                    if (fieldDef.getTypeData().equals(ISODataType.NUMERIC_DECIMAL)) {
                        // Convertimos el valor a formato ISO
                        Double amount = FieldUtil.convertAmountDouble(value);
                        if (amount != null) {
                            value = FieldUtil.convertAmountString(amount);
                        }
                    }
                    isoDataMap.put(fieldDef.getName(), value);
                } else {
                    // Si no está definido en el Enum, usamos el estándar "Pxx"
                    isoDataMap.put(String.format("P%02d", id), value);
                }
            }
            // Si no está seleccionado (chk.isSelected() == false),
            // simplemente no se agrega al mapa y el parser lo ignorará.
        });

        // 4. Agregar campos obligatorios de control (MTI)
        isoDataMap.put("messageType", "0100");

        // 5. Generar la trama
        try {
            String trama = delegateParser.unParser(isoDataMap);
            inputTextArea.setText(trama);

            // Opcional: Estilo BBVA para el éxito
            inputTextArea.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(BBVA_ACCENT_BLUE), "Trama Generada con Éxito"));

        } catch (Exception ex) {
            // Manejo de errores en caso de que algún dato no cumpla el formato ISO
            inputTextArea.setText("ERROR EN LA GENERACIÓN: " + ex.getMessage());
            inputTextArea.setForeground(Color.RED);
        }
    }

    private void estiloBotonProcesar() {
        procesarButton.setBackground(BBVA_NAVY);
        procesarButton.setForeground(BBVA_WHITE);
        procesarButton.setFocusPainted(false);
        procesarButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        procesarButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        procesarButton.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
    }

    /**
     * Valida que al menos 2 campos estén seleccionados para generar la trama.
     *
     * @return true si la validación es exitosa, false de lo contrario.
     */
    private boolean validarSeleccionMinima() {
        // Contamos los checkboxes seleccionados usando streams o un bucle simple
        long seleccionados = checkBoxes.values().stream()
                .filter(JCheckBox::isSelected)
                .count();

        if (seleccionados < 5) {
            JOptionPane.showMessageDialog(
                    this,
                    "Para generar una trama válida, debe seleccionar al menos 5 campos de la especificación.",
                    "Selección Insuficiente",
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }
        return true;
    }

    // Centralizamos los valores por defecto
    private String getDefaultValue(int i) {
        //format MMDDHHMMSS
        return switch (i) {
            case 2 -> "5536509999999999";
            case 3 -> "000000";
            case 4 -> "100.00";
            case 5 -> "200.00";
            case 6 -> "300.00";
            case 7 -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss"));
            case 8, 9, 10 -> "00000000";
            case 11 -> String.format("%06d", new java.util.Random().nextInt(1000000));
            case 12 -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd"));
            case 13 -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
            case 14 -> "2701";
            case 15 -> "0610";
            default -> "";
        };
    }


    private JPanel crearColumnaTableLayout2(String titulo, int inicio, int fin) {


        // Definición de columnas: Check(35), Input(160), Radio(80)
        double[][] size = {
                {50, 180, 80},
                new double[(fin - inicio) + 1]
        };

        // Altura de fila de 30px
        Arrays.fill(size[1], 25);

        JPanel panel = new JPanel(new TableLayout(size));
        panel.setBackground(BBVA_WHITE);

        // Título con estilo BBVA
        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BBVA_NAVY, 1), titulo);
        border.setTitleColor(BBVA_NAVY);
        border.setTitleFont(new Font("SansSerif", Font.BOLD, 12));
        panel.setBorder(border);


        int row = 0;
        for (int i = inicio; i <= fin; i++) {
            String fieldName = String.format("P%02d", i);

            // Crear CheckBox
            JCheckBox chk = new JCheckBox(fieldName, false);
            chk.setBackground(BBVA_WHITE);
            chk.setForeground(BBVA_NAVY);
            checkBoxes.put(i, chk);

            // Crear TextField con valores por defecto específicos
            JTextField txt = new JTextField(getDefaultValue(i));
            txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(2, 5, 2, 5)));
            txt.setDisabledTextColor(BBVA_NAVY);
            txt.setBackground(BBVA_ACCENT_BLUE);
            txt.setEnabled(false);
            txt.setEditable(false);
            textFields.put(i, txt);

            // Crear RadioButton (Toggle Editable)
            JRadioButton rb = new JRadioButton("Edit", true);
            rb.setBackground(BBVA_WHITE);
            rb.setFont(new Font("SansSerif", Font.PLAIN, 10));
            rb.setSelected(false);
            radioButtons.put(i, rb);

            // Evento para habilitar/deshabilitar edición
            rb.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (rb.isSelected()) {
                        txt.setEditable(true);
                        txt.setEnabled(true);
                    } else {
                        txt.setEditable(false);
                        txt.setEnabled(false);
                    }
                }
            });

            panel.add(chk, "0, " + row);
            panel.add(txt, "1, " + row);
            panel.add(rb, "2, " + row);

            // Guardar en los mapas (textFields, checkBoxes, etc.)
            row++;
        }


        return panel;
    }
}
