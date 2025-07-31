package com.bbva.orchestrator.parser.refactor.field;

/**
 * @param value
 * @param consumedLengthInChars Longitud de los caracteres HEX consumidos de la trama
 */

public record ParsedFieldResult(String value, int consumedLengthInChars) {

}