package com.bbva.gui.commons;

import com.bbva.gui.dto.ParseResult;

import java.util.HashMap;
import java.util.Map;

public final class ParseProcessor {

    private static final String[] INTERNAL_KEYS = {
            "networkName", "plainTextPCI", "header", "rejectFlag", "transactionType", "binCode"
    };

    private ParseProcessor() {
    }

    public static ParseResult process(Map<String, String> mapValues, String peerId) {
        Map<String, String> clean = new HashMap<>(mapValues);
        for (String key : INTERNAL_KEYS) {
            clean.remove(key);
        }
        Map<String, String> fieldsById = new HashMap<>();
        for (Map.Entry<String, String> entry : clean.entrySet()) {
            String fieldId = ISOFieldFinder.findFieldIdByName(entry.getKey(), peerId);
            fieldsById.put(fieldId != null ? fieldId : entry.getKey(), entry.getValue());
        }
        return new ParseResult(clean, fieldsById);
    }

    public static ParseResult process(Map<String, String> mapValues) {
        return process(mapValues, "peer02");
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
