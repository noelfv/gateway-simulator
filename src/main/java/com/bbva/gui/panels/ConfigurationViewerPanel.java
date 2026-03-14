package com.bbva.gui.panels;

import com.bbva.gui.components.InputTextPanel;
import com.bbva.gui.components.JtreeOutputPanel;
import com.bbva.gui.components.OutputTextPanel;
import com.bbva.gui.components.PanelDoggy;
import com.bbva.gui.dto.ParseResult;
import com.bbva.gui.spring.ApplicationContextProvider;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.ParseGUI;
import com.bbva.gui.utils.UtilGUI;
import com.bbva.orchestrator.core.parser.iso8583.handlers.impl.MastercardHandlerField;
import com.bbva.orchestrator.core.parser.iso8583.strategy.subfields.CompositeTlvFieldParser;
import com.bbva.orchestrator.core.utils.ISOUtil;
import lombok.Setter;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ConfigurationViewerPanel extends AbstractBasePanel {

    private static final String SAMPLE_MESSAGE = "E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C3C3";

    @Setter
    private JInternalFrame parentFrame;
    private InputTextPanel inputTextPanel;
    private OutputTextPanel outputTextPanel;
    private JtreeOutputPanel treeStructurePanel;

    public ConfigurationViewerPanel(BeanProviderInstance beanProviderInstance) {
        initializeComponents();
        createPanelsLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        inputTextPanel = new InputTextPanel("Mensaje de entrada", "Parser", "Importar Config");
        inputTextPanel.getTextArea().setText(SAMPLE_MESSAGE);
        outputTextPanel = new OutputTextPanel("output", "Copiar");
        treeStructurePanel = new JtreeOutputPanel("Estructura TLV");
    }

    private void createPanelsLayout() {
        MyDoggyToolWindowManager toolWindowManager = PanelDoggy.setupStructureMyDoggy(treeStructurePanel, inputTextPanel, outputTextPanel);
        add(toolWindowManager, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        registerPrimaryButton(inputTextPanel, this::parseMessage);
        registerSecondaryButton(inputTextPanel, this::importarConfiguracionCampos);
        setupCopyToClipboard(outputTextPanel);
        setupTreeClickHandler(treeStructurePanel);
    }

    private void parseMessage() {
        try {
            String inputMessage = inputTextPanel.getTextArea().getText().trim();
            if (inputMessage.isEmpty()) {
                JOptionPane.showMessageDialog(parentFrame, "Por favor ingrese un mensaje para parsear",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            MastercardHandlerField mastercardHandlerField = ApplicationContextProvider.getBean(MastercardHandlerField.class);
            CompositeTlvFieldParser fieldTLV = new CompositeTlvFieldParser("48");
            Map<String, String> mapValues = new HashMap<>();
            String inputMessageTemp;

            if (inputMessage.substring(2, 3).startsWith("F")) {
                inputMessageTemp = inputMessage;
                mapValues = fieldTLV.parseToMap(inputMessageTemp, null, mastercardHandlerField);
                inputMessageTemp = ISOUtil.ebcdicToString(inputMessageTemp);
            } else {
                inputMessageTemp = ISOUtil.stringToEBCDICHex(inputMessage);
                mapValues = fieldTLV.parseToMap(inputMessageTemp, null, mastercardHandlerField);
            }

            ParseResult result = ParseGUI.processTLV(mapValues);
            ParseGUI.updateTreeViewTLV(treeStructurePanel.getTreeModel(), treeStructurePanel.getResultTree(), result);
            outputTextPanel.getTextArea().setText(inputMessageTemp);

        } catch (Exception ex) {
            UtilGUI.showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
            outputTextPanel.getTextArea().setText("Error: " + ex.getMessage());
        }
    }

    private void importarConfiguracionCampos() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Seleccionar Configuración de Campos (JSON)");
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
                JOptionPane.showMessageDialog(this, "Configuración cargada: " + nuevaLista.size() + " campos.",
                        "Importación BBVA", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al leer el archivo JSON: " + e.getMessage(),
                        "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void setInputText(String text) {
        inputTextPanel.getTextArea().setText(text);
    }
}
