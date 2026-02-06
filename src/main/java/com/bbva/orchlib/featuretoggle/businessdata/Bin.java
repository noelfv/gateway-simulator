package com.bbva.orchlib.featuretoggle.businessdata;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Bin {
    private String key;
    private List<Fields> fields;
}
