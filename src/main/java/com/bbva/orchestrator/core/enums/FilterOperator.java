package com.bbva.orchestrator.core.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum FilterOperator {

    PURCHASE("00","PURCHASE"),
    WTHDMON("01","WTHDMON"),
    ACCOUNT_FUNDING_TRANSFER("10","AFT"),
    QUASI_CASH("11","QUASI CASH"),
    WTHDMON_WINDOW ("17","WTHDMON WINDOW"),
    REFUND("20","REFUND"),
    ORIGINAL_CREDIT_TRANSFER("26","OCT"),
    PAYMENT("28","PAYMENT"),
    BALANCE_INQUIRY("30","BALANCE INQUIRY");

    private final String key;
    private final String value;


    FilterOperator(String key, String value) {
        this.key = key;
        this.value = value;
    }

    private static final Map<String, String> lookupMap = new HashMap<>();

    static {
        for (FilterOperator mf : FilterOperator.values()) {
            lookupMap.put(mf.getKey(), mf.getValue());
        }
    }

    public static String getFilterOperator(String key) {
        return lookupMap.get(key);
    }

}
