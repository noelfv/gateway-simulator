package com.bbva.gateway.featuretoggle.sensitivedata;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensitiveDataRule {
    private String field;
    private String action;
    private String value;
    private String character;
    private String direction;
    private int startPosition;
    private int numberOfChars;
}