package com.bbva.orchestrator.refactor.impl4;

import com.bbva.orchestrator.refactor.impl4.commons.IFieldDefinition;
import com.bbva.orchestrator.refactor.impl4.commons.ISOUtil;
import com.bbva.orchestrator.refactor.impl4.commons.ParsedFieldResult;
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