package com.bbva.orchlib.configuration.preloaddto.rulesglobal;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;


@Getter
@Setter
public class RulesGlobal {
    private Map<String, String> filterLabels;
    private Map<String, String> functionParamLabels;
    private List<ValidationsGlobal> validations;
}
