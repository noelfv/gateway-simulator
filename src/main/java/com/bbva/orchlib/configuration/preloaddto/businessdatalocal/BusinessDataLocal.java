package com.bbva.orchlib.configuration.preloaddto.businessdatalocal;

import com.bbva.orchlib.configuration.preloaddto.businessdata.Custom;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class BusinessDataLocal {
    private String network;
    private List<Process> process;
    private List<InputLRA> inputLRA;
    private List<OutputLRA> outputLRA;
    private Map<String, List<Custom>> custom;
}
