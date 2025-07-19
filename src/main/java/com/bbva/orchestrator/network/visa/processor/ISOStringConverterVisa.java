package com.bbva.orchestrator.network.visa.processor;

import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchestrator.parser.common.ISOStringConverter;
import com.bbva.orchestrator.network.visa.ISOFieldVisa;


import java.util.Map;

public class ISOStringConverterVisa extends ISOStringConverter {

    private static final ISOStringConverterVisa INSTANCE = new ISOStringConverterVisa();

    private ISOStringConverterVisa() {
    }

    public static ISOStringConverterVisa getInstance() {
        return INSTANCE;
    }

    @Override
    public String convertToISOString(Map<String, String> mapValues, boolean clean) {
        return mapValues.get("header") + super.convertToISOString(mapValues, !clean);
    }

    @Override
    protected ISOField getISOFieldById(int id) {
        return ISOFieldVisa.getById(id);
    }
}
