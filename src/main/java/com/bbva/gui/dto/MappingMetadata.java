package com.bbva.gui.dto;

import lombok.Getter;

import java.util.Comparator;
import java.util.TreeMap;
import java.util.Map;

// Crear una clase para almacenar metadatos de mapeo
@Getter
public class MappingMetadata {

    private final Map<String, String> fieldFilled = new TreeMap<>(Comparator.naturalOrder());
    private final Map<String, String> fieldEmpty = new TreeMap<>(Comparator.naturalOrder());

    public void addFieldOrigin( String origin,String fieldPath) {
        if (origin != null && !origin.trim().isEmpty() && !shouldExclude(origin)) {
            fieldFilled.put(origin, fieldPath);
        }

        assert origin != null;
        if(shouldExclude(origin)){
            fieldEmpty.put( origin,fieldPath);
        }

    }

    private boolean shouldExclude(String value) {
        String trimmed = value.trim();
        // Excluir si contiene corchetes vacíos [] o el patrón [P128][]
        return trimmed.contains("[]") ||
                trimmed.matches(".*\\[P\\d+\\]\\[\\].*");
    }

}