package com.bbva.orchestrator.parser.refactor.utils;


import org.springframework.stereotype.Service;

@Service
public class ProcessMonitoringService {

    public String operationTypeValue(String messageType, String transactionType) {
        // Lógica real según tus reglas de negocio
        return "OP_" + messageType + "_" + transactionType;
    }

    public String channelValue(String messageType, Boolean isEcommerce, String terminalKey) {
        if (isEcommerce != null && isEcommerce) return "ECOMMERCE";
        if (terminalKey != null && terminalKey.startsWith("ATM")) return "ATM";
        return "POS";
    }

    public String createTransactionReference(Object inputObject) {
        return "TRX-" + System.currentTimeMillis();
    }
}