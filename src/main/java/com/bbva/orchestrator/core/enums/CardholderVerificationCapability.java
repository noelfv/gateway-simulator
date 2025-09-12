package com.bbva.orchestrator.core.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public enum CardholderVerificationCapability {

    // Define los valores raw y enums
    ENUM_0("0", "UNSP"),
    ENUM_1("1", "NPIN"),
    ENUM_2("2", "NOPN"),
    ENUM_3("3", "OTHN"),
    ENUM_8("8", "OTHN"),

    // Define los valores enums y raw
    RAW_UNSP("UNSP", "0"),
    RAW_NPIN("MLEY", "1"),
    RAW_NOPN("MGST", "2"),
    //RAW_OTHN("OTHN", "3"),
    RAW_OTH8("OTHN", "8");

    private final String key;
    private final String value;

    CardholderVerificationCapability(String key, String value) {
        this.key = key;
        this.value = value;
    }

    // Método estático para buscar eficientemente por clave
    private static final Map<String, String> lookupMap = new HashMap<>();
    static {
        for (CardholderVerificationCapability mf : CardholderVerificationCapability.values()) {
            lookupMap.put(mf.getKey(), mf.getValue());
        }
    }

    public static String convertCardholderVerificationCapability(String typeMessage) {
        return lookupMap.get(typeMessage);
    }

    public static String convertTypeCardholderVerificationCapability(String cardholderVerificationCapability) {
        return lookupMap.get(cardholderVerificationCapability);
    }

    public static Boolean mapSubField01AttendedIndicator(String posTerminalAttendance) {
        if (posTerminalAttendance == null) {
            return null;
        }
        return "1".equals(posTerminalAttendance);
    }

    public static String mapSubField01_10Category(String posTerminalAttendance, String cardHolderATL){

        if (posTerminalAttendance == null && cardHolderATL == null) {
            return null; // O un valor por defecto si es necesario
        }

        String result = null;

        if (cardHolderATL != null) {
            switch (cardHolderATL) {
                case "1" -> result = "CAT LEVEL 1";
                case "2" -> result = "CAT LEVEL 2";
                case "3" -> result = "CAT LEVEL 3";
                case "4" -> result = "CAT LEVEL 4";
                case "6" -> result = "CAT LEVEL 6";
                case "7" -> result = "CAT LEVEL 7";
                case "8" -> result = "MBOB INIT";
                case "9" -> result = "MPOS ACCEP";
            }
        }

        if ("2".equals(posTerminalAttendance)) {
            result = "Voice/Audio";
        }
        return result;
    }

    public static Map<String, String> mapSubField61_03(String posTerminalLocation, String posTerminalAttendance, String type) {
        if (posTerminalAttendance == null || !List.of("0", "1", "2", "3", "4").contains(posTerminalLocation)) {
            return Map.of(); // Devuelve un mapa inmutable vacío
        }

        Map<String, String> result = new HashMap<>();
        switch (posTerminalLocation) {
            case "0" -> {
                result.put("offPremisesIndicator", "false");
                result.put("type", "POS");
            }
            case "1" -> {
                result.put("offPremisesIndicator", "true");
                result.put("OtherType", "Merchant terminal");
            }
            case "2" -> {
                result.put("offPremisesIndicator", "true");
                result.put("OtherType", "Cardholder terminal");
            }
            case "3" -> {
                if ("2".equals(posTerminalAttendance)) {
                    result.put("offPremisesIndicator", "false");
                    result.put("OtherType", "Voice/Audio");
                }
            }
            case "4" -> {
                result.put("offPremisesIndicator", "false");
                result.put("GeographicLocation", "Cardholder terminal");
            }
        }

        if(type != null && type.length() > 4)
            result.put("OtherType", type.substring(4));

        return result;
    }

    public static Map<String, String> mapSubField61_04_cardHolderPresent(String posCardholderPresence) {
        if (posCardholderPresence == null) {
            return Map.of();
        }

        Map<String, String> result = new HashMap<>();
        result.put("cardholderPresent", "0".equals(posCardholderPresence) ? "false" : "true");

        switch (posCardholderPresence) {
            case "2" -> result.put("MOTOCode", "MAOR");
            case "3" -> result.put("MOTOCode", "TPOR");
            case "4" -> result.put("otherTransactionAttribute", "RCPT");
            case "5" -> result.put("ecommerceIndicator", "true");
        }

        return result;
    }

    public static Boolean mapSubField61_05_cardPresent(String posCardPresence){
        if (posCardPresence == null) {
            return null;
        }

        return "0".equals(posCardPresence);
    }

    public static Boolean mapSubField61_06_capable(String posCardCaptureCapabilities){
        if (posCardCaptureCapabilities == null) {
            return null;
        }

        return !"0".equals(posCardCaptureCapabilities);
    }

    public static Map<String, String> mapSubField61_11(String value){
        if (value == null) {
            return Map.of();
        }

        Map<String, String> result = new HashMap<>();

        switch (value) {
            case "0" -> result.put("capability", "UNKW");
            case "1" -> {
                result.put("capability", "MAOR");
                result.put("otherCapability", "VALUE");
            }
            case "2" -> result.put("capability", "MGST");
            case "3" -> result.put("capability", "ECTL");
            case "4" -> result.put("capability", "MSIP");
            case "5", "7", "8" -> {
                result.put("capability", "OTHN");
                result.put("otherCapability", "VALUE");
            }
            case "6" -> result.put("capability", "KEEN");
            case "9" -> result.put("capability", "CICC");
        }

        return result;
    }

    public static Boolean mapEcommerceIndicator(String value, Boolean ecommerceValue){
        Boolean result = null;
        if("5".equals(value))
            result = true;

        if(ecommerceValue != null)
            result = ecommerceValue;

        return result;
    }



}
