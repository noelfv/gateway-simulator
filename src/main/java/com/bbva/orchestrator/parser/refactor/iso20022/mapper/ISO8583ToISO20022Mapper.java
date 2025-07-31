package com.bbva.orchestrator.parser.refactor.iso20022.mapper;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import java.util.Map;

public interface ISO8583ToISO20022Mapper {

    ISO20022 translate(ISO8583 input, Map<String, String> subFields);
}
