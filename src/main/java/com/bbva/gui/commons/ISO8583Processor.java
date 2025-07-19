package com.bbva.gui.commons;

import com.bbva.orchestrator.ParserException;
import com.bbva.orchestrator.network.mastercard.ISOFieldMastercard;
import com.bbva.orchestrator.network.mastercard.processor.ISOStringMapper;
import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchestrator.parser.common.ISOUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.bbva.orchestrator.network.mastercard.ISOFieldMastercard.*;
import static com.bbva.orchestrator.parser.common.ISODataType.BINARY_STRING;
import static com.bbva.orchestrator.parser.common.ISODataType.NUMERIC_DECIMAL;

public class ISO8583Processor {


    public static Map<String, String> mapFields(String iso) {
        return  ISOStringMapper.mapFields(iso);
    }


    public static Map<String, String> createMapFieldsISO8583(String iso) {

        Map<String, String> valuesMap = new LinkedHashMap<>();
        Map<String, String> valuesMapInt = new  LinkedHashMap<>();

        boolean containsSecondaryBitmap = false;

        try {
            int position = 0;
            StringBuilder isoMessage = new StringBuilder(iso);

            position = processNextFieldTramaClaro(MESSAGE_TYPE, isoMessage, position, valuesMap, valuesMapInt);
            position = processNextFieldTramaClaro(BITMAP_PRIMARY, isoMessage, position, valuesMap, valuesMapInt);

            String binaryBitMapPrimary = valuesMap.get(BITMAP_PRIMARY.getName());
            if (binaryBitMapPrimary.charAt(0) == '1') {
                position = processNextFieldTramaClaro(BITMAP_SECONDARY,isoMessage, position, valuesMap, valuesMapInt);
                containsSecondaryBitmap = true;
            }
            String binaryBitMap = valuesMap.get(BITMAP_PRIMARY.getName()).concat(valuesMap.getOrDefault(BITMAP_SECONDARY.getName(),""));

            //Se comienza la iteración desde el campo 2, el campo 1 se salta, ya que es el bitmap secundario
            for (int i = 2; i <= binaryBitMap.length(); i++) {
                //Se le resta 1, ya que .charAt toma el 0 como la posición inicial
                if (binaryBitMap.charAt(i-1) == '1') {
                    ISOField field = ISOFieldMastercard.getById(i);

                    if (field == null) {
                        throw new ParserException(createMessageError(i, "There is no mapping available"));
                    }

                    position = processNextFieldTramaClaro(field,isoMessage, position, valuesMap, valuesMapInt);
                }
            }
        } catch (Exception e) {
            throw new ParserException("Cannot parse iso message: " + e.getMessage()+"|"+ ISOUtil.processError(iso, containsSecondaryBitmap));
        }
        return valuesMap;
    }


    private static int processNextFieldTramaClaro(ISOField isoField, StringBuilder isoMessage, int position, Map<String, String> mapString, Map<String, String> mapInt) {

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
            return ISOUtil.validAmount(value);
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
