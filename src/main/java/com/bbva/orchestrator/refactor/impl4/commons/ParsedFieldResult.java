package com.bbva.orchestrator.refactor.impl4.commons;


/**
 * Represents the result of parsing a field, containing the parsed value and the number of characters consumed.
 * This is used to track how many characters were processed during the parsing operation.
 * * @param value The parsed value of the field.
 * * @param consumedLengthInChars The number of characters consumed during parsing.
 */
public record ParsedFieldResult(String value, int consumedLengthInChars) {

}