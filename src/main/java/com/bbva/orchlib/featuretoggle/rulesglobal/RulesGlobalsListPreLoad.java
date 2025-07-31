package com.bbva.orchlib.featuretoggle.rulesglobal;

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
}
