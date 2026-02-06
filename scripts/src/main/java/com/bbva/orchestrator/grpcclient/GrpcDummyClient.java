package com.bbva.orchestrator.grpcclient;

import org.springframework.stereotype.Component;

/**
 * Clase GRPC cliente de Dummy
 */
@Component
public class GrpcDummyClient implements IGrpcDummyClient {

    @Override
    public ISO20022 callDummyService(String serverGrpcClient, int portGrpcClient, ISO20022 iso20022) {
        return iso20022;
    }
}

