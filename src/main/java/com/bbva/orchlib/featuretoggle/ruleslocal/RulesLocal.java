package com.bbva.orchlib.featuretoggle.ruleslocal;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class RulesLocal {
    private Map<String, String> filterLabels;
    private List<Orchestrations> orchestrations;
    private List<ValidationsLocal> validations;
}
