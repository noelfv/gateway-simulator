package com.bbva.orchestrator.refactor.impl3;

import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchlib.parser.ParserException;

public class AlphaNumericFieldParser implements FieldParserStrategy {
    @Override
    public ParsedFieldResult parse(String rawDataSegment, int expectedHexLength, ISOField isoField) {
        if (rawDataSegment.length() < expectedHexLength) {
            throw new ParserException("Trama demasiado corta para el campo fijo " + isoField.getId() + ". Esperado: " + expectedHexLength + ", Disponible: " + rawDataSegment.length());
        }
        String extractedHex = rawDataSegment.substring(0, expectedHexLength);
        // Utiliza el convertHEXtoEBCDIC de tu ISOUtil, que ahora usa Cp1047
        String decodedValue = ISOUtil.convertHEXtoEBCDIC(extractedHex);
        return new ParsedFieldResult(decodedValue, expectedHexLength);
    }

    @Override
    public String build(String fieldValue, ISOField isoField) {
        // Asegura que el valor se rellene a la longitud del campo antes de convertir a EBCDIC HEX
        String paddedValue = String.format("%-" + isoField.getLength() + "s", fieldValue);
        // Utiliza el stringToEBCDICHex de tu ISOUtil, que usa Cp1047
        return ISOUtil.stringToEBCDICHex(paddedValue.substring(0, isoField.getLength()));
    }
}