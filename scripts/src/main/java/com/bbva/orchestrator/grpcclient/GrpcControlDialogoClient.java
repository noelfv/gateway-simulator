package com.bbva.orchestrator.grpcclient;

import org.springframework.stereotype.Component;

/**
 * Clase GRPC cliente de Control de Dialogo
 */
@Component
public class GrpcControlDialogoClient implements IGrpcControlDialogoClient {

    @Override
    public ISO20022 callControlDialogoService(String serverGrpcClient, int portGrpcClient, ISO20022 iso20022) {
        return iso20022;
    }
}
