package com.bbva.orchestrator.core.logic.subfields;

import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.fields.definitions.ISOSubField;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MastercardSubFieldLogic implements NetworkSubFieldLogic {

    @Override
    public Map<String, String> apply(ISO8583 iso8583, ISOSubField isoSubField) {
        return Map.of();
    }
}
