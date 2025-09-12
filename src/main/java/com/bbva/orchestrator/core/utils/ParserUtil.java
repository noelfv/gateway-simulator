package com.bbva.orchestrator.core.utils;

import com.bbva.orchestrator.core.exception.ParserFieldsException;
import com.bbva.orchestrator.core.fields.definitions.IFieldDefinition;
import com.bbva.orchestrator.core.fields.definitions.ISOField;
import com.bbva.orchestrator.core.parser.iso8583.ParsedFieldResult;
import com.bbva.orchestrator.core.parser.iso8583.handlers.NetworkHandlerField;
import com.bbva.orchlib.parser.ParserException;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
public class ParserUtil {

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
            throw new ParserException(ISOUtil.formatMessageException(e.getCode(),e.getDescription(),e));
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
}
