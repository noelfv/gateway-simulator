package com.bbva.orchestrator.refactor.impl4.subfields;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchestrator.refactor.impl4.ISOFieldMastercard;
import com.bbva.orchlib.parser.ParserException;

import java.util.LinkedHashMap;
import java.util.Map;

public class ISO8583Processor {

    private ISO8583Processor() {
        // Constructor privado para clase de utilidad
    }

    /**
     * Parsea los subcampos de campos variables específicos (como el Campo 48)
     * a partir de un mapa de campos principales ya parseados.
     *
     * @param mainFieldsMap Un mapa con los campos principales parseados,
     * donde el Campo 48 (si está presente) contiene su valor hexadecimal crudo.
     * @return Un mapa que contiene todos los subcampos y sub-subcampos parseados,
     * con claves como "48.01", "48.11.01", etc.
     * Si el Campo 48 no está presente, devuelve un mapa vacío.
     */
    public static Map<String, String> parseSubfields(Map<String, String> mainFieldsMap) {
        Map<String, String> allParsedSubfields = new LinkedHashMap<>();

        // 1. Obtener la data hexadecimal cruda del Campo 48
        String field48RawHex = mainFieldsMap.get(ISOFieldMastercard.ADDITIONAL_DATA_48.getName());

        if (field48RawHex == null || field48RawHex.isEmpty()) {
            LogsTraces.writeInfo("Campo 48 (additionalDataRetailer) no presente en la trama principal o está vacío. No se parsearán subcampos.");
            return allParsedSubfields;
        }

        try {
            // 2. Instanciar el CompositeSubFieldParser para el Campo 48
            // Le pasamos "48" para que sepa qué definiciones de subcampos buscar.
            // Ahora, el CompositeSubFieldParser se inicializa de forma segura.
            // CORRECCIÓN: Obtener las definiciones de subcampos del Campo 48 a través de la nueva clase de definiciones
            CompositeSubFieldParser field48Parser = new CompositeSubFieldParser("48", ISOMastercardFieldDefinitions.getDirectSubFieldDefinitionsForField48());

            // 3. Parsear la data hexadecimal cruda del Campo 48 usando parseToMap
            // Necesitamos pasarle la definición del campo 48 para que el parseToMap tenga acceso a ella.
            Map<String, String> parsedInternalSubfields = field48Parser.parseToMap(field48RawHex, ISOFieldMastercard.ADDITIONAL_DATA_48);

            // 4. Combinar los subcampos parseados con el mapa principal
            allParsedSubfields.putAll(parsedInternalSubfields);

        } catch (Exception e) {
            LogsTraces.writeError("Error al parsear subcampos del Campo 48: " + e.getMessage());
            throw new ParserException("Error al procesar subcampos del Campo 48", e);
        }

        return allParsedSubfields;
    }
}