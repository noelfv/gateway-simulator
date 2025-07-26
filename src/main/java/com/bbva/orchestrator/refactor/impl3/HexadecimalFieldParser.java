package com.bbva.orchestrator.refactor.impl3;

import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchlib.parser.ParserException;

public class HexadecimalFieldParser implements FieldParserStrategy {
    @Override
    public ParsedFieldResult parse(String rawDataSegment, int expectedHexLength, ISOField isoField) {
        if (rawDataSegment.length() < expectedHexLength) {
            throw new ParserException("Trama demasiado corta para el campo hexadecimal " + isoField.getId() + ". Esperado: " + expectedHexLength + ", Disponible: " + rawDataSegment.length());
        }
        String extractedHex = rawDataSegment.substring(0, expectedHexLength);
        // Para HEXADECIMAL, el valor es la propia cadena HEX
        return new ParsedFieldResult(extractedHex, expectedHexLength);
    }

    @Override
    public String build(String fieldValue, ISOField isoField) {
        // Asegurarse de que el valor sea HEX válido y tenga la longitud correcta
        // Puede requerir relleno o truncado si la longitud lo exige.
        String paddedValue = String.format("%-" + isoField.getLength() * 2 + "s", fieldValue).replace(' ', '0');
        return paddedValue.substring(0, isoField.getLength() * 2);
    }
}