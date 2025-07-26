package com.bbva.orchestrator.parser.iso20022;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.gateway.utils.LogsTraces;
import com.bbva.gui.dto.Metadata;
import com.bbva.orchestrator.parser.common.ISO8583Context;
import com.bbva.orchestrator.parser.common.ISOSubFieldProcess;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.validations.FieldLocalCodeMapper;
import com.bbva.orchlib.parser.ParserException;
import com.bbva.orchestrator.processes.ProcessMonitoring;
import com.bbva.orchestrator.processes.ProcessingResult;

import java.math.BigDecimal;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import com.bbva.orchestrator.configuration.monitoring.FieldDto;

import java.time.format.DateTimeFormatter;

public class ISO8583To20022Mapper {

    public static final String FIELD_NOT_MAPPED = "Field not mapped";
    private static final String TYPE_MESSAGE = "TYPE_MESSAGE";

    private static final String ADDITIONAL_AMOUNT_DOUBLE = "54.05";
    private static final String ADDITIONAL_ACCOUNT_TYPE = "54.01";
    private static final String ADDITIONAL_AMOUNT_TYPE = "54.02";
    private static final String ADDITIONAL_CURRENCY_CODE = "54.03";
    private static final String ADDITIONAL_INDICATOR = "54.04";


    private ISO8583To20022Mapper() {
    }

