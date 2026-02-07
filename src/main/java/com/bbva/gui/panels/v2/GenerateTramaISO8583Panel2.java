package com.bbva.gui.panels.v2;

import com.bbva.gui.components.PanelDoggy;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.ComponentsUtil;
import com.bbva.orchestrator.core.fields.MastercardISOField;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

import static com.bbva.gui.utils.ComponentsUtil.*;


public class GenerateTramaISO8583Panel2 extends JPanel {

    // Usamos estructuras de datos para evitar declarar 192 variables manuales
    private final Map<Integer, JCheckBox> checkBoxes = new TreeMap<>();
    private final Map<Integer, JTextField> textFields = new TreeMap<>();
    //private final Map<Integer, JRadioButton> radioButtons = new TreeMap<>();
    private final List<Integer> camposPermitidos = Arrays.asList(2, 3, 4, 5, 6, 7, 9, 11, 12, 13, 14,15,16,18,20,22,26,32,33,34,35,37,38,39,41,42,43,48,49,50,51,52,54,55,60,61,62,63,73,95,104,112,120,127);
    private JButton procesarButton ;
    private  JTextArea outputTextArea;
    private final ParserFactory parserFactory;

    public GenerateTramaISO8583Panel2(BeanProviderInstance beanProviderInstance) {
        this.parserFactory = beanProviderInstance.parserFactory();
        initializeComponents();
        //createPanelsLayout();
        setupEventHandlers();
    }


    private void initializeComponents() {
        // 1. Crear el panel de campos usando TableLayout
        JPanel panelCampos = new JPanel(new GridLayout(1, 3, 10, 0));
        // Distribuir campos equitativamente en 3 columnas
        int totalCampos = camposPermitidos.size();
        int camposPorColumna = (int) Math.ceil(totalCampos / 3.0);

        for (int col = 0; col < 3; col++) {
            int inicio = col * camposPorColumna;
            int fin = Math.min(inicio + camposPorColumna, totalCampos);

            if (inicio < totalCampos) {
                // Obtener los campos correspondientes a esta columna
                List<Integer> camposColumna = camposPermitidos.subList(inicio, fin);
                panelCampos.add(createMainPanel(
                        String.format("Bloque %d", col + 1),
                        camposColumna
                ));
            }
        }

        // 2. Panel superior para acciones
        procesarButton=ComponentsUtil.createButton("Procesar","procesar");
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        headerPanel.add(procesarButton);

        // 3. Unir todo en un contenedor intermedio
        JPanel mainContent = new JPanel(new BorderLayout());
        mainContent.add(headerPanel, BorderLayout.NORTH);
        mainContent.add(new JScrollPane(panelCampos), BorderLayout.CENTER);

        // 4. Inyectar en MyDoggy para manejo de ventanas pro
        setupMyDoggy(mainContent);
    }

    private void createPanelsLayout() {
        //MyDoggyToolWindowManager toolWindowManager = PanelDoggy.setupStructureMyDoggy(createMainPanel(), resultTree, createOutputPanel());
        MyDoggyToolWindowManager toolWindowManager = PanelDoggy.setupStructureMyDoggy(createMainPanel("Hola",camposPermitidos), createOutputPanel());
        add(toolWindowManager, BorderLayout.CENTER);
    }

