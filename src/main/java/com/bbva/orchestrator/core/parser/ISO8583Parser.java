package com.bbva.orchestrator.core.parser;

import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.builders.ISO8583Builder;
import com.bbva.orchestrator.core.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.core.parser.factory.ParserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ISO8583Parser {

    private final ParserFactory parserFactory;

    public ISO8583 build(String originalMessage) {
        String network= GrpcHeadersInfo.getNetwork();
        ISO8583DelegateParser messageParser = parserFactory.getDelegateParser(network);
        if (messageParser == null) {
            throw new IllegalStateException("No mapper available for network: " + network);
        }
        // 2. Parsear trama principal
        Map<String, String> mapFieldValues = messageParser.parser(originalMessage);
        // 3. Mapear subcampos(esto lo vamos a hacer despues del parser)
        //Map<String, String> mapSubFieldValues = messageParser.mapSubFields(mapFieldValues);
        // 4. Construir ISO8583 (Mejorar este paso) deberia ser de con Inyenction Dependencies
        return ISO8583Builder.buildISO8583(originalMessage, mapFieldValues);
    }


}