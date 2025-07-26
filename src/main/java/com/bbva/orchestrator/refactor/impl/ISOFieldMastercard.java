package com.bbva.orchestrator.refactor.impl;

import com.bbva.orchestrator.parser.common.ISODataType;
import com.bbva.orchestrator.parser.common.ISOField;

import java.util.HashMap;
import java.util.Map;

public enum ISOFieldMastercard implements ISOField {

    // Campos de longitud fija
    MESSAGE_TYPE(0, "messageType", ISODataType.NUMERIC, false, 4, new NumericFieldParser()),
    BITMAP_PRIMARY(0, "bitmap_primary", ISODataType.BINARY_STRING, false, 8, new BinaryStringFieldParser()), // 8 bytes = 16 chars HEX
    BITMAP_SECONDARY(1, "bitmap_secondary", ISODataType.BINARY_STRING, false, 8, new BinaryStringFieldParser()),
    // Campos de longitud variable (LLVAR/LLLVAR)
    // El 'length' para campos variables es la longitud del prefijo (ej. 2 para LLVAR, 3 para LLLVAR)
    // Y la estrategia envuelve la estrategia del tipo de dato real del campo
    PRIMARY_ACCOUNT_NUMBER(2, "primaryAccountNumber", ISODataType.NUMERIC, true, 2, new LlvarLengthPrefixParser(new NumericFieldParser())),
    PROCESSING_CODE(3, "processingCode", ISODataType.NUMERIC, false, 6, new NumericFieldParser()),
    TRANSACTION_AMOUNT(4, "transactionAmount", ISODataType.NUMERIC_DECIMAL, false, 12, new NumericDecimalFieldParser()),
    SETTLEMENT_AMOUNT(5, "settlementAmount", ISODataType.NUMERIC_DECIMAL, false, 12, new NumericFieldParser()),
    CARD_HOLDER_BILLING_AMOUNT(6, "cardHolderBillingAmount", ISODataType.NUMERIC_DECIMAL, false, 12, new NumericFieldParser()),
    TRANSMISSION_DATE_TIME(7, "transmissionDateTime", ISODataType.NUMERIC, false, 10,new NumericFieldParser()),
    AMOUNT_CARD_HOLDER_BILLING_FEE(8, "amountCardholderBillingFee", ISODataType.NUMERIC_DECIMAL, false, 8, new NumericFieldParser()),
    CONVERSION_RATE_SETTLEMENT(9,"conversionRateSettlement",ISODataType.NUMERIC,false,8, new NumericFieldParser()),
    CONVERSION_RATE(10, "conversionRate", ISODataType.NUMERIC, false, 8, new NumericFieldParser()),
    SYSTEM_TRACE_AUDIT_NUMBER(11, "systemTraceAuditNumber", ISODataType.NUMERIC, false, 6, new NumericFieldParser()),
    LOCAL_TRANSACTION_TIME(12, "localTransactionTime", ISODataType.NUMERIC, false, 6, new NumericFieldParser()),
    LOCAL_TRANSACTION_DATE(13, "localTransactionDate", ISODataType.NUMERIC, false, 4, new NumericFieldParser()),
    DATE_EXPIRATION(14, "dateExpiration", ISODataType.NUMERIC, false, 4, new NumericFieldParser()),
    SETTLEMENT_DATE(15, "settlementDate", ISODataType.NUMERIC, false, 4, new NumericFieldParser()),
    DATE_CONVERSION(16,"dateConversion",ISODataType.NUMERIC,false,4, new NumericFieldParser()),
    CAPTURE_DATE(17, "captureDate", ISODataType.NUMERIC, false, 4, new NumericFieldParser()),
    MERCHANT_TYPE(18, "merchantType", ISODataType.NUMERIC, false, 4, new NumericFieldParser()),
    ACQUIRER_COUNTRY_CODE(19, "acquirerCountryCode", ISODataType.NUMERIC, false, 3, new NumericFieldParser()),
    PAN_COUNTRY_CODE(20, "primaryAccountNumberCountryCode", ISODataType.NUMERIC, false, 3, new NumericFieldParser()),
    FORWARDING_INSTITUTION_COUNTRY_CODE(21, "forwardingInstitutionCountryCode", ISODataType.NUMERIC, false, 3, new NumericFieldParser()),
    POINT_SERVICE_ENTRY_MODE(22, "pointServiceEntryMode", ISODataType.NUMERIC, false, 3, new NumericFieldParser()),
    CARD_SEQUENCE_NUMBER(23,"cardSequenceNumber",ISODataType.NUMERIC,false,3, new NumericFieldParser()),
    NETWORK_INTERNATIONAL_ID(24,"networkInternationalId",ISODataType.NUMERIC,false,3, new NumericFieldParser()),
    POINT_SERVICE_CONDITION_CODE(25, "pointServiceConditionCode", ISODataType.NUMERIC, false, 2, new NumericFieldParser()),
    POINT_SERVICE_PERSONAL_ID_NUMBER(26, "pointServicePIN", ISODataType.NUMERIC, false, 2, new NumericFieldParser()),
    AUTHORIZATION_ID_RESPONSE_LENGTH(27, "authorizationIdResponseLength", ISODataType.NUMERIC, false, 1, new NumericFieldParser()),
    AMOUNT_TRANSACTION_FEE(28, "amountTransactionFee", ISODataType.ALPHA_NUMERIC, false, 9,new AlphaNumericFieldParser()),
    AMOUNT_SETTLEMENT_FEE(29, "amountSettlementFee", ISODataType.ALPHA_NUMERIC, false, 9,new AlphaNumericFieldParser()),
    AMOUNT_TRANSACTION_PROCESSING_FEE(30, "amountTransactionProcessingFee", ISODataType.ALPHA_NUMERIC, false, 9,new AlphaNumericFieldParser()),
    AMOUNT_SETTLEMENT_PROCESSING_FEE(31, "amountSettlementProcessingFee", ISODataType.ALPHA_NUMERIC, false, 9,new AlphaNumericFieldParser()),
    ACQUIRING_INSTITUTION_IDENTIFICATION_CODE(32, "acquiringInstitutionIdentificationCode", ISODataType.NUMERIC, true, 2,new LlvarLengthPrefixParser(new AlphaNumericFieldParser())),
    FORWARDING_INSTITUTION_IDENTIFICATION_CODE(33, "forwardingInstitutionIdentificationCode", ISODataType.NUMERIC, true, 2,new LlvarLengthPrefixParser(new AlphaNumericFieldParser())),
    PAN_EXTENDED(34, "primaryAccountNumberExtended", ISODataType.ALPHA_NUMERIC, true, 2,new LlvarLengthPrefixParser(new AlphaNumericFieldParser())),
    TRACK_TWO_DATA(35, "trackTwoData", ISODataType.ALPHA_NUMERIC, true, 2,new LlvarLengthPrefixParser(new AlphaNumericFieldParser())),
    TRACK_THREE_DATA(36, "trackThreeData", ISODataType.ALPHA_NUMERIC, true, 2,new LlvarLengthPrefixParser(new AlphaNumericFieldParser())),
    RETRIEVAL_REFERENCE_NUMBER(37, "retrievalReferenceNumber", ISODataType.ALPHA_NUMERIC, false, 12,new AlphaNumericFieldParser()),
    AUTHORIZATION_IDENTIFICATION_RESPONSE(38, "authorizationIdentificationResponse", ISODataType.ALPHA_NUMERIC, false, 6,new AlphaNumericFieldParser()),
    RESPONSE_CODE(39, "responseCode", ISODataType.ALPHA_NUMERIC, false, 2,new AlphaNumericFieldParser()),
    SERVICE_RESTRICTION_CODE(40, "serviceRestrictionCode", ISODataType.ALPHA_NUMERIC, false, 3,new AlphaNumericFieldParser()),
    CARD_ACCEPTOR_TERMINAL_IDENTIFICATION(41, "cardAcceptorTerminalIdentification", ISODataType.ALPHA_NUMERIC, false, 8,new AlphaNumericFieldParser()),
    CARD_ACCEPTOR_IDENTIFICATION_CODE(42, "cardAcceptorIdentificationCode", ISODataType.ALPHA_NUMERIC, false, 15,new AlphaNumericFieldParser()),
    CARD_ACCEPTOR_NAME_LOCATION(43, "cardAcceptorNameLocation", ISODataType.ALPHA_NUMERIC, false, 40,new AlphaNumericFieldParser()),
    ADDITIONAL_RESPONSE_DATA(44, "additionalResponseData", ISODataType.ALPHA_NUMERIC, true, 2,new LlvarLengthPrefixParser(new AlphaNumericFieldParser())),
    TRACK_ONE_DATA(45, "trackOneData", ISODataType.ALPHA_NUMERIC, true, 2,new LlvarLengthPrefixParser(new AlphaNumericFieldParser())),
    EXPANDED_ADDITIONAL_AMOUNTS(46, "expandedAdditionalAmounts", ISODataType.ALPHA_NUMERIC, true, 3,new LlvarLengthPrefixParser(new AlphaNumericFieldParser())),
    ADDITIONAL_DATA_NATIONAL_USE_2(47, "additionalDataNationalUse2", ISODataType.ALPHA_NUMERIC, true, 3,new LlvarLengthPrefixParser(new AlphaNumericFieldParser())),
    ADDITIONAL_DATA_RETAILER(48, "additionalDataRetailer", ISODataType.ALPHA_NUMERIC, true, 3,new LlvarLengthPrefixParser(new AlphaNumericFieldParser())),
    TRANSACTION_CURRENCY_CODE(49, "transactionCurrencyCode", ISODataType.NUMERIC, false, 3,new NumericFieldParser()),
    SETTLEMENT_CURRENCY_CODE(50, "settlementCurrencyCode", ISODataType.NUMERIC, false, 3,new NumericFieldParser()),
    CARD_HOLDER_BILLING_CURRENCY_CODE(51, "cardholderBillingCurrencyCode", ISODataType.NUMERIC, false, 3,new NumericFieldParser()),
    PIN_DATA(52, "pinData", ISODataType.HEXADECIMAL, false, 8,new HexadecimalFieldParser()),
    SECURITY_CONTROL_INFORMATION(53, "securityControlInformation", ISODataType.NUMERIC, false, 16,new NumericFieldParser()),
    ADDITIONAL_AMOUNTS(54, "additionalAmounts", ISODataType.ALPHA_NUMERIC, true, 3,new LlvarLengthPrefixParser(new AlphaNumericFieldParser())),
    INTEGRATED_CIRCUIT_CARD(55,"integratedCircuitCard",ISODataType.HEXADECIMAL,true,3,new LlvarLengthPrefixParser(new HexadecimalFieldParser())),
    PAYMENT_ACCOUNT_DATA(56,"paymentAccountData",ISODataType.ALPHA_NUMERIC,true,3,new LlvarLengthPrefixParser(new AlphaNumericFieldParser())),
    REDEMPTION_POINTS(58, "redemptionPoints", ISODataType.ALPHA_NUMERIC, true, 3,new LlvarLengthPrefixParser(new AlphaNumericFieldParser())),
    CAMPAIGN_DATA(59, "campaignData", ISODataType.ALPHA_NUMERIC, true, 3,new LlvarLengthPrefixParser(new AlphaNumericFieldParser())),
    POS_TERMINAL_DATA(60, "posTerminalData", ISODataType.ALPHA_NUMERIC, true, 3,new LlvarLengthPrefixParser(new AlphaNumericFieldParser())),
    POS_CARD_ISSUER(61, "posCardIssuer", ISODataType.ALPHA_NUMERIC, true, 3,new LlvarLengthPrefixParser(new AlphaNumericFieldParser())),
    POSTAL_CODE(62, "postalCode", ISODataType.ALPHA_NUMERIC, true, 3,new LlvarLengthPrefixParser(new AlphaNumericFieldParser())),
    NETWORK_DATA(63, "networkData", ISODataType.ALPHA_NUMERIC, true, 3,new LlvarLengthPrefixParser(new AlphaNumericFieldParser())),
    MESSAGE_AUTHENTICATION_CODE(64, "messageAuthenticationCode", ISODataType.HEXADECIMAL, false, 8,new HexadecimalFieldParser());

    private final int id;
    private final String name;
    private final ISODataType typeData;
    private final boolean isVariable;
    private final int length; // Longitud del campo o longitud del prefijo LLVAR/LLLVAR
    private final FieldParserStrategy parserStrategy; // La estrategia de parsing/building para este campo

    ISOFieldMastercard(int id, String name, ISODataType typeData, boolean isVariable, int length, FieldParserStrategy parserStrategy) {
        this.id = id;
        this.name = name;
        this.typeData = typeData;
        this.isVariable = isVariable;
        this.length = length;
        this.parserStrategy = parserStrategy;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public ISODataType getTypeData() { return typeData; }
    public boolean isVariable() { return isVariable; }
    public int getLength() { return length; } // Longitud definida en el enum

    @Override
    public String parseISOValue(ISOField isoField, String value, boolean clean) {
        return "";
    }

    public FieldParserStrategy getParserStrategy() { return parserStrategy; }

    // Método estático para obtener un campo por su ID
    private static final Map<Integer, ISOFieldMastercard> BY_ID = new HashMap<>();
    static {
        for (ISOFieldMastercard field : values()) {
            BY_ID.put(field.id, field);
        }
    }
    public static ISOFieldMastercard getById(int id) {
        return BY_ID.get(id);
    }
}