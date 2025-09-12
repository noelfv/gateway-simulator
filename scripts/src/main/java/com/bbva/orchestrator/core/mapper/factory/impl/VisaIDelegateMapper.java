package com.bbva.orchestrator.core.mapper.factory.impl;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.orchestrator.core.builders.ISO8583;
import com.bbva.orchestrator.core.mapper.factory.ISO20022DelegateMapper;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class VisaIDelegateMapper implements ISO20022DelegateMapper {

    private static final String NETWORK_VISA = "PEER01";
    private final DefaultDelegateMapper delegate;

    public VisaIDelegateMapper(DefaultDelegateMapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public ISO20022 mapper(ISO8583 input, Map<String, String> subFields) {
        // Puedes personalizar el comportamiento para Visa
        // Ej: modificar ciertos campos, agregar reglas de negocio, etc.
        return delegate.mapper(input, subFields);
    }

    @Override
    public Map<String, String> unMapper(ISO20022 input) {
        // Puedes personalizar el comportamiento para Mastercard
        return delegate.unMapper( input);
    }
}
