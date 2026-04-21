package com.bbva.orchlib.configuration.preloaddto.ruleslocal;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RulesL {
    private List<FilterLocal> filter;
    private String function;
}
