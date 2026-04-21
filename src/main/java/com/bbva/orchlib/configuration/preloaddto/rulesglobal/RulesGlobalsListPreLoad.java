package com.bbva.orchlib.configuration.preloaddto.rulesglobal;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class RulesGlobalsListPreLoad {

    private List<RulesGlobalsPreLoad> rulesList;

    public RulesGlobalsListPreLoad() {
        this.rulesList = new ArrayList<>();
    }

    /**
     * Constructor de copia para deep copy
     *
     * @param rulesList la otra lista de RulesGlobalsPreLoad de la que copiar
     */
    public RulesGlobalsListPreLoad(List<RulesGlobalsPreLoad> rulesList) {
        this.rulesList = new ArrayList<>();
        for (RulesGlobalsPreLoad rulesGlobalsPreLoad : rulesList) {
            this.rulesList.add(new RulesGlobalsPreLoad(rulesGlobalsPreLoad));
        }
    }
}
