package com.bbva.orchlib.featuretoggle.ruleslocal;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RulesLocalsPreLoad {

    private String network;
    private String function;
    private List<ConditionLocal> condition;

}
