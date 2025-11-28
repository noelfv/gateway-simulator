package com.bbva.orchestrator.core.mapper.iso20022.strategy.impl;

import com.bbva.gateway.dto.iso20022.AdditionalInformationDTO;
import com.bbva.gateway.dto.iso20022.ProcessingResultDTO;
import com.bbva.gateway.dto.iso20022.ResultDataDTO;
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

        String approvalCode = input.getApprovalCode();
        ResultDataDTO resultData = input.getResultData();
        String respondeCode;
        String result;
        if(approvalCode != null && !approvalCode.isEmpty()){
            // ======== FIELD 38 (AUTHORIZATION IDENTIFICATION RESPONSE) ========
            mapValues.put("authorizationIdentificationResponse", approvalCode);
            respondeCode = resultData.getResult();
            result = mapperUtil.convertResponseCodeToResultData(networkName,respondeCode);
            mapValues.put("responseCode",result);

            //TODO En el tiempo se debe definir que otros campos se pueden mapear del processing result
            // ======== FIELD 48.87 TAG ========

            mapValues.put("48.87",findValueInAdditionalInformation(input,"cvv_validation_result"));
            mapValues.put("48.87_d",findValueInAdditionalInformation(input,"dcvv_validation_result"));
            mapValues.put("48.87_2",findValueInAdditionalInformation(input,"cvv2_validation_result"));
            mapValues.put("48.87_i",findValueInAdditionalInformation(input,"icvv_validation_result"));

        }else{
            respondeCode= resultData.getOtherResult();
            result = mapperUtil.convertResponseCodeToResultData(networkName,respondeCode);
            if(result==null || result.isEmpty()){
                throw new RuntimeException(resultData.getResultDetails());
            }
            // ======== FIELD 39 (RESPONDE CODE) ========
            mapValues.put("responseCode",result);
        }

        return mapValues;
    }

    private String findValueInAdditionalInformation(ProcessingResultDTO processingResult, String key) {
        if (processingResult.getAdditionalInformation() == null || key == null) {
            return null;
        }
        return processingResult.getAdditionalInformation().stream()
                .filter(data -> key.equals(data.getKey()))
                .map(AdditionalInformationDTO::getValue)
                .findFirst()
                .orElse(null);
    }
}
