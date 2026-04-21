package com.bbva.orchestrator.core.parser.iso8583.strategy.subfields;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchestrator.core.fields.definitions.IFieldDefinition;
import com.bbva.orchestrator.core.fields.definitions.subfields.tlv.Field104;
import com.bbva.orchestrator.core.fields.definitions.subfields.tlv.TLVField104LoadStructure;
import com.bbva.orchestrator.core.parser.iso8583.ParsedFieldResult;
import com.bbva.orchestrator.core.parser.iso8583.handlers.NetworkHandlerField;
import com.bbva.orchestrator.core.parser.iso8583.strategy.FieldParserStrategy;

import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("all")
public class CompositeTlvField104Parser implements FieldParserStrategy {
    private static final int TAG_LENGTH_HEX = 2;        // 2 bytes → 4 caracteres hex
    private static final int LENGTH_PREFIX_HEX = 4;     // 2 bytes → 4 caracteres hex
    private static final int DATASET_ID_LENGTH_HEX = 2; // 1 byte → 2 caracteres hex

    private final String compositeFieldId;
    private final Map<String, Field104> datasetDefinitions;

    public CompositeTlvField104Parser(String compositeFieldId) {
        this.compositeFieldId = compositeFieldId;
        this.datasetDefinitions = TLVField104LoadStructure.getDirectSubFieldDefinitionsForField104();
        if (this.datasetDefinitions == null || this.datasetDefinitions.isEmpty()) {
            LogsTraces.writeWarning("CompositeTlvField104Parser inicializado con definiciones vacías para: " + compositeFieldId);
        }
    }

    public CompositeTlvField104Parser(String compositeFieldId, Map<String, Field104> datasetDefinitions) {
        this.compositeFieldId = compositeFieldId;
        this.datasetDefinitions = datasetDefinitions;
        if (this.datasetDefinitions == null || this.datasetDefinitions.isEmpty()) {
            LogsTraces.writeWarning("CompositeTlvField104Parser inicializado con definiciones vacías para: " + compositeFieldId);
        }
    }

    @Override
    public String build(String fieldValue, IFieldDefinition fieldDefinition, NetworkHandlerField networkProfile) {
        return "";
    }

    @Override
    public ParsedFieldResult parse(String rawDataSegment, IFieldDefinition fieldDefinition, NetworkHandlerField networkProfile) {
        Map<String, String> parsedSubFieldsMap = parseToMap(rawDataSegment, networkProfile);

        // Convertir el mapa a JSON string
        StringBuilder resultBuilder = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, String> entry : parsedSubFieldsMap.entrySet()) {
            if (!first) resultBuilder.append(", ");
            resultBuilder.append("\"").append(entry.getKey()).append("\": \"").append(entry.getValue()).append("\"");
            first = false;
        }
        resultBuilder.append("}");

        return new ParsedFieldResult(resultBuilder.toString(), rawDataSegment.length());
    }

    public Map<String, String> parseToMap(String rawDataSegment, NetworkHandlerField networkHandlerField) {
        Map<String, String> parsedSubFieldsMap = new LinkedHashMap<>();
        int currentOffset = 0;

        try {
            // --- Parsear estructura principal ---
            boolean shouldStop = false;
            while (currentOffset < rawDataSegment.length() && !shouldStop) {
                // Validación de borde
                if (currentOffset + DATASET_ID_LENGTH_HEX > rawDataSegment.length()) {
                    shouldStop = true;
                }
                // A. Leer Dataset ID (1 byte = 2 chars hex)
                String datasetIdHex = rawDataSegment.substring(currentOffset, currentOffset + DATASET_ID_LENGTH_HEX);
                currentOffset += DATASET_ID_LENGTH_HEX;

                // B. Leer Dataset Length (2 bytes = 4 chars hex)
                if (currentOffset + LENGTH_PREFIX_HEX > rawDataSegment.length()) {
                    shouldStop = true;
                }
                String datasetLenHex = rawDataSegment.substring(currentOffset, currentOffset + LENGTH_PREFIX_HEX);
                int datasetLengthInBytes = Integer.parseInt(datasetLenHex);
                int datasetLengthInHex = datasetLengthInBytes * 2;
                currentOffset += LENGTH_PREFIX_HEX;

                // C. Leer Dataset Content
                if (currentOffset + datasetLengthInHex > rawDataSegment.length()) {
                    LogsTraces.writeWarning("Dataset " + datasetIdHex + " truncado. Se detiene el parsing.");
                    shouldStop = true;
                }
                String datasetContent = rawDataSegment.substring(currentOffset, currentOffset + datasetLengthInHex);
                currentOffset += datasetLengthInHex;

                // D. Parsear tags dentro del dataset
                parseDatasetTags(datasetIdHex, datasetContent, parsedSubFieldsMap, networkHandlerField);
            }

        } catch (Exception e) {
            LogsTraces.writeWarning("Parseo del Campo 104 interrumpido: " + e.getMessage());
        }

        return parsedSubFieldsMap;
    }


    private void parseDatasetTags(String datasetId, String datasetContent,
                                  Map<String, String> resultContainer,
                                  NetworkHandlerField networkHandlerField) {
        int offset = 0;
        Map<String, Field104> tagDefinitions = TLVField104LoadStructure.getSubFieldDefinitionsForDataset(datasetId);

        try {
            boolean shouldStop = false;
            while (offset < datasetContent.length() && !shouldStop) {
                // Validación de borde para tag
                if (offset + TAG_LENGTH_HEX > datasetContent.length()) {
                    shouldStop = true;
                }

                // A. Leer Tag (puede ser 2 bytes para tags tipo C0, C1, 86)
                String tagHex = datasetContent.substring(offset, offset + TAG_LENGTH_HEX);
                String tag = tagHex.trim();
                offset += TAG_LENGTH_HEX;

                // B. Leer Tag Length
                if (offset + DATASET_ID_LENGTH_HEX > datasetContent.length()) {
                    shouldStop = true;
                }
                String tagLenHex = datasetContent.substring(offset, offset + DATASET_ID_LENGTH_HEX);
                int tagLengthInBytes = Integer.parseInt(tagLenHex);
                int tagLengthInHex = tagLengthInBytes * 2;
                offset += DATASET_ID_LENGTH_HEX;

                // C. Leer Tag Value
                if (offset + tagLengthInHex > datasetContent.length()) {
                    LogsTraces.writeWarning("Tag " + tag + " en dataset " + datasetId + " truncado.");
                    shouldStop = true;
                }
                String tagValueHex = datasetContent.substring(offset, offset + tagLengthInHex);
                offset += tagLengthInHex;

                // D. Decodificar y guardar
                Field104 tagDef = tagDefinitions.get(tag);
                if (tagDef == null) {
                    LogsTraces.writeWarning("Tag desconocido ignorado: " + datasetId + "." + tag);
                    resultContainer.put(compositeFieldId + "." + datasetId + "." + tag, tagValueHex);
                } else {
                    String decodedValue = networkHandlerField.decode(tagValueHex, tagDef.getTypeData());
                    resultContainer.put(compositeFieldId + "." + datasetId + "." + tag, decodedValue);
                }
            }
        } catch (Exception e) {
            LogsTraces.writeWarning("Error parseando tags del dataset " + datasetId + ": " + e.getMessage());
        }
    }
}
