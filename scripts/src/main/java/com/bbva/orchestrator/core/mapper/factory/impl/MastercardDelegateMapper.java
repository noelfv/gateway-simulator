package com.bbva.orchestrator.core.mapper.factory.impl;

import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.mapper.factory.ISO20022DelegateMapper;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class MastercardDelegateMapper implements ISO20022DelegateMapper {

    private static final String NETWORK_MASTERCARD = "PEER02";
    private final DefaultDelegateMapper delegate;

    public MastercardDelegateMapper(DefaultDelegateMapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public ISO20022 mapper(ISO8583 input, Map<String, String> subFields) {
        // Puedes personalizar el comportamiento para Mastercard
        return delegate.mapper(input, subFields);
    }

    @Override
    public Map<String, String> unMapper(ISO20022 input) {
        // Puedes personalizar el comportamiento para Mastercard
        return delegate.unMapper(input);
    }
}
