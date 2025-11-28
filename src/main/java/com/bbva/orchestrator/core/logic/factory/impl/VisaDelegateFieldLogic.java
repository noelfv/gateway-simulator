package com.bbva.orchestrator.core.logic.factory.impl;

import com.bbva.orchestrator.configuration.ApplicationDataLocalCache;
import com.bbva.orchestrator.core.dto.ISO8583;
import com.bbva.orchestrator.core.fields.VisaISOField;
import com.bbva.orchestrator.core.logic.factory.NetworkDelegateFieldLogic;
import com.bbva.orchestrator.core.parser.iso8583.strategy.subfields.CompositeFixedFieldParser;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class VisaDelegateFieldLogic implements NetworkDelegateFieldLogic {

    private final ApplicationDataLocalCache applicationDataLocalCache;
    private final CompositeFixedFieldParser compositeFieldParser;

    public VisaDelegateFieldLogic(ApplicationDataLocalCache applicationDataLocalCache, CompositeFixedFieldParser compositeFieldParser) {
        this.applicationDataLocalCache = applicationDataLocalCache;
        this.compositeFieldParser = compositeFieldParser;
    }

    @Override
    public Map<String, String> parseSubfields(ISO8583 iso8583) {
        Map<String, String> allParsedSubfields = new HashMap<>();
        if(!requiredProcessSubFields(iso8583.getMessageType())){
            return allParsedSubfields;
        }
        //TODO: Validar el tratamiento de subcampos por tipo de mensaje si es necesario
        Map<String, String> mapSubField03 = compositeFieldParser.buildSubFieldsSpecific("03", iso8583.getProcessingCode());
        Map<String, String> mapSubField22 = compositeFieldParser.buildSubFieldsSpecific("22", iso8583.getPointServiceEntryMode());
        Map<String, String> mapSubField54 = compositeFieldParser.buildSubFieldsSpecific("54", iso8583.getAdditionalAmounts());
        Map<String, String> mapSubField61 = compositeFieldParser.buildSubFieldsSpecific("61", iso8583.getPosCardIssuer());
        allParsedSubfields.putAll(mapSubField03);
        allParsedSubfields.putAll(mapSubField22);
        allParsedSubfields.putAll(mapSubField54);
        allParsedSubfields.putAll(mapSubField61);

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
            String fieldName = VisaISOField.getById(fieldId).getName();

            // Comprueba si el campo existe en mapValues y lo añade al nuevo mapa.
            if (mapValues.containsKey(fieldName)) {
                mapValuesResponse.put(fieldName, mapValues.get(fieldName));
            }

            // Aquí puedes mantener tu lógica de validación.
            // Por ejemplo, si el campo es mandatorio y no se encontró.
            if ("M".equals(condition) && !mapValues.containsKey(fieldName)) {
                // Lanza una excepción o maneja el error como necesites.
                System.out.println("Error: Campo mandatorio faltante: " + fieldName + " (ID: " + fieldId + ")");
            }
        }

        mapValuesResponse.put("header",mapValues.get("header"));
        mapValuesResponse.put("messageType",mapValues.get("messageType"));
        return mapValuesResponse;
    }

    private boolean requiredProcessSubFields(String messageType) {
        return Set.of("0100","0101","0120","0400","0401","0420").contains(messageType);
    }
}