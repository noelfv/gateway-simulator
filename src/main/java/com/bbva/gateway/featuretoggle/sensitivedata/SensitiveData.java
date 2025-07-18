package com.bbva.gateway.featuretoggle.sensitivedata;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SensitiveData {
    private Map<String, List<SensitiveDataRule>> applications;
}