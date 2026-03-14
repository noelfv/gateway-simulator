package com.bbva.gui.panels;

import com.bbva.gui.commons.ISO8583Processor;
import com.bbva.gui.components.InputTextPanel;
import com.bbva.gui.components.JtreeOutputPanel;
import com.bbva.gui.components.OutputTextPanel;
import com.bbva.gui.components.PanelDoggy;
import com.bbva.gui.panels.AbstractBasePanel;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.UtilGUI;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import lombok.Setter;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;

import javax.swing.*;
import java.awt.*;
import java.util.Map;


public class ConvertTramaOriginalVisaViewerPanel extends AbstractBasePanel {

    private static final String SAMPLE_MESSAGE = "0100FEFF660128E1E30A0000000000000010165193481234564714000000000000001811000000000539000000001811022012033872976256610000001687000703380220300702200219554160407100106007963370000000000000000000000000000000000000605112168700FD8068848742323        IZI*ESTACION DE SERVIC SAN JUAN DE L PER240D9F2F3F0F2F0F0F3F7F3F4F0F1F1F1F0F0F0F0F0F2F5F3F3F5F7F0F3F1F5F0F0F0F0F0F0F0F0F8F7F4F2F3F2F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F1C1D8E2F1F9F9C1D8C6F1F1F1F7F5F3F2F0F1F0F3F0F8F6F0F2F0F2F5F6F0F3F0F3F0F8F6F0F4F0F2F5F6F0F5F0F2F0F0F7F1F0F4F1F8C3406048406042485F2A020604820219808407A0000000041010950500000080019A032602209C01009F02060000000018119F03060000000000009F10120110A04001220000000000000000000000FF9F1A0206049F260827BB155934A62ECF9F2701809F3303E008C89F34031F03029F3501229F360200BC9F3704622DCBAA9F53015203701330129500132OWBA1Z3TJW13I6GFD31U5OJ0190000000000300604L36009MDS4A32IH0011";

    @Setter
    private JInternalFrame parentFrame;
    private final ParserFactory parserFactory;
    private InputTextPanel inputTextPanel;
    private OutputTextPanel outputTextPanel;
    private JtreeOutputPanel treeStructurePanel;

    public ConvertTramaOriginalVisaViewerPanel(BeanProviderInstance beanProviderInstance) {
        initializeComponents();
        createPanelsLayout();
        setupEventHandlers();
        this.parserFactory = beanProviderInstance.parserFactory();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        inputTextPanel = new InputTextPanel("input", "Parsear", "Limpiar");
        inputTextPanel.getTextArea().setText(SAMPLE_MESSAGE);
        outputTextPanel = new OutputTextPanel("output", "Copiar");
        treeStructurePanel = new JtreeOutputPanel("Estructura del mensaje");
    }

    private void createPanelsLayout() {
        MyDoggyToolWindowManager toolWindowManager = PanelDoggy.setupStructureMyDoggy(treeStructurePanel, inputTextPanel, outputTextPanel);
        add(toolWindowManager, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        registerPrimaryButton(inputTextPanel, this::parseMessage);
        registerSecondaryButton(inputTextPanel, () -> clearFields(inputTextPanel, outputTextPanel, treeStructurePanel, "Mensaje Parseado"));
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

            Map<String, String> mapValues = ISO8583Processor.createMapFieldsISO8583Visa(inputMessage);
            ISO8583DelegateParser delegateParser = parserFactory.getDelegateParser("PEER01");
            String trama = delegateParser.unParser(mapValues);
            outputTextPanel.getTextArea().setText(trama);

        } catch (Exception ex) {
            UtilGUI.showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
            outputTextPanel.getTextArea().setText("Error: " + ex.getMessage());
        }
    }
}