    public static ISO20022 translateToISO20022(ISO8583 inputObject, Map<String, String> subFields, boolean flag) {
        try {
            /*----------------------------------------
            * TRACE DATA SECTION
            ----------------------------------------*/

            // ======== FIELD 63 (MAPPED AS POS ADDITIONAL DATA) ========
            TraceDataDTO posAdditionalData = TraceDataDTO.builder()
                    .key("posAdditionalData")
                    .value(inputObject.getNetworkData())
                    .build();

            // ======== HEADER OF ISO ========
            TraceDataDTO originHeader = TraceDataDTO.builder()
                    .key("header")
                    .value(inputObject.getHeader())
                    .build();

            // ======== TEMPORAL FIELD NOT ASSOCIATED WITH ISO ========
            TraceDataDTO traceDataDTO = TraceDataDTO.builder()
                    .key("PAYMENT_ID")
                    .value(GrpcHeadersInfo.getTraceId())
                    .build();

            List<TraceDataDTO> traceDataList = new ArrayList<>();
            traceDataList.add(posAdditionalData);
            traceDataList.add(originHeader);
            traceDataList.add(traceDataDTO);

            /*----------------------------------------
            * ENVIRONMENT SECTION
            ----------------------------------------*/

            // ======== FIELD 35 (TRACK 2 DATA) ========
            Track2DTO track2 = Track2DTO
                    .builder()
                    .textValue(inputObject.getTrackTwoData())
                    .build();

            CardDTO card = CardDTO.builder()
                    // ======== FIELD 23 (CARD SEQUENCE NUMBER) ========
                    .cardSequenceNumber(inputObject.getCardSequenceNumber())
                    // ======== FIELD 14 (EXPIRATION DATE) ========
                    .expiryDate(convertFormatExpiryDate(inputObject.getDateExpiration()))
                    // ======== FIELD 2 (PAN) ========
                    .pan(inputObject.getPrimaryAccountNumber())
                    // ======== FIELD 40 (SERVICE RESTRICTION CODE) ========
                    .serviceCode(FIELD_NOT_MAPPED)
                    // ======== FIELD 45 (TRACK 1 DATA) ========
                    .track1(inputObject.getTrackOneData())
                    .track2(track2)
                    .build();

            CapabilitiesDTO capabilities = CapabilitiesDTO.builder()
                    .approvalCodeLength(null)
                    .build();

            TerminalIdDTO terminalId = TerminalIdDTO.builder()
                    // ======== FIELD 41 (CARD ACCEPTOR TERMINAL IDENTIFICATION) ========
                    .id(inputObject.getCardAcceptorTerminalIdentification())
                    // ======== FIELD 60 (POS TERMINAL DATA) ========
                    .assigner(inputObject.getPosTerminalData())
                    .build();

            String type = ISOSubFieldProcess.channelTPVIndicator(inputObject, subFields, GrpcHeadersInfo.getNetwork());
            String otherType = type != null && type.length() > 4 ? type.substring(4) : null;
            String typeValue = type != null ? type.substring(0,4) : null;
            TerminalDTO terminal = TerminalDTO.builder()
                    .capabilities(capabilities)
                    .terminalId(terminalId)
                    .key(typeValue)
                    .otherType(otherType)
                    .build();

            // ======== FIELD 62 (MAPPED AS POSTAL CODE) ========
            AdditionalIdDTO postalCodeData = AdditionalIdDTO.builder()
                    .key("postalCode")
                    .value(inputObject.getPostalCode())
                    .build();

            AcquirerDTO acquirer = AcquirerDTO.builder()
                    // ======== FIELD 32 (ACQUIRING INSTITUTION IDENTIFICATION CODE) ========
                    .id(inputObject.getAcquiringInstitutionIdentificationCode())
                    .additionalId(postalCodeData)
                    // ======== FIELD 19 (ACQUIRING INSTITUTION COUNTRY CODE) ========
                    .country(inputObject.getAcquirerCountryCode())
                    .build();

            // ======== FIELD 48 (ADDITIONAL DATA RETAILER) ========
            AdditionalIdDTO additionalDataRetailer = AdditionalIdDTO.builder()
                    .key("additionalDataRetailer")
                    .value(inputObject.getAdditionalDataRetailer())
                    .build();

            SenderDTO sender = SenderDTO.builder()
                    // ======== FIELD 33 (FORWARDING INSTITUTION IDENTIFICATION CODE) ========
                    .id(inputObject.getForwardingInstitutionIdentificationCode())
                    .additionalId(additionalDataRetailer)
                    .build();

            AcceptorDTO acceptor = AcceptorDTO.builder()
                    // ======== FIELD 42 (CARD ACCEPTOR IDENTIFICATION CODE) ========
                    .id(inputObject.getCardAcceptorIdentificationCode())
                    // ======== FIELD 43 (CARD ACCEPTOR NAME AND LOCATION) ========
                    .nameAndLocation(inputObject.getCardAcceptorNameLocation())
                    .build();

            IssuerDTO issuer = IssuerDTO.builder()
                    // ======== FIELD 61 (POS CARD ISSUER / OTHER AMOUNTS) ========
                    .assigner(inputObject.getPosCardIssuer())
                    .build();

            EnvironmentDTO environment = EnvironmentDTO.builder()
                    .card(card)
                    .terminal(terminal)
                    .acquirer(acquirer)
                    .sender(sender)
                    .acceptor(acceptor)
                    .issuer(issuer)
                    .build();

            /*----------------------------------------
            * TRANSACTION SECTION
            ----------------------------------------*/

            String processingCodeStr = inputObject.getProcessingCode();
            AccountFromDTO accountFrom = AccountFromDTO.builder()
                    .accountId(FIELD_NOT_MAPPED)
                    // ======== FIELD 3.2 (PROCESSING CODE) ========
                    .accountType(isNullOrEmptySubstring(processingCodeStr, 2, 4))
                    .build();

            AccountToDTO accountTo = AccountToDTO.builder()
                    // ======== FIELD 103 (ACCOUNT ID 2) ======== COMENTADO
                    //.accountId(inputObject.getAccountIdentification())
                    // ======== FIELD 3.3 (PROCESSING CODE) ========
                    .accountType(isNullOrEmptySubstring(processingCodeStr, 4, 6))
                    .build();

            TransactionAmountDTO transactionAmount = TransactionAmountDTO.builder()
                    // ======== FIELD 4 (TRANSACTION AMOUNT) ========
                    .amount(parseDouble(inputObject.getTransactionAmount()))
                    // ======== FIELD 49 (TRANSACTION CURRENCY CODE) ========
                    .currency(inputObject.getTransactionCurrencyCode())
                    .build();

            ReconciliationAmountDTO reconciliationAmount = ReconciliationAmountDTO.builder()
                    // ======== FIELD 5 (RECONCILIATION AMOUNT) ========
                    .amount(parseDouble(inputObject.getSettlementAmount()))
                    // ======== FIELD 9 (RECONCILIATION EXCHANGE RATE) ========
                   // .effectiveExchangeRate(parseDouble(inputObject.getConversionRateSettlement()))
                    .effectiveExchangeRate(inputObject.getConversionRateSettlement()!=null?conversionRateValidation(inputObject.getConversionRateSettlement()):null)
                    // ======== FIELD 50 (SETTLEMENT CURRENCY CODE) ========
                    .currency(inputObject.getSettlementCurrencyCode())
                    .build();

            CardholderBillingAmountDTO cardholderBillingAmount = CardholderBillingAmountDTO.builder()
                    // ======== FIELD 6 (CARDHOLDER BILLING AMOUNT) ========
                    .amount(parseDouble(inputObject.getCardHolderBillingAmount()))
                    // ======== FIELD 10 (CARDHOLDER BILLING EXCHANGE RATE) ========
                    .effectiveExchangeRate(inputObject.getConversionRate()!=null?conversionRateValidation(inputObject.getConversionRate()):null)
                    // ======== FIELD 51 (CARDHOLDER BILLING CURRENCY CODE) ========
                    .currency(inputObject.getCardholderBillingCurrencyCode())
                    .build();


            TransactionAmountsDTO transactionAmounts = TransactionAmountsDTO.builder()
                    .transactionAmount(transactionAmount)
                    .reconciliationAmount(reconciliationAmount)
                    .cardholderBillingAmount(cardholderBillingAmount)
                    .build();

            String oriDataElementsStr = inputObject.getOriginalDataElements();

            // ======== FIELD 90 (ORIGINAL DATA ELEMENTS) ========
            OriginalDataElementsDTO originalDataElements = OriginalDataElementsDTO.builder()
                    // 90.1 MESSAGE TYPE
                    .messageFunction(isNullOrEmptySubstring(oriDataElementsStr, 0, 4))
                    // 90.2 SYSTEMS TRACE AUDIT NUMBER
                    .systemTraceAuditNumber(isNullOrEmptySubstring(oriDataElementsStr, 4, 10))
                    // 90.3 TRANSMISSION DATE & TIME
                    .transmissionDateTime(isNullOrEmptySubstring(oriDataElementsStr, 10, 20))
                    // 90.4 ACQUIRER INSTITUTION ID
                    .acquirerId(isNullOrEmptySubstring(oriDataElementsStr, 20, 31))
                    // 90.5 FORWARDING INSTITUTION ID
                    .senderIdentification(isNullOrEmptySubstring(oriDataElementsStr, 31, 42))
                    .build();

            TransactionIdDTO transactionId = TransactionIdDTO.builder()
                    // ======== FIELD 11 (SYSTEM TRACE AUDIT NUMBER) ========
                    .systemTraceAuditNumber(inputObject.getSystemTraceAuditNumber())
                    // ======== FIELD 12 (LOCAL TRANSACTION DATE) ========
                    .localDate(inputObject.getLocalTransactionDate())
                    // ======== FIELD 13 (LOCAL TRANSACTION TIME) ========
                    .localTime(inputObject.getLocalTransactionTime())
                    .acquirerReferenceData(FIELD_NOT_MAPPED)
                    // ======== FIELD 37 (RETRIEVAL REFERENCE NUMBER) ========
                    .retrievalReferenceNumber(inputObject.getRetrievalReferenceNumber())
                    .originalDataElements(originalDataElements)
                    // ======== FIELD 7 (TRANSMISSION DATE & TIME) ========
                    .transmissionDateTime(convertFormatDateTime(inputObject.getTransmissionDateTime()))
                    // ======== ID MONITOR ========
                    .transactionReference(ProcessMonitoring.createTransactionReference(inputObject, subFields,  GrpcHeadersInfo.getNetwork()))
                    .build();

            FeeAmountDTO feeAmount = FeeAmountDTO.builder()
                    .amount(null)
                    .build();

            FeeReconciliationAmountDTO feeReconciliation = FeeReconciliationAmountDTO.builder()
                    .amount(null)
                    .build();

            AdditionalFeesDTO additionalFees = AdditionalFeesDTO.builder()
                    .feeAmount(feeAmount)
                    .feeReconciliationAmount(feeReconciliation)
                    .build();

            List<AdditionalFeesDTO> additionalFeesList = new ArrayList<>();
            additionalFeesList.add(additionalFees);

            List<AdditionalAmountDTO> additionalAmountList = new ArrayList<>();
            // ======== FIELD 54 (ADDITIONAL AMOUNTS) ========
            AdditionalAmountDTO additionalAmount = AdditionalAmountDTO.builder()
                    .key("additionalAmounts")
                    .amount(
                            AmountDTO.builder()
                                    .amount(subFields.containsKey(ADDITIONAL_AMOUNT_DOUBLE)
                                                    ? parseDouble(subFields.get(ADDITIONAL_AMOUNT_DOUBLE))
                                                    : null)
                                    .build()
                    )
                    .build();


            additionalAmountList.add(additionalAmount);

            List<AdditionalDataDTO> additionalTransactionDataList = new ArrayList<>();

            // ======== FIELD 58 (MAPPED AS REDEMPTION POINTS) ======== COMENTADO
            /*AdditionalDataDTO redemptionPointsData = AdditionalDataDTO.builder()
                    .key("redemptionPoints")
                    .value(inputObject.getRedemptionPoints())
                    .build();
            additionalTransactionDataList.add(redemptionPointsData);*/

            // ======== FIELD 44 (ADDITIONAL RESPONSE DATA) ========
            AdditionalDataDTO additionalResponseData = AdditionalDataDTO.builder()
                    .key("additionalResponseData")
                    .value(inputObject.getAdditionalResponseData())
                    .build();
            additionalTransactionDataList.add(additionalResponseData);

            // FIELD 54 - Se mapea los 4 primeros subcampos a esta variable AdditionalData temporalmente
            // SUB CAMPO 01
            AdditionalDataDTO accountType = AdditionalDataDTO.builder()
                    .key("accountType")
                    .value(subFields.getOrDefault(ADDITIONAL_ACCOUNT_TYPE, null))
                    .build();
            additionalTransactionDataList.add(accountType);

            // SUB CAMPO 02
            AdditionalDataDTO amountType = AdditionalDataDTO.builder()
                    .key("amountType")
                    .value(subFields.containsKey(ADDITIONAL_AMOUNT_TYPE)
                            ? subFields.get(ADDITIONAL_AMOUNT_TYPE)
                            : null)
                    .build();
            additionalTransactionDataList.add(amountType);

            // SUB CAMPO 03
            AdditionalDataDTO currencyCode = AdditionalDataDTO.builder()
                    .key("currencyCode")
                    .value(subFields.containsKey(ADDITIONAL_CURRENCY_CODE)
                            ? subFields.get(ADDITIONAL_CURRENCY_CODE)
                            : null)
                    .build();
            additionalTransactionDataList.add(currencyCode);

            // SUB CAMPO 04
            AdditionalDataDTO indicator = AdditionalDataDTO.builder()
                    .key("indicator")
                    .value(subFields.containsKey(ADDITIONAL_INDICATOR)
                            ? subFields.get(ADDITIONAL_INDICATOR)
                            : null)
                    .build();
            additionalTransactionDataList.add(indicator);

            TransactionDTO transaction = TransactionDTO.builder()
                    // ======== FIELD 3.1 (PROCESSING CODE) ========
                    .transactionType(isNullOrEmptySubstring(processingCodeStr, 0, 2))
                    .accountFrom(accountFrom)
                    .accountTo(accountTo)
                    .transactionAmounts(transactionAmounts)
                    .transactionId(transactionId)
                    .transactionAttribute(0L)
                    // ======== FIELD 25 (POS CONDITION CODE) ========
                    .messageReason(inputObject.getPointServiceConditionCode())
                    .additionalFee(additionalFeesList)
                    .additionalAmount(additionalAmountList)
                    .additionalData(additionalTransactionDataList)
                    .build();


            /*----------------------------------------
            * CONTEXT SECTION
            ----------------------------------------*/

            Boolean eCommerceIndicatorString= ISOSubFieldProcess.channelECommerceIndicator(inputObject, subFields, GrpcHeadersInfo.getNetwork());

            // ======== FIELD 28 (AMOUNT TRANSACTION FEE) ========
            ReconciliationDTO reconciliation = ReconciliationDTO.builder()
                    .date(inputObject.getAmountTransactionFee())
                    .build();

            // ======== FIELD 15 (SETTLEMENT DATE) ========
            SettlementServiceDatesDTO settlementServiceDates = SettlementServiceDatesDTO.builder()
                    .settlementDate(inputObject.getSettlementDate())
                    .build();

            SettlementServiceDTO settlementService = SettlementServiceDTO.builder()
                    .settlementServiceDates(settlementServiceDates)
                    .build();

            AdditionalDataDTO operationType = AdditionalDataDTO.builder()
                    .key("OPERATION_TYPE")
                    .value(ProcessMonitoring.operationTypeValue(inputObject.getMessageType(),transaction.getTransactionType()))
                    .build();

            AdditionalDataDTO channel = AdditionalDataDTO.builder()
                    .key("CHANNEL")
                    .value(ProcessMonitoring.channelValue(inputObject.getMessageType(),eCommerceIndicatorString,environment.getTerminal().getKey()))
                    .build();

            List<AdditionalDataDTO> additionalDataList = new ArrayList<>();
            additionalDataList.add(operationType);
            additionalDataList.add(channel);

            TransactionContextDTO transactionContext = TransactionContextDTO.builder()
                    // ======== FIELD 18 (MERCHANT TYPE) ========
                    .merchantCategoryCode(inputObject.getMerchantType())
                    .reconciliation(reconciliation)
                    .settlementService(settlementService)
                    // ======== FIELD 17 (CAPTURE DATE) ======== COMENTADO
                    //.captureDate(inputObject.getCaptureDate())
                    .additionalData(additionalDataList)
                    .build();

            PointOfServiceContextDTO pointOfServiceContext = PointOfServiceContextDTO.builder()
                    // ======== FIELD 22 (POINT OF SERVICE ENTRY MODE) ========
                    .cardDataEntryMode(inputObject.getPointServiceEntryMode())
                    .ecommerceIndicator(eCommerceIndicatorString)
                    .build();

            PINDataDTO pinDataDTO = PINDataDTO.builder()
                    // ======== FIELD 52 (PIN DATA) ========
                    .encryptedPINBlock(inputObject.getPinData())
                    .build();

            ValueDTO verificationValue = ValueDTO.builder()
                    .pinData(pinDataDTO)
                    .build();

            VerificationInformationDTO verificationInformation = VerificationInformationDTO.builder()
                    .value(verificationValue)
                    .build();

            List<VerificationInformationDTO> verificationInformationList = new ArrayList<>();
            verificationInformationList.add(verificationInformation);

            VerificationDTO verification = VerificationDTO.builder()
                    .verificationInformation(verificationInformationList)
                    .build();

            // ======== FIELD 59 (NATIONAL POS GEOGRAPHIC DATA/ CAMPAIGN DATA) ========
            AdditionalDataDTO campaignData = AdditionalDataDTO.builder()
                    .key("campaignData")
                    .value(inputObject.getCampaignData())
                    .build();

            List<AdditionalDataDTO> saleContextList = new ArrayList<>();
            saleContextList.add(campaignData);

            SaleContextDTO saleContext = SaleContextDTO.builder()
                    .additionalData(saleContextList)
                    .build();

            List<VerificationDTO> verificationList = new ArrayList<>();
            verificationList.add(verification);

            ContextDTO context = ContextDTO.builder()
                    .transactionContext(transactionContext)
                    .pointOfServiceContext(pointOfServiceContext)
                    .verification(verificationList)
                    .saleContext(saleContext)
                    .build();

            /*----------------------------------------
            * SUPPLEMENTARY DATA SECTION
            ----------------------------------------*/

            // ======== FIELD 70 (NETWORK MANAGEMENT INFORMATION CODE) ========
            SupplementaryDataDTO networkManagementInfoCode = SupplementaryDataDTO.builder()
                    .placeAndName("networkManagementInfoCode")
                    .envelope(inputObject.getNetworkManagementInformationCode())
                    .build();

            // ======== FIELD 120 (MAPPED AS KEY MANAGEMENT) ======== DISTINTO
            SupplementaryDataDTO keyManagement = SupplementaryDataDTO.builder()
                    .placeAndName("keyManagement")
                    .envelope(inputObject.getKeyManagement())
                    .build();

            // ======== FIELD 125 (MAPPED AS SETTLEMENT DATA) ========
            SupplementaryDataDTO settlementData = SupplementaryDataDTO.builder()
                    .placeAndName("settlementData")
                    .envelope(inputObject.getSettlementData())
                    .build();

            // ======== FIELD 126 (MAPPED AS ISSUER TRACE ID) ========
            SupplementaryDataDTO issuerTraceId = SupplementaryDataDTO.builder()
                    .placeAndName("issuerTraceId")
                    .envelope(inputObject.getIssuerTraceId())
                    .build();

            /*MAPEO TEMPORAL- INICIO.
             *
             * Los campos que no tengan una correspondencia en el ISO20022 se mapean como SupplementaryDataDTO
             * En donde el campo placeAndName es el nombre del campo y el campo envelope es el valor del campo
             * Se debe realizar el mapeo correcto de estos campos cuando Global libere el DTO
             *
             */

            // ======== FIELD 16 (MAPPED AS DATE CONVERSION) ========
            SupplementaryDataDTO dateConversion = SupplementaryDataDTO.builder()
                    .placeAndName("dateConversion")
                    .envelope(inputObject.getDateConversion())
                    .build();

            // ======== FIELD 26 (POINT SERVICE PIN) ========
            SupplementaryDataDTO pointServicePIN = SupplementaryDataDTO.builder()
                    .placeAndName("pointServicePIN")
                    .envelope(inputObject.getPointServicePIN())
                    .build();

            // ======== FIELD 56 (PAYMENT ACCOUNT DATA) ========
            SupplementaryDataDTO paymentAccountData = SupplementaryDataDTO.builder()
                    .placeAndName("paymentAccountData")
                    .envelope(inputObject.getPaymentAccountData())
                    .build();

            // ======== FIELD 94 (SERVICE INDICATOR) ========
            SupplementaryDataDTO serviceIndicator = SupplementaryDataDTO.builder()
                    .placeAndName("serviceIndicator")
                    .envelope(inputObject.getServiceIndicator())
                    .build();

            // ======== FIELD 96 (MESSAGE SECURITY CODE) ========
            SupplementaryDataDTO messageSecurityCode = SupplementaryDataDTO.builder()
                    .placeAndName("messageSecurityCode")
                    .envelope(inputObject.getMessageSecurityCode())
                    .build();

            // ======== FIELD 104 (TRANSACTION DATA) ========
            SupplementaryDataDTO transactionData = SupplementaryDataDTO.builder()
                    .placeAndName("transactionData")
                    .envelope(inputObject.getTransactionData())
                    .build();

            // ======== FIELD 112 (ADDITIONAL DATA NATIONAL USE) ========
            SupplementaryDataDTO additionalDataNationalUse = SupplementaryDataDTO.builder()
                    .placeAndName("additionalDataNationalUse")
                    .envelope(inputObject.getAdditionalDataNationalUse())
                    .build();

            // ======== FIELD 121 (AUTHORIZING AGENT ID CODE) ======== DISTINTO
            SupplementaryDataDTO authorizingAgentIdCode = SupplementaryDataDTO.builder()
                    .placeAndName("authorizingAgentIdCode")
                    .envelope(inputObject.getAuthorizingAgentIdCode())
                    .build();

            // ======== FIELD 8 (AMOUNT_CARD_HOLDER_BILLING_FEE) ======== COMENTADO
            /*SupplementaryDataDTO amountCardholderBillingFee = SupplementaryDataDTO.builder()
                    .placeAndName("amountCardholderBillingFee")
                    .envelope(inputObject.getAmountCardholderBillingFee())
                    .build();*/

            // ======== FIELD 20 (PAN_COUNTRY_CODE) ========
            SupplementaryDataDTO primaryAccountNumberCountryCode = SupplementaryDataDTO.builder()
                    .placeAndName("primaryAccountNumberCountryCode")
                    .envelope(inputObject.getPrimaryAccountNumberCountryCode())
                    .build();

            // ======== FIELD 21 (FORWARDING_INSTITUTION_COUNTRY_CODE) ======== COMENTADO
            /*SupplementaryDataDTO forwardingInstitutionCountryCode = SupplementaryDataDTO.builder()
                    .placeAndName("forwardingInstitutionCountryCode")
                    .envelope(inputObject.getForwardingInstitutionCountryCode())
                    .build();*/

            // ======== FIELD 24 (NETWORK_INTERNATIONAL_ID) ======== COMENTADO
            /*SupplementaryDataDTO networkInternationalId = SupplementaryDataDTO.builder()
                    .placeAndName("networkInternationalId")
                    .envelope(inputObject.getNetworkInternationalId())
                    .build();*/

            // ======== FIELD 27 (AUTHORIZATION_ID_RESPONSE_LENGTH) ======== COMENTADO
            /*SupplementaryDataDTO authorizationIdResponseLength = SupplementaryDataDTO.builder()
                    .placeAndName("authorizationIdResponseLength")
                    .envelope(inputObject.getAuthorizationIdResponseLength())
                    .build();*/

            // ======== FIELD 29 (AMOUNT_SETTLEMENT_FEE) ======== COMENTADO
            /*SupplementaryDataDTO amountSettlementFee = SupplementaryDataDTO.builder()
                    .placeAndName("amountSettlementFee")
                    .envelope(inputObject.getAmountSettlementFee())
                    .build();*/

            // ======== FIELD 30 (AMOUNT_TRANSACTION_PROCESSING_FEE) ======== COMENTADO
            /*SupplementaryDataDTO amountTransactionProcessingFee = SupplementaryDataDTO.builder()
                    .placeAndName("amountTransactionProcessingFee")
                    .envelope(inputObject.getAmountTransactionProcessingFee())
                    .build();*/

            // ======== FIELD 31 (AMOUNT_SETTLEMENT_PROCESSING_FEE) ======== COMENTADO
            /*SupplementaryDataDTO amountSettlementProcessingFee = SupplementaryDataDTO.builder()
                    .placeAndName("amountSettlementProcessingFee")
                    .envelope(inputObject.getAmountSettlementProcessingFee())
                    .build();*/

            // ======== FIELD 34 (PAN_EXTENDED) ========
            SupplementaryDataDTO primaryAccountNumberExtended = SupplementaryDataDTO.builder()
                    .placeAndName("primaryAccountNumberExtended")
                    .envelope(inputObject.getPrimaryAccountNumberExtended())
                    .build();

            // ======== FIELD 36 (TRACK_THREE_DATA) ======== COMENTADO
            /*SupplementaryDataDTO trackThreeData = SupplementaryDataDTO.builder()
                    .placeAndName("trackThreeData")
                    .envelope(inputObject.getTrackThreeData())
                    .build();*/

            // ======== FIELD 46 (EXPANDED_ADDITIONAL_AMOUNTS) ======== COMENTADO
            /*SupplementaryDataDTO expandedAdditionalAmounts = SupplementaryDataDTO.builder()
                    .placeAndName("expandedAdditionalAmounts")
                    .envelope(inputObject.getExpandedAdditionalAmounts())
                    .build();*/

            // ======== FIELD 47 (ADDITIONAL_DATA_NATIONAL_USE_2) ======== COMENTADO
            /*SupplementaryDataDTO additionalDataNationalUse2 = SupplementaryDataDTO.builder()
                    .placeAndName("additionalDataNationalUse2")
                    .envelope(inputObject.getAdditionalDataNationalUse2())
                    .build();*/

            // ======== FIELD 64 (MESSAGE_AUTHENTICATION_CODE) ======== COMENTADO
            /*SupplementaryDataDTO messageAuthenticationCode = SupplementaryDataDTO.builder()
                    .placeAndName("messageAuthenticationCode")
                    .envelope(inputObject.getMessageAuthenticationCode())
                    .build();*/

            // ======== FIELD 66 (SETTLEMENT_CODE) ======== COMENTADO
            /*SupplementaryDataDTO settlementCode = SupplementaryDataDTO.builder()
                    .placeAndName("settlementCode")
                    .envelope(inputObject.getSettlementCode())
                    .build();*/

            // ======== FIELD 67 (EXTENDED_PAYMENT_CODE) ======== COMENTADO
            /*SupplementaryDataDTO extendedPaymentCode = SupplementaryDataDTO.builder()
                    .placeAndName("extendedPaymentCode")
                    .envelope(inputObject.getExtendedPaymentCode())
                    .build();*/

            // ======== FIELD 68 (RECEIVING_INSTITUTION_COUNTRY_CODE) ========
            SupplementaryDataDTO receivingInstitutionCountryCode = SupplementaryDataDTO.builder()
                    .placeAndName("receivingInstitutionCountryCode")
                    .envelope(inputObject.getReceivingInstitutionCountryCode())
                    .build();

            // ======== FIELD 69 (SETTLEMENT_INSTITUTION_COUNTRY_CODE) ======== COMENTADO
            /*SupplementaryDataDTO settlementInstitutionCountryCode = SupplementaryDataDTO.builder()
                    .placeAndName("settlementInstitutionCountryCode")
                    .envelope(inputObject.getSettlementInstitutionCountryCode())
                    .build();*/

            // ======== FIELD 71 (MESSAGE_NUMBER) ======== COMENTADO
            /*SupplementaryDataDTO messageNumber = SupplementaryDataDTO.builder()
                    .placeAndName("messageNumber")
                    .envelope(inputObject.getMessageNumber())
                    .build();*/

            // ======== FIELD 72 (MESSAGE_NUMBER_LAST) ======== COMENTADO
            /*SupplementaryDataDTO messageNumberLast = SupplementaryDataDTO.builder()
                    .placeAndName("messageNumberLast")
                    .envelope(inputObject.getMessageNumberLast())
                    .build();*/

            // ======== FIELD 73 (DATE_ACTION) ========
            SupplementaryDataDTO dateAction = SupplementaryDataDTO.builder()
                    .placeAndName("dateAction")
                    .envelope(inputObject.getDateAction())
                    .build();

            /*// ======== FIELD 74 (CREDITS_NUMBER) ======== COMENTADO HASTA EL 89
            SupplementaryDataDTO creditsNumber = SupplementaryDataDTO.builder()
                    .placeAndName("creditsNumber")
                    .envelope(inputObject.getCreditsNumber())
                    .build();

            // ======== FIELD 75 (CREDITS_REVERSAL_NUMBER) ========
            SupplementaryDataDTO creditsReversalNumber = SupplementaryDataDTO.builder()
                    .placeAndName("creditsReversalNumber")
                    .envelope(inputObject.getCreditsReversalNumber())
                    .build();

            // ======== FIELD 76 (DEBITS_NUMBER) ========
            SupplementaryDataDTO debitsNumber = SupplementaryDataDTO.builder()
                    .placeAndName("debitsNumber")
                    .envelope(inputObject.getDebitsNumber())
                    .build();

            // ======== FIELD 77 (DEBITS_REVERSAL_NUMBER) ========
            SupplementaryDataDTO debitsReversalNumber = SupplementaryDataDTO.builder()
                    .placeAndName("debitsReversalNumber")
                    .envelope(inputObject.getDebitsReversalNumber())
                    .build();

            // ======== FIELD 78 (TRANSFER_NUMBER) ========
            SupplementaryDataDTO transferNumber = SupplementaryDataDTO.builder()
                    .placeAndName("transferNumber")
                    .envelope(inputObject.getTransferNumber())
                    .build();

            // ======== FIELD 79 (TRANSFER_REVERSAL_NUMBER) ========
            SupplementaryDataDTO transferReversalNumber = SupplementaryDataDTO.builder()
                    .placeAndName("transferReversalNumber")
                    .envelope(inputObject.getTransferReversalNumber())
                    .build();

            // ======== FIELD 80 (INQUIRIES_NUMBER) ========
            SupplementaryDataDTO inquiriesNumber = SupplementaryDataDTO.builder()
                    .placeAndName("inquiriesNumber")
                    .envelope(inputObject.getInquiriesNumber())
                    .build();

            // ======== FIELD 81 (AUTHORIZATION_NUMBER) ========
            SupplementaryDataDTO authorizationNumber = SupplementaryDataDTO.builder()
                    .placeAndName("authorizationNumber")
                    .envelope(inputObject.getAuthorizationNumber())
                    .build();

            // ======== FIELD 82 (CREDITS_PROCESSING_FEE_AMOUNT) ========
            SupplementaryDataDTO creditsProcessingFeeAmount = SupplementaryDataDTO.builder()
                    .placeAndName("creditsProcessingFeeAmount")
                    .envelope(inputObject.getCreditsProcessingFeeAmount())
                    .build();

            // ======== FIELD 83 (CREDITS_TRANSACTION_FEE_AMOUNT) ========
            SupplementaryDataDTO creditsTransactionFeeAmount = SupplementaryDataDTO.builder()
                    .placeAndName("creditsTransactionFeeAmount")
                    .envelope(inputObject.getCreditsTransactionFeeAmount())
                    .build();

            // ======== FIELD 84 (DEBITS_PROCESSING_FEE_AMOUNT) ========
            SupplementaryDataDTO debitsProcessingFeeAmount = SupplementaryDataDTO.builder()
                    .placeAndName("debitsProcessingFeeAmount")
                    .envelope(inputObject.getDebitsProcessingFeeAmount())
                    .build();

            // ======== FIELD 85 (DEBITS_TRANSACTION_FEE_AMOUNT) ========
            SupplementaryDataDTO debitsTransactionFeeAmount = SupplementaryDataDTO.builder()
                    .placeAndName("debitsTransactionFeeAmount")
                    .envelope(inputObject.getDebitsTransactionFeeAmount())
                    .build();

            // ======== FIELD 86 (CREDITS_AMOUNT) ========
            SupplementaryDataDTO creditsAmount = SupplementaryDataDTO.builder()
                    .placeAndName("creditsAmount")
                    .envelope(inputObject.getCreditsAmount())
                    .build();

            // ======== FIELD 87 (CREDITS_REVERSAL_AMOUNT) ========
            SupplementaryDataDTO creditsReversalAmount = SupplementaryDataDTO.builder()
                    .placeAndName("creditsReversalAmount")
                    .envelope(inputObject.getCreditsReversalAmount())
                    .build();

            // ======== FIELD 88 (DEBITS_AMOUNT) ========
            SupplementaryDataDTO debitsAmount = SupplementaryDataDTO.builder()
                    .placeAndName("debitsAmount")
                    .envelope(inputObject.getDebitsAmount())
                    .build();

            // ======== FIELD 89 (DEBITS_REVERSAL_AMOUNT) ========
            SupplementaryDataDTO debitsReversalAmount = SupplementaryDataDTO.builder()
                    .placeAndName("debitsReversalAmount")
                    .envelope(inputObject.getDebitsReversalAmount())
                    .build();*/

            // ======== FIELD 91 (ISSUER_FILE_UPDATE_CODE) ========
            SupplementaryDataDTO issuerFileUpdateCode = SupplementaryDataDTO.builder()
                    .placeAndName("issuerFileUpdateCode")
                    .envelope(inputObject.getIssuerFileUpdateCode())
                    .build();

            // ======== FIELD 92 (FILE_SECURITY_CODE) ========
            SupplementaryDataDTO fileSecurityCode = SupplementaryDataDTO.builder()
                    .placeAndName("fileSecurityCode")
                    .envelope(inputObject.getFileSecurityCode())
                    .build();

            // ======== FIELD 93 (RESPONSE_INDICATOR) ========
            SupplementaryDataDTO responseIndicator = SupplementaryDataDTO.builder()
                    .placeAndName("responseIndicator")
                    .envelope(inputObject.getResponseIndicator())
                    .build();

            // ======== FIELD 95 (REPLACEMENT_AMOUNTS) ========
            SupplementaryDataDTO replacementAmounts = SupplementaryDataDTO.builder()
                    .placeAndName("replacementAmounts")
                    .envelope(inputObject.getReplacementAmounts())
                    .build();

            /*// ======== FIELD 97 (AMOUNT_NET_SETTLEMENT) ======== COMENTADO HASTA EL 100
            SupplementaryDataDTO amountNetSettlement = SupplementaryDataDTO.builder()
                    .placeAndName("amountNetSettlement")
                    .envelope(inputObject.getAmountNetSettlement())
                    .build();

            // ======== FIELD 98 (PAYEE) ========
            SupplementaryDataDTO payee = SupplementaryDataDTO.builder()
                    .placeAndName("payee")
                    .envelope(inputObject.getPayee())
                    .build();

            // ======== FIELD 99 (SETTLEMENT_INSTITUTION_IDENTIFICATION_CODE) ========
            SupplementaryDataDTO settlementInstitutionIdentificationCode = SupplementaryDataDTO.builder()
                    .placeAndName("settlementInstitutionIdentificationCode")
                    .envelope(inputObject.getSettlementInstitutionIdentificationCode())
                    .build();

            // ======== FIELD 100 (RECEIVING_INSTITUTION_IDENTIFICATION_CODE) ========
            SupplementaryDataDTO receivingInstitutionIdentificationCode = SupplementaryDataDTO.builder()
                    .placeAndName("receivingInstitutionIdentificationCode")
                    .envelope(inputObject.getReceivingInstitutionIdentificationCode())
                    .build();*/

            // ======== FIELD 101 (FILE_NAME) ========
            SupplementaryDataDTO fileName = SupplementaryDataDTO.builder()
                    .placeAndName("fileName")
                    .envelope(inputObject.getFileName())
                    .build();

            // ======== FIELD 102 (ACCOUNT_IDENTIFICATION_1) ========
            SupplementaryDataDTO accountIdentification1 = SupplementaryDataDTO.builder()
                    .placeAndName("accountIdentification1")
                    .envelope(inputObject.getAccountIdentification1())
                    .build();

            // ======== FIELD 106 (FLEET_SERVICE_DATA) ======== COMENTADO
            /*SupplementaryDataDTO fleetServiceData = SupplementaryDataDTO.builder()
                    .placeAndName("fleetServiceData")
                    .envelope(inputObject.getFleetServiceData())
                    .build();*/

            // ======== FIELD 108 (ADDITIONAL_TRANSACTION_REFERENCE_DATA) ======== COMENTADO
            /*SupplementaryDataDTO additionalTransactionReferenceData = SupplementaryDataDTO.builder()
                    .placeAndName("additionalTransactionReferenceData")
                    .envelope(inputObject.getAdditionalTransactionReferenceData())
                    .build();*/

            // ======== FIELD 109 (ISO_USE) ======== COMENTADO
            /*SupplementaryDataDTO isoUse = SupplementaryDataDTO.builder()
                    .placeAndName("isoUse")
                    .envelope(inputObject.getIsoUse())
                    .build();
*/
            // ======== FIELD 115 (RESERVED_NATIONAL_USE) ========
            SupplementaryDataDTO reservedNationalUse = SupplementaryDataDTO.builder()
                    .placeAndName("reservedNationalUse")
                    .envelope(inputObject.getReservedNationalUse())
                    .build();

            // ======== FIELD 119 (RESERVED_NATIONAL_USE_2) ======== COMENTADO
            /*SupplementaryDataDTO reservedNationalUse2 = SupplementaryDataDTO.builder()
                    .placeAndName("reservedNationalUse2")
                    .envelope(inputObject.getReservedNationalUse2())
                    .build();*/


            // ======== FIELD 122 (RESERVED_NATIONAL_USE_2) ======== COMENTADO
            SupplementaryDataDTO additionalRecordData = SupplementaryDataDTO.builder()
                    .placeAndName("additionalRecordData")
                    .envelope(inputObject.getAdditionalRecordData())
                    .build();

            // ======== FIELD 127 (PRIVATE_DATA) ========
            SupplementaryDataDTO privateData = SupplementaryDataDTO.builder()
                    .placeAndName("privateData")
                    .envelope(inputObject.getPrivateData())
                    .build();

            // ======== FIELD 128 (MESSAGE_AUTHENTICATION_CODE_2) ======== COMENTADO - NO SE ENCUENTRA
            /*SupplementaryDataDTO messageAuthenticationCode2 = SupplementaryDataDTO.builder()
                    .placeAndName("messageAuthenticationCode2")
                    .envelope(inputObject.getMessageAuthenticationCode2())
                    .build();*/

            /*MAPEO TEMPORAL. - FIN*/

            List<SupplementaryDataDTO> supplementaryDataList = new ArrayList<>();
            supplementaryDataList.add(networkManagementInfoCode);
            supplementaryDataList.add(keyManagement);
            supplementaryDataList.add(settlementData);
            supplementaryDataList.add(issuerTraceId);
            // MAPEOS TEMPORALES
            supplementaryDataList.add(dateConversion);
            supplementaryDataList.add(pointServicePIN);
            supplementaryDataList.add(primaryAccountNumberCountryCode);
            supplementaryDataList.add(serviceIndicator);
            supplementaryDataList.add(messageSecurityCode);
            supplementaryDataList.add(additionalDataNationalUse);
            supplementaryDataList.add(authorizingAgentIdCode);
            supplementaryDataList.add(dateAction);
            supplementaryDataList.add(issuerFileUpdateCode);
            supplementaryDataList.add(replacementAmounts);
            supplementaryDataList.add(fileName);
            supplementaryDataList.add(reservedNationalUse);
            supplementaryDataList.add(privateData);
            supplementaryDataList.add(paymentAccountData);
            supplementaryDataList.add(transactionData);
            supplementaryDataList.add(primaryAccountNumberExtended);
            supplementaryDataList.add(receivingInstitutionCountryCode);
            supplementaryDataList.add(fileSecurityCode);
            supplementaryDataList.add(responseIndicator);
            supplementaryDataList.add(accountIdentification1);
            supplementaryDataList.add(additionalRecordData);
            /*supplementaryDataList.add(messageAuthenticationCode2);
            supplementaryDataList.add(amountCardholderBillingFee);
            supplementaryDataList.add(forwardingInstitutionCountryCode);
            supplementaryDataList.add(networkInternationalId);
            supplementaryDataList.add(authorizationIdResponseLength);
            supplementaryDataList.add(amountSettlementFee);
            supplementaryDataList.add(amountTransactionProcessingFee);
            supplementaryDataList.add(amountSettlementProcessingFee);
            supplementaryDataList.add(trackThreeData);
            supplementaryDataList.add(expandedAdditionalAmounts);
            supplementaryDataList.add(additionalDataNationalUse2);
            supplementaryDataList.add(messageAuthenticationCode);
            supplementaryDataList.add(settlementCode);
            supplementaryDataList.add(extendedPaymentCode);
            supplementaryDataList.add(settlementInstitutionCountryCode);
            supplementaryDataList.add(messageNumber);
            supplementaryDataList.add(messageNumberLast);
            supplementaryDataList.add(creditsNumber);
            supplementaryDataList.add(creditsReversalNumber);
            supplementaryDataList.add(debitsNumber);
            supplementaryDataList.add(debitsReversalNumber);
            supplementaryDataList.add(transferNumber);
            supplementaryDataList.add(transferReversalNumber);
            supplementaryDataList.add(inquiriesNumber);
            supplementaryDataList.add(authorizationNumber);
            supplementaryDataList.add(creditsProcessingFeeAmount);
            supplementaryDataList.add(creditsTransactionFeeAmount);
            supplementaryDataList.add(debitsProcessingFeeAmount);
            supplementaryDataList.add(debitsTransactionFeeAmount);
            supplementaryDataList.add(creditsAmount);
            supplementaryDataList.add(creditsReversalAmount);
            supplementaryDataList.add(debitsAmount);
            supplementaryDataList.add(debitsReversalAmount);
            supplementaryDataList.add(amountNetSettlement);
            supplementaryDataList.add(payee);
            supplementaryDataList.add(settlementInstitutionIdentificationCode);
            supplementaryDataList.add(receivingInstitutionIdentificationCode);
            supplementaryDataList.add(fleetServiceData);
            supplementaryDataList.add(additionalTransactionReferenceData);
            supplementaryDataList.add(isoUse);
            supplementaryDataList.add(reservedNationalUse2);*/

            /*----------------------------------------
            * PROTECTED DATA SECTION
            ----------------------------------------*/

            // ======== FIELD 123 (MAPPED AS CRYPTOGRAPHIC SERVICE MESSAGE) ========
            KEKIdDTO kekId = KEKIdDTO.builder()
                    .keyId(inputObject.getCryptographicServiceMessage())
                    .build();

            KEKDTO kek = KEKDTO.builder()
                    .kekId(kekId)
                    .build();

            RecipientDTO recipient = RecipientDTO.builder()
                    .kek(kek)
                    .build();

            List<RecipientDTO> recipientList = new ArrayList<>();
            recipientList.add(recipient);

            EnvelopedDataDTO envelopedData = EnvelopedDataDTO.builder()
                    .recipient(recipientList)
                    .build();

            ProtectedDataDTO protectedData = ProtectedDataDTO.builder()
                    .envelopedData(envelopedData)
                    .build();

            List<ProtectedDataDTO> protectedDataList = new ArrayList<>();
            protectedDataList.add(protectedData);

            /*----------------------------------------
            * SECURITY TRAILER SECTION
            ----------------------------------------*/

            MacDataDTO macData=null;
            SecurityTrailerDTO securityTrailer = null;
            if(inputObject.getSecurityControlInformation()!=null) {
                String secControlInformation = inputObject.getSecurityControlInformation();

                // ======== FIELD 53 (SECURITY RELATED CONTROL INFORMATION) ========
                macData = MacDataDTO.builder()
                        // 53.1 (SECURITY FORMAT CODE / SECURITY TYPE CODE)
                        .keyProtection(isNullOrEmptySubstring(secControlInformation, 0, 2))
                        // 53.2 (PIN ENCRYPTION CODE)
                        .algorithm(isNullOrEmptySubstring(secControlInformation, 2, 4))
                        // 53.3 (PIN BLOCK FORMAT CODE)
                        .derivedInformation(isNullOrEmptySubstring(secControlInformation, 4, 6))
                        // 53.4 (KEY INDEX NUMBER)
                        .keyIndex(isNullOrEmptySubstring(secControlInformation, 6, secControlInformation.length()))
                        .build();

            }

            securityTrailer = SecurityTrailerDTO.builder()
                    .macData(macData)
                    .build();

            /*----------------------------------------
            * ADDENDUM DATA SECTION
            ----------------------------------------*/

            // ======== ORIGINAL ISO 8583 ========
            AdditionalDataDTO iso8583AdditionalDataHost = AdditionalDataDTO.builder()
                    .key("ISO8583_HOST")
                    .value(inputObject.getOriginalMessage())//0100C2200000820000020400000000000000050868903111550000750000600868900009MCCW9U1JV061
                    .build();



            AdditionalDataDTO iso8583AdditionalData = AdditionalDataDTO.builder()
                        .key("ISO8583")
                        .value(ISO8583Context.getISO8583(GrpcHeadersInfo.getTraceId()))
                        .build();



            // ======== MSGTYPE ========
            AdditionalDataDTO iso8583AdditionalDataMsgtype = AdditionalDataDTO.builder()
                    .key("MSGTYPE")
                    .value(inputObject.getMessageType())//0100
                    .build();

            List<AdditionalDataDTO> addendrumDataList = new ArrayList<>();

            if(flag) {
                addendrumDataList.add(iso8583AdditionalData);
            }
            addendrumDataList.add(iso8583AdditionalDataMsgtype);
            addendrumDataList.add(iso8583AdditionalDataHost);

            AddendumDataDTO addendumData = AddendumDataDTO
                    .builder()
                    .additionalData(addendrumDataList)
                    .build();

            // ======== MONITORING ========

            FieldDto fieldDto = new FieldDto();
            fieldDto.setNetworkName(GrpcHeadersInfo.getNetwork());
            fieldDto.setECommerceIndicator(context.getPointOfServiceContext().getEcommerceIndicator());
            fieldDto.setTerminalKey(environment.getTerminal().getKey());
            fieldDto.setProcessingCode(transaction.getTransactionType());

            String isoMessage = ISO8583Context.getISO8583(GrpcHeadersInfo.getTraceId());
            String transactionReference = transaction.getTransactionId().getTransactionReference();
            LogsTraces.writeInfo(String.format("requestMessage %s|%s", isoMessage, transactionReference));

            if (flag){
                return ISO20022.builder()
                        .networkName(GrpcHeadersInfo.getNetwork())
                        // Cambio de tipo de mensaje hacia uno interno de gateway
                        .messageFunction(FieldLocalCodeMapper.getCode(TYPE_MESSAGE, "in", inputObject.getMessageType(), GrpcHeadersInfo.getNetwork()))
                        .socketPort(GrpcHeadersInfo.getPort())
                        .traceData(traceDataList)
                        .transaction(transaction)
                        .environment(environment)
                        .context(context)
                        .processingResult(ProcessingResult.createProcessingResult(inputObject))
                        .securityTrailer(securityTrailer)
                        // ======== FIELD 55 (ICC RELATED DATA) ========
                        .iccRelatedData(inputObject.getIntegratedCircuitCard())
                        .protectedData(protectedDataList)
                        .supplementaryData(supplementaryDataList)
                        .addendumData(addendumData)
                        .monitoring(ProcessMonitoring.createMonitoring(inputObject, fieldDto))
                        .mappingMetadata(Metadata.createMetadata(inputObject))
                        .build();
            }else{

                TransactionIdDTO transactionIdReturn = TransactionIdDTO.builder()
                        .transactionReference(transactionReference)
                        .build();

                TransactionDTO transactionReturn = TransactionDTO.builder()
                        .transactionId(transactionIdReturn)
                        .build();

                return ISO20022.builder()
                        .networkName(GrpcHeadersInfo.getNetwork())
                        // Cambio de tipo de mensaje hacia uno interno de gateway
                        .messageFunction(FieldLocalCodeMapper.getCode(TYPE_MESSAGE, "in", inputObject.getMessageType(), GrpcHeadersInfo.getNetwork()))
                        .socketPort(GrpcHeadersInfo.getPort())
                        .processingResult(ProcessingResult.createProcessingResult(inputObject))
                        .addendumData(addendumData)
                        .monitoring(ProcessMonitoring.createMonitoring(inputObject, fieldDto))
                        .transaction(transactionReturn)
                        .mappingMetadata(Metadata.createMetadata(inputObject))
                        .build();
            }


        } catch (Exception e) {
            LogsTraces.writeWarning("Error on request: " + e);

            // ======== ORIGINAL ISO 8583 ========
            AdditionalDataDTO iso8583AdditionalData = AdditionalDataDTO.builder()
                    .key("ISO8583_HOST")
                    .value(inputObject.getOriginalMessage())
                    .build();

            // ======== MSGTYPE ========
            AdditionalDataDTO iso8583AdditionalDataMsgtype = AdditionalDataDTO.builder()
                    .key("MSGTYPE")
                    .value(inputObject.getMessageType())
                    .build();

            List<AdditionalDataDTO> addendrumDataList = new ArrayList<>();
            addendrumDataList.add(iso8583AdditionalData);
            addendrumDataList.add(iso8583AdditionalDataMsgtype);

            AddendumDataDTO addendumData = AddendumDataDTO
                    .builder()
                    .additionalData(addendrumDataList)
                    .build();

            CardDTO card = CardDTO.builder()
                    .pan(inputObject.getPrimaryAccountNumber())
                    .build();

            EnvironmentDTO environment = EnvironmentDTO.builder()
                    .card(card)
                    .build();

            return ISO20022.builder()
                    .networkName(GrpcHeadersInfo.getNetwork())
                    .messageFunction(FieldLocalCodeMapper.getCode(TYPE_MESSAGE, "in", inputObject.getMessageType(), GrpcHeadersInfo.getNetwork()))
                    .socketPort(GrpcHeadersInfo.getPort())
                    .environment(environment)
                    .addendumData(addendumData).build();
        }

    }


    private static String isNullOrEmptySubstring(String field, int start, int end) {
        if (field == null || field.isEmpty()) {
            return "";
        } else if (field.length() < end) {
            return "";
        } else {
            return field.substring(start, end);
        }
    }

    private static Double parseDouble(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new ParserException("Error parsing double value: " + value);
        }
    }


    public static void main(String[] args) {
        //String valorOriginal="72802343";
        //String valorOriginal="61000000";
        String valorOriginal="60000100";

        String res="0.2802343"; //72802343 y no esto 70280234
        System.out.println("Valor original : " +valorOriginal);
        Double resultadoDouble = conversionRateValidationOrigin(valorOriginal);
        BigDecimal resultado = conversionRateValidation(valorOriginal);
        System.out.println("Valor convertido double : " +resultadoDouble);
        System.out.println("Valor experado : " + valorOriginal + " reconversionDouble : " + getConversionRate(resultadoDouble,8));
        System.out.println("-------------------------------------------");
        System.out.println("Valor convertido bigdecimal : " +resultado);
        System.out.println("Valor experado : " + valorOriginal + " reconversionBigdecimal : " + getOriginalConversionRate(resultado));


    }

    public static String getOriginalConversionRate(BigDecimal resultado) {
        if (resultado == null) return null;
        String str = resultado.toPlainString();
        int index = str.indexOf(".");
        int precision = (index < 0) ? 0 : str.length() - index - 1;
        String digits = str.replace(".", "");
        // Si empieza con '0' y la precisión es 7, reemplaza el primer dígito por la precisión
        if (str.startsWith("0.") && precision == 7) {
            digits = precision + digits.substring(1);
            return digits;
        }
        /*if (str.startsWith("0.") && precision >= 1 && precision <= 7) {
            return precision + digits.substring(1);
        }*/
        return precision + digits;
    }

    public static String getOriginalConversionRate3(BigDecimal resultado) {
        if (resultado == null) return null;
        String str = resultado.toString();
        int index = str.indexOf(".");
        int precision = (index < 0) ? 0 : str.length() - index - 1;
        String sinPunto = str.replace(".", "");
        return precision + sinPunto;
    }

    private static String getConversionRate(Double value, int padding) {
        if (value == null) {
            return "";
        }

        StringBuilder conversionRate = new StringBuilder(value.toString());
        while (conversionRate.length() < padding) {
            conversionRate.append("0");
        }

        int dotPosition = conversionRate.indexOf(".");
        int precision = conversionRate.length() - (dotPosition + 1);
        conversionRate.deleteCharAt(dotPosition);
        conversionRate.insert(0, precision);
        return conversionRate.toString();
    }

    private static BigDecimal conversionRateValidation(String value) {
        if (value == null || value.length() < 2) {
            throw new ParserException("El valor de la tasa de conversión es nulo o demasiado corto: " + value);
        }

        int precision = Character.getNumericValue(value.charAt(0));
        String digits = value.substring(1);

        if (precision < 0 || precision > 7 || precision > digits.length()) {
            throw new ParserException("Precisión inválida en la tasa de conversión: " + value);
        }

        StringBuilder sb = new StringBuilder(digits);
        sb.insert(sb.length() - precision, '.');
        try {
            return new BigDecimal(sb.toString());
        } catch (NumberFormatException e) {
            throw new ParserException("Error al parsear la tasa de conversión: " + value);
        }
    }


    private static BigDecimal conversionRateValidation3(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        StringBuilder conversionRate = new StringBuilder(value);
        int precision = Integer.parseInt(conversionRate.substring(0, 1));

        if (precision > 7 || precision < 0) {
            throw new ParserException("Error parsing exchange rate value: " + value);
        }

        conversionRate.deleteCharAt(0);
        conversionRate.insert(conversionRate.length() - precision, ".");
        return new BigDecimal(conversionRate.toString());
    }


    private static Double conversionRateValidationOrigin(String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }

        StringBuilder conversionRate = new StringBuilder(value);
        int precision = Integer.parseInt(conversionRate.substring(0, 1));

        if (precision > 7 || precision < 0) {
            throw new ParserException("Error parsing exchange rate value: " + value);
        }

        conversionRate.deleteCharAt(0);
        conversionRate.insert(conversionRate.length() - precision, ".");
        return Double.parseDouble(conversionRate.toString());
    }

    public static String convertFormatDateTime(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            int year = LocalDate.now().getYear(); // year of the system
            String fullDate = year + value;  // yyyyMMddHHmmss

            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            LocalDateTime localDateTime = LocalDateTime.parse(fullDate, inputFormatter);
            Instant instant = localDateTime.atZone(ZoneOffset.UTC).toInstant();

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

            return outputFormatter.withZone(ZoneOffset.UTC).format(instant);
        } catch (Exception e) {
            return null;
        }
    }

    public static String convertFormatExpiryDate(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMM");
            YearMonth yearMonth = YearMonth.parse(value, formatter);
            return yearMonth.toString();

        } catch (Exception e) {
            return null;
        }
    }

}
