package org.example.orchestrator.common;

import java.util.Comparator;
import java.util.TreeMap;
import java.util.Map;

// Crear una clase para almacenar metadatos de mapeo
public class MappingMetadata {
    //private Map<String, String> fieldOrigins = new TreeMap<>();
    private Map<String, String> fieldFilled = new TreeMap<>(Comparator.naturalOrder());
    private Map<String, String> fieldEmpty = new TreeMap<>(Comparator.naturalOrder());

    public void addFieldOrigin( String origin,String fieldPath) {
        if (origin != null && !origin.trim().isEmpty() && !shouldExclude(origin)) {
            fieldFilled.put(origin, fieldPath);
        }

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

    public Map<String, String> getFieldFilled() {
        return fieldFilled;
    }

    public Map<String, String> getFieldEmpty() {
        return fieldEmpty;
    }

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