package com.bbva.orchestrator.parser.refactor.field.strategy.impl;

import com.bbva.orchestrator.parser.refactor.field.strategy.FieldParserStrategy;
import com.bbva.orchestrator.parser.refactor.field.IFieldDefinition;
import com.bbva.orchestrator.parser.refactor.field.ParsedFieldResult;
import com.bbva.orchestrator.parser.refactor.utils.ISOUtil;
import com.bbva.orchlib.parser.ParserException;

public class  NumericFieldParser implements FieldParserStrategy {
    @Override
    public ParsedFieldResult parse(String rawDataSegment, int expectedHexLength, IFieldDefinition fieldDefinition) {
        if (rawDataSegment.length() < expectedHexLength) {
            throw new ParserException("Trama demasiado corta para el campo fijo " + fieldDefinition.getIdentifier() + ". Esperado: " + expectedHexLength + ", Disponible: " + rawDataSegment.length());
        }
        String extractedHex = rawDataSegment.substring(0, expectedHexLength);
        String decodedValue = ISOUtil.ebcdicToString(extractedHex);
        return new ParsedFieldResult(decodedValue, expectedHexLength);
    }

    @Override
    public String build(String fieldValue, IFieldDefinition fieldDefinition) {
        String paddedValue = ISOUtil.padAsciiLeft(fieldValue, fieldDefinition.getLength());
        return ISOUtil.stringToEBCDICHex(paddedValue);
    }
}