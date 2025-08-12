package com.bbva.orchestrator.parser.refactor.mapper.iso20022.strategy;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import java.util.Map;

//@FunctionalInterface
public interface SectionMappingStrategy<T> {
    String DEFAULT_EMPTY_VALUE = "";

    T map(ISO8583 input, Map<String, String> subFields);

    void translate(ISO20022 input, ISO8583.ISO8583Builder iso8583Builder);
}
