package com.bbva.orchestrator.core;

import com.bbva.gateway.dto.iso20022.AddendumDataDTO;
import com.bbva.gateway.dto.iso20022.AdditionalDataDTO;
import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.gateway.dto.iso20022.ProcessingResultDTO;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.core.builders.ISO8583Builder;
import com.bbva.orchestrator.core.dto.ISO8583;
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
import java.util.Set;

@Component
@RequiredArgsConstructor
public class OrchestratorFlowProcess implements IParser {

    private final ParserFactory parserFactory;
    private final MapperFactory mapperFactory;
    private final FieldLogicFactory fieldLogicFactory;

    @Override
    public ISO20022 convert8583to20022(String originalMessage) {
        ISO8583DelegateParser delegateParser = parserFactory.getDelegateParser(GrpcHeadersInfo.getNetwork());
        Map<String, String> fieldsValues = delegateParser.parser(originalMessage);
        ISO8583 iso8583 = ISO8583Builder.buildISO8583(originalMessage, fieldsValues);
        NetworkDelegateFieldLogic delegateFieldLogic = fieldLogicFactory.getDelegateFieldLogic(iso8583.getNetworkName());
        Map<String, String> subFieldsValues = delegateFieldLogic.parseSubfields(iso8583);
        ISO20022DelegateMapper delegateMapper = mapperFactory.getDelegateMapper(iso8583.getNetworkName());

        return delegateMapper.mapper(iso8583, subFieldsValues);
    }

    @Override
    public String convert20022to8583(ISO20022 iso20022) {

        // TODO: como saber si es un flujo sincrono o asincrono? Solicitar a global poder identificar un iso20022 de respuesta de PAP
        String typeMessage= extractMessageType(iso20022);
        if(!requiredUnparser(typeMessage)) {
            return extractMessageOriginal(iso20022);
        }else{
            //TODO: Aca entra los mensajes 0100,0101,0200,0400,0401 y solo los que tengan el ProcessingResult nulo o vacio , se
            // ProcessingResult == (nulo o vacio) son operaciones que irian a HOST ademas considerar el label que pueda colocar crypto
            // ProcessingResult != (nulo o vacio) operaciones que han sido procesadas por PAP y se debe generar la trama de respuesta
            if(isProcessingResultNullOrEmpty(iso20022)){
                return extractMessageOriginal(iso20022);
            }else{
                //TODO Este bloque solo genera la trama de respuesta para el flujo sincrono(PAP)
                ISO20022DelegateMapper delegateMapper = mapperFactory.getDelegateMapper(iso20022.getNetworkName());
                Map<String, String> fieldsValues = delegateMapper.unMapper(iso20022);
                NetworkDelegateFieldLogic delegateFieldLogic = fieldLogicFactory.getDelegateFieldLogic(iso20022.getNetworkName());
                Map<String, String> fieldsValuesResponse = delegateFieldLogic.applyLogicFields(fieldsValues);
                ISO8583DelegateParser delegateParser = parserFactory.getDelegateParser(iso20022.getNetworkName());

                return delegateParser.unParser(fieldsValuesResponse);
            }
        }
    }

    private boolean requiredUnparser(String messageType) {
        //TODO los mensajes de aviso se les incluye hasta que nos brinden una propuesta de solucion respecto al indicador de PAP
        return Set.of("0100","0101","0400","0401").contains(messageType);
    }

    private String extractMessageOriginal(ISO20022 iso20022){
        return findValueByKey(iso20022.getAddendumData(),"ISO8583_HOST");
    }

    private String extractMessageType(ISO20022 iso20022) {
        return findValueByKey(iso20022.getAddendumData(), "UNSP");
    }

    private boolean isProcessingResultNullOrEmpty(ISO20022 iso20022) {
        ProcessingResultDTO processingResult = iso20022.getProcessingResult();
        if (processingResult == null) {
            return true;
        }
        return processingResult.getResultData() == null;
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
