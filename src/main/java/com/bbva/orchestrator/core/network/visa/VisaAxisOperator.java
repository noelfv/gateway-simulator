package com.bbva.orchestrator.core.network.visa;

import java.util.Map;
import java.util.Set;

public class VisaAxisOperator {

    private static final String VAR_6008 = "60.08";
    private static final String VAR_2201 = "22.01";
    private static final String VAR_6001 = "60.01";
    private static final String VAR_6002 = "60.02";
    private static final String VAR_0301 = "03.01";

    private static final Set<String> VALID_VALUES = Set.of("05","06","07","08");
    private static final Set<String> VALID_VALUES_TPV = Set.of("05","07","02","90","91","95");
    private static final Set<String> VALID_VALUES_6001 = Set.of("0","3","4","5");
    private static final Set<String> VALID_VALUES_6002 = Set.of("1","0","2","5","8");
    // TODO: Revisar implementacion especifica para VISA, momentaneamente se devuelve null y valores por defecto en duro.
    private VisaAxisOperator() {
    }
    public static Boolean channelECommerceIndicator(Map<String, String> subFields, String pointServiceConditionCode) {

        if (!subFields.containsKey(VAR_6008) || pointServiceConditionCode == null) {
            return false;
        }

        return VALID_VALUES.contains(subFields.get(VAR_6008)) && "59".equals(pointServiceConditionCode);
    }

    public static String valueElectronicCommerceIndicators(Map<String, String> subFields) {
        return null;
    }

    public static String securityLevelECI(String ECI) {
        return null;
    }

    public static String channelTPVIndicator(Map<String, String> subFields, String merchantType){

        if (subFields.containsKey(VAR_2201) && subFields.containsKey(VAR_6001) && subFields.containsKey(VAR_6002) &&
                    VALID_VALUES_TPV.contains(subFields.get(VAR_2201)) &&
                    VALID_VALUES_6001.contains(subFields.get(VAR_6001)) &&
                    VALID_VALUES_6002.contains(subFields.get(VAR_6002))) {
                return "TPV";
        }else if(subFields.containsKey(VAR_0301) && subFields.get(VAR_0301).equals("01") && merchantType != null && merchantType.equals("6011")){
            return "ATMT";
        }else if(subFields.containsKey(VAR_0301) && subFields.get(VAR_0301).equals("01") && merchantType != null && merchantType.equals("6010")){
            return "OTHNRETV";
        }else{
            return "OTHN";
        }
    }

    public static String entryModeIndicator(Map<String, String> subFields, String cardDataEntryMode) {
        return "ALL";
    }
}
