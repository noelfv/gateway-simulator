package com.bbva.orchlib.configuration.preloaddto.rulesglobal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConditionGlobal {
    private String name;
    private String operation;
    private String value;
    private String filterLabelProcess;

    public ConditionGlobal() {
    }

    /**
     * Constructor de copia para deep copy
     *
     * @param other el otro ConditionGlobal del que copiar
     */
    public ConditionGlobal(ConditionGlobal other) {
        this.name = other.getName();
        this.operation = other.getOperation();
        this.value = other.getValue();
        this.filterLabelProcess = other.getFilterLabelProcess();
    }
}
