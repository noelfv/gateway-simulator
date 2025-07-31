package com.bbva.orchlib.featuretoggle.rulesglobal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConditionGlobal {
    private String name;
    private String operation;
    private String value;
    private String filterLabelProcess;
}
