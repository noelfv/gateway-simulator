package com.bbva.gui.commons;


import com.bbva.orchestrator.core.exception.ParserFieldsException;
import com.bbva.orchestrator.core.fields.MastercardISOField;
import com.bbva.orchestrator.core.fields.definitions.ISOField;
import com.bbva.orchestrator.core.utils.FieldUtil;
import com.bbva.orchestrator.core.utils.ISOUtil;
import com.bbva.orchlib.parser.ParserException;

import java.util.LinkedHashMap;
import java.util.Map;
import static com.bbva.orchestrator.core.fields.MastercardISOField.*;
import static com.bbva.orchestrator.core.fields.definitions.ISODataType.*;


public class ISO8583Processor {

    public static String buildFromMap(Map<String, String> subFieldsMap) {
        StringBuilder tlvBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : subFieldsMap.entrySet()) {
            // Extraer el tag (última parte de la clave, ej: "48.33.01" → "01")
            String[] keyParts = entry.getKey().split("\\.");
            String tag = keyParts[keyParts.length - 1];

            // Codificar tag a EBCDIC Hex (2 bytes → 4 hex)
            String tagHex = ISOUtil.stringToEBCDICHex(tag);

            // Codificar valor a EBCDIC Hex
            String valueHex = ISOUtil.stringToEBCDICHex(entry.getValue());

            // Calcular longitud en bytes y codificar a EBCDIC Hex (2 bytes → 4 hex)
            int valueLength = valueHex.length() / 2;
            String lengthHex = ISOUtil.stringToEBCDICHex(String.format("%02d", valueLength));

            // Concatenar TLV
            tlvBuilder.append(tagHex).append(lengthHex).append(valueHex);
        }

        return tlvBuilder.toString();
    }

    public static Map<String, String> createMapFieldsISO8583(String messageOriginal) {

        Map<String, String> valuesMap = new LinkedHashMap<>();
        Map<String, String> valuesMapInt = new  LinkedHashMap<>();

        try {
            int position = 0;

            position = processNextFieldTramaClaro(MESSAGE_TYPE, messageOriginal, position, valuesMap, valuesMapInt);
            position = processNextFieldTramaClaro(BITMAP_PRIMARY, messageOriginal, position, valuesMap, valuesMapInt);

            String binaryBitMapPrimary = valuesMap.get(BITMAP_PRIMARY.getName());
            if (binaryBitMapPrimary.charAt(0) == '1') {
                position = processNextFieldTramaClaro(BITMAP_SECONDARY,messageOriginal, position, valuesMap, valuesMapInt);
            }
            String binaryBitMap = valuesMap.get(BITMAP_PRIMARY.getName()).concat(valuesMap.getOrDefault(BITMAP_SECONDARY.getName(),""));

            int positionActual=position;
            //Se comienza la iteración desde el campo 2, el campo 1 se salta, ya que es el bitmap secundario
            for (int i = 2; i <= binaryBitMap.length(); i++) {
                //Se le resta 1, ya que .charAt toma el 0 como la posición inicial
                if (binaryBitMap.charAt(i-1) == '1') {
                    ISOField field = MastercardISOField.getById(i);

                    if (field == null) {
                        throw new ParserException(createMessageError(i, "There is no mapping available"));
                    }

                    String rawData=messageOriginal.substring(positionActual);
                    position = processNextFieldTramaClaro(field,rawData, valuesMap, valuesMapInt);
                    positionActual+=position;
                }
            }
        } catch (ParserException e) {
            //throw new ParserException("Cannot parse iso message: " + e.getMessage()+"|"+ ISOUtil.processError(iso, containsSecondaryBitmap));
            //throw new ParserLocalException("Cannot parse iso message: " + e.getMessage()+"|"+ ISOUtil.processError(iso, containsSecondaryBitmap),valuesMap);
            throw new ParserFieldsException("Cannot parse iso message: " + e.getMessage());
        }
        return valuesMap;
    }

    private static int processNextFieldTramaClaro(ISOField isoField, String isoMessage, int position, Map<String, String> mapString, Map<String, String> mapInt) {

        try {
            int fieldLength = isoField.getLength();

            if(isoField.getTypeData().equals(BINARY_STRING)){
                fieldLength = isoField.getLength()*2;
            }

            String value = isoMessage.substring(position, position + fieldLength);
            position += fieldLength;
            addFieldToMapTramaClaro(isoField, value, mapString,mapInt);
            return position;

        } catch (Exception e) {
            throw new ParserException(createMessageError(position,mapInt,isoField.getId(), e.getMessage()));
        }
    }

    private static int processNextFieldTramaClaro(ISOField isoField, String isoMessage, Map<String, String> mapString, Map<String, String> mapInt) {

        int position=0;
        try {
            int fieldLength = isoField.getLength();

            if (isoField.isVariable()) {
                String header = isoMessage.substring(position, position + fieldLength);
                position += fieldLength;
                fieldLength = Integer.parseInt(header);
            }

            String value = isoMessage.substring(position, position + fieldLength);
            position += fieldLength;
            addFieldToMapTramaClaro(isoField, value, mapString,mapInt);
            return position;

        } catch (Exception e) {
            throw new ParserException(createMessageError(position,mapInt,isoField.getId(), e.getMessage()));
        }
    }

    private static void addFieldToMapTramaClaro(ISOField isoField, String value, Map<String, String> mapString, Map<String, String> mapInt) {
        String fieldName = isoField.getName();
        String fieldNameId = String.valueOf(isoField.getId());
        String processedValue = processFieldTramaClaro(isoField, value);
        mapString.put(fieldName, processedValue);
        mapInt.put(fieldNameId, processedValue);
    }

    private static String processFieldTramaClaro(ISOField isoField, String value) {
        if (isoField.getTypeData() == NUMERIC_DECIMAL) {
            return FieldUtil.validAmount(value);
        } else if (isoField.getTypeData() == BINARY_STRING) {
            return  ISOUtil.convertHEXtoBITMAP(value);
        }else
            return  value;
    }

    private static String createMessageError(int id, String message) {
        return "Error found in field " + id + ": " + message;
    }


    private static String createMessageError(int positionLast, Map<String,String> mapFields,int id, String message) {
        StringBuilder resultado = new StringBuilder();
        for (Map.Entry<String, String> entry : mapFields.entrySet()) {
            resultado.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("; ");
        }

        return    positionLast + " fieldLast : " +resultado +  "Error found in field " + id + ": " + message;
    }



}
