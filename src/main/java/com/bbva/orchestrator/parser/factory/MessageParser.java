package com.bbva.orchestrator.parser.factory;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.orchestrator.parser.iso8583.ISO8583;

import java.util.HashMap;
import java.util.Map;

public interface MessageParser {

    /**
     * Se encarga de convertir un String ISO8583 a un mapa conformado
     * por sus campos
     *
     * @param originalMessage Trama ISO8583 original
     * @return Mapa conteniendo todos los campos de la trama
     */
    Map<String, String> parser(String originalMessage);

    default Map<String, String> mapSubFields(Map<String, String> values) {
        return new HashMap<>();
    }

    default ISO20022 reMap(ISO8583 iso8583, ISO20022 iso20022) {
        return iso20022;
    }

    /**
     * Convierte un mapa de campos a un String ISO8583
     *
     * @param mappedFields Mapa con todos los valores del ISO8583
     * @return Una trama ISO8583 con el bitmap recreado
     */
    String unParser(Map<String, String> mappedFields, boolean clean);

    default ISO20022 unMap(ISO20022 iso20022) {
        return iso20022;
    }
}
