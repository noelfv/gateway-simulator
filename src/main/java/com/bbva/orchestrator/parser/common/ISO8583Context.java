package com.bbva.orchestrator.parser.common;

import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.gateway.sensitivedata.SensitiveDataHandler;
import com.bbva.orchestrator.parser.factory.MessageParser;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import java.util.HashMap;
import java.util.Map;

public class ISO8583Context {

    private static final Map<String, String> iso8583Map= new HashMap<>();
    private static final String PRIMARY_ACCOUNT_NUMBER = "primaryAccountNumber";
    private static final String MASK_CHAR = "*";
    private static final String DEFAULT_MASK_CHAR = "0";


    private ISO8583Context() {
    }

    public static void storeISO8583(ISO8583 iso8583, Map<String, String> mapValuesOrigin, MessageParser messageParser) {

        if(iso8583.getMessageType().startsWith("08") || iso8583.getMessageType().startsWith("019") ){
            iso8583Map.put(GrpcHeadersInfo.getTraceId(), iso8583.getMessageType());
            return;
        }

        Map<String, String> mapSensitiveFields= maskSensitiveFields(mapValuesOrigin);
        String tramSensitiveFields=messageParser.unParser(mapSensitiveFields, true);

        iso8583Map.put(GrpcHeadersInfo.getTraceId(), tramSensitiveFields);
    }

    private static Map<String, String> maskSensitiveFields(Map<String, String> mapValues) {
        Map<String, String> mapMaskedValues = new HashMap<>(mapValues);
        // Define the fields to mask
        String[] sensitiveFields = {
                PRIMARY_ACCOUNT_NUMBER,
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

    private static String maskWithDefaultChar(String value) {
        return value == null ? null : ISO8583Context.DEFAULT_MASK_CHAR.repeat(value.length());
    }

    public static String getISO8583(String key) {
        return iso8583Map.get(key);
    }

    public static void removeStoreId() {
        iso8583Map.remove(GrpcHeadersInfo.getTraceId());
    }

}