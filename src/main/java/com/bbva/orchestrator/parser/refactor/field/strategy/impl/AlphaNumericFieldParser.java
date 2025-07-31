package com.bbva.orchestrator.parser.refactor.field.strategy.impl;

import com.bbva.orchestrator.parser.refactor.field.strategy.FieldParserStrategy;
import com.bbva.orchestrator.parser.refactor.field.IFieldDefinition;
import com.bbva.orchestrator.parser.refactor.field.ParsedFieldResult;
import com.bbva.orchestrator.parser.refactor.utils.ISOUtil;
import com.bbva.orchlib.parser.ParserException;

/**
 * Implementación de la estrategia de parsing para campos alfanuméricos.
 * Esta clase maneja la conversión de datos alfanuméricos a partir de segmentos hexadecimales
 * y viceversa.
 * */
public class AlphaNumericFieldParser implements FieldParserStrategy {
    @Override
    public ParsedFieldResult parse(String rawDataSegment, int expectedHexLength, IFieldDefinition fieldDefinition) {
        if (rawDataSegment.length() < expectedHexLength) {
            throw new ParserException("Trama demasiado corta para el campo fijo " + fieldDefinition.getIdentifier() + ". Esperado: " + expectedHexLength + ", Disponible: " + rawDataSegment.length());
        }
        String extractedHex = rawDataSegment.substring(0, expectedHexLength);
        String decodedValue = ISOUtil.convertHEXtoEBCDIC(extractedHex);
        return new ParsedFieldResult(decodedValue, expectedHexLength);
    }

    @Override
    public String build(String fieldValue, IFieldDefinition fieldDefinition) {
        String paddedValue = ISOUtil.padAsciiRight(fieldValue, fieldDefinition.getLength());
        return ISOUtil.stringToEBCDICHex(paddedValue);
    }
}