package com.bbva.orchestrator.refactor.impl2;

import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchlib.parser.ParserException;

public class NumericFieldParser implements FieldParserStrategy {
    @Override
    public ParsedFieldResult parse(String rawDataSegment, ISOField isoField) {
        int expectedLengthInChars = isoField.getLength() * 2;
        if (rawDataSegment.length() < expectedLengthInChars) {
            throw new ParserException("Trama demasiado corta para el campo fijo " + isoField.getId() + ". Esperado: " + expectedLengthInChars + ", Disponible: " + rawDataSegment.length());
        }
        String extractedHex = rawDataSegment.substring(0, expectedLengthInChars);
        String decodedValue = ISOUtil.ebcdicToString(extractedHex);
        return new ParsedFieldResult(decodedValue, expectedLengthInChars);
    }

    @Override
    public String build(String fieldValue, ISOField isoField) {
        String paddedValue = String.format("%" + isoField.getLength() + "s", fieldValue).replace(' ', '0');
        return ISOUtil.stringToEbcdicHex(paddedValue.substring(0, isoField.getLength()));
    }
}