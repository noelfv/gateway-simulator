package com.bbva.orchestrator.parser.refactor.parser.factory;

import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ParserFactory {

    private final Map<String, ISO8583DelegateParser> parsers;
    /**
     * Mapa que relaciona peerId con el nombre de la red correspondiente
     * Ejemplo: "peer01" → "visa", "peer02" → "mastercard"
     */
    private static final Map<String, String> PEER_TO_NETWORK = Map.of(
            "peer01", "visa",
            "peer02", "mastercard"
    );


    /**
     * Inyección automática de todos los beans que implementan ISO8583DelegateParser
     * Clave: nombre del bean (visaDelegateParser, mastercardDelegateParser, etc.)
     */
    public ParserFactory(List<ISO8583DelegateParser> parserList) {
        this.parsers = parserList.stream()
                .collect(Collectors.toMap(
                        p -> p.getClass().getSimpleName().replace("DelegateParser", "").toLowerCase(),
                        p -> p
                ));
    }

    /**
     * Obtiene el parser adecuado según el peerId (ej. peer01 → Visa)
     */
    public ISO8583DelegateParser getDelegateParser(String peerId) {
        if (peerId == null || peerId.isEmpty()) {
            return getDefaultParser();
        }

        String network = PEER_TO_NETWORK.get(peerId.toLowerCase());
        if (network == null) {
            return getDefaultParser();
        }

        return parsers.getOrDefault(network, getDefaultParser());
    }

    private ISO8583DelegateParser getDefaultParser() {
        return parsers.getOrDefault("default",
                parsers.values().stream().findFirst().orElseThrow(
                        () -> new IllegalStateException("No hay ningún ISO8583DelegateParser disponible")
                ));
    }
}
