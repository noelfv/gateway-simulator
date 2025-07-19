package com.bbva.orchestrator.network.visa.subfields;

import java.util.Map;
import java.util.Set;

public class ProcessSubFieldsVisa {

    private static final String VAR_2201 = "22.01";

    private ProcessSubFieldsVisa() {
    }

    // TO DO: docuemntar valores
    private static final Set<String> VALID_VALUES = Set.of("05", "06", "07", "08");

    public static Boolean channelECommerceIndicator(String pointServiceConditionCode, Map<String, String> subFields) {
        if (!subFields.containsKey("60.08")) {
            return false;
        }
        return pointServiceConditionCode != null && pointServiceConditionCode.equals("59") && VALID_VALUES.contains(subFields.get("60.08"));
    }

    public static String channelTPVIndicator(Map<String, String> subFields, String merchantType){

        if( subFields.containsKey(VAR_2201) && subFields.containsKey("60.01") && subFields.containsKey("60.02")
                && (subFields.get(VAR_2201).equals("5") || subFields.get(VAR_2201).equals("7"))
                && subFields.get("60.01").equals("0") && subFields.get("60.02").equals("1")){
            return "POST";
        }else if(merchantType!=null && subFields.containsKey("3.01") && subFields.get("3.01").equals("01") && merchantType.equals("6011")){
            return "ATMT";
        }else{
            return "OTHN";
        }
    }


}
