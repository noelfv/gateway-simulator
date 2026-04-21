package com.bbva.orchestrator.core.mapper.factory.impl;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.orchestrator.core.dto.ISO8583;
import com.bbva.orchestrator.core.handler.ErrorLabelHandler;
import com.bbva.orchestrator.core.mapper.factory.ISO20022DelegateMapper;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class MastercardDelegateMapper implements ISO20022DelegateMapper {

    private final DefaultDelegateMapper delegate;
    private final ErrorLabelHandler errorLabelHandler;

    public MastercardDelegateMapper(DefaultDelegateMapper delegate, ErrorLabelHandler errorLabelHandler) {
        this.delegate = delegate;
        this.errorLabelHandler = errorLabelHandler;
    }

    @Override
    public ISO20022 mapper(ISO8583 input, Map<String, String> subFields,String label) {
        ISO20022 iso20022 = delegate.mapper(input, subFields, label);
        // Puedes personalizar el comportamiento para Visa
        // Ej: modificar ciertos campos, agregar reglas de negocio, etc.
        return errorLabelHandler.handleLabel(label, "PEER02")
                .orElse(iso20022);
    }

    @Override
    public Map<String, String> unMapper(ISO20022 input) {
        // Puedes personalizar el comportamiento para Mastercard
        return delegate.unMapper(input);
    }
}
