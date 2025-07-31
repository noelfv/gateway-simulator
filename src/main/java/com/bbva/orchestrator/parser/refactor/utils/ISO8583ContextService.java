package com.bbva.orchestrator.parser.refactor.utils;


import com.bbva.orchestrator.parser.common.ISO8583Context;
import org.springframework.stereotype.Service;

@Service
public class ISO8583ContextService {

    public String getISO8583(String traceId) {
        // Aquí iría la lógica real (ej. caché, contexto distribuido)

        return ISO8583Context.getISO8583(traceId);
    }
}