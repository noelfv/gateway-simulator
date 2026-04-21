package com.bbva.orchestrator.core;

import com.bbva.gateway.dto.iso20022.AddendumDataDTO;
import com.bbva.gateway.dto.iso20022.AdditionalDataDTO;
import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.core.dto.ISO8583;
import com.bbva.orchestrator.core.builders.ISO8583Builder;
import com.bbva.orchestrator.core.logic.factory.FieldLogicFactory;
import com.bbva.orchestrator.core.logic.factory.NetworkDelegateFieldLogic;
import com.bbva.orchestrator.core.mapper.factory.ISO20022DelegateMapper;
import com.bbva.orchestrator.core.mapper.factory.MapperFactory;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import com.bbva.orchlib.parser.IParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class OrchestratorFlowProcess implements IParser {

    private final ParserFactory parserFactory;
    private final MapperFactory mapperFactory;
    private final FieldLogicFactory fieldLogicFactory;

    @Override
    public ISO20022 convert8583to20022(String originalMessage, String label) {
        String networkName = GrpcHeadersInfo.getNetwork();
        ISO8583DelegateParser delegateParser = parserFactory.getDelegateParser(networkName);
        Map<String, String> fieldsValues = delegateParser.parser(originalMessage);
        NetworkDelegateFieldLogic delegateFieldLogic = fieldLogicFactory.getDelegateFieldLogic(networkName);
        ISO8583 iso8583 = ISO8583Builder.buildISO8583(originalMessage, fieldsValues);
        Map<String, String> subFieldsValues = delegateFieldLogic.parseSubfields(iso8583);
        ISO20022DelegateMapper delegateMapper = mapperFactory.getDelegateMapper(networkName);
        return delegateMapper.mapper(iso8583, subFieldsValues, label);
    }

    @Override
    public String convert20022to8583(ISO20022 iso20022) {
        Boolean isNextGen = iso20022.getMonitoring().getIsNextGen();
        if (isNextGen) {
            return flowPaymentAuthorization(iso20022);
        }
        // TODO traza opcional, para production se debe eliminar
        return flowPassThrough(iso20022, "ISO8583_HOST");
    }

    @Override
    public ISO20022 reversalInternal(ISO20022 iso20022Message) {
        return null;
    }

    private String flowPaymentAuthorization(ISO20022 iso20022) {
        ISO20022DelegateMapper delegateMapper = mapperFactory.getDelegateMapper();
        Map<String, String> fieldsValues = delegateMapper.unMapper(iso20022);
        NetworkDelegateFieldLogic delegateFieldLogic = fieldLogicFactory
                .getDelegateFieldLogic(iso20022.getNetworkName());
        Map<String, String> fieldsValuesResponse = delegateFieldLogic.applyLogicFields(fieldsValues);
        ISO8583DelegateParser delegateParser = parserFactory.getDelegateParser();
        return delegateParser.unParser(fieldsValuesResponse);
    }

    private String flowPassThrough(ISO20022 iso20022, String keyTramaOrigin) {
        return findValueByKey(iso20022.getAddendumData(), keyTramaOrigin);
    }

    private String findValueByKey(AddendumDataDTO input, String key) {
        if (input == null || input.getAdditionalData() == null) {
            return null;
        }
        return input.getAdditionalData().stream()
                .filter(data -> key.equals(data.getKey()))
                .map(AdditionalDataDTO::getValue)
                .findFirst()
                .orElse(null);
    }
}