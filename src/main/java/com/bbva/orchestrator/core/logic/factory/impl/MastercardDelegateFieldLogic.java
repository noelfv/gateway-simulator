package com.bbva.orchestrator.core.logic.factory.impl;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchestrator.configuration.ApplicationDataLocalCache;
import com.bbva.orchestrator.core.dto.ISO8583;
import com.bbva.orchestrator.core.fields.MastercardISOField;
import com.bbva.orchestrator.core.logic.factory.NetworkDelegateFieldLogic;
import com.bbva.orchestrator.core.logic.process.MastercardProcessSubField;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class MastercardDelegateFieldLogic implements NetworkDelegateFieldLogic {

    private final ApplicationDataLocalCache applicationDataLocalCache;
    private final MastercardProcessSubField mastercardISOSubFieldParser;

    public MastercardDelegateFieldLogic(ApplicationDataLocalCache applicationDataLocalCache, MastercardProcessSubField mastercardISOSubFieldParser) {
        this.applicationDataLocalCache = applicationDataLocalCache;
        this.mastercardISOSubFieldParser = mastercardISOSubFieldParser;
    }

    @Override
    public Map<String, String> parseSubfields(ISO8583 iso8583) {
        Map<String, String> allParsedSubfields = new HashMap<>();
        if(!requiredProcessSubFields(iso8583.getMessageType())){
            return allParsedSubfields;
        }
        allParsedSubfields=mastercardISOSubFieldParser.parseSubfields(iso8583);

        return allParsedSubfields;
    }


    @Override
    public Map<String, String> applyLogicFields(Map<String, String> mapValues) {
        String networkName= mapValues.get("networkName");
        String messageType= mapValues.get("messageType");
        Map<Integer,String> mapFieldsResponse = applicationDataLocalCache.getFieldsResponse(networkName,messageType);
        Map<String, String> mapValuesResponse = new HashMap<>();
        for (Map.Entry<Integer, String> entry : mapFieldsResponse.entrySet()) {
            Integer fieldId = entry.getKey();
            String condition = entry.getValue();
            String fieldName = MastercardISOField.getById(fieldId).getName();

            // Comprueba si el campo existe en mapValues y lo añade al nuevo mapa.
            if (mapValues.containsKey(fieldName)) {
                mapValuesResponse.put(fieldName, mapValues.get(fieldName));
            }

            // Aquí puedes mantener tu lógica de validación.
            // Por ejemplo, si el campo es mandatorio y no se encontró.
            if ("M".equals(condition) && !mapValues.containsKey(fieldName)) {
                // Lanza una excepción o maneja el error como necesites.
                //throw new MandatoryFieldsException();
                LogsTraces.writeError("Error: Campo mandatorio faltante: " + fieldName + " (ID: " + fieldId + ")");
            }
        }

        mapValuesResponse.put("messageType",mapValues.get("messageType"));
        mapValuesResponse.put("additionalDataRetailer",applyLogicField48(messageType,mapValuesResponse));
        return mapValuesResponse;

    }

    private String applyLogicField48(String messageType, Map<String, String> mapValues) {
        String field48 = mapValues.get("additionalDataRetailer");
        if (messageType.contains("0110") && field48 != null) {
            String field48Tag87 = mapValues.get("48.87");
            if ("true".equalsIgnoreCase(field48Tag87)) {
                return field48 + "F8F7F0F1D4";
            }
            if ("false".equalsIgnoreCase(field48Tag87)) {
                return field48 + "F8F7F0F1D5";
            }
        }
        return field48;
    }

    private boolean requiredProcessSubFields(String messageType) {
        return Set.of("0100","0110","0400","0410").contains(messageType);
    }

}
