package com.bbva.orchestrator.refactor.impl2;

public class ParsedFieldResult {
    private final String value;
    private final int consumedLengthInChars; // Longitud de los caracteres HEX consumidos de la trama

    public ParsedFieldResult(String value, int consumedLengthInChars) {
        this.value = value;
        this.consumedLengthInChars = consumedLengthInChars;
    }

    public String getValue() {
        return value;
    }

    public int getConsumedLengthInChars() {
        return consumedLengthInChars;
    }
}