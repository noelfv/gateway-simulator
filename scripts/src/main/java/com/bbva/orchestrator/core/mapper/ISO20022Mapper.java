package com.bbva.orchestrator.core.mapper;

import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.mapper.factory.ISO20022DelegateMapper;
import com.bbva.orchestrator.core.mapper.factory.MapperFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ISO20022Mapper {

    private final MapperFactory mapperFactory;

    public ISO20022 build(ISO8583 input, Map<String, String> subFields) {
        String network= GrpcHeadersInfo.getNetwork();
        ISO20022DelegateMapper mapper = mapperFactory.getDelegateMapper(network);
        if (mapper == null) {
            throw new IllegalStateException("No mapper available for network: " + network);
        }
        return mapper.mapper(input, subFields);
    }
}