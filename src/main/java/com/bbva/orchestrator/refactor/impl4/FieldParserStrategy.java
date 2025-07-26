package com.bbva.orchestrator.refactor.impl4;

import com.bbva.orchestrator.refactor.impl4.subfields.IFieldDefinition;
import com.bbva.orchlib.parser.ParserException;

public interface FieldParserStrategy {
    /**
     * Parsea una porción de la trama ISO (en formato hexadecimal EBCDIC) y devuelve el valor decodificado
     * junto con la cantidad de caracteres hexadecimales consumidos.
     *
     * @param rawDataSegment La porción de la trama HEX a parsear.
     * @param expectedHexLength La longitud esperada en caracteres HEX que este parser debe consumir.
     * Para campos fijos, es la longitud total del campo.
     * Para campos variables, es la longitud del valor *después* del prefijo.
     * @param fieldDefinition La definición del campo/subcampo (implementa IFieldDefinition).
     * @return Un objeto ParsedFieldResult que contiene el valor decodificado y la longitud consumida.
     * @throws ParserException Si la trama es demasiado corta o el formato es inválido.
     */
    ParsedFieldResult parse(String rawDataSegment, int expectedHexLength, IFieldDefinition fieldDefinition); // Tipo cambiado a IFieldDefinition

    /**
     * Construye la porción de la trama ISO (siempre en HEX EBCDIC para la salida) a partir de un valor decodificado.
     *
     * @param fieldValue El valor del campo ya decodificado (ej. "1234.56", "ABC").
     * @param fieldDefinition La definición del campo/subcampo.
     * @return La representación hexadecimal EBCDIC del campo lista para ser insertada en la trama.
     */
    String build(String fieldValue, IFieldDefinition fieldDefinition); // Tipo cambiado a IFieldDefinition
}
