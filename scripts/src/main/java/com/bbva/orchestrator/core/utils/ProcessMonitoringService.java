package com.bbva.orchestrator.core.utils;

import com.bbva.orchestrator.core.builders.ISO8583;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class ProcessMonitoringService {

    private static final String NETWORK_PEER01 = "PEER01";
    private static final String NETWORK_PEER02 = "PEER02";
    private static final String FIELD63PART1  = "63.1";

    public String operationTypeValue(String messageType, String transactionType) {
        // Lógica real según tus reglas de negocio
        return "OP_" + messageType + "_" + transactionType;
    }

    public String channelValue(String messageType, Boolean isEcommerce, String terminalKey) {
        if (isEcommerce != null && isEcommerce) return "ECOMMERCE";
        if (terminalKey != null && terminalKey.startsWith("ATM")) return "ATM";
        return "POS";
    }

    public  String createTransactionReference(ISO8583 inputObject, Map<String, String> subFields, String networkName){
        StringBuilder transactionReference = new StringBuilder();

        String field11 = isNullOrEmpty(inputObject.getSystemTraceAuditNumber());
        String field32 = isNullOrEmpty(inputObject.getAcquiringInstitutionIdentificationCode());
        String field37 = isNullOrEmpty(inputObject.getRetrievalReferenceNumber());
        String field41 = isNullOrEmpty(inputObject.getCardAcceptorTerminalIdentification());
        String field42 = isNullOrEmpty(inputObject.getCardAcceptorIdentificationCode());
        String field63 = isNullOrEmpty(inputObject.getNetworkData());
        String field63Part1 = subFields.get(FIELD63PART1);

        if (networkName.equalsIgnoreCase(NETWORK_PEER01)) {
            transactionReference.append(field11)
                    .append(field32)
                    .append(field37)
                    .append(field41)
                    .append(field42)
                    .append(field63Part1);
        } else if (networkName.equalsIgnoreCase(NETWORK_PEER02)) {
            transactionReference.append(field11)
                    .append(field32)
                    .append(field37)
                    .append(field41)
                    .append(field63);
        }

        return transactionReference.toString();
    }
    private static String isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty() ? "" : value;
    }
}