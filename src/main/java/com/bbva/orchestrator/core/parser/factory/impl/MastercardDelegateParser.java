package com.bbva.orchestrator.core.parser.factory.impl;

import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.network.mastercard.MastercardISOFieldParser;
import com.bbva.orchestrator.network.mastercard.MastercardISOSubFieldParser;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MastercardDelegateParser implements ISO8583DelegateParser {

    private final MastercardISOFieldParser fieldParser;
    private final MastercardISOSubFieldParser subFieldParser;

    /**
     * Constructor que recibe los parsers de campo y subcampo de Mastercard.
     * @param fieldParser Parser de campos de Mastercard.
     * @param subFieldParser Parser de subcampos de Mastercard.
     */
    public MastercardDelegateParser(MastercardISOFieldParser fieldParser,
                                    MastercardISOSubFieldParser subFieldParser) {
        this.fieldParser = fieldParser;
        this.subFieldParser = subFieldParser;
    }

    @Override
    public Map<String, String> parser(String originalMessage) {
        return fieldParser.mapFields(originalMessage);
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
