package com.bbva.gui.utils;

import com.bbva.gui.dto.ParseResult;

import java.util.HashMap;
import java.util.Map;

public final class ParseProcessor {

    private static final String[] INTERNAL_KEYS = {
        "networkName", "plainTextPCI", "header", "rejectFlag", "transactionType", "binCode"
    };

    private ParseProcessor() {}

    public static ParseResult process(Map<String, String> mapValues) {
        for (String key : INTERNAL_KEYS) {
            mapValues.remove(key);
        }
        Map<String, String> fieldsById = new HashMap<>();
        for (Map.Entry<String, String> entry : mapValues.entrySet()) {
            String fieldId = ISOFieldFinder.findFieldIdByName(entry.getKey());
            fieldsById.put(fieldId != null ? fieldId : entry.getKey(), entry.getValue());
        }
        return new ParseResult(mapValues, fieldsById);
    }

    public static ParseResult processTLV(Map<String, String> mapValues) {
        Map<String, String> fieldsById = new HashMap<>();
        for (Map.Entry<String, String> entry : mapValues.entrySet()) {
            String fieldId = ISOFieldFinder.findFieldTLVIdByName(entry.getKey());
            fieldsById.put(fieldId != null ? fieldId : entry.getKey(), entry.getValue());
        }
        return new ParseResult(mapValues, fieldsById);
    }
}
