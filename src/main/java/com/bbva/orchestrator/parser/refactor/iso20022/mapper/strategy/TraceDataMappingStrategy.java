package com.bbva.orchestrator.parser.refactor.iso20022.mapper.strategy;

import com.bbva.gateway.dto.iso20022.TraceDataDTO;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.refactor.iso20022.mapper.SectionMappingStrategy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TraceDataMappingStrategy implements SectionMappingStrategy<List<TraceDataDTO>> {
    @Override
    public List<TraceDataDTO> map(ISO8583 input, Map<String, String> subFields) {

        TraceDataDTO posAdditionalData = TraceDataDTO.builder()
                .key("posAdditionalData")
                .value(input.getNetworkData())
                .build();

        // ======== HEADER OF ISO ========
        TraceDataDTO originHeader = TraceDataDTO.builder()
                .key("header")
                .value(input.getHeader())
                .build();

        // ======== TEMPORAL FIELD NOT ASSOCIATED WITH ISO ========
        TraceDataDTO traceDataDTO = TraceDataDTO.builder()
                .key("PAYMENT_ID")
                .value(GrpcHeadersInfo.getTraceId())
                .build();

        List<TraceDataDTO> traceDataList = new ArrayList<>();
        traceDataList.add(posAdditionalData);
        traceDataList.add(originHeader);
        traceDataList.add(traceDataDTO);

        return traceDataList;
    }
}
