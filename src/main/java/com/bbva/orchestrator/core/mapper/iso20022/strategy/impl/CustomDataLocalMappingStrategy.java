package com.bbva.orchestrator.core.mapper.iso20022.strategy.impl;

import com.bbva.gateway.dto.iso20022.AdditionalDataCustomDataLocalDTO;
import com.bbva.gateway.dto.iso20022.CustomDataLocalDTO;
import com.bbva.gateway.dto.iso20022.RequestDTO;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.mapper.iso20022.strategy.SectionMappingStrategy;
import com.bbva.orchestrator.core.utils.ISO8583ContextService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CustomDataLocalMappingStrategy implements SectionMappingStrategy<CustomDataLocalDTO> {

    private final ISO8583ContextService iso8583ContextService;

    public CustomDataLocalMappingStrategy(ISO8583ContextService iso8583ContextService) {
        this.iso8583ContextService = iso8583ContextService;
    }

    @Override
    public CustomDataLocalDTO mapper(ISO8583 input, Map<String, String> subFields) {

        //TODO Se debe considerar la siguiente logica, para los mensaje de entrada se debe de crear los
        // los objetos addendumData y customData con la siguiente logica:
        // 1. AddendumData:    MGSTYPE, ISO8583_HOST, ISO8583
        // 2. CustomDataLocal: MGSTYPE, ISO8583, FLOWTYPE(para el caso de flowType siempre seria ASYNC)
        List<AdditionalDataCustomDataLocalDTO> additionalDataCustomDataLocalList = new ArrayList<>();

        // 1. Crear y añadir el primer par (MSGTYPE)
        RequestDTO msgTypeRequest = RequestDTO.builder()
                .key("MSGTYPE")
                .value(input.getMessageType())
                .build();
        additionalDataCustomDataLocalList.add(AdditionalDataCustomDataLocalDTO.builder()
                .request(msgTypeRequest)
                .build());

        // 2. Crear y añadir el segundo par (ISO8583)
        RequestDTO iso8583Request = RequestDTO.builder()
                .key("ISO8583")
                .value(iso8583ContextService.getISO8583(GrpcHeadersInfo.getTraceId()))
                .build();
        additionalDataCustomDataLocalList.add(AdditionalDataCustomDataLocalDTO.builder()
                .request(iso8583Request)
                .build());

        // 3. Crear y añadir el tercer par (FLOWTYPE)
        RequestDTO flowTypeRequest = RequestDTO.builder()
                .key("FLOWTYPE")
                .value("ASYNC")
                .build();
        additionalDataCustomDataLocalList.add(AdditionalDataCustomDataLocalDTO.builder()
                .request(flowTypeRequest)
                .build());

        // 4. Construir el DTO final con la lista poblada
        return CustomDataLocalDTO.builder()
                .additionalData(additionalDataCustomDataLocalList)
                .build();

    }

    @Override
    public Map<String, String> unMapper(CustomDataLocalDTO input) {

        input.getAdditionalData().get(0).getRequest().getKey();

        return Map.of();
    }
}