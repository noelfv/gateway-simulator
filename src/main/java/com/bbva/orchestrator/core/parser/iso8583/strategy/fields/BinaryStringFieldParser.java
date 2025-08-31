package com.bbva.orchestrator.core.parser.iso8583.strategy.fields;

import com.bbva.orchestrator.core.exception.ParserLocalException;
import com.bbva.orchestrator.core.fields.definitions.IFieldDefinition;
import com.bbva.orchestrator.core.parser.iso8583.ParsedFieldResult;
import com.bbva.orchestrator.core.parser.iso8583.handlers.NetworkHandlerField;
import com.bbva.orchestrator.core.parser.iso8583.strategy.FieldParserStrategy;

/**
 * Implementación de la estrategia de parsing para campos BinaryString.
 * Esta clase maneja la conversión de datos BinaryString a partir de segmentos hexadecimales
 * y viceversa.
 * */
public class BinaryStringFieldParser implements FieldParserStrategy {

    @Override
    public ParsedFieldResult parse(String rawDataSegment, IFieldDefinition fieldDefinition, NetworkHandlerField networkHandlerField) {
        try {

            int expectedHexLengthCalculated = networkHandlerField.decodeLengthField(fieldDefinition);
            String extractedHex = rawDataSegment.substring(0, expectedHexLengthCalculated);
            String decodedValue = networkHandlerField.decode(extractedHex,fieldDefinition.getTypeData());
            return new ParsedFieldResult(decodedValue, expectedHexLengthCalculated);
        } catch (RuntimeException e) {
            throw new ParserLocalException("PGWP-00104","Error procesando campo " + fieldDefinition.getIdentifier() + " ¨[" + rawDataSegment + "]",e);
        }
    }

    @Override
    public String build(String processedDataSegment, IFieldDefinition fieldDefinition, NetworkHandlerField networkHandlerField) {
        return networkHandlerField.encode(processedDataSegment,fieldDefinition.getTypeData());
    }
}
