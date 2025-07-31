package com.bbva.orchestrator.parser;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchestrator.parser.common.ISO8583Context;
import com.bbva.orchestrator.parser.factory.FactoryParser;
import com.bbva.orchestrator.parser.factory.MessageParser;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.iso8583.ISO8583Builder;
import com.bbva.orchestrator.parser.iso20022.ISO20022To8583Mapper;
import com.bbva.orchestrator.parser.iso20022.ISO8583To20022Mapper;
import com.bbva.orchestrator.parser.refactor.iso20022.ISO20022Builder;
import com.bbva.orchestrator.validations.PassThroughValidator;
import com.bbva.orchlib.parser.IParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class Parser implements IParser {

    @Autowired
    private ISO20022Builder iso20022Builder;

    @Override
    public ISO20022 convert8583to20022(String originalMessage) {
        MessageParser messageParser = FactoryParser.getMessageParser();
        Map<String, String> values = messageParser.parser(originalMessage);
        Map<String, String> subFields = messageParser.mapSubFields(values);
        ISO8583 iso8583 = ISO8583Builder.buildISO8583(originalMessage, values);
        ISO8583Context.storeISO8583(iso8583,values,messageParser);
        //ISO20022 iso20022 = ISO8583To20022Mapper.translateToISO20022(iso8583, subFields);
        ISO20022 iso20022 = iso20022Builder.process(iso8583,subFields, "PEER02");
        iso20022 = messageParser.reMap(iso8583, iso20022);

        return iso20022;
    }

    @Override
    public String convert20022to8583(ISO20022 inputObject) {
        ISO8583Context.removeStoreId();
        if (PassThroughValidator.isPassThroughEnabled(inputObject)) {
            // Se obtiene la trama original
            return ISO20022To8583Mapper.getOriginalIsoMessage(inputObject);
        }

        // Se genera la trama 8583 a partir del 20022
        MessageParser messageParser = FactoryParser.getMessageParser();
        inputObject = messageParser.unMap(inputObject);
        ISO8583 iso8583 = ISO20022To8583Mapper.translateToISO8583(inputObject);
        Map<String, String> values = ISO8583Builder.buildMapISO8583(iso8583);
        return messageParser.unParser(values, false);
    }
}
