package com.bbva.orchestrator.core.fields.definitions.subfields.tlv;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchestrator.core.parser.iso8583.strategy.subfields.CompositeTlvFieldParser;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

// --- NUEVA CLASE: ISOMastercardFieldDefinitions ---
//REVISAR
// Esta clase centraliza la carga y gestión de todas las definiciones de campos y subcampos.
// Su bloque estático se ejecutará una única vez cuando se acceda por primera vez a un método estático.
public class TLVFieldLoadStructure {

    private static final Map<String, Field48> BY_ID = new HashMap<>();
    private static final Map<String, Map<String, Field48>> SUB_SUBFIELD_MAP = new LinkedHashMap<>();
    private static final Map<String, Field48> DIRECT_FIELD_48_DEFS = new LinkedHashMap<>();

    static {
        // Fase 1: Llenar BY_ID y SUB_SUBFIELD_MAP con todas las constantes de ISOMastercardSubField
        // En esta fase, los parserStrategy de los campos compuestos son null.
        for (Field48 subField : Field48.values()) {
            BY_ID.put(subField.getId(), subField); // Usar getId() para la clave
            if (subField.getId().contains(".")) {
                String[] parts = subField.getId().split("\\.");
                String parentSubFieldId = parts[0];
                String subSubFieldId = parts[1];
                SUB_SUBFIELD_MAP.computeIfAbsent(parentSubFieldId, k -> new LinkedHashMap<>())
                        .put(subSubFieldId, subField);
            }
        }

        // Fase 2: Después de que todos los mapas estén poblados,
        // inicializar los CompositeSubFieldParser para los campos compuestos.
        // Ahora, los CompositeSubFieldParser pueden acceder a los mapas de forma segura.
        for (Field48 subField : Field48.values()) {
            if (subField.isVariable() && subField.getParserStrategy() == null) {
                Map<String, Field48> defs = "48".equals(subField.getId()) ?
                        getDirectSubFieldDefinitionsForField48() :
                        SUB_SUBFIELD_MAP.getOrDefault(subField.getId(), Collections.emptyMap());
                subField.setParserStrategy(new CompositeTlvFieldParser(subField.getId(), defs));
            }
        }



        // Fase 3: Llenar el mapa de definiciones directas del Campo 48
        // Esto se hace una vez aquí para que getDirectSubFieldDefinitionsForField48() sea eficiente
        String[] direct48SubfieldOrder = {
                "01", "03", "05", "09", "11", "13", "15", "18", "20", "21", "22", "23", "24", "25", "26", "27", "32", "33", "34",
                "36", "37", "40", "41", "42","43", "48", "49", "50", "51", "53", "55", "56", "57", "58", "60", "61",
                "62", "64", "65", "66", "67", "68", "71","72", "74", "75", "78", "79", "92", "93"
        };
        for (String subFieldId : direct48SubfieldOrder) {
            Field48 subField = BY_ID.get(subFieldId);
            if (subField != null && !subField.getId().contains(".")) {
                DIRECT_FIELD_48_DEFS.put(subFieldId, subField);
            }
        }
        LogsTraces.writeInfo("ISOSubFieldDefinitions inicializado completamente.");
    }

    // Constructor privado para evitar instanciación
    private TLVFieldLoadStructure() {}


    public static Map<String, Field48> getDirectSubFieldDefinitionsForField48() {
        ensureInitialized();
        return DIRECT_FIELD_48_DEFS;
    }

    // Método para obtener las definiciones de subcampos para un CompositeFieldParser específico
    public static Map<String, Field48> getSubFieldDefinitionsForComposite(String compositeFieldId) {
        ensureInitialized();
        if ("48".equals(compositeFieldId)) {
            return DIRECT_FIELD_48_DEFS;
        } else {
            return SUB_SUBFIELD_MAP.getOrDefault(compositeFieldId, Collections.emptyMap());
        }
    }

    // Método de inicialización perezosa (lazy initialization)
    // Se asegura de que el bloque estático se ejecute en la primera llamada.
    private static void ensureInitialized() {
        // Acceder a cualquier campo estático (ej. BY_ID) fuerza la ejecución del bloque estático.
        // No hace falta hacer nada más aquí, solo el acceso.
        @SuppressWarnings("unused")
        boolean initialized = BY_ID.isEmpty(); // Acceder para forzar la inicialización
    }
}
