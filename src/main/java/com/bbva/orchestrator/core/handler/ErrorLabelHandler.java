package com.bbva.orchestrator.core.handler;

import com.bbva.gateway.dto.iso20022.ISO20022;
import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchestrator.core.builders.ISO20022Builder;
import com.bbva.orchestrator.core.enums.ErrorLabel;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Componente con responsabilidad única de evaluar si un label recibido corresponde
 * a un error conocido y, en tal caso, construir el ISO20022 de error con trazabilidad.
 */
@Component
public class ErrorLabelHandler {

    /**
     * Evalúa si el label recibido es un error conocido y retorna el ISO20022 de error correspondiente.
     *
     * @param label       etiqueta recibida en el flujo
     * @param networkName nombre de la red obtenido de los headers gRPC
     * @return Optional con el ISO20022 de error si el label es conocido, vacío si no lo es
     */
    public Optional<ISO20022> handleLabel(String label, String networkName) {
        return ErrorLabel.fromLabel(label)
                .map(error -> {
                    LogsTraces.writeError("Error detectado en flujo [%s] - red: %s, label: %s"
                            .formatted(error.getValue(), networkName, label));
                    return ISO20022Builder.buildISO20022WithErrorTrace(networkName, label, error);
                });
    }
}
