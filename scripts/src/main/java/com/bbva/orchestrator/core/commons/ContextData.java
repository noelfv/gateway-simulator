package com.bbva.orchestrator.core.commons;

import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ContextData {

    private final Map<String, String> contextMap = new ConcurrentHashMap<>();
    private static final String PRIMARY_ACCOUNT_NUMBER = "primaryAccountNumber";
    private static final String MASK_CHAR = "*";
    private static final String DEFAULT_MASK_CHAR = "0";

    /**
     * Almacena la trama ISO8583 en su versión enmascarada y original.
     */
    public void storeISO8583(ISO8583 iso8583, Map<String, String> rawFields, ISO8583DelegateParser delegateParser) {

        if (iso8583.getMessageType().startsWith("08") || iso8583.getMessageType().startsWith("019")) {
            contextMap.put(GrpcHeadersInfo.getTraceId(), iso8583.getMessageType());
            return;
        }

        // Generar trama enmascarada
        Map<String, String> maskedFields = maskSensitiveFields(rawFields);
        String maskedMessage = delegateParser.unParserPlainText(maskedFields);

        contextMap.put(GrpcHeadersInfo.getTraceId(), maskedMessage);
    }


    public String createFramePlainText( Map<String, String> mapFieldsValue, ISO8583DelegateParser delegateParser) {

        if (mapFieldsValue.get("messageType").startsWith("08") || mapFieldsValue.get("messageType").startsWith("019")) {
            return mapFieldsValue.get("messageType");
        }
        // Generar trama enmascarada
        Map<String, String> maskedFields = maskSensitiveFields(mapFieldsValue);
        return delegateParser.unParserPlainText(maskedFields);

    }


    private Map<String, String> maskSensitiveFields(Map<String, String> mapValues) {
        Map<String, String> mapMaskedValues = new HashMap<>(mapValues);
        // Define the fields to mask
        String[] sensitiveFields = {
                "primaryAccountNumber",
                "trackOneData",
                "trackTwoData"
        };

        for (String fieldName : sensitiveFields) {
            mapMaskedValues.computeIfPresent(fieldName, (key, value) -> {
                if (PRIMARY_ACCOUNT_NUMBER.equals(key)) {
                    return SensitiveDataHandler.obfuscate(value, "left", MASK_CHAR, 6, 6);
                } else {
                    return maskWithDefaultChar(value);
                }
            });
        }

        return mapMaskedValues;
    }

    private  String maskWithDefaultChar(String value) {
        return value == null ? null : DEFAULT_MASK_CHAR.repeat(value.length());
    }

    public  String getISO8583(String key) {
        return contextMap.get(key);
    }

    public  void removeStoreId() {
        contextMap.remove(GrpcHeadersInfo.getTraceId());
    }
}