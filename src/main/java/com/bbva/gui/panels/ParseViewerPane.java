package com.bbva.gui.panels;

import com.bbva.gateway.dto.iso20022.ISO20022;
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
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import com.bbva.orchestrator.core.utils.ISOUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.ComboBox;

import java.util.HashMap;
import java.util.Map;

public class ParseViewerPane extends AbstractBasePane {

    private static final String TRAMA_VISA = "1601020198880403000000021000489D1029020000000120F66464814AF0A016000000000100002410123456789012345600000000000000250000000000122912110724127491600092240830115812015610005906462921003801000EC002F0F2C101F18605F24BF24BA75600249F1F0CF1F2F54BF1F1F44BF6F14BF9DF1F02F2F79F2001019F2101029F2801079F290100F5F3F4F4F0F7F9F2F2F4F0F8F6F5F9F9F9F9F9F9F9F9F1F0F0F1F9F5F5F8F0F2F0F1404040C1D3D75C948995878388A4819587A896A497899587A4404040F0F2F1F6F1F6F8F6F8F8F84040C3D50FF140404040404040404040404040F2015606040501000040051F40001C000000000003853452665303863900620000F0F9F0F0F0F0F0F0F0F007900000000290200E5700040102E6E35B00048502F0F3496800460110F4F3F8F4F6F6F5F7F5F0F0F8F3F4F9F30202F1F0030BF4F0F0F6F0F4F0F6F6F6F20604F3F0F1F10702F0F1800100860604001005224282010083010084010085030000001C00800000000000000002010447146900000009C41563457500000000";
    private static final String TRAMA_MASTERCARD = "F0F1F0F0FEFF640188E1E10A0000000000000040F1F6F5F5F3F6F5F0F9F9F9F9F9F9F9F9F9F9F0F0F0F0F0F0F0F0F0F0F0F0F0F2F2F9F9F0F0F0F0F0F0F0F0F0F6F3F5F6F0F0F0F0F0F0F0F2F2F9F9F0F0F6F1F6F0F7F2F7F2F4F7F2F7F6F4F6F8F0F6F1F0F0F0F0F0F0F8F9F8F7F1F6F0F3F2F7F2F4F0F6F1F6F2F9F0F5F0F6F1F6F0F6F1F5F5F8F1F8F8F4F0F1F0F0F0F6F0F0F3F2F8F6F0F6F0F0F3F2F8F6F5F1F6F7F5F4F8F9F8F7F1F6F0F0F4F0F0F2F1F6F4F0F0F2F1F6F0F0F0F1F0F8F7F7F8C1D7D7D3C54BC3D6D461C2C9D3D3404040404040404040F8F6F660F7F1F260F7F7F5F34040E4E2C1F1F1F8E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C340F6F0F4F8F4F0F6F0F4F0F3F7F0F1F3F3F0F1F2F9F5F0F0F1F9F3C8D2D8C9E6C5E8E5C9E2C5F7E4D2E3D8D2F8E8D1F5C3F0F0F2F6F0F0F0F4F1F0F0F0F0F0F6F0F0F8F4F0F9F5F0F1F44040404040F0F0F9D4C2D2C3C7F4F6F2C6F1F0F1F0F0F1F0F9F5F0F0F1F0F1F8D6D5C540C1D7D7D3C540D7C1D9D240E6C1E8F0F0F2F0F0F3C3C140F0F0F3F0F1F3C1D7D7D3C54BC3D6D440C2C9D3F0F0F4F0F1F0F8F6F6F7F1F2F7F7F5F3F0F0F7F0F2F1F8F4F2F8F0F5F8F2F24040404040404040404040E8";

    private final ParserFactory parserFactory;
    private final MapperFactory mapperFactory;
    private final FieldLogicFactory fieldLogicFactory;
    private String itemSeleccionado = "peer02";
    private InputTextPane inputPane;
    private OutputTextPane outputPane;
    private TreeOutputPane treePane;

    public ParseViewerPane(BeanProviderInstance beans) {
        this.parserFactory = beans.parserFactory();
        this.mapperFactory = beans.mapperFactory();
        this.fieldLogicFactory = beans.fieldLogicFactory();
        initComponents();
        buildLayout();
        setupHandlers();
    }

    private void initComponents() {
        ComboBox<InputTextPane.ComboItem> comboBox = new ComboBox<>();
        comboBox.getItems().addAll(
                new InputTextPane.ComboItem("peer02", "Mastercard"),
                new InputTextPane.ComboItem("peer01", "Visa"));
        comboBox.getSelectionModel().selectFirst();

        inputPane = new InputTextPane("input", "Parsear", "Limpiar", comboBox);
        inputPane.getTextArea().setText(TRAMA_MASTERCARD);
        outputPane = new OutputTextPane("output", "Copiar");
        treePane = new TreeOutputPane("Estructura del mensaje");

        comboBox.setOnAction(e -> {
            InputTextPane.ComboItem item = comboBox.getValue();
            if (item != null) {
                itemSeleccionado = item.getId();
                if ("peer01".equalsIgnoreCase(itemSeleccionado)) {
                    inputPane.getTextArea().setText(TRAMA_VISA);
                } else {
                    inputPane.getTextArea().setText(TRAMA_MASTERCARD);
                }
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
        setupTreeClickHandler(treePane);
    }

    private void parseMessage() {
        try {
            String inputMessage = inputPane.getTextArea().getText().trim();
            if (inputMessage.isEmpty()) {
                FXUtils.showInfoAlert("Aviso", "Por favor ingrese un mensaje para parsear");
                return;
            }

            ISO8583DelegateParser delegateParser = parserFactory.getDelegateParser(itemSeleccionado);
            ISO20022DelegateMapper delegateMapper = mapperFactory.getDelegateMapper(itemSeleccionado);
            NetworkDelegateFieldLogic delegateFieldLogic = fieldLogicFactory.getDelegateFieldLogic(itemSeleccionado);
            Map<String, String> mapValues = delegateParser.parser(inputMessage);

            Map<String, String> mapValuesTree = new HashMap<>(mapValues);
            if (mapValues.containsKey("additionalDataRetailer")) {
                mapValuesTree.put("additionalDataRetailer",
                        ISOUtil.ebcdicToString(mapValuesTree.get("additionalDataRetailer")));
            }

            ParseResult result = FXParseGUI.process(mapValuesTree);
            ISO8583 iso8583 = ISO8583Builder.buildISO8583(inputMessage, mapValues);
            Map<String, String> subFields = delegateFieldLogic.parseSubfields(iso8583);
            ISO20022 iso20022 = delegateMapper.mapper(iso8583, subFields);

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