    private JPanel createMainPanel(String titulo, List<Integer> campos) {
        if (campos.isEmpty()) return new JPanel();

        double[][] size = {
                {55, 160, 80},
                new double[campos.size()]
        };
        Arrays.fill(size[1], 25);

        JPanel panel = new JPanel(new TableLayout(size));
        panel.setBackground(BBVA_WHITE);

        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BBVA_NAVY, 1), titulo);
        border.setTitleColor(BBVA_NAVY);
        border.setTitleFont(new Font("SansSerif", Font.BOLD, 12));
        panel.setBorder(border);

        int row = 0;
        for (int i : campos) {
            String fieldName = String.format("P%02d", i);

            JCheckBox chk = new JCheckBox(fieldName, false);
            chk.setBackground(BBVA_WHITE);
            chk.setForeground(BBVA_NAVY);
            checkBoxes.put(i, chk);

            JTextField txt = new JTextField(getDefaultValue(i));
            txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(2, 5, 2, 5)));
            txt.setDisabledTextColor(BBVA_NAVY);
            txt.setBackground(BBVA_ACCENT_BLUE);
            txt.setEnabled(false);
            txt.setEditable(false);
            textFields.put(i, txt);

            JRadioButton rb = new JRadioButton("Edit", false);
            rb.setBackground(BBVA_WHITE);
            rb.setFont(new Font("SansSerif", Font.PLAIN, 10));
            //radioButtons.put(i, rb);

            rb.addActionListener(e -> {
                txt.setEditable(rb.isSelected());
                txt.setEnabled(rb.isSelected());
            });

            panel.add(chk, "0, " + row);
            panel.add(txt, "1, " + row);
            panel.add(rb, "2, " + row);
            row++;
        }

        return panel;
    }

    private JPanel createOutputPanel() {

        // --- PANEL DE CONSOLA PERSONALIZADO ---
        JPanel outputPanel = new JPanel(new BorderLayout());
        outputTextArea = ComponentsUtil.createOutputTextArea();

        // Botón de Copiar con estilo BBVA
        JButton btnCopiar =ComponentsUtil.createButton("Copiar Trama","Copiar trama al portapapeles");
        btnCopiar.addActionListener(e -> copiarAlPortapapeles());

        // Botón de limpiar con estilo BBVA
        JButton btnLimpiar= ComponentsUtil.createButton("Limpiar","");
        btnLimpiar.addActionListener(e -> limpiar());

        // Panel de herramientas para la consola
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 2));
        toolBar.setBackground(BBVA_LIGHT_GRAY);
        toolBar.add(btnCopiar);
        toolBar.add(btnLimpiar);

        // Unir componentes
        outputPanel.add(toolBar, BorderLayout.NORTH);
        outputPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        return outputPanel;
    }


    private void setupMyDoggy(JPanel mainContent) {
        MyDoggyToolWindowManager windowManager = new MyDoggyToolWindowManager();

        // --- PANEL DE CONSOLA PERSONALIZADO ---
        JPanel consolaPanel = new JPanel(new BorderLayout());
        outputTextArea = ComponentsUtil.createOutputTextArea();

        // Botón de Copiar con estilo BBVA
        JButton btnCopiar =ComponentsUtil.createButton("Copiar Trama","Copiar trama al portapapeles");
        btnCopiar.addActionListener(e -> copiarAlPortapapeles());

        // Botón de limpiar con estilo BBVA
        JButton btnLimpiar= ComponentsUtil.createButton("Limpiar","");
        btnLimpiar.addActionListener(e -> limpiar());

        // Panel de herramientas para la consola
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 2));
        toolBar.setBackground(BBVA_LIGHT_GRAY);
        toolBar.add(btnCopiar);
        toolBar.add(btnLimpiar);

        // Unir componentes
        consolaPanel.add(toolBar, BorderLayout.NORTH);
        consolaPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        // --- REGISTRO EN MYDOGGY ---
        ToolWindow toolWindow = windowManager.registerToolWindow(
                "TRAMA_ISO", "Output Trama ISO-8583",
                null,
                consolaPanel, // Pasamos el nuevo panel con botón
                ToolWindowAnchor.BOTTOM
        );

        DockedTypeDescriptor descriptor = (DockedTypeDescriptor) toolWindow.getTypeDescriptor(DockedTypeDescriptor.class);
        if (descriptor != null) {
            descriptor.setDockLength(450); // Altura del panel
            descriptor.setPopupMenuEnabled(true);
        }
        toolWindow.setActive(true);

        this.setLayout(new BorderLayout());
        this.add(windowManager, BorderLayout.CENTER);

        windowManager.getContentManager().addContent("Generador", "Campos Visa", null, mainContent);
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
            outputTextArea.setText(trama);

            // Opcional: Estilo BBVA para el éxito
            outputTextArea.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(BBVA_ACCENT_BLUE), "Trama Generada con Éxito"));

        } catch (Exception ex) {
            // Manejo de errores en caso de que algún dato no cumpla el formato ISO
            outputTextArea.setText("ERROR EN LA GENERACIÓN: " + ex.getMessage());
            outputTextArea.setForeground(Color.RED);
        }
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




    private void copiarAlPortapapeles() {
        String trama = outputTextArea.getText();
        if (!trama.isEmpty()) {
            java.awt.datatransfer.StringSelection selection = new java.awt.datatransfer.StringSelection(trama);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);

            // Opcional: Mostrar un feedback visual rápido
            JOptionPane.showMessageDialog(this, "Trama copiada al portapapeles", "BBVA Generator", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void limpiar() {
        outputTextArea.setText("");
    }


}
