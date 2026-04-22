package com.bbva.gui.commons;

import com.bbva.orchestrator.core.exception.ParserFieldsException;
import com.bbva.orchestrator.core.fields.VisaISOField;
import com.bbva.orchestrator.core.fields.definitions.ISOField;
import com.bbva.orchestrator.core.utils.FieldUtil;
import com.bbva.orchestrator.core.utils.ISOUtil;
import com.bbva.orchlib.parser.ParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.IntFunction;

import static com.bbva.orchestrator.core.fields.MastercardISOField.*;
import static com.bbva.orchestrator.core.fields.definitions.ISODataType.*;

public class ISO8583Processor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ISO8583Processor.class);

    public static String buildFromMap(Map<String, String> subFieldsMap) {
        StringBuilder tlvBuilder = new StringBuilder();

        for (Map.Entry<String, String> entry : subFieldsMap.entrySet()) {
            String[] keyParts = entry.getKey().split("\\.");
            String tag = keyParts[keyParts.length - 1];

            String tagHex = ISOUtil.stringToEBCDICHex(tag);
            String valueHex = ISOUtil.stringToEBCDICHex(entry.getValue());
            int valueLength = valueHex.length() / 2;
            String lengthHex = ISOUtil.stringToEBCDICHex(String.format("%02d", valueLength));

            tlvBuilder.append(tagHex).append(lengthHex).append(valueHex);
        }

        return tlvBuilder.toString();
    }

    public static Map<String, String> createMapFieldsISO8583Mastercard(String messageOriginal) {
        return parseBitmapFields(messageOriginal, 0, id -> getById(id));
    }

    public static Map<String, String> createMapFieldsISO8583Visa(String messageOriginal) {
        Map<String, String> valuesMap = new LinkedHashMap<>();
        int startPosition = 0;

        String header = processHeader(messageOriginal);
        valuesMap.put(VisaISOField.HEADER.getName(), header);
        startPosition = header.startsWith("160218") ? 0 : header.length();

        Map<String, String> parsed = parseBitmapFields(messageOriginal, startPosition, VisaISOField::getById);
        valuesMap.putAll(parsed);
        return valuesMap;
    }

    private static Map<String, String> parseBitmapFields(
            String messageOriginal, int startPosition, IntFunction<ISOField> fieldResolver) {

        Map<String, String> valuesMap = new LinkedHashMap<>();
        Map<String, String> valuesMapInt = new LinkedHashMap<>();

        try {
            int position = startPosition;

            position = processNextFieldTramaClaro(MESSAGE_TYPE, messageOriginal, position, valuesMap, valuesMapInt);
            position = processNextFieldTramaClaro(BITMAP_PRIMARY, messageOriginal, position, valuesMap, valuesMapInt);

            String binaryBitMapPrimary = valuesMap.get(BITMAP_PRIMARY.getName());
            if (binaryBitMapPrimary.charAt(0) == '1') {
                position = processNextFieldTramaClaro(BITMAP_SECONDARY, messageOriginal, position, valuesMap, valuesMapInt);
            }
            String binaryBitMap = valuesMap.get(BITMAP_PRIMARY.getName())
                    .concat(valuesMap.getOrDefault(BITMAP_SECONDARY.getName(), ""));

            int positionActual = position;
            for (int i = 2; i <= binaryBitMap.length(); i++) {
                if (binaryBitMap.charAt(i - 1) == '1') {
                    ISOField field = fieldResolver.apply(i);

                    if (field == null) {
                        throw new ParserException(createMessageError(i, "There is no mapping available"));
                    }

                    String rawData = messageOriginal.substring(positionActual);
                    position = processNextFieldTramaClaro(field, rawData, valuesMap, valuesMapInt);
                    positionActual += position;
                }
            }
        } catch (ParserException e) {
            LOGGER.error("Cannot parse iso message at field {}: {}", valuesMapInt.size(), e.getMessage(), e);
            throw new ParserFieldsException("Cannot parse iso message: " + e.getMessage());
        }
        return valuesMap;
    }

    private static int processNextFieldTramaClaro(ISOField isoField, String isoMessage, int position,
            Map<String, String> mapString, Map<String, String> mapInt) {

        try {
            int fieldLength = isoField.getLength();

            if (isoField.getTypeData().equals(BINARY_STRING)) {
                fieldLength = isoField.getLength() * 2;
            }

            String value = isoMessage.substring(position, position + fieldLength);
            position += fieldLength;
            addFieldToMapTramaClaro(isoField, value, mapString, mapInt);
            return position;

        } catch (Exception e) {
            throw new ParserException(createMessageError(position, mapInt, isoField.getId(), e.getMessage()));
        }
    }

    private static int processNextFieldTramaClaro(ISOField isoField, String isoMessage, Map<String, String> mapString,
            Map<String, String> mapInt) {

        int position = 0;
        try {
            int fieldLength = isoField.getLength();

            if (isoField.isVariable()) {
                if (isoField.getId() == 123) {
                    fieldLength = 3;
                }
                String header = isoMessage.substring(position, position + fieldLength);
                position += fieldLength;
                fieldLength = Integer.parseInt(header);
            }

            String value = isoMessage.substring(position, position + fieldLength);
            position += fieldLength;
            addFieldToMapTramaClaro(isoField, value, mapString, mapInt);
            return position;

        } catch (Exception e) {
            throw new ParserException(createMessageError(position, mapInt, isoField.getId(), e.getMessage()));
        }
    }

    private static void addFieldToMapTramaClaro(ISOField isoField, String value, Map<String, String> mapString,
            Map<String, String> mapInt) {
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
            return ISOUtil.convertHEXtoBITMAP(value);
        } else {
            return value;
        }
    }

    private static String createMessageError(int id, String message) {
        return "Error found in field " + id + ": " + message;
    }

    private static String processHeader(String isoMessage) {
        int start = 0;
        int length = VisaISOField.HEADER.getLength();
        String longHeader = isoMessage.substring(start, length);
        if (longHeader.startsWith("0")) {
            return "1602180198880403000000021000489D102902000000";
        }
        int dec = Integer.parseInt(longHeader, 16);
        length = dec * 2;
        return isoMessage.substring(0, length);
    }

    private static String createMessageError(int positionLast, Map<String, String> mapFields, int id, String message) {
        StringBuilder resultado = new StringBuilder();
        for (Map.Entry<String, String> entry : mapFields.entrySet()) {
            resultado.append(entry.getKey()).append("=").append(entry.getValue()).append("; ");
        }
        return positionLast + " fieldLast : " + resultado + "Error found in field " + id + ": " + message;
    }
}
