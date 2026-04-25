package com.bbva.gui.panels;

import com.bbva.gui.commons.FXParseGUI;
import com.bbva.gui.commons.ParseProcessor;
import com.bbva.gui.components.InputTextPane;
import com.bbva.gui.components.OutputTextPane;
import com.bbva.gui.components.TreeOutputPane;
import com.bbva.gui.dto.ParseResult;
import com.bbva.gui.spring.ApplicationContextProvider;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.FXUtils;
import com.bbva.orchestrator.core.fields.MastercardISOField;
import com.bbva.orchestrator.core.fields.VisaISOField;
import com.bbva.orchestrator.core.fields.definitions.subfields.tlv.TLVFieldLoadStructure;
import com.bbva.orchestrator.core.parser.iso8583.handlers.NetworkHandlerField;
import com.bbva.orchestrator.core.parser.iso8583.handlers.impl.MastercardHandlerField;
import com.bbva.orchestrator.core.parser.iso8583.handlers.impl.VisaHandlerField;
import com.bbva.orchestrator.core.parser.iso8583.strategy.subfields.CompositeTlvFieldParser;
import com.bbva.orchestrator.core.parser.iso8583.strategy.subfields.CompositeTlvFieldParserMixed;
import com.bbva.orchestrator.core.utils.ISOUtil;
import javafx.scene.control.ComboBox;

import java.util.Map;

public class TLVParseViewerPane extends AbstractBasePane {

    private static final String SAMPLE_CAMPO_48 = "E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C3C3";

    private InputTextPane inputPane;
    private OutputTextPane outputPane;
    private TreeOutputPane treePane;
    private ComboBox<InputTextPane.ComboItem> comboBoxRedes;
    private ComboBox<InputTextPane.ComboItem> comboBoxCampos;

    public TLVParseViewerPane(BeanProviderInstance beans) {
        initComponents();
        buildLayout();
        setupHandlers();
    }

    private void initComponents() {
        comboBoxRedes = new ComboBox<>();
        comboBoxRedes.getItems().addAll(
                new InputTextPane.ComboItem("peer02", "Mastercard"),
                new InputTextPane.ComboItem("peer01", "Visa"));
        comboBoxRedes.getSelectionModel().selectFirst();

        comboBoxCampos = new ComboBox<>();
        comboBoxCampos.getItems().addAll(
                new InputTextPane.ComboItem("48", "Campo 48"),
                new InputTextPane.ComboItem("104", "Campo 104"));
        comboBoxCampos.getSelectionModel().selectFirst();

        inputPane = new InputTextPane("Mensaje de entrada", "Procesar", "Limpiar",
                comboBoxRedes,
                comboBoxCampos, "Campo TLV:",
                InputTextPane.ComboDirection.LEFT);
        inputPane.getTextArea().setText(SAMPLE_CAMPO_48);

        outputPane = new OutputTextPane("output", "Copiar");
        treePane = new TreeOutputPane("Estructura TLV");

        comboBoxCampos.setOnAction(e -> {
            InputTextPane.ComboItem campo = comboBoxCampos.getValue();
            if (campo != null && "48".equals(campo.getId())) {
                inputPane.getTextArea().setText(SAMPLE_CAMPO_48);
            } else {
                inputPane.getTextArea().clear();
            }
        });
    }

    private void buildLayout() {
        setCenter(createLayoutWithTree(treePane, inputPane, outputPane));
    }

    private void setupHandlers() {
        registerPrimaryButton(inputPane, this::parseMessage);
        registerSecondaryButton(inputPane, () -> clearFields(inputPane, outputPane, treePane));
        setupCopyToClipboard(outputPane);
        setupTreeClickHandler(treePane, () -> comboBoxRedes.getValue() != null
                ? comboBoxRedes.getValue().getLabel()
                : "");
    }

    private void parseMessage() {
        try {
            String inputMessage = inputPane.getTextArea().getText();
            if (inputMessage.isEmpty()) {
                FXUtils.showInfoAlert("Aviso", "Por favor ingrese un mensaje para parsear");
                return;
            }

            String red = comboBoxRedes.getValue().getId();
            String campo = comboBoxCampos.getValue().getId();

            NetworkHandlerField handlerField = "peer01".equalsIgnoreCase(red)
                    ? ApplicationContextProvider.getBean(VisaHandlerField.class)
                    : ApplicationContextProvider.getBean(MastercardHandlerField.class);

            Map<String, String> mapValues;
            if ("104".equals(campo)) {
                CompositeTlvFieldParserMixed parser104 = new CompositeTlvFieldParserMixed("104");
                mapValues = parser104.parseToMap(inputMessage, handlerField);
            } else {
                var fieldDef = "peer01".equalsIgnoreCase(red)
                        ? VisaISOField.ADDITIONAL_DATA_48
                        : MastercardISOField.ADDITIONAL_DATA_48;
                CompositeTlvFieldParser field48Parser = new CompositeTlvFieldParser("48",
                        TLVFieldLoadStructure.getDirectSubFieldDefinitionsForField48());
                mapValues = field48Parser.parseToMap(inputMessage, fieldDef, handlerField);
            }

            ParseResult result = ParseProcessor.processTLV(mapValues);
            FXParseGUI.updateTreeViewTLV(treePane.getTreeView(), result);
            outputPane.getTextArea().setText(ISOUtil.ebcdicToString(inputMessage));

        } catch (Exception ex) {
            FXUtils.showErrorAlert("Error al parsear el mensaje: " + ex.getMessage());
            outputPane.getTextArea().setText("Error: " + ex.getMessage());
        }
    }

    public void setInputText(String text) {
        inputPane.getTextArea().setText(text);
    }
}
