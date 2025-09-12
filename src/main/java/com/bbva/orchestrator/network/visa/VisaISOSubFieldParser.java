package com.bbva.orchestrator.network.visa;

import com.bbva.orchestrator.core.dto.ISO8583;

import com.bbva.orchestrator.network.ISOSubFieldParser;
import com.bbva.orchestrator.network.common.DefaultISOSubFieldParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

/**
 * Clase para parsear los subcampos del Campo 48 (additionalDataRetailer) de mensajes ISO 8583 de Mastercard.
 * Esta clase procesa los subcampos de un mensaje ISO 8583 y los mapea a un mapa de valores.
 */
@Component
@RequiredArgsConstructor
public class VisaISOSubFieldParser implements ISOSubFieldParser {

    private final DefaultISOSubFieldParser defaultISOSubFieldParser;

    /**
     * Parsea los subcampos de campos variables específicos (como el Campo 48)
     * a partir de un mapa de campos principales ya parseados.
     *
     * @param iso8583 objeto iso8583 con los campos principales parseados,
     * donde el Campo 48 (si está presente) contiene su valor hexadecimal crudo.
     * @return Un mapa que contiene todos los subcampos y sub-subcampos parseados,
     * con claves como "48.01", "48.11.01", etc.
     * Si el Campo 48 no está presente, devuelve un mapa vacío.
     */
    @Override
    public  Map<String, String> parseSubfields(ISO8583 iso8583) {
        Map<String, String> allParsedSubfields = new HashMap<>();

        if(iso8583.getMessageType().equals("0800")){ //0810,0130,0430,
            return allParsedSubfields;
        }
        return defaultISOSubFieldParser.parseSubfields(iso8583);
    }

}