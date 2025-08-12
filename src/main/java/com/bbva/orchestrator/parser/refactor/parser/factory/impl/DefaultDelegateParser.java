package com.bbva.orchestrator.parser.refactor.parser.factory.impl;

import com.bbva.orchestrator.parser.refactor.parser.factory.ISO8583DelegateParser;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
@Primary // Por si no se encuentra ninguno
public class DefaultDelegateParser implements ISO8583DelegateParser {

    @Override
    public Map<String, String> parser(String originalMessage) {
        throw new UnsupportedOperationException("No se puede parsear: red no soportada");
    }

    @Override
    public String unParser(Map<String, String> mappedFields) {
        throw new UnsupportedOperationException("No se puede unparse: red no soportada");
    }

    @Override
    public Map<String, String> mapSubFields(Map<String, String> values) {
        return new HashMap<>();
    }
}
