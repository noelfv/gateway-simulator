package com.bbva.orchlib.grpcclient;

import com.bbva.gateway.dto.iso20022.ISO20022;

public interface IGrpcControlDialogoClient {
    ISO20022 callControlDialogoService(String serverGrpcClient, int portGrpcClient, ISO20022 iso20022);
}
