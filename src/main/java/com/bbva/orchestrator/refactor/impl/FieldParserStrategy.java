package com.bbva.orchestrator.refactor.impl;

import com.bbva.orchestrator.parser.common.ISOField;

public interface FieldParserStrategy {
    // Parsea la porción de la trama (en HEX/EBCDIC) y devuelve el valor decodificado
    // 'rawData' es la parte de la trama correspondiente al campo
    // 'isoField' es la definición del campo (para acceder a length, isVariable, etc.)
    String parse(String rawData, ISOField isoField);

    // Construye la porción de la trama (en HEX/EBCDIC) a partir del valor decodificado
    // 'fieldValue' es el valor ya procesado (ej. "123456")
    // 'isoField' es la definición del campo
    String build(String fieldValue, ISOField isoField);
}
