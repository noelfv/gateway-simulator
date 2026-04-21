package com.bbva.orchestrator.core.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Field6001TerminalType {

    // DE ISO 8583 A ISO 20022
    ENUM_0("0", "UNSP"),
    ENUM_1("1", "OTHP"),
    ENUM_2("2", "ATMT"),
    ENUM_3("3", "OTHP"),
    ENUM_4("4", "POST"),
    ENUM_5("5", "OTHN"),
    ENUM_7("7", "OTHN"),
    ENUM_9("9", "MPOS"),

    // DE ISO 20022 A ISO 8583
    RAW_UNSP_0("UNSP", "0"),
    RAW_OTHP_1("OTHP", "1"),
    RAW_ATMT_2("ATMT", "2"),
    RAW_OTHP_3("OTHP", "3"),
    RAW_POST_4("POST", "4"),
    RAW_OTHN_5("OTHN", "5"),
    RAW_OTHN_7("OTHN", "7"),
    RAW_MPOS_9("MPOS", "9");

    private final String key;
    private final String value;

    Field6001TerminalType(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private static final Map<String, String> lookupMap = new HashMap<>();
    static {
        for (Field6001TerminalType field : Field6001TerminalType.values()) {
            lookupMap.put(field.getKey(), field.getValue());
        }
    }

    public static String convertToISO20022(String iso8583Value) {
        return lookupMap.get(iso8583Value);
    }

    public static String convertToISO8583(String iso20022Value) {
        return lookupMap.get(iso20022Value);
    }
}
