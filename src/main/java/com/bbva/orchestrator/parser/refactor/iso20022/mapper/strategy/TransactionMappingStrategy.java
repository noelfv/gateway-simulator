package com.bbva.orchestrator.parser.refactor.iso20022.mapper.strategy;


import com.bbva.gateway.dto.iso20022.*;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.refactor.iso20022.mapper.SectionMappingStrategy;
import com.bbva.orchestrator.parser.refactor.utils.FieldProcessingService;
import com.bbva.orchestrator.parser.refactor.utils.FieldUtils;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TransactionMappingStrategy implements SectionMappingStrategy<TransactionDTO> {

    private final FieldProcessingService fieldService;

    public TransactionMappingStrategy(FieldProcessingService fieldService) {
        this.fieldService = fieldService;
    }

    @Override
    public TransactionDTO map(ISO8583 input, Map<String, String> subFields) {
        String processingCodeStr = input.getProcessingCode();

        // Account From
        AccountFromDTO accountFrom = AccountFromDTO.builder()
                .accountId(FieldUtils.FIELD_NOT_MAPPED)
                .accountType(fieldService.isNullOrEmptySubstring(processingCodeStr, 2, 4))
                .build();

        // Account To
        AccountToDTO accountTo = AccountToDTO.builder()
                .accountId(input.getAccountIdentification())
                .accountType(fieldService.isNullOrEmptySubstring(processingCodeStr, 4, 6))
                .build();

        // Transaction Amount
        TransactionAmountDTO transactionAmount = TransactionAmountDTO.builder()
                .amount(fieldService.parseDouble(input.getTransactionAmount()))
                .currency(input.getTransactionCurrencyCode())
                .build();

        // Reconciliation Amount
        ReconciliationAmountDTO reconciliationAmount = ReconciliationAmountDTO.builder()
                .amount(fieldService.parseDouble(input.getSettlementAmount()))
                //.effectiveExchangeRate(fieldService.parseDouble(input.getConversionRateSettlement()))
                .effectiveExchangeRate(fieldService.conversionRateValidation(input.getConversionRateSettlement()))
                .currency(input.getSettlementCurrencyCode())
                .build();

        // Cardholder Billing Amount
        CardholderBillingAmountDTO cardholderBillingAmount = CardholderBillingAmountDTO.builder()
                .amount(fieldService.parseDouble(input.getCardHolderBillingAmount()))
                .effectiveExchangeRate(fieldService.conversionRateValidation(input.getConversionRate()))
                .currency(input.getCardholderBillingCurrencyCode())
                .build();

        TransactionAmountsDTO transactionAmounts = TransactionAmountsDTO.builder()
                .transactionAmount(transactionAmount)
                .reconciliationAmount(reconciliationAmount)
                .cardholderBillingAmount(cardholderBillingAmount)
                .build();

        // Original Data Elements
        String oriDataElementsStr = input.getOriginalDataElements();
        OriginalDataElementsDTO originalDataElements = OriginalDataElementsDTO.builder()
                .messageFunction(fieldService.isNullOrEmptySubstring(oriDataElementsStr, 0, 4))
                .systemTraceAuditNumber(fieldService.isNullOrEmptySubstring(oriDataElementsStr, 4, 10))
                .transmissionDateTime(fieldService.isNullOrEmptySubstring(oriDataElementsStr, 10, 20))
                .acquirerId(fieldService.isNullOrEmptySubstring(oriDataElementsStr, 20, 31))
                .senderIdentification(fieldService.isNullOrEmptySubstring(oriDataElementsStr, 31, 42))
                .build();

        // Transaction ID
        TransactionIdDTO transactionId = TransactionIdDTO.builder()
                .systemTraceAuditNumber(input.getSystemTraceAuditNumber())
                .localDate(input.getLocalTransactionDate())
                .localTime(input.getLocalTransactionTime())
                .retrievalReferenceNumber(input.getRetrievalReferenceNumber())
                .originalDataElements(originalDataElements)
                .transmissionDateTime(fieldService.convertFormatDateTime(input.getTransmissionDateTime()))
                .transactionReference(fieldService.createTransactionReference(input))
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

        // Redemption Points
        additionalTransactionDataList.add(AdditionalDataDTO.builder()
                .key("redemptionPoints")
                .value(input.getRedemptionPoints())
                .build());

        // Additional Response Data
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
}