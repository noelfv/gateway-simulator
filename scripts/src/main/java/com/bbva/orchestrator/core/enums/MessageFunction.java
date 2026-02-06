package com.bbva.orchestrator.core.enums;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

// Nuevo enum para gestionar las funciones de mensaje
@Getter
public enum MessageFunction {

    // Define los MTI y sus valores de función
    MTI_0100("0100", "AUTQ"),
    MTI_0120("0120", "FAUQ"),
    MTI_0400("0400", "RVRA"),
    MTI_0420("0420", "FRVA"),
    MTI_0800("0800", "CMPV"),
    MTI_0190("0190", "UNKW"),
    // Define los valores de función y sus MTI de respuesta
    FUNC_AUTQ("AUTQ", "0110"),
    FUNC_FAUQ("FAUQ", "0130"),
    FUNC_RVRA("RVRA", "0410"),
    FUNC_FRVA("FRVA", "0430"),
    FUNC_CMPV("CMPV", "0810");

    private final String key;
    private final String value;

    MessageFunction(String key, String value) {
        this.key = key;
        this.value = value;
    }

    // Método estático para buscar eficientemente por clave
    private static final Map<String, String> lookupMap = new HashMap<>();
    static {
        for (MessageFunction mf : MessageFunction.values()) {
            lookupMap.put(mf.getKey(), mf.getValue());
        }
    }

    public static String convertMessageFunction(String typeMessage) {
        return lookupMap.get(typeMessage);
    }

    public static String convertTypeMessageResponse(String messageFunction) {
        return lookupMap.get(messageFunction);
    }

}