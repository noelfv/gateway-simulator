package com.bbva.orchestrator.parser.refactor.iso20022.mapper.impl;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchestrator.parser.common.ISO8583Context;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.refactor.iso20022.mapper.ISO8583ToISO20022Mapper;
import com.bbva.orchestrator.parser.refactor.iso20022.mapper.strategy.*;
import com.bbva.orchestrator.parser.refactor.utils.FieldUtils;
import com.bbva.orchestrator.parser.refactor.utils.MonitoringService;
import com.bbva.orchestrator.processes.ProcessingResult;
import com.bbva.orchestrator.validations.FieldLocalCodeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DefaultISO8583ToISO20022Mapper implements ISO8583ToISO20022Mapper {

    private final EnvironmentMappingStrategy environmentStrategy;
    private final TransactionMappingStrategy transactionStrategy;
    private final ContextMappingStrategy contextStrategy;
    private final SupplementaryDataMappingStrategy supplementaryDataStrategy;
    private final SecurityTrailerMappingStrategy securityTrailerStrategy;
    private final AddendumDataMappingStrategy addendumDataStrategy;
    private final ProtectedDataMappingStrategy protectedDataStrategy;
    private final TraceDataMappingStrategy traceDataStrategy;
    private final MonitoringService monitoringService;
    //private final HeaderService headerService;*/

    @Override
    public ISO20022 translate(ISO8583 input, Map<String, String> subFields) {
        try {
            // Mapeo paralelo (opcional, si necesitas alto rendimiento)
            EnvironmentDTO environment = environmentStrategy.map(input, subFields);
            TransactionDTO transaction = transactionStrategy.map(input, subFields);
            ContextDTO context = contextStrategy.map(input, subFields);
            AddendumDataDTO addendumData = addendumDataStrategy.map(input, subFields);
            List<SupplementaryDataDTO> supplementaryData = supplementaryDataStrategy.map(input, subFields);
            List<TraceDataDTO> traceData = traceDataStrategy.map(input, subFields);
            List<ProtectedDataDTO> protectedData = protectedDataStrategy.map(input, subFields);
            SecurityTrailerDTO securityTrailer = securityTrailerStrategy.map(input, subFields);

            LogsTraces.writeInfo("requestMessage %s|%s"
                    .formatted(ISO8583Context.getISO8583(GrpcHeadersInfo.getTraceId()),
                            transaction.getTransactionId().getTransactionReference()));

            // Construir el objeto ISO20022 final usando su builder, solo con objetos no nulos
            ISO20022.ISO20022Builder iso20022Builder = ISO20022.builder()
                    .networkName(GrpcHeadersInfo.getNetwork())
                    .messageFunction(FieldLocalCodeMapper.getCode(FieldUtils.TYPE_MESSAGE, "in", input.getMessageType(), GrpcHeadersInfo.getNetwork()))
                    .socketPort(GrpcHeadersInfo.getPort())
                    .traceData(traceData)
                    .transaction(transaction)
                    .environment(environment)
                    .addendumData(addendumData)
                    .monitoring(monitoringService.createMonitoring(input, environment, transaction));

            if (Objects.nonNull(context)) { // Asumiendo que context es un objeto DTO
                iso20022Builder.context(context);
            }
            if (Objects.nonNull(protectedData)) {
                iso20022Builder.protectedData(protectedData);
            }
            if (Objects.nonNull(securityTrailer)) {
                iso20022Builder.securityTrailer(securityTrailer);
            }
            if (Objects.nonNull(supplementaryData)) {
                iso20022Builder.supplementaryData(supplementaryData);
            }

            //  Solo agregar processingResult si es un mensaje de respuesta
            if (isResponseMessageType(input.getMessageType())) {
                iso20022Builder.processingResult(ProcessingResult.createProcessingResult(input));
            }

            return iso20022Builder.build();

        } catch (Exception e) {
            LogsTraces.writeWarning("Error mapping ISO8583 to ISO20022");
            return buildFallbackResponse(input);
        }
    }

    // Metodo auxiliar para verificar si es un mensaje de respuesta
    private boolean isResponseMessageType(String messageType) {
        if (messageType == null || messageType.length() < 4) return false;
        return Set.of("0110", "0120", "0130", "0410", "0430", "0210").contains(messageType); //Validar si hay que incluir el 0130
    }


    //Esto solo aplica para el passtrhoug luego se debe de evaluar si se debe de eliminar
    private ISO20022 buildFallbackResponse(ISO8583 input) {
        return ISO20022.builder()
                .networkName(GrpcHeadersInfo.getNetwork())
                .messageFunction(FieldLocalCodeMapper.getCode(FieldUtils.TYPE_MESSAGE, "in", input.getMessageType(), GrpcHeadersInfo.getNetwork()))
                .socketPort(GrpcHeadersInfo.getPort())
                .environment(EnvironmentDTO.builder()
                        .card(CardDTO.builder()
                                .pan(input.getPrimaryAccountNumber())
                                .build())
                        .build())
                .addendumData(AddendumDataDTO.builder()
                        .additionalData(List.of(
                                AdditionalDataDTO.builder()
                                        .key("ISO8583_HOST")
                                        .value(input.getOriginalMessage())
                                        .build(),
                                AdditionalDataDTO.builder()
                                        .key("MSGTYPE")
                                        .value(input.getMessageType())
                                        .build()
                        ))
                        .build())
                .build();
    }
}