package com.bbva.orchestrator.refactor.impl2;

import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchlib.parser.ParserException;

public class NumericDecimalFieldParser implements FieldParserStrategy {
    @Override
    public ParsedFieldResult parse(String rawDataSegment, ISOField isoField) {
        int expectedLengthInChars = isoField.getLength() * 2;
        if (rawDataSegment.length() < expectedLengthInChars) {
            throw new ParserException("Trama demasiado corta para el campo decimal " + isoField.getId() + ". Esperado: " + expectedLengthInChars + ", Disponible: " + rawDataSegment.length());
        }
        String extractedHex = rawDataSegment.substring(0, expectedLengthInChars);
        String numericValue = ISOUtil.ebcdicToString(extractedHex);
        String decodedValue = ISOUtil.validAmount(numericValue);
        return new ParsedFieldResult(decodedValue, expectedLengthInChars);
    }

    @Override
    public String build(String fieldValue, ISOField isoField) {
        String cleanValue = fieldValue.replace(".", "").replace(",", "");
        String paddedValue = String.format("%" + isoField.getLength() + "s", cleanValue).replace(' ', '0');
        return ISOUtil.stringToEbcdicHex(paddedValue.substring(0, isoField.getLength()));
    }
}