package com.bbva.orchlib.featuretoggle.ruleslocal;

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
}
