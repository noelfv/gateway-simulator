package com.bbva.gui.components.viewers;

import com.bbva.orchestrator.parser.common.ISO8583SubFieldsParser;
import com.bbva.orchestrator.parser.iso20022.ISO20022To8583Mapper;
import com.bbva.orchestrator.parser.iso20022.ISO8583To20022Mapper;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.iso8583.ISO8583Builder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bbva.gui.commons.ISO8583Processor;
import com.bbva.gui.utils.ParseGUI;
import com.bbva.gui.dto.ParseResult;
import com.bbva.gui.utils.UtilGUI;
import com.bbva.gateway.dto.iso20022.ISO20022;
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

public class ParseViewerPanel extends JPanel {

    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JTree resultTree;
    private DefaultTreeModel treeModel;
    private JButton parseButton;
    //  private MCMessageParserImpl messageParser;

    public ParseViewerPanel() {
       //messageParser = new MCMessageParserImpl();
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
        MyDoggyToolWindowManager toolWindowManager = new MyDoggyToolWindowManager();
        // Panel principal con entrada y botones
        JPanel mainPanel = createMainPanel();
        // Crear un panel contenedor para combinar radio buttons y panel principal
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(mainPanel, BorderLayout.CENTER);
        // Agregar el panel contenedor como contenido central
        toolWindowManager.getContentManager().addContent("main", "Parser Principal", null, contentPanel);

        // Tool Window para el árbol jerárquico
        ToolWindow treeToolWindow = toolWindowManager.registerToolWindow("Parser",
                "Estructura Jerárquica", null, new JScrollPane(resultTree), ToolWindowAnchor.LEFT);
        treeToolWindow.setAvailable(true);
        treeToolWindow.setVisible(true);

        // Tool Window para ISO20022
        ToolWindow outputToolWindow = toolWindowManager.registerToolWindow("ISO 20022",
                "Resultado", null, createOutputPanel(), ToolWindowAnchor.BOTTOM);
        outputToolWindow.setAvailable(true);
        outputToolWindow.setVisible(true);

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
                           ParseGUI.showNodeDetails(node, evt.getX(), evt.getY());
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

            Map<String,String> mapValues;
            if(inputMessage.startsWith("F0")){
                System.out.println("Mensaje en formato EBCDIC");
                mapValues=ISO8583Processor.mapFields(inputMessage);
            }else {
                System.out.println("Mensaje en formato ASCII");
                mapValues=ISO8583Processor.mapFieldsTramaClaro(inputMessage);
            }

            ParseResult result = ParseGUI.process(mapValues);

            Map<String, String> currentMappedFieldsByDescription = result.fieldsByDescription();
            //Map<String, String> currentMappedFieldsById = result.fieldsById();
           // Map<String, String> subFields = messageParser.mapSubFields(currentMappedFieldsByDescription);
            Map<String, String> subFields =  ISO8583SubFieldsParser.mapSubFieldsMastercard(currentMappedFieldsByDescription);
            ISO8583 iso8583 = ISO8583Builder.buildISO8583(inputMessage, currentMappedFieldsByDescription);
            ISO20022 iso20022 = ISO8583To20022Mapper.translateToISO20022(iso8583, subFields,true);
            String tramaGen= ISO20022To8583Mapper.getOriginalIsoMessage(iso20022);
            System.out.println("Trama generada: [" + tramaGen+"]");

            //updateTreeView();
            ParseGUI.updateTreeView(treeModel, resultTree, result);


            ObjectMapper objectMapper = new ObjectMapper();
            outputTextArea.setText(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(iso20022) );

        } catch (Exception ex) {
            UtilGUI.showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
            outputTextArea.setText("Error: " + ex.getMessage());
        }
    }


    public void setInputText(String text) {
        inputTextArea.setText(text);
    }

}