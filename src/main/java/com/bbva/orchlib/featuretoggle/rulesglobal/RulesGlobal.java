package com.bbva.orchlib.featuretoggle.rulesglobal;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@Getter
@Setter
public class RulesGlobal {
    private Map<String, String> filterLabels;
    private List<ValidationsGlobal> validations;
}
