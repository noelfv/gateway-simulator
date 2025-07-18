package com.bbva.orchestrator.parser.iso8583;

import java.util.HashMap;
import java.util.Map;

public class ISO8583Builder {

    private ISO8583Builder() {
    }

    public static ISO8583 buildISO8583(String originalMessage, Map<String, String> mapValues) {

        return ISO8583.builder()
                .originalMessage(originalMessage)
                .header(getValue("header", mapValues))
                .messageType(getValue("messageType", mapValues))
                .primaryAccountNumber(getValue("primaryAccountNumber", mapValues)) //PAN prendido valor->37 NETWORK_MANAGEMENT_INFORMATION_CODE
                .processingCode(getValue("processingCode", mapValues))
                .transactionAmount(getValue("transactionAmount", mapValues))
                .settlementAmount(getValue("settlementAmount", mapValues))
                .cardHolderBillingAmount(getValue("cardHolderBillingAmount", mapValues))
                .transmissionDateTime(getValue("transmissionDateTime", mapValues))
                .amountCardholderBillingFee(getValue("amountCardholderBillingFee", mapValues))
                .conversionRateSettlement(getValue("conversionRateSettlement", mapValues))
                .conversionRate(getValue("conversionRate", mapValues))
                .systemTraceAuditNumber(getValue("systemTraceAuditNumber", mapValues))
                .localTransactionTime(getValue("localTransactionTime", mapValues))
                .localTransactionDate(getValue("localTransactionDate", mapValues))
                .dateExpiration(getValue("dateExpiration", mapValues))
                .settlementDate(getValue("settlementDate", mapValues))
                .dateConversion(getValue("dateConversion", mapValues))
                .captureDate(getValue("captureDate", mapValues))
                .merchantType(getValue("merchantType", mapValues))
                .acquirerCountryCode(getValue("acquirerCountryCode", mapValues))
                .primaryAccountNumberCountryCode(getValue("primaryAccountNumberCountryCode", mapValues))
                .forwardingInstitutionCountryCode(getValue("forwardingInstitutionCountryCode", mapValues))
                .pointServiceEntryMode(getValue("pointServiceEntryMode", mapValues))
                .cardSequenceNumber(getValue("cardSequenceNumber", mapValues))
                .networkInternationalId(getValue("networkInternationalId", mapValues))
                .pointServiceConditionCode(getValue("pointServiceConditionCode", mapValues))
                .pointServicePIN(getValue("pointServicePIN", mapValues))
                .authorizationIdResponseLength(getValue("authorizationIdResponseLength", mapValues))
                .amountTransactionFee(getValue("amountTransactionFee", mapValues))
                .amountSettlementFee(getValue("amountSettlementFee", mapValues))
                .amountTransactionProcessingFee(getValue("amountTransactionProcessingFee", mapValues))
                .amountSettlementProcessingFee(getValue("amountSettlementProcessingFee", mapValues))
                .acquiringInstitutionIdentificationCode(getValue("acquiringInstitutionIdentificationCode", mapValues))
                .forwardingInstitutionIdentificationCode(getValue("forwardingInstitutionIdentificationCode", mapValues))
                .primaryAccountNumberExtended(getValue("primaryAccountNumberExtended", mapValues))
                .trackTwoData(getValue("trackTwoData", mapValues))
                .trackThreeData(getValue("trackThreeData", mapValues))
                .retrievalReferenceNumber(getValue("retrievalReferenceNumber", mapValues))
                .authorizationIdentificationResponse(getValue("authorizationIdentificationResponse", mapValues))
                .responseCode(getValue("responseCode", mapValues))
                .serviceRestrictionCode(getValue("serviceRestrictionCode", mapValues))
                .cardAcceptorTerminalIdentification(getValue("cardAcceptorTerminalIdentification", mapValues))
                .cardAcceptorIdentificationCode(getValue("cardAcceptorIdentificationCode", mapValues))
                .cardAcceptorNameLocation(getValue("cardAcceptorNameLocation", mapValues))
                .additionalResponseData(getValue("additionalResponseData", mapValues))
                .trackOneData(getValue("trackOneData", mapValues))
                .expandedAdditionalAmounts(getValue("expandedAdditionalAmounts", mapValues))
                .additionalDataNationalUse(getValue("additionalDataNationalUse", mapValues))
                .additionalDataRetailer(getValue("additionalDataRetailer", mapValues))
                .transactionCurrencyCode(getValue("transactionCurrencyCode", mapValues))
                .settlementCurrencyCode(getValue("settlementCurrencyCode", mapValues))
                .cardholderBillingCurrencyCode(getValue("cardholderBillingCurrencyCode", mapValues))
                .pinData(getValue("pinData", mapValues))
                .securityControlInformation(getValue("securityControlInformation", mapValues))
                .additionalAmounts(getValue("additionalAmounts", mapValues))
                .integratedCircuitCard(getValue("integratedCircuitCard", mapValues))
                .paymentAccountData(getValue("paymentAccountData", mapValues))
                .redemptionPoints(getValue("redemptionPoints", mapValues))
                .campaignData(getValue("campaignData", mapValues))
                .posTerminalData(getValue("posTerminalData", mapValues))
                .posCardIssuer(getValue("posCardIssuer", mapValues))
                .postalCode(getValue("postalCode", mapValues))
                .networkData(getValue("networkData", mapValues))
                .messageAuthenticationCode(getValue("messageAuthenticationCode", mapValues))
                .settlementCode(getValue("settlementCode", mapValues))
                .extendedPaymentCode(getValue("extendedPaymentCode", mapValues))
                .receivingInstitutionCountryCode(getValue("receivingInstitutionCountryCode", mapValues))
                .settlementInstitutionCountryCode(getValue("settlementInstitutionCountryCode", mapValues))
                .networkManagementInformationCode(getValue("networkManagementInformationCode", mapValues))
                .messageNumber(getValue("messageNumber", mapValues))
                .messageNumberLast(getValue("messageNumberLast", mapValues))
                .dateAction(getValue("dateAction", mapValues))
                .creditsNumber(getValue("creditsNumber", mapValues))
                .creditsReversalNumber(getValue("creditsReversalNumber", mapValues))
                .debitsNumber(getValue("debitsNumber", mapValues))
                .debitsReversalNumber(getValue("debitsReversalNumber", mapValues))
                .transferNumber(getValue("transferNumber", mapValues))
                .transferReversalNumber(getValue("transferReversalNumber", mapValues))
                .inquiriesNumber(getValue("inquiriesNumber", mapValues))
                .authorizationNumber(getValue("authorizationNumber", mapValues))
                .creditsProcessingFeeAmount(getValue("creditsProcessingFeeAmount", mapValues))
                .creditsTransactionFeeAmount(getValue("creditsTransactionFeeAmount", mapValues))
                .debitsProcessingFeeAmount(getValue("debitsProcessingFeeAmount", mapValues))
                .debitsTransactionFeeAmount(getValue("debitsTransactionFeeAmount", mapValues))
                .creditsAmount(getValue("creditsAmount", mapValues))
                .creditsReversalAmount(getValue("creditsReversalAmount", mapValues))
                .debitsAmount(getValue("debitsAmount", mapValues))
                .debitsReversalAmount(getValue("debitsReversalAmount", mapValues))
                .originalDataElements(getValue("originalDataElements", mapValues))
                .issuerFileUpdateCode(getValue("issuerFileUpdateCode", mapValues))
                .fileSecurityCode(getValue("fileSecurityCode", mapValues))
                .responseIndicator(getValue("responseIndicator", mapValues))
                .serviceIndicator(getValue("serviceIndicator", mapValues))
                .replacementAmounts(getValue("replacementAmounts", mapValues))
                .messageSecurityCode(getValue("messageSecurityCode", mapValues))
                .amountNetSettlement(getValue("amountNetSettlement", mapValues))
                .payee(getValue("payee", mapValues))
                .settlementInstitutionIdentificationCode(getValue("settlementInstitutionIdentificationCode", mapValues))
                .receivingInstitutionIdentificationCode(getValue("receivingInstitutionIdentificationCode", mapValues))
                .fileName(getValue("fileName", mapValues))
                .accountIdentification1(getValue("accountIdentification1", mapValues))
                .accountIdentification(getValue("accountIdentification", mapValues))
                .transactionData(getValue("transactionData", mapValues))
                .doubleLengthDesKey(getValue("doubleLengthDesKey", mapValues))
                .fleetServiceData(getValue("fleetServiceData", mapValues))
                .additionalTransactionReferenceData(getValue("additionalTransactionReferenceData", mapValues))
                .isoUse(getValue("isoUse", mapValues))
                .encryptionData(getValue("encryptionData", mapValues))
                .additionalDataNationalUse2(getValue("additionalDataNationalUse2", mapValues))
                .reservedNationalUse(getValue("reservedNationalUse", mapValues))
                .reservedNationalUse2(getValue("reservedNationalUse2", mapValues))
                .keyManagement(getValue("keyManagement", mapValues))
                .authorizingAgentIdCode(getValue("authorizingAgentIdCode", mapValues))
                .additionalRecordData(getValue("additionalRecordData", mapValues))
                .cryptographicServiceMessage(getValue("cryptographicServiceMessage", mapValues))
                .infoText(getValue("infoText", mapValues))
                .settlementData(getValue("settlementData", mapValues))
                .issuerTraceId(getValue("issuerTraceId", mapValues))
                .privateData(getValue("privateData", mapValues))
                .messageAuthenticationCode2(getValue("messageAuthenticationCode2", mapValues))
                .build();
    }


    public static Map<String, String> buildMapISO8583(ISO8583 iso8583) {
        Map<String, String> mapValues = new HashMap<>();

        mapValues.put("header", iso8583.getHeader());
        mapValues.put("messageType", iso8583.getMessageType());
        mapValues.put("primaryAccountNumber", iso8583.getPrimaryAccountNumber());
        mapValues.put("processingCode", iso8583.getProcessingCode());
        mapValues.put("transactionAmount", iso8583.getTransactionAmount());
        mapValues.put("settlementAmount", iso8583.getSettlementAmount());
        mapValues.put("cardHolderBillingAmount", iso8583.getCardHolderBillingAmount());
        mapValues.put("transmissionDateTime", iso8583.getTransmissionDateTime());
        mapValues.put("amountCardholderBillingFee", iso8583.getAmountCardholderBillingFee());
        mapValues.put("conversionRateSettlement", iso8583.getConversionRateSettlement());
        mapValues.put("conversionRate", iso8583.getConversionRate());
        mapValues.put("systemTraceAuditNumber", iso8583.getSystemTraceAuditNumber());
        mapValues.put("localTransactionTime", iso8583.getLocalTransactionTime());
        mapValues.put("localTransactionDate", iso8583.getLocalTransactionDate());
        mapValues.put("dateExpiration", iso8583.getDateExpiration());
        mapValues.put("settlementDate", iso8583.getSettlementDate());
        mapValues.put("dateConversion", iso8583.getDateConversion());
        mapValues.put("captureDate", iso8583.getCaptureDate());
        mapValues.put("merchantType", iso8583.getMerchantType());
        mapValues.put("acquirerCountryCode", iso8583.getAcquirerCountryCode());
        mapValues.put("primaryAccountNumberCountryCode", iso8583.getPrimaryAccountNumberCountryCode());
        mapValues.put("forwardingInstitutionCountryCode", iso8583.getForwardingInstitutionCountryCode());
        mapValues.put("pointServiceEntryMode", iso8583.getPointServiceEntryMode());
        mapValues.put("cardSequenceNumber", iso8583.getCardSequenceNumber());
        mapValues.put("networkInternationalId", iso8583.getNetworkInternationalId());
        mapValues.put("pointServiceConditionCode", iso8583.getPointServiceConditionCode());
        mapValues.put("pointServicePIN", iso8583.getPointServicePIN());
        mapValues.put("authorizationIdResponseLength", iso8583.getAuthorizationIdResponseLength());
        mapValues.put("amountTransactionFee", iso8583.getAmountTransactionFee());
        mapValues.put("amountSettlementFee", iso8583.getAmountSettlementFee());
        mapValues.put("amountTransactionProcessingFee", iso8583.getAmountTransactionProcessingFee());
        mapValues.put("amountSettlementProcessingFee", iso8583.getAmountSettlementProcessingFee());
        mapValues.put("acquiringInstitutionIdentificationCode", iso8583.getAcquiringInstitutionIdentificationCode());
        mapValues.put("forwardingInstitutionIdentificationCode", iso8583.getForwardingInstitutionIdentificationCode());
        mapValues.put("primaryAccountNumberExtended", iso8583.getPrimaryAccountNumberExtended());
        mapValues.put("trackTwoData", iso8583.getTrackTwoData());
        mapValues.put("trackThreeData", iso8583.getTrackThreeData());
        mapValues.put("retrievalReferenceNumber", iso8583.getRetrievalReferenceNumber());
        mapValues.put("authorizationIdentificationResponse", iso8583.getAuthorizationIdentificationResponse());
        mapValues.put("responseCode", iso8583.getResponseCode());
        mapValues.put("serviceRestrictionCode", iso8583.getServiceRestrictionCode());
        mapValues.put("cardAcceptorTerminalIdentification", iso8583.getCardAcceptorTerminalIdentification());
        mapValues.put("cardAcceptorIdentificationCode", iso8583.getCardAcceptorIdentificationCode());
        mapValues.put("cardAcceptorNameLocation", iso8583.getCardAcceptorNameLocation());
        mapValues.put("additionalResponseData", iso8583.getAdditionalResponseData());
        mapValues.put("trackOneData", iso8583.getTrackOneData());
        mapValues.put("expandedAdditionalAmounts", iso8583.getExpandedAdditionalAmounts());
        mapValues.put("additionalDataNationalUse", iso8583.getAdditionalDataNationalUse());
        mapValues.put("additionalDataRetailer", iso8583.getAdditionalDataRetailer());
        mapValues.put("transactionCurrencyCode", iso8583.getTransactionCurrencyCode());
        mapValues.put("settlementCurrencyCode", iso8583.getSettlementCurrencyCode());
        mapValues.put("cardholderBillingCurrencyCode", iso8583.getCardholderBillingCurrencyCode());
        mapValues.put("pinData", iso8583.getPinData());
        mapValues.put("securityControlInformation", iso8583.getSecurityControlInformation());
        mapValues.put("additionalAmounts", iso8583.getAdditionalAmounts());
        mapValues.put("integratedCircuitCard", iso8583.getIntegratedCircuitCard());
        mapValues.put("paymentAccountData", iso8583.getPaymentAccountData());
        mapValues.put("redemptionPoints", iso8583.getRedemptionPoints());
        mapValues.put("campaignData", iso8583.getCampaignData());
        mapValues.put("posTerminalData", iso8583.getPosTerminalData());
        mapValues.put("posCardIssuer", iso8583.getPosCardIssuer());
        mapValues.put("postalCode", iso8583.getPostalCode());
        mapValues.put("networkData", iso8583.getNetworkData());
        mapValues.put("messageAuthenticationCode", iso8583.getMessageAuthenticationCode());
        mapValues.put("settlementCode", iso8583.getSettlementCode());
        mapValues.put("extendedPaymentCode", iso8583.getExtendedPaymentCode());
        mapValues.put("receivingInstitutionCountryCode", iso8583.getReceivingInstitutionCountryCode());
        mapValues.put("settlementInstitutionCountryCode", iso8583.getSettlementInstitutionCountryCode());
        mapValues.put("networkManagementInformationCode", iso8583.getNetworkManagementInformationCode());
        mapValues.put("messageNumber", iso8583.getMessageNumber());
        mapValues.put("messageNumberLast", iso8583.getMessageNumberLast());
        mapValues.put("dateAction", iso8583.getDateAction());
        mapValues.put("creditsNumber", iso8583.getCreditsNumber());
        mapValues.put("creditsReversalNumber", iso8583.getCreditsReversalNumber());
        mapValues.put("debitsNumber", iso8583.getDebitsNumber());
        mapValues.put("debitsReversalNumber", iso8583.getDebitsReversalNumber());
        mapValues.put("transferNumber", iso8583.getTransferNumber());
        mapValues.put("transferReversalNumber", iso8583.getTransferReversalNumber());
        mapValues.put("inquiriesNumber", iso8583.getInquiriesNumber());
        mapValues.put("authorizationNumber", iso8583.getAuthorizationNumber());
        mapValues.put("creditsProcessingFeeAmount", iso8583.getCreditsProcessingFeeAmount());
        mapValues.put("creditsTransactionFeeAmount", iso8583.getCreditsTransactionFeeAmount());
        mapValues.put("debitsProcessingFeeAmount", iso8583.getDebitsProcessingFeeAmount());
        mapValues.put("debitsTransactionFeeAmount", iso8583.getDebitsTransactionFeeAmount());
        mapValues.put("creditsAmount", iso8583.getCreditsAmount());
        mapValues.put("creditsReversalAmount", iso8583.getCreditsReversalAmount());
        mapValues.put("debitsAmount", iso8583.getDebitsAmount());
        mapValues.put("debitsReversalAmount", iso8583.getDebitsReversalAmount());
        mapValues.put("originalDataElements", iso8583.getOriginalDataElements());
        mapValues.put("issuerFileUpdateCode", iso8583.getIssuerFileUpdateCode());
        mapValues.put("fileSecurityCode", iso8583.getFileSecurityCode());
        mapValues.put("responseIndicator", iso8583.getResponseIndicator());
        mapValues.put("serviceIndicator", iso8583.getServiceIndicator());
        mapValues.put("replacementAmounts", iso8583.getReplacementAmounts());
        mapValues.put("messageSecurityCode", iso8583.getMessageSecurityCode());
        mapValues.put("amountNetSettlement", iso8583.getAmountNetSettlement());
        mapValues.put("payee", iso8583.getPayee());
        mapValues.put("settlementInstitutionIdentificationCode", iso8583.getSettlementInstitutionIdentificationCode());
        mapValues.put("receivingInstitutionIdentificationCode", iso8583.getReceivingInstitutionIdentificationCode());
        mapValues.put("fileName", iso8583.getFileName());
        mapValues.put("accountIdentification1", iso8583.getAccountIdentification1());
        mapValues.put("accountIdentification", iso8583.getAccountIdentification());
        mapValues.put("transactionData", iso8583.getTransactionData());
        mapValues.put("doubleLengthDesKey", iso8583.getDoubleLengthDesKey());
        mapValues.put("fleetServiceData", iso8583.getFleetServiceData());
        mapValues.put("additionalTransactionReferenceData", iso8583.getAdditionalTransactionReferenceData());
        mapValues.put("isoUse", iso8583.getIsoUse());
        mapValues.put("encryptionData", iso8583.getEncryptionData());
        mapValues.put("additionalDataNationalUse2", iso8583.getAdditionalDataNationalUse2());
        mapValues.put("reservedNationalUse", iso8583.getReservedNationalUse());
        mapValues.put("reservedNationalUse2", iso8583.getReservedNationalUse2());
        mapValues.put("keyManagement", iso8583.getKeyManagement());
        mapValues.put("authorizingAgentIdCode", iso8583.getAuthorizingAgentIdCode());
        mapValues.put("additionalRecordData", iso8583.getAdditionalRecordData());
        mapValues.put("cryptographicServiceMessage", iso8583.getCryptographicServiceMessage());
        mapValues.put("infoText", iso8583.getInfoText());
        mapValues.put("settlementData", iso8583.getSettlementAmount());
        mapValues.put("issuerTraceId", iso8583.getIssuerTraceId());
        mapValues.put("privateData", iso8583.getPrivateData());
        mapValues.put("messageAuthenticationCode2", iso8583.getMessageAuthenticationCode2());

        return mapValues;
    }

    private static String getValue(String fieldName, Map<String, String> values) {
        if (values.containsKey(fieldName)) {
            return values.get(fieldName);
        }
        return "";
    }

}
