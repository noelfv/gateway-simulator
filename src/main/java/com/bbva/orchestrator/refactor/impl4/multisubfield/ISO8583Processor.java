package com.bbva.orchestrator.refactor.impl4.multisubfield;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchestrator.refactor.impl4.*;
import com.bbva.orchestrator.refactor.impl4.subfields.CompositeSubFieldParser;
import com.bbva.orchlib.parser.ParserException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ISO8583Processor {

    private ISO8583Processor() {
        // Constructor privado para clase de utilidad
    }

    /**
     * Parsea los subcampos de una lista específica de campos variables (como el Campo 48, Campo 56)
     * a partir de un mapa de campos principales ya parseados.
     *
     * @param mainFieldsMap Un mapa con los campos principales parseados,
     * donde los campos variables (si están presentes) contienen sus valores hexadecimales crudos.
     * @param compositeFieldIdsToParse Una lista de IDs de campos principales (ej. 48, 56)
     * que se sabe que contienen subcampos y deben ser parseados.
     * @return Un mapa que contiene todos los subcampos y sub-subcampos parseados,
     * con claves como "48.01", "48.11.01", "56.01", etc.
     * Si un campo compuesto no está presente, simplemente se ignora.
     * @throws ParserException Si ocurre un error durante el parsing de algún campo compuesto.
     */
    public static Map<String, String> parseSubfields(Map<String, String> mainFieldsMap, List<Integer> compositeFieldIdsToParse) {
        Map<String, String> allParsedSubfields = new LinkedHashMap<>();

        for (Integer fieldId : compositeFieldIdsToParse) {
            ISOFieldMastercard fieldDef = ISOFieldMastercard.getById(fieldId);
            if (fieldDef == null) {
                LogsTraces.writeWarning("Campo compuesto con ID " + fieldId + " no encontrado en las definiciones de ISOFieldMastercard. Saltando.");
                continue;
            }

            // Verificar si el campo es de longitud variable y su estrategia es un LlvarLengthPrefixParser
            // que envuelve un CompositeSubFieldParser.
            if (!fieldDef.isVariable() || !(fieldDef.getParserStrategy() instanceof LlvarLengthPrefixParser)) {
                LogsTraces.writeError("El campo " + fieldDef.getIdentifier() + " no es un campo variable con prefijo de longitud o no usa LlvarLengthPrefixParser. No se puede parsear como compuesto.");
                continue;
            }

            LlvarLengthPrefixParser llvarParser = (LlvarLengthPrefixParser) fieldDef.getParserStrategy();
            FieldParserStrategy actualValueParser = llvarParser.getActualValueParser();

            if (!(actualValueParser instanceof CompositeSubFieldParser)) {
                LogsTraces.writeError("La estrategia de parser de valor real para el campo " + fieldDef.getIdentifier() + " no es un CompositeSubFieldParser. No se puede parsear como compuesto.");
                continue;
            }

            String fieldRawHex = mainFieldsMap.get(fieldDef.getName());

            if (fieldRawHex != null && !fieldRawHex.isEmpty()) {
                try {
                    CompositeSubFieldParser compositeParser = (CompositeSubFieldParser) actualValueParser;

                    // El compositeParser ya está inicializado con sus definiciones correctas
                    // a través de ISOMastercardFieldDefinitions en el bloque estático del enum.

                    Map<String, String> parsedInternalSubfields = compositeParser.parseToMap(fieldRawHex, fieldDef);
                    allParsedSubfields.putAll(parsedInternalSubfields);

                } catch (Exception e) {
                    LogsTraces.writeError("Error al parsear subcampos del Campo " + fieldDef.getIdentifier() + ": " + e.getMessage());
                    throw new ParserException("Error al procesar subcampos del Campo " + fieldDef.getIdentifier(), e);
                }
            } else {
                LogsTraces.writeInfo("Campo " + fieldDef.getIdentifier() + " (" + fieldDef.getName() + ") no presente en la trama principal o está vacío. No se parsearán subcampos.");
            }
        }
        return allParsedSubfields;
    }
}
