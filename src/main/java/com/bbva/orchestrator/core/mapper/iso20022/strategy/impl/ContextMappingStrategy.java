package com.bbva.orchestrator.core.mapper.iso20022.strategy.impl;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.orchestrator.core.dto.ISO8583;
import com.bbva.orchestrator.core.enums.CardDataEntryMode;
import com.bbva.orchestrator.core.enums.CardholderVerificationCapability;
import com.bbva.orchestrator.core.mapper.iso20022.strategy.SectionMappingStrategy;
import com.bbva.orchestrator.core.utils.FieldUtil;
import com.bbva.orchestrator.core.utils.MapperUtil;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ContextMappingStrategy implements SectionMappingStrategy<ContextDTO> {

    private final MapperUtil mapperUtil;

    public ContextMappingStrategy(MapperUtil mapperUtil) {
        this.mapperUtil = mapperUtil;
    }

    @Override
    public ContextDTO mapper(ISO8583 input, Map<String, String> subFields) {
        // === TRANSACTION CONTEXT ===

        Boolean isECommerceIndicator= mapperUtil.channelECommerceIndicator(input, subFields);

        ReconciliationDTO reconciliation = ReconciliationDTO.builder()
                .date(input.getAmountTransactionFee())
                .build();

        SettlementServiceDatesDTO settlementServiceDates = SettlementServiceDatesDTO.builder()
                .settlementDate(input.getSettlementDate())
                .build();

        SettlementServiceDTO settlementService = SettlementServiceDTO.builder()
                .settlementServiceDates(settlementServiceDates)
                .build();

        String operationType = mapperUtil.operationTypeValue(
                input.getMessageType(),
                FieldUtil.isNullOrEmptySubstring(input.getProcessingCode(), 0, 2)
        );

        String channel = mapperUtil.channelValue(
                input.getMessageType(),
                isECommerceIndicator,
                input.getPosTerminalData()
        );

        List<AdditionalDataDTO> transactionContextAdditionalData = List.of(
                AdditionalDataDTO.builder()
                        .key("OPERATION_TYPE")
                        .value(operationType)
                        .build(),
                AdditionalDataDTO.builder()
                        .key("CHANNEL")
                        .value(channel)
                        .build()
        );

        TransactionContextDTO transactionContext = TransactionContextDTO.builder()
                .merchantCategoryCode(input.getMerchantType())
                .reconciliation(reconciliation)
                .settlementService(settlementService)
                .captureDate(input.getCaptureDate())
                .additionalData(transactionContextAdditionalData)
                .build();

        // === POINT OF SERVICE CONTEXT ===

        Map<String, String> posCardHolderPresence = CardholderVerificationCapability.mapSubField61_04_cardHolderPresent(
                subFields.getOrDefault("61.04",null)
        );

        PointOfServiceContextDTO pointOfServiceContext = PointOfServiceContextDTO.builder()
                //.cardDataEntryMode(input.getPointServiceEntryMode())
                .cardDataEntryMode(CardDataEntryMode.convertCardDataEntryMode(
                        subFields.getOrDefault("22.01",null)))
                .ecommerceIndicator(CardholderVerificationCapability.mapEcommerceIndicator(
                        subFields.getOrDefault("61.04",null),isECommerceIndicator))
                .attendedIndicator(CardholderVerificationCapability.mapSubField01AttendedIndicator(
                        subFields.getOrDefault("61.01",null)))
                .unattendedLevelCategory(CardholderVerificationCapability.mapSubField01_10Category(
                        subFields.getOrDefault("61.01",null), subFields.getOrDefault("61.10",null)))
                .cardholderPresent(mapperUtil.safeBooleanValueOf(
                        posCardHolderPresence.getOrDefault("cardholderPresent",null))
                )
                .motoCode(posCardHolderPresence.getOrDefault("MOTOCode",null)
                )
                .cardPresent(CardholderVerificationCapability.mapSubField61_05_cardPresent(
                        subFields.getOrDefault("61.05",null))
                )
                .build();

        // === VERIFICATION ===
        PINDataDTO pinData = PINDataDTO.builder()
                .encryptedPINBlock(input.getPinData())
                .build();

        ValueDTO value = ValueDTO.builder()
                .pinData(pinData)
                .build();

        VerificationInformationDTO verificationInfo = VerificationInformationDTO.builder()
                .value(value)
                .build();

        ValueDTO valueCVC2 = ValueDTO.builder()
                .textValue(subFields.getOrDefault("48.92",null))
                .build();

        //CAMPO 48 SUBCAMPO 92
        VerificationInformationDTO verificationInfoCVC2 = VerificationInformationDTO.builder()
                .key("CVC_2")
                .value(valueCVC2)
                .build();

        ResultDetailsDTO resultDetails = ResultDetailsDTO.builder()
                .key("CVC")
                .value(subFields.getOrDefault("48.87",null))
                .build();

        VerificationResultDTO verificationResult = VerificationResultDTO.builder()
                .key("card_validation_code_result")
                .resultDetails(List.of(resultDetails))
                .build();

        VerificationDTO verification = VerificationDTO.builder()
                .verificationInformation(List.of(verificationInfo,verificationInfoCVC2))
                .verificationResult(List.of(verificationResult))
                .build();

        List<VerificationDTO> verificationList = List.of(verification);

        // === SALE CONTEXT ===
        AdditionalDataDTO campaignData = AdditionalDataDTO.builder()
                .key("campaignData")
                .value(input.getCampaignData())
                .build();

        SaleContextDTO saleContext = SaleContextDTO.builder()
                .additionalData(List.of(campaignData))
                .build();

        return ContextDTO.builder()
                .transactionContext(transactionContext)
                .pointOfServiceContext(pointOfServiceContext)
                .verification(verificationList)
                .saleContext(saleContext)
                .build();
    }

    @Override
    public Map<String, String> unMapper(String networkName,ContextDTO input) {
        Map<String, String> mapValues = new HashMap<>();

        TransactionContextDTO transactionContext = input.getTransactionContext();
        PointOfServiceContextDTO posContext = input.getPointOfServiceContext();
        SaleContextDTO saleContext = input.getSaleContext();

        // --- TransactionContext ---
        mapValues.put("amountTransactionFee", mapperUtil.getFieldValue(transactionContext.getReconciliation(), ReconciliationDTO::getDate, DEFAULT_EMPTY_VALUE));
        mapValues.put("settlementDate", mapperUtil.getFieldValue(transactionContext.getSettlementService().getSettlementServiceDates(), SettlementServiceDatesDTO::getSettlementDate, DEFAULT_EMPTY_VALUE));
        mapValues.put("captureDate", mapperUtil.getFieldValue(transactionContext, TransactionContextDTO::getCaptureDate, DEFAULT_EMPTY_VALUE));
        mapValues.put("merchantType", mapperUtil.getFieldValue(transactionContext, TransactionContextDTO::getMerchantCategoryCode, DEFAULT_EMPTY_VALUE));

        // --- Point Service Context ---
        mapValues.put("pointServiceEntryMode", mapperUtil.getFieldValue(posContext, PointOfServiceContextDTO::getCardDataEntryMode, DEFAULT_EMPTY_VALUE));

        // --- Sale Context ---
        String campaignData = saleContext.getAdditionalData().stream()
                .filter(dto -> "campaignData".equals(dto.getKey()))
                .map(AdditionalDataDTO::getValue)
                .findFirst()
                .orElse(DEFAULT_EMPTY_VALUE);
        mapValues.put("campaignData", campaignData);

        // --- Verification ---
        String pinData = input.getVerification().stream().findFirst()
                .map(VerificationDTO::getVerificationInformation).flatMap(list -> list.stream().findFirst())
                .map(VerificationInformationDTO::getValue)
                .map(ValueDTO::getPinData)
                .map(PINDataDTO::getEncryptedPINBlock)
                .orElse(DEFAULT_EMPTY_VALUE);
        mapValues.put("pinData", pinData);

        return mapValues;
    }
}