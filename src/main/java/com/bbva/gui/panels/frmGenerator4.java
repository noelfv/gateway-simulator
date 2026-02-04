package com.bbva.gui.panels;

import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.orchestrator.core.fields.VisaISOField;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import info.clearthought.layout.TableLayout;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public class frmGenerator4 extends JPanel {

    // Usamos estructuras de datos para evitar declarar 192 variables manuales
    private final Map<Integer, JCheckBox> checkBoxes = new TreeMap<>();
    private final Map<Integer, JTextField> textFields = new TreeMap<>();
    private final Map<Integer, JRadioButton> radioButtons = new TreeMap<>();

    private final JButton procesarButton = new JButton("Procesar");
    private final JTextArea inputTextArea = new JTextArea(4, 40);
    private final ParserFactory parserFactory;

    public frmGenerator4(BeanProviderInstance beanProviderInstance) {
        this.parserFactory = beanProviderInstance.parserFactory();
        initializeComponents();
        setupEventHandlers();
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
        // Definimos las proporciones de las 3 columnas internas
        // [30px para el Check, 120px para el Text, 70px para el Radio]
        double[][] size = {
                {50, 180, 80}, // Columnas
                new double[(fin - inicio) + 1] // Filas dinámicas
        };

        // Inicializamos todas las filas con un alto de 25px
        for (int i = 0; i < size[1].length; i++) {
            size[1][i] = 25;
        }

        JPanel panel = new JPanel(new TableLayout(size));
        panel.setBorder(BorderFactory.createTitledBorder(titulo));

        int row = 0;
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
            rb.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!rb.isSelected()) {
                        txt.setEditable(true);
                        txt.setEnabled(true);
                    } else {
                        txt.setEditable(false);
                        txt.setEnabled(false);
                    }
                }
            });
            // El formato de TableLayout es: "columna, fila"
            panel.add(chk, "0, " + row);
            panel.add(txt, "1, " + row);
            panel.add(rb, "2, " + row);
            row++;
        }
        return panel;
    }


    private void setupMyDoggy(JPanel panelContenedor) {
        MyDoggyToolWindowManager windowManager = new MyDoggyToolWindowManager();

        // Registramos el área de texto como una herramienta inferior
        ToolWindow toolWindow = windowManager.registerToolWindow(
                "Trama_ISO",               // ID
                "Visualizador de Trama",    // Título
                null,                       // Icono
                new JScrollPane(inputTextArea), // Componente
                ToolWindowAnchor.BOTTOM     // Ubicación inicial
        );

        toolWindow.setActive(true);

        // El panel principal ahora se añade al ContentManager de MyDoggy
        windowManager.getContentManager().addContent(
                "Generador", "Campos Visa", null, panelContenedor
        );

        // Añadimos el componente principal del manager al JPanel actual
        this.setLayout(new BorderLayout());
        this.add(windowManager, BorderLayout.CENTER);
    }



    private void setupEventHandlers() {
        procesarButton.addActionListener(this::procesarTrama);
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