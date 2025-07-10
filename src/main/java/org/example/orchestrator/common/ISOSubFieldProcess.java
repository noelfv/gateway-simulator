package org.example.orchestrator.common;



import org.example.orchestrator.iso8583.ISO8583;
import org.example.orchestrator.mastercard.subfields.ProcessSubFieldsMastercard;

import java.util.Map;

public class ISOSubFieldProcess {

    private ISOSubFieldProcess() {
    }

    public static Boolean channelECommerceIndicator(ISO8583 values, Map<String, String> subFields, String network){

            return ProcessSubFieldsMastercard.channelECommerceIndicator(values.getPointServiceEntryMode(), subFields);

    }

    public static String channelTPVIndicator(ISO8583 values, Map<String, String> subFields, String network){

            return ProcessSubFieldsMastercard.channelTPVIndicator(subFields, values.getMerchantType());

    }
}
