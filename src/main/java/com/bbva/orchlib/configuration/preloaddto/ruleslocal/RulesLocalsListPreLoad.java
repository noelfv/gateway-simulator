package com.bbva.orchlib.configuration.preloaddto.ruleslocal;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RulesLocalsListPreLoad {

    private List<RulesLocalsPreLoad> rulesList;

    public RulesLocalsListPreLoad() {
        this.rulesList = new ArrayList<>();
    }

    /**
     * Constructor de copia para deep copy
     *
     * @param rulesList la otra lista de RulesLocalsPreLoad de la que copiar
     */
    public RulesLocalsListPreLoad(List<RulesLocalsPreLoad> rulesList) {
        this.rulesList = new ArrayList<>();
        for (RulesLocalsPreLoad rulesLocalsPreLoad : rulesList) {
            this.rulesList.add(new RulesLocalsPreLoad(rulesLocalsPreLoad));
        }
    }
}
