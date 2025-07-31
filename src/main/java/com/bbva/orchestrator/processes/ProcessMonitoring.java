package com.bbva.orchestrator.processes;

import com.bbva.gateway.dto.iso20022.MonitoringDTO;
import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.configuration.monitoring.FieldDto;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import com.bbva.orchestrator.validations.FieldLocalCodeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProcessMonitoring {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessMonitoring.class);
    private static final String NETWORK_PEER01 = "PEER01";
    private static final String NETWORK_PEER02 = "PEER02";
    private static final String CODE_08 = "08";
    private static final String CODE_019 = "019";
    private static final String CODE_00 = "00";
    private static final String CODE_420829 = "420829";
    private static final String LIST_BIN = "LIST_BIN";
    private static final String MERCHANT_CATEGORY_DES = "MERCHANT_CATEGORY_DES";
    private static final String OPERATION_FILTER = "OPERATION_FILTER";
    private static final String TRANSACTION_TYPE_DES = "TRANSACTION_TYPE_DES";
    private static final String CHANNEL_DESCRIPTION = "CHANNEL_DESCRIPTION";
    private static final String INTERNAL_FAILURE = "internal_failure";
    private static final String APPROVED = "Approved";
    private static final String DENIED = "Denied";
    private static final String UNKNOWN = "UNKNOWN";
    private static final String OTHERS = "OTHERS";
    private static final String WALLET = "WALLET";
    private static final String RESULT_DATA = "RESULT_DATA";
    private static final String FIELD63PART1  = "63.1";
    private static final String LABEL_OPERATION = "LABEL_OPERATION";
    private static final List<String> MTI_INPUT = List.of("0100", "0120", "0400", "0420");
    private static final List<String> MTI_OUTPUT = List.of("0110", "0130", "0410", "0430");

    private ProcessMonitoring() {
    }

    public static MonitoringDTO createMonitoring(ISO8583 inputObject, FieldDto fieldDto) {
        MonitoringDTO monitoring = MonitoringDTO.builder().build();

        if (inputObject.getMessageType().startsWith(CODE_08) ||
                inputObject.getMessageType().startsWith(CODE_019)) {
            return monitoring;
        }

        String merchantName = "";
        String networkName = fieldDto.getNetworkName().toUpperCase();
        String binCode = inputObject.getPrimaryAccountNumber();
        String envAcceptorNameAndLocation = inputObject.getCardAcceptorNameLocation();
        String merchantCategory = inputObject.getMerchantType() != null ? inputObject.getMerchantType() : "9999";
        String processingCode = fieldDto.getProcessingCode();
        String envAcquirerId = inputObject.getAcquiringInstitutionIdentificationCode();
        String envAcceptorId  = inputObject.getCardAcceptorIdentificationCode();

        if (MTI_INPUT.contains(inputObject.getMessageType())) {

            binCode = extractBinCode(binCode);
            merchantName = extractMerchantName(envAcceptorNameAndLocation);

            long endDate = Instant.now().toEpochMilli();
            String currentTime = String.valueOf(endDate);
            monitoring.setStartDateMs(currentTime);

            monitoring.setBinCode(binCode);
            monitoring.setBinDescription(getMappedCode(LIST_BIN, binCode, networkName));
            monitoring.setMerchantNameAceptor(merchantName.trim());
            monitoring.setMerchantCategoryDescription(getMappedCode(MERCHANT_CATEGORY_DES, merchantCategory, networkName));
            monitoring.setChannelFilter(channelFilterDescription(fieldDto.getECommerceIndicator(), fieldDto.getTerminalKey(), networkName));
            monitoring.setOperationFilter(getMappedCode(OPERATION_FILTER, processingCode, networkName));
            monitoring.setTransactionTypeDescription(getMappedCode(TRANSACTION_TYPE_DES, processingCode, networkName));

            if(networkName.equalsIgnoreCase(NETWORK_PEER01)) {
                monitoring.setP2pType(envAcquirerId.equals(CODE_420829) ? envAcceptorNameAndLocation.substring(0, 4) : null);
                monitoring.setOriginBankCode(envAcceptorId);
                monitoring.setOriginBankDescription(getMappedCode(WALLET, envAcceptorId, networkName));
            }
        } else{
            if (MTI_OUTPUT.contains(inputObject.getMessageType())) {
                monitoring.setTransactionStatus(CODE_00.equals(inputObject.getResponseCode()) ? APPROVED : DENIED);
            }else{
                LOGGER.info("Message type not allowed: {}", inputObject.getMessageType());
                return monitoring;
            }
        }
        return monitoring;
    }

    private static String extractBinCode(String binCode) {
        String resultBinCode = "";
        try{
            resultBinCode = binCode.substring(0, 6);
        }catch (Exception e){
            LOGGER.info("Error getting the binCode from the field PAN {} - {}  ", binCode, e.getMessage());
        }
        return resultBinCode;
    }

    private static String extractMerchantName(String nameLocation) {
        String resultNameLocation = "";
        try{
            resultNameLocation = nameLocation.substring(0,22);
        }catch (Exception e){
            LOGGER.info("Error getting AcceptorName {} - {} ", nameLocation, e.getMessage());
        }
        return resultNameLocation;
    }

    /**
     * Obtiene un código mapeado y lo reemplaza por UNKNOWN si es igual al código original.
     */
    private static String getMappedCode(String type, String code, String networkName) {
        return Optional.ofNullable(FieldLocalCodeMapper.getCode(type, "in", code, networkName))
                .filter(mappedCode -> !mappedCode.equals(code))
                .orElse(UNKNOWN);
    }

    public static String additionalInfoValue(String responseCode){
        return CODE_00.equals(responseCode) ? APPROVED : DENIED;
    }

    public static String channelFilterDescription(Boolean eCommerceIndicator, String terminalKey, String networkName){
        if (Boolean.TRUE.equals(eCommerceIndicator)) {
            return FieldLocalCodeMapper.getCode(CHANNEL_DESCRIPTION, "in", "ECOMMER", networkName);
        }
        else {
            return Optional.ofNullable(FieldLocalCodeMapper.getCode(CHANNEL_DESCRIPTION, "in", terminalKey, networkName))
                    .filter(mappedCode -> !mappedCode.equals(terminalKey))
                    .orElse(OTHERS);
        }
    }

    public static String labelOperationDes(String responseCode, String messageType, String network){

        if (messageType.startsWith(CODE_08)) {
            return "";
        }

        String labelOperationStr=FieldLocalCodeMapper.getCode(LABEL_OPERATION, "in", responseCode, network);

        if (labelOperationStr.equals(responseCode)) {
            return INTERNAL_FAILURE;
        }

        return labelOperationStr;
    }

    public static String createTransactionReference(ISO8583 inputObject, Map<String, String> subFields, String networkName){
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

    public static String operationTypeValue(String messageType, String transactionType){

        return messageType.startsWith(CODE_08) ? ""
                : FieldLocalCodeMapper.getCode(OPERATION_FILTER, "in", transactionType, GrpcHeadersInfo.getNetwork());
    }

    public static String channelValue(String messageType, Boolean eCommerceIndicatorString, String terminalKey){

        return messageType.startsWith(CODE_08) ? ""
                : channelFilterDescription(eCommerceIndicatorString, terminalKey, GrpcHeadersInfo.getNetwork());

    }

    public static String resultDataValue(String messageType){

        return messageType.startsWith(CODE_08) ? ""
                : FieldLocalCodeMapper.getCode(RESULT_DATA, "in", messageType, GrpcHeadersInfo.getNetwork());

    }
}