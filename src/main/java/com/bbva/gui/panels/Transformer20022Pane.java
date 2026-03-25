package com.bbva.gui.panels;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.gui.commons.ISO8583Processor;
import com.bbva.gui.components.InputTextPane;
import com.bbva.gui.components.OutputTextPane;
import com.bbva.gui.components.TreeOutputPane;
import com.bbva.gui.dto.ParseResult;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.FXParseGUI;
import com.bbva.gui.utils.FXUtils;
import com.bbva.orchestrator.core.exception.ParserFieldsException;
import com.bbva.orchestrator.core.mapper.factory.ISO20022DelegateMapper;
import com.bbva.orchestrator.core.mapper.factory.MapperFactory;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Transformer20022Pane extends AbstractBasePane {

    private static final Logger LOGGER = LoggerFactory.getLogger(Transformer20022Pane.class);
    private static final String SAMPLE_ISO20022 = "{\"iccRelatedData\":\"\",\"networkName\":\"PEER02\",\"traceData\":[{\"value\":\"MDS6KC4PF\",\"key\":\"posAdditionalData\"},{\"value\":\"\",\"key\":\"header\"},{\"value\":\"a960cdbf-ecc6-4904-946a-6a5cac57f907\",\"key\":\"PAYMENT_ID\"}],\"messageFunction\":\"FAUQ\",\"monitoring\":{\"startDateMs\":\"1765560510774\",\"endDateMs\":\"1765560513369\",\"binCode\":\"519348\",\"binDescription\":\"MASTERCARD FPF\",\"merchantNameAceptor\":\"Plaza Vea\",\"merchantCategoryDescription\":\"TIENDAS DE COMESTIBLES Y SUPERMERCADOS\",\"transactionStatus\":\"Pending\",\"channelFilter\":\"ECOMMER\",\"operationFilter\":\"PURCHASE\",\"transactionTypeDescription\":\"COMPRAS\",\"countryDate\":\"2025-12-12T12:28:30.774-05:00\",\"isNextGen\":false},\"socketPort\":\"7003\",\"addendumData\":{\"additionalData\":[{\"value\":\"0120\",\"key\":\"UNSP\"}]},\"environment\":{\"card\":{\"pan\":\"519348******9702\"},\"acquirer\":{\"id\":\"007963\",\"country\":\"604\"}},\"isSimulation\":false,\"transaction\":{\"transactionId\":{\"transactionReference\":\"72f054d9-a465-395c-9650-771365cc528\",\"systemTraceAuditNumber\":\"061286\",\"retrievalReferenceNumber\":\"534625806129\"},\"transactionType\":\"00\",\"transactionAmounts\":{\"transactionAmount\":{\"amount\":53.6,\"currency\":\"PEN\"}}},\"processingResult\":{\"approvalCode\":\"\",\"resultData\":{\"result\":\"PRCS\"}}}";

    private final ParserFactory parserFactory;
    private final MapperFactory mapperFactory;
    private InputTextPane inputPane;
    private OutputTextPane outputPane;
    private TreeOutputPane treePane;

    public Transformer20022Pane(BeanProviderInstance beans) {
        this.parserFactory = beans.parserFactory();
        this.mapperFactory = beans.mapperFactory();
        initComponents();
        buildLayout();
        setupHandlers();
    }

    private void initComponents() {
        inputPane = new InputTextPane("ISO20022 de entrada", "Unparser", "Limpiar");
        inputPane.getTextArea().setText(prettyPrint(SAMPLE_ISO20022));
        outputPane = new OutputTextPane("output", "Copiar");
        treePane = new TreeOutputPane("Estructura del mensaje");
    }

    private void buildLayout() {
        setCenter(createLayoutWithTree(treePane, inputPane, outputPane));
    }

    private void setupHandlers() {
        registerPrimaryButton(inputPane, this::convertMessage);
        registerSecondaryButton(inputPane, () -> clearFields(inputPane, outputPane, treePane));
        setupCopyToClipboard(outputPane);
        setupTreeClickHandler(treePane);
    }

    private void convertMessage() {
        try {
            String jsonString = inputPane.getTextArea().getText().trim();
            if (!jsonString.startsWith("{")) {
                FXUtils.showInfoAlert("WARNING", "Por favor ingrese una estructura json correcta en formato ISO20022");
                return;
            }

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
            ISO20022 iso20022 = objectMapper.readValue(jsonString, ISO20022.class);

            ISO8583DelegateParser delegateParser = parserFactory.getDelegateParser(iso20022.getNetworkName());
            ISO20022DelegateMapper delegateMapper = mapperFactory.getDelegateMapper(iso20022.getNetworkName());
            Map<String, String> fieldsValues = delegateMapper.unMapper(iso20022);
            String trama = delegateParser.unParserPlainText(fieldsValues);
            LOGGER.info("Trama generada: [{}]", trama);
            outputPane.getTextArea().setText(trama);

            Map<String, String> mapValues;
            if (iso20022.getNetworkName().equalsIgnoreCase("PEER02")) {
                mapValues = ISO8583Processor.createMapFieldsISO8583Mastercard(trama);
            } else {
                mapValues = ISO8583Processor.createMapFieldsISO8583Visa(trama);
            }
            ParseResult result = FXParseGUI.process(mapValues);
            FXParseGUI.updateTreeView(treePane.getTreeView(), result);

        } catch (ParserFieldsException ex) {
            FXUtils.showErrorAlert("Error al parsear el mensaje: " + ex.getMessage());
            outputPane.getTextArea().setText("Error: " + ex.getMessage());
        } catch (Exception ex) {
            FXUtils.showErrorAlert("Error al parsear el mensaje: " + ex.getMessage());
            outputPane.getTextArea().setText("Error: " + ex.getMessage());
        }
    }

    private String prettyPrint(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Object jsonObject = objectMapper.readValue(json, Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (Exception e) {
            LOGGER.error("Error al formatear JSON: {}", e.getMessage());
            return json;
        }
    }
}
