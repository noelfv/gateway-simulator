package com.bbva.orchestrator.refactor.impl;

import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchestrator.parser.common.ISOUtil;

// Implementación para BINARY_STRING (Bitmaps, requiere manejo especial)
public class BinaryStringFieldParser implements FieldParserStrategy {
    @Override
    public String parse(String rawData, ISOField isoField) {
        // Convierte el HEX del bitmap a su representación binaria (String de '0's y '1's)
        return ISOUtil.convertHEXtoBITMAP(rawData);
    }

    @Override
    public String build(String fieldValue, ISOField isoField) {
        // Convierte el String binario de vuelta a HEX para el bitmap
        // El 'fieldValue' es "0101..." y debe convertirse a "4000..." si es BCD o similar.
        // Esto depende de cómo ISOUtil.convertHEXtoBITMAP funciona inversamente.
        // Generalmente, un bitmap binario se representa en la trama como hexadecimal (ej. 8 bytes = 16 chars HEX).
        //return ISOUtil.convertBinaryToHex(fieldValue);
        return null;
    }
}