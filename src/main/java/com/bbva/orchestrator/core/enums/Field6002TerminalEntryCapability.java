package com.bbva.orchestrator.core.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Field6002TerminalEntryCapability {

    // DE ISO 8583 A ISO 20022
    ENUM_0("0", "UNKW"),
    ENUM_1("1", "MLEY"),
    ENUM_2("2", "MGST"),
    ENUM_3("3", "QRCD"),
    ENUM_4("4", "OCRR"),
    ENUM_5("5", "ICCY"),
    ENUM_8("8", "CTLS"),
    ENUM_9("9", "KEEN"),

   // DE ISO 20022 A ISO 8583
    RAW_UNKW_0("UNKW", "0"),
    RAW_MLEY_1("MLEY", "1"),
    RAW_MGST_2("MGST", "2"),
    RAW_QRCD_3("QRCD", "3"),
    RAW_OCRR_4("OCRR", "4"),
    RAW_ICCY_5("ICCY", "5"),
    RAW_ICPY_5("ICPY", "5"),
    RAW_CTLS_8("CTLS", "8"),
    RAW_KEEN_9("KEEN", "9");

    private final String key;
    private final String value;

    Field6002TerminalEntryCapability(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private static final Map<String, String> lookupMap = new HashMap<>();
    static {
        for (Field6002TerminalEntryCapability field : Field6002TerminalEntryCapability.values()) {
            lookupMap.put(field.getKey(), field.getValue());
        }
    }

    public static String convertToISO20022(String iso8583Value) {
        String result = lookupMap.get(iso8583Value);
        return result != null ? result : null;
    }

    public static String convertToISO8583(String iso20022Value) {
        return lookupMap.get(iso20022Value);
    }
}
