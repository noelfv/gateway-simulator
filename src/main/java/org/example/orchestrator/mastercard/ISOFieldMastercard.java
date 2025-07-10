package org.example.orchestrator.mastercard;

import lombok.Getter;
import org.example.orchestrator.common.ISODataType;
import org.example.orchestrator.common.ISOField;
import org.example.orchestrator.common.ISOUtil;

import java.util.HashMap;
import java.util.Map;

import static org.example.orchestrator.common.ISODataType.HEXADECIMAL;


@Getter
public enum ISOFieldMastercard implements ISOField {

    MESSAGE_TYPE(0, "messageType", ISODataType.NUMERIC, false, 4),
    BITMAP_PRIMARY(0, "bitmap_primary", ISODataType.BINARY_STRING, false, 16),
    BITMAP_SECONDARY(1, "bitmap_secondary", ISODataType.BINARY_STRING, false, 16),
    PRIMARY_ACCOUNT_NUMBER(2, "primaryAccountNumber", ISODataType.NUMERIC, true, 2),
    PROCESSING_CODE(3, "processingCode", ISODataType.NUMERIC, false, 6),
    TRANSACTION_AMOUNT(4, "transactionAmount", ISODataType.NUMERIC_DECIMAL, false, 12),
    SETTLEMENT_AMOUNT(5, "settlementAmount", ISODataType.NUMERIC, false, 12),
    CARD_HOLDER_BILLING_AMOUNT(6, "cardHolderBillingAmount", ISODataType.NUMERIC_DECIMAL, false, 12),
    TRANSMISSION_DATE_TIME(7, "transmissionDateTime", ISODataType.NUMERIC, false, 10),
    AMOUNT_CARD_HOLDER_BILLING_FEE(8, "amountCardholderBillingFee", ISODataType.NUMERIC, false, 8),
    CONVERSION_RATE_SETTLEMENT(9,"conversionRateSettlement",ISODataType.NUMERIC,false,8),
    CONVERSION_RATE(10, "conversionRate", ISODataType.NUMERIC, false, 8),
    SYSTEM_TRACE_AUDIT_NUMBER(11, "systemTraceAuditNumber", ISODataType.NUMERIC, false, 6),
    LOCAL_TRANSACTION_TIME(12, "localTransactionTime", ISODataType.NUMERIC, false, 6),
    LOCAL_TRANSACTION_DATE(13, "localTransactionDate", ISODataType.NUMERIC, false, 4),
    DATE_EXPIRATION(14, "dateExpiration", ISODataType.NUMERIC, false, 4),
    SETTLEMENT_DATE(15, "settlementDate", ISODataType.NUMERIC, false, 4),
    DATE_CONVERSION(16,"dateConversion",ISODataType.NUMERIC,false,4),
    CAPTURE_DATE(17, "captureDate", ISODataType.NUMERIC, false, 4),
    MERCHANT_TYPE(18, "merchantType", ISODataType.NUMERIC, false, 4),
    ACQUIRER_COUNTRY_CODE(19, "acquirerCountryCode", ISODataType.NUMERIC, false, 3),
    PAN_COUNTRY_CODE(20, "primaryAccountNumberCountryCode", ISODataType.NUMERIC, false, 3),
    FORWARDING_INSTITUTION_COUNTRY_CODE(21, "forwardingInstitutionCountryCode", ISODataType.NUMERIC, false, 3),
    POINT_SERVICE_ENTRY_MODE(22, "pointServiceEntryMode", ISODataType.NUMERIC, false, 3),
    CARD_SEQUENCE_NUMBER(23,"cardSequenceNumber",ISODataType.NUMERIC,false,3),
    NETWORK_INTERNATIONAL_ID(24,"networkInternationalId",ISODataType.NUMERIC,false,3),
    POINT_SERVICE_CONDITION_CODE(25, "pointServiceConditionCode", ISODataType.NUMERIC, false, 2),
    POINT_SERVICE_PERSONAL_ID_NUMBER(26, "pointServicePIN", ISODataType.NUMERIC, false, 2),
    AUTHORIZATION_ID_RESPONSE_LENGTH(27, "authorizationIdResponseLength", ISODataType.NUMERIC, false, 1),
    AMOUNT_TRANSACTION_FEE(28, "amountTransactionFee", ISODataType.ALPHA_NUMERIC, false, 9),
    AMOUNT_SETTLEMENT_FEE(29, "amountSettlementFee", ISODataType.ALPHA_NUMERIC, false, 9),
    AMOUNT_TRANSACTION_PROCESSING_FEE(30, "amountTransactionProcessingFee", ISODataType.ALPHA_NUMERIC, false, 9),
    AMOUNT_SETTLEMENT_PROCESSING_FEE(31, "amountSettlementProcessingFee", ISODataType.ALPHA_NUMERIC, false, 9),
    ACQUIRING_INSTITUTION_IDENTIFICATION_CODE(32, "acquiringInstitutionIdentificationCode", ISODataType.NUMERIC, true, 2),
    FORWARDING_INSTITUTION_IDENTIFICATION_CODE(33, "forwardingInstitutionIdentificationCode", ISODataType.NUMERIC, true, 2),
    PAN_EXTENDED(34, "primaryAccountNumberExtended", ISODataType.ALPHA_NUMERIC, true, 2),
    TRACK_TWO_DATA(35, "trackTwoData", ISODataType.ALPHA_NUMERIC, true, 2),
    TRACK_THREE_DATA(36, "trackThreeData", ISODataType.ALPHA_NUMERIC, true, 2),
    RETRIEVAL_REFERENCE_NUMBER(37, "retrievalReferenceNumber", ISODataType.ALPHA_NUMERIC, false, 12),
    AUTHORIZATION_IDENTIFICATION_RESPONSE(38, "authorizationIdentificationResponse", ISODataType.ALPHA_NUMERIC, false, 6),
    RESPONSE_CODE(39, "responseCode", ISODataType.ALPHA_NUMERIC, false, 2),
    SERVICE_RESTRICTION_CODE(40, "serviceRestrictionCode", ISODataType.ALPHA_NUMERIC, false, 3),
    CARD_ACCEPTOR_TERMINAL_IDENTIFICATION(41, "cardAcceptorTerminalIdentification", ISODataType.ALPHA_NUMERIC, false, 8),
    CARD_ACCEPTOR_IDENTIFICATION_CODE(42, "cardAcceptorIdentificationCode", ISODataType.ALPHA_NUMERIC, false, 15),
    CARD_ACCEPTOR_NAME_LOCATION(43, "cardAcceptorNameLocation", ISODataType.ALPHA_NUMERIC, false, 40),
    ADDITIONAL_RESPONSE_DATA(44, "additionalResponseData", ISODataType.ALPHA_NUMERIC, true, 2),
    TRACK_ONE_DATA(45, "trackOneData", ISODataType.ALPHA_NUMERIC, true, 2),
    EXPANDED_ADDITIONAL_AMOUNTS(46, "expandedAdditionalAmounts", ISODataType.ALPHA_NUMERIC, true, 3),
    ADDITIONAL_DATA_NATIONAL_USE_2(47, "additionalDataNationalUse2", ISODataType.ALPHA_NUMERIC, true, 3),
    ADDITIONAL_DATA_RETAILER(48, "additionalDataRetailer", ISODataType.ALPHA_NUMERIC, true, 3),
    TRANSACTION_CURRENCY_CODE(49, "transactionCurrencyCode", ISODataType.NUMERIC, false, 3),
    SETTLEMENT_CURRENCY_CODE(50, "settlementCurrencyCode", ISODataType.NUMERIC, false, 3),
    CARD_HOLDER_BILLING_CURRENCY_CODE(51, "cardholderBillingCurrencyCode", ISODataType.NUMERIC, false, 3),
    PIN_DATA(52, "pinData", HEXADECIMAL, false, 8),
    SECURITY_CONTROL_INFORMATION(53, "securityControlInformation", ISODataType.NUMERIC, false, 16),
    ADDITIONAL_AMOUNTS(54, "additionalAmounts", ISODataType.ALPHA_NUMERIC, true, 3),
    INTEGRATED_CIRCUIT_CARD(55,"integratedCircuitCard", HEXADECIMAL,true,3),
    PAYMENT_ACCOUNT_DATA(56,"paymentAccountData",ISODataType.ALPHA_NUMERIC,true,3),
    REDEMPTION_POINTS(58, "redemptionPoints", ISODataType.ALPHA_NUMERIC, true, 3),
    CAMPAIGN_DATA(59, "campaignData", ISODataType.ALPHA_NUMERIC, true, 3),
    POS_TERMINAL_DATA(60, "posTerminalData", ISODataType.ALPHA_NUMERIC, true, 3),
    POS_CARD_ISSUER(61, "posCardIssuer", ISODataType.ALPHA_NUMERIC, true, 3),
    POSTAL_CODE(62, "postalCode", ISODataType.ALPHA_NUMERIC, true, 3),
    NETWORK_DATA(63, "networkData", ISODataType.ALPHA_NUMERIC, true, 3),
    MESSAGE_AUTHENTICATION_CODE(64, "messageAuthenticationCode", HEXADECIMAL, false, 8),
    SETTLEMENT_CODE(66,"settlementCode",ISODataType.NUMERIC,false,1),
    EXTENDED_PAYMENT_CODE(67,"extendedPaymentCode",ISODataType.NUMERIC,false,2),
    RECEIVING_INSTITUTION_COUNTRY_CODE(68, "receivingInstitutionCountryCode", ISODataType.NUMERIC, false, 3),
    SETTLEMENT_INSTITUTION_COUNTRY_CODE(69, "settlementInstitutionCountryCode", ISODataType.NUMERIC, false, 3),
    NETWORK_MANAGEMENT_INFORMATION_CODE(70, "networkManagementInformationCode", ISODataType.NUMERIC, false, 3),
    MESSAGE_NUMBER(71, "messageNumber", ISODataType.NUMERIC, false, 4),
    MESSAGE_NUMBER_LAST(72, "messageNumberLast", ISODataType.NUMERIC, false, 4),
    DATE_ACTION(73, "dateAction", ISODataType.NUMERIC, false, 6),
    CREDITS_NUMBER(74, "creditsNumber", ISODataType.NUMERIC, false, 10),
    CREDITS_REVERSAL_NUMBER(75, "creditsReversalNumber", ISODataType.NUMERIC, false, 10),
    DEBITS_NUMBER(76, "debitsNumber", ISODataType.NUMERIC, false, 10),
    DEBITS_REVERSAL_NUMBER(77, "debitsReversalNumber", ISODataType.NUMERIC, false, 10),
    TRANSFER_NUMBER(78, "transferNumber", ISODataType.NUMERIC, false, 10),
    TRANSFER_REVERSAL_NUMBER(79, "transferReversalNumber", ISODataType.NUMERIC, false, 10),
    INQUIRIES_NUMBER(80, "inquiriesNumber", ISODataType.NUMERIC, false, 10),
    AUTHORIZATION_NUMBER(81, "authorizationNumber", ISODataType.NUMERIC, false, 10),
    CREDITS_PROCESSING_FEE_AMOUNT(82, "creditsProcessingFeeAmount", ISODataType.NUMERIC, false, 12),
    CREDITS_TRANSACTION_FEE_AMOUNT(83, "creditsTransactionFeeAmount", ISODataType.NUMERIC, false, 12),
    DEBITS_PROCESSING_FEE_AMOUNT(84, "debitsProcessingFeeAmount", ISODataType.NUMERIC, false, 12),
    DEBITS_TRANSACTION_FEE_AMOUNT(85, "debitsTransactionFeeAmount", ISODataType.NUMERIC, false, 12),
    CREDITS_AMOUNT(86, "creditsAmount", ISODataType.NUMERIC, false, 16),
    CREDITS_REVERSAL_AMOUNT(87, "creditsReversalAmount", ISODataType.NUMERIC, false, 16),
    DEBITS_AMOUNT(88, "debitsAmount", ISODataType.NUMERIC, false, 16),
    DEBITS_REVERSAL_AMOUNT(89, "debitsReversalAmount", ISODataType.NUMERIC, false, 16),
    ORIGINAL_DATA_ELEMENTS(90, "originalDataElements", ISODataType.NUMERIC, false, 42),
    ISSUER_FILE_UPDATE_CODE(91, "issuerFileUpdateCode", ISODataType.ALPHA_NUMERIC, false, 1),
    FILE_SECURITY_CODE(92, "fileSecurityCode", ISODataType.ALPHA_NUMERIC, false, 2),
    RESPONSE_INDICATOR(93, "responseIndicator", ISODataType.NUMERIC, false, 5),
    SERVICE_INDICATOR(94,"serviceIndicator",ISODataType.ALPHA_NUMERIC,false,7),
    REPLACEMENT_AMOUNTS(95, "replacementAmounts", ISODataType.NUMERIC, false, 42),
    MESSAGE_SECURITY_CODE(96,"messageSecurityCode",ISODataType.NUMERIC,false,8),
    AMOUNT_NET_SETTLEMENT(97, "amountNetSettlement", ISODataType.ALPHA_NUMERIC, false, 17),
    PAYEE(98, "payee", ISODataType.ALPHA_NUMERIC, false, 25),
    SETTLEMENT_INSTITUTION_IDENTIFICATION_CODE(99, "settlementInstitutionIdentificationCode", ISODataType.NUMERIC, true, 2),
    RECEIVING_INSTITUTION_IDENTIFICATION_CODE(100, "receivingInstitutionIdentificationCode", ISODataType.NUMERIC, true, 2),
    FILE_NAME(101, "fileName", ISODataType.ALPHA_NUMERIC, true, 2),
    ACCOUNT_IDENTIFICATION_1(102, "accountIdentification1", ISODataType.ALPHA_NUMERIC, true, 2),
    ACCOUNT_IDENTIFICATION(103, "accountIdentification", ISODataType.ALPHA_NUMERIC, true, 2),
    TRANSACTION_DATA(104, "transactionData",ISODataType.ALPHA_NUMERIC,true,3),
    DOUBLE_LENGTH_DES_KEY(105, "doubleLengthDesKey", ISODataType.ALPHA_NUMERIC, true, 3),
    FLEET_SERVICE_DATA(106, "fleetServiceData", ISODataType.ALPHA_NUMERIC, true, 3),
    ADDITIONAL_TRANSACTION_REFERENCE_DATA(108, "additionalTransactionReferenceData", ISODataType.ALPHA_NUMERIC, true, 3),
    ISO_USE(109, "isoUse", ISODataType.ALPHA_NUMERIC, true, 3),
    ENCRYPTION_DATA(110, "encryptionData", HEXADECIMAL, true, 3),
    ADDITIONAL_DATA_NATIONAL_USE(112, "additionalDataNationalUse", ISODataType.ALPHA_NUMERIC, true, 3),
    RESERVED_NATIONAL_USE(115, "reservedNationalUse", ISODataType.ALPHA_NUMERIC, true, 3),
    RESERVED_NATIONAL_USE_2(119, "reservedNationalUse2", ISODataType.ALPHA_NUMERIC, true, 3),
    KEY_MANAGEMENT(120, "keyManagement", ISODataType.ALPHA_NUMERIC, true, 3),
    AUTHORIZING_AGENT_ID_CODE(121,"authorizingAgentIdCode",ISODataType.NUMERIC,true,3),
    ADDITIONAL_RECORD_DATA(122,"additionalRecordData",ISODataType.ALPHA_NUMERIC,true,3),
    CRYPTOGRAPHIC_SERVICE_MESSAGE(123, "cryptographicServiceMessage", ISODataType.ALPHA_NUMERIC, true, 3),
    INFO_TEXT(124, "infoText", ISODataType.ALPHA_NUMERIC, true, 3),
    SETTLEMENT_DATA(125, "settlementData", HEXADECIMAL, false, 8),
    ISSUER_TRACE_ID(126, "issuerTraceId", ISODataType.ALPHA_NUMERIC, true, 3),
    PRIVATE_DATA(127, "privateData", ISODataType.ALPHA_NUMERIC, true, 3),
    MESSAGE_AUTHENTICATION_CODE_2(128, "messageAuthenticationCode2", HEXADECIMAL, false, 8);


