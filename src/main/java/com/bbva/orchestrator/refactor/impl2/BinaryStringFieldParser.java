package com.bbva.orchestrator.refactor.impl2;

import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchlib.parser.ParserException;

public class BinaryStringFieldParser implements FieldParserStrategy {
    @Override
    public ParsedFieldResult parse(String rawDataSegment, ISOField isoField) {
        int expectedLengthInChars = isoField.getLength() * 2; // Bitmaps son 8 bytes = 16 chars HEX
        if (rawDataSegment.length() < expectedLengthInChars) {
            throw new ParserException("Trama demasiado corta para el campo binario " + isoField.getId() + ". Esperado: " + expectedLengthInChars + ", Disponible: " + rawDataSegment.length());
        }
        String extractedHex = rawDataSegment.substring(0, expectedLengthInChars);
        String decodedValue =ISOUtil.convertHEXtoBITMAP(extractedHex);
        return new ParsedFieldResult(decodedValue, expectedLengthInChars);
    }

    @Override
    public String build(String fieldValue, ISOField isoField) {
        return ISOUtil.convertBinaryToHex(fieldValue);
    }
}