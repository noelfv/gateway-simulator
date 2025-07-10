package org.example.orchestrator.common;

import java.util.Map;

public class ParseResult {
    private Map<String, String> fieldsByDescription;
    private Map<String, String> fieldsById;

    public ParseResult(Map<String, String> fieldsByDescription, Map<String, String> fieldsById) {
        this.fieldsByDescription = fieldsByDescription;
        this.fieldsById = fieldsById;
    }

    public Map<String, String> getFieldsByDescription() {
        return fieldsByDescription;
    }

    public Map<String, String> getFieldsById() {
        return fieldsById;
    }
}