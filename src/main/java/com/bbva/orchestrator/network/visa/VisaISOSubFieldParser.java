package com.bbva.orchestrator.network.visa;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.exception.ParserLocalException;
import com.bbva.orchestrator.core.fields.VisaISOField;
import com.bbva.orchestrator.core.fields.definitions.subfields.ISOSubFieldDefinitions;
import com.bbva.orchestrator.core.parser.iso8583.handlers.impl.VisaHandlerField;
import com.bbva.orchestrator.core.parser.iso8583.strategy.subfields.CompositeFixedFieldParser;
import com.bbva.orchestrator.core.parser.iso8583.strategy.subfields.CompositeTlvFieldParser;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Clase para parsear los subcampos del Campo 48 (additionalDataRetailer) de mensajes ISO 8583 de Mastercard.
 * Esta clase procesa los subcampos de un mensaje ISO 8583 y los mapea a un mapa de valores.
 */
@Component
public class VisaISOSubFieldParser {

    private final CompositeFixedFieldParser compositeFieldParser;
    private final VisaHandlerField visaHandlerField;


    public VisaISOSubFieldParser(CompositeFixedFieldParser compositeFieldParser,VisaHandlerField visaHandlerField)
    {
        this.compositeFieldParser = compositeFieldParser;
        this.visaHandlerField = visaHandlerField;
    }

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

    public  Map<String, String> parseSubfields(ISO8583 iso8583) {
        //TODO Colocar la logica de subcampos por tipo de mensaje y de red de ser necesario
        Map<String, String> allParsedSubfields = new HashMap<>();
        if(iso8583.getMessageType().equals("0800")){ //0810,0130,0430,
            return allParsedSubfields;
        }
        //TODO: Agregar mas campos compuestos si es necesario 61, 22, 54
        Map<String, String> mapSubField03 = compositeFieldParser.buildSubFieldsSpecific("03", iso8583.getProcessingCode());
        Map<String, String> mapSubField54 = compositeFieldParser.buildSubFieldsSpecific("54", iso8583.getAdditionalAmounts());
        Map<String, String> mapSubField48 = processField48(iso8583);
        allParsedSubfields.putAll(mapSubField03);
        allParsedSubfields.putAll(mapSubField48);
        allParsedSubfields.putAll(mapSubField54);
        return allParsedSubfields;
    }



    /**
     * Parsea los subcampos de campos variables específicos (como el Campo 48)
     * a partir de un mapa de campos principales ya parseados.
     *
     * @param iso8583 Un mapa con los campos principales parseados,
     * donde el Campo 48 (si está presente) contiene su valor hexadecimal crudo.
     * @return Un mapa que contiene todos los subcampos y sub-subcampos parseados,
     * con claves como "48.01", "48.11.01", etc.
     * Si el Campo 48 no está presente, devuelve un mapa vacío.
     */
    private Map<String,String> processField48(ISO8583 iso8583){

        Map<String, String> mapField48 = new HashMap<>();//FIXME: Para optimizacion de procesamiento deberia ser un HashMap
        // 1. Obtener la data hexadecimal cruda del Campo 48
        String field48RawHex = iso8583.getAdditionalDataRetailer();

        if (field48RawHex == null || field48RawHex.isEmpty()) {
            LogsTraces.writeInfo("Campo 48 (additionalDataRetailer) no presente en la trama principal o está vacío. No se parsearán subcampos.");
            return mapField48;
        }

        try {
            // 2. Instanciar el CompositeSubFieldParser para el Campo 48
            // Le pasamos "48" para que sepa qué definiciones de subcampos buscar.
            // Ahora, el CompositeSubFieldParser se inicializa de forma segura.
            // CORRECCIÓN: Obtener las definiciones de subcampos del Campo 48 a través de la nueva clase de definiciones
            CompositeTlvFieldParser field48Parser = new CompositeTlvFieldParser("48", ISOSubFieldDefinitions.getDirectSubFieldDefinitionsForField48());

            // 3. Parsear la data hexadecimal cruda del Campo 48 usando parseToMap
            // Necesitamos pasarle la definición del campo 48 para que el parseToMap tenga acceso a ella.
            Map<String, String> parsedInternalSubfields = field48Parser.parseToMap(field48RawHex, VisaISOField.ADDITIONAL_DATA_48,visaHandlerField);

            // 4. Combinar los subcampos parseados con el mapa principal
            mapField48.putAll(parsedInternalSubfields);

        } catch (RuntimeException e) {
            throw new ParserLocalException("PGWP-00140","Error al procesar subcampos del Campo 48 :" +field48RawHex,e);
        }

        return mapField48;
    }
}