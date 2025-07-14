package org.example.gui.panels;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.orchestrator.MCMessageParserImpl;
import org.example.orchestrator.common.ISOFieldInfo;
import org.example.orchestrator.common.ISOUtil;
import org.example.orchestrator.common.ParseResult;
import org.example.orchestrator.dto.ISO20022;
import org.example.orchestrator.iso20022.ISO20022To8583Mapper;
import org.example.orchestrator.iso20022.ISO8583To20022Mapper;
import org.example.orchestrator.iso8583.ISO8583;
import org.example.orchestrator.iso8583.ISO8583Builder;
import org.example.orchestrator.mastercard.ISOFieldMastercard;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.TreeMap;

public class ParseViewerPanel extends JPanel {

    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JTree resultTree;
    private DefaultTreeModel treeModel;
    private JButton parseButton;
    private MyDoggyToolWindowManager toolWindowManager;
    private MCMessageParserImpl messageParser;
    private Map<String, String> currentMappedFields;
    private Map<String, String> currentMappedFieldsByDescription;
    private Map<String, String> currentMappedFieldsById;
    private ISO8583 iso8583;
    private ISO20022 iso20022;

   public ParseViewerPanel() {
       messageParser = new MCMessageParserImpl();
       initializeComponents();
       setupEventHandlers();
       setupMyDoggy();
    }

    private void initializeComponents() {

        setLayout(new BorderLayout());
        //add(new JLabel("Panel de Parseo de Trama"), BorderLayout.CENTER);

        inputTextArea = new JTextArea(8, 60);
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        outputTextArea = new JTextArea(12, 60);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        outputTextArea.setEditable(false);

        // Inicializar el árbol jerárquico
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mensaje Parseado");
        treeModel = new DefaultTreeModel(root);
        resultTree = new JTree(treeModel);
        //resultTree.setCellRenderer(new NoIconTreeCellRenderer());
        resultTree.setRootVisible(true);
        parseButton = new JButton("Parser");

    }

    private void setupMyDoggy() {
        toolWindowManager = new MyDoggyToolWindowManager();
        // Panel principal con entrada y botones
        JPanel mainPanel = createMainPanel();
        // Crear un panel contenedor para combinar radio buttons y panel principal
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        // Agregar el panel contenedor como contenido central
        toolWindowManager.getContentManager().addContent("main", "Parser Principal", null, contentPanel);
        // Agregar el panel principal como contenido central
        //toolWindowManager.getContentManager().addContent("main", "Parser Principal", null, mainPanel);

        // Tool Window para el árbol jerárquico
        ToolWindow treeToolWindow = toolWindowManager.registerToolWindow("Parser",
                "Estructura Jerárquica", null, new JScrollPane(resultTree), ToolWindowAnchor.LEFT);
        treeToolWindow.setAvailable(true);

        // Tool Window para resultados
        ToolWindow outputToolWindow = toolWindowManager.registerToolWindow("output",
                "Resultado", null, createOutputPanel(), ToolWindowAnchor.BOTTOM);
        outputToolWindow.setAvailable(true);
        // Cambiar esta línea para obtener el componente correcto

        add(toolWindowManager, BorderLayout.CENTER);
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

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        JPanel compareButtonPanel = new JPanel(new FlowLayout());
        panel.add(compareButtonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void setupEventHandlers() {
        parseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseMessage();
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

            boolean clear=false;
            if(inputMessage.startsWith("F0")){
                System.out.println("Mensaje en formato EBCDIC");
                clear=false;
            }else {
                System.out.println("Mensaje en formato ASCII");
                clear=true;
            }

            ParseResult result = messageParser.parseWithBothMaps(inputMessage,clear);
            currentMappedFieldsByDescription = result.getFieldsByDescription();
            currentMappedFieldsById = result.getFieldsById();
            Map<String, String> subFields = messageParser.mapSubFields(currentMappedFieldsByDescription);
            iso8583 = ISO8583Builder.buildISO8583(inputMessage, currentMappedFieldsByDescription);
            iso20022 = ISO8583To20022Mapper.translateToISO20022(iso8583, subFields);

            // ISO8583 iso8583temp= ISO20022To8583Mapper.translateToISO8583(iso20022);
            String tramaGen= ISO20022To8583Mapper.getOriginalIsoMessage(iso20022);
            System.out.println("Trama generada: [" + tramaGen+"]");

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

        } catch (Exception ex) {
            showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
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
            String nodeText = "P"+ ISOUtil.padLeft(field,3,'0') + ": " + "["+value+"]";
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
                ISOFieldInfo dataType = getDataTypeISO8583(fieldId);

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
                contentPanel.add(new JLabel("Tipo dato: " + dataType.getTypeData()));
                contentPanel.add(new JLabel("Caracteristicas: " + dataType.getCaracteristicaDato()));
                contentPanel.add(new JLabel("Longitud: " + dataType.getLength()));

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


    private ISOFieldInfo getDataTypeISO8583(String fieldId) {
        try {
            int id = Integer.parseInt(fieldId);
            // Buscar en el enum ISOFieldMastercard por el ID numérico
            for (ISOFieldMastercard field : ISOFieldMastercard.values()) {
                if (field.getId() == id) {
                    //return field.getDescription();
                    if(!field.isVariable()){
                        return new ISOFieldInfo(field.getId(),field.getName(),
                                field.getTypeData().name(), "FIXED", field.getLength());
                    }

                    return new ISOFieldInfo(field.getId(),field.getName(),
                            field.getTypeData().name(), "VARIABLE", field.getLength());
                }
            }
        } catch (NumberFormatException e) {
            // Si no es un número válido, no hacer nada
        }

        return new ISOFieldInfo();
    }

    public void setInputText(String text) {
        inputTextArea.setText(text);
    }

}