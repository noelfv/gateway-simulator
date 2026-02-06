package com.bbva.gui.components.panels;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.gui.commons.ISO8583Processor;
import com.bbva.gui.components.PanelDoggy;
import com.bbva.gui.dto.*;
import com.bbva.gui.spring.ApplicationContextProvider;
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
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import lombok.Setter;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class ConverterIso20022ViewerPanel extends JPanel {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConverterIso20022ViewerPanel.class);
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JButton convertirTramaButton;
    private JButton limpiarButton;
    private JButton copiarRespuestaButton;
    private JTree resultTree;
    private DefaultTreeModel treeModel;
    final String SAMPLE_ISO20022 = "{\"networkName\":\"PEER02\",\"messageFunction\":\"AUTQ\",\"socketPort\":\"6034\",\"exchangeIdentification\":null,\"protocolVersion\":null,\"traceData\":[{\"key\":\"posAdditionalData\",\"value\":\"MBKCG462F\"},{\"key\":\"header\",\"value\":\"\"},{\"key\":\"PAYMENT_ID\",\"value\":\"traceID\"}],\"transaction\":{\"transactionId\":{\"transactionReference\":\"898716003286516754898716004002160616072724\",\"transmissionDateTime\":\"2025-06-16T07:27:24.000Z\",\"systemTraceAuditNumber\":\"898716\",\"localDate\":\"0616\",\"localTime\":\"032724\",\"localDateTime\":null,\"acquirerReferenceData\":null,\"retrievalReferenceNumber\":\"516754898716\",\"originalDataElements\":null,\"cardIssuerReferenceData\":null},\"transactionType\":\"00\",\"accountFrom\":{\"accountId\":\"\",\"accountType\":\"00\"},\"accountTo\":{\"accountId\":\"\",\"accountType\":\"00\"},\"transactionAmounts\":{\"transactionAmount\":{\"amount\":229.9,\"currency\":\"604\"},\"reconciliationAmount\":{\"amount\":63.56,\"currency\":\"840\",\"effectiveExchangeRate\":\"72764680\",\"conversionDate\":null},\"cardholderBillingAmount\":{\"amount\":229.9,\"currency\":\"604\",\"effectiveExchangeRate\":\"61000000\"},\"originalTransactionAmounts\":null},\"transactionAttribute\":0,\"messageReason\":\"\",\"originalAdditionalFee\":null,\"additionalFee\":[{\"feeAmount\":{\"amount\":null,\"currency\":null},\"feeReconciliationAmount\":{\"amount\":null},\"key\":null,\"otherType\":null}],\"additionalAmount\":[{\"key\":\"additionalAmounts\",\"amount\":{\"amount\":null,\"currency\":null},\"description\":null}],\"additionalData\":[{\"key\":\"opera\",\"value\":\"000000\"},{\"key\":\"redemptionPoints\",\"value\":\"\"},{\"key\":\"additionalResponseData\",\"value\":\"\"}],\"alternateMessageReason\":null},\"environment\":{\"card\":{\"pan\":\"5536509999999999\",\"effectiveDate\":null,\"expiryDate\":\"2029-05\",\"cardSequenceNumber\":\"\"},\"terminal\":{\"capabilities\":{\"approvalCodeLength\":null},\"terminalId\":{\"id\":\"00400216\",\"assigner\":\"\",\"country\":null},\"key\":\"OTHN\",\"otherType\":null,\"geographicLocation\":null,\"poiComponent\":null},\"acquirer\":{\"id\":\"003286\",\"acquirerInstitution\":null,\"country\":\"840\",\"additionalId\":{\"key\":\"postalCode\",\"value\":\"\"}},\"sender\":{\"id\":\"003286\",\"additionalId\":{\"key\":\"additionalDataRetailer\",\"value\":\"E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C340\"},\"localData\":null},\"acceptor\":{\"id\":\"400216000108778\",\"nameAndLocation\":\"APPLE.COM/BILL         866-712-7753  USA\",\"localData\":null,\"additionalData\":null},\"issuer\":{\"assigner\":\"000410000060084095014     \",\"additionalIdentification\":null},\"customerDevice\":null},\"context\":{\"transactionContext\":{\"settlementService\":{\"settlementServiceDates\":{\"settlementDate\":\"0616\"},\"additionalSettlementInformation\":null},\"captureDate\":\"\",\"merchantCategoryCode\":\"5818\",\"merchantCategorySpecificData\":null,\"reconciliation\":{\"date\":\"\"},\"additionalData\":[{\"key\":\"OPERATION_TYPE\",\"value\":\"OP_0100_00\"},{\"key\":\"CHANNEL\",\"value\":\"POS\"}]},\"verification\":[{\"key\":null,\"verificationInformation\":[{\"key\":null,\"value\":{\"pinData\":{\"encryptedPINBlock\":\"\"},\"textValue\":null}}],\"verificationResult\":null}],\"pointOfServiceContext\":{\"cardDataEntryMode\":\"100\",\"ecommerceIndicator\":false},\"saleContext\":{\"additionalData\":[{\"key\":\"campaignData\",\"value\":\"\"}]}},\"processingResult\":null,\"securityTrailer\":null,\"iccRelatedData\":null,\"protectedData\":[],\"supplementaryData\":[{\"placeAndName\":\"dateConversion\",\"envelope\":\"0615\",\"key\":null},{\"placeAndName\":\"paymentAccountData\",\"envelope\":\"01330129500193HKQIWEYVISE7UKTQK8YJ5C0\",\"key\":null},{\"placeAndName\":\"additionalRecordData\",\"envelope\":\"001095001018ONE APPLE PARK WAY002003CA 003013APPLE.COM BIL0040108667127753007021842805822           Y\",\"key\":null}],\"addendumData\":{\"additionalData\":[{\"key\":\"MSGTYPE\",\"value\":\"0100\"}],\"invoice\":null,\"sale\":null}}";
    @Setter
    private JInternalFrame parentFrame;

    private  ParserFactory parserFactory;
    private  MapperFactory mapperFactory;
    private ISO8583DelegateParser delegateParser;
    private ISO20022DelegateMapper delegateMapper;

    public ConverterIso20022ViewerPanel() {
        initializeComponents();
        createPanelsLayout();
        setupEventHandlers();

        //Poner el foco en el JTextArea de entrada
        SwingUtilities.invokeLater(() -> {
            inputTextArea.requestFocusInWindow();
        });
    }


    private String jsonPrettyPrint(String json) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Object jsonObject = objectMapper.readValue(SAMPLE_ISO20022, Object.class);
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (Exception e) {
            LOGGER.error("Error al formatear JSON: {}", e.getMessage());
            return json;
        }
    }

   public void initializeComponents() {

       setLayout(new BorderLayout());

       inputTextArea = new JTextArea(8, 60);
       inputTextArea.setLineWrap(true);
       inputTextArea.setWrapStyleWord(true);
       inputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
       //inputTextArea.setBackground(Color.LIGHT_GRAY);
       inputTextArea.setText(jsonPrettyPrint(SAMPLE_ISO20022));

       outputTextArea = new JTextArea(12, 60);
       outputTextArea.setLineWrap(true);
       outputTextArea.setWrapStyleWord(true);
       outputTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

       convertirTramaButton = new JButton("Unparser");
       limpiarButton = new JButton("Limpiar");
       copiarRespuestaButton = new JButton("Copiar respuesta");

       DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mensaje Parseado");
       treeModel = new DefaultTreeModel(root);
       resultTree = new JTree(treeModel);
       resultTree.setRootVisible(true);
    }

    private void  createPanelsLayout() {
        MyDoggyToolWindowManager toolWindowManager= PanelDoggy.setupStructureMyDoggy(createMainPanel(), resultTree, createOutputPanel());
        add(toolWindowManager, BorderLayout.CENTER);
    }


    private JPanel createMainPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Panel de entrada
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("ISO20022 de entrada"));
        inputPanel.add(new JScrollPane(inputTextArea), BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(convertirTramaButton);
        buttonPanel.add(limpiarButton);

        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Establecer tamaño preferido para el panel
        panel.setPreferredSize(new Dimension(800, 200));

        outputTextArea.setPreferredSize(new Dimension(780, 180));
        panel.add(new JScrollPane(outputTextArea), BorderLayout.CENTER);

        //JPanel compareButtonPanel = new JPanel(new FlowLayout());
        //panel.add(compareButtonPanel, BorderLayout.SOUTH);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.add(copiarRespuestaButton, BorderLayout.EAST);
        panel.add(buttonPanel, BorderLayout.SOUTH);


        return panel;
    }

    private void setupEventHandlers() {
        convertirTramaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                convertMessage();
            }
        });

        limpiarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputTextArea.setText("");
                outputTextArea.setText("");
                // Limpiar el JTree
                DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mensaje Parseado");
                treeModel.setRoot(root);
                treeModel.reload();
            }
        });

        copiarRespuestaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection selection = new StringSelection(outputTextArea.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
                outputTextArea.requestFocusInWindow();
                outputTextArea.selectAll();


                // Mostrar tooltip temporal
                copiarRespuestaButton.setToolTipText("¡Texto copiado!");
                ToolTipManager.sharedInstance().mouseMoved(
                        new java.awt.event.MouseEvent(
                                copiarRespuestaButton, 0, 0, 0,
                                0, 0, // X-Y
                                0, false));
                // Quitar el tooltip después de 1 segundo
                Timer timer = new Timer(1000, evt -> copiarRespuestaButton.setToolTipText(null));
                timer.setRepeats(false);
                timer.start();

            }
        });
    }

    private void convertMessage() {

        ParseResult result;

        try {
            String jsonString = inputTextArea.getText().trim();
            if (!jsonString.startsWith("{")) {
                JOptionPane.showMessageDialog(parentFrame, "Por favor ingrese una estructura json correcta en formato ISO20022",
                        "WARNING", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Convertir de String JSON a objeto ISO20022
            ObjectMapper objectMapper = new ObjectMapper();
            // 1. Registrar el módulo para nombres de parámetros de constructor
            objectMapper.registerModule(new ParameterNamesModule());
            // 2. Añadir el Mix-in para enseñarle a Jackson cómo construir ISO20022
            //objectMapper.addMixIn(ISO20022.class, ISO20022Mixin.class);
            // 3. AÑADIR EL MIX-IN PARA CardDTO
            // Esto le enseña a Jackson cómo construir el objeto CardDTO.
            objectMapper.addMixIn(TransactionDTO.class, TransactionDTOMixin.class);
            objectMapper.addMixIn(CardDTO.class, CardDTOMixin.class);
            objectMapper.addMixIn(SettlementServiceDatesDTO.class, SettlementServiceDatesDTOMixin.class);
            objectMapper.addMixIn(FeeReconciliationAmountDTO.class, FeeReconciliationAmountDTOMixin.class);
            // 2. Usar Jackson2ObjectMapperBuilder para crear el ObjectMapper
            //ObjectMapper objectMapper = Jackson2ObjectMapperBuilder.json().build();
            //objectMapper.registerModule(new ParameterNamesModule());
            //objectMapper.addMixIn(ISO20022.class, ISO20022Mixin.class);

            // Configurar ObjectMapper de forma más permisiva
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
           // objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
           // objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
           // objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
           // objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

            objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

            ISO20022 inputObject = objectMapper.readValue(jsonString, ISO20022.class);

            delegateParser = ApplicationContextProvider.getBean(ParserFactory.class).getDelegateParser("PEER02");
            delegateMapper = ApplicationContextProvider.getBean(MapperFactory.class).getDelegateMapper("PEER02");
            System.out.println("Delegate Parser: " + delegateParser.getClass().getName());
            System.out.println("DelegateMapper Mapper: " + delegateMapper.getClass().getName());
            Map<String, String> fieldsValues  = delegateMapper.unMapper(inputObject);
            String trama=delegateParser.unParserPlainText(fieldsValues);
            LOGGER.info("Trama generada: [{}]", trama);
            outputTextArea.setText(trama );

            Map<String,String> mapValues=ISO8583Processor.createMapFieldsISO8583(trama);

            result = ParseGUI.process(mapValues);
            ParseGUI.updateTreeView(treeModel, resultTree, result);

        } catch (ParserFieldsException ex) {
            UtilGUI.showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
            /*result = ParseGUI.process(ex.getValuesMap());
            ParseGUI.updateTreeView(treeModel, resultTree, result);*/
            outputTextArea.setText("Error: " + ex.getMessage());
        }catch (Exception ex) {
            UtilGUI.showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
            outputTextArea.setText("Error: " + ex.getMessage());
        }
    }

}