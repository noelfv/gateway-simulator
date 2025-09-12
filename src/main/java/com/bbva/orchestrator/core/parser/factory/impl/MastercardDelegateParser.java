package com.bbva.orchestrator.core.parser.factory.impl;

import com.bbva.orchestrator.core.dto.ISO8583;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.network.mastercard.MastercardISOFieldParser;
import com.bbva.orchestrator.network.mastercard.MastercardISOSubFieldParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MastercardDelegateParser implements ISO8583DelegateParser {

    private static final String NETWORK_MASTERCARD = "PEER02";
    private final MastercardISOFieldParser fieldParser;
    private final MastercardISOSubFieldParser subFieldParser;

    @Override
    public Map<String, String> parser(String originalMessage) {
        Map<String, String> mappedFields=fieldParser.mapFields(originalMessage);
        mappedFields.put("networkName",NETWORK_MASTERCARD);
        return mappedFields;
    }

    @Override
    public String unParser(Map<String, String> mappedFields) {
        return fieldParser.unMapFields(mappedFields);
    }

    @Override
    public String unParserPlainText(Map<String, String> mappedFields) {
        return fieldParser.unMapFieldsPlainText(mappedFields);
    }

    @Override
    public Map<String, String> parserSubFields(ISO8583 iso8583) {
        return subFieldParser.parseSubfields(iso8583);
    }

}
