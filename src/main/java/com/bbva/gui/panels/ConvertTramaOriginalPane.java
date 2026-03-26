package com.bbva.gui.panels;

import com.bbva.gui.commons.ISO8583Processor;
import com.bbva.gui.components.InputTextPane;
import com.bbva.gui.components.OutputTextPane;
import com.bbva.gui.components.TreeOutputPane;
import com.bbva.gui.dto.ParseResult;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.FXParseGUI;
import com.bbva.gui.utils.FXUtils;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import com.bbva.orchestrator.core.utils.ISOUtil;
import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ConvertTramaOriginalPane extends AbstractBasePane {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertTramaOriginalPane.class);
    private static final String SAMPLE_MASTERCARD = "0100FEFF660128E1E30A0000000000000010165193481234564714000000000000001811000000000539000000001811022012033872976256610000001687000703380220300702200219554160407100106007963370000000000000000000000000000000000000605112168700FD8068848742323        IZI*ESTACION DE SERVIC SAN JUAN DE L PER240D9F2F3F0F2F0F0F3F7F3F4F0F1F1F1F0F0F0F0F0F2F5F3F3F5F7F0F3F1F5F0F0F0F0F0F0F0F0F8F7F4F2F3F2F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F1C1D8E2F1F9F9C1D8C6F1F1F1F7F5F3F2F0F1F0F3F0F8F6F0F2F0F2F5F6F0F3F0F3F0F8F6F0F4F0F2F5F6F0F5F0F2F0F0F7F1F0F4F1F8C3406048406042485F2A020604820219808407A0000000041010950500000080019A032602209C01009F02060000000018119F03060000000000009F10120110A04001220000000000000000000000FF9F1A0206049F260827BB155934A62ECF9F2701809F3303E008C89F34031F03029F3501229F360200BC9F3704622DCBAA9F53015203701330129500132OWBA1Z3TJW13I6GFD31U5OJ0190000000000300604L36009MDS4A32IH0011";
    private static final String SAMPLE_VISA = "1601020198880403000000021000489D1029020000000120F66464814AF0A016000000000100002410123456789012345600000000000000250000000000122912110724127491600092240830115812015610005906462921003801000EC002F0F2C101F18605F24BF24BA75600249F1F0CF1F2F54BF1F1F44BF6F14BF9DF1F02F2F79F2001019F2101029F2801079F290100F5F3F4F4F0F7F9F2F2F4F0F8F6F5F9F9F9F9F9F9F9F9F1F0F0F1F9F5F5F8F0F2F0F1404040C1D3D75C948995878388A4819587A896A497899587A4404040F0F2F1F6F1F6F8F6F8F8F84040C3D50FF140404040404040404040404040F2015606040501000040051F40001C000000000003853452665303863900620000F0F9F0F0F0F0F0F0F0F007900000000290200E5700040102E6E35B00048502F0F3496800460110F4F3F8F4F6F6F5F7F5F0F0F8F3F4F9F30202F1F0030BF4F0F0F6F0F4F0F6F6F6F20604F3F0F1F10702F0F1800100860604001005224282010083010084010085030000001C00800000000000000002010447146900000009C41563457500000000";

    private final ParserFactory parserFactory;
    private InputTextPane inputPane;
    private OutputTextPane outputPane;
    private TreeOutputPane treePane;
    private ComboBox<InputTextPane.ComboItem> comboBoxRedes;
    private ComboBox<InputTextPane.ComboItem> comboBoxFormato;
    private String redSeleccionada    = "peer02";
    private String formatoSeleccionado = "hex";

    public ConvertTramaOriginalPane(BeanProviderInstance beans) {
        this.parserFactory = beans.parserFactory();
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

        comboBoxFormato = new ComboBox<>();
        comboBoxFormato.getItems().addAll(
                new InputTextPane.ComboItem("hex",   "Hexadecimal"),
                new InputTextPane.ComboItem("plain", "Texto plano"));
        comboBoxFormato.getSelectionModel().selectFirst();

        inputPane = new InputTextPane("input", "Parsear", "Limpiar",
                comboBoxRedes,
                comboBoxFormato, "Formato de salida:",
                InputTextPane.ComboDirection.LEFT);
        outputPane = new OutputTextPane("output", "Copiar");
        treePane   = new TreeOutputPane("Estructura del mensaje");

        Platform.runLater(() -> inputPane.getTextArea().setText(SAMPLE_MASTERCARD));

        comboBoxRedes.setOnAction(e -> {
            InputTextPane.ComboItem item = comboBoxRedes.getValue();
            if (item != null) {
                redSeleccionada = item.getId();
                inputPane.getTextArea().setText(
                        "peer01".equalsIgnoreCase(redSeleccionada) ? SAMPLE_VISA : SAMPLE_MASTERCARD);
            }
        });

        comboBoxFormato.setOnAction(e -> {
            InputTextPane.ComboItem item = comboBoxFormato.getValue();
            if (item != null) {
                formatoSeleccionado = item.getId();
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

            Map<String, String> mapValues = "peer01".equalsIgnoreCase(redSeleccionada)
                    ? ISO8583Processor.createMapFieldsISO8583Visa(inputMessage)
                    : ISO8583Processor.createMapFieldsISO8583Mastercard(inputMessage);

            Map<String, String> mapValuesTree = new HashMap<>(mapValues);
            if (mapValues.containsKey("additionalDataRetailer")) {
                String campo48 = mapValuesTree.get("additionalDataRetailer");
                LOGGER.info("Campo 48 en crudo: [{}]", campo48);
                mapValuesTree.put("additionalDataRetailer", ISOUtil.ebcdicToString(campo48));
            }

            ParseResult result = FXParseGUI.process(mapValuesTree);
            FXParseGUI.updateTreeView(treePane.getTreeView(), result);

            ISO8583DelegateParser delegateParser = parserFactory.getDelegateParser(redSeleccionada);
            String trama = "plain".equalsIgnoreCase(formatoSeleccionado)
                    ? delegateParser.unParserPlainText(mapValues)
                    : delegateParser.unParser(mapValues);
            outputPane.getTextArea().setText(trama);

        } catch (Exception ex) {
            FXUtils.showErrorAlert("Error al parsear el mensaje: " + ex.getMessage());
            outputPane.getTextArea().setText("Error: " + ex.getMessage());
        }
    }
}
