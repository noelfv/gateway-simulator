package com.bbva.gui.panels.v2;

import com.bbva.gui.commons.ISO8583Processor;
import com.bbva.gui.components.InputTextPanel;
import com.bbva.gui.components.JtreeOutputPanel;
import com.bbva.gui.components.OutputTextPanel;
import com.bbva.gui.components.PanelDoggy;
import com.bbva.gui.dto.ParseResult;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.ParseGUI;
import com.bbva.gui.utils.SwingUtils;
import com.bbva.gui.utils.UtilGUI;
import com.bbva.orchestrator.core.logic.factory.FieldLogicFactory;
import com.bbva.orchestrator.core.logic.factory.NetworkDelegateFieldLogic;
import com.bbva.orchestrator.core.mapper.factory.ISO20022DelegateMapper;
import com.bbva.orchestrator.core.mapper.factory.MapperFactory;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import com.bbva.orchestrator.core.utils.ISOUtil;
import lombok.Setter;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;


public class ConvertTramaOriginalVisaViewerPanel extends JPanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertTramaOriginalVisaViewerPanel.class);

    @Setter
    private JInternalFrame parentFrame;
    private ISO8583DelegateParser delegateParser;
    private ISO20022DelegateMapper delegateMapper;
    private NetworkDelegateFieldLogic delegateFieldLogic;
    private String inputMessage = "0100FEFF660128E1E30A0000000000000010165193481234564714000000000000001811000000000539000000001811022012033872976256610000001687000703380220300702200219554160407100106007963370000000000000000000000000000000000000605112168700FD8068848742323        IZI*ESTACION DE SERVIC SAN JUAN DE L PER240D9F2F3F0F2F0F0F3F7F3F4F0F1F1F1F0F0F0F0F0F2F5F3F3F5F7F0F3F1F5F0F0F0F0F0F0F0F0F8F7F4F2F3F2F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F1C1D8E2F1F9F9C1D8C6F1F1F1F7F5F3F2F0F1F0F3F0F8F6F0F2F0F2F5F6F0F3F0F3F0F8F6F0F4F0F2F5F6F0F5F0F2F0F0F7F1F0F4F1F8C3406048406042485F2A020604820219808407A0000000041010950500000080019A032602209C01009F02060000000018119F03060000000000009F10120110A04001220000000000000000000000FF9F1A0206049F260827BB155934A62ECF9F2701809F3303E008C89F34031F03029F3501229F360200BC9F3704622DCBAA9F53015203701330129500132OWBA1Z3TJW13I6GFD31U5OJ0190000000000300604L36009MDS4A32IH0011";
    private String networkName = "PEER02";
    private final ParserFactory parserFactory;
    private final MapperFactory mapperFactory;
    private final FieldLogicFactory fieldLogicFactory;

    private InputTextPanel inputTextPanel;
    private OutputTextPanel outputTextPanel;
    private JtreeOutputPanel treeStructurePanel;

    public ConvertTramaOriginalVisaViewerPanel(BeanProviderInstance beanProviderInstance) {
        initializeComponents();
        createPanelsLayout();
        setupEventHandlers();
        this.parserFactory = beanProviderInstance.parserFactory();
        this.mapperFactory = beanProviderInstance.mapperFactory();
        this.fieldLogicFactory = beanProviderInstance.fieldLogicFactory();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        inputTextPanel=new InputTextPanel("input","Parsear","Limpiar");
        inputTextPanel.getTextArea().setText(inputMessage);
        outputTextPanel=new OutputTextPanel("output","Copiar");
        treeStructurePanel=new JtreeOutputPanel("Estructura del mensaje");
    }

    private void createPanelsLayout() {
        MyDoggyToolWindowManager toolWindowManager = PanelDoggy.setupStructureMyDoggy(treeStructurePanel,inputTextPanel,outputTextPanel);
        add(toolWindowManager, BorderLayout.CENTER);
    }

    private void limpiarCampos() {
        inputTextPanel.getTextArea().setText("");
        outputTextPanel.getTextArea().setText("");
        // Limpiar el JTree

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mensaje Parseado");
        treeStructurePanel.getTreeModel().setRoot(root);
        treeStructurePanel.getTreeModel().reload();
    }



    private void setupEventHandlers() {


        inputTextPanel.getBtnPrimary().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseMessage();
            }
        });

        inputTextPanel.getBtnSecondary().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarCampos();
            }
        });


        outputTextPanel.getBtnPrimary().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection selection = new StringSelection(outputTextPanel.getTextArea().getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                outputTextPanel.getTextArea().requestFocusInWindow();
                outputTextPanel.getTextArea().selectAll();

                SwingUtils.mostrarTooltipTemporal(outputTextPanel.getBtnPrimary(), "¡Texto copiado!", 1000);
                // Mostrar ventana emergente
                //SwingUtils.crearMenuContextual(outputTextArea);
            }
        });


        // Agregar MouseListener al árbol
        treeStructurePanel.getResultTree().addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 1) {
                    int selRow = treeStructurePanel.getResultTree().getRowForLocation(evt.getX(), evt.getY());
                    if (selRow != -1) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                                treeStructurePanel.getResultTree().getLastSelectedPathComponent();
                        if (node != null && node.isLeaf()) {
                            ParseGUI.showNodeDetails(node, evt.getX(), evt.getY());
                        }
                    }
                }
            }
        });

        /*optionMastercard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //if (optionMastercard.isSelected()) {
                networkName = "PEER02";
                optionVisa.setSelected(false);
                inputMessage = "F0F1F0F0FEFF640188E1E10A0000000000000040F1F6F5F5F3F6F5F0F9F9F9F9F9F9F9F9F9F9F0F0F0F0F0F0F0F0F0F0F0F0F0F2F2F9F9F0F0F0F0F0F0F0F0F0F6F3F5F6F0F0F0F0F0F0F0F2F2F9F9F0F0F6F1F6F0F7F2F7F2F4F7F2F7F6F4F6F8F0F6F1F0F0F0F0F0F0F8F9F8F7F1F6F0F3F2F7F2F4F0F6F1F6F2F9F0F5F0F6F1F6F0F6F1F5F5F8F1F8F8F4F0F1F0F0F0F6F0F0F3F2F8F6F0F6F0F0F3F2F8F6F5F1F6F7F5F4F8F9F8F7F1F6F0F0F4F0F0F2F1F6F4F0F0F2F1F6F0F0F0F1F0F8F7F7F8C1D7D7D3C54BC3D6D461C2C9D3D3404040404040404040F8F6F660F7F1F260F7F7F5F34040E4E2C1F1F1F8E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C340F6F0F4F8F4F0F6F0F4F0F3F7F0F1F3F3F0F1F2F9F5F0F0F1F9F3C8D2D8C9E6C5E8E5C9E2C5F7E4D2E3D8D2F8E8D1F5C3F0F0F2F6F0F0F0F4F1F0F0F0F0F0F6F0F0F8F4F0F9F5F0F1F44040404040F0F0F9D4C2D2C3C7F4F6F2C6F1F0F1F0F0F1F0F9F5F0F0F1F0F1F8D6D5C540C1D7D7D3C540D7C1D9D240E6C1E8F0F0F2F0F0F3C3C140F0F0F3F0F1F3C1D7D7D3C54BC3D6D440C2C9D3F0F0F4F0F1F0F8F6F6F7F1F2F7F7F5F3F0F0F7F0F2F1F8F4F2F8F0F5F8F2F24040404040404040404040E8";
                inputTextArea.setText(inputMessage);
            }
        });*/


    }

    private void parseMessage() {

        Map<String, String> mapValues;

        try {

            String inputMessage = inputTextPanel.getTextArea().getText().trim();
            if (inputMessage.isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame, "Por favor ingrese un mensaje para parsear",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //if (optionMastercard.isSelected()) {
                delegateParser = parserFactory.getDelegateParser("PEER01");
                delegateMapper = mapperFactory.getDelegateMapper("PEER01");
                delegateFieldLogic = fieldLogicFactory.getDelegateFieldLogic("PEER01");
            /*} else {
                delegateParser = parserFactory.getDelegateParser("PEER01");
                delegateMapper = mapperFactory.getDelegateMapper("PEER01");
                delegateFieldLogic = fieldLogicFactory.getDelegateFieldLogic("PEER01");
            }*/

            //mapValues = delegateParser.parser(inputMessage);
           mapValues =ISO8583Processor.createMapFieldsISO8583Visa(inputMessage);

            /*Map<String, String> mapValuesTree = new HashMap<>(mapValues);
            if (mapValues.containsKey("additionalDataRetailer")) {
                String campo48 = mapValuesTree.get("additionalDataRetailer");
                LOGGER.info("Campo 48 en crudo: [{}]", campo48);
                mapValuesTree.put("additionalDataRetailer", ISOUtil.ebcdicToString(campo48));
            }*/

          //  ParseResult result = ParseGUI.process(mapValues);
          //  ParseGUI.updateTreeView(treeStructurePanel.getTreeModel(),treeStructurePanel.getResultTree(), result);


            String trama= delegateParser.unParser(mapValues);

            outputTextPanel.getTextArea().setText(trama);

        } catch (Exception ex) {
            UtilGUI.showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
            outputTextPanel.getTextArea().setText("Error: " + ex.getMessage());
        }
    }


}