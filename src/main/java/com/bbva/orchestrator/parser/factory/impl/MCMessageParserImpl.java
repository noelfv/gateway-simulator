package com.bbva.orchestrator.parser.factory.impl;

import com.bbva.gateway.dto.iso20022.AdditionalDataDTO;
import com.bbva.gateway.dto.iso20022.ISO20022;

import com.bbva.orchestrator.parser.factory.MessageParser;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.network.mastercard.processor.ISOStringConverterMastercard;
import com.bbva.orchestrator.network.mastercard.processor.ISOStringMapper;
import com.bbva.orchestrator.parser.common.ISO8583SubFieldsParser;
import com.bbva.orchestrator.parser.refactor.mastercard.ISOFieldMastercardParser;
import com.bbva.orchestrator.parser.refactor.mastercard.ISOSubFieldMastercardParser;

import java.util.Map;

public class MCMessageParserImpl implements MessageParser {

    public MCMessageParserImpl() {
    }

    @Override
    public Map<String, String> parser(String originalMessage) {
        //return ISOStringMapper.mapFields(originalMessage);
        return ISOFieldMastercardParser.mapFields(originalMessage);
    }

    @Override
    public ISO20022 reMap(ISO8583 iso8583, ISO20022 iso20022) {
            switch (iso20022.getAddendumData().getAdditionalData().get(1).getValue()) {
            case "0100", "0110":
                // Remapping mensajes 0100
                break;
            case "0400":
                // Remapping mensajes 0400
                break;
            case "0800", "0810":
                AdditionalDataDTO iso8583AdditionalData = AdditionalDataDTO.builder()
                        .key("ICA")
                        .value(iso8583.getPrimaryAccountNumber())
                        .build();

                // Por el momento el ICA se guarda en additionalData hasta que se encuentre un mapping
                iso20022.getTransaction().getAdditionalData().add(iso8583AdditionalData);
                break;
            default:
                break;
        }

        return iso20022;
    }

    @Override
    public String unParser(Map<String, String> mappedFields, boolean clean) {
        return ISOStringConverterMastercard.getInstance().convertToISOString(mappedFields, clean);
    }

    @Override
    public ISO20022 unMap(ISO20022 iso20022) {
        switch (iso20022.getMessageFunction()) {
            case "0100", "0110":
                // Remapping mensajes 0100
                break;
            case "0400":
                // Remapping mensajes 0400
                break;
            case "0800", "0810":
                // Por el momento el ICA se guarda en additionalData hasta que se encuentre un mapping
                AdditionalDataDTO ica = iso20022
                        .getTransaction()
                        .getAdditionalData()
                        .stream()
                        .filter(data -> data.getKey().equals("ICA"))
                        .findFirst()
                        .orElse(AdditionalDataDTO.builder().value("").build());

                iso20022.getEnvironment().getCard().setPan(ica.getValue());
                break;
            default:
                break;
        }

        return iso20022;
    }

    @Override
    public Map<String, String> mapSubFields(Map<String, String> values) {
        //return ISO8583SubFieldsParser.mapSubFieldsMastercard(values);
        return ISOSubFieldMastercardParser.parseSubfields(values);
    }
}