    private final int id;
    private final String name;
    private final ISODataType typeData;
    private final boolean isVariable;
    private final int length;

    private static final Map<Integer, ISOField> ISO_MAP = new HashMap<>();

    static {
        for (ISOFieldMastercard field : ISOFieldMastercard.values()) {
            ISO_MAP.put(field.id, field);
        }
    }

    ISOFieldMastercard(int id, String name, ISODataType typeData, boolean isVariable, int length) {
        this.id = id;
        this.name = name;
        this.typeData = typeData;
        this.isVariable = isVariable;
        this.length = length;
    }

    public static ISOField getById(int id) {
        return ISO_MAP.get(id);
    }

    @Override
    public String parseISOValue(ISOField isoField, String value, boolean clean) {
        StringBuilder originalField = new StringBuilder();
        if (isoField.isVariable()) {

            if (!clean){
                originalField.append(
                        ISOUtil.stringToEBCDICHex(
                                ISOUtil.convertToNumericString(isoField.getTypeData().equals(HEXADECIMAL) ? value.length()/2 : value.length() , 10, isoField.getLength()))
                );
            }else {
                originalField.append(ISOUtil.convertToNumericString(value.length() , 10, isoField.getLength()));
            }

        }

        return clean ? processValueClean(originalField, isoField, value) : processValue(originalField, isoField, value);
    }

    private static String processValue(StringBuilder originalField, ISOField isoField, String value) {

        switch (isoField.getTypeData()) {
            case ALPHA_NUMERIC, NUMERIC -> {
                value = ISOUtil.stringToEBCDICHex(value);
            }
            case NUMERIC_DECIMAL -> {
                value = ISOUtil.revertValidAmount(value);
                value = ISOUtil.stringToEBCDICHex(value);
            }
            case BINARY_STRING -> {
                value = ISOUtil.convertBITMAPtoHEX(value);
                value = ISOUtil.stringToEBCDICHex(value);
            }
            default -> {
            }
        }

        return originalField.append(value).toString();
    }

    private static String processValueClean(StringBuilder originalField, ISOField isoField, String value){
        switch (isoField.getTypeData()) {
            case NUMERIC_DECIMAL -> value = ISOUtil.revertValidAmount(value);
            case BINARY_STRING -> value = ISOUtil.convertBITMAPtoHEX(value);
            default -> {
            }
        }
        return originalField.append(value).toString();
    }


}