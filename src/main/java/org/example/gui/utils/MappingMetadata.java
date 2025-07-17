package org.example.gui.utils;

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

   /* public String getFieldOrigin(String origin) {
        return fieldOrigins.get(origin);
    }

    public Map<String, String> getFieldAlls(String origin) {
        return fieldAlls.get(origin);
    }*/

    private boolean shouldExclude(String value) {
        String trimmed = value.trim();
        // Excluir si contiene corchetes vacíos [] o el patrón [P128][]
        return trimmed.contains("[]") ||
                trimmed.matches(".*\\[P\\d+\\]\\[\\].*");
    }

    private boolean shouldExclude2(String value) {
        String trimmed = value.trim();
        // Excluir si termina con [] o tiene el patrón [Pxxx][]
        return trimmed.endsWith("[]") ||
                trimmed.matches(".*\\[P\\d+\\]\\[\\]$");
    }
}