package com.bbva.orchestrator.core.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum TransactionType {

    PURCHASE("00","COMPRAS"),
    WTHDMON("01","RETIROS DE ATM"),
    WALLET_AFT("10","BILLETERA AFT"),
    QUASI_CASH("11","COMPRAS"),
    WTHDMON_WINDOW ("17","RETIROS DE VENTANILLA"),
    REFUND("20","DEVOLUCIONES"),
    WALLET_OCT("26","BILLETERA OCT"),
    PAYMENT("28","PAGOS"),
    BALANCE_INQUIRY("30","CONSULTA SALDO");

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
