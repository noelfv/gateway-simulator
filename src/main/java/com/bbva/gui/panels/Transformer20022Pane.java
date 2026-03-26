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
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class Transformer20022Pane extends AbstractBasePane {

    private static final Logger LOGGER = LoggerFactory.getLogger(Transformer20022Pane.class);
    private static final String SAMPLE_ISO20022 = "{\"networkName\":\"PEER02\",\"messageFunction\":\"AUTQ\",\"socketPort\":\"6034\",\"exchangeIdentification\":null,\"protocolVersion\":null,\"traceData\":[{\"key\":\"posAdditionalData\",\"value\":\"MBKCG462F\"},{\"key\":\"header\",\"value\":\"\"},{\"key\":\"PAYMENT_ID\",\"value\":\"traceID\"}],\"transaction\":{\"transactionId\":{\"transactionReference\":\"76d30a4be36d-35d8-a277-1d1ad455fd6e\",\"transmissionDateTime\":\"2026-06-16T07:27:24.000Z\",\"systemTraceAuditNumber\":\"898716\",\"localDate\":\"0616\",\"localTime\":\"032724\",\"localDateTime\":\"2026-06-16T03:27:24.000Z\",\"acquirerReferenceData\":null,\"retrievalReferenceNumber\":\"516754898716\",\"originalDataElements\":null,\"cardIssuerReferenceData\":null},\"transactionType\":\"00\",\"accountFrom\":{\"accountId\":\"\",\"accountType\":\"00\"},\"accountTo\":{\"accountId\":\"\",\"accountType\":\"00\"},\"transactionAmounts\":{\"transactionAmount\":{\"amount\":229.9,\"currency\":\"PEN\"},\"reconciliationAmount\":{\"amount\":63.56,\"currency\":\"USD\",\"effectiveExchangeRate\":\".2764680\",\"conversionDate\":null},\"cardholderBillingAmount\":{\"amount\":229.9,\"currency\":\"PEN\",\"effectiveExchangeRate\":\"1.000000\"},\"originalTransactionAmounts\":null},\"transactionAttribute\":0,\"messageReason\":\"\",\"originalAdditionalFee\":null,\"additionalFee\":[{\"feeAmount\":{\"amount\":null,\"currency\":null},\"feeReconciliationAmount\":{\"amount\":null},\"key\":null,\"otherType\":null}],\"additionalAmount\":[{\"key\":\"BLNCHECK\",\"amount\":{\"amount\":null,\"currency\":null},\"description\":null}],\"additionalData\":[{\"key\":\"opera\",\"value\":\"000000\"},{\"key\":\"redemptionPoints\",\"value\":\"\"},{\"key\":\"electronic_commerce_indicators\":null,\"ECI\":null,\"additionalResponseData\":\"\",\"transaction_category_code\":\"T\",\"on_behalf_services\":\"18C \"}],\"additionalService\":null,\"networkManagementType\":null,\"keyExchangeData\":null,\"detailedRequestedAmount\":null,\"otherTransactionAttribute\":\"RCPT\",\"alternateMessageReason\":null,\"transactionSubtype\":null,\"specialProgrammeQualification\":[{\"detail\":[{\"name\":\"mastercard_promotion_code\",\"value\":null}]}]},\"environment\":{\"card\":{\"pan\":\"5536509999999999\",\"effectiveDate\":null,\"expiryDate\":\"2029-05-31\",\"cardSequenceNumber\":\"\",\"track1\":\"\",\"track2\":{\"textValue\":\"\"},\"track3\":null,\"serviceCode\":\"\",\"additionalCardData\":null},\"terminal\":{\"capabilities\":{\"approvalCodeLength\":null,\"cardCaptureCapable\":false,\"cardReadingCapabilities\":[{\"capability\":\"KEEN\"}],\"cardholderVerificationCapabilities\":[{\"capability\":\"UNSP\"}],\"cardWritingCapabilities\":null,\"pINLengthCapability\":null},\"terminalId\":{\"id\":\"00400216\",\"assigner\":\"\",\"country\":\"840\"},\"key\":\"OTHN\",\"otherType\":null,\"geographicLocation\":null,\"poiComponent\":null,\"offPremisesIndicator\":false},\"acquirer\":{\"id\":\"003286\",\"acquirerInstitution\":null,\"country\":\"840\",\"additionalId\":{\"key\":\"postalCode\",\"value\":\"\"}},\"sender\":{\"id\":\"003286\",\"additionalId\":{\"key\":\"additionalDataRetailer\",\"value\":\"E3F3F7F1F5F0F5F1F1F0F0F0F0F0F9F9F9F9F9F7F4F2F0F7F0F1F0F3F2F1F0F2F2F0F8F0F5F0F4D4F1F0F3F6F1F0F5F0F0F0F0F1F5F6F1F8C1D8E5F1F1F6C1D8E2F6F0F9C1D8C6F1F1F6F7F5F3F2F0F1F0F3F8F8F0F0F2F0F2F1F4F0F3\"},\"localData\":null},\"acceptor\":{\"id\":\"400216000108778\",\"nameAndLocation\":\"APPLE.COM/BILL         866-712-7753  USA\",\"localData\":{\"address\":{\"townName\":null,\"countrySubDivisionMajorName\":null,\"country\":null,\"postalCode\":\"95014     \"}},\"additionalData\":null},\"issuer\":{\"assigner\":\"000410000060084095014     \",\"additionalIdentification\":null},\"cardholder\":null,\"receiver\":null,\"atmManagerIdentification\":null,\"token\":null,\"customerDevice\":null},\"context\":{\"transactionContext\":{\"settlementService\":{\"settlementServiceDates\":{\"settlementDate\":\"0616\"},\"additionalSettlementInformation\":null},\"captureDate\":null,\"merchantCategoryCode\":\"5818\",\"merchantCategorySpecificData\":\"NATIONAL\",\"reconciliation\":{\"date\":null},\"transactionInitiator\":null,\"magneticStripeFallbackIndicator\":null,\"reSubmissionIndicator\":null,\"additionalData\":[{\"key\":\"ENTRY_MODE\",\"value\":\"ALL\"},{\"key\":\"OPERATION_TYPE\",\"value\":\"PURCHASE\"},{\"key\":\"CHANNEL\",\"value\":\"POS\"},{\"key\":\"OWNER\",\"value\":\"ONUS\"}],\"iCCFallbackIndicator\":null},\"verification\":[{\"key\":null,\"verificationInformation\":[{\"key\":\"CVC\",\"value\":{\"pinData\":{\"control\":null,\"keySetIdentifier\":null,\"derivedInformation\":null,\"algorithm\":null,\"keyLength\":null,\"keyProtection\":null,\"keyIndex\":null,\"pinBlockFormat\":null,\"encryptedPINBlock\":\"\"},\"textValue\":null}},{\"key\":\"CVV2\",\"value\":{\"pinData\":null,\"textValue\":null}}],\"verificationResult\":[{\"key\":\"card_validation_code_result\",\"entity\":null,\"otherEntity\":null,\"otherResult\":null,\"resultDetails\":[{\"key\":\"PENDING\",\"value\":\"\"}]}]}],\"pointOfServiceContext\":{\"cardDataEntryMode\":\"DFLE\",\"otherCardDataEntryMode\":null,\"cardPresent\":false,\"cardholderPresent\":false,\"unattendedLevelCategory\":null,\"partialApprovalSupported\":null,\"otherSecurityCharacteristics\":null,\"attendedIndicator\":true,\"additionalData\":[{\"key\":\"UNKNOWN\",\"value\":\"100\"}],\"eCommerceIndicator\":false,\"mOTOCode\":null},\"saleContext\":{\"additionalData\":[{\"key\":\"campaignData\",\"value\":\"\"}]},\"riskContext\":null},\"processingResult\":null,\"securityTrailer\":null,\"iccRelatedData\":\"\",\"protectedData\":[],\"supplementaryData\":[{\"placeAndName\":\"dateConversion\",\"envelope\":\"0615\",\"key\":null},{\"placeAndName\":\"paymentAccountData\",\"envelope\":\"01330129500193HKQIWEYVISE7UKTQK8YJ5C0\",\"key\":null},{\"placeAndName\":\"additionalRecordData\",\"envelope\":\"001095001018ONE APPLE PARK WAY002003CA 003013APPLE.COM BIL0040108667127753007021842805822           Y\",\"key\":null}],\"initiatingParty\":null,\"recipientParty\":null,\"isSimulation\":false,\"monitoring\":{\"startDateMs\":\"1774500144659\",\"endDateMs\":null,\"differenceDateTime\":null,\"binCode\":\"553650\",\"binDescription\":\"MASTERCARD BLACK\",\"binType\":null,\"binBrand\":null,\"merchantNameAceptor\":\"APPLE.COM/BILL\",\"merchantCategoryDescription\":\"VENTAS VIRTUALES COMERCIOS DE PRODUCTOS DIGITALES\",\"transactionStatus\":\"Pending\",\"channelFilter\":\"OTHER\",\"operationFilter\":\"PURCHASE\",\"transactionTypeDescription\":\"COMPRAS\",\"p2pType\":null,\"originBankCode\":null,\"originBankDescription\":null,\"countryDate\":\"2026-03-25T23:42:24.659-05:00\",\"isNextGen\":null,\"isMessageInvalid\":null},\"socketIp\":null,\"socketClusterPort\":null}";

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
        outputPane = new OutputTextPane("output", "Copiar");
        treePane = new TreeOutputPane("Estructura del mensaje");
        // Platform.runLater garantiza que el setText se ejecuta después de que
        // el TextArea esté completamente inicializado y en escena, lo que resuelve:
        //  1. El JSON no se muestra en formato pretty en la carga inicial.
        //  2. El primer parse falla porque getText() devuelve vacío antes del layout.
        Platform.runLater(() -> inputPane.getTextArea().setText(prettyPrint(SAMPLE_ISO20022)));
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
            String jsonString = inputPane.getTextArea().getText().trim().replaceAll("\\p{C}", "");
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
