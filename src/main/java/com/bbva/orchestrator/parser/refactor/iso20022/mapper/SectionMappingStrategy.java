package com.bbva.orchestrator.parser.refactor.iso20022.mapper;

import com.bbva.orchestrator.parser.iso8583.ISO8583;
import java.util.Map;

@FunctionalInterface
public interface SectionMappingStrategy<T> {

    T map(ISO8583 input, Map<String, String> subFields);

}
