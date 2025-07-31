package com.bbva.orchlib.featuretoggle.ruleslocal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConditionLocal {
    private String name;
    private String operation;
    private String value;
    private String filterLabelProcess;
}
