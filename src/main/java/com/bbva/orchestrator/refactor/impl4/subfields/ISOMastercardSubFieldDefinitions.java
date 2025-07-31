package com.bbva.orchestrator.refactor.impl4.subfields;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ISOMastercardSubFieldDefinitions {

    private static final Map<String, Map<String, ISOSubField48Mastercard>> SUB_SUBFIELD_MAP = new LinkedHashMap<>();
    private static final Map<String, ISOSubField48Mastercard> BY_ID = new HashMap<>();

    static {
        // Inicializar el mapa de sub-subcampos
        Map<String, ISOSubField48Mastercard> subSubFields56 = new LinkedHashMap<>();
        subSubFields56.put("56.01", ISOSubField48Mastercard.SF_48_56_01);
        subSubFields56.put("56.02", ISOSubField48Mastercard.SF_48_56_02);
        SUB_SUBFIELD_MAP.put("56", subSubFields56);

        // Inicializar el mapa BY_ID con todos los valores del enum
        for (ISOSubField48Mastercard subField : ISOSubField48Mastercard.values()) {
            BY_ID.put(subField.getId(), subField);
        }
    }

    public static ISOSubField48Mastercard getById(String id) {
        return BY_ID.get(id);
    }

    public static Map<String, ISOSubField48Mastercard> getSubSubFieldsForParent(String parentSubFieldId) {
        return SUB_SUBFIELD_MAP.get(parentSubFieldId);
    }

    public static Map<String, ISOSubField48Mastercard> getDirectSubFieldDefinitionsForField48() {
        Map<String, ISOSubField48Mastercard> directSubFields = new HashMap<>();
        for (ISOSubField48Mastercard subField : ISOSubField48Mastercard.values()) {
            if (!subField.getId().contains(".")) {
                directSubFields.put(subField.getId(), subField);
            }
        }
        return directSubFields;
    }
}