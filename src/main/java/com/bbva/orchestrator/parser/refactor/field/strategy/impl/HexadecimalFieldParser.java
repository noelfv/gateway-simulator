package com.bbva.orchestrator.parser.refactor.field.strategy.impl;

import com.bbva.orchestrator.parser.refactor.field.strategy.FieldParserStrategy;
import com.bbva.orchestrator.parser.refactor.field.IFieldDefinition;
import com.bbva.orchestrator.parser.refactor.field.ParsedFieldResult;
import com.bbva.orchlib.parser.ParserException;

public class HexadecimalFieldParser implements FieldParserStrategy {
    @Override
    public ParsedFieldResult parse(String rawDataSegment, int expectedHexLength, IFieldDefinition fieldDefinition) {
        if (rawDataSegment.length() < expectedHexLength) {
            throw new ParserException("Trama demasiado corta para el campo hexadecimal " + fieldDefinition.getIdentifier() + ". Esperado: " + expectedHexLength + ", Disponible: " + rawDataSegment.length());
        }
        String extractedHex = rawDataSegment.substring(0, expectedHexLength);
        return new ParsedFieldResult(extractedHex, expectedHexLength);
    }

    @Override
    public String build(String fieldValue, IFieldDefinition fieldDefinition) {
        String paddedValue = String.format("%-" + fieldDefinition.getLength() * 2 + "s", fieldValue).replace(' ', '0');
        return paddedValue.substring(0, fieldDefinition.getLength() * 2);
    }
}