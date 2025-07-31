package com.bbva.orchlib.featuretoggle.rulesglobal;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RulesGlobalsPreLoad {

    private String network;
    private String function;
    private List<ConditionGlobal> condition;
}
