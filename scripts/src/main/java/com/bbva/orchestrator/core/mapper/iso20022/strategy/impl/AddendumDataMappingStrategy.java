package com.bbva.orchestrator.core.mapper.iso20022.strategy.impl;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.mapper.iso20022.strategy.SectionMappingStrategy;
import com.bbva.orchestrator.core.utils.ISO8583ContextService;
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
    public AddendumDataDTO mapper(ISO8583 input, Map<String, String> subFields) {

        CustomDataLocalDTO x;
        List<AdditionalDataDTO> additionalDataList = new ArrayList<>();
        //TODO Se debe considerar la siguiente logica, para los mensaje de entrada se debe de crear los
        // los objetos addendumData y customData con la siguiente logica:
        // 1. AddendumData:    MGSTYPE, ISO8583_HOST, ISO8583
        // 2. CustomDataLocal: MGSTYPE, ISO8583, FLOWTYPE(para el caso de flowType siempre seria ASYNC)
        additionalDataList.add(AdditionalDataDTO.builder()
                .key("MSGTYPE")
                .value(input.getMessageType())
                .build());

        additionalDataList.add(AdditionalDataDTO.builder()
                .key("ISO8583_HOST")
                .value(input.getOriginalMessage())
                .build());

        additionalDataList.add(AdditionalDataDTO.builder()
                .key("ISO8583")
                .value(iso8583ContextService.getISO8583(GrpcHeadersInfo.getTraceId()))
                .build());

        return AddendumDataDTO.builder()
                .additionalData(additionalDataList)
                .build();
    }

    @Override
    public Map<String, String> unMapper(String networkName,AddendumDataDTO input) {
        return Map.of();
    }
}