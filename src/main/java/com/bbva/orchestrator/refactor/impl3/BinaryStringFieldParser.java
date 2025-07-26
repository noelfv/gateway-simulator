package com.bbva.orchestrator.refactor.impl3;

import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchlib.parser.ParserException;

public class BinaryStringFieldParser implements FieldParserStrategy {
    @Override
    public ParsedFieldResult parse(String rawDataSegment, int expectedHexLength, ISOField isoField) {
        if (rawDataSegment.length() < expectedHexLength) {
            throw new ParserException("Trama demasiado corta para el campo binario " + isoField.getId() + ". Esperado: " + expectedHexLength + ", Disponible: " + rawDataSegment.length());
        }
        String extractedHex = rawDataSegment.substring(0, expectedHexLength);
        // Utiliza el convertHEXtoBITMAP de tu ISOUtil
        String decodedValue = ISOUtil.convertHEXtoBITMAP(extractedHex);
        return new ParsedFieldResult(decodedValue, expectedHexLength);
    }

    @Override
    public String build(String fieldValue, ISOField isoField) {
        // Utiliza el convertBITMAPtoHEX de tu ISOUtil
        return ISOUtil.convertBITMAPtoHEX(fieldValue);
    }
}