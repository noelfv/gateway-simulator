package com.bbva.orchestrator.core.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum CardDataEntryMode {

    // Define los valores raw y enums
    ENUM_00("00", "UNSP"),
    ENUM_01("01", "MLEY"),
    ENUM_02("02", "MGST"),
    ENUM_03("03", "OPTC"),
    ENUM_04("04", "OCRR"),
    ENUM_05("05", "ICCY"),
    ENUM_07("07", "ICPY"),
    ENUM_10("20", "DFLE"),
    // Define los valores enums y raw
    RAW_UNSP("UNSP", "00"),
    RAW_MLEY("MLEY", "01"),
    RAW_MGST("MGST", "02"),
    RAW_OPTC("OPTC", "03"),
    RAW_OCRR("OCRR", "04"),
    RAW_ICCY("ICCY", "05"),
    RAW_ICPY("ICPY", "07"),
    RAW_DFLE("DFLE", "20");

    private final String key;
    private final String value;

    CardDataEntryMode(String key, String value) {
        this.key = key;
        this.value = value;
    }

    // Método estático para buscar eficientemente por clave
    private static final Map<String, String> lookupMap = new HashMap<>();
    static {
        for (CardDataEntryMode mf : CardDataEntryMode.values()) {
            lookupMap.put(mf.getKey(), mf.getValue());
        }
    }

    public static String convertCardDataEntryMode(String typeMessage) {
        return lookupMap.get(typeMessage);
    }

    public static String convertTypeCardDataEntryMode(String cardDataEntryMode) {
        return lookupMap.get(cardDataEntryMode);
    }

}
