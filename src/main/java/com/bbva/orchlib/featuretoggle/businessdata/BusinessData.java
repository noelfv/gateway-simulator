package com.bbva.orchlib.featuretoggle.businessdata;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class BusinessData {
    private List<Bin> bins;
    private List<Currency> currency;
    private Map<String, List<Custom>> custom;
}
