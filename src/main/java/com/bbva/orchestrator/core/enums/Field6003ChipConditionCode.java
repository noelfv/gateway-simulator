package com.bbva.orchestrator.core.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("all")
@Getter
public enum Field6003ChipConditionCode {


    ENUM_0("0", "FALSE", null),
    ENUM_1("1", "TRUE", "EDIP"),
    ENUM_2("2", "TRUE", "TERI"),


    RAW_FALSE("FALSE", "0", null),
    RAW_TRUE_EDIP("TRUE", "1", "EDIP"),
    RAW_TRUE_TERI("TRUE", "2", "TERI"),
    RAW_TRUE_CIIA("TRUE", "2", "CIIA");

    private final String key;
    private final String iccFallbackIndicator;
    private final String iccFallbackReasonCode;
    private static final String FALSE = "FALSE";

    Field6003ChipConditionCode(String key, String iccFallbackIndicator, String iccFallbackReasonCode) {
        this.key = key;
        this.iccFallbackIndicator = iccFallbackIndicator;
        this.iccFallbackReasonCode = iccFallbackReasonCode;
    }


    private static final Map<String, Field6003ChipConditionCode> lookupMap = new HashMap<>();
    static {
        for (Field6003ChipConditionCode mf : Field6003ChipConditionCode.values()) {
            if (mf.name().startsWith("ENUM_")) {
                lookupMap.put(mf.getKey(), mf);
            }
        }
    }

    public static Field6003ChipConditionCode getByISO8583Value(String iso8583Value) {
        return lookupMap.get(iso8583Value);
    }

    public static String convertToISO8583(String iccFallbackIndicator, String iccFallbackReasonCode) {
        if (FALSE.equals(iccFallbackIndicator)) {
            return "0";
        }
        if ("TRUE".equals(iccFallbackIndicator)) {
            if ("EDIP".equals(iccFallbackReasonCode)) {
                return "1";
            } else if ("TERI".equals(iccFallbackReasonCode) || "CIIA".equals(iccFallbackReasonCode)) {
                return "2";
            }
        }
        return null;
    }
}
