package com.bbva.gui.panels;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.gui.commons.ISO8583Processor;
import com.bbva.gui.commons.ParseProcessor;
import com.bbva.gui.components.InputTextPane;
import com.bbva.gui.components.OutputTextPane;
import com.bbva.gui.components.TreeOutputPane;
import com.bbva.gui.dto.ParseResult;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.FXParseGUI;
import com.bbva.gui.utils.FXUtils;
import com.bbva.orchestrator.core.builders.ISO8583Builder;
import com.bbva.orchestrator.core.dto.ISO8583;
import com.bbva.orchestrator.core.logic.factory.FieldLogicFactory;
import com.bbva.orchestrator.core.logic.factory.NetworkDelegateFieldLogic;
import com.bbva.orchestrator.core.mapper.factory.ISO20022DelegateMapper;
import com.bbva.orchestrator.core.mapper.factory.MapperFactory;
import com.bbva.orchestrator.core.utils.ISOUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

public class ParseClearViewerPane extends AbstractBasePane {

    private static final String SAMPLE_MESSAGE = "0100FEFF660128E1E30A0000000000000010165193481234564714000000000000001811000000000539000000001811022012033872976256610000001687000703380220300702200219554160407100106007963370000000000000000000000000000000000000605112168700FD8068848742323        IZI*ESTACION DE SERVIC SAN JUAN DE L PER240D9F2F3F0F2F0F0F3F7F3F4F0F1F1F1F0F0F0F0F0F2F5F3F3F5F7F0F3F1F5F0F0F0F0F0F0F0F0F8F7F4F2F3F2F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F1C1D8E2F1F9F9C1D8C6F1F1F1F7F5F3F2F0F1F0F3F0F8F6F0F2F0F2F5F6F0F3F0F3F0F8F6F0F4F0F2F5F6F0F5F0F2F0F0F7F1F0F4F1F8C3406048406042485F2A020604820219808407A0000000041010950500000080019A032602209C01009F02060000000018119F03060000000000009F10120110A04001220000000000000000000000FF9F1A0206049F260827BB155934A62ECF9F2701809F3303E008C89F34031F03029F3501229F360200BC9F3704622DCBAA9F53015203701330129500132OWBA1Z3TJW13I6GFD31U5OJ0190000000000300604L36009MDS4A32IH0011";

    private final MapperFactory mapperFactory;
    private final FieldLogicFactory fieldLogicFactory;
    private InputTextPane inputPane;
    private OutputTextPane outputPane;
    private TreeOutputPane treePane;

    public ParseClearViewerPane(BeanProviderInstance beans) {
        this.mapperFactory = beans.mapperFactory();
        this.fieldLogicFactory = beans.fieldLogicFactory();
        initComponents();
        buildLayout();
        setupHandlers();
    }

    private void initComponents() {
        inputPane = new InputTextPane("input", "Procesar", "Limpiar");
        inputPane.getTextArea().setText(SAMPLE_MESSAGE);
        outputPane = new OutputTextPane("output", "Copiar");
        treePane = new TreeOutputPane("Estructura del mensaje");
    }

    private void buildLayout() {
        setCenter(createLayoutWithTree(treePane, inputPane, outputPane));
    }

    private void setupHandlers() {
        registerPrimaryButton(inputPane, this::parseMessage);
        registerSecondaryButton(inputPane, () -> clearFields(inputPane, outputPane, treePane));
        setupCopyToClipboard(outputPane);
        setupTreeClickHandler(treePane);
    }

    private void parseMessage() {
        try {
            String inputMessage = inputPane.getTextArea().getText();
            if (inputMessage.isEmpty()) {
                FXUtils.showInfoAlert("Aviso", "Por favor ingrese un mensaje para parsear");
                return;
            }

            ISO20022DelegateMapper delegateMapper = mapperFactory.getDelegateMapper("PEER02");
            NetworkDelegateFieldLogic delegateFieldLogic = fieldLogicFactory.getDelegateFieldLogic("PEER02");
            Map<String, String> mapValues = ISO8583Processor.createMapFieldsISO8583Mastercard(inputMessage);

            Map<String, String> mapValuesTree = new HashMap<>(mapValues);
            if (mapValues.containsKey("additionalDataRetailer")) {
                mapValuesTree.put("additionalDataRetailer",
                        ISOUtil.ebcdicToString(mapValuesTree.get("additionalDataRetailer")));
            }

            ParseResult result = ParseProcessor.process(mapValuesTree);
            ISO8583 iso8583 = ISO8583Builder.buildISO8583(inputMessage, mapValues);
            Map<String, String> subFields = delegateFieldLogic.parseSubfields(iso8583);
            ISO20022 iso20022 = delegateMapper.mapper(iso8583, subFields, "peer02");

            FXParseGUI.updateTreeView(treePane.getTreeView(), result);
            ObjectMapper objectMapper = new ObjectMapper();
            outputPane.getTextArea()
                    .setText(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(iso20022));

        } catch (Exception ex) {
            FXUtils.showErrorAlert("Error al parsear el mensaje: " + ex.getMessage());
            outputPane.getTextArea().setText("Error: " + ex.getMessage());
        }
    }
}
