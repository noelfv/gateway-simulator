package com.bbva.orchlib.featuretoggle.businessdatalocal;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Process {
    private String type;
    private List<Data> data;
}
