package com.bbva.gui.panels;

import com.bbva.gui.commons.ISO8583Processor;
import com.bbva.gui.components.InputTextPane;
import com.bbva.gui.components.OutputTextPane;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.FXUtils;
import com.bbva.orchestrator.core.exception.ParserFieldsException;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ConverterTramaTextPlainPane extends AbstractBasePane {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterTramaTextPlainPane.class);
    private static final String SAMPLE_MESSAGE = "F0F1F0F0FEFF640188E1E10A0000000000000040F1F6F5F5F3F6F5F0F9F9F9F9F9F9F9F9F9F9F0F0F0F0F0F0F0F0F0F0F0F0F0F2F2F9F9F0F0F0F0F0F0F0F0F0F6F3F5F6F0F0F0F0F0F0F0F2F2F9F9F0F0F6F1F6F0F7F2F7F2F4F7F2F7F6F4F6F8F0F6F1F0F0F0F0F0F0F8F9F8F7F1F6F0F3F2F7F2F4F0F6F1F6F2F9F0F5F0F6F1F6F0F6F1F5F5F8F1F8F8F4F0F1F0F0F0F6F0F0F3F2F8F6F0F6F0F0F3F2F8F6F5F1F6F7F5F4F8F9F8F7F1F6F0F0F4F0F0F2F1F6F4F0F0F2F1F6F0F0F0F1F0F8F7F7F8C1D7D7D3C54BC3D6D461C2C9D3D3404040404040404040F8F6F660F7F1F260F7F7F5F34040E4E2C1F1F1F8E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C340F6F0F4F8F4F0F6F0F4F0F3F7F0F1F3F3F0F1F2F9F5F0F0F1F9F3C8D2D8C9E6C5E8E5C9E2C5F7E4D2E3D8D2F8E8D1F5C3F0F0F2F6F0F0F0F4F1F0F0F0F0F0F6F0F0F8F4F0F9F5F0F1F44040404040F0F0F9D4C2D2C3C7F4F6F2C6F1F0F1F0F0F1F0F9F5F0F0F1F0F1F8D6D5C540C1D7D7D3C540D7C1D9D240E6C1E8F0F0F2F0F0F3C3C140F0F0F3F0F1F3C1D7D7D3C54BC3D6D440C2C9D3F0F0F4F0F1F0F8F6F6F7F1F2F7F7F5F3F0F0F7F0F2F1F8F4F2F8F0F5F8F2F24040404040404040404040E8";

    private final ISO8583DelegateParser delegateParser;
    private InputTextPane inputPane;
    private OutputTextPane outputPane;

    public ConverterTramaTextPlainPane(BeanProviderInstance beans) {
        this.delegateParser = beans.parserFactory().getDelegateParser("PEER02");
        initComponents();
        buildLayout();
        setupHandlers();
    }

    private void initComponents() {
        inputPane = new InputTextPane("Input", "Procesar", "Limpiar");
        inputPane.getTextArea().setText(SAMPLE_MESSAGE);
        outputPane = new OutputTextPane("Output", "Copiar");
    }

    private void buildLayout() {
        setCenter(createSimpleLayout(inputPane, outputPane));
    }

    private void setupHandlers() {
        registerPrimaryButton(inputPane, this::convertMessage);
        registerSecondaryButton(inputPane, () -> {
            inputPane.getTextArea().setText("");
            outputPane.getTextArea().setText("");
        });
        setupCopyToClipboard(outputPane);
    }

    private void convertMessage() {
        Map<String, String> currentMappedFieldsByDescription = new HashMap<>();
        try {
            String inputMessage = inputPane.getTextArea().getText().trim();
            if (inputMessage.isEmpty()) {
                FXUtils.showInfoAlert("Aviso", "Por favor ingrese un mensaje para parsear");
                return;
            }

            if (inputMessage.startsWith("F0")) {
                LOGGER.info("Mensaje en formato EBCDIC");
                currentMappedFieldsByDescription = delegateParser.parser(inputMessage);
            } else {
                currentMappedFieldsByDescription = ISO8583Processor.createMapFieldsISO8583Mastercard(inputMessage);
            }

            String trama = delegateParser.unParser(currentMappedFieldsByDescription);
            LOGGER.info("Trama generada: [{}]", trama);
            outputPane.getTextArea().setText(trama);

        } catch (ParserFieldsException ex) {
            FXUtils.showErrorAlert("Error al parsear el mensaje: " + ex.getMessage());
            outputPane.getTextArea().setText("Error: " + ex.getMessage());
        }
    }
}
