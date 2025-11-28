package com.bbva.orchestrator.core.parser.iso8583.strategy.subfields;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchestrator.core.fields.definitions.IFieldDefinition;
import com.bbva.orchestrator.core.parser.iso8583.ParsedFieldResult;
import com.bbva.orchestrator.core.parser.iso8583.strategy.FieldParserStrategy;
import com.bbva.orchestrator.core.utils.ISOUtil;
import com.bbva.orchestrator.core.fields.definitions.subfields.ISOSubFieldDefinitions;
import com.bbva.orchestrator.core.fields.definitions.subfields.tlv.Field48;
import com.bbva.orchestrator.core.parser.iso8583.handlers.NetworkHandlerField;

import java.util.LinkedHashMap;
import java.util.Map;


public class CompositeTlvFieldParser implements FieldParserStrategy {

    private static final int TAG_LENGTH_HEX = 4;        // 2 bytes → 4 caracteres hex
    private static final int LENGTH_PREFIX_HEX = 4;     // 2 bytes → 4 caracteres hex
    private final String compositeFieldId;
    private final Map<String, Field48> subFieldDefinitions;

    public CompositeTlvFieldParser(String compositeFieldId) {
        this.compositeFieldId = compositeFieldId;
        this.subFieldDefinitions = ISOSubFieldDefinitions.getSubFieldDefinitionsForComposite(compositeFieldId);
        if (this.subFieldDefinitions == null || this.subFieldDefinitions.isEmpty()) {
            LogsTraces.writeWarning("CompositeSubFieldParser inicializado con definiciones de subcampo vacías o nulas para: " + compositeFieldId);
        }
    }

    public CompositeTlvFieldParser(String compositeFieldId, Map<String, Field48> subFieldDefinitions) {
        this.compositeFieldId = compositeFieldId;
        this.subFieldDefinitions = subFieldDefinitions;
        if (this.subFieldDefinitions == null || this.subFieldDefinitions.isEmpty()) {
            LogsTraces.writeWarning("CompositeSubFieldParser inicializado con definiciones de subcampo vacías o nulas para: " + compositeFieldId);
        }
    }


    public ParsedFieldResult parsexxx(String rawDataSegment, IFieldDefinition fieldDefinition, NetworkHandlerField networkProfile) {
        Map<String, String> parsedSubFieldsMap = parseToMap(rawDataSegment, fieldDefinition,networkProfile);

        // Convertir el mapa a JSON string
        StringBuilder resultBuilder = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, String> entry : parsedSubFieldsMap.entrySet()) {
            if (!first) resultBuilder.append(", ");
            resultBuilder.append("\"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\"");
            first = false;
        }
        resultBuilder.append("}");

        // Devolver el JSON como String + longitud consumida
        return new ParsedFieldResult(resultBuilder.toString(), rawDataSegment.length());
    }

    @Override
    public String build(String fieldValue, IFieldDefinition fieldDefinition, NetworkHandlerField networkProfile) {
        return "";
    }

    @Override
    public ParsedFieldResult parse(String rawDataSegment, IFieldDefinition fieldDefinition, NetworkHandlerField networkProfile) {
        Map<String, String> parsedSubFieldsMap = parseToMap(rawDataSegment, fieldDefinition,networkProfile);

        // Convertir el mapa a JSON string
        StringBuilder resultBuilder = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, String> entry : parsedSubFieldsMap.entrySet()) {
            if (!first) resultBuilder.append(", ");
            resultBuilder.append("\"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\"");
            first = false;
        }
        resultBuilder.append("}");

        // Devolver el JSON como String + longitud consumida
        return new ParsedFieldResult(resultBuilder.toString(), rawDataSegment.length());
    }

    /**
     * Parsea la trama y devuelve un mapa de subcampos (clave: "48.33.01", valor: "valor_decodificado")
     */
    public Map<String, String> parseToMap(String rawDataSegment, IFieldDefinition fieldDefinition, NetworkHandlerField networkHandlerField) {
        Map<String, String> parsedSubFieldsMap = new LinkedHashMap<>();
        int currentOffset = 0;

        // Caso especial: subcampo fijo 48.01
        if ("48".equals(compositeFieldId)) {
            Field48 sf01Def = subFieldDefinitions.get("01");
            if (sf01Def != null && !sf01Def.isVariable() && sf01Def.getLength() > 0) {
                int lenHex = sf01Def.getLength() * 2;
                if (currentOffset + lenHex <= rawDataSegment.length()) {
                    String valueHex = rawDataSegment.substring(currentOffset, currentOffset + lenHex);
                    ParsedFieldResult result = sf01Def.getParserStrategy().parse(valueHex, sf01Def,networkHandlerField);
                    parsedSubFieldsMap.put(compositeFieldId+".01", result.value());
                    currentOffset += lenHex;
                }
            }
        }

        // Parsear el resto como TLV: Tag (2B) + Length (2B) + Value
        while (currentOffset < rawDataSegment.length()) {
            // === 1. Leer Tag (4 hex chars) ===
            if (currentOffset + TAG_LENGTH_HEX > rawDataSegment.length()) break;
            String tagHex = rawDataSegment.substring(currentOffset, currentOffset + TAG_LENGTH_HEX);
            String tag = ISOUtil.ebcdicToString(tagHex).trim();
            currentOffset += TAG_LENGTH_HEX;

            // === 2. Leer Longitud (4 hex chars, EBCDIC) ===
            if (currentOffset + LENGTH_PREFIX_HEX > rawDataSegment.length()) {
                throw new ParserException("Falta longitud para subcampo " + compositeFieldId + "." + tag);
            }
            String lenHex = rawDataSegment.substring(currentOffset, currentOffset + LENGTH_PREFIX_HEX);
            int valueLengthInBytes = Integer.parseInt(ISOUtil.ebcdicToString(lenHex));
            int valueLengthInHex = valueLengthInBytes * 2;
            currentOffset += LENGTH_PREFIX_HEX;

            // === 3. Validar que hay suficientes datos ===
            if (currentOffset + valueLengthInHex > rawDataSegment.length()) {
                throw new ParserException("Datos insuficientes para el valor del subcampo " + compositeFieldId + "." + tag);
            }
            String valueHex = rawDataSegment.substring(currentOffset, currentOffset + valueLengthInHex);
            currentOffset += valueLengthInHex;

            // === 4. Buscar definición del subcampo ===
            Field48 subFieldDef = subFieldDefinitions.get(tag);
            if (subFieldDef == null) {
                LogsTraces.writeWarning("Subcampo no definido: " + compositeFieldId + "." + tag);
                continue;
            }

            // === 5. Si es variable (TLV anidado), parsear recursivamente y agregar al mapa ===
            if (subFieldDef.isVariable()) {
                parseNestedVariableSubField(tag, valueHex, parsedSubFieldsMap);
            } else {
                // === 6. Si es fijo, delegar al parserStrategy ===
                //ParsedFieldResult result = subFieldDef.getParserStrategy().parse(valueHex, valueLengthInHex, subFieldDef);
                //ParsedFieldResult result = subFieldDef.getParserStrategy().parse(valueHex, subFieldDef,networkProfile);
                String result=networkHandlerField.decode(valueHex,subFieldDef.getTypeData());
                //parsedSubFieldsMap.put(compositeFieldId + "." + tag, result.value());
                parsedSubFieldsMap.put(compositeFieldId + "." + tag, result);
            }
        }

        return parsedSubFieldsMap;
    }

    /**
     * Maneja subcampos variables (TLV anidado) como 48.33
     * Agrega los resultados directamente al mapa pasado como parámetro.
     */
    private void parseNestedVariableSubField(String parentTag, String valueHex, Map<String, String> resultContainer) {
        int offset = 0;

        while (offset < valueHex.length()) {
            // === 1. Leer Tag del sub-subcampo (4 hex chars) ===
            if (offset + TAG_LENGTH_HEX > valueHex.length()) break;
            String subTagHex = valueHex.substring(offset, offset + TAG_LENGTH_HEX);
            String subTag = ISOUtil.ebcdicToString(subTagHex).trim();
            offset += TAG_LENGTH_HEX;

            // === 2. Leer Longitud (4 hex chars, EBCDIC) ===
            if (offset + LENGTH_PREFIX_HEX > valueHex.length()) {
                throw new ParserException("Falta longitud para sub-subcampo " + parentTag + "." + subTag);
            }
            String lenHex = valueHex.substring(offset, offset + LENGTH_PREFIX_HEX);
            int valueLengthInBytes = Integer.parseInt(ISOUtil.ebcdicToString(lenHex));
            int valueLengthInHex = valueLengthInBytes * 2;
            offset += LENGTH_PREFIX_HEX;

            // === 3. Leer Valor ===
            if (offset + valueLengthInHex > valueHex.length()) {
                throw new ParserException("Datos insuficientes para el valor del sub-subcampo " + parentTag + "." + subTag);
            }
            String subValueHex = valueHex.substring(offset, offset + valueLengthInHex);
            offset += valueLengthInHex;

            // === 4. Decodificar valor (EBCDIC) ===
            String decodedValue = ISOUtil.ebcdicToString(subValueHex);

            // === 5. Guardar resultado en el contenedor (ej: "48.33.01") ===
            String fullKey = compositeFieldId + "." + parentTag + "." + subTag;
            resultContainer.put(fullKey, decodedValue.trim());
        }
    }
}