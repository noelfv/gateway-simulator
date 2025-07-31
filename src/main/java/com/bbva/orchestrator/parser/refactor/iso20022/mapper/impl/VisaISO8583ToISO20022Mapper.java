package com.bbva.orchestrator.parser.refactor.iso20022.mapper.impl;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.parser.refactor.iso20022.mapper.ISO8583ToISO20022Mapper;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class VisaISO8583ToISO20022Mapper implements ISO8583ToISO20022Mapper {

    private final DefaultISO8583ToISO20022Mapper delegate;

    public VisaISO8583ToISO20022Mapper(DefaultISO8583ToISO20022Mapper delegate) {
        this.delegate = delegate;
    }

    @Override
    public ISO20022 translate(ISO8583 input, Map<String, String> subFields) {
        // Puedes personalizar el comportamiento para Visa
        // Ej: modificar ciertos campos, agregar reglas de negocio, etc.
        return delegate.translate(input, subFields);
    }
}
