package com.bbva.orchestrator.core.logic.subfields;

import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.fields.definitions.ISOSubField;

import java.util.Map;

@FunctionalInterface
public interface NetworkSubFieldLogic {

    Map<String, String>  apply(ISO8583 iso8583, ISOSubField isoSubField);

}
