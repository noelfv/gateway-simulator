package com.bbva.orchlib.configuration.preloaddto.rulesglobal;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RulesGlobalsPreLoad {

    private String network;
    private String function;
    private List<ConditionGlobal> condition;

    public RulesGlobalsPreLoad() {
    }

    /**
     * Constructor de copia para deep copy
     *
     * @param other el otro RulesGlobalsPreLoad del que copiar
     */
    public RulesGlobalsPreLoad(RulesGlobalsPreLoad other) {
        this.setNetwork(other.network);
        this.setFunction(other.function);
        this.setCondition(new ArrayList<>());
        if (other.condition != null) {
            for (ConditionGlobal conditionGlobal : other.condition) {
                this.getCondition().add(new ConditionGlobal(conditionGlobal));
            }
        }
    }
}
