package com.bbva.orchestrator.core.mapper.iso20022.strategy.impl;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.orchestrator.core.dto.ISO8583;
import com.bbva.orchestrator.core.exception.MapperFieldsException;
import com.bbva.orchestrator.core.mapper.iso20022.strategy.SectionMappingStrategy;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class AddendumDataMappingStrategy implements SectionMappingStrategy<AddendumDataDTO> {

    @Override
    public AddendumDataDTO mapper(ISO8583 input, Map<String, String> subFields) {

        try {
            CustomDataLocalDTO x;
            List<AdditionalDataDTO> additionalDataList = new ArrayList<>();
            //TODO Se debe considerar la siguiente logica, para los mensaje de entrada se debe de crear los
            // los objetos addendumData y customData con la siguiente logica:
            // 1. AddendumData:    MGSTYPE, ISO8583_HOST, ISO8583
            // 2. CustomDataLocal: MGSTYPE, ISO8583, FLOWTYPE(para el caso de flowType siempre seria ASYNC)
            additionalDataList.add(AdditionalDataDTO.builder()
                    .key("UNSP")
                    .value(input.getMessageType())
                    .build());

            additionalDataList.add(AdditionalDataDTO.builder()
                    .key("ISO8583_HOST")
                    .value(input.getOriginalMessage())
                    .build());

            return AddendumDataDTO.builder()
                    .additionalData(additionalDataList)
                    .build();

        } catch (RuntimeException e) {
            // Manejo de excepciones, puedes lanzar una RuntimeException o una excepción personalizada
            throw new MapperFieldsException("PGWP-00121", "Error al mapear AddendumDataDTO desde ISO8583", e);
        }
    }

    public AddendumDataDTO mapper_response(ISO8583 input, Map<String, String> subFields) {

        List<AdditionalDataDTO> additionalDataList = new ArrayList<>();

        additionalDataList.add(AdditionalDataDTO.builder()
                .key("ISO8583_HOST")
                .value(input.getOriginalMessage())
                .build());

        additionalDataList.add(AdditionalDataDTO.builder()
                .key("ISO8583")
                .value(input.getPlainTextPCI())
                .build());

        additionalDataList.add(AdditionalDataDTO.builder()
                .key("UNSP")
                .value(input.getMessageType())
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