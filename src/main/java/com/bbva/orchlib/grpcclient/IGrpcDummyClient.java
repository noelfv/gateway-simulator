package com.bbva.orchlib.grpcclient;

import com.bbva.gateway.dto.iso20022.ISO20022;

public interface IGrpcDummyClient {
    ISO20022 callDummyService(String serverGrpcClient, int portGrpcClient, ISO20022 iso20022);
}
