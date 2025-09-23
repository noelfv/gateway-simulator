package com.bbva.orchestrator.core.parser.factory.impl;

import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.utils.ParserUtil;
import com.bbva.orchestrator.core.network.mastercard.MastercardProcessField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class MastercardDelegateParser implements ISO8583DelegateParser {

    private static final String NETWORK_MASTERCARD = "PEER02";
    private final MastercardProcessField mastercardProcessField;

    @Override
    public Map<String, String> parser(String originalMessage) {
        Map<String, String> mappedFields=mastercardProcessField.mapFields(originalMessage);
        mappedFields.put("networkName",NETWORK_MASTERCARD);
        mappedFields.put("plainTextPCI",unParserPlainTextPCI(mappedFields));
        return mappedFields;
    }

    @Override
    public String unParser(Map<String, String> mappedFields) {
        return mastercardProcessField.unMapFields(mappedFields);
    }

    @Override
    public String unParserPlainText(Map<String, String> mappedFields) {
        return mastercardProcessField.unMapFieldsPlainText(mappedFields);
    }

    private String unParserPlainTextPCI(Map<String, String> mapFieldsValue) {

        //TODO : Colocar una variable de entorno para definir el flujo de masking
        //if (ISOUtil.getEnvVariableOrDefault("MASK_SENSITIVE_DATA") != null) {
        //      return mapFieldsValue.get("messageType");
        if (mapFieldsValue.get("messageType").startsWith("08") || mapFieldsValue.get("messageType").startsWith("019")) {
            return mapFieldsValue.get("messageType");
        }
        try{
            Map<String, String> maskedFields = ParserUtil.maskSensitiveFields(mapFieldsValue);
            return mastercardProcessField.unMapFieldsPlainText(maskedFields);
        }catch (Exception e){
            return mapFieldsValue.get("messageType");
        }

    }

}
