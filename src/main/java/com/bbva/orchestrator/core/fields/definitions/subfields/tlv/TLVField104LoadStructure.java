package com.bbva.orchestrator.core.fields.definitions.subfields.tlv;

import com.bbva.orchestrator.core.parser.iso8583.strategy.subfields.CompositeTlvField104Parser;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@SuppressWarnings("all")
public class TLVField104LoadStructure {

    private static final Map<String, Field104> BY_ID = new HashMap<>();
    private static final Map<String, Map<String, Field104>> SUB_SUBFIELD_MAP = new LinkedHashMap<>();
    private static final Map<String, Field104> DIRECT_FIELD_104_DEFS = new LinkedHashMap<>();

    static {
        // --- FASE 1: CLASIFICACIÓN ---
        for (Field104 field : Field104.values()) {
            String id = field.getId();
            BY_ID.put(id, field);

            if (id.contains(".")) {
                // Es un subcampo de dataset (ej: "01.C0", "56.9F1F")
                String[] parts = id.split("\\.", 2);
                String datasetId = parts[0];
                String tagId = parts[1];

                SUB_SUBFIELD_MAP
                        .computeIfAbsent(datasetId, k -> new LinkedHashMap<>())
                        .put(tagId, field);
            } else {
                // Es un Dataset directo (ej: "01", "56")
                DIRECT_FIELD_104_DEFS.put(id, field);
            }
        }

        // --- FASE 2: PROMOCIÓN DE DATASETS ---
        for (Map.Entry<String, Field104> entry : DIRECT_FIELD_104_DEFS.entrySet()) {
            String id = entry.getKey();
            Field104 fieldDef = entry.getValue();

            Map<String, Field104> childrenDefs = SUB_SUBFIELD_MAP.get(id);

            // Si tiene tags definidos, le asignamos la estrategia compuesta
            if (childrenDefs != null && !childrenDefs.isEmpty()) {
                fieldDef.setParserStrategy(new CompositeTlvField104Parser(id, childrenDefs));
            }
        }
    }

    private TLVField104LoadStructure() {}


    public static Map<String, Field104> getDirectSubFieldDefinitionsForField104() {
        ensureInitialized();
        return DIRECT_FIELD_104_DEFS;
    }


    public static Map<String, Field104> getSubFieldDefinitionsForDataset(String datasetId) {
        ensureInitialized();
        return SUB_SUBFIELD_MAP.getOrDefault(datasetId, Collections.emptyMap());
    }

    private static void ensureInitialized() {
        // Forzar la carga del bloque static si aún no se ha hecho
        if (BY_ID.isEmpty()) {
            // no-op 
        }
    }
}
