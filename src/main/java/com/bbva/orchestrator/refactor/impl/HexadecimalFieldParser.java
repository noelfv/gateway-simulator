package com.bbva.orchestrator.refactor.impl;

import com.bbva.orchestrator.parser.common.ISOField;

// Implementación para HEXADECIMAL (si es necesario un campo que solo sea HEX puro)
public class HexadecimalFieldParser implements FieldParserStrategy {
    @Override
    public String parse(String rawData, ISOField isoField) {
        return rawData; // Ya viene en HEX
    }

    @Override
    public String build(String fieldValue, ISOField isoField) {
        // Asegurarse de que el valor sea HEX válido y tenga la longitud correcta
        // Puede requerir relleno o truncado si la longitud lo exige.
        String paddedValue = String.format("%-" + isoField.getLength() * 2 + "s", fieldValue).replace(' ', '0');
        return paddedValue.substring(0, isoField.getLength() * 2);
    }
}
