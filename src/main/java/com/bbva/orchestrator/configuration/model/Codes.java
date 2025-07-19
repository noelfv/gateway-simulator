package com.bbva.orchestrator.configuration.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Codes {
    private List<In> in;
    private List<Out> out;
}
