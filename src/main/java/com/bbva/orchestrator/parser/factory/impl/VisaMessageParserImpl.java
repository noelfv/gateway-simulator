package com.bbva.orchestrator.parser.factory.impl;

import com.bbva.orchestrator.parser.common.ISO8583SubFieldsParser;
import com.bbva.orchestrator.parser.factory.MessageParser;
import com.bbva.orchestrator.network.visa.processor.ISOStringConverterVisa;
import com.bbva.orchestrator.network.visa.processor.ISOStringMapper;

import java.util.Map;

public class VisaMessageParserImpl implements MessageParser {


    public VisaMessageParserImpl() {
    }

    @Override
    public Map<String, String> parser(String originalMessage) {
        return ISOStringMapper.mapFields(originalMessage);
    }

    @Override
    public Map<String, String> mapSubFields(Map<String, String> values) {
        return ISO8583SubFieldsParser.mapSubFieldsVisa(values);
    }

    @Override
    public String unParser(Map<String, String> mappedFields, boolean clean) {
        return ISOStringConverterVisa.getInstance().convertToISOString(mappedFields, clean);
    }
}