package org.example.orchestrator.mastercard.processor;


import org.example.orchestrator.common.ISOField;
import org.example.orchestrator.common.ISOStringConverter;
import org.example.orchestrator.mastercard.ISOFieldMastercard;

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
