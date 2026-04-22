package com.bbva.gui.utils;

import com.bbva.gui.dto.ISOFieldInfo;
import com.bbva.orchestrator.core.fields.MastercardISOField;
import com.bbva.orchestrator.core.fields.definitions.ISOField;
import com.bbva.orchestrator.core.fields.definitions.ISOSubField;
import com.bbva.orchestrator.core.fields.definitions.subfields.tlv.Field48;

public final class ISOFieldFinder {

    private ISOFieldFinder() {}

    public static String findFieldIdByName(String fieldName) {
        for (ISOField field : MastercardISOField.values()) {
            if (field.getName().equalsIgnoreCase(fieldName)) {
                return String.valueOf(field.getId());
            }
        }
        return null;
    }

    public static String findFieldTLVIdByName(String fieldName) {
        for (ISOSubField field : Field48.values()) {
            if (field.getName().equalsIgnoreCase(fieldName)) {
                return String.valueOf(field.getId());
            }
        }
        return null;
    }

    public static ISOFieldInfo getDataTypeISO8583(String fieldId) {
        try {
            int id = Integer.parseInt(fieldId);
            for (MastercardISOField field : MastercardISOField.values()) {
                if (field.getId() == id) {
                    String caracteristica = field.isVariable() ? "VARIABLE" : "FIXED";
                    return new ISOFieldInfo(field.getId(), field.getName(),
                            field.getTypeData().name(), caracteristica, field.getLength());
                }
            }
        } catch (NumberFormatException ignored) {
        }
        return new ISOFieldInfo();
    }
}
