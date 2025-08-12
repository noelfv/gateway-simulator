package com.bbva.orchestrator.parser.refactor.mapper.factory.impl;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.refactor.mapper.factory.ISO20022DelegateMapper;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class VisaIDelegateMapper implements ISO20022DelegateMapper {

    private final DefaultDelegateMapper delegate;

    public VisaIDelegateMapper(DefaultDelegateMapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public ISO20022 translate(ISO8583 input, Map<String, String> subFields) {
        // Puedes personalizar el comportamiento para Visa
        // Ej: modificar ciertos campos, agregar reglas de negocio, etc.
        return delegate.translate(input, subFields);
    }
}
