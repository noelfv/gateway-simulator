package com.bbva.orchlib.configuration.preloaddto.ruleslocal;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RulesLocalsPreLoad {

    private String network;
    private String function;
    private List<ConditionLocal> condition;

    public RulesLocalsPreLoad() {
    }

    /**
     * Constructor de copia para deep copy
     *
     * @param other el otro RulesLocalsPreLoad del que copiar
     */
    public RulesLocalsPreLoad(RulesLocalsPreLoad other) {
        this.setNetwork(other.network);
        this.setFunction(other.function);
        this.setCondition(new ArrayList<>());
        if (other.condition != null) {
            for (ConditionLocal conditionLocal : other.condition) {
                this.getCondition().add(new ConditionLocal(conditionLocal));
            }
        }
    }
}
