package com.bbva.orchestrator.parser.refactor.mapper.factory;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import java.util.Map;

public interface ISO20022DelegateMapper {

    ISO20022 translate(ISO8583 input, Map<String, String> subFields);
}
