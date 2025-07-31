package com.bbva.orchestrator.network.visa;

import com.bbva.orchestrator.parser.common.ISODataType;
import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchestrator.parser.common.ISOUtil;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum ISOFieldVisa implements ISOField {
    HEADER(0, "header", ISODataType.HEXADECIMAL, true, 2),
    MESSAGE_TYPE(0, "messageType", ISODataType.NUMERIC, false, 4),
    BITMAP_PRIMARY(0, "bitmap_primary", ISODataType.BINARY_STRING, false, 16),
    BITMAP_SECONDARY(1, "bitmap_secondary", ISODataType.BINARY_STRING, false, 16),
    PRIMARY_ACCOUNT_NUMBER(2, "primaryAccountNumber", ISODataType.NUMERIC, true, 2),
    PROCESSING_CODE(3, "processingCode", ISODataType.NUMERIC, false, 6),
    TRANSACTION_AMOUNT(4, "transactionAmount", ISODataType.NUMERIC_DECIMAL, false, 12),
    SETTLEMENT_AMOUNT(5, "settlementAmount", ISODataType.NUMERIC, false, 12),
    CARD_HOLDER_BILLING_AMOUNT(6, "cardHolderBillingAmount", ISODataType.NUMERIC_DECIMAL, false, 12),
    TRANSMISSION_DATE_TIME(7, "transmissionDateTime", ISODataType.NUMERIC, false, 10),
    CONVERSION_RATE_SETTLEMENT(9, "conversionRateSettlement", ISODataType.NUMERIC, false, 8),
    CONVERSION_RATE(10, "conversionRate", ISODataType.NUMERIC, false, 8),
    SYSTEM_TRACE_AUDIT_NUMBER(11, "systemTraceAuditNumber", ISODataType.NUMERIC, false, 6),
    LOCAL_TRANSACTION_TIME(12, "localTransactionTime", ISODataType.NUMERIC, false, 6),
    LOCAL_TRANSACTION_DATE(13, "localTransactionDate", ISODataType.NUMERIC, false, 4),
    DATE_EXPIRATION(14, "dateExpiration", ISODataType.NUMERIC, false, 4),
    SETTLEMENT_DATE(15, "settlementDate", ISODataType.NUMERIC, false, 4),
    DATE_CONVERSION(16, "dateConversion", ISODataType.NUMERIC, false, 4),
    CAPTURE_DATE(17, "captureDate", ISODataType.ALPHA_NUMERIC, false, 4),
    MERCHANT_TYPE(18, "merchantType", ISODataType.NUMERIC, false, 4),
    ACQUIRER_COUNTRY_CODE(19, "acquirerCountryCode", ISODataType.NUMERIC, false, 3),
    POINT_SERVICE_ENTRY_MODE(22, "pointServiceEntryMode", ISODataType.NUMERIC, false, 4),
    CARD_SEQUENCE_NUMBER(23, "cardSequenceNumber", ISODataType.NUMERIC, false, 3),
    POINT_SERVICE_CONDITION_CODE(25, "pointServiceConditionCode", ISODataType.NUMERIC, false, 2),
    AMOUNT_TRANSACTION_FEE(28, "amountTransactionFee", ISODataType.ALPHA_NUMERIC, false, 9),
    ACQUIRING_INSTITUTION_IDENTIFICATION_CODE(32, "acquiringInstitutionIdentificationCode", ISODataType.NUMERIC, true, 2),
    FORWARDING_INSTITUTION_IDENTIFICATION_CODE(33, "forwardingInstitutionIdentificationCode", ISODataType.NUMERIC, true, 2),
    TRACK_TWO_DATA(35, "trackTwoData", ISODataType.NUMERIC, true, 2),
    RETRIEVAL_REFERENCE_NUMBER(37, "retrievalReferenceNumber", ISODataType.ALPHA_NUMERIC, false, 12),
    AUTHORIZATION_IDENTIFICATION_RESPONSE(38, "authorizationIdentificationResponse", ISODataType.ALPHA_NUMERIC, false, 6),
    RESPONSE_CODE(39, "responseCode", ISODataType.ALPHA_NUMERIC, false, 2),
    SERVICE_RESTRICTION_CODE(40, "serviceRestrictionCode", ISODataType.ALPHA_NUMERIC, false, 3),
    CARD_ACCEPTOR_TERMINAL_IDENTIFICATION(41, "cardAcceptorTerminalIdentification", ISODataType.ALPHA_NUMERIC, false, 8),
    CARD_ACCEPTOR_IDENTIFICATION_CODE(42, "cardAcceptorIdentificationCode", ISODataType.ALPHA_NUMERIC, false, 15),
    CARD_ACCEPTOR_NAME_LOCATION(43, "cardAcceptorNameLocation", ISODataType.ALPHA_NUMERIC, false, 40),
    ADDITIONAL_RESPONSE_DATA(44, "additionalResponseData", ISODataType.ALPHA_NUMERIC, true, 2),
    TRACK_ONE_DATA(45, "trackOneData", ISODataType.ALPHA_NUMERIC, true, 2),
    ADDITIONAL_DATA_RETAILER(48, "additionalDataRetailer", ISODataType.HEXADECIMAL, true, 2),
    TRANSACTION_CURRENCY_CODE(49, "transactionCurrencyCode", ISODataType.NUMERIC, false, 3),
    SETTLEMENT_CURRENCY_CODE(50, "settlementCurrencyCode", ISODataType.NUMERIC, false, 3),
    CARD_HOLDER_BILLING_CURRENCY_CODE(51, "cardholderBillingCurrencyCode", ISODataType.NUMERIC, false, 3),
    PIN_DATA(52, "pinData", ISODataType.BINARY_STRING, false, 16),
    SECURITY_CONTROL_INFORMATION(53, "securityControlInformation", ISODataType.NUMERIC, false, 16),
    ADDITIONAL_AMOUNTS(54, "additionalAmounts", ISODataType.ALPHA_NUMERIC, true, 2),
    INTEGRATED_CIRCUIT_CARD(55, "integratedCircuitCard", ISODataType.HEXADECIMAL, true, 2),
    PAYMENT_ACCOUNT_DATA(56, "paymentAccountData", ISODataType.HEXADECIMAL, true, 3),
    REDEMPTION_POINTS(58, "redemptionPoints", ISODataType.ALPHA_NUMERIC, true, 2),
    CAMPAIGN_DATA(59, "campaignData", ISODataType.ALPHA_NUMERIC, true, 2),
    POS_TERMINAL_DATA(60, "posTerminalData", ISODataType.HEXADECIMAL, true, 2),
    POS_CARD_ISSUER(61, "posCardIssuer", ISODataType.NUMERIC, true, 2),
    POSTAL_CODE(62, "postalCode", ISODataType.HEXADECIMAL, true, 2),
    NETWORK_DATA(63, "networkData", ISODataType.HEXADECIMAL, true, 2),
    NETWORK_MANAGEMENT_INFORMATION_CODE(70, "networkManagementInformationCode", ISODataType.NUMERIC, false, 3),
    ORIGINAL_DATA_ELEMENTS(90, "originalDataElements", ISODataType.NUMERIC, false, 42),
    SERVICE_INDICATOR(94, "serviceIndicator", ISODataType.ALPHA_NUMERIC, false, 7),
    MESSAGE_SECURITY_CODE(96, "messageSecurityCode", ISODataType.HEXADECIMAL, false, 8),
    ACCOUNT_IDENTIFICATION(103, "accountIdentification", ISODataType.ALPHA_NUMERIC, true, 2),
    TRANSACTION_DATA(104, "transactionData", ISODataType.HEXADECIMAL, true, 2),
    DOUBLE_LENGTH_DES_KEY(105, "doubleLengthDesKey", ISODataType.BINARY_STRING, false, 32),
    ENCRYPTION_DATA(110, "encryptionData", ISODataType.HEXADECIMAL, true, 3),
    KEY_MANAGEMENT(120, "keyManagement", ISODataType.HEXADECIMAL, true, 3),
    ADDITIONAL_RECORD_DATA(122, "additionalRecordData", ISODataType.HEXADECIMAL, true, 3),
    CRYPTOGRAPHIC_SERVICE_MESSAGE(123, "cryptographicServiceMessage", ISODataType.HEXADECIMAL, true, 2),
    INFO_TEXT(124, "infoText", ISODataType.ALPHA_NUMERIC, true, 3),
    SETTLEMENT_DATA(125, "settlementData", ISODataType.HEXADECIMAL, true, 2),
    ISSUER_TRACE_ID(126, "issuerTraceId", ISODataType.HEXADECIMAL, true, 2);

    private final int id;
    private final String name;
    private final ISODataType typeData;
    private final boolean isVariable;
    private final int length;

    private static final Map<Integer, ISOField> ISO_MAP = new HashMap<>();

    static {
        for (ISOFieldVisa field : ISOFieldVisa.values()) {
            ISO_MAP.put(field.id, field);
        }
    }

    ISOFieldVisa(int id, String name, ISODataType typeData, boolean isVariable, int length) {
        this.id = id;
        this.name = name;
        this.typeData = typeData;
        this.isVariable = isVariable;
        this.length = length;
    }
    @Override
    public String parseISOValue(ISOField isoField, String value, boolean clean) {

        StringBuilder originalField = new StringBuilder();
        if (isoField.isVariable()) {
            if (isoField.getTypeData() == ISODataType.HEXADECIMAL) {
                originalField.append(ISOUtil.convertToNumericString(value.length() / 2, 16, isoField.getLength()));
            } else {
                originalField.append(ISOUtil.convertToNumericString(value.length(), 16, isoField.getLength()));
            }
        }

        return processValue(originalField, isoField, value);
    }

    private static String processValue(StringBuilder originalField, ISOField isoField, String value) {
        switch (isoField.getTypeData()) {
            case ALPHA_NUMERIC:
                originalField.append(ISOUtil.convertEBCDICtoHEX(value));
                break;
            case NUMERIC_DECIMAL:
                originalField.append(ISOUtil.revertValidAmount(value));
                break;
            case NUMERIC:
                if (value.length() % 2 != 0) {
                    originalField.append("0");
                }
                originalField.append(value);
                break;
            case HEXADECIMAL:
                originalField.append(value);
                break;
            case BINARY_STRING:
                originalField.append(ISOUtil.convertBITMAPtoHEX(value));
                break;
        }

        return originalField.toString();
    }

    public static ISOField getById(int id) {
        return ISO_MAP.get(id);
    }
}