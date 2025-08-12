package com.bbva.orchestrator.parser.refactor.parser.factory.impl;

import com.bbva.orchestrator.network.visa.processor.ISOStringConverterVisa;
import com.bbva.orchestrator.network.visa.processor.ISOStringMapper;
import com.bbva.orchestrator.parser.common.ISO8583SubFieldsParser;
import com.bbva.orchestrator.parser.refactor.parser.factory.ISO8583DelegateParser;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import java.util.Map;


@Component
@Qualifier("visaDelegateParser")
public class VisaDelegateParser implements ISO8583DelegateParser {

    @Override
    public Map<String, String> parser(String originalMessage) {
        return ISOStringMapper.mapFields(originalMessage);
    }

    @Override
    public Map<String, String> mapSubFields(Map<String, String> values) {
        return ISO8583SubFieldsParser.mapSubFieldsVisa(values);
    }

    @Override
    public String unParser(Map<String, String> mappedFields) {
        return ISOStringConverterVisa.getInstance().convertToISOString(mappedFields,true);
    }
}
