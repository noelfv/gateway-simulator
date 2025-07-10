package org.example.gui;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.formdev.flatlaf.FlatLightLaf;
import org.example.orchestrator.MCMessageParserImpl;
import org.example.orchestrator.common.DataTypeISO8583;
import org.example.orchestrator.common.ISOUtil;
import org.example.orchestrator.common.ParseResult;
import org.example.orchestrator.dto.ISO20022;
import org.example.orchestrator.iso20022.ISO8583To20022Mapper;
import org.example.orchestrator.iso8583.ISO8583;
import org.example.orchestrator.iso8583.ISO8583Builder;
import org.example.orchestrator.mastercard.ISOFieldMastercard;
import org.noos.xing.mydoggy.*;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MastercardParserGUI2 extends JFrame {

    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JTextArea comparisonTextArea;
    private JTree resultTree;
    private DefaultTreeModel treeModel;
    private JButton parseButton;
    private JButton unparseCleanButton;
    private JButton unparseEBCDICButton;
    private JButton compareButton;
    private JMenuBar menuBar;
    private JMenu parseMenu;
    private JMenu conversionMenu;
    private JMenuItem parseMenuItem;
    private JMenuItem clearMenuItem;
    private JMenuItem ebcdicMenuItem;
    private MCMessageParserImpl messageParser;
    private Map<String, String> currentMappedFields;
    private ToolWindowManager toolWindowManager;
    private Map<String, String> currentMappedFieldsByDescription;
    private Map<String, String> currentMappedFieldsById;
    private ISO8583 iso8583;
    private ISO20022 iso20022;

    public MastercardParserGUI2() {
        messageParser = new MCMessageParserImpl();
        initializeComponents();
        initializeMenu();
        setupMyDoggy();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setTitle("Mastercard Message Parser");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);

        inputTextArea = new JTextArea(8, 60);
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        outputTextArea = new JTextArea(12, 60);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        outputTextArea.setEditable(false);

        comparisonTextArea = new JTextArea(5, 60);
        comparisonTextArea.setLineWrap(true);
        comparisonTextArea.setWrapStyleWord(true);
        comparisonTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        comparisonTextArea.setEditable(false);

        // Inicializar el árbol jerárquico
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mensaje Parseado");
        treeModel = new DefaultTreeModel(root);
        resultTree = new JTree(treeModel);
        resultTree.setCellRenderer(new NoIconTreeCellRenderer());
        resultTree.setRootVisible(true);

        parseButton = new JButton("Parser");
        unparseCleanButton = new JButton("Unparser (Clean)");
        unparseEBCDICButton = new JButton("Unparser (EBCDIC)");
        compareButton = new JButton("Compare");
        compareButton.setVisible(false);

        unparseCleanButton.setEnabled(false);
        unparseEBCDICButton.setEnabled(false);
    }


    private void initializeMenu() {
        // Crear la barra de menú
        menuBar = new JMenuBar();

        // Crear menú "Parse"
        parseMenu = new JMenu("Parse");
        parseMenuItem = new JMenuItem("Parsear mensaje");
        parseMenu.add(parseMenuItem);

        // Crear menú "Conversión" con submenús
        conversionMenu = new JMenu("Conversión");
        clearMenuItem = new JMenuItem("Clear");
        ebcdicMenuItem = new JMenuItem("EBCDIC");
        conversionMenu.add(clearMenuItem);
        conversionMenu.add(ebcdicMenuItem);

        // Agregar menús a la barra
        menuBar.add(parseMenu);
        menuBar.add(conversionMenu);

        // Establecer la barra de menú en el frame
        setJMenuBar(menuBar);

        // Configurar los listeners para los menús
        parseMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseMessage();
            }
        });

        clearMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unparseMessage(true);
            }
        });

        ebcdicMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unparseMessage(false);
                // Mostrar el tool window de comparación
                ToolWindow comparisonToolWindow = toolWindowManager.getToolWindow("comparison");
                comparisonToolWindow.setAvailable(true);
                compareButton.setVisible(true);
            }
        });

        // Deshabilitar opciones de conversión hasta que se parsee un mensaje
        clearMenuItem.setEnabled(false);
        ebcdicMenuItem.setEnabled(false);
    }


    private void setupMyDoggy() {
        toolWindowManager = new MyDoggyToolWindowManager();

        // Panel principal con entrada y botones
        JPanel mainPanel = createMainPanel();

        // Agregar el panel principal como contenido central
        toolWindowManager.getContentManager().addContent("main", "Parser Principal", null, mainPanel);

        // Tool Window para el árbol jerárquico
        ToolWindow treeToolWindow = toolWindowManager.registerToolWindow("tree",
                "Estructura Jerárquica", null, new JScrollPane(resultTree), ToolWindowAnchor.LEFT);
        treeToolWindow.setAvailable(true);

        // Tool Window para resultados
        ToolWindow outputToolWindow = toolWindowManager.registerToolWindow("output",
                "Resultado", null, createOutputPanel(), ToolWindowAnchor.BOTTOM);
        outputToolWindow.setAvailable(true);

        // Tool Window para comparación
        ToolWindow comparisonToolWindow = toolWindowManager.registerToolWindow("comparison",
                "Comparación", null, createComparisonPanel(), ToolWindowAnchor.RIGHT);
        comparisonToolWindow.setAvailable(false); // Inicialmente oculto

        // Cambiar esta línea para obtener el componente correcto
        getContentPane().add((Component) toolWindowManager, BorderLayout.CENTER);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de entrada
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Mensaje de entrada"));
        inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(parseButton);
        buttonPanel.add(unparseCleanButton);
        buttonPanel.add(unparseEBCDICButton);

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        JPanel compareButtonPanel = new JPanel(new FlowLayout());
        compareButtonPanel.add(compareButton);
        panel.add(compareButtonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createComparisonPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(comparisonTextArea), BorderLayout.CENTER);
        return panel;
    }

    private void setupEventHandlers() {
        parseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseMessage();
            }
        });

        unparseCleanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unparseMessage(true);
            }
        });

        unparseEBCDICButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                unparseMessage(false);
                // Mostrar el tool window de comparación
                ToolWindow comparisonToolWindow = toolWindowManager.getToolWindow("comparison");
                comparisonToolWindow.setAvailable(true);
                compareButton.setVisible(true);
            }
        });

        compareButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                compareMessagesInNewArea();
            }
        });

        // Agregar MouseListener al árbol
        resultTree.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    int selRow = resultTree.getRowForLocation(evt.getX(), evt.getY());
                    if (selRow != -1) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                                resultTree.getLastSelectedPathComponent();
                        if (node != null && node.isLeaf()) {
                            showNodeDetails(node, evt.getX(), evt.getY());
                        }
                    }
                }
            }
        });

    }

    private void parseMessage() {
        try {
            String inputMessage = inputTextArea.getText().trim();
            if (inputMessage.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor ingrese un mensaje para parsear",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Habilitar botones y opciones de menú
            unparseCleanButton.setEnabled(true);
            unparseEBCDICButton.setEnabled(true);
            clearMenuItem.setEnabled(true);
            ebcdicMenuItem.setEnabled(true);

            boolean clear=false;
            if(inputMessage.startsWith("F0")){
                System.out.println("Mensaje en formato EBCDIC");
                clear=false;
            }else {
                System.out.println("Mensaje en formato ASCII");
                clear=true;
            }

            //currentMappedFields = messageParser.parser(inputMessage);
            ParseResult result = messageParser.parseWithBothMaps(inputMessage,clear);
            currentMappedFieldsByDescription = result.getFieldsByDescription();
            currentMappedFieldsById = result.getFieldsById();
            Map<String, String> subFields = messageParser.mapSubFields(currentMappedFieldsByDescription);
            iso8583 = ISO8583Builder.buildISO8583(inputMessage, currentMappedFieldsByDescription);
            iso20022 = ISO8583To20022Mapper.translateToISO20022(iso8583, subFields);

            // Actualizar el árbol jerárquico
            updateTreeView();

            // Actualizar el área de texto
            StringBuilder output = new StringBuilder();
            output.append("=== CAMPOS PARSEADOS ===\n\n");

            //for (Map.Entry<String, String> entry : currentMappedFields.entrySet()) {
            for (Map.Entry<String, String> entry : currentMappedFieldsByDescription.entrySet()) {
                output.append("Campo ").append(entry.getKey()).append(": ")
                        .append(entry.getValue()).append("\n");
            }

            ObjectMapper objectMapper = new ObjectMapper();
            //outputTextArea.setText(output.toString());
            outputTextArea.setText(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(iso20022) );

            unparseCleanButton.setEnabled(true);
            unparseEBCDICButton.setEnabled(true);

        } catch (Exception ex) {
            showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
            //showErrorDialog("titulo","Error al parsear el mensaje: " + ex.getMessage());
            outputTextArea.setText("Error: " + ex.getMessage());
        }
    }

    private void showErrorDialog(String message) {
        // Limitar mensaje a 150 caracteres y usar HTML para wrap
        String displayMessage = message.length() > 200 ?
                message.substring(0, 200) + "..." : message;

        String htmlMessage = "<html><body style='width: 250px; padding: 10px;'>" +
                displayMessage.replace("\n", "<br>") + "</body></html>";

        JOptionPane.showMessageDialog(this, htmlMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }



    private void updateTreeView() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mensaje Parseado");

        // Crear nodos categorizados
        DefaultMutableTreeNode headerNode = new DefaultMutableTreeNode("TypeMessage");
        DefaultMutableTreeNode bitmapNode1 = new DefaultMutableTreeNode("Bitmap1");
        DefaultMutableTreeNode bitmapNode2 = new DefaultMutableTreeNode("Bitmap2");
        DefaultMutableTreeNode fieldsNode = new DefaultMutableTreeNode("Campos de Datos");

        // Usar TreeMap para ordenar automáticamente por clave numérica
        Map<Integer, DefaultMutableTreeNode> sortedFieldsBitmap1 = new TreeMap<>();
        Map<Integer, DefaultMutableTreeNode> sortedFieldsBitmap2 = new TreeMap<>();

        String typeMessage = currentMappedFieldsByDescription.get("messageType");
        headerNode.add(new DefaultMutableTreeNode("typeMessage : "+ typeMessage));

        //for (Map.Entry<String, String> entry : currentMappedFields.entrySet()) {
        for (Map.Entry<String, String> entry : currentMappedFieldsById.entrySet()) {
            String field = entry.getKey();
            String value = entry.getValue();
            String nodeText = "P"+ISOUtil.padLeft(field,3,'0') + ": " + "["+value+"]";
            DefaultMutableTreeNode fieldNode = new DefaultMutableTreeNode(nodeText);

            // Categorizar campos
            if (field.equals("0")) {
                fieldNode.setUserObject("bitmap_primary : " + ISOUtil.convertBITMAPtoHEX(value));
                bitmapNode1.add(fieldNode);
         /*   }if (field.equals("0")) {
                fieldNode.setUserObject("bitmap_prymary : " + ISOUtil.convertBITMAPtoHEX(value));
                bitmapNode1.add(fieldNode);*/
            }else {
                // Para campos numéricos, intentar convertir a entero
                try {
                    int fieldNumber = Integer.parseInt(field);
                    if (fieldNumber < 65) {
                        // Campos del bitmap primario (1-64)
                        String bitmap1=(String)fieldNode.getUserObject();
                        sortedFieldsBitmap1.put(fieldNumber, fieldNode);
                    } else {
                        // Campos del bitmap secundario (65+)
                        //fieldsNode.add(fieldNode);
                        sortedFieldsBitmap2.put(fieldNumber, fieldNode);
                    }
                } catch (NumberFormatException e) {
                    // Si no es numérico, agregar directamente a campos de datos
                    fieldsNode.add(fieldNode);
                }
            }
        }

        // Agregar los campos ordenados numéricamente
        for (DefaultMutableTreeNode node : sortedFieldsBitmap1.values()) {

            bitmapNode1.add(node);
        }
        for (DefaultMutableTreeNode node : sortedFieldsBitmap2.values()) {
            //fieldsNode.add(node);
            bitmapNode2.add(node);
        }

        if (headerNode.getChildCount() > 0) root.add(headerNode);
        if (bitmapNode1.getChildCount() > 0) root.add(bitmapNode1);
        if (bitmapNode2.getChildCount() > 0) root.add(bitmapNode2);

        treeModel.setRoot(root);

        // Expandir todos los nodos
        for (int i = 0; i < resultTree.getRowCount(); i++) {
            resultTree.expandRow(i);
        }
    }

    private void unparseMessage(boolean clean) {
        try {

            if (currentMappedFieldsByDescription == null || currentMappedFieldsByDescription.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Primero debe parsear un mensaje",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //String result = messageParser.unParser(currentMappedFields, clean);
            String result = messageParser.unParser(currentMappedFieldsByDescription, clean);
            String type = clean ? "CLEAN" : "EBCDIC";

            StringBuilder output = new StringBuilder();
            output.append("=== MENSAJE UNPARSED (").append(type).append(") ===\n\n");
            output.append(result);

            outputTextArea.setText(output.toString());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al unparsear el mensaje: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            outputTextArea.setText("Error: " + ex.getMessage());
        }
    }

    private void compareMessagesInNewArea() {
        String originalMessage = inputTextArea.getText().trim();
        String unparsedMessage = extractUnparsedMessage();

        if (originalMessage.isEmpty() || unparsedMessage.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay mensajes para comparar",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        StringBuilder comparison = new StringBuilder();
        comparison.append("=== COMPARACIÓN DE TRAMAS ===\n\n");

        int minLength = Math.min(originalMessage.length(), unparsedMessage.length());
        comparison.append("RESULTADO:\n");

        boolean hasDifferences = false;
        for (int i = 0; i < minLength; i++) {
            if (originalMessage.charAt(i) != unparsedMessage.charAt(i)) {
                comparison.append("Posición ").append(i).append(": '")
                        .append(originalMessage.charAt(i)).append("' vs '")
                        .append(unparsedMessage.charAt(i)).append("'\n");
                hasDifferences = true;
            }
        }

        if (originalMessage.length() != unparsedMessage.length()) {
            comparison.append("Longitud diferente: ").append(originalMessage.length())
                    .append(" vs ").append(unparsedMessage.length()).append("\n");
            hasDifferences = true;
        }

        if (!hasDifferences) {
            comparison.append("¡Las tramas son idénticas!\n");
        }

        comparisonTextArea.setText(comparison.toString());
    }

    private String extractUnparsedMessage() {
        String output = outputTextArea.getText();
        String[] lines = output.split("\n");

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].contains("=== MENSAJE UNPARSED")) {
                if (i + 2 < lines.length) {
                    return lines[i + 2].trim();
                }
            }
        }
        return "";
    }


    /**
     * Muestra los detalles de un nodo en un popup
     */
    private void showNodeDetails(DefaultMutableTreeNode node, int x, int y) {
        String nodeText = node.getUserObject().toString();

        // Extraer información del nodo
        String title = "Detalle del campo";
        String message = nodeText;

        // Si es un nodo de campo de datos, intentar extraer más información
        if (nodeText.startsWith("P")) {
            try {
                // Extraer el ID del campo (P001, P002, etc.)
                String fieldId = nodeText.substring(1, 4).trim();
                while (fieldId.startsWith("0") && fieldId.length() > 1) {
                    fieldId = fieldId.substring(1);
                }

                // Buscar información adicional del campo
                String value = currentMappedFieldsById.get(fieldId);
                String description = getFieldDescription(fieldId);
                DataTypeISO8583 dataType = getDataTypeISO8583(fieldId);

                // Crear panel personalizado para el popup
                JPanel panel = new JPanel(new BorderLayout(10, 10));
                panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

                // Título
                JLabel titleLabel = new JLabel("Campo " + fieldId);
                titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 14));
                panel.add(titleLabel, BorderLayout.NORTH);

                // Contenido
                JPanel contentPanel = new JPanel(new GridLayout(0, 1, 5, 5));
                contentPanel.add(new JLabel("ID: " + dataType.getId()));

                if (description != null && !description.isEmpty()) {
                    contentPanel.add(new JLabel("Name: " + dataType.getName()));
                }

                //contentPanel.add(new JLabel("Valor: " + value));
                contentPanel.add(new JLabel("Tipo dato: " + dataType.getTypeData()));
                contentPanel.add(new JLabel("Caracteristicas: " + dataType.getCaracteristicaDato()));
                contentPanel.add(new JLabel("Longitud: " + dataType.getLength()));

                // Si es un valor hexadecimal, mostrar también en ASCII
               /* if (value != null && ISOUtil.isHexadecimal(value)) {
                    try {
                        String asciiValue = ISOUtil.hexToAscii(value);
                        contentPanel.add(new JLabel("Valor ASCII: " + asciiValue));
                    } catch (Exception e) {
                        // No hacer nada si no se puede convertir
                    }
                }*/

                panel.add(contentPanel, BorderLayout.CENTER);

                // Mostrar el panel en un JOptionPane
                JOptionPane.showMessageDialog(this, panel, title, JOptionPane.INFORMATION_MESSAGE);
                return;
            } catch (Exception e) {
                // Si hay algún error, mostrar el mensaje simple
            }
        }
        // Si no es un campo especial o hubo error, mostrar mensaje simple
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private DataTypeISO8583 getDataTypeISO8583(String fieldId) {
        try {
            int id = Integer.parseInt(fieldId);
            // Buscar en el enum ISOFieldMastercard por el ID numérico
            for (ISOFieldMastercard field : ISOFieldMastercard.values()) {
                if (field.getId() == id) {
                    //return field.getDescription();
                    if(!field.isVariable()){
                        return new DataTypeISO8583(field.getId(),field.getName(),
                                field.getTypeData().name(), "FIXED", field.getLength());
                    }

                    return new DataTypeISO8583(field.getId(),field.getName(),
                            field.getTypeData().name(), "VARIABLE", field.getLength());
                }
            }
        } catch (NumberFormatException e) {
            // Si no es un número válido, no hacer nada
        }

        return new DataTypeISO8583();
    }

    /**
     * Obtiene la descripción de un campo basado en su ID
     */
    private String getFieldDescription(String fieldId) {
        // Mapeo de IDs a descripciones - esto puedes ampliarlo según tus necesidades
        Map<String, String> fieldDescriptions = new HashMap<>();
        fieldDescriptions.put("0", "Bitmap Primario");
        fieldDescriptions.put("1", "Bitmap Secundario");
        fieldDescriptions.put("2", "Número de Tarjeta (PAN)");
        fieldDescriptions.put("3", "Código de Procesamiento");
        fieldDescriptions.put("4", "Monto de la Transacción");
        fieldDescriptions.put("7", "Fecha y Hora de Transmisión");
        fieldDescriptions.put("11", "Número de Trace");
        fieldDescriptions.put("12", "Hora Local de la Transacción");
        fieldDescriptions.put("13", "Fecha Local de la Transacción");
        fieldDescriptions.put("32", "Código de Institución Adquirente");
        fieldDescriptions.put("37", "Número de Referencia");
        fieldDescriptions.put("38", "Código de Autorización");
        fieldDescriptions.put("39", "Código de Respuesta");
        fieldDescriptions.put("41", "Terminal ID");
        fieldDescriptions.put("42", "Merchant ID");
        fieldDescriptions.put("43", "Nombre/Ubicación del Comercio");
        fieldDescriptions.put("48", "Datos Adicionales");
        fieldDescriptions.put("49", "Moneda de la Transacción");
        fieldDescriptions.put("54", "Montos Adicionales");
        fieldDescriptions.put("55", "Datos ICC");

        return fieldDescriptions.getOrDefault(fieldId, "");
    }

    public static void main(String[] args) {
        final String SAMPLE_MESSAGE = "F0F1F0F0FEFF640188E1E10A0000000000000040F1F6F5F5F3F6F5F0F9F9F9F9F9F9F9F9F9F9F0F0F0F0F0F0F0F0F0F0F0F0F0F2F2F9F9F0F0F0F0F0F0F0F0F0F6F3F5F6F0F0F0F0F0F0F0F2F2F9F9F0F0F6F1F6F0F7F2F7F2F4F7F2F7F6F4F6F8F0F6F1F0F0F0F0F0F0F8F9F8F7F1F6F0F3F2F7F2F4F0F6F1F6F2F9F0F5F0F6F1F6F0F6F1F5F5F8F1F8F8F4F0F1F0F0F0F6F0F0F3F2F8F6F0F6F0F0F3F2F8F6F5F1F6F7F5F4F8F9F8F7F1F6F0F0F4F0F0F2F1F6F4F0F0F2F1F6F0F0F0F1F0F8F7F7F8C1D7D7D3C54BC3D6D461C2C9D3D3404040404040404040F8F6F660F7F1F260F7F7F5F34040E4E2C1F1F1F8E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C340F6F0F4F8F4F0F6F0F4F0F3F7F0F1F3F3F0F1F2F9F5F0F0F1F9F3C8D2D8C9E6C5E8E5C9E2C5F7E4D2E3D8D2F8E8D1F5C3F0F0F2F6F0F0F0F4F1F0F0F0F0F0F6F0F0F8F4F0F9F5F0F1F44040404040F0F0F9D4C2D2C3C7F4F6F2C6F1F0F1F0F0F1F0F9F5F0F0F1F0F1F8D6D5C540C1D7D7D3C540D7C1D9D240E6C1E8F0F0F2F0F0F3C3C140F0F0F3F0F1F3C1D7D7D3C54BC3D6D440C2C9D3F0F0F4F0F1F0F8F6F6F7F1F2F7F7F5F3F0F0F7F0F2F1F8F4F2F8F0F5F8F2F24040404040404040404040E8";

        // Establece el Look and Feel ANTES de crear cualquier componente Swing
        //FlatLightLaf.setup(); // O el tema que prefieras

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {

                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                        UIManager.setLookAndFeel(UIManager.getLookAndFeel());
                    }

                } catch (Exception e) {
                    // Usar el look and feel por defecto
                }

                MastercardParserGUI2 gui = new MastercardParserGUI2();
                gui.inputTextArea.setText(SAMPLE_MESSAGE);
                gui.setVisible(true);
            }
        });
    }
}