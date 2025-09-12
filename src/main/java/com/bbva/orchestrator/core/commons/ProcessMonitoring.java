package com.bbva.orchestrator.core.commons;

import com.bbva.gateway.interceptors.GrpcHeadersInfo;
import com.bbva.orchestrator.validations.FieldLocalCodeMapper;


public class ProcessMonitoring {

    private static final String CODE_08 = "08";
    private static final String CODE_00 = "00";
    private static final String INTERNAL_FAILURE = "internal_failure";
    private static final String APPROVED = "Approved";
    private static final String DENIED = "Denied";
    private static final String RESULT_DATA = "RESULT_DATA";
    private static final String LABEL_OPERATION = "LABEL_OPERATION";

    private ProcessMonitoring() {
    }

    public static String additionalInfoValue(String responseCode){
        return CODE_00.equals(responseCode) ? APPROVED : DENIED;
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

    public static String resultDataValue(String messageType){

        return messageType.startsWith(CODE_08) ? ""
                : FieldLocalCodeMapper.getCode(RESULT_DATA, "in", messageType, GrpcHeadersInfo.getNetwork());

    }
}