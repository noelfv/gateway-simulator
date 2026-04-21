package com.bbva.orchestrator.core.fields.definitions.subfields.tlv;

import com.bbva.orchestrator.core.fields.definitions.IFieldDefinition;
import com.bbva.orchestrator.core.fields.definitions.ISOSubField;
import java.util.*;
import java.lang.reflect.Method;

/**
 * Clase genérica para cargar estructuras TLV de cualquier campo (Field48, Field104, etc).
 * Permite parametrizar el tipo de campo y la estrategia de parser.
 */
public class TLVFieldLoadStructureMixed {
    private static final Map<String, ISOSubField> byId = new HashMap<>();
    private static final Map<String, Map<String, IFieldDefinition>> subSubfieldMap = new LinkedHashMap<>();
    private static final Map<String, ISOSubField> directFieldDefs = new LinkedHashMap<>();

    private TLVFieldLoadStructureMixed(){}
    static {
        try {
            // Por defecto, usar Field104 y CompositeTlvField104Parser
            Class<ISOSubField> enumClass = (Class<ISOSubField>) Class.forName("com.bbva.orchestrator.core.fields.definitions.subfields.tlv.Field104");
            Class<?> compositeParserClass = Class.forName("com.bbva.orchestrator.core.parser.iso8583.strategy.subfields.CompositeTlvField104Parser");
            Method valuesMethod = enumClass.getMethod("values");
            ISOSubField[] fields = (ISOSubField[]) valuesMethod.invoke(null);
            for (ISOSubField field : fields) {
                String id = field.getId();
                byId.put(id, field);
                if (id.contains(".")) {
                    String[] parts = id.split("\\.", 2);
                    String parentId = parts[0];
                    String subId = parts[1];
                    subSubfieldMap.computeIfAbsent(parentId, k -> new LinkedHashMap<>()).put(subId, field);
                } else {
                    directFieldDefs.put(id, field);
                }
            }
            // Promoción de padres con hijos
            for (Map.Entry<String, ISOSubField> entry : directFieldDefs.entrySet()) {
                String id = entry.getKey();
                ISOSubField fieldDef = entry.getValue();
                Map<String, IFieldDefinition> childrenDefs = subSubfieldMap.get(id);
                if (childrenDefs != null && !childrenDefs.isEmpty()) {
                    Object parserStrategy = compositeParserClass
                        .getConstructor(String.class, Map.class)
                        .newInstance(id, childrenDefs);
                    Method setParserStrategy = enumClass.getMethod("setParserStrategy", Object.class);
                    setParserStrategy.invoke(fieldDef, parserStrategy);
                }
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error inicializando TLVFieldLoadStructureMixed", e);
        }
    }

    public static Map<String, ISOSubField> getDirectSubFieldDefinitions() {
        ensureInitialized();
        return directFieldDefs;
    }

    private static void ensureInitialized() {
        // Forzar la carga del bloque static si aún no se ha hecho
        if (byId.isEmpty()) {
            // no-op
        }
    }
}
