package com.bbva.orchestrator.refactor.impl3;

import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchlib.parser.ParserException;

public class NumericDecimalFieldParser implements FieldParserStrategy {
    @Override
    public ParsedFieldResult parse(String rawDataSegment, int expectedHexLength, ISOField isoField) {
        if (rawDataSegment.length() < expectedHexLength) {
            throw new ParserException("Trama demasiado corta para el campo decimal " + isoField.getId() + ". Esperado: " + expectedHexLength + ", Disponible: " + rawDataSegment.length());
        }
        String extractedHex = rawDataSegment.substring(0, expectedHexLength);
        // Primero decodifica de EBCDIC a String (numérico)
        String numericValue = ISOUtil.ebcdicToString(extractedHex);
        // Luego valida y formatea el monto
        String decodedValue = ISOUtil.validAmount(numericValue);
        return new ParsedFieldResult(decodedValue, expectedHexLength);
    }

    @Override
    public String build(String fieldValue, ISOField isoField) {
        // Primero revierte el formato del monto (ej. "1234.56" -> "123456")
        String cleanValue = ISOUtil.revertValidAmount(fieldValue);
        // Rellena con ceros a la izquierda y convierte a EBCDIC HEX
        String paddedValue = String.format("%" + isoField.getLength() + "s", cleanValue).replace(' ', '0');
        return ISOUtil.stringToEBCDICHex(paddedValue.substring(0, isoField.getLength()));
    }
}