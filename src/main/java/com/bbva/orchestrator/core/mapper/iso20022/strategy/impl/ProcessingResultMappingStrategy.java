package com.bbva.orchestrator.core.mapper.iso20022.strategy.impl;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.orchestrator.core.dto.ISO8583;
import com.bbva.orchestrator.core.mapper.iso20022.strategy.SectionMappingStrategy;
import com.bbva.orchestrator.core.utils.MapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class ProcessingResultMappingStrategy implements SectionMappingStrategy<ProcessingResultDTO> {

    private final MapperUtil mapperUtil;

    @Override
    public ProcessingResultDTO mapper(ISO8583 input, Map<String, String> subFields) {
        throw new UnsupportedOperationException("Mapping from ISO8583 to ProcessingResultDTO is not implemented yet.");
    }

    @Override
    public Map<String, String> unMapper(String networkName,ProcessingResultDTO input) {
        Map<String, String> mapValues = new HashMap<>();
        //TODO De manera provisional si no viene el processing result o el result data se retorna un map vacio
        if(input==null){
            return Map.of();
        }

        ResultDataDTO resultData = input.getResultData();
        String respondeCode= resultData.getResult();//TODO Aca se peude hacer la conversion
        String result = mapperUtil.convertResponseCodeToResultData(networkName,respondeCode);

        // ======== FIELD 39 (RESPONDE CODE) ========
        mapValues.put("responde_code",result);

        if(result.equals("00")){
            // ======== FIELD 38 (AUTHORIZATION IDENTIFICATION RESPONSE) ========
            mapValues.put("authorizationIdentificationResponse", input.getApprovalCode());
        }

        //TODO En el tiempo se debe definir que otros campos se pueden mapear del processing result
        // ======== FIELD 48.87 TAG ========
        mapValues.put("48.87",findValueInAdditionalInformation(input,"cvv_validation_result"));

        return mapValues;
    }

    private String findValueInAdditionalInformation(ProcessingResultDTO processingResult, String key) {
        if (processingResult.getAdditionalInformation() == null) {
            return null;
        }
        return processingResult.getAdditionalInformation().stream()
                .filter(data -> key.equals(data.getKey()))
                .map(AdditionalInformationDTO::getValue)
                .findFirst()
                .orElse(null);
    }
}
