package com.bbva.gui.panels;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.gui.components.PanelDoggy;
import com.bbva.gui.components.InputTextPanel;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.ComponentsUtil;
import com.bbva.gui.utils.SwingUtils;
import com.bbva.orchestrator.core.builders.ISO8583Builder;
import com.bbva.orchestrator.core.dto.ISO8583;
import com.bbva.orchestrator.core.logic.factory.FieldLogicFactory;
import com.bbva.orchestrator.core.logic.factory.NetworkDelegateFieldLogic;
import com.bbva.orchestrator.core.mapper.factory.ISO20022DelegateMapper;
import com.bbva.orchestrator.core.mapper.factory.MapperFactory;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import com.bbva.orchestrator.core.utils.ISOUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.bbva.gui.utils.ParseGUI;
import com.bbva.gui.dto.ParseResult;
import com.bbva.gui.utils.UtilGUI;
import lombok.Setter;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;


public class ParseViewerPanel extends JPanel {

    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JTree resultTree;
    private DefaultTreeModel treeModel;
    private JButton parseButton;
    private JButton limpiarButton;
    private JButton copiarRespuestaButton;
    private JRadioButton optionMastercard;
    private JRadioButton optionVisa;
    @Setter
    private JInternalFrame parentFrame;
    private ISO8583DelegateParser delegateParser;
    private ISO20022DelegateMapper delegateMapper;
    private NetworkDelegateFieldLogic delegateFieldLogic;
    private String inputMessage = "F0F1F0F0FEFF640188E1E10A0000000000000040F1F6F5F5F3F6F5F0F9F9F9F9F9F9F9F9F9F9F0F0F0F0F0F0F0F0F0F0F0F0F0F2F2F9F9F0F0F0F0F0F0F0F0F0F6F3F5F6F0F0F0F0F0F0F0F2F2F9F9F0F0F6F1F6F0F7F2F7F2F4F7F2F7F6F4F6F8F0F6F1F0F0F0F0F0F0F8F9F8F7F1F6F0F3F2F7F2F4F0F6F1F6F2F9F0F5F0F6F1F6F0F6F1F5F5F8F1F8F8F4F0F1F0F0F0F6F0F0F3F2F8F6F0F6F0F0F3F2F8F6F5F1F6F7F5F4F8F9F8F7F1F6F0F0F4F0F0F2F1F6F4F0F0F2F1F6F0F0F0F1F0F8F7F7F8C1D7D7D3C54BC3D6D461C2C9D3D3404040404040404040F8F6F660F7F1F260F7F7F5F34040E4E2C1F1F1F8E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C340F6F0F4F8F4F0F6F0F4F0F3F7F0F1F3F3F0F1F2F9F5F0F0F1F9F3C8D2D8C9E6C5E8E5C9E2C5F7E4D2E3D8D2F8E8D1F5C3F0F0F2F6F0F0F0F4F1F0F0F0F0F0F6F0F0F8F4F0F9F5F0F1F44040404040F0F0F9D4C2D2C3C7F4F6F2C6F1F0F1F0F0F1F0F9F5F0F0F1F0F1F8D6D5C540C1D7D7D3C540D7C1D9D240E6C1E8F0F0F2F0F0F3C3C140F0F0F3F0F1F3C1D7D7D3C54BC3D6D440C2C9D3F0F0F4F0F1F0F8F6F6F7F1F2F7F7F5F3F0F0F7F0F2F1F8F4F2F8F0F5F8F2F24040404040404040404040E8";
    private String networkName = "PEER02";
    private final ParserFactory parserFactory;
    private final MapperFactory mapperFactory;
    private final FieldLogicFactory fieldLogicFactory;

    public ParseViewerPanel(BeanProviderInstance beanProviderInstance) {
        initializeComponents();
        createPanelsLayout();
        setupEventHandlers();
        this.parserFactory = beanProviderInstance.parserFactory();
        this.mapperFactory = beanProviderInstance.mapperFactory();
        this.fieldLogicFactory = beanProviderInstance.fieldLogicFactory();
        //Poner el foco en el JTextArea de entrada
        SwingUtilities.invokeLater(() -> {
            inputTextArea.requestFocusInWindow();
        });
    }

    private void initializeComponents() {

        setLayout(new BorderLayout());
        inputTextArea = ComponentsUtil.createInputTextArea();
        inputTextArea.setText(inputMessage);

        outputTextArea = ComponentsUtil.createOutputTextArea();

        parseButton = ComponentsUtil.createButton("Parser");
        limpiarButton = ComponentsUtil.createButton("Limpiar");
        copiarRespuestaButton = ComponentsUtil.createButton("Copiar respuesta");

        optionMastercard = ComponentsUtil.createRadioButton("Mastercard", true);
        // optionMastercard.setVisible(true);
        optionVisa = ComponentsUtil.createRadioButton("Visa", false);
        //optionVisa.setVisible(true);

        // Inicializar el árbol jerárquico
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mensaje Parseado");
        treeModel = new DefaultTreeModel(root);
        resultTree = new JTree(treeModel);
        //resultTree.setFont(new Font("SansSerif"v Font.PLAIN, 12));
        //resultTree.setCellRenderer(new NoIconTreeCellRenderer());
        resultTree.setRootVisible(true);
    }

    private void createPanelsLayout() {
        //MyDoggyToolWindowManager toolWindowManager = PanelDoggy.setupStructureMyDoggy(createMainPanel(), resultTree, createOutputPanel());
        MyDoggyToolWindowManager toolWindowManager = PanelDoggy.setupStructureMyDoggy(createMainPanel2(), createOutputPanel());
        add(toolWindowManager, BorderLayout.CENTER);
    }

    private JPanel createMainPanel2() {
        // Panel para el JTree
        JPanel treePanel = new JPanel(new BorderLayout());
        treePanel.setBorder(BorderFactory.createTitledBorder("Estructura del mensaje"));
        treePanel.add(new JScrollPane(resultTree), BorderLayout.CENTER);

        // Panel de entrada y botones (como antes)
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Mensaje de entrada"));
        inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(parseButton);
        buttonPanel.add(limpiarButton);

        JPanel optionPanel = new JPanel(new FlowLayout());
        optionPanel.add(optionMastercard);
        optionPanel.add(optionVisa);

        inputPanel.add(optionPanel, BorderLayout.NORTH);
        inputPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Dividir horizontalmente: árbol a la izquierda, entrada a la derecha
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel, inputPanel);
        splitPane.setResizeWeight(0.3); // 30% para el árbol, 70% para la entrada

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setSize(new Dimension(1000, 600));
        mainPanel.add(splitPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private JPanel createMainPanel3() {
        // 1. Instanciar los paneles reutilizables
        // Usamos la clase TextActionPanel que definimos anteriormente
        InputTextPanel inputPanel = new InputTextPanel("Mensaje de Entrada", "Parsear", "Limpiar");


        // 2. Configurar el panel de opciones (Visa/Mastercard)
       /* JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionPanel.add(optionMastercard);
        optionPanel.add(optionVisa);
        inputPanel.add(optionPanel, BorderLayout.NORTH);*/

        // 3. Crear el panel del árbol
        JPanel treePanel = new JPanel(new BorderLayout());
        treePanel.setBorder(BorderFactory.createTitledBorder("Estructura del mensaje"));
        treePanel.add(new JScrollPane(resultTree), BorderLayout.CENTER);

        // 4. Organizar en un SplitPane para que sea redimensionable
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePanel, inputPanel);
        splitPane.setResizeWeight(0.3); // 30% árbol, 70% entrada
        splitPane.setBorder(null);

        // 5. Panel contenedor final
        JPanel container = new JPanel(new BorderLayout());
        container.add(splitPane, BorderLayout.CENTER);


        // 6. Asignar los listeners a los botones del panel reutilizable
        inputPanel.getBtnPrimary().addActionListener(e -> parseMessage());
        inputPanel.getBtnSecondary().addActionListener(e -> limpiarCampos());

        return container;

       /*
        windowManager.registerToolWindow("TREE_VIEW", "Estructura", null,
                treePanel, ToolWindowAnchor.LEFT);

        // Configurar el panel de opciones (Visa/Mastercard) encima del área de texto
       JPanel optionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        optionPanel.add(optionMastercard);
        optionPanel.add(optionVisa);
        inputPanel.add(optionPanel, BorderLayout.NORTH);

        // Registrar en MyDoggy
        ToolWindow inputTW = windowManager.registerToolWindow("INPUT_PANEL", "Entrada ISO", null,
                inputPanel, ToolWindowAnchor.RIGHT);

        // 3. Configurar Acciones (Listeners)
        inputPanel.getBtnPrimary().addActionListener(e -> ejecutarParseo());
        inputPanel.getBtnSecondary().addActionListener(e -> limpiarCampos());

        // Hacerlos visibles por defecto
        inputTW.setActive(true);*/
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

    private void limpiarCampos() {
        inputTextArea.setText("");
        outputTextArea.setText("");
        // Limpiar el JTree
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mensaje Parseado");
        treeModel.setRoot(root);
        treeModel.reload();
    }



    private void setupEventHandlers() {


        parseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseMessage();
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

        optionMastercard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //if (optionMastercard.isSelected()) {
                networkName = "PEER02";
                optionVisa.setSelected(false);
                inputMessage = "F0F1F0F0FEFF640188E1E10A0000000000000040F1F6F5F5F3F6F5F0F9F9F9F9F9F9F9F9F9F9F0F0F0F0F0F0F0F0F0F0F0F0F0F2F2F9F9F0F0F0F0F0F0F0F0F0F6F3F5F6F0F0F0F0F0F0F0F2F2F9F9F0F0F6F1F6F0F7F2F7F2F4F7F2F7F6F4F6F8F0F6F1F0F0F0F0F0F0F8F9F8F7F1F6F0F3F2F7F2F4F0F6F1F6F2F9F0F5F0F6F1F6F0F6F1F5F5F8F1F8F8F4F0F1F0F0F0F6F0F0F3F2F8F6F0F6F0F0F3F2F8F6F5F1F6F7F5F4F8F9F8F7F1F6F0F0F4F0F0F2F1F6F4F0F0F2F1F6F0F0F0F1F0F8F7F7F8C1D7D7D3C54BC3D6D461C2C9D3D3404040404040404040F8F6F660F7F1F260F7F7F5F34040E4E2C1F1F1F8E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C340F6F0F4F8F4F0F6F0F4F0F3F7F0F1F3F3F0F1F2F9F5F0F0F1F9F3C8D2D8C9E6C5E8E5C9E2C5F7E4D2E3D8D2F8E8D1F5C3F0F0F2F6F0F0F0F4F1F0F0F0F0F0F6F0F0F8F4F0F9F5F0F1F44040404040F0F0F9D4C2D2C3C7F4F6F2C6F1F0F1F0F0F1F0F9F5F0F0F1F0F1F8D6D5C540C1D7D7D3C540D7C1D9D240E6C1E8F0F0F2F0F0F3C3C140F0F0F3F0F1F3C1D7D7D3C54BC3D6D440C2C9D3F0F0F4F0F1F0F8F6F6F7F1F2F7F7F5F3F0F0F7F0F2F1F8F4F2F8F0F5F8F2F24040404040404040404040E8";
                inputTextArea.setText(inputMessage);
                /*} else {
                    networkName="PEER01";
                    optionMastercard.setSelected(true);
                    inputMessage="160102019488040300000002100048841029020071000100F664648148F0A016000000000100002410123456789012345600000000000000250000000000122912110724127491600092240830115812015610005906462921003801000E8605F24BF24BA7C002F0F2C101F15600249F2801079F2901009F1F0CF1F2F54BF1F1F44BF6F14BF9DF1F02F2F79F2001019F210102F5F3F4F4F0F7F9F2F2F4F0F8F9F9F9F9F9F9F9F9F1F0F0F1F9F5F5F8F0F2F0F1404040C1D3D75C948995878388A4819587A896A497899587A4404040F0F2F1F6F1F6F8F6F8F8F84040C3D50F4040404040404040404040404040F2015606040501000040051F40001C000000000003853452665303863900620000F0F9F0F0F0F0F0F0F0F00580000000020E5700040102E6E35B00048502F0F3496800460110F4F3F8F4F6F6F5F7F5F0F0F8F3F4F9F30202F1F0030BF4F0F0F6F0F4F0F6F6F6F20604F3F0F1F10702F0F1800100860604001005224282010083010084010085030000001C00800000000000000002010447146900000009C41563457500000000";
                    inputTextArea.setText(inputMessage);
                }*/
            }
        });

        optionVisa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /*if (optionVisa.isSelected()) {
                    networkName="PEER01";
                    optionMastercard.setSelected(false);
                } else {
                    optionVisa.setSelected(true);
                }*/
                networkName = "PEER01";
                optionMastercard.setSelected(false);
                inputMessage = "160102019488040300000002100048841029020071000100F664648148F0A016000000000100002410123456789012345600000000000000250000000000122912110724127491600092240830115812015610005906462921003801000E8605F24BF24BA7C002F0F2C101F15600249F2801079F2901009F1F0CF1F2F54BF1F1F44BF6F14BF9DF1F02F2F79F2001019F210102F5F3F4F4F0F7F9F2F2F4F0F8F9F9F9F9F9F9F9F9F1F0F0F1F9F5F5F8F0F2F0F1404040C1D3D75C948995878388A4819587A896A497899587A4404040F0F2F1F6F1F6F8F6F8F8F84040C3D50F4040404040404040404040404040F2015606040501000040051F40001C000000000003853452665303863900620000F0F9F0F0F0F0F0F0F0F00580000000020E5700040102E6E35B00048502F0F3496800460110F4F3F8F4F6F6F5F7F5F0F0F8F3F4F9F30202F1F0030BF4F0F0F6F0F4F0F6F6F6F20604F3F0F1F10702F0F1800100860604001005224282010083010084010085030000001C00800000000000000002010447146900000009C41563457500000000";
                inputTextArea.setText(inputMessage);
            }
        });
    }

    private void parseMessage() {

        Map<String, String> mapValues;

        try {

            String inputMessage = inputTextArea.getText().trim();
            if (inputMessage.isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame, "Por favor ingrese un mensaje para parsear",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (optionMastercard.isSelected()) {
                delegateParser = parserFactory.getDelegateParser("PEER02");
                delegateMapper = mapperFactory.getDelegateMapper("PEER02");
                delegateFieldLogic = fieldLogicFactory.getDelegateFieldLogic("PEER02");
            } else {
                delegateParser = parserFactory.getDelegateParser("PEER01");
                delegateMapper = mapperFactory.getDelegateMapper("PEER01");
                delegateFieldLogic = fieldLogicFactory.getDelegateFieldLogic("PEER01");
            }

            mapValues = delegateParser.parser(inputMessage);

            Map<String, String> mapValuesTree = new HashMap<>(mapValues);
            if (mapValues.containsKey("additionalDataRetailer")) {
                String inputMessageTemp = mapValuesTree.get("additionalDataRetailer");
                inputMessageTemp = ISOUtil.ebcdicToString(inputMessageTemp);
                mapValuesTree.put("additionalDataRetailer", inputMessageTemp);
            }

            ParseResult result = ParseGUI.process(mapValuesTree);

            ISO8583 iso8583 = ISO8583Builder.buildISO8583(inputMessage, mapValues);
            Map<String, String> subFields = delegateFieldLogic.parseSubfields(iso8583);
            ISO20022 iso20022 = delegateMapper.mapper(iso8583, subFields);
            //String tramaHex=delegateParser.unParser(mapValues);

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