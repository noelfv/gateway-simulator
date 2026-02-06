package com.bbva.orchestrator.core.mapper.iso20022.strategy.impl;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.mapper.iso20022.strategy.SectionMappingStrategy;
import com.bbva.orchestrator.core.utils.MapperUtil;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class TransactionMappingStrategy implements SectionMappingStrategy<TransactionDTO> {

    private final MapperUtil mapperUtil;

    public TransactionMappingStrategy( MapperUtil mapperUtil) {
        this.mapperUtil = mapperUtil;
    }

    @Override
    public TransactionDTO mapper(ISO8583 input, Map<String, String> subFields) {

        // ======== FIELD 3 (CODE PROCESS) ========
        // 03.01 TRANSACTION TYPE
        String transactionType = subFields.get("03.01");
        // 03.02 ACCOUNT FROM
        String accountFromType = subFields.get("03.02");
        // 03.03 ACCOUNT TO
        String accountToType = subFields.get("03.03");

        AccountFromDTO accountFrom = AccountFromDTO.builder()
                .accountId(input.getAccountIdentification())
                //.accountType(mapperUtil.isNullOrEmptySubstring(processingCodeStr, 2, 4))
                .accountType(accountFromType)
                .build();

        AccountToDTO accountTo = AccountToDTO.builder()
                .accountId(input.getAccountIdentification1())
                //.accountType(mapperUtil.isNullOrEmptySubstring(processingCodeStr, 4, 6))
                .accountType(accountToType)
                .build();

        // Transaction Amount
        TransactionAmountDTO transactionAmount = TransactionAmountDTO.builder()
                // ======== FIELD 4 (TRANSACTION AMOUNT) ========
                .amount(mapperUtil.convertAmountDouble(input.getTransactionAmount()))
                // ======== FIELD 49 (TRANSACTION CURRENCY CODE) ========
                .currency(mapperUtil.convertCurrencyIdToCurrencyCode(input.getTransactionCurrencyCode()))
                .build();

        // Reconciliation Amount
        ReconciliationAmountDTO reconciliationAmount = ReconciliationAmountDTO.builder()
                // ======== FIELD 5 (RECONCILIATION AMOUNT) ========
                .amount(mapperUtil.convertAmountDouble(input.getSettlementAmount()))
                // ======== FIELD 9 (RECONCILIATION EXCHANGE RATE) ========
                .effectiveExchangeRate(mapperUtil.convertEffectiveExchangeRate(input.getConversionRateSettlement()))
                // ======== FIELD 50 (SETTLEMENT CURRENCY CODE) ========
                .currency(mapperUtil.convertCurrencyIdToCurrencyCode(input.getSettlementCurrencyCode()))
                .build();

        // Cardholder Billing Amount
        CardholderBillingAmountDTO cardholderBillingAmount = CardholderBillingAmountDTO.builder()
                // ======== FIELD 6 (CARDHOLDER BILLING AMOUNT) ========
                .amount(mapperUtil.convertAmountDouble(input.getCardHolderBillingAmount()))
                // ======== FIELD 10 (CARDHOLDER BILLING EXCHANGE RATE) ========
                .effectiveExchangeRate(mapperUtil.convertEffectiveExchangeRate(input.getConversionRate()))
                // ======== FIELD 51 (CARDHOLDER BILLING CURRENCY CODE) ========
                .currency(mapperUtil.convertCurrencyIdToCurrencyCode(input.getCardholderBillingCurrencyCode()))
                .build();

        TransactionAmountsDTO transactionAmounts = TransactionAmountsDTO.builder()
                .transactionAmount(transactionAmount)
                .reconciliationAmount(reconciliationAmount)
                .cardholderBillingAmount(cardholderBillingAmount)
                .build();

        // ======== FIELD 90 (ORIGINAL DATA ELEMENTS) ========
        String oriDataElementsStr = input.getOriginalDataElements();
        OriginalDataElementsDTO originalDataElements = null;
        if (oriDataElementsStr != null && !oriDataElementsStr.isEmpty()) {
            originalDataElements = OriginalDataElementsDTO.builder()
                    // 90.1 MESSAGE TYPE
                    .messageFunction(mapperUtil.isNullOrEmptySubstring(oriDataElementsStr, 0, 4))
                    // 90.2 SYSTEMS TRACE AUDIT NUMBER
                    .systemTraceAuditNumber(mapperUtil.isNullOrEmptySubstring(oriDataElementsStr, 4, 10))
                    // 90.3 TRANSMISSION DATE & TIME
                    .transmissionDateTime(mapperUtil.isNullOrEmptySubstring(oriDataElementsStr, 10, 20))
                    // 90.4 ACQUIRER INSTITUTION ID
                    .acquirerId(mapperUtil.isNullOrEmptySubstring(oriDataElementsStr, 20, 31))
                    // 90.5 FORWARDING INSTITUTION ID
                    .senderIdentification(mapperUtil.isNullOrEmptySubstring(oriDataElementsStr, 31, 42))
                    .build();
        }

        // Transaction ID
        TransactionIdDTO transactionId = TransactionIdDTO.builder()
                // ======== FIELD 11 (SYSTEM TRACE AUDIT NUMBER) ========
                .systemTraceAuditNumber(input.getSystemTraceAuditNumber())
                // ======== FIELD 12 (LOCAL TRANSACTION DATE) ========
                .localDate(input.getLocalTransactionDate())
                // ======== FIELD 13 (LOCAL TRANSACTION TIME) ========
                .localTime(input.getLocalTransactionTime())
                // ======== FIELD 37 (RETRIEVAL REFERENCE NUMBER) ========
                .retrievalReferenceNumber(input.getRetrievalReferenceNumber())
                // ======== FIELD 90 (ORIGINAL DATA ELEMENTS) ========
                .originalDataElements(originalDataElements)
                // ======== FIELD 7 (TRANSMISSION DATE & TIME) ========
                .transmissionDateTime(mapperUtil.convertFormatDateTime(input.getTransmissionDateTime()))
                // ======== ID MONITOR ========
                .transactionReference(mapperUtil.createTransactionReference(input, subFields,  GrpcHeadersInfo.getNetwork()))
                .build();

        // Additional Fees
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

        // Additional Amounts
        List<AdditionalAmountDTO> additionalAmountList = new ArrayList<>();
        // ======== FIELD 54 (ADDITIONAL AMOUNTS) ========
        AdditionalAmountDTO additionalAmount = AdditionalAmountDTO.builder()
                .key("additionalAmounts")
                .amount(AmountDTO.builder()
                        .amount(subFields.containsKey("ADDITIONAL_AMOUNT_DOUBLE") ?
                                mapperUtil.convertAmountDouble(subFields.get("ADDITIONAL_AMOUNT_DOUBLE")) : null)
                        .build())
                .build();
        additionalAmountList.add(additionalAmount);

        // Additional Transaction Data
        List<AdditionalDataDTO> additionalTransactionDataList = new ArrayList<>();


        // ======== FIELD 3 (CODE PROCESS) ========
        additionalTransactionDataList.add(AdditionalDataDTO.builder()
                .key("opera")
                .value(input.getProcessingCode())
                .build());

        // Redemption Points
        // ======== FIELD 58 (MAPPED AS REDEMPTION POINTS) ========
        additionalTransactionDataList.add(AdditionalDataDTO.builder()
                .key("redemptionPoints")
                .value(input.getRedemptionPoints())
                .build());

        // Additional Response Data
        // ======== FIELD 44 (ADDITIONAL RESPONSE DATA) ========
        additionalTransactionDataList.add(AdditionalDataDTO.builder()
                .key("additionalResponseData")
                .value(input.getAdditionalResponseData())
                .build());


        // FIELD 54 - Se mapea los 4 primeros subcampos a esta variable AdditionalData temporalmente
        addAdditionalData(additionalTransactionDataList, subFields, "ADDITIONAL_ACCOUNT_TYPE", "accountType");
        addAdditionalData(additionalTransactionDataList, subFields, "ADDITIONAL_AMOUNT_TYPE", "amountType");
        addAdditionalData(additionalTransactionDataList, subFields, "ADDITIONAL_CURRENCY_CODE", "currencyCode");
        addAdditionalData(additionalTransactionDataList, subFields, "ADDITIONAL_INDICATOR", "indicator");

        return TransactionDTO.builder()
                //.transactionType(mapperUtil.isNullOrEmptySubstring(processingCodeStr, 0, 2))
                .transactionType(transactionType)
                .accountFrom(accountFrom)
                .accountTo(accountTo)
                .transactionAmounts(transactionAmounts)
                .transactionId(transactionId)
                .transactionAttribute(0L)
                .messageReason(input.getPointServiceConditionCode())
                .additionalFee(additionalFeesList)
                .additionalAmount(additionalAmountList)
                .additionalData(additionalTransactionDataList)
                .build();
    }

    @Override
    public Map<String, String> unMapper(String networkName,TransactionDTO transaction) {

        Map<String, String> mapValues = new HashMap<>();
        String processingCode=getAdditionalData("opera", transaction.getAdditionalData());
        mapValues.put("processingCode", processingCode);

        // --- Amounts ---
        TransactionAmountsDTO transactionAmounts = transaction.getTransactionAmounts();
        TransactionAmountDTO transactionAmount = transactionAmounts.getTransactionAmount();
        ReconciliationAmountDTO reconciliationAmountDTO = transactionAmounts.getReconciliationAmount();
        CardholderBillingAmountDTO cardholderBillingAmountDTO = transactionAmounts.getCardholderBillingAmount();


        if (transactionAmount.getAmount() != null) {
            // ======== FIELD 4 (TRANSACTION AMOUNT) ========
            mapValues.put("transactionAmount", mapperUtil.convertAmountString(transactionAmount.getAmount()));
            // ======== FIELD 49 (TRANSACTION CURRENCY CODE) ========
            mapValues.put("transactionCurrencyCode",  mapperUtil.convertCurrencyCodeToCurrencyId(transactionAmount.getCurrency()));//TODO hacer la inversa
        }


        if (reconciliationAmountDTO.getAmount() != null) {
            // ======== FIELD 5 (RECONCILIATION AMOUNT) ========
            mapValues.put("settlementAmount", mapperUtil.convertAmountString(reconciliationAmountDTO.getAmount()));
            // ======== FIELD 9 (RECONCILIATION EXCHANGE RATE) ========
            mapValues.put("conversionRateSettlement", mapperUtil.convertConversionRate(reconciliationAmountDTO.getEffectiveExchangeRate()));
            // ======== FIELD 50 (SETTLEMENT CURRENCY CODE) ========
            mapValues.put("settlementCurrencyCode", mapperUtil.convertCurrencyCodeToCurrencyId(reconciliationAmountDTO.getCurrency()));
        }

        if (cardholderBillingAmountDTO.getAmount() != null) {
            // ======== FIELD 6 (CARDHOLDER BILLING AMOUNT) ========
            mapValues.put("cardHolderBillingAmount", mapperUtil.convertAmountString(cardholderBillingAmountDTO.getAmount()));
            // ======== FIELD 10 (CARDHOLDER BILLING EXCHANGE RATE) ========
            mapValues.put("conversionRate", mapperUtil.convertConversionRate(cardholderBillingAmountDTO.getEffectiveExchangeRate()));
            // ======== FIELD 51 (CARDHOLDER BILLING CURRENCY CODE) ========
            mapValues.put("cardholderBillingCurrencyCode", mapperUtil.convertCurrencyCodeToCurrencyId(cardholderBillingAmountDTO.getCurrency()));
        }

        // --- Transaction ID y Original Data Elements ---
        TransactionIdDTO transId = transaction.getTransactionId();
        // ======== FIELD 11 (SYSTEM TRACE AUDIT NUMBER) ========
        mapValues.put("systemTraceAuditNumber", transId.getSystemTraceAuditNumber());
        // ======== FIELD 12 (LOCAL TRANSACTION DATE) ========
        mapValues.put("localTransactionDate", transId.getLocalDate());
        // ======== FIELD 13 (LOCAL TRANSACTION TIME) ========
        mapValues.put("localTransactionTime", transId.getLocalTime());
        // ======== FIELD 37 (RETRIEVAL REFERENCE NUMBER) ========
        mapValues.put("retrievalReferenceNumber", transId.getRetrievalReferenceNumber());
        // ======== FIELD 7 (TRANSMISSION DATE & TIME) ========
        mapValues.put("transmissionDateTime", mapperUtil.reConvertFormatDateTime(transId.getTransmissionDateTime())); // Asumiendo que mapperUtil convierte a la inversa si es necesario.


        // ======== FIELD 44 (ADDITIONAL RESPONSE DATA) ========
        String additionalResponseDataValue = getAdditionalData("additionalResponseData", transaction.getAdditionalData());
        mapValues.put("additionalResponseData", additionalResponseDataValue);


        // --- Data Elements ---
        //TODO evaluar si este bloqeu es nesario, quiza en los mensajes de respuesta no es necesario
        String originalDataElements = reconstructOriginalDataElements(transId.getOriginalDataElements());
        mapValues.put("originalDataElements", originalDataElements);


        // --- Message Reason ---
        mapValues.put("pointServiceConditionCode", transaction.getMessageReason());
        processAdditionalAmount(mapValues, transaction.getAdditionalAmount()); // <-- LÓGICA AÑADIDA
        return mapValues;
    }

    private String getAdditionalData(String key, List<AdditionalDataDTO> additionalDataList) {
        if (additionalDataList == null) return null;
        return additionalDataList.stream()
                .filter(data -> key.equals(data.getKey()))
                .map(AdditionalDataDTO::getValue)
                .findFirst()
                .orElse(null);
    }

    private String reconstructOriginalDataElements(OriginalDataElementsDTO origData) {
        return mapperUtil.getFieldValue(origData, OriginalDataElementsDTO::getMessageFunction, "") +
                mapperUtil.getFieldValue(origData, OriginalDataElementsDTO::getSystemTraceAuditNumber, "") +
                mapperUtil.getFieldValue(origData, OriginalDataElementsDTO::getTransmissionDateTime, "") +
                mapperUtil.getFieldValue(origData, OriginalDataElementsDTO::getAcquirerId, "") +
                mapperUtil.getFieldValue(origData, OriginalDataElementsDTO::getSenderIdentification, "");
    }

    private void addAdditionalData(List<AdditionalDataDTO> list, Map<String, String> subFields,
                                   String subFieldKey, String keyName) {
        String value = subFields.getOrDefault(subFieldKey, null);
        if (value != null) {
            list.add(AdditionalDataDTO.builder()
                    .key(keyName)
                    .value(value)
                    .build());
        }
    }

    private void processAdditionalAmount(Map<String, String> mapValues, List<AdditionalAmountDTO> additionalAmountList) {
        if (additionalAmountList == null) return;

        // Busca el primer DTO con la clave "additionalAmounts" y extrae su valor.
        additionalAmountList.stream()
                .filter(a -> "additionalAmounts".equals(a.getKey()))
                .findFirst()
                .ifPresent(dto -> {
                    String amountValue = mapperUtil.getFieldValueDouble(
                            dto.getAmount(), // El objeto fuente es AmountDTO
                            AmountDTO::getAmount, // El getter para el Double
                            DEFAULT_EMPTY_VALUE
                    );
                    // Solo añade al mapa si el valor no está vacío.
                    if (!amountValue.isEmpty()) {
                        mapValues.put("ADDITIONAL_AMOUNT_DOUBLE", amountValue);
                    }
                });
    }



}