package com.bbva.orchestrator.network.mastercard.subfields;

import java.util.Map;
import java.util.Set;

public class ProcessSubFieldsMastercard {

    private static final String VAR_2201 = "22.01";
    private static final String VAR_6111 = "61.11";
    private static final String VAR_48042 = "48.42";
    private static final String VAR_6104 = "61.04";
    private static final String VAR_6105 = "61.05";
    private static final String VAR_6110 = "61.10";
    private static final String VAR_4801 = "48.01";
    private static final String POINT_SERVICE_ENTRY_MODE_810 = "810";
    private static final String POINT_SERVICE_ENTRY_MODE_001 = "001";
    private static final String POINT_SERVICE_ENTRY_MODE_100 = "100";
    private static final Set<String> VALID_VALUES = Set.of("2","3","4","5","7","8","9");

    private static final String RETAIL = "R";

    private ProcessSubFieldsMastercard() {
    }


    public static Boolean channelECommerceIndicator(String pointServiceEntryMode, Map<String, String> subFields) {
        if (!subFields.containsKey(VAR_48042) || !subFields.containsKey(VAR_6104) ||
                !subFields.containsKey(VAR_6105) || !subFields.containsKey(VAR_6110)) {
            return false;
        }

        return (
                POINT_SERVICE_ENTRY_MODE_100.equals(pointServiceEntryMode) &&
                        "4".equals(subFields.get(VAR_6104)) &&
                        ("6".equals(subFields.get(VAR_6110)) || "0".equals(subFields.get(VAR_6110)))&&
                        "1".equals(subFields.get(VAR_6105))
        ) ||
                (
                        (POINT_SERVICE_ENTRY_MODE_810.equals(pointServiceEntryMode) ||
                                POINT_SERVICE_ENTRY_MODE_001.equals(pointServiceEntryMode) ||
                                POINT_SERVICE_ENTRY_MODE_100.equals(pointServiceEntryMode)) &&
                                "5".equals(subFields.get(VAR_6104)) &&
                                "1".equals(subFields.get(VAR_6105)) &&
                                ("6".equals(subFields.get(VAR_6110)) || "0".equals(subFields.get(VAR_6110)))
                );
    }

    public static String channelTPVIndicator(Map<String, String> subFields, String merchantType){

        if( subFields.containsKey(VAR_2201) && subFields.containsKey(VAR_6111)
                &&(subFields.get(VAR_2201).equals("05") || subFields.get(VAR_2201).equals("07"))
                && VALID_VALUES.contains(subFields.get(VAR_6111))
                && RETAIL.equals(subFields.get(VAR_4801))
                ){
            return "POST";
        }else if(subFields.containsKey("3.01") && subFields.get("3.01").equals("01") && merchantType != null && merchantType.equals("6011")){
            return "ATMT";
        }else if(subFields.containsKey("3.01") && subFields.get("3.01").equals("01") && merchantType != null && merchantType.equals("6010")){
            return "OTHNRETV";
        }else{
            return "OTHN";
        }
    }


}
