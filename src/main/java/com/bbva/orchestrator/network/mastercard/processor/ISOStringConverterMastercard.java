package com.bbva.orchestrator.network.mastercard.processor;

import com.bbva.orchestrator.parser.common.ISOField;
import com.bbva.orchestrator.parser.common.ISOStringConverter;
import com.bbva.orchestrator.network.mastercard.ISOFieldMastercard;


public class ISOStringConverterMastercard extends ISOStringConverter {

    private static final ISOStringConverterMastercard INSTANCE = new ISOStringConverterMastercard();
    private ISOStringConverterMastercard(){}

    public static ISOStringConverterMastercard getInstance() {
        return INSTANCE;
    }

    @Override
    protected ISOField getISOFieldById(int id) {
        return ISOFieldMastercard.getById(id);
    }
}
