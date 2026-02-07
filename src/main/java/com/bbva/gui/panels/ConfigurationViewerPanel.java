package com.bbva.gui.panels;

import com.bbva.gui.components.PanelDoggy;
import com.bbva.gui.dto.ParseResult;
import com.bbva.gui.spring.ApplicationContextProvider;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.ParseGUI;
import com.bbva.gui.utils.SwingUtils;
import com.bbva.gui.utils.UtilGUI;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.iso8583.handlers.impl.MastercardHandlerField;
import com.bbva.orchestrator.core.parser.iso8583.strategy.subfields.CompositeTlvFieldParser;
import com.bbva.orchestrator.core.utils.ISOUtil;
import lombok.Setter;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConfigurationViewerPanel extends JPanel {

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

    final String SAMPLE_MESSAGE = "E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C3C3";

    public ConfigurationViewerPanel(BeanProviderInstance beanProviderInstance) {
        initializeComponents();
        createPanelsLayout();
        setupEventHandlers();
        //ParserFactory parserFactory = ApplicationContextProvider.getBean(ParserFactory.class);
        delegateParser = beanProviderInstance.parserFactory().getDelegateParser("PEER02");
        System.out.println("DelegateParser bean: " + delegateParser);

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
                importarConfiguracionCampos();
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
            MastercardHandlerField mastercardHandlerField=ApplicationContextProvider.getBean(MastercardHandlerField.class);

            CompositeTlvFieldParser fieldTLV= new CompositeTlvFieldParser("48");
            Map<String, String> mapValues=new HashMap<>();
            String inputMessageTemp;
            if(inputMessage.substring(2,3).startsWith("F")){
                inputMessageTemp=inputMessage;
                System.out.println("Mensaje en formato EBCDIC " + inputMessageTemp);
                mapValues=fieldTLV.parseToMap(inputMessageTemp,null,mastercardHandlerField);
                inputMessageTemp=ISOUtil.ebcdicToString(inputMessageTemp);
            }else {
                inputMessageTemp= ISOUtil.stringToEBCDICHex(inputMessage);
                System.out.println("Mensaje en formato ASCII " + inputMessageTemp);
                mapValues=fieldTLV.parseToMap(inputMessageTemp,null,mastercardHandlerField);
            }

            ParseResult result = ParseGUI.processTLV(mapValues);
            ParseGUI.updateTreeViewTLV(treeModel, resultTree, result);
            outputTextArea.setText(inputMessageTemp);

        } catch (Exception ex) {
            UtilGUI.showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
            outputTextArea.setText("Error: " + ex.getMessage());
        }
    }

    private void importarConfiguracionCampos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Configuración de Campos (JSON)");
        //fileChooser.setBackground(BBVA_GRIS);
       // fileChooser.setForeground(BBVA_AZUL);
        int selection = fileChooser.showOpenDialog(this);
        if (selection == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            try {
                String content = new String(java.nio.file.Files.readAllBytes(archivo.toPath()));
                String valores = content.split(":")[1].replace("\"", "").replace("}", "").trim();
                List<Integer> nuevaLista = Arrays.stream(valores.split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .toList();
                JOptionPane.showMessageDialog(this, "Configuración cargada: " + nuevaLista.size() + " campos.", "Importación BBVA", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo JSON: " + e.getMessage(), "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setInputText(String text) {
        inputTextArea.setText(text);
    }

}