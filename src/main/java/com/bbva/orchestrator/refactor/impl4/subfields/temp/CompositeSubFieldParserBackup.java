package com.bbva.orchestrator.refactor.impl4.subfields.temp;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchestrator.refactor.impl4.FieldParserStrategy;
import com.bbva.orchestrator.refactor.impl4.commons.IFieldDefinition;
import com.bbva.orchestrator.refactor.impl4.commons.ISOUtil;
import com.bbva.orchestrator.refactor.impl4.commons.ParsedFieldResult;
import com.bbva.orchestrator.refactor.impl4.subfields.ISOMastercardFieldDefinitions;
import com.bbva.orchestrator.refactor.impl4.subfields.ISOSubField48Mastercard;
import com.bbva.orchlib.parser.ParserException;

import java.util.LinkedHashMap;
import java.util.Map;

public class CompositeSubFieldParserBackup implements FieldParserStrategy {

    private final Map<String, ISOSubField48Mastercard> subFieldDefinitions;
    private final String compositeFieldId;

    // Constructor existente (para uso cuando las definiciones se obtienen a demanda, ej. en ISO8583Processor)
    // Este constructor ya no será llamado directamente por el enum.
    public CompositeSubFieldParserBackup(String compositeFieldId) {
        this.compositeFieldId = compositeFieldId;
        // Se inicializará a través del método estático de ISOMastercardFieldDefinitions
        this.subFieldDefinitions = ISOMastercardFieldDefinitions.getSubFieldDefinitionsForComposite(compositeFieldId);
        if (this.subFieldDefinitions == null || this.subFieldDefinitions.isEmpty()) {
            LogsTraces.writeWarning("CompositeSubFieldParser inicializado con definiciones de subcampo vacías o nulas para: " + compositeFieldId);
        }
    }

    // Constructor para uso cuando las definiciones se pasan directamente (ej. desde ISOMastercardSubField enum)
    // Este constructor será llamado por el LlvarLengthPrefixParser en el enum.
    public CompositeSubFieldParserBackup(String compositeFieldId, Map<String, ISOSubField48Mastercard> subFieldDefinitions) {
        this.compositeFieldId = compositeFieldId;
        this.subFieldDefinitions = subFieldDefinitions;
        if (this.subFieldDefinitions == null || this.subFieldDefinitions.isEmpty()) {
            LogsTraces.writeWarning("CompositeSubFieldParser inicializado con definiciones de subcampo vacías o nulas para: " + compositeFieldId);
        }
    }


    /**
     * Parsea la data hexadecimal de un campo compuesto en un Map de subcampos.
     * Este es el método principal para la lógica de parsing de subcampos.
     *
     * @param rawDataSegment La porción de la trama HEX que contiene el campo compuesto.
     * @param fieldDefinition La definición del campo ISO (usado para el ID del campo compuesto).
     * @return Un Map donde la clave es el ID completo del subcampo (ej. "48.01", "48.11.01") y el valor es el contenido decodificado.
     * @throws ParserException Si la trama es demasiado corta o el formato es inválido.
     */
    public Map<String, String> parseToMap(String rawDataSegment, IFieldDefinition fieldDefinition) { // Tipo cambiado a IFieldDefinition
        // Asegurarse de que subFieldDefinitions no sea null antes de usarlo
        if (this.subFieldDefinitions == null || this.subFieldDefinitions.isEmpty()) {
            throw new ParserException("CompositeSubFieldParser no inicializado con definiciones de subcampo para: " + compositeFieldId);
        }

        Map<String, String> parsedSubFieldsMap = new LinkedHashMap<>();
        int currentOffset = 0;

        // Lógica para el Campo 48 principal (si compositeFieldId es "48")
        // Asumimos que el primer subcampo 48.01 es fijo y siempre presente en la especificación
        // y que los demás subcampos directos del 48 son TLV.
        if ("48".equals(compositeFieldId)) {
            ISOSubField48Mastercard sf01Def = this.subFieldDefinitions.get("01");
            if (sf01Def != null) {
                int sf01HexLength = sf01Def.getLength() * 2;
                if (rawDataSegment.length() < currentOffset + sf01HexLength) {
                    LogsTraces.writeWarning("Trama demasiado corta para subcampo 48.01. Data: " + rawDataSegment);
                } else {
                    String sf01Hex = rawDataSegment.substring(currentOffset, currentOffset + sf01HexLength);
                    ParsedFieldResult sf01Result = sf01Def.getParserStrategy().parse(sf01Hex, sf01HexLength, sf01Def);
                    parsedSubFieldsMap.put(fieldDefinition.getIdentifier() + ".01", sf01Result.value());
                    currentOffset += sf01Result.consumedLengthInChars();
                }
            } else {
                LogsTraces.writeWarning("Definición para subcampo 48.01 no encontrada en el mapa de definiciones.");
            }
        }

        // Iterar sobre los subcampos restantes (asumiendo formato Tag(2 bytes) + Length(2 bytes) + Value)
        while (rawDataSegment.length() > currentOffset) {
            // 1. Leer Subfield Tag (2 bytes = 4 hex chars)
            int subFieldTagHexLength = 2 * 2; // 2 bytes para el Tag (ej. "75")
            if (rawDataSegment.length() < currentOffset + subFieldTagHexLength) {
                LogsTraces.writeWarning("Data restante insuficiente para leer el Tag del subcampo en " + compositeFieldId + ". Data: " + rawDataSegment.substring(currentOffset));
                break;
            }
            String subFieldTagHex = rawDataSegment.substring(currentOffset, currentOffset + subFieldTagHexLength);
            String subFieldTagId = ISOUtil.ebcdicToString(subFieldTagHex); // Decodifica el Tag (ej. "75")
            currentOffset += subFieldTagHexLength;

            ISOSubField48Mastercard subFieldDef = subFieldDefinitions.get(subFieldTagId);
            if (subFieldDef == null) {
                LogsTraces.writeWarning("Subcampo " + compositeFieldId + "." + subFieldTagId + " no definido. Saltando o error.");
                throw new ParserException("Subcampo " + compositeFieldId + "." + subFieldTagId + " no definido en ISOMastercardSubField.");
            }

            // 2. Leer Subfield Value Length Prefix (2 bytes = 4 hex chars, para LL)
            int subFieldValueLlPrefixHexLength = 2 * 2; // 2 bytes para la longitud (ej. "26")
            if (rawDataSegment.length() < currentOffset + subFieldValueLlPrefixHexLength) {
                throw new ParserException("Trama demasiado corta para leer el prefijo de longitud del valor del subcampo " + compositeFieldId + "." + subFieldTagId);
            }
            String subFieldValueLlPrefixHex = rawDataSegment.substring(currentOffset, currentOffset + subFieldValueLlPrefixHexLength);
            int actualValueLengthInBytes = Integer.parseInt(ISOUtil.ebcdicToString(subFieldValueLlPrefixHex)); // Decodifica la longitud (ej. "26")
            currentOffset += subFieldValueLlPrefixHexLength;

            // 3. Leer Subfield Value Data
            int subFieldValueHexLength = actualValueLengthInBytes * 2; // Longitud del valor en caracteres HEX
            if (rawDataSegment.length() < currentOffset + subFieldValueHexLength) {
                throw new ParserException("Trama demasiado corta para el valor del subcampo " + compositeFieldId + "." + subFieldTagId + ". Esperado: " + subFieldValueHexLength + ", Disponible: " + (rawDataSegment.length() - currentOffset));
            }
            String subFieldValueHex = rawDataSegment.substring(currentOffset, currentOffset + subFieldValueHexLength);

            // Ahora, delegar el parsing del VALOR a la estrategia del subFieldDef.
            // La estrategia espera la data HEX cruda del valor y su longitud HEX.
            //ParsedFieldResult subFieldResult = subFieldDef.getParserStrategy().parse(subFieldValueHex, subFieldValueHexLength, subFieldDef);
            ParsedFieldResult subFieldResult = subFieldDef.getParserStrategy().parse(subFieldValueHex, actualValueLengthInBytes, subFieldDef);

            parsedSubFieldsMap.put(compositeFieldId + "." + subFieldTagId, subFieldResult.value());

            currentOffset += subFieldResult.consumedLengthInChars();
            // currentOffset ya fue avanzado por el Tag y el Prefijo de Longitud.
            // No necesitamos añadir subFieldResult.getConsumedLengthInChars() aquí,
            // ya que el subFieldResult.getConsumedLengthInChars() es solo para el valor.
            // currentOffset ya avanzó por el Tag y el Prefijo, y ahora por el Valor.
            // El avance total ya está cubierto por la lógica del bucle.
        }
        return parsedSubFieldsMap;
    }

    @Override
    public ParsedFieldResult parse(String rawDataSegment, int expectedHexLength, IFieldDefinition fieldDefinition) {
        Map<String, String> parsedSubFieldsMap = parseToMap(rawDataSegment, fieldDefinition);

        StringBuilder resultBuilder = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, String> entry : parsedSubFieldsMap.entrySet()) {
            if (!first) resultBuilder.append(", ");
            resultBuilder.append("\"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\"");
            first = false;
        }
        resultBuilder.append("}");

        return new ParsedFieldResult(resultBuilder.toString(), expectedHexLength);
    }

    @Override
    public String build(String fieldValue, IFieldDefinition fieldDefinition) {
        LogsTraces.writeWarning("Construcción de campo compuesto (Field " + fieldDefinition.getIdentifier() + ") no implementada completamente.");
        return "";
    }
}
