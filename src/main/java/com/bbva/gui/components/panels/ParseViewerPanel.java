package com.bbva.gui.components.panels;

import com.bbva.gui.components.PanelDoggy;
import com.bbva.gui.spring.ApplicationContextProvider;
import com.bbva.orchestrator.core.builders.ISO8583Builder;
import com.bbva.orchestrator.core.dto.ISO8583;
import com.bbva.orchestrator.core.logic.factory.FieldLogicFactory;
import com.bbva.orchestrator.core.logic.factory.NetworkDelegateFieldLogic;
import com.bbva.orchestrator.core.mapper.factory.ISO20022DelegateMapper;
import com.bbva.orchestrator.core.mapper.factory.MapperFactory;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bbva.gui.utils.ParseGUI;
import com.bbva.gui.dto.ParseResult;
import com.bbva.gui.utils.UtilGUI;
import com.bbva.gateway.dto.iso20022.ISO20022;
import lombok.Setter;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;


public class ParseViewerPanel extends JPanel {

    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JTree resultTree;
    private DefaultTreeModel treeModel;
    private JButton parseButton;
    private JButton limpiarButton;
    private JButton copiarRespuestaButton;
    @Setter
    private JInternalFrame parentFrame;
    private final ISO8583DelegateParser delegateParser;
    private final ISO20022DelegateMapper delegateMapper;
    private final NetworkDelegateFieldLogic delegateFieldLogic;

    final String SAMPLE_MESSAGE = "F0F1F0F0FEFF640188E1E10A0000000000000040F1F6F5F5F3F6F5F0F9F9F9F9F9F9F9F9F9F9F0F0F0F0F0F0F0F0F0F0F0F0F0F2F2F9F9F0F0F0F0F0F0F0F0F0F6F3F5F6F0F0F0F0F0F0F0F2F2F9F9F0F0F6F1F6F0F7F2F7F2F4F7F2F7F6F4F6F8F0F6F1F0F0F0F0F0F0F8F9F8F7F1F6F0F3F2F7F2F4F0F6F1F6F2F9F0F5F0F6F1F6F0F6F1F5F5F8F1F8F8F4F0F1F0F0F0F6F0F0F3F2F8F6F0F6F0F0F3F2F8F6F5F1F6F7F5F4F8F9F8F7F1F6F0F0F4F0F0F2F1F6F4F0F0F2F1F6F0F0F0F1F0F8F7F7F8C1D7D7D3C54BC3D6D461C2C9D3D3404040404040404040F8F6F660F7F1F260F7F7F5F34040E4E2C1F1F1F8E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C340F6F0F4F8F4F0F6F0F4F0F3F7F0F1F3F3F0F1F2F9F5F0F0F1F9F3C8D2D8C9E6C5E8E5C9E2C5F7E4D2E3D8D2F8E8D1F5C3F0F0F2F6F0F0F0F4F1F0F0F0F0F0F6F0F0F8F4F0F9F5F0F1F44040404040F0F0F9D4C2D2C3C7F4F6F2C6F1F0F1F0F0F1F0F9F5F0F0F1F0F1F8D6D5C540C1D7D7D3C540D7C1D9D240E6C1E8F0F0F2F0F0F3C3C140F0F0F3F0F1F3C1D7D7D3C54BC3D6D440C2C9D3F0F0F4F0F1F0F8F6F6F7F1F2F7F7F5F3F0F0F7F0F2F1F8F4F2F8F0F5F8F2F24040404040404040404040E8";

