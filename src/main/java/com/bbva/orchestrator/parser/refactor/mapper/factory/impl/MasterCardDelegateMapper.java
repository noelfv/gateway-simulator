package com.bbva.orchestrator.parser.refactor.mapper.factory.impl;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.refactor.mapper.factory.ISO20022DelegateMapper;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MasterCardDelegateMapper implements ISO20022DelegateMapper {

    private final DefaultDelegateMapper delegate;

    public MasterCardDelegateMapper(DefaultDelegateMapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public ISO20022 translate(ISO8583 input, Map<String, String> subFields) {
        // Puedes personalizar el comportamiento para Mastercard
        // Ej: modificar ciertos campos, agregar reglas de negocio, etc.
        ISO8583 modifiedInput = input;
        // Ejemplo de modificación: agregar un campo adicional específico para Mastercard
        if(input.getMessageType().startsWith("0800")) {
            modifiedInput.builder().acquirerCountryCode(input.getPrimaryAccountNumber());
            modifiedInput.builder().primaryAccountNumber(null);        }
        return delegate.translate(modifiedInput, subFields);
    }
}
