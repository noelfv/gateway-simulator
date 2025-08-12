package com.bbva.orchestrator.parser.refactor.parser;

import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.iso8583.ISO8583Builder;
import com.bbva.orchestrator.parser.refactor.parser.factory.ISO8583DelegateParser;
import com.bbva.orchestrator.parser.refactor.parser.factory.ParserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ISO8583Parser {

    private final ParserFactory parserFactory;
    private ISO8583DelegateParser messageParser;
    private Map<String, String> mapFieldValues;

    public ISO8583 build(String originalMessage) {
        //String network= GrpcHeadersInfo.getNetwork();
        String network= "peer02";
        messageParser = parserFactory.getDelegateParser(network);
        if (messageParser == null) {
            throw new IllegalStateException("No mapper available for network: " + network);
        }
        // 2. Parsear trama principal
        mapFieldValues = messageParser.parser(originalMessage);
        // 3. Mapear subcampos(esto lo vamos a hacer despues del parser)
        //Map<String, String> mapSubFieldValues = messageParser.mapSubFields(mapFieldValues);
        // 4. Construir ISO8583 (Mejorar este paso) deberia ser de con Inyenction Dependencies
        ISO8583 iso8583 = ISO8583Builder.buildISO8583(originalMessage, mapFieldValues);

        return iso8583;
    }

    public Map<String, String> mapSubFields() {
        return messageParser.mapSubFields(mapFieldValues);
    }


}