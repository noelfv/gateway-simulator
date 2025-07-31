package com.bbva.orchestrator.parser.refactor.iso20022.mapper.strategy;

import com.bbva.gateway.dto.iso20022.AddendumDataDTO;
import com.bbva.gateway.dto.iso20022.AdditionalDataDTO;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.refactor.iso20022.mapper.SectionMappingStrategy;
import com.bbva.orchestrator.parser.refactor.utils.ISO8583ContextService;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AddendumDataMappingStrategy implements SectionMappingStrategy<AddendumDataDTO> {

    private final ISO8583ContextService iso8583ContextService;

    public AddendumDataMappingStrategy(ISO8583ContextService iso8583ContextService) {
        this.iso8583ContextService = iso8583ContextService;
    }

    @Override
    public AddendumDataDTO map(ISO8583 input, Map<String, String> subFields) {
        List<AdditionalDataDTO> additionalDataList = new ArrayList<>();

        // Mensaje ISO8583 original
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("ISO8583_HOST")
                .value(input.getOriginalMessage())
                .build());

        // Mensaje ISO8583 desde contexto (si está disponible)
        String iso8583FromContext = iso8583ContextService.getISO8583(GrpcHeadersInfo.getTraceId());
        if (iso8583FromContext != null && !iso8583FromContext.isEmpty()) {
            additionalDataList.add(AdditionalDataDTO.builder()
                    .key("ISO8583")
                    .value(iso8583FromContext)
                    .build());
        }

        // Tipo de mensaje (Message Type)
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("MSGTYPE")
                .value(input.getMessageType())
                .build());

        return AddendumDataDTO.builder()
                .additionalData(additionalDataList)
                .build();
    }
}