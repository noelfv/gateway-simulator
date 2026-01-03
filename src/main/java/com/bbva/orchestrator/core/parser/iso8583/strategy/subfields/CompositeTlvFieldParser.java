package com.bbva.orchestrator.core.parser.iso8583.strategy.subfields;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchestrator.core.fields.definitions.IFieldDefinition;
import com.bbva.orchestrator.core.fields.definitions.subfields.tlv.Field48;
import com.bbva.orchestrator.core.fields.definitions.subfields.tlv.TLVFieldLoadStructure;
import com.bbva.orchestrator.core.parser.iso8583.ParsedFieldResult;
import com.bbva.orchestrator.core.parser.iso8583.handlers.NetworkHandlerField;
import com.bbva.orchestrator.core.parser.iso8583.strategy.FieldParserStrategy;
import com.bbva.orchestrator.core.utils.ISOUtil;
import com.bbva.orchlib.parser.ParserException;

import java.util.LinkedHashMap;
import java.util.Map;

public class CompositeTlvFieldParser implements FieldParserStrategy {

    private static final int TAG_LENGTH_HEX = 4;        // 2 bytes → 4 caracteres hex
    private static final int LENGTH_PREFIX_HEX = 4;     // 2 bytes → 4 caracteres hex
    private final String compositeFieldId;
    private final Map<String, Field48> subFieldDefinitions;

    public CompositeTlvFieldParser(String compositeFieldId) {
        this.compositeFieldId = compositeFieldId;
        this.subFieldDefinitions = TLVFieldLoadStructure.getSubFieldDefinitionsForComposite(compositeFieldId);
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
            Map<String, Field48> subSubFields = TLVFieldLoadStructure.getSubFieldDefinitionsForComposite(tag);

            // === 5. Verificamos si es un campo con hijos (Complex/Composite) ===
            if (!subSubFields.isEmpty()) {

                // TRUCO: Miramos la definición del PRIMER hijo para decidir la estrategia del grupo.
                // Si el primer hijo (ej: 61.01) es FIJO (false), asumimos que el bloque es posicional.
                // Si el primer hijo (ej: 33.01) es VARIABLE (true), asumimos que el bloque es TLV anidado.
                Field48 firstChild = subSubFields.values().iterator().next();

                if (firstChild.isVariable()) {
                    // Caso TLV Anidado (Busca Tags internos)
                    parseNestedVariableSubField(tag, valueHex, parsedSubFieldsMap);
                } else {
                    // Caso Posicional Concatenado (Corta por longitud fija definida)
                    parseNestedFixedSubField(tag, valueHex, parsedSubFieldsMap, networkHandlerField, subSubFields);
                }

            } else {
                // === 6. Si es fijo simple (Hoja), decodificamos directamente ===
                String result = networkHandlerField.decode(valueHex, subFieldDef.getTypeData());
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

    /**
     * Maneja subcampos que son concatenaciones fijas (Ej: 48.61).
     * Trama: [Dato61.01][Dato61.02][Dato61.03]... (Sin tags ni longitudes intermedias).
     */
    private void parseNestedFixedSubField(String parentTag, String valueHex, Map<String, String> resultContainer,
                                          NetworkHandlerField networkHandlerField, Map<String, Field48> subSubFields) {

        if (subSubFields == null || subSubFields.isEmpty()) {
            LogsTraces.writeWarning("No hay definiciones para hijos de: " + parentTag);
            return;
        }

        int offset = 0;

        // 2. Iterar en orden (01, 02, 03...)
        for (Map.Entry<String, Field48> entry : subSubFields.entrySet()) {
            String subTag = entry.getKey();      // ej: "01"
            Field48 subDef = entry.getValue();   // ej: SF_48_61_01

            // 3. Calcular tamaño a cortar BASADO EN LA DEFINICIÓN
            // Como subDef.getLength() retorna bytes, multiplicamos por 2 para Hex.
            int lengthToCut = subDef.getLength() * 2;

            // 4. Validar que no nos pasemos del final de la cadena
            if (offset + lengthToCut > valueHex.length()) {
                LogsTraces.writeWarning("Datos insuficientes en campo " + parentTag + " para leer subcampo " + subTag + ". Se requiere: " + lengthToCut + ", Queda: " + (valueHex.length() - offset));
                break;
            }

            // 5. Cortar (Substring puro)
            String subValueHex = valueHex.substring(offset, offset + lengthToCut);

            // 6. Decodificar (Hex -> ASCII/EBCDIC)
            String decodedValue = networkHandlerField.decode(subValueHex, subDef.getTypeData());

            // 7. Guardar resultado
            // Clave resultante: "48.61.01"
            String fullKey = compositeFieldId + "." + parentTag + "." + subTag;
            resultContainer.put(fullKey, decodedValue);

            // 8. Avanzar el cursor para el siguiente hijo
            offset += lengthToCut;
        }
    }
}