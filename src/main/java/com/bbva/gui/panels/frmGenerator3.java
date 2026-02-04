package com.bbva.gui.panels;

import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.orchestrator.core.fields.VisaISOField;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class frmGenerator3 extends JPanel {

    // Usamos estructuras de datos para evitar declarar 192 variables manuales
    private final Map<Integer, JCheckBox> checkBoxes = new TreeMap<>();
    private final Map<Integer, JTextField> textFields = new TreeMap<>();
    private final Map<Integer, JRadioButton> radioButtons = new TreeMap<>();

    private final JButton procesarButton = new JButton("Procesar");
    private final JTextArea inputTextArea = new JTextArea(4, 40);
    private final ParserFactory parserFactory;

    public frmGenerator3(BeanProviderInstance beanProviderInstance) {
        this.parserFactory = beanProviderInstance.parserFactory();
        initializeComponents();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelPrincipal = new JPanel(new GridLayout(1, 3, 20, 10));

        // Definimos los rangos de las columnas para crearlas dinámicamente
        panelPrincipal.add(crearColumna("Campos P02 - P23", 2, 23));
        panelPrincipal.add(crearColumna("Campos P24 - P45", 24, 45));
        panelPrincipal.add(crearColumna("Campos P46 - P65", 46, 65));

        // Panel superior para el botón
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(procesarButton);

        // Panel inferior para el resultado
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(BorderFactory.createTitledBorder("Trama Generada"));
        panelInferior.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        add(buttonPanel, BorderLayout.NORTH);
        add(new JScrollPane(panelPrincipal), BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);
    }

    private JPanel crearColumna(String titulo, int inicio, int fin) {
        int filas = (fin - inicio) + 1;
        JPanel columna = new JPanel(new GridLayout(filas, 3, 4, 4));
        columna.setBorder(BorderFactory.createTitledBorder(titulo));

        for (int i = inicio; i <= fin; i++) {
            String fieldName = String.format("P%02d", i);

            // Crear CheckBox
            JCheckBox chk = new JCheckBox(fieldName, true);
            checkBoxes.put(i, chk);

            // Crear TextField con valores por defecto específicos
            JTextField txt = new JTextField(getDefaultValue(i), 12);
            textFields.put(i, txt);

            // Crear RadioButton (Toggle Editable)
            JRadioButton rb = new JRadioButton("Enabled", true);
            radioButtons.put(i, rb);

            // Evento para habilitar/deshabilitar edición
            rb.addActionListener(e -> txt.setEditable(!rb.isSelected()));

            columna.add(chk);
            columna.add(txt);
            columna.add(rb);
        }
        return columna;
    }

    private void setupEventHandlers() {
        procesarButton.addActionListener(this::procesarTrama);
    }

    private void procesarTrama2(ActionEvent e) {
        ISO8583DelegateParser delegateParser = parserFactory.getDelegateParser("PEER02");
        Map<String, String> isoDataMap = new HashMap<>();

        // Recorrer los mapas dinámicamente
        checkBoxes.forEach((id, chk) -> {
            if (chk.isSelected()) {
                String key = String.format("P%02d", id);
                isoDataMap.put(key, textFields.get(id).getText());
            }
        });

        isoDataMap.put("messageType", "0100");
        String trama = delegateParser.unParser(isoDataMap);
        inputTextArea.setText(trama);
    }


    private void procesarTrama(ActionEvent e) {
        ISO8583DelegateParser delegateParser = parserFactory.getDelegateParser("PEER02");

        // Este es el mapa final que enviamos al parser
        Map<String, String> isoDataMap = new HashMap<>();

        // Recorremos los checkboxes para ver qué campos están activos
        checkBoxes.forEach((id, chk) -> {
            if (chk.isSelected()) {
                // Buscamos la definición del campo en el Enum
                VisaISOField fieldDef = VisaISOField.getById(id);

                if (fieldDef != null) {
                    // Usamos el nombre descriptivo del Enum (ej. "systemTraceAuditNumber")
                    String fieldName = fieldDef.getName();
                    String value = textFields.get(id).getText();

                    isoDataMap.put(fieldName, value);
                } else {
                    // Opcional: Si el campo no está en el Enum, usar el formato Pxx por defecto
                    isoDataMap.put(String.format("P%02d", id), textFields.get(id).getText());
                }
            }
        });

        isoDataMap.put("messageType", "0100");

        // El delegateParser recibirá un mapa con nombres como "primaryAccountNumber", "processingCode", etc.
        String trama = delegateParser.unParser(isoDataMap);
        inputTextArea.setText(trama);
    }

    // Centralizamos los valores por defecto
    private String getDefaultValue(int i) {
        return switch (i) {
            case 2 -> "5536509999999999";
            case 3 -> "000000";
            case 4 -> "1000";
            case 5 -> "2000";
            case 6 -> "3000";
            case 7 -> "202406101200";
            case 8 -> "123456";
            case 9 -> "4000";
            case 10 -> "5000";
            case 15 -> "0610";
            default -> "";
        };
    }
}