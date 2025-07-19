package com.bbva.orchestrator.parser.common;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchestrator.network.mastercard.ISOFieldMastercard;
import com.bbva.orchestrator.network.mastercard.subfields.ISOMastercardSubfieldsUtil;
import com.bbva.orchestrator.network.visa.ISOFieldVisa;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import static com.bbva.orchestrator.network.visa.subfields.ISOVisaSubfieldsUtil.networkDataSubFields;
import static com.bbva.orchestrator.network.visa.subfields.ISOVisaSubfieldsUtil.pointServiceEntryMode;
import static com.bbva.orchestrator.network.visa.subfields.ISOVisaSubfieldsUtil.posTerminalDataSubFields;
import static com.bbva.orchestrator.network.visa.subfields.ISOVisaSubfieldsUtil.postalCodeSubFields;
import static com.bbva.orchestrator.network.visa.subfields.ISOVisaSubfieldsUtil.processingCode;



public class ISO8583SubFieldsParser {
    private ISO8583SubFieldsParser() {
    }

    private static final String SUBFIELD_ERROR_MESSAGE = "Error at mapping subfields for field : ";
    private static final String DASH = " - ";
    private static final String VAR_MASTERCARD="MASTERCARD";

    public static Map<String, String> mapSubFieldsVisa(Map<String, String> iso8583) {

        Map<String, String> subFields = new HashMap<>();

        //campo 3
        try {
            mapSubfieldsInSequence(iso8583.get(ISOFieldVisa.PROCESSING_CODE.getName()), processingCode, subFields, "3", "VISA");
        } catch (Exception e) {
            LogsTraces.writeWarning(SUBFIELD_ERROR_MESSAGE + DASH + "processingCode");
        }

        //campo 22
        try {
            mapSubfieldsInSequence(iso8583.get(ISOFieldVisa.POINT_SERVICE_ENTRY_MODE.getName()), pointServiceEntryMode, subFields, "22", "VISA");
        } catch (Exception e) {
            LogsTraces.writeWarning(SUBFIELD_ERROR_MESSAGE + DASH + "posTerminalData");
        }

        //Campo 60
        try {
            mapSubfieldsInSequence(iso8583.get(ISOFieldVisa.POS_TERMINAL_DATA.getName()), posTerminalDataSubFields, subFields, "60", "VISA");
        } catch (Exception e) {
            LogsTraces.writeWarning(SUBFIELD_ERROR_MESSAGE + DASH + "posTerminalData");
        }

        //Campo 62
        try {
            mapSubfieldsWithBitMap(iso8583.get(ISOFieldVisa.POSTAL_CODE.getName()), postalCodeSubFields, 16, subFields, "62");
        } catch (Exception e) {
            LogsTraces.writeWarning(SUBFIELD_ERROR_MESSAGE + DASH + "postalCode");
        }

        //Campo 63
        try {
            mapSubfieldsWithBitMap(iso8583.get(ISOFieldVisa.NETWORK_DATA.getName()), networkDataSubFields, 6, subFields, "63");
        } catch (Exception e) {
            LogsTraces.writeWarning(SUBFIELD_ERROR_MESSAGE + DASH + "networkData");
        }

        return subFields;
    }

    public static Map<String, String> mapSubFieldsMastercard(Map<String, String> iso8583) {

        Map<String, String> subFields = new HashMap<>();

        //campo 3
        try {
            mapSubfieldsInSequence(iso8583.get(ISOFieldMastercard.PROCESSING_CODE.getName()), ISOMastercardSubfieldsUtil.processingCode, subFields, "3",VAR_MASTERCARD);
        } catch (Exception e) {
            LogsTraces.writeWarning(SUBFIELD_ERROR_MESSAGE + DASH + "processingCode");
        }

        //campo 22
        try {
            mapSubfieldsInSequence(iso8583.get(ISOFieldMastercard.POINT_SERVICE_ENTRY_MODE.getName()), ISOMastercardSubfieldsUtil.pointServiceEntryMode, subFields, "22",VAR_MASTERCARD);
        } catch (Exception e) {
            LogsTraces.writeWarning(SUBFIELD_ERROR_MESSAGE + DASH + "pointServiceEntryMode");
        }

        //Campo 48
        try {
            mapSubfieldsInSequenceVar48Mc(iso8583.get(ISOFieldMastercard.ADDITIONAL_DATA_RETAILER.getName()),ISOMastercardSubfieldsUtil.additionalDataRetailerSubFields, subFields, "48");
        } catch (Exception e) {
            LogsTraces.writeWarning(SUBFIELD_ERROR_MESSAGE + DASH + "additionalDataRetailer");
        }

        //Campo 54
        try {
            mapSubfieldsInSequence(iso8583.get(ISOFieldMastercard.ADDITIONAL_AMOUNTS.getName()),ISOMastercardSubfieldsUtil.additionalAmountsSubFields, subFields, "54",VAR_MASTERCARD);
            if (subFields.containsKey("54.05")) subFields.put("54.05", ISOUtil.validAmount(subFields.get("54.05")));
        } catch (Exception e) {
            LogsTraces.writeWarning(SUBFIELD_ERROR_MESSAGE + DASH + "additionalAmounts");
        }

        //Campo 61
        try {
            mapSubfieldsInSequence(iso8583.get(ISOFieldMastercard.POS_CARD_ISSUER.getName()), ISOMastercardSubfieldsUtil.posCardIssuerSubFields, subFields, "61",VAR_MASTERCARD);
        } catch (Exception e) {
            LogsTraces.writeWarning(SUBFIELD_ERROR_MESSAGE + DASH + "posCardIssuer");
        }

        return subFields;
    }

    public static void mapSubfieldsInSequence(String value, Map<String, Integer> subFieldsLengths, Map<String, String> subFields, String fieldId, String network) {
        if (value == null || value.isEmpty()) {
            return;
        }

        StringBuilder field = new StringBuilder(value);
        int stringIndex = 0;

        Iterator<Map.Entry<String, Integer>> entryIterator = subFieldsLengths.entrySet().iterator();
        while (field.length() > stringIndex && entryIterator.hasNext()) {
            var entry = entryIterator.next();
            String subField = field.substring(stringIndex,
                                              stringIndex + getLengthCase(entry.getValue(),field.length(),stringIndex,field.toString(), network));
            subFields.put(fieldId + "." + (entry.getKey()), subField);
            stringIndex = stringIndex + getLengthCase(entry.getValue(), field.length(), stringIndex, field.toString(), network);
        }
    }

    public static void mapSubfieldsInSequenceVar48Mc(String value,Map<String, Integer> subFieldsLengths, Map<String, String> subFields, String fieldId){
        if (value == null || value.isEmpty()) {
            return;
        }
        subFields.put(fieldId +  ".01", value.substring(0, 1));
        List<String> subFieldsIn = parserSubFieldsAndLength(value.substring(1), subFields, fieldId, 2, 2);

        for(String element : subFieldsIn){
            if(subFieldsLengths.get(element)!= null && subFieldsLengths.get(element) == 1){
                parserSubFieldsAndLength(subFields.get(fieldId + "." + element), subFields, fieldId + "." + element, 2, 2);
            }else if(subFieldsLengths.get(element)!= null && subFieldsLengths.get(element) == -1){
                mapSubfieldsInSequence(subFields.get(fieldId + "." + element), ISOMastercardSubfieldsUtil.internalSubFields48.get(element), subFields, fieldId + "." + element, VAR_MASTERCARD);
            }
        }



    }

    public static List<String> parserSubFieldsAndLength(String value, Map<String, String> subFields, String fieldId, int subFieldLength, int fieldLength) {
        int stringIndex = 0;
        List<String> subFieldsIn = new ArrayList<>();

        while(value.length() > stringIndex){
            String subField = value.substring(stringIndex, stringIndex + subFieldLength);
            stringIndex = stringIndex + subFieldLength;
            int length = Integer.parseInt(value.substring(stringIndex, stringIndex + fieldLength));
            stringIndex = stringIndex + fieldLength;
            subFields.put(fieldId + "." + subField, value.substring(stringIndex, stringIndex + length));
            stringIndex = stringIndex + length;
            subFieldsIn.add(subField);
        }
        return subFieldsIn;
    }

    public static int getLengthCase(int length, int totalLength, int currentIndex, String field, String network){
        if(length > 0){
            return length;
        }else if(length == 0){
            return totalLength - currentIndex;
        }else {
            return variableLength(field, currentIndex, length, network);
        }
    }

    public static int variableLength(String field,int currentIndex, int length, String network){
        String cut = field.substring(currentIndex, currentIndex + length*(-1));
        if(network.equals("VISA")){
            return Integer.parseInt(cut,16);
        }else {
            return Integer.parseInt(cut);
        }
    }

    public static void mapSubfieldsWithBitMap(String value, Map<String, Integer> subFieldsLengths, int bitmapLength, Map<String, String> subFields, String fieldId) {
        if (value == null || value.isEmpty()) {
            return;
        }

        StringBuilder field = new StringBuilder(value);
        String binaryBitMap = ISOUtil.convertHEXtoBITMAP(field.substring(0, bitmapLength));
        int stringIndex = bitmapLength;

        for (int subFieldIndex = 0; binaryBitMap.length() > subFieldIndex; subFieldIndex++) {
            if (binaryBitMap.charAt(subFieldIndex) == '1') {
                String subField = field.substring(stringIndex, stringIndex + subFieldsLengths.get(formatNumber(subFieldIndex + 1)));
                subFields.put(fieldId + "." + (subFieldIndex + 1), subField);
                stringIndex = stringIndex + subFieldsLengths.get(formatNumber(subFieldIndex + 1));
            }
        }
    }

    public static String formatNumber(int number) {
        if (number >= 0 && number < 10) {
            return "0" + number;
        } else if (number > -10 && number < 0) {
            return "-0" + Math.abs(number);
        } else {
            return String.valueOf(number);
        }
    }

    @SafeVarargs
    public static <K, V> Map<K, V> ofEntries(Map.Entry<K, V>... entries) {
        LinkedHashMap<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : entries) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }
}