    public ParseViewerPanel() {
        initializeComponents();
        createPanelsLayout();
        setupEventHandlers();
        ParserFactory parserFactory = ApplicationContextProvider.getBean(ParserFactory.class);
        MapperFactory mapperFactory = ApplicationContextProvider.getBean(MapperFactory.class);
        FieldLogicFactory fieldLogicFactory = ApplicationContextProvider.getBean(FieldLogicFactory.class);
        delegateParser = parserFactory.getDelegateParser("PEER02");
        delegateMapper = mapperFactory.getDelegateMapper("PEER02");
        delegateFieldLogic = fieldLogicFactory.getDelegateFieldLogic("PEER02");
        System.out.println("DelegateParser bean: " + delegateParser);
        System.out.println("DelegateMapper bean: " + delegateMapper);
        System.out.println("DelegateFieldLogic bean: " + delegateFieldLogic);

        //Poner el foco en el JTextArea de entrada
        SwingUtilities.invokeLater(() -> {
            inputTextArea.requestFocusInWindow();
        });
    }


    private void initializeComponents() {

        setLayout(new BorderLayout());
        inputTextArea = new JTextArea(8, 60);
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);
        inputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        inputTextArea.setText(SAMPLE_MESSAGE);

        outputTextArea = new JTextArea(12, 60);
        outputTextArea.setLineWrap(true);
        outputTextArea.setWrapStyleWord(true);
        outputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        outputTextArea.setEditable(false);

        parseButton = new JButton("Parser");
        limpiarButton = new JButton("Limpiar");
        copiarRespuestaButton = new JButton("Copiar respuesta");

        // Inicializar el árbol jerárquico
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mensaje Parseado");
        treeModel = new DefaultTreeModel(root);
        resultTree = new JTree(treeModel);
        //resultTree.setCellRenderer(new NoIconTreeCellRenderer());
        resultTree.setRootVisible(true);
    }

    private void createPanelsLayout() {
        MyDoggyToolWindowManager toolWindowManager = PanelDoggy.setupStructureMyDoggy(createMainPanel(), resultTree, createOutputPanel());
        add(toolWindowManager, BorderLayout.CENTER);
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de entrada
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Mensaje de entrada"));
        //inputPanel.setSize(new Dimension(500,300));
        inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(parseButton);
        buttonPanel.add(limpiarButton);

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        JPanel compareButtonPanel = new JPanel(new FlowLayout());
        panel.add(compareButtonPanel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(copiarRespuestaButton, BorderLayout.EAST);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void setupEventHandlers() {


        parseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseMessage();
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputTextArea.setText("");
                outputTextArea.setText("");
                // Limpiar el JTree
                DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mensaje Parseado");
                treeModel.setRoot(root);
                treeModel.reload();
            }
        });

        copiarRespuestaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection selection = new StringSelection(outputTextArea.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                outputTextArea.requestFocusInWindow();
                outputTextArea.selectAll();

                SwingUtils.mostrarTooltipTemporal(copiarRespuestaButton, "¡Texto copiado!", 1000);
                // Mostrar ventana emergente
                //SwingUtils.crearMenuContextual(outputTextArea);
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
                JOptionPane.showMessageDialog(parentFrame, "Por favor ingrese un mensaje para parsear",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Map<String, String> mapValues = delegateParser.parser(inputMessage);
            ParseResult result = ParseGUI.process(mapValues);

            ISO8583 iso8583 = ISO8583Builder.buildISO8583(inputMessage, mapValues);
            Map<String, String> subFields = delegateFieldLogic.parseSubfields(iso8583);
            ISO20022 iso20022 = delegateMapper.mapper(iso8583, subFields);

           /* Map<String, String> fieldsValues = delegateMapper.unMapper(iso20022);
            String tramaGen= delegateParser.unParserPlainText(fieldsValues);
            System.out.println("Trama generada: [" + tramaGen+"]");*/

            ParseGUI.updateTreeView(treeModel, resultTree, result);
            ObjectMapper objectMapper = new ObjectMapper();
            outputTextArea.setText(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(iso20022));

        } catch (Exception ex) {
            UtilGUI.showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
            outputTextArea.setText("Error: " + ex.getMessage());
        }
    }

    public void setInputText(String text) {
        inputTextArea.setText(text);
    }

}