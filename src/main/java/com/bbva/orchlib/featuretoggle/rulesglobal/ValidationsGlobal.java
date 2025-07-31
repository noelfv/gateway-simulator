package com.bbva.orchlib.featuretoggle.rulesglobal;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class ValidationsGlobal {
    private String network;
    private List<RulesG> rules;
}
