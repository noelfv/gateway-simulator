package com.bbva.orchestrator.core.mapper.iso20022.strategy.impl;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.mapper.iso20022.strategy.SectionMappingStrategy;
import com.bbva.orchestrator.core.utils.FieldProcessingService;
import com.bbva.orchestrator.core.utils.FieldUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class TransactionMappingStrategy implements SectionMappingStrategy<TransactionDTO> {

    private final FieldProcessingService fieldService;

    public TransactionMappingStrategy(FieldProcessingService fieldService) {
        this.fieldService = fieldService;
    }

    @Override
    public TransactionDTO mapper(ISO8583 input, Map<String, String> subFields) {

        String processingCodeStr = input.getProcessingCode();

        // 03.02 ACCOUNT FROM
        AccountFromDTO accountFrom = AccountFromDTO.builder()
                .accountId(input.getAccountIdentification())
                .accountType(fieldService.isNullOrEmptySubstring(processingCodeStr, 2, 4))
                .build();

        // 03.03 ACCOUNT TO
        AccountToDTO accountTo = AccountToDTO.builder()
                .accountId(input.getAccountIdentification1())
                .accountType(fieldService.isNullOrEmptySubstring(processingCodeStr, 4, 6))
                .build();

        // Transaction Amount
        TransactionAmountDTO transactionAmount = TransactionAmountDTO.builder()
                // ======== FIELD 4 (TRANSACTION AMOUNT) ========
                .amount(fieldService.parseDouble(input.getTransactionAmount()))
                // ======== FIELD 49 (TRANSACTION CURRENCY CODE) ========
                .currency(input.getTransactionCurrencyCode())
                .build();

        // Reconciliation Amount
        ReconciliationAmountDTO reconciliationAmount = ReconciliationAmountDTO.builder()
                // ======== FIELD 5 (RECONCILIATION AMOUNT) ========
                .amount(fieldService.parseDouble(input.getSettlementAmount()))
                // ======== FIELD 9 (RECONCILIATION EXCHANGE RATE) ========
                .effectiveExchangeRate(input.getConversionRateSettlement())
                // ======== FIELD 50 (SETTLEMENT CURRENCY CODE) ========
                .currency(input.getSettlementCurrencyCode())
                .build();

        // Cardholder Billing Amount
        CardholderBillingAmountDTO cardholderBillingAmount = CardholderBillingAmountDTO.builder()
                // ======== FIELD 6 (CARDHOLDER BILLING AMOUNT) ========
                .amount(fieldService.parseDouble(input.getCardHolderBillingAmount()))
                // ======== FIELD 10 (CARDHOLDER BILLING EXCHANGE RATE) ========
                .effectiveExchangeRate(input.getConversionRate())
                // ======== FIELD 51 (CARDHOLDER BILLING CURRENCY CODE) ========
                .currency(input.getCardholderBillingCurrencyCode())
                .build();

        TransactionAmountsDTO transactionAmounts = TransactionAmountsDTO.builder()
                .transactionAmount(transactionAmount)
                .reconciliationAmount(reconciliationAmount)
                .cardholderBillingAmount(cardholderBillingAmount)
                .build();

        //TODO esto solo aplica para mensajes de reversa y anulacion, en los demas casos va vacio
        // ======== FIELD 90 (ORIGINAL DATA ELEMENTS) ========
        String oriDataElementsStr = input.getOriginalDataElements();
        OriginalDataElementsDTO originalDataElements = null;
        if (oriDataElementsStr != null && !oriDataElementsStr.isEmpty()) {
             originalDataElements = OriginalDataElementsDTO.builder()
                    // 90.1 MESSAGE TYPE
                    .messageFunction(fieldService.isNullOrEmptySubstring(oriDataElementsStr, 0, 4))
                    // 90.2 SYSTEMS TRACE AUDIT NUMBER
                    .systemTraceAuditNumber(fieldService.isNullOrEmptySubstring(oriDataElementsStr, 4, 10))
                    // 90.3 TRANSMISSION DATE & TIME
                    .transmissionDateTime(fieldService.isNullOrEmptySubstring(oriDataElementsStr, 10, 20))
                    // 90.4 ACQUIRER INSTITUTION ID
                    .acquirerId(fieldService.isNullOrEmptySubstring(oriDataElementsStr, 20, 31))
                    // 90.5 FORWARDING INSTITUTION ID
                    .senderIdentification(fieldService.isNullOrEmptySubstring(oriDataElementsStr, 31, 42))
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
                .originalDataElements(originalDataElements)
                // ======== FIELD 7 (TRANSMISSION DATE & TIME) ========
                .transmissionDateTime(fieldService.convertFormatDateTime(input.getTransmissionDateTime()))
                // ======== ID MONITOR ========
                .transactionReference(fieldService.createTransactionReference(input, subFields,  GrpcHeadersInfo.getNetwork()))
                .build();

        // Additional Fees
        FeeAmountDTO feeAmount = FeeAmountDTO.builder().amount(null).build();
        FeeReconciliationAmountDTO feeReconciliation = FeeReconciliationAmountDTO.builder().amount(null).build();
        AdditionalFeesDTO additionalFees = AdditionalFeesDTO.builder()
                .feeAmount(feeAmount)
                .feeReconciliationAmount(feeReconciliation)
                .build();

        List<AdditionalFeesDTO> additionalFeesList = List.of(additionalFees);

        // Additional Amounts
        List<AdditionalAmountDTO> additionalAmountList = new ArrayList<>();
        AdditionalAmountDTO additionalAmount = AdditionalAmountDTO.builder()
                .key("additionalAmounts")
                .amount(AmountDTO.builder()
                        .amount(subFields.containsKey("ADDITIONAL_AMOUNT_DOUBLE") ?
                                fieldService.parseDouble(subFields.get("ADDITIONAL_AMOUNT_DOUBLE")) : null)
                        .build())
                .build();
        additionalAmountList.add(additionalAmount);

        // Additional Transaction Data
        List<AdditionalDataDTO> additionalTransactionDataList = new ArrayList<>();


        // Additional processingCode
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

        // Subfields from 54 (mapped as AdditionalData)
        addAdditionalData(additionalTransactionDataList, subFields, "ADDITIONAL_ACCOUNT_TYPE", "accountType");
        addAdditionalData(additionalTransactionDataList, subFields, "ADDITIONAL_AMOUNT_TYPE", "amountType");
        addAdditionalData(additionalTransactionDataList, subFields, "ADDITIONAL_CURRENCY_CODE", "currencyCode");
        addAdditionalData(additionalTransactionDataList, subFields, "ADDITIONAL_INDICATOR", "indicator");

        return TransactionDTO.builder()
                // 03.01 TRANSACTION TYPE
                .transactionType(fieldService.isNullOrEmptySubstring(processingCodeStr, 0, 2))
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
    public Map<String, String> unMapper(TransactionDTO transaction) {

        Map<String, String> mapValues = new HashMap<>();
        String processingCode=getAdditionalData("opera", transaction.getAdditionalData());
        mapValues.put("processingCode", processingCode);

        // --- Amounts ---
        TransactionAmountsDTO transactionAmounts = transaction.getTransactionAmounts();
        TransactionAmountDTO transactionAmount = transactionAmounts.getTransactionAmount();
        ReconciliationAmountDTO reconciliationAmountDTO = transactionAmounts.getReconciliationAmount();
        CardholderBillingAmountDTO cardholderBillingAmountDTO = transactionAmounts.getCardholderBillingAmount();


        if (transactionAmount.getAmount() != null) {
            String amount = formatAmount(transactionAmount.getAmount());
            // ======== FIELD 4 (TRANSACTION AMOUNT) ========
            mapValues.put("transactionAmount", amount);
            // ======== FIELD 49 (TRANSACTION CURRENCY CODE) ========
            mapValues.put("transactionCurrencyCode",  transactionAmount.getCurrency());//TODO hacer la inversa
        }


        if (reconciliationAmountDTO.getAmount() != null) {
            String reconciliationAmount = formatAmount(reconciliationAmountDTO.getAmount());
            // ======== FIELD 5 (RECONCILIATION AMOUNT) ========
            mapValues.put("settlementAmount", reconciliationAmount);
            // ======== FIELD 9 (RECONCILIATION EXCHANGE RATE) ========
            mapValues.put("conversionRateSettlement", fieldService.getFieldValue(reconciliationAmountDTO, ReconciliationAmountDTO::getEffectiveExchangeRate, DEFAULT_EMPTY_VALUE));
            // ======== FIELD 50 (SETTLEMENT CURRENCY CODE) ========
            mapValues.put("settlementCurrencyCode", fieldService.getFieldValue(reconciliationAmountDTO, ReconciliationAmountDTO::getCurrency, DEFAULT_EMPTY_VALUE));
        }

        if (cardholderBillingAmountDTO.getAmount() != null) {
            String cardholderBillingAmount = formatAmount(cardholderBillingAmountDTO.getAmount());
            // ======== FIELD 6 (CARDHOLDER BILLING AMOUNT) ========
            mapValues.put("cardHolderBillingAmount", cardholderBillingAmount);
            // ======== FIELD 10 (CARDHOLDER BILLING EXCHANGE RATE) ========
            mapValues.put("conversionRate", fieldService.getFieldValue(cardholderBillingAmountDTO, CardholderBillingAmountDTO::getEffectiveExchangeRate, DEFAULT_EMPTY_VALUE));
            // ======== FIELD 51 (CARDHOLDER BILLING CURRENCY CODE) ========
            mapValues.put("cardholderBillingCurrencyCode", fieldService.getFieldValue(cardholderBillingAmountDTO, CardholderBillingAmountDTO::getCurrency, DEFAULT_EMPTY_VALUE));
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
        mapValues.put("transmissionDateTime", transId.getTransmissionDateTime().substring(4)); // Asumiendo que fieldService convierte a la inversa si es necesario.


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
        return fieldService.getFieldValue(origData, OriginalDataElementsDTO::getMessageFunction, "") +
                fieldService.getFieldValue(origData, OriginalDataElementsDTO::getSystemTraceAuditNumber, "") +
                fieldService.getFieldValue(origData, OriginalDataElementsDTO::getTransmissionDateTime, "") +
                fieldService.getFieldValue(origData, OriginalDataElementsDTO::getAcquirerId, "") +
                fieldService.getFieldValue(origData, OriginalDataElementsDTO::getSenderIdentification, "");
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
                    String amountValue = fieldService.getFieldValueDouble(
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

    /**
     * Convierte un valor double a un String de 12 caracteres.
     * El método elimina el punto decimal y rellena con ceros a la izquierda
     * hasta alcanzar la longitud de 12.
     *
     * @param amount El valor double a convertir.
     * @return Un String de 12 caracteres.
     */
    private String formatAmount(double amount) {
        // Usar BigDecimal para evitar problemas de precisión con double
        BigDecimal amountInCents = BigDecimal.valueOf(amount)
                .setScale(2, RoundingMode.HALF_UP)
                .multiply(BigDecimal.valueOf(100));
        // Formatea a 12 dígitos con ceros a la izquierda
        return String.format("%012d", amountInCents.longValue());
    }

}