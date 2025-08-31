package com.bbva.orchestrator.core.mapper.iso20022.strategy.impl;

import com.bbva.gateway.dto.iso20022.ProcessingResultDTO;
import com.bbva.gateway.dto.iso20022.ResultDataDTO;
import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.mapper.iso20022.strategy.SectionMappingStrategy;
import com.bbva.orchestrator.core.utils.FieldProcessingService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ProcessingResultMappingStrategy implements SectionMappingStrategy<ProcessingResultDTO> {

    private final FieldProcessingService fieldService;

    public ProcessingResultMappingStrategy(FieldProcessingService fieldService) {
        this.fieldService = fieldService;
    }

    @Override
    public ProcessingResultDTO mapper(ISO8583 input, Map<String, String> subFields) {
        throw new UnsupportedOperationException("Mapping from ISO8583 to ProcessingResultDTO is not implemented yet.");
    }

    @Override
    public Map<String, String> unMapper(ProcessingResultDTO input) {
        Map<String, String> mapValues = new HashMap<>();
        //De manera provisional si no viene el processing result o el result data se retorna un map vacio
        if(input==null){
            return Map.of();
        }

        ResultDataDTO resultData = input.getResultData();
        String respondeCode= resultData.getResult();//TODO ACa se peude hacer la conversion
        //String result = fieldService.convertResponseCode(respondeCode);
        if(respondeCode.equals("00")){
            // ======== FIELD 38 (AUTHORIZATION IDENTIFICATION RESPONSE) ========
            mapValues.put("respondeCode", input.getApprovalCode());
        }
        // ======== FIELD 39 (RESPONSE CODE) ========
        mapValues.put("respondeCode", respondeCode);

        //TODO En el tiempo se debe definir que otros campos se pueden mapear del processing result

        return Map.of();
    }
}
