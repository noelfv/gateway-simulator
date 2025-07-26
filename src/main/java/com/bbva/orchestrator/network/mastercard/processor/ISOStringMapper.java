package com.bbva.orchestrator.network.mastercard.processor;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchestrator.parser.common.ISOUtil;
import com.bbva.orchestrator.network.mastercard.ISOFieldMastercard;
import com.bbva.orchlib.parser.ParserException;
import java.util.HashMap;
import java.util.Map;

import static com.bbva.orchestrator.parser.common.ISODataType.*;

public class ISOStringMapper {

    private ISOStringMapper() {
    }
    
    public static Map<String, String> mapFields(String iso) {
        Map<String, String> valuesMap = new HashMap<>();
        boolean containsSecondaryBitmap = false;
        boolean flag = false;
        try {
            int position = 0;
            StringBuilder isoMessage = new StringBuilder(iso);

            position = processNextField(ISOFieldMastercard.MESSAGE_TYPE, isoMessage, position, valuesMap, flag);
            position = processNextField(ISOFieldMastercard.BITMAP_PRIMARY, isoMessage, position, valuesMap, flag);

            String binaryBitMapPrimary = valuesMap.get(ISOFieldMastercard.BITMAP_PRIMARY.getName());
            if (binaryBitMapPrimary.charAt(0) == '1') {
                position = processNextField(ISOFieldMastercard.BITMAP_SECONDARY,isoMessage, position, valuesMap, flag);
                containsSecondaryBitmap = true;
            }
            String binaryBitMap = valuesMap.get(ISOFieldMastercard.BITMAP_PRIMARY.getName()).concat(valuesMap.getOrDefault(ISOFieldMastercard.BITMAP_SECONDARY.getName(),""));

            //Se comienza la iteración desde el campo 2, el campo 1 se salta, ya que es el bitmap secundario
            for (int i = 2; i <= binaryBitMap.length(); i++) {
                //Se le resta 1, ya que .charAt toma el 0 como la posición inicial
                if (binaryBitMap.charAt(i-1) == '1') {
                    ISOField field = ISOFieldMastercard.getById(i);

                    if (i == 48) flag = true;
                    //Se intenta enviar un campo fuera del estandar
                    if (field == null) {
                        LogsTraces.writeInfo("Field not allowed: " + (i));
                        throw new ParserException(createMessageError(i, "There is no mapping available"));
                    }
                    position = processNextField(field,isoMessage, position, valuesMap, flag);
                    flag = false;
                }
            }
        } catch (Exception e) {
            throw new ParserException("Cannot parse iso message: " + e.getMessage()+"|"+ISOUtil.processError(iso, containsSecondaryBitmap));
        }
        return valuesMap;
    }

    private static int processNextField(ISOField isoField, StringBuilder isoMessage, int position, Map<String, String> valuesMap, boolean flag) {
        try {
            int fieldLength = isoField.getLength();

            if(isoField.getTypeData() != BINARY_STRING) {
                fieldLength = isoField.getLength()*2;
            }

            if (isoField.isVariable()) {
                String header = isoMessage.substring(position, position + fieldLength);
                position += fieldLength;
                fieldLength = Integer.parseInt(ISOUtil.ebcdicToString(header), 10);
                fieldLength = fieldLength * 2;
            }

            String value = isoMessage.substring(position, position + fieldLength);

            //Impresion del campo 48 temporal - validacion productiva
            if(flag) LogsTraces.writeInfo("Field 48 EBCDIC: " + (value));

            if (isoField.getTypeData() == NUMERIC_DECIMAL) {
                value = ISOUtil.ebcdicToString(value);
            }

            position += fieldLength;
            addFieldToMap(isoField, value, valuesMap);
            return position;
        } catch (Exception e) {
            throw new ParserException(createMessageError(isoField.getId(), e.getMessage()));
        }
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
            case NUMERIC -> ISOUtil.ebcdicToString(value);
            case HEXADECIMAL -> value;
        };
    }

    private static String createMessageError(int id, String message) {
        return "Error found in field " + id + ": " + message;
    }
}
