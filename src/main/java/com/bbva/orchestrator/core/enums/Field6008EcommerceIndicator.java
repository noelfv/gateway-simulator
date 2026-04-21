package com.bbva.orchestrator.core.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum Field6008EcommerceIndicator {
    ENUM_00("00", "UNSP", null, null),
    ENUM_01("01", null, "MOTO", null),
    ENUM_02("02", "RCPT", null, null),
    ENUM_03("03", "INST", null, null),
    ENUM_04("04", null, null, null),
    ENUM_05("05", null, null, "THDS"),
    ENUM_06("06", null, null, "CSEC"),
    ENUM_07("07", null, null, "AUVA"),
    ENUM_08("08", null, null, "OTHN"),
    ENUM_09("09", null, null, null),


    RAW_UNSP("UNSP", "00", null, null),
    RAW_MOTO("MOTO", "01", null, null),
    RAW_RCPT("RCPT", "02", null, null),
    RAW_INST("INST", "03", null, null),
    RAW_THDS("THDS", "05", null, null),
    RAW_CSEC("CSEC", "06", null, null),
    RAW_AUVA("AUVA", "07", null, null),
    RAW_OTHN("OTHN", "08", null, null);

    private final String key;
    private final String transactionAttribute;
    private final String motoCode;
    private final String verificationKey;

    Field6008EcommerceIndicator(String key, String transactionAttribute, String motoCode, String verificationKey) {
        this.key = key;
        this.transactionAttribute = transactionAttribute;
        this.motoCode = motoCode;
        this.verificationKey = verificationKey;
    }

    private static final Map<String, Field6008EcommerceIndicator> lookupMap = new HashMap<>();
    static {
        for (Field6008EcommerceIndicator mf : Field6008EcommerceIndicator.values()) {
            if (mf.name().startsWith("ENUM_")) {
                lookupMap.put(mf.getKey(), mf);
            }
        }
    }

    public static Field6008EcommerceIndicator getByISO8583Value(String iso8583Value) {
        return lookupMap.get(iso8583Value);
    }

    public static String convertToISO8583FromTransactionAttribute(String transactionAttribute) {
        if ("UNSP".equals(transactionAttribute)) return "00";
        if ("RCPT".equals(transactionAttribute)) return "02";
        if ("INST".equals(transactionAttribute)) return "03";
        return null;
    }

    public static String convertToISO8583FromMotoCode(String motoCode) {
        if ("MOTO".equals(motoCode)) return "01";
        return null;
    }

    public static String convertToISO8583FromVerificationKey(String verificationKey) {
        if ("THDS".equals(verificationKey)) return "05";
        if ("CSEC".equals(verificationKey)) return "06";
        if ("AUVA".equals(verificationKey)) return "07";
        if ("OTHN".equals(verificationKey)) return "08";
        return null;
    }
}
