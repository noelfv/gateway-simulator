package com.bbva.orchestrator.parser.refactor.iso20022.mapper.strategy;


import com.bbva.gateway.dto.iso20022.*;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.parser.common.ISOSubFieldProcess;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.refactor.iso20022.mapper.SectionMappingStrategy;
import com.bbva.orchestrator.parser.refactor.utils.FieldProcessingService;
import com.bbva.orchestrator.parser.refactor.utils.ProcessMonitoringService;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

@Component
public class ContextMappingStrategy implements SectionMappingStrategy<ContextDTO> {

    private final FieldProcessingService fieldService;
    private final ProcessMonitoringService processMonitoringService;

    public ContextMappingStrategy(FieldProcessingService fieldService,
                                  ProcessMonitoringService processMonitoringService) {
        this.fieldService = fieldService;
        this.processMonitoringService = processMonitoringService;
    }

    @Override
    public ContextDTO map(ISO8583 input, Map<String, String> subFields) {
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
                fieldService.isNullOrEmptySubstring(input.getProcessingCode(), 0, 2)
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
}