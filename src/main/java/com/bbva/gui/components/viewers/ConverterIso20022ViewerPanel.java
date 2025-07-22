package com.bbva.gui.components.viewers;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.gateway.utils.LogsTraces;
import com.bbva.gui.commons.ISO8583Processor;
import com.bbva.gui.components.PanelDoggy;
import com.bbva.gui.dto.ParseResult;
import com.bbva.gui.utils.ParseGUI;
import com.bbva.gui.utils.UtilGUI;
import com.bbva.orchestrator.network.mastercard.processor.ISOStringConverterMastercard;
import com.bbva.orchestrator.parser.common.ISO8583SubFieldsParser;
import com.bbva.orchestrator.parser.exception.ParserLocalException;
import com.bbva.orchestrator.parser.factory.MessageParser;
import com.bbva.orchestrator.parser.factory.impl.MCMessageParserImpl;
import com.bbva.orchestrator.parser.iso20022.ISO20022To8583Mapper;
import com.bbva.orchestrator.parser.iso20022.ISO8583To20022Mapper;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.iso8583.ISO8583Builder;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Setter;
import org.noos.xing.mydoggy.plaf.MyDoggyToolWindowManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    final String SAMPLE_ISO20022 = "{\"networkName\":\"PEER02\",\"messageFunction\":\"0100\",\"socketPort\":\"6034\",\"exchangeIdentification\":null,\"protocolVersion\":null,\"traceData\":[{\"key\":\"posAdditionalData\",\"value\":\"MBKCG462F\"},{\"key\":\"header\",\"value\":\"\"},{\"key\":\"PAYMENT_ID\",\"value\":\"traceID\"}],\"transaction\":{\"transactionId\":{\"transactionReference\":\"89871600328651675489871600400216MBKCG462F\",\"transmissionDateTime\":\"2025-06-16T07:27:24.000Z\",\"systemTraceAuditNumber\":\"898716\",\"localDate\":\"0616\",\"localTime\":\"032724\",\"localDateTime\":null,\"acquirerReferenceData\":\"Field not mapped\",\"retrievalReferenceNumber\":\"516754898716\",\"originalDataElements\":{\"messageFunction\":\"\",\"transactionReference\":null,\"systemTraceAuditNumber\":\"\",\"localDate\":null,\"localTime\":null,\"localDateTime\":null,\"acquirerId\":\"\",\"transmissionDateTime\":\"\",\"senderIdentification\":\"\",\"retrievalReferenceNumber\":null},\"cardIssuerReferenceData\":null},\"transactionType\":\"00\",\"accountFrom\":{\"accountId\":\"Field not mapped\",\"accountType\":\"00\"},\"accountTo\":{\"accountId\":null,\"accountType\":\"00\"},\"transactionAmounts\":{\"transactionAmount\":{\"amount\":229.9,\"currency\":\"604\"},\"reconciliationAmount\":{\"amount\":63.56,\"currency\":\"840\",\"effectiveExchangeRate\":727646.8,\"conversionDate\":null},\"cardholderBillingAmount\":{\"amount\":229.9,\"currency\":\"604\",\"effectiveExchangeRate\":1.0},\"originalTransactionAmounts\":null},\"transactionAttribute\":0,\"messageReason\":\"\",\"originalAdditionalFee\":null,\"additionalFee\":[{\"feeAmount\":{\"amount\":null,\"currency\":null},\"feeReconciliationAmount\":{\"amount\":null},\"key\":null,\"otherType\":null}],\"additionalAmount\":[{\"key\":\"additionalAmounts\",\"amount\":{\"amount\":null,\"currency\":null},\"description\":null}],\"additionalData\":[{\"key\":\"additionalResponseData\",\"value\":\"\"},{\"key\":\"accountType\",\"value\":null},{\"key\":\"amountType\",\"value\":null},{\"key\":\"currencyCode\",\"value\":null},{\"key\":\"indicator\",\"value\":null}],\"additionalService\":null,\"networkManagementType\":null,\"keyExchangeData\":null,\"detailedRequestedAmount\":null,\"otherTransactionAttribute\":null,\"alternateMessageReason\":null},\"environment\":{\"card\":{\"pan\":\"5536509999999999\",\"effectiveDate\":null,\"expiryDate\":\"2029-05\",\"cardSequenceNumber\":\"\",\"track1\":\"\",\"track2\":{\"textValue\":\"\"},\"track3\":null,\"serviceCode\":\"Field not mapped\",\"additionalCardData\":null},\"terminal\":{\"capabilities\":{\"approvalCodeLength\":null,\"cardCaptureCapable\":null,\"cardReadingCapabilities\":null,\"cardholderVerificationCapabilities\":null,\"cardWritingCapabilities\":null,\"pINLengthCapability\":null},\"terminalId\":{\"id\":\"00400216\",\"assigner\":\"\",\"country\":null},\"key\":\"OTHN\",\"otherType\":null,\"geographicLocation\":null,\"poiComponent\":null},\"acquirer\":{\"id\":\"003286\",\"acquirerInstitution\":null,\"country\":\"840\",\"additionalId\":{\"key\":\"postalCode\",\"value\":\"\"}},\"sender\":{\"id\":\"003286\",\"additionalId\":{\"key\":\"additionalDataRetailer\",\"value\":\"T37150511000009999974207010321022080504M1036105000015618AQV116AQS609AQF116753201038800202140303880040214050200710418C \"},\"localData\":null},\"acceptor\":{\"id\":\"400216000108778\",\"nameAndLocation\":\"APPLE.COM/BILL         866-712-7753  USA\",\"localData\":null,\"additionalData\":null},\"issuer\":{\"assigner\":\"000410000060084095014     \",\"additionalIdentification\":null},\"cardholder\":null,\"receiver\":null,\"atmManagerIdentification\":null,\"token\":null,\"customerDevice\":null},\"context\":{\"transactionContext\":{\"settlementService\":{\"settlementServiceDates\":{\"settlementDate\":\"0616\"},\"additionalSettlementInformation\":null},\"captureDate\":null,\"merchantCategoryCode\":\"5818\",\"merchantCategorySpecificData\":null,\"reconciliation\":{\"date\":\"\"},\"transactionInitiator\":null,\"iccFallbackIndicator\":null,\"magneticStripeFallbackIndicator\":null,\"reSubmissionIndicator\":null,\"additionalData\":[{\"key\":\"OPERATION_TYPE\",\"value\":\"00\"},{\"key\":\"CHANNEL\",\"value\":\"ECOMMER\"}]},\"verification\":[{\"key\":null,\"verificationInformation\":[{\"key\":null,\"value\":{\"pinData\":{\"control\":null,\"keySetIdentifier\":null,\"derivedInformation\":null,\"algorithm\":null,\"keyLength\":null,\"keyProtection\":null,\"keyIndex\":null,\"pinBlockFormat\":null,\"encryptedPINBlock\":\"\"},\"textValue\":null}}],\"verificationResult\":null}],\"pointOfServiceContext\":{\"cardDataEntryMode\":\"100\",\"otherCardDataEntryMode\":null,\"cardPresent\":null,\"cardholderPresent\":null,\"unattendedLevelCategory\":null,\"partialApprovalSupported\":null,\"otherSecurityCharacteristics\":null,\"ecommerceIndicator\":true,\"motoCode\":null,\"attendedIndicator\":null,\"additionalData\":null},\"saleContext\":{\"additionalData\":[{\"key\":\"campaignData\",\"value\":\"\"}]}},\"processingResult\":{\"approvalCode\":\"\",\"approvalData\":null,\"action\":[{\"actionType\":\"\",\"additionalData\":null}],\"resultData\":{\"result\":\"\",\"resultDetails\":null,\"otherResult\":\"\",\"otherResultDetails\":\"\",\"additionalResultInformation\":null},\"additionalInformation\":[{\"key\":\"transaction\",\"value\":\"\"}]},\"securityTrailer\":{\"macData\":{\"algorithm\":\"\",\"derivedInformation\":\"\",\"keyProtection\":\"\",\"keyIndex\":\"\",\"paddingMethod\":null},\"mac\":null},\"iccRelatedData\":\"\",\"protectedData\":[{\"envelopedData\":{\"recipient\":[{\"kek\":{\"kekId\":{\"keyId\":\"\"}}}]}}],\"supplementaryData\":[{\"placeAndName\":\"networkManagementInfoCode\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"keyManagement\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"settlementData\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"issuerTraceId\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"dateConversion\",\"envelope\":\"0615\",\"key\":null},{\"placeAndName\":\"pointServicePIN\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"primaryAccountNumberCountryCode\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"serviceIndicator\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"messageSecurityCode\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"additionalDataNationalUse\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"authorizingAgentIdCode\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"dateAction\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"issuerFileUpdateCode\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"replacementAmounts\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"fileName\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"reservedNationalUse\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"privateData\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"paymentAccountData\",\"envelope\":\"01330129500193HKQIWEYVISE7UKTQK8YJ5C0\",\"key\":null},{\"placeAndName\":\"transactionData\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"primaryAccountNumberExtended\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"receivingInstitutionCountryCode\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"fileSecurityCode\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"responseIndicator\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"accountIdentification1\",\"envelope\":\"\",\"key\":null},{\"placeAndName\":\"additionalRecordData\",\"envelope\":\"001095001018ONE APPLE PARK WAY002003CA 003013APPLE.COM BIL0040108667127753007021842805822           Y\",\"key\":null}],\"initiatingParty\":null,\"recipientParty\":null,\"isSimulation\":null,\"addendumData\":{\"additionalData\":[{\"key\":\"ISO8583\",\"value\":\"0100FEFF640188E1E10A000000000000004016553650******99990000000000000229900000000063560000000229900616072724727646806100000089871603272406162905061606155818840100060032860600328651675489871600400216400216000108778APPLE.COM/BILL         866-712-7753  USA118T37150511000009999974207010321022080504M1036105000015618AQV116AQS609AQF116753201038800202140303880040214050200710418C 60484060403701330129500193HKQIWEYVISE7UKTQK8YJ5C0026000410000060084095014     009MBKCG462F101001095001018ONE APPLE PARK WAY002003CA 003013APPLE.COM BIL0040108667127753007021842805822           Y\"},{\"key\":\"MSGTYPE\",\"value\":\"0100\"},{\"key\":\"ISO8583_HOST\",\"value\":\"F0F1F0F0FEFF640188E1E10A0000000000000040F1F6F5F5F3F6F5F0F9F9F9F9F9F9F9F9F9F9F0F0F0F0F0F0F0F0F0F0F0F0F0F2F2F9F9F0F0F0F0F0F0F0F0F0F6F3F5F6F0F0F0F0F0F0F0F2F2F9F9F0F0F6F1F6F0F7F2F7F2F4F7F2F7F6F4F6F8F0F6F1F0F0F0F0F0F0F8F9F8F7F1F6F0F3F2F7F2F4F0F6F1F6F2F9F0F5F0F6F1F6F0F6F1F5F5F8F1F8F8F4F0F1F0F0F0F6F0F0F3F2F8F6F0F6F0F0F3F2F8F6F5F1F6F7F5F4F8F9F8F7F1F6F0F0F4F0F0F2F1F6F4F0F0F2F1F6F0F0F0F1F0F8F7F7F8C1D7D7D3C54BC3D6D461C2C9D3D3404040404040404040F8F6F660F7F1F260F7F7F5F34040E4E2C1F1F1F8E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3F0F3F8F8F0F0F4F0F2F1F4F0F5F0F2F0F0F7F1F0F4F1F8C340F6F0F4F8F4F0F6F0F4F0F3F7F0F1F3F3F0F1F2F9F5F0F0F1F9F3C8D2D8C9E6C5E8E5C9E2C5F7E4D2E3D8D2F8E8D1F5C3F0F0F2F6F0F0F0F4F1F0F0F0F0F0F6F0F0F8F4F0F9F5F0F1F44040404040F0F0F9D4C2D2C3C7F4F6F2C6F1F0F1F0F0F1F0F9F5F0F0F1F0F1F8D6D5C540C1D7D7D3C540D7C1D9D240E6C1E8F0F0F2F0F0F3C3C140F0F0F3F0F1F3C1D7D7D3C54BC3D6D440C2C9D3F0F0F4F0F1F0F8F6F6F7F1F2F7F7F5F3F0F0F7F0F2F1F8F4F2F8F0F5F8F2F24040404040404040404040E8\"}],\"invoice\":null,\"sale\":null},\"monitoring\":{\"startDateMs\":\"1752963132174\",\"endDateMs\":null,\"differenceDateTime\":null,\"binCode\":\"553650\",\"binDescription\":\"UNKNOWN\",\"merchantNameAceptor\":\"APPLE.COM/BILL\",\"merchantCategoryDescription\":\"UNKNOWN\",\"transactionStatus\":null,\"channelFilter\":\"ECOMMER\",\"operationFilter\":\"UNKNOWN\",\"transactionTypeDescription\":\"UNKNOWN\",\"p2pType\":null,\"originBankCode\":null,\"originBankDescription\":null},\"socketIp\":null}";
    @Setter
    private JInternalFrame parentFrame;
    private final MessageParser messageParser;

    public ConverterIso20022ViewerPanel() {
        messageParser=new MCMessageParserImpl();
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
            // Configurar ObjectMapper de forma más permisiva
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
            objectMapper.configure(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES, false);
            objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
            objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);


            ISO20022 inputObject = objectMapper.readValue(jsonString, ISO20022.class);
            ISO8583 iso8583 = ISO20022To8583Mapper.translateToISO8583(inputObject);
            Map<String, String> mapValues = ISO8583Builder.buildMapISO8583(iso8583);
            //String trama=ISOStringConverterMastercard.getInstance().convertToISOString(mapValues, true);
            String trama=messageParser.unParser(mapValues,true);
            LOGGER.info("Trama generada: [{}]", trama);
            outputTextArea.setText(trama );

            result = ParseGUI.process(ISO8583Processor.createMapFieldsISO8583(trama));
            ParseGUI.updateTreeView(treeModel, resultTree, result);

        } catch (ParserLocalException ex) {
           /* UtilGUI.showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
            outputTextArea.setText("Error: " + ex.getMessage());*/

            UtilGUI.showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
            result = ParseGUI.process(ex.getValuesMap());
            ParseGUI.updateTreeView(treeModel, resultTree, result);
            outputTextArea.setText("Error: " + ex.getMessage());
        }catch (Exception ex) {
           /* UtilGUI.showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
            outputTextArea.setText("Error: " + ex.getMessage());*/
            UtilGUI.showErrorDialog("Error al parsear el mensaje: " + ex.getMessage());
            //result = ParseGUI.process(ex.getValuesMap());
            //ParseGUI.updateTreeView(treeModel, resultTree, result);
            outputTextArea.setText("Error: " + ex.getMessage());
        }
    }

}