package com.bbva.orchlib.featuretoggle.ruleslocal;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ValidationsLocal {
    private String network;
    private List<RulesL> rules;
}
