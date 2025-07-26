package com.bbva.orchestrator.refactor.impl3;

import com.bbva.orchestrator.parser.common.ISOField;

public interface FieldParserStrategy {
    /**
     * Parsea una porción de la trama ISO (en formato hexadecimal) y devuelve el valor decodificado
     * junto con la cantidad de caracteres hexadecimales consumidos.
     *
     * @param rawDataSegment La porción de la trama HEX a parsear.
     * @param expectedHexLength La longitud esperada en caracteres HEX que este parser debe consumir.
     * Para campos fijos, es la longitud total del campo.
     * Para campos variables, es la longitud del valor *después* del prefijo.
     * @param isoField La definición del campo ISO (desde el enum).
     * @return Un objeto ParsedFieldResult que contiene el valor decodificado y la longitud consumida.
     * @throws ParserException Si la trama es demasiado corta o el formato es inválido.
     */
    ParsedFieldResult parse(String rawDataSegment, int expectedHexLength, ISOField isoField);

    /**
     * Construye la porción de la trama ISO (en formato hexadecimal) a partir de un valor decodificado.
     *
     * @param fieldValue El valor del campo ya decodificado (ej. "1234.56", "ABC").
     * @param isoField La definición del campo ISO.
     * @return La representación hexadecimal del campo lista para ser insertada en la trama.
     */
    String build(String fieldValue, ISOField isoField);
}