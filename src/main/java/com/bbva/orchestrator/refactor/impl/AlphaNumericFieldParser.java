package com.bbva.orchestrator.refactor.impl;

import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchestrator.parser.common.ISOUtil;

// Implementación para datos ALPHANUMERIC (relleno con espacios, conversión de HEX a EBCDIC)
public class AlphaNumericFieldParser implements FieldParserStrategy {
    @Override
    public String parse(String rawData, ISOField isoField) {
        // Asume que rawData viene en HEX y necesita ser convertido a EBCDIC (ASCII en Java)
        return ISOUtil.convertHEXtoEBCDIC(rawData);
    }

    @Override
    public String build(String fieldValue, ISOField isoField) {
        // Rellena con espacios y convierte a HEX/EBCDIC para la trama
        String paddedValue = String.format("%-" + isoField.getLength() + "s", fieldValue);
        //return ISOUtil.ebcdicToHex(paddedValue.substring(0, isoField.getLength()));
        return ISOUtil.convertEBCDICtoHEX(paddedValue.substring(0, isoField.getLength()));
    }
}