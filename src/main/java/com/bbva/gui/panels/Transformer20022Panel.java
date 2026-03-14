package com.bbva.gui.panels;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.gui.commons.ISO8583Processor;
import com.bbva.gui.components.InputTextPanel;
import com.bbva.gui.components.JtreeOutputPanel;
import com.bbva.gui.components.OutputTextPanel;
import com.bbva.gui.components.PanelDoggy;
import com.bbva.gui.dto.ParseResult;
import com.bbva.gui.spring.BeanProviderInstance;
import com.bbva.gui.utils.ParseGUI;
import com.bbva.gui.utils.UtilGUI;
import com.bbva.orchestrator.core.exception.ParserFieldsException;
import com.bbva.orchestrator.core.mapper.factory.ISO20022DelegateMapper;
import com.bbva.orchestrator.core.mapper.factory.MapperFactory;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class Transformer20022Panel extends AbstractBasePanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(Transformer20022Panel.class);
    private static final String SAMPLE_ISO20022 = "{\"iccRelatedData\":\"\",\"networkName\":\"PEER02\",\"traceData\":[{\"value\":\"MDS6KC4PF\",\"key\":\"posAdditionalData\"},{\"value\":\"\",\"key\":\"header\"},{\"value\":\"a960cdbf-ecc6-4904-946a-6a5cac57f907\",\"key\":\"PAYMENT_ID\"}],\"messageFunction\":\"FAUQ\",\"monitoring\":{\"startDateMs\":\"1765560510774\",\"endDateMs\":\"1765560513369\",\"binCode\":\"519348\",\"binDescription\":\"MASTERCARD FPF\",\"merchantNameAceptor\":\"Plaza Vea\",\"merchantCategoryDescription\":\"TIENDAS DE COMESTIBLES Y SUPERMERCADOS\",\"transactionStatus\":\"Pending\",\"channelFilter\":\"ECOMMER\",\"operationFilter\":\"PURCHASE\",\"transactionTypeDescription\":\"COMPRAS\",\"countryDate\":\"2025-12-12T12:28:30.774-05:00\",\"isNextGen\":false},\"socketPort\":\"7003\",\"addendumData\":{\"additionalData\":[{\"value\":\"0120\",\"key\":\"UNSP\"}]},\"environment\":{\"card\":{\"pan\":\"519348******9702\"},\"acquirer\":{\"id\":\"007963\",\"country\":\"604\"}},\"isSimulation\":false,\"transaction\":{\"transactionId\":{\"transactionReference\":\"72f054d9-a465-395c-9650-771365cc528\",\"systemTraceAuditNumber\":\"061286\",\"retrievalReferenceNumber\":\"534625806129\"},\"transactionType\":\"00\",\"transactionAmounts\":{\"transactionAmount\":{\"amount\":53.6,\"currency\":\"PEN\"}}},\"processingResult\":{\"approvalCode\":\"\",\"resultData\":{\"result\":\"PRCS\"}}}";

    @Setter
    private JInternalFrame parentFrame;
    private final ParserFactory parserFactory;
    private final MapperFactory mapperFactory;
    private InputTextPanel inputTextPanel;
    private OutputTextPanel outputTextPanel;
    private JtreeOutputPanel treeStructurePanel;

    public Transformer20022Panel(BeanProviderInstance beanProviderInstance) {
        this.parserFactory = beanProviderInstance.parserFactory();
        this.mapperFactory = beanProviderInstance.mapperFactory();
        initializeComponents();
        createPanelsLayout();
        setupEventHandlers();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        inputTextPanel = new InputTextPanel("ISO20022 de entrada", "Unparser", "Limpiar");
        inputTextPanel.getTextArea().setText(prettyPrint(SAMPLE_ISO20022));
        outputTextPanel = new OutputTextPanel("output", "Copiar");
        treeStructurePanel = new JtreeOutputPanel("Estructura del mensaje");
    }

    private void createPanelsLayout() {
        MyDoggyToolWindowManager toolWindowManager = PanelDoggy.setupStructureMyDoggy(treeStructurePanel, inputTextPanel, outputTextPanel);
        add(toolWindowManager, BorderLayout.CENTER);
    }

    private void setupEventHandlers() {
        registerPrimaryButton(inputTextPanel, this::convertMessage);
        registerSecondaryButton(inputTextPanel, () -> clearFields(inputTextPanel, outputTextPanel, treeStructurePanel, "Mensaje Parseado"));
        setupCopyToClipboard(outputTextPanel);
        setupTreeClickHandler(treeStructurePanel);
    }

    private void convertMessage() {
        try {
            String jsonString = inputTextPanel.getTextArea().getText().trim();
            if (!jsonString.startsWith("{")) {
                JOptionPane.showMessageDialog(parentFrame,
                        "Por favor ingrese una estructura json correcta en formato ISO20022",
                        "WARNING", JOptionPane.WARNING_MESSAGE);
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
            outputTextPanel.getTextArea().setText(trama);

            Map<String, String> mapValues;
            if (iso20022.getNetworkName().equalsIgnoreCase("PEER02")) {
                mapValues = ISO8583Processor.createMapFieldsISO8583Mastercard(trama);
            } else {
                mapValues = ISO8583Processor.createMapFieldsISO8583Visa(trama);
            }
            ParseResult result = ParseGUI.process(mapValues);
            ParseGUI.updateTreeView(treeStructurePanel.getTreeModel(), treeStructurePanel.getResultTree(), result);

        } catch (ParserFieldsException ex) {
            UtilGUI.showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
            outputTextPanel.getTextArea().setText("Error: " + ex.getMessage());
        } catch (Exception ex) {
            UtilGUI.showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
            outputTextPanel.getTextArea().setText("Error: " + ex.getMessage());
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
