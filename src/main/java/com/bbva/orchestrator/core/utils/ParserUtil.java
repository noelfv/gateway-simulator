package com.bbva.orchestrator.core.utils;

import com.bbva.gateway.sensitivedata.SensitiveDataHandler;
import com.bbva.orchestrator.core.exception.ParserFieldsException;
import com.bbva.orchestrator.core.fields.definitions.IFieldDefinition;
import com.bbva.orchestrator.core.fields.definitions.ISOField;
import com.bbva.orchestrator.core.parser.iso8583.ParsedFieldResult;
import com.bbva.orchestrator.core.parser.iso8583.handlers.NetworkHandlerField;
import com.bbva.orchlib.parser.ParserException;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class ParserUtil {

    private static final String PRIMARY_ACCOUNT_NUMBER = "primaryAccountNumber";
    private static final String MASK_CHAR = "*";
    private static final String DEFAULT_MASK_CHAR = "0";
    // ---- Metodos para Fields ----

    public static int processFieldData(ISOField isoField, StringBuilder isoMessage, int currentPosition, Map<String, String> valuesMap, NetworkHandlerField networkHandlerField) {
        try {
            String remainingMessageSegment = isoMessage.substring(currentPosition);
            ParsedFieldResult result;

            result = isoField.getParserStrategy().parse(remainingMessageSegment,  isoField,networkHandlerField);

            valuesMap.put(isoField.getName(), result.value());

            currentPosition += result.consumedLengthInChars(); // Usar getConsumedLength()

            return currentPosition;

        } catch (ParserFieldsException e) {
            throw new ParserException(FieldUtil.formatMessageException(e.getCode(),e.getDescription(),e));
        }
    }

    // -- > falta agregar el plain text, eso falta

    public static String createMessageError(int fieldId) {
        return "Error en campo " + fieldId + ": " + "No hay mapeo disponible";
    }

    // ---- Metodos para Subfields ----
    public static ParsedFieldResult processFixedLengthSubField(String rawDataSegment, IFieldDefinition fieldDefinition, NetworkHandlerField networkHandlerField, String errorCode) {
        try {
            int expectedHexLengthCalculated = networkHandlerField.decodeLengthField(fieldDefinition);
            String extractedHex = rawDataSegment.substring(0, expectedHexLengthCalculated);
            String decodedValue = networkHandlerField.decode(extractedHex, fieldDefinition.getTypeData());
            return new ParsedFieldResult(decodedValue, expectedHexLengthCalculated);
        } catch (RuntimeException e) {
            throw new ParserFieldsException(errorCode, "Error procesando campo " + fieldDefinition.getIdentifier() + " ¨[" + rawDataSegment + "]", e);
        }
    }

    public static String buildFixedLengthSubField(String processedDataSegment, IFieldDefinition fieldDefinition, NetworkHandlerField networkHandlerField) {
        return networkHandlerField.encode(processedDataSegment, fieldDefinition.getTypeData());
    }

    public static Map<String, String> maskSensitiveFields(Map<String, String> mapValues) {
        Map<String, String> mapMaskedValues = new HashMap<>(mapValues);
        // Define the fields to mask
        String[] sensitiveFields = {
                "primaryAccountNumber",
                "trackOneData",
                "trackTwoData"
        };

        for (String fieldName : sensitiveFields) {
            mapMaskedValues.computeIfPresent(fieldName, (key, value) -> {
                if (PRIMARY_ACCOUNT_NUMBER.equals(key)) {
                    return SensitiveDataHandler.obfuscate(value, "left", MASK_CHAR, 6, 6);
                } else {
                    return maskWithDefaultChar(value);
                }
            });
        }

        return mapMaskedValues;
    }

    private static  String maskWithDefaultChar(String value) {
        return value == null ? null : DEFAULT_MASK_CHAR.repeat(value.length());
    }
}
