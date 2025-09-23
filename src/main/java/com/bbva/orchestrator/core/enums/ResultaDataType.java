package com.bbva.orchestrator.core.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum ResultaDataType {

    APPR("0100","APPR"),
    PRCS("0120","PRCS"),
    PRCS_0420("0420","PRCS"),
    SUCC("0400","SUCC");


    private final String key;
    private final String value;

    ResultaDataType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    // Método estático para buscar eficientemente por clave
    private static final Map<String, String> lookupMap = new HashMap<>();
    static {
        for (ResultaDataType mf : ResultaDataType.values()) {
            lookupMap.put(mf.getKey(), mf.getValue());
        }
    }

    public static String convertResultDataType(String resultDataType) {
        return lookupMap.get(resultDataType);
    }

}
