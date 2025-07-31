package com.bbva.orchestrator.parser.refactor.iso20022;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.refactor.iso20022.mapper.ISO8583ToISO20022Mapper;
import com.bbva.orchestrator.parser.refactor.iso20022.mapper.factory.MapperFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ISO20022Builder {

    private final MapperFactory mapperFactory;

    public ISO20022 process(ISO8583 input, Map<String, String> subFields, String network) {
        ISO8583ToISO20022Mapper mapper = mapperFactory.getMapper(network);
        if (mapper == null) {
            throw new IllegalStateException("No mapper available for network: " + network);
        }
        return mapper.translate(input, subFields);
    }
}