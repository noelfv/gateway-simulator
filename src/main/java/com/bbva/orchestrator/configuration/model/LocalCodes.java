package com.bbva.orchestrator.configuration.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LocalCodes {
    private String network;
    private List<Field> fields;
}
