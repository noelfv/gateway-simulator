package com.bbva.orchestrator.parser.refactor.mapper.iso20022.strategy;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.refactor.mapper.exception.MappingException;

public interface ISO8583ToISO20022Mapper {
    ISO20022   map(ISO8583 input) throws MappingException;
}
