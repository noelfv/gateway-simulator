package org.example.orchestrator.mastercard.processor;


import org.example.orchestrator.ParserException;
import org.example.orchestrator.common.ISOField;
import org.example.orchestrator.common.ISOUtil;
import org.example.orchestrator.mastercard.ISOFieldMastercard;
import java.util.HashMap;
import java.util.Map;
import static org.example.orchestrator.common.ISODataType.BINARY_STRING;
import static org.example.orchestrator.common.ISODataType.NUMERIC_DECIMAL;
import static org.example.orchestrator.mastercard.ISOFieldMastercard.*;


public class ISOStringMapper {

    private ISOStringMapper() {
    }
    
    public static Map<String, String> mapFields(String iso) {
        Map<String, String> valuesMap = new HashMap<>();
        Map<String, String> valuesMapInt = new HashMap<>();
        boolean containsSecondaryBitmap = false;
        boolean flag = false;
        try {
            int position = 0;
            StringBuilder isoMessage = new StringBuilder(iso);

           // position = processNextField(MESSAGE_TYPE, isoMessage, position, valuesMap, flag);
            position = processNextField(MESSAGE_TYPE, isoMessage, position, valuesMap, valuesMapInt);
           // position = processNextField(ISOFieldMastercard.BITMAP_PRIMARY, isoMessage, position, valuesMap, flag);
            position = processNextField(BITMAP_PRIMARY, isoMessage, position, valuesMap, valuesMapInt);

            String binaryBitMapPrimary = valuesMap.get(BITMAP_PRIMARY.getName());
            if (binaryBitMapPrimary.charAt(0) == '1') {
                position = processNextField(BITMAP_SECONDARY,isoMessage, position, valuesMap, flag);
                containsSecondaryBitmap = true;
            }
            String binaryBitMap = valuesMap.get(BITMAP_PRIMARY.getName()).concat(valuesMap.getOrDefault(BITMAP_SECONDARY.getName(),""));

            //Se comienza la iteración desde el campo 2, el campo 1 se salta, ya que es el bitmap secundario
            for (int i = 2; i <= binaryBitMap.length(); i++) {
                //Se le resta 1, ya que .charAt toma el 0 como la posición inicial
                if (binaryBitMap.charAt(i-1) == '1') {
                    ISOField field = ISOFieldMastercard.getById(i);

                    //if (i == 48) flag = true;
                    //Se intenta enviar un campo fuera del estandar
                    if (field == null) {
                        throw new ParserException(createMessageError(i, "There is no mapping available"));
                    }
                    //position = processNextField(field,isoMessage, position, valuesMap, flag);
                    position = processNextField(field,isoMessage, position, valuesMap, valuesMapInt);
                    //flag = false;
                }
            }
        } catch (Exception e) {
            throw new ParserException("Cannot parse iso message: " + e.getMessage()+"|"+ISOUtil.processError(iso, containsSecondaryBitmap));
        }
        return valuesMap;
    }


    public static void main(String[] args) {
        String iso="0100FEFF640188E1E10A00000000000000401655365099999999990000000000000229900000000063560000000229900616072724727646806100000089871603272406162905061606155818840100060032860600328651675489871600400216400216000108778APPLE.COM/BILL         866-712-7753  USA118T37150511000009999974207010321022080504M1036105000015618AQV116AQS609AQF116753201038800202140303880040214050200710418C 60484060403701330129500193HKQIWEYVISE7UKTQK8YJ5C0026000410000060084095014     009MBKCG462F101001095001018ONE APPLE PARK WAY002003CA 003013APPLE.COM BIL0040108667127753007021842805822           Y";
        Map<String, String> valuesMap = mapFieldsTramaClaro( iso) ;
        System.out.println("terminando");
    }

    public static Map<String, String> mapFieldsTramaClaro(String iso) {
        Map<String, String> valuesMap = new HashMap<>();
        Map<String, String> valuesMapInt = new HashMap<>();
        boolean containsSecondaryBitmap = false;
        boolean flag = false;
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

                    //if (i == 48) flag = true;
                    //Se intenta enviar un campo fuera del estandar
                    if (field == null) {
                        throw new ParserException(createMessageError(i, "There is no mapping available"));
                    }
                    //position = processNextField(field,isoMessage, position, valuesMap, flag);
                    position = processNextFieldTramaClaro(field,isoMessage, position, valuesMap, valuesMapInt);
                    //flag = false;
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

    private static void addFieldToMap(ISOField isoField, String value, Map<String, String> mapString, Map<String, String> mapInt) {
        String fieldName = isoField.getName();
        String fieldNameId = String.valueOf(isoField.getId());
        String processedValue = processField(isoField, value);
        mapString.put(fieldName, processedValue);
        mapInt.put(fieldNameId, processedValue);
    }


    private static void addFieldToMapTramaClaro(ISOField isoField, String value, Map<String, String> mapString, Map<String, String> mapInt) {
        String fieldName = isoField.getName();
        String fieldNameId = String.valueOf(isoField.getId());
        String processedValue = processFieldTramaClaro(isoField, value);
        mapString.put(fieldName, processedValue);
        mapInt.put(fieldNameId, processedValue);
    }




    private static int processNextField(ISOField isoField, StringBuilder isoMessage, int position, Map<String, String> mapString, Map<String, String> mapInt) {
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

            if (isoField.getTypeData() == NUMERIC_DECIMAL) {
                value = ISOUtil.ebcdicToString(value);
            }

            position += fieldLength;
            //addFieldToMap(isoField, value, mapString);
            addFieldToMap(isoField, value, mapString,mapInt);
            return position;
        } catch (Exception e) {
            throw new ParserException(createMessageError(isoField.getId(), e.getMessage()));
        }
    }

    private static int processNextFieldTramaClaro(ISOField isoField, StringBuilder isoMessage, int position, Map<String, String> mapString, Map<String, String> mapInt) {
        try {
            int fieldLength = isoField.getLength();

            /*if(isoField.getTypeData() != BINARY_STRING) {
                //fieldLength = isoField.getLength()*2;
                fieldLength = isoField.getLength();
            }*/

            if (isoField.isVariable()) {
                String header = isoMessage.substring(position, position + fieldLength);
                position += fieldLength;
               // fieldLength = Integer.parseInt(ISOUtil.ebcdicToString(header), 10);

                fieldLength = Integer.parseInt(header);
            }

            String value = isoMessage.substring(position, position + fieldLength);

            /*if (isoField.getTypeData() == NUMERIC_DECIMAL) {
                value = ISOUtil.ebcdicToString(value);
            }*/

            position += fieldLength;
            //addFieldToMap(isoField, value, mapString);
            addFieldToMapTramaClaro(isoField, value, mapString,mapInt);
            return position;
        } catch (Exception e) {
            throw new ParserException(createMessageError(isoField.getId(), e.getMessage()));
        }
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
