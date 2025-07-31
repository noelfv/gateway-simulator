package com.bbva.orchestrator.parser.refactor.field.strategy.impl;

import com.bbva.orchestrator.parser.refactor.field.strategy.FieldParserStrategy;
import com.bbva.orchestrator.parser.refactor.field.IFieldDefinition;
import com.bbva.orchestrator.parser.refactor.field.ParsedFieldResult;
import com.bbva.orchestrator.parser.refactor.utils.ISOUtil;
import com.bbva.orchlib.parser.ParserException;

public class NumericDecimalFieldParser implements FieldParserStrategy {
    @Override
    public ParsedFieldResult parse(String rawDataSegment, int expectedHexLength, IFieldDefinition fieldDefinition) {
        if (rawDataSegment.length() < expectedHexLength) {
            throw new ParserException("Trama demasiado corta para el campo decimal " + fieldDefinition.getIdentifier() + ". Esperado: " + expectedHexLength + ", Disponible: " + rawDataSegment.length());
        }
        String extractedHex = rawDataSegment.substring(0, expectedHexLength);
        String numericValue = ISOUtil.ebcdicToString(extractedHex);
        String decodedValue = ISOUtil.validAmount(numericValue);
        return new ParsedFieldResult(decodedValue, expectedHexLength);
    }

    @Override
    public String build(String fieldValue, IFieldDefinition fieldDefinition) {
        String cleanValue = ISOUtil.revertValidAmount(fieldValue);
        String paddedValue = ISOUtil.padAsciiLeft(cleanValue, fieldDefinition.getLength());
        return ISOUtil.stringToEBCDICHex(paddedValue);
    }
}