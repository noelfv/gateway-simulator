package com.bbva.orchestrator.network.visa.processor;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchestrator.parser.common.ISOUtil;
import com.bbva.orchestrator.network.visa.ISOFieldVisa;
import com.bbva.orchlib.parser.ParserException;

import java.util.HashMap;
import java.util.Map;
import static com.bbva.orchestrator.parser.common.ISODataType.ALPHA_NUMERIC;
import static com.bbva.orchestrator.parser.common.ISODataType.HEXADECIMAL;

public class ISOStringMapper {

    private ISOStringMapper() {
    }

    public static Map<String, String> mapFields(String iso) {
        Map<String, String> valuesMap = new HashMap<>();
        StringBuilder isoMessage = new StringBuilder(iso);

        try {
            int position = 0;
            String header = processHeader(isoMessage);
            position += header.length();
            valuesMap.put(ISOFieldVisa.HEADER.getName(), header);

            position = processNextField(ISOFieldVisa.MESSAGE_TYPE, isoMessage, position, valuesMap);
            position = processNextField(ISOFieldVisa.BITMAP_PRIMARY, isoMessage, position, valuesMap);

            String binaryBitMapPrimary = valuesMap.get(ISOFieldVisa.BITMAP_PRIMARY.getName());
            if (binaryBitMapPrimary.charAt(0) == '1') {
                position = processNextField(ISOFieldVisa.BITMAP_SECONDARY, isoMessage, position, valuesMap);
            }

            String binaryBitMap = valuesMap.get(ISOFieldVisa.BITMAP_PRIMARY.getName()).concat(valuesMap.getOrDefault(ISOFieldVisa.BITMAP_SECONDARY.getName(), ""));

            // Se comienza la iteración desde el campo 2, el campo 1 se salta, ya que es el bitmap secundario
            for (int i = 2; i <= binaryBitMap.length(); i++) {
                // Se le resta 1, ya que .charAt toma el 0 como la posición inicial
                if (binaryBitMap.charAt(i - 1) == '1') {
                    ISOField field = ISOFieldVisa.getById(i);
                    //Se intenta enviar un campo fuera del estandar
                    if (field == null) {
                        LogsTraces.writeInfo("Field not allowed: " + i);
                        throw new ParserException(createMessageError(i, "There is no mapping available"));
                    }
                    position = processNextField(field, isoMessage, position, valuesMap);
                }
            }
        } catch (Exception e) {
             throw new ParserException("Cannot parse iso message: " + e.getMessage());
        }

        return valuesMap;
    }

    private static String processHeader(StringBuilder isoMessage) {
        int start = 0;
        int length = ISOFieldVisa.HEADER.getLength();
        String longHeader = isoMessage.substring(start, length);
        int dec = Integer.parseInt(longHeader, 16);
        length = dec * 2;

        return isoMessage.substring(0, length);
    }

    private static int processNextField(ISOField isoField, StringBuilder isoMessage, int position, Map<String, String> valuesMap) {
        try {
            int fieldLength = isoField.getLength();

            if (isoField.isVariable()) {
                String header = isoMessage.substring(position, position + fieldLength);
                position += fieldLength;
                fieldLength = Integer.parseInt(header, 16);
            }

            fieldLength = calculateFieldLength(isoField, fieldLength);
            String value = isoMessage.substring(((fieldLength % 2 != 0) ? ++position : position), position + fieldLength);
            position += fieldLength;
            addFieldToMap(isoField, value, valuesMap);
            return position;
        } catch (Exception e) {
            throw new ParserException(createMessageError(isoField.getId(), e.getMessage()));
        }
    }

    private static int calculateFieldLength(ISOField isoField, int length) {
        if (isoField.getTypeData().equals(ALPHA_NUMERIC) || isoField.getTypeData().equals(HEXADECIMAL)) {
            length *= 2;
        }
        return length;
    }

    private static void addFieldToMap(ISOField isoField, String value, Map<String, String> valuesMap) {
        String fieldName = isoField.getName();
        String processedValue = processField(isoField, value);
        valuesMap.put(fieldName, processedValue);
    }

    private static String processField(ISOField isoField, String value) {
        return switch (isoField.getTypeData()) {
            case ALPHA_NUMERIC -> ISOUtil.convertHEXtoEBCDIC(value);
            case NUMERIC_DECIMAL -> ISOUtil.validAmount(value);
            case BINARY_STRING -> ISOUtil.convertHEXtoBITMAP(value);
            case HEXADECIMAL, NUMERIC -> value;
        };
    }

    private static String createMessageError(int id, String message) {
        return "Error found in field " + id + ": " + message;
    }
}
