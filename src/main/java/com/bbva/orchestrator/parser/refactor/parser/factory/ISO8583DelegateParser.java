package com.bbva.orchestrator.parser.refactor.parser.factory;

import java.util.HashMap;
import java.util.Map;

public interface ISO8583DelegateParser {

    /**
     * Se encarga de convertir un String ISO8583 a un mapa conformado
     * por sus campos
     *
     * @param originalMessage Trama ISO8583 original
     * @return Mapa conteniendo todos los campos de la trama
     */
    Map<String, String> parser(String originalMessage);

    /**
     * Convierte un mapa de campos a un String ISO8583
     *
     * @param mappedFields Mapa con todos los valores del ISO8583
     * @return Una trama ISO8583 con el bitmap recreado
     */
    String unParser(Map<String, String> mappedFields);

    //Validar este punto en que casos requiere un mapa de subcampos para los 0800 no es necesario
    default Map<String, String> mapSubFields(Map<String, String> values) {
        return new HashMap<>();
    }



}
