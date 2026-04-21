package com.bbva.orchestrator.core.builders;

import com.bbva.gateway.dto.iso20022.*;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.core.enums.ErrorLabel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class ISO20022Builder {

    /**
     * Construye un ISO20022 de error enriquecido con datos de traza para monitoreo.
     * Incluye el label original, la descripción del error, el componente que originó el fallo
     * y el mensaje específico de la excepción para facilitar la identificación del error.
     *
     * @param networkName  nombre de la red
     * @param label        etiqueta de error original recibida
     * @param errorLabel   enum ErrorLabel resuelto
     * @param errorMessage mensaje específico de la excepción
     * @return ISO20022 con datos mínimos y traceData para monitoreo
     */
    public static ISO20022 buildISO20022WithErrorTrace(String networkName, String label, ErrorLabel errorLabel, String errorMessage) {
        String currentTime = String.valueOf(Instant.now().toEpochMilli());
        String traceValue = errorLabel.getValue() + (errorMessage != null ? " - " + errorMessage : "");

        return ISO20022.builder()
                .networkName(networkName)
                .messageFunction("ERRO")
                .socketPort(GrpcHeadersInfo.getPort())
                .transaction(TransactionDTO.builder()
                        .transactionId(TransactionIdDTO.builder()
                                .transactionReference("error-%s".formatted(UUID.randomUUID()))
                                .build())
                        .build())
                .traceData(List.of(
                        TraceDataDTO.builder()
                                .key(label)
                                .value(traceValue)
                                .componentError(errorLabel.getComponent())
                                .build()
                ))
                .monitoring(MonitoringDTO.builder()
                        .startDateMs(currentTime)
                        .transactionStatus("ERROR")
                        .channelFilter("ERROR")
                        .isNextGen(Boolean.FALSE)
                        .isMessageInvalid(Boolean.TRUE)
                        .build())
                .build();
    }

    /**
     * Construye un ISO20022 de error enriquecido con datos de traza para monitoreo.
     * Incluye el label original, la descripción del error y el componente que originó el fallo.
     *
     * @param networkName nombre de la red
     * @param label       etiqueta de error original recibida
     * @param errorLabel  enum ErrorLabel resuelto
     * @return ISO20022 con datos mínimos y traceData para monitoreo
     */
    public static ISO20022 buildISO20022WithErrorTrace(String networkName, String label, ErrorLabel errorLabel) {
        String currentTime = String.valueOf(Instant.now().toEpochMilli());

        return ISO20022.builder()
                .networkName(networkName)
                .messageFunction("ERRO")
                .socketPort(GrpcHeadersInfo.getPort())
                .transaction(TransactionDTO.builder()
                        .transactionId(TransactionIdDTO.builder()
                                .transactionReference("error-%s".formatted(UUID.randomUUID()))
                                .build())
                        .build())
                .traceData(List.of(
                        TraceDataDTO.builder()
                                .key(label)
                                .value(errorLabel.getValue())
                                .componentError(errorLabel.getComponent())
                                .build()
                ))
                .monitoring(MonitoringDTO.builder()
                        .startDateMs(currentTime)
                        .transactionStatus("ERROR")//En el dashboarde transacciones los que tengan este estado se pueden filtrar para análisis de errores
                        .channelFilter("ERROR")
                        .isNextGen(Boolean.FALSE)
                        .isMessageInvalid(Boolean.TRUE)
                        .build())
                .build();
    }
}