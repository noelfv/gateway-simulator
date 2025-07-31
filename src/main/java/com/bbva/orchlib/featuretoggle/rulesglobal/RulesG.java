package com.bbva.orchlib.featuretoggle.rulesglobal;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RulesG {
    private List<FilterGlobal> filter;
    private String function;
}
