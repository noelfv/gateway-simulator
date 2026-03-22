package com.bbva.gui.panels.v2;

import com.bbva.gui.components.InputTextPane;
import com.bbva.gui.components.OutputTextPane;
import com.bbva.gui.components.TreeOutputPane;
import com.bbva.gui.dto.ParseResult;
import com.bbva.gui.spring.ApplicationContextProvider;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.FXParseGUI;
import com.bbva.gui.utils.FXUtils;
import com.bbva.orchestrator.core.parser.iso8583.handlers.impl.MastercardHandlerField;
import com.bbva.orchestrator.core.parser.iso8583.strategy.subfields.CompositeTlvFieldParser;
import com.bbva.orchestrator.core.utils.ISOUtil;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigurationPane extends AbstractBasePane {

    private static final String SAMPLE_MESSAGE = "E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C3C3";

    private InputTextPane inputPane;
    private OutputTextPane outputPane;
    private TreeOutputPane treePane;

    public ConfigurationPane(BeanProviderInstance beans) {
        initComponents();
        buildLayout();
        setupHandlers();
    }

    private void initComponents() {
        inputPane = new InputTextPane("Mensaje de entrada", "Parser", "Importar Config");
        inputPane.getTextArea().setText(SAMPLE_MESSAGE);
        outputPane = new OutputTextPane("output", "Copiar");
        treePane = new TreeOutputPane("Estructura TLV");
    }

    private void buildLayout() {
        setCenter(createLayoutWithTree(treePane, inputPane, outputPane));
    }

    private void setupHandlers() {
        registerPrimaryButton(inputPane, this::parseMessage);
        registerSecondaryButton(inputPane, this::importarConfiguracionCampos);
        setupCopyToClipboard(outputPane);
        setupTreeClickHandler(treePane);
    }

    private void parseMessage() {
        try {
            String inputMessage = inputPane.getTextArea().getText().trim();
            if (inputMessage.isEmpty()) {
                FXUtils.showInfoAlert("Aviso", "Por favor ingrese un mensaje para parsear");
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

            ParseResult result = FXParseGUI.processTLV(mapValues);
            FXParseGUI.updateTreeViewTLV(treePane.getTreeView(), result);
            outputPane.getTextArea().setText(inputMessageTemp);

        } catch (Exception ex) {
            FXUtils.showErrorAlert("Error al parsear el mensaje: " + ex.getMessage());
            outputPane.getTextArea().setText("Error: " + ex.getMessage());
        }
    }

    private void importarConfiguracionCampos() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Configuración de Campos (JSON)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON Files", "*.json"));
        File archivo = fileChooser.showOpenDialog(getScene() != null ? getScene().getWindow() : null);
        if (archivo != null) {
            try {
                String content = new String(java.nio.file.Files.readAllBytes(archivo.toPath()));
                String valores = content.split(":")[1].replace("\"", "").replace("}", "").trim();
                List<Integer> nuevaLista = Arrays.stream(valores.split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .toList();
                FXUtils.showInfoAlert("Importación BBVA", "Configuración cargada: " + nuevaLista.size() + " campos.");
            } catch (Exception e) {
                FXUtils.showErrorAlert("Error al leer el archivo JSON: " + e.getMessage());
            }
        }
    }

    public void setInputText(String text) {
        inputPane.getTextArea().setText(text);
    }
}
