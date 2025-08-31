package com.bbva.orchestrator.core.commons;

import com.bbva.orchestrator.core.builders.ISO8583;

import java.util.Map;

public class ISOSubFieldProcess {

    private ISOSubFieldProcess() {
    }

    public static Boolean channelECommerceIndicator(ISO8583 values, Map<String, String> subFields, String network){
        if(network.equalsIgnoreCase("peer02")){
            return ProcessSubFieldsMastercard.channelECommerceIndicator(values.getPointServiceEntryMode(), subFields);
        }else {
            //return ProcessSubFieldsVisa.channelECommerceIndicator(values.getPointServiceConditionCode(), subFields);
            return null;
        }
    }

    public static String channelTPVIndicator(ISO8583 values, Map<String, String> subFields, String network){
        if(network.equalsIgnoreCase("peer02")){
            return ProcessSubFieldsMastercard.channelTPVIndicator(subFields, values.getMerchantType());
        }else {
            //return ProcessSubFieldsVisa.channelTPVIndicator(subFields, values.getMerchantType());
            return null;
        }
    }
}
