package org.example.gui.utils;

import org.example.orchestrator.ParserException;
import org.example.orchestrator.common.ISOField;
import org.example.orchestrator.common.ISOUtil;
import org.example.orchestrator.mastercard.ISOFieldMastercard;
import org.example.orchestrator.mastercard.processor.ISOStringMapper;

import java.util.HashMap;
import java.util.Map;

import static org.example.orchestrator.common.ISODataType.BINARY_STRING;
import static org.example.orchestrator.common.ISODataType.NUMERIC_DECIMAL;
import static org.example.orchestrator.mastercard.ISOFieldMastercard.*;
import static org.example.orchestrator.mastercard.ISOFieldMastercard.BITMAP_PRIMARY;
import static org.example.orchestrator.mastercard.ISOFieldMastercard.BITMAP_SECONDARY;

public class ISO8583Processor {


    public static Map<String, String> mapFields(String iso) {
        return  ISOStringMapper.mapFields(iso);
    }


    public static Map<String, String> mapFieldsTramaClaro(String iso) {

        Map<String, String> valuesMap = new HashMap<>();
        Map<String, String> valuesMapInt = new HashMap<>();
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
            throw new ParserException(createMessageError(isoField.getId(), e.getMessage()));
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



}
