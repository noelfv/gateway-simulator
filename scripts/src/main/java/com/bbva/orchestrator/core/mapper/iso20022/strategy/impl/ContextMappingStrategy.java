package com.bbva.orchestrator.core.mapper.iso20022.strategy.impl;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.core.commons.ISOSubFieldProcess;
import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.mapper.iso20022.strategy.SectionMappingStrategy;
import com.bbva.orchestrator.core.utils.MapperUtil;
import com.bbva.orchestrator.core.utils.ProcessMonitoringService;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ContextMappingStrategy implements SectionMappingStrategy<ContextDTO> {

    private final MapperUtil mapperUtil;
    private final ProcessMonitoringService processMonitoringService;

    public ContextMappingStrategy(MapperUtil mapperUtil,
                                  ProcessMonitoringService processMonitoringService) {
        this.mapperUtil = mapperUtil;
        this.processMonitoringService = processMonitoringService;
    }

    @Override
    public ContextDTO mapper(ISO8583 input, Map<String, String> subFields) {
        // === TRANSACTION CONTEXT ===
        ReconciliationDTO reconciliation = ReconciliationDTO.builder()
                .date(input.getAmountTransactionFee())
                .build();

        SettlementServiceDatesDTO settlementServiceDates = SettlementServiceDatesDTO.builder()
                .settlementDate(input.getSettlementDate())
                .build();

        SettlementServiceDTO settlementService = SettlementServiceDTO.builder()
                .settlementServiceDates(settlementServiceDates)
                .build();

        String operationType = processMonitoringService.operationTypeValue(
                input.getMessageType(),
                mapperUtil.isNullOrEmptySubstring(input.getProcessingCode(), 0, 2)
        );

        String channel = processMonitoringService.channelValue(
                input.getMessageType(),
                ISOSubFieldProcess.channelECommerceIndicator(input, subFields, GrpcHeadersInfo.getNetwork()),
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
        Boolean eCommerceIndicator = ISOSubFieldProcess.channelECommerceIndicator(input, subFields, GrpcHeadersInfo.getNetwork());

        PointOfServiceContextDTO pointOfServiceContext = PointOfServiceContextDTO.builder()
                .cardDataEntryMode(input.getPointServiceEntryMode())
                .ecommerceIndicator(eCommerceIndicator)
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

        VerificationDTO verification = VerificationDTO.builder()
                .verificationInformation(List.of(verificationInfo))
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