package com.bbva.orchestrator.core.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TransactionType {

    COMPRAS("00","COMPRAS"),
    RETIRO_DE_CAJERO("01","RETIRO DE CAJERO"),
    RETIRO_DE_OFICINA("02","RETIRO DE OFICINA"),
    ANULACION("04","ANUL. DEVOLUCION");

    private final String key;
    private final String value;


    TransactionType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private static final Map<String, String> lookupMap = new HashMap<>();

    static {
        for (TransactionType mf : TransactionType.values()) {
            lookupMap.put(mf.getKey(), mf.getValue());
        }
    }

    public static String getTransactionType(String key) {
        return lookupMap.get(key);
    }

}
