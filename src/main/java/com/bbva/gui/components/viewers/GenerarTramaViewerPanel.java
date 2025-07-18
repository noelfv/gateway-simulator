package com.bbva.gui.components.viewers;


import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import com.bbva.gui.components.NoIconTreeCellRenderer;
import com.bbva.gui.components.TextFieldTreeCellEditor;
import com.bbva.gui.components.TextFieldTreeCellRenderer;
import com.bbva.gui.components.TreeNodeData;
import com.bbva.gui.utils.UtilGUI;
import com.bbva.orchestrator.MCMessageParserImpl;
import com.bbva.gui.dto.ISOFieldInfo;
import com.bbva.orchestrator.network.mastercard.ISOFieldMastercard;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import org.noos.xing.mydoggy.ToolWindow;
import org.noos.xing.mydoggy.ToolWindowAnchor;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.*;

public class GenerarTramaViewerPanel extends JPanel {

    private JTextArea outputTextArea;
    private JTree resultTree;
    private JButton processarButton;
    private DefaultTreeModel treeModel;
    private MyDoggyToolWindowManager toolWindowManager;
    private ISO8583 iso8583;
    private MCMessageParserImpl messageParser;
    private Map<String, String> currentMappedFieldsByDescription;
    private DefaultMutableTreeNode currentEditingNode;
    private TreeNodeData currentEditingData;
    private boolean editorListenerAdded = false;

    public GenerarTramaViewerPanel() {
        messageParser = new MCMessageParserImpl();
        initializeComponents();
        setupEventHandlers();
        setupMyDoggy();
    }


    private void initializeComponents() {

        setLayout(new BorderLayout());
        outputTextArea = new JTextArea(12, 60);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        outputTextArea.setEditable(false);
        //outputTextArea.setEnabled(false);
        // Inicializar el árbol jerárquico
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mensaje Parseado");
        treeModel = new DefaultTreeModel(root);
        resultTree = new JTree(treeModel);
        resultTree.setCellRenderer(new NoIconTreeCellRenderer());
        resultTree.setRootVisible(true);
        processarButton = new JButton("Procesar");

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
        treeToolWindow.setActive(true);

        updateTreeView();
        add(toolWindowManager, BorderLayout.CENTER);

    }


    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de entrada
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Resultado"));
        inputPanel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(processarButton);

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.NORTH);

        return panel;
    }


    private void setupEventHandlers() {
        processarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateOutputTextArea();
            }
        });
    }

    private void updateTreeView() {
        // Solo crear el árbol una vez, no recrear si ya existe
        if (treeModel.getRoot() == null ||
                !treeModel.getRoot().toString().equals("Campos ISO8583")) {

            DefaultMutableTreeNode root = new DefaultMutableTreeNode("Campos ISO8583");

            //campo 6
            // String cardHolderBillingAmount = P010/P004

            //campo 7
            String operationDateTime = UtilGUI.generateOperationDateTime();
            //campo 11
            String systemCurrentTrace = UtilGUI.generateRandomSixDigitNumber();
            //campo 15
            String settlementDate = UtilGUI.generateOperationDateTime().substring(1,4);
            //campo 18
            String merchantCategoryCode = "6011";
            //campo 22
            String cardDataEntryMode = "051";

            //campo 49
            String currency = "valor ingresado";


            root.add(new DefaultMutableTreeNode(new TreeNodeData("P002", "5536509999999999")));
            root.add(new DefaultMutableTreeNode(new TreeNodeData("P003", "000000")));
            root.add(new DefaultMutableTreeNode(new TreeNodeData("P004", "")));
            root.add(new DefaultMutableTreeNode(new TreeNodeData("P005", "")));
            root.add(new DefaultMutableTreeNode(new TreeNodeData("P006", "")));
            treeModel.setRoot(root);
        }

        // Configurar renderer y editor
        resultTree.setCellRenderer(new TextFieldTreeCellRenderer());
        resultTree.setCellEditor(new TextFieldTreeCellEditor());
        resultTree.setEditable(true);
        resultTree.setInvokesStopCellEditing(true);


        // Agregar listener solo una vez
        if (!editorListenerAdded) {
            resultTree.getCellEditor().addCellEditorListener(new CellEditorListener() {
                @Override
                public void editingStopped(ChangeEvent e) {
                    treeModel.nodeChanged((TreeNode) resultTree.getSelectionPath().getLastPathComponent());
                }

                @Override
                public void editingCanceled(ChangeEvent e) {
                    System.out.println("Edición cancelada");
                }
            });
            editorListenerAdded = true;
        }

        // Expandir todos los nodos
        for (int i = 0; i < resultTree.getRowCount(); i++) {
            resultTree.expandRow(i);
        }
    }


    private void collectTreeData(DefaultMutableTreeNode node, StringBuilder output, String indent) {
        if (node == null) return;

        Object userObject = node.getUserObject();
        boolean nodoTieneValor = false;

        if (userObject instanceof TreeNodeData) {
            TreeNodeData data = (TreeNodeData) userObject;
            String label = data.getLabel();
            String value = data.getValue();




            int fieldId = UtilGUI.extractFieldIdFromLabel(label);
            Map<String, String> mapData = new HashMap<>();
            if (value != null && !value.isBlank()) {

                ISOFieldMastercard.findById(fieldId).ifPresent(field -> {
                    ISOFieldInfo fieldInfo = new ISOFieldInfo(
                            field.getName(),
                            data.getValue()
                    );
                    mapData.put(field.getName(),data.getValue());
                    System.out.println("tamaño " + mapData.size());
                    System.out.println("data " + mapData);
                });

                output.append(indent)
                        .append(data.getLabel())
                        .append(" = ")
                        .append(value)
                        .append("\n");
                nodoTieneValor = true;
            }

        }

        // Recorremos hijos recursivamente
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
            collectTreeData(child, output, indent + "  "); // indentación visual
        }
    }


    private void collectTreeData(DefaultMutableTreeNode node, StringBuilder output, String indent, Map<String, String> mapData) {
        if (node == null) return;

        Object userObject = node.getUserObject();

        if (userObject instanceof TreeNodeData data) {
            String label = data.getLabel();
            String value = data.getValue();

            if (value != null && !value.isBlank()) {
                int fieldId = UtilGUI.extractFieldIdFromLabel(label);

                ISOFieldMastercard.findById(fieldId).ifPresent(field -> {
                    mapData.put(field.getName(), value);
                    System.out.println("Map actualizado: " + mapData);
                });

                output.append(indent)
                        .append(label)
                        .append(" = ")
                        .append(value)
                        .append("\n");
            }
        }

        // Recorremos hijos recursivamente (pasando el mismo mapData)
        for (int i = 0; i < node.getChildCount(); i++) {
            DefaultMutableTreeNode child = (DefaultMutableTreeNode) node.getChildAt(i);
            collectTreeData(child, output, indent + "  ", mapData);
        }
    }



    private void updateOutputTextArea() {
        StringBuilder output = new StringBuilder();
        Map<String, String> isoDataMap = new HashMap<>();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) resultTree.getModel().getRoot();
        collectTreeData(root, output, "", isoDataMap);
        isoDataMap.put("messageType", "0100");
        String trama=messageParser.unParser(isoDataMap, true);
        outputTextArea.setText(trama);
        ISO8583 iso = mapToISO8583(isoDataMap);
        System.out.println(iso.getProcessingCode());
        System.out.println(iso.getPrimaryAccountNumber());
    }

    private void processMap0100(){
        Map<String,String> inputMessage = new HashMap<>();
        inputMessage.put("messageType", "0100");
        //campo 2
        inputMessage.put("primaryAccountNumber", "5536509999999999");
        //campo 3
        inputMessage.put("processingCode", "000000");
        //campo 4
        inputMessage.put("transactionAmount", "0000000229.90");
        //campo 5
        inputMessage.put("settlementAmount", "000000006356");
        //campo 6
        inputMessage.put("cardHolderBillingAmount", "0000000229.90");
        //campo 7
        inputMessage.put("transmissionDateTime", "0616072724");
        //campo 9
        inputMessage.put("conversionRateSettlement", "72764680");
        //campo 10
        inputMessage.put("conversionRate", "61000000");
        //campo 11
        inputMessage.put("systemTraceAuditNumber", "898716");
        //campo 12
        inputMessage.put("localTransactionTime", "101604");
        //campo 13
        inputMessage.put("localTransactionDate", "0715");
        //campo 14
        inputMessage.put("dateExpiration", "2905");
        //campo 15
        inputMessage.put("settlementDate", "0616");
        //campo 16
        inputMessage.put("dateConversion", "0615");
        //campo 18
        inputMessage.put("merchantType", "0001");
        //campo 20
        inputMessage.put("primaryAccountNumberCountryCode", "123");
        //campo 22
        inputMessage.put("pointServiceEntryMode", "123");
        //campo 26
        inputMessage.put("pointServicePIN", "12");
        //campo 32
        inputMessage.put("acquiringInstitutionIdentificationCode", "003286");
        // campo 33
        inputMessage.put("forwardingInstitutionIdentificationCode", "003286");
        // campo 35
        inputMessage.put("trackTwoData", "12345678901234567890=29051000000000000000");
        //campo 37
        inputMessage.put("retrievalReferenceNumber", "516754898716");
        //campo 41
        inputMessage.put("cardAcceptorTerminalIdentification", "00400216");
        //campo 42
        inputMessage.put("cardAcceptorIdentificationCode", "400216000108778");
        //campo 43
        inputMessage.put("cardAcceptorNameLocation", "APPLE.COM/BILL         866-712-7753  USA");
        //campo 44
        //inputMessage.put("additionalResponseData", "22");
        //campo 45
        inputMessage.put("trackOneData", "000000000000000000000000000000000000000");
        //campo 48
        inputMessage.put("additionalDataRetailer", "T37150511000009999974207010321022080504M1036105000015618AQV116AQS609AQF116753201038800202140303880040214050200710418C ");
        //campo 49
        inputMessage.put("transactionCurrencyCode", "604");
        //campo 50
        inputMessage.put("settlementCurrencyCode", "840");
        //campo 51
        inputMessage.put("cardholderBillingCurrencyCode", "604");
        //campo 52
        inputMessage.put("pinData", "ABCDEF12");
        //campo 54
        inputMessage.put("additionalAmounts", "123");
        //campo 55
        inputMessage.put("integratedCircuitCard", "123");
        //campo 61
        inputMessage.put("posCardIssuer", "000410000060084095014     ");
        //campo 62
        inputMessage.put("postalCode", "123");
        //campo 63
        inputMessage.put("networkData", "123");
        //campo 73
        inputMessage.put("dateAction", "123456");
        //campo 95
        inputMessage.put("replacementAmounts", "000000000000000000000000000000000000000000");
        //campo 112
        inputMessage.put("additionalDataNationalUse", "123");
        //campo 120
        inputMessage.put("keyManagement", "123");
        // campo 121
        //inputMessage.put("authorizingAgentIdCode", "123");
        //campo 127
        inputMessage.put("privateData", "123");

        String trama=messageParser.unParser(inputMessage, true);
        outputTextArea.setText(trama);
    }


    private void processMap0110(){
        Map<String,String> inputMessage = new HashMap<>();
        inputMessage.put("messageType", "0110");
        //campo 2
        inputMessage.put("primaryAccountNumber", "5536509999999999");
        //campo 3
        inputMessage.put("processingCode", "000000");
        //campo 4
        inputMessage.put("transactionAmount", "0000000229.90");
        //campo 5
        inputMessage.put("settlementAmount", "000000006356");
        //campo 6
        inputMessage.put("cardHolderBillingAmount", "0000000229.90");
        //campo 7
        inputMessage.put("transmissionDateTime", "0616072724");
        //campo 9
        inputMessage.put("conversionRateSettlement", "72764680");
        //campo 10
        inputMessage.put("conversionRate", "61000000");
        //campo 11
        inputMessage.put("systemTraceAuditNumber", "898716");
        //campo 12
        //inputMessage.put("localTransactionTime", "061604");
        //campo 15
        inputMessage.put("settlementDate", "0616");
        //campo 16
        inputMessage.put("dateConversion", "0615");
        //campo 20
        inputMessage.put("primaryAccountNumberCountryCode", "123");
        //campo 32
        inputMessage.put("acquiringInstitutionIdentificationCode", "003286");
        // campo 33
        inputMessage.put("forwardingInstitutionIdentificationCode", "003286");
        //campo 37
        inputMessage.put("retrievalReferenceNumber", "516754898716");
        //campo 38
        inputMessage.put("authorizationIdentificationResponse", "012345");
        //campo 39
        inputMessage.put("responseCode", "00");
        //campo 41
        inputMessage.put("cardAcceptorTerminalIdentification", "00400216");
        //campo 44
        inputMessage.put("additionalResponseData", "22");
        //campo 48
        inputMessage.put("additionalDataRetailer", "T37150511000009999974207010321022080504M1036105000015618AQV116AQS609AQF116753201038800202140303880040214050200710418C ");
        //campo 49
        inputMessage.put("transactionCurrencyCode", "604");
        //campo 50
        inputMessage.put("settlementCurrencyCode", "840");
        //campo 51
        inputMessage.put("cardholderBillingCurrencyCode", "604");
        //campo 54
        inputMessage.put("additionalAmounts", "123");
        //campo 55
        inputMessage.put("integratedCircuitCard", "123");
        //campo 62
        inputMessage.put("postalCode", "123");
        //campo 63
        inputMessage.put("networkData", "123");
        //campo 112
        inputMessage.put("additionalDataNationalUse", "123");
        //campo 120
        inputMessage.put("keyManagement", "123");
        //campo 121
        inputMessage.put("authorizingAgentIdCode", "123");
        //campo 127
        inputMessage.put("privateData", "123");

        String trama=messageParser.unParser(inputMessage, true);
        outputTextArea.setText(trama);
    }


    public ISO8583 mapToISO8583(Map<String, String> fieldMap) {
        ISO8583.ISO8583Builder builder = ISO8583.builder();   // usa tu builder real
        Class<?> bClass = builder.getClass();

        fieldMap.forEach((key, rawValue) -> {
            if (rawValue == null || rawValue.isBlank()) return;

            // Elimina espacios accidentales
            String methodName = key.trim();

            // Busca un método con ese nombre y un solo parámetro
            Method setter = Arrays.stream(bClass.getMethods())
                    .filter(m -> m.getName().equals(methodName) && m.getParameterCount() == 1)
                    .findFirst()
                    .orElse(null);

            if (setter == null) {
                System.out.println("Setter no encontrado en builder: " + methodName);
                return;
            }

            Class<?> paramType = setter.getParameterTypes()[0];
            Object converted;

            try {
                // Conversión básica si el campo NO es String
                if (paramType.equals(String.class)) {
                    converted = rawValue;
                } else if (paramType.equals(Integer.class) || paramType.equals(int.class)) {
                    converted = Integer.valueOf(rawValue);
                } else if (paramType.equals(Boolean.class) || paramType.equals(boolean.class)) {
                    converted = Boolean.valueOf(rawValue);
                } else {
                    System.out.println("Tipo no soportado para " + methodName + ": " + paramType.getSimpleName());
                    return;
                }

                setter.invoke(builder, converted);

            } catch (Exception ex) {
                throw new RuntimeException("Error asignando " + methodName + ": '" + rawValue + "'", ex);
            }
        });

        return builder.build();
    }


}