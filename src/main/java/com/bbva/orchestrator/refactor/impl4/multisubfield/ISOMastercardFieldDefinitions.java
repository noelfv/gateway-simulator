package com.bbva.orchestrator.refactor.impl4.multisubfield;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchestrator.refactor.impl4.subfields.CompositeSubFieldParser;
import com.bbva.orchestrator.refactor.impl4.LlvarLengthPrefixParser;
import com.bbva.orchestrator.refactor.impl4.subfields.ISOSubField48Mastercard;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class ISOMastercardFieldDefinitions {

    private static final Map<String, ISOSubField48Mastercard> BY_ID = new HashMap<>();
    private static final Map<String, Map<String, ISOSubField48Mastercard>> SUB_SUBFIELD_MAP = new LinkedHashMap<>();
    private static final Map<String, ISOSubField48Mastercard> DIRECT_FIELD_48_DEFS = new LinkedHashMap<>();
    private static final Map<String, ISOSubField48Mastercard> DIRECT_FIELD_56_DEFS = new LinkedHashMap<>(); // Nuevo mapa para Campo 56

    static {
        // Fase 1: Llenar BY_ID y SUB_SUBFIELD_MAP con todas las constantes de ISOMastercardSubField
        // En esta fase, los parserStrategy de los campos compuestos son null.
        for (ISOSubField48Mastercard subField : ISOSubField48Mastercard.values()) {
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
        for (ISOSubField48Mastercard subField : ISOSubField48Mastercard.values()) {
            if (subField.isVariable() && subField.getParserStrategy() == null) { // Identificar los compuestos que aún no tienen parser
                Map<String, ISOSubField48Mastercard> definitionsForThisComposite = getSubFieldDefinitionsForCompositeInternal(subField.getId());
                // Crear la instancia de CompositeSubFieldParser y asignarla
                subField.setParserStrategy(new LlvarLengthPrefixParser(new CompositeSubFieldParser(subField.getId(), definitionsForThisComposite)));
            }
        }

        // Fase 3: Llenar los mapas de definiciones directas de campos compuestos específicos
        // Campo 48
        String[] direct48SubfieldOrder = {
                "01", "03", "05", "09", "11", "13", "15", "18", "21", "22", "23", "24", "25", "26", "27", "33", "34",
                "36", "37", "40", "41", "42", "48", "49", "50", "51", "53", "55", "56", "57", "58", "60", "61",
                "62", "64", "65", "66", "67", "68", "71", "74", "75", "78", "79", "93"
        };
        for (String subFieldId : direct48SubfieldOrder) {
            ISOSubField48Mastercard subField = BY_ID.get(subFieldId);
            if (subField != null && !subField.getId().contains(".")) {
                DIRECT_FIELD_48_DEFS.put(subFieldId, subField);
            }
        }

        // Campo 56 (Ejemplo: Asumiendo que tiene subcampos 01, 02)
        // DEBES DEFINIR LOS SUB-SUB CAMPOS REALES PARA EL CAMPO 56 EN ISOMastercardSubField
        String[] direct56SubfieldOrder = {
                "01", "02", "03" // Ejemplo de IDs de subcampos directos para el Campo 56
        };
        for (String subFieldId : direct56SubfieldOrder) {
            ISOSubField48Mastercard subField = BY_ID.get("56." + subFieldId); // Asumiendo que se definen como "56.01"
            if (subField != null) { // No verificar contains(".") aquí, ya que son sub-subcampos
                DIRECT_FIELD_56_DEFS.put(subFieldId, subField);
            }
        }

        LogsTraces.writeInfo("ISOMastercardFieldDefinitions inicializado completamente.");
    }

    // Constructor privado para evitar instanciación
    private ISOMastercardFieldDefinitions() {}

    public static ISOSubField48Mastercard getById(String id) {
        ensureInitialized();
        return BY_ID.get(id);
    }

    public static Map<String, ISOSubField48Mastercard> getSubSubFieldsForParent(String parentSubFieldId) {
        ensureInitialized();
        return SUB_SUBFIELD_MAP.getOrDefault(parentSubFieldId, Collections.emptyMap());
    }

    public static Map<String, ISOSubField48Mastercard> getDirectSubFieldDefinitionsForField48() {
        ensureInitialized();
        return DIRECT_FIELD_48_DEFS;
    }

    public static Map<String, ISOSubField48Mastercard> getDirectSubFieldDefinitionsForField56() { // Nuevo getter para Campo 56
        ensureInitialized();
        return DIRECT_FIELD_56_DEFS;
    }

    // Método para obtener las definiciones de subcampos para un CompositeFieldParser específico
    private static Map<String, ISOSubField48Mastercard> getSubFieldDefinitionsForCompositeInternal(String compositeFieldId) {
        if ("48".equals(compositeFieldId)) {
            return getDirectSubFieldDefinitionsForField48Internal();
        } else if ("56".equals(compositeFieldId)) { // Lógica para Campo 56
            return getDirectSubFieldDefinitionsForField56Internal();
        } else {
            return SUB_SUBFIELD_MAP.getOrDefault(compositeFieldId, Collections.emptyMap());
        }
    }

    // Métodos internos para obtener las definiciones directas de campos compuestos
    private static Map<String, ISOSubField48Mastercard> getDirectSubFieldDefinitionsForField48Internal() {
        Map<String, ISOSubField48Mastercard> definitions = new LinkedHashMap<>();
        String[] direct48SubfieldOrder = {
                "01", "03", "05", "09", "11", "13", "15", "18", "21", "22", "23", "24", "25", "26", "27", "33", "34",
                "36", "37", "40", "41", "42", "48", "49", "50", "51", "53", "55", "56", "57", "58", "60", "61",
                "62", "64", "65", "66", "67", "68", "71", "74", "75", "78", "79", "93"
        };
        for (String subFieldId : direct48SubfieldOrder) {
            ISOSubField48Mastercard subField = BY_ID.get(subFieldId);
            if (subField != null && !subField.getId().contains(".")) {
                definitions.put(subFieldId, subField);
            }
        }
        return definitions;
    }

    private static Map<String, ISOSubField48Mastercard> getDirectSubFieldDefinitionsForField56Internal() { // Nuevo método interno para Campo 56
        Map<String, ISOSubField48Mastercard> definitions = new LinkedHashMap<>();
        // DEBES DEFINIR LOS SUB-SUB CAMPOS REALES PARA EL CAMPO 56 EN ISOMastercardSubField
        String[] direct56SubfieldOrder = {
                "01", "02", "03" // Ejemplo de IDs de subcampos directos para el Campo 56
        };
        for (String subFieldId : direct56SubfieldOrder) {
            ISOSubField48Mastercard subField = BY_ID.get("56." + subFieldId); // Asumiendo que se definen como "56.01"
            if (subField != null) { // No verificar contains(".") aquí, ya que son sub-subcampos
                definitions.put(subFieldId, subField);
            }
        }
        return definitions;
    }

    // Método de inicialización perezosa (lazy initialization)
    private static void ensureInitialized() {
        @SuppressWarnings("unused")
        boolean initialized = BY_ID.isEmpty();
    }
}