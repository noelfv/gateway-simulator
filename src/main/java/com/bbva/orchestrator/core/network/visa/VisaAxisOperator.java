package com.bbva.orchestrator.core.network.visa;

import java.util.Map;

public class VisaAxisOperator {

    // TODO: Revisar implementacion especifica para VISA, momentaneamente se devuelve null y valores por defecto en duro.
    private VisaAxisOperator() {
    }
    public static Boolean channelECommerceIndicator(Map<String, String> subFields) {
        return false;
    }

    public static String valueElectronicCommerceIndicators(Map<String, String> subFields) {
        return null;
    }

    public static String securityLevelECI(String ECI) {
        return null;
    }

    public static String channelTPVIndicator(Map<String, String> subFields, String merchantType){
        return "OTHN";
    }

    public static String entryModeIndicator(Map<String, String> subFields, String cardDataEntryMode) {
        return "ALL";
    }
}
