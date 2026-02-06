package com.bbva.orchestrator.core;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.builders.ISO8583Builder;
import com.bbva.orchestrator.core.commons.ContextData;
import com.bbva.orchestrator.core.mapper.factory.ISO20022DelegateMapper;
import com.bbva.orchestrator.core.mapper.factory.MapperFactory;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OrchestratorFlowProcess implements IParser {

    private final ParserFactory parserFactory;
    private final MapperFactory mapperFactory;
    private final ContextData contextData;

    @Override
    public ISO20022 convert8583to20022(String originalMessage) {
        ISO8583DelegateParser delegateParser = parserFactory.getDelegateParser();
        Map<String, String> fieldsValues = delegateParser.parser(originalMessage);
        ISO8583 iso8583 = ISO8583Builder.buildISO8583(originalMessage, fieldsValues);
        contextData.storeISO8583(iso8583, fieldsValues, delegateParser);
        Map<String, String> subFieldsValues = delegateParser.parserSubFields(iso8583);
        ISO20022DelegateMapper delegateMapper = mapperFactory.getDelegateMapper();
        return delegateMapper.mapper(iso8583, subFieldsValues);
    }

    @Override
    public String convert20022to8583(ISO20022 iso20022) {
        contextData.removeStoreId();
        // TODO: como saber si es un flujo sincrono o asincrono? Solicitar a global poder identificar un iso20022 de respuesta
        //  en este ejemplo se podria configurar en el archivo senditiveData eliminar el objeto customDataLocal
        if(isFlowAsynchronousResponse(iso20022)) {
            return extractMessageOriginal(iso20022);
        }else{
            String indicator=findValueInAdditionalInformation(iso20022,"indicator");
            //TODO: Para los mensajes de aviso se debe de colocar el siguiente indicador ASYNC
            //Se requiere la estructura de respuesta de un mensaje de aviso
            if(!isAdditionalInformationPresent(iso20022) || Objects.equals(indicator, "ASYNC")){
                return extractMessageOriginal(iso20022);
            }else{
                //TODO Este bloque solo genera la trama de respuesta para el flujo sincrono
                ISO20022DelegateMapper delegateMapper = mapperFactory.getDelegateMapper();
                Map<String, String> fieldsValues = delegateMapper.unMapper(iso20022);
                Map<String, String> fieldsValuesResponse=  ISO8583Builder.logicResponse(fieldsValues);
                ISO8583DelegateParser delegateParser = parserFactory.getDelegateParser();
                return delegateParser.unParser(fieldsValuesResponse);
            }
        }
    }

    private boolean isFlowAsynchronousResponse(ISO20022 iso20022) {
        String messageType= extractMessageType(iso20022);
        return Set.of("0110", "0130", "0410", "0430", "0210","0810").contains(messageType);
    }

    private boolean isMessageAdvices(ISO20022 iso20022) {
        String messageType= extractMessageType(iso20022);
        return Set.of("0120", "0420").contains(messageType);
    }

    private String extractMessageOriginal(ISO20022 iso20022){
        return findValueByKey(iso20022.getAddendumData(),"ISO8583_HOST");
    }

    private String extractMessageType(ISO20022 iso20022) {
        return findValueByKey(iso20022.getAddendumData(), "MSGTYPE");
    }

    private boolean isCustomDataLocalPresent(ISO20022 iso20022) {
        return iso20022.getCustomDataLocal() != null;
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

    private boolean isAdditionalInformationPresent(ISO20022 iso20022) {
        return iso20022.getProcessingResult().getAdditionalInformation() != null
                && !iso20022.getProcessingResult().getAdditionalInformation().isEmpty();
    }

    private String findValueInAdditionalInformation(ISO20022 iso20022, String key) {
        if (iso20022.getProcessingResult() == null
                || iso20022.getProcessingResult().getAdditionalInformation() == null) {
            return null;
        }
        return iso20022.getProcessingResult().getAdditionalInformation().stream()
                .filter(data -> key.equals(data.getKey()))
                .map(AdditionalInformationDTO::getValue)
                .findFirst()
                .orElse(null);
    }

    private String findValueByKey(CustomDataLocalDTO input, String key) {
        if (input == null || input.getAdditionalData() == null) {
            return null;
        }
        return input.getAdditionalData().stream()
                .filter(data -> data.getRequest() != null && key.equals(data.getRequest().getKey()))
                .map(data -> data.getRequest().getValue())
                .findFirst()
                .orElse(null);
    }

}
