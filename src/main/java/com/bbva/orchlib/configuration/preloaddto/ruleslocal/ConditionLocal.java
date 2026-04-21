package com.bbva.orchlib.configuration.preloaddto.ruleslocal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConditionLocal {
    private String name;
    private String operation;
    private String value;
    private String filterLabelProcess;

    public ConditionLocal() {
    }

    /**
     * Constructor de copia para deep copy
     *
     * @param other el otro ConditionLocal del que copiar
     */
    public ConditionLocal(ConditionLocal other) {
        this.setName(other.name);
        this.setOperation(other.operation);
        this.setValue(other.value);
        this.setFilterLabelProcess(other.filterLabelProcess);
    }
}
