package com.bbva.gui.components;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TreeNodeData implements Serializable {
    private String label;
    private String value;
    private static Map<String, String> persistentValues = new HashMap<>();

    public TreeNodeData(String label, String value) {
        this.label = label;
        this.value = value != null ? value : "";

        // Restaurar valor persistente si existe
        if (persistentValues.containsKey(label)) {
            this.value = persistentValues.get(label);
        }
    }

    public void setValue(String value) {
        this.value = value;
        persistentValues.put(this.label, value); // Persistir el valor
        System.out.println("TreeNodeData.setValue() - Guardando: '" + value +
                "' en nodo: " + label);
    }

    public String getValue() {
        // Verificar si el valor se perdió y restaurar desde persistencia
        if ((this.value == null || this.value.isEmpty()) &&
                persistentValues.containsKey(this.label)) {
            this.value = persistentValues.get(this.label);
        }
        return this.value;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}