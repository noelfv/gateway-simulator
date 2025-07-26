package com.bbva.orchestrator.refactor.impl;

import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchestrator.parser.common.ISOUtil;

// Implementación para datos NUMERIC (relleno con ceros, conversión de EBCDIC a String)
public class NumericFieldParser implements FieldParserStrategy {
    @Override
    public String parse(String rawData, ISOField isoField) {
        // Asume que rawData viene en EBCDIC HEX y necesita ser convertido a String
        return ISOUtil.ebcdicToString(rawData);
    }

    @Override
    public String build(String fieldValue, ISOField isoField) {
        // Rellena con ceros a la izquierda y convierte a EBCDIC HEX
        String paddedValue = String.format("%" + isoField.getLength() + "s", fieldValue).replace(' ', '0');
        return ISOUtil.stringToEBCDICHex(paddedValue.substring(0, isoField.getLength()));

    }
}