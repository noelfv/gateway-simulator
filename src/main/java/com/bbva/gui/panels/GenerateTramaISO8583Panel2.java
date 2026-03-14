package com.bbva.gui.panels;


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
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.ArrayList;

import static com.bbva.gui.utils.ComponentsUtil.*;


public class GenerateTramaISO8583Panel2 extends JPanel {

    // Usamos estructuras de datos para evitar declarar 192 variables manuales
    private final Map<Integer, JCheckBox> checkBoxes = new TreeMap<>();
    private final Map<Integer, JTextField> textFields = new TreeMap<>();
    //private final Map<Integer, JRadioButton> radioButtons = new TreeMap<>();
    // Listas por tipo de operación: solo los campos relacionados a cada uno (carga inicial)
    private final List<Integer> listCompras = new ArrayList<>(Arrays.asList(2, 3, 4, 7, 11, 12, 13,14,19,20,25));
    private final List<Integer> listBilletera = new ArrayList<>(Arrays.asList(2, 3, 4, 7, 25, 32));
    private final List<Integer> listRetiros = new ArrayList<>(Arrays.asList(2, 3, 4, 7, 11, 41, 42));
    private JPanel mainContentPanel;
    private Set<Integer> camposMandatoriosCompras= new HashSet<>(Arrays.asList(2, 3, 4, 7, 11, 12, 13));
    private  Set<Integer> camposMandatoriosBilletera= new HashSet<>(Arrays.asList(2, 3, 4, 7, 25,32));
    private  Set<Integer> camposMandatoriosRetiros= new HashSet<>(Arrays.asList(2, 3, 4, 7, 11, 41,42));
    private JButton procesarButton ;
    private JComboBox<String> tipoOperacionComboBox;
    private JComboBox<String> procesarComboBox;
    private  JTextArea outputTextArea;
    private final ParserFactory parserFactory;

    public GenerateTramaISO8583Panel2(BeanProviderInstance beanProviderInstance) {
        this.parserFactory = beanProviderInstance.parserFactory();
        initializeComponents();
        setupEventHandlers();
    }


    private void initializeComponents() {
        mainContentPanel = new JPanel(new BorderLayout());
        mainContentPanel.add(createHeaderPanel(), BorderLayout.NORTH);
        mainContentPanel.add(new JScrollPane(createPanelCampos()), BorderLayout.CENTER);
        setupMyDoggy(mainContentPanel);
    }

    private List<Integer> getCamposActuales() {
        String tipo = tipoOperacionComboBox != null && tipoOperacionComboBox.getSelectedItem() != null
                ? tipoOperacionComboBox.getSelectedItem().toString()
                : "Compras";
        return switch (tipo) {
            case "Billetera" -> listBilletera;
            case "Retiros" -> listRetiros;
            default -> listCompras;
        };
    }

    private Set<Integer> getMandatoriosActuales() {
        String tipo = tipoOperacionComboBox != null && tipoOperacionComboBox.getSelectedItem() != null
                ? tipoOperacionComboBox.getSelectedItem().toString()
                : "Compras";
        return switch (tipo) {
            case "Billetera" -> camposMandatoriosBilletera;
            case "Retiros" -> camposMandatoriosRetiros;
            default -> camposMandatoriosCompras;
        };
    }

    private JPanel createHeaderPanel() {
        procesarComboBox=ComponentsUtil.createComboBox(new String[]{"Mastercard", "Visa"}, "Mastercard");
        tipoOperacionComboBox = ComponentsUtil.createComboBox(new String[]{"Compras", "Billetera", "Retiros"},"Compras");

        JLabel lblProcesar = new JLabel("Marca:");
        lblProcesar.setLabelFor(procesarComboBox);
        JLabel lblTipoOperacion = new JLabel("Tipo de operación:");
        lblTipoOperacion.setLabelFor(tipoOperacionComboBox);
        

        JPanel leftPanel = new JPanel(new GridLayout(2, 2, 8, 6));
        leftPanel.add(lblProcesar);
        leftPanel.add(procesarComboBox);
        leftPanel.add(lblTipoOperacion);
        leftPanel.add(tipoOperacionComboBox);
       

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.add(leftPanel);

        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BBVA_NAVY, 1), "Opciones");
        border.setTitleColor(BBVA_NAVY);
        border.setTitleFont(new Font("SansSerif", Font.BOLD, 12));
        border.setTitlePosition(TitledBorder.TOP);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        return headerPanel;
    }

    private JPanel createPanelCampos() {
        checkBoxes.clear();
        textFields.clear();
        List<Integer> camposActuales = getCamposActuales();
        JPanel panelCampos = new JPanel(new GridLayout(1, 3, 10, 0));
        int totalCampos = camposActuales.size();
        int camposPorColumna = (int) Math.ceil(totalCampos / 3.0);

        for (int col = 0; col < 3; col++) {
            int inicio = col * camposPorColumna;
            int fin = Math.min(inicio + camposPorColumna, totalCampos);
            if (inicio < totalCampos) {
                List<Integer> camposColumna = camposActuales.subList(inicio, fin);
                panelCampos.add(createMainPanel(
                        String.format("Bloque %d", col + 1),
                        camposColumna
                ));
            }
        }
        procesarButton = ComponentsUtil.createButton("Procesar", "procesar");
        procesarButton.addActionListener(this::procesarTrama);
        JButton btnAgregarCampo = ComponentsUtil.createButton("Agregar campo", "Agregar un nuevo campo");
        btnAgregarCampo.setBackground(Color.GREEN);
        btnAgregarCampo.addActionListener(e -> mostrarDialogoAgregarCampo());

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        topBar.add(btnAgregarCampo);
        topBar.add(procesarButton);

        JPanel wrapper = new JPanel(new BorderLayout(0, 6));
        wrapper.add(topBar, BorderLayout.NORTH);
        wrapper.add(panelCampos, BorderLayout.CENTER);

        TitledBorder border = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(BBVA_NAVY, 1), "Campos ISO-8583");
        border.setTitleColor(BBVA_NAVY);
        border.setTitleFont(new Font("SansSerif", Font.BOLD, 12));
        border.setTitlePosition(TitledBorder.TOP);
        wrapper.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(6, 8, 6, 8)));
        return wrapper;
    }

    private void refreshPanelCampos() {
        if (mainContentPanel == null) return;
        mainContentPanel.remove(1);
        mainContentPanel.add(new JScrollPane(createPanelCampos()), BorderLayout.CENTER);
        mainContentPanel.revalidate();
        mainContentPanel.repaint();
    }

    private void mostrarDialogoAgregarCampo() {
        JTextField txtNumero = new JTextField(10);
        txtNumero.setToolTipText("Ingrese el número de campo (ej: 2, 3, 48)");
        txtNumero.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                txtNumero.requestFocusInWindow();
            }
            @Override
            public void ancestorRemoved(AncestorEvent event) {}
            @Override
            public void ancestorMoved(AncestorEvent event) {}
        });
        Object[] message = {"Número de campo:", txtNumero};
        int option = JOptionPane.showConfirmDialog(this, message, "Agregar campo",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (option == JOptionPane.OK_OPTION) {
            String valor = txtNumero.getText();
            if (valor != null && !valor.isBlank()) {
                try {
                    int numero = Integer.parseInt(valor.trim());
                    List<Integer> listaActual = getCamposActuales();
                    if (listaActual.contains(numero)) {
                        JOptionPane.showMessageDialog(this, "El campo " + numero + " ya está en la lista.",
                                "Agregar campo", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        listaActual.add(numero);
                        Collections.sort(listaActual);
                        refreshPanelCampos();
                        actualizarValoresPorDefecto();
                        JOptionPane.showMessageDialog(this, "Campo " + numero + " agregado.",
                                "Agregar campo", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Debe ingresar un número válido.",
                            "Agregar campo", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
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
            if (getMandatoriosActuales().contains(i)) {
                chk.setSelected(true);
            }
            checkBoxes.put(i, chk);

            JTextField txt = new JTextField(getDefaultValue(i));
            txt.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                    BorderFactory.createEmptyBorder(2, 5, 2, 5)));
            txt.setDisabledTextColor(BBVA_NAVY);
            txt.setBackground(BBVA_ACCENT_BLUE);
            txt.setEnabled(false);
            txt.setEditable(false);
            txt.setSize(new Dimension(260, 25));
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
        tipoOperacionComboBox.addActionListener(e -> {
            refreshPanelCampos();
            actualizarValoresPorDefecto();
        });
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

                if (isValorVacio(value)) {
                    value = valorPorDefectoParaCampoVacio(fieldDef);
                }

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

    // Centralizamos los valores por defecto según tipo de operación
    private String getDefaultValue(int i) {
        String tipo = tipoOperacionComboBox != null && tipoOperacionComboBox.getSelectedItem() != null
                ? tipoOperacionComboBox.getSelectedItem().toString()
                : "Compras";

        //format MMDDHHMMSS
        return switch (tipo) {
            case "Billetera" -> switch (i) {
                case 2 -> "5536508888888888";
                case 3 -> "311000";       // Código ejemplo billetera
                case 4 -> "50.00";
                case 5 -> "100.00";
                case 6 -> "150.00";
                case 7 -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss"));
                case 8, 9, 10 -> "00000000";
                case 11 -> String.format("%06d", new java.util.Random().nextInt(1000000));
                case 12 -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd"));
                case 13 -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
                case 14 -> "2701";
                case 15 -> "0610";
                default -> "";
            };
            case "Retiros" -> switch (i) {
                case 2 -> "5536507777777777";
                case 3 -> "010000";       // Código ejemplo retiros
                case 4 -> "200.00";
                case 5 -> "400.00";
                case 6 -> "600.00";
                case 7 -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss"));
                case 8, 9, 10 -> "00000000";
                case 11 -> String.format("%06d", new java.util.Random().nextInt(1000000));
                case 12 -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMdd"));
                case 13 -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmmss"));
                case 14 -> "2701";
                case 15 -> "0610";
                default -> "";
            };
            // Compras (por defecto)
            default -> switch (i) {
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
        };
    }

    private void actualizarValoresPorDefecto() {
        // Determinar lista de campos mandatorios según el tipo seleccionado
        String tipo = tipoOperacionComboBox != null && tipoOperacionComboBox.getSelectedItem() != null
                ? tipoOperacionComboBox.getSelectedItem().toString()
                : "Compras";

        Set<Integer> mandatoriosActuales = switch (tipo) {
            case "Billetera" -> camposMandatoriosBilletera;
            case "Retiros" -> camposMandatoriosRetiros;
            default -> camposMandatoriosCompras;
        };

        for (Integer campo : getCamposActuales()) {
            // Actualizar valores por defecto si el campo no está en modo edición
            JTextField txt = textFields.get(campo);
            if (txt != null && !txt.isEditable()) {
                txt.setText(getDefaultValue(campo));
            }

            // Actualizar selección de checkboxes según mandatorios
            JCheckBox chk = checkBoxes.get(campo);
            if (chk != null) {
                chk.setSelected(mandatoriosActuales.contains(campo));
            }
        }
    }

    /** Indica si el valor del campo está vacío (null, vacío o solo espacios). */
    private boolean isValorVacio(String value) {
        return value == null || value.isBlank();
    }

    /** Devuelve el valor por defecto para un campo vacío según su definición ISO. */
    private String valorPorDefectoParaCampoVacio(MastercardISOField fieldDef) {
        if (fieldDef.isVariable()) {
            return fieldDef.getLength() == 2 ? "00" : "000";
        }
        if (fieldDef.getTypeData() == ISODataType.ALPHA_NUMERIC) {
            return completar(" ", fieldDef.getLength());
        }
        if (fieldDef.getTypeData() == ISODataType.NUMERIC) {
            return completar("0", fieldDef.getLength());
        }
        return completar(" ", fieldDef.getLength());
    }

    /**
     * Genera una cadena de longitud {@code length} rellena con el carácter indicado.
     * Si {@code padChar} es null o vacío, se usa espacio.
     */
    private String completar(String padChar, int length) {
        if (length <= 0) return "";
        char c = (padChar == null || padChar.isEmpty()) ? ' ' : padChar.charAt(0);
        return String.valueOf(c).repeat(length);
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
