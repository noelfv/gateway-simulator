package com.bbva.orchestrator.refactor.impl4.subfields;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ISOMastercardSubFieldDefinitions {

    private static final Map<String, Map<String, ISOMastercardSubField>> SUB_SUBFIELD_MAP = new LinkedHashMap<>();
    private static final Map<String, ISOMastercardSubField> BY_ID = new HashMap<>();

    static {
        // Inicializar el mapa de sub-subcampos
        Map<String, ISOMastercardSubField> subSubFields56 = new LinkedHashMap<>();
        subSubFields56.put("56.01", ISOMastercardSubField.SF_48_56_01);
        subSubFields56.put("56.02", ISOMastercardSubField.SF_48_56_02);
        SUB_SUBFIELD_MAP.put("56", subSubFields56);

        // Inicializar el mapa BY_ID con todos los valores del enum
        for (ISOMastercardSubField subField : ISOMastercardSubField.values()) {
            BY_ID.put(subField.getId(), subField);
        }
    }

    public static ISOMastercardSubField getById(String id) {
        return BY_ID.get(id);
    }

    public static Map<String, ISOMastercardSubField> getSubSubFieldsForParent(String parentSubFieldId) {
        return SUB_SUBFIELD_MAP.get(parentSubFieldId);
    }

    public static Map<String, ISOMastercardSubField> getDirectSubFieldDefinitionsForField48() {
        Map<String, ISOMastercardSubField> directSubFields = new HashMap<>();
        for (ISOMastercardSubField subField : ISOMastercardSubField.values()) {
            if (!subField.getId().contains(".")) {
                directSubFields.put(subField.getId(), subField);
            }
        }
        return directSubFields;
    }
}