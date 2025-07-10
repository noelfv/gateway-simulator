package org.example.orchestrator.processes;



import org.example.orchestrator.dto.ActionDTO;
import org.example.orchestrator.dto.AdditionalInformationDTO;
import org.example.orchestrator.dto.ProcessingResultDTO;
import org.example.orchestrator.dto.ResultDataDTO;
import org.example.orchestrator.iso8583.ISO8583;

import java.util.ArrayList;
import java.util.List;

public class ProcessingResult {
    private static final List<String> MTI_OUTPUT = List.of("0110", "0130", "0410", "0430" , "0312");
    private ProcessingResult() {
    }
    public static ProcessingResultDTO createProcessingResult(ISO8583 inputObject) {
        // Initialize variables that will be filled depending on the condition
        String result = "";
        String otherResult = "";
        String additionalValue = "";

        // Create action (common for both cases)
        ActionDTO action = ActionDTO.builder()
                .actionType("")
                .build();

        List<ActionDTO> actionList = new ArrayList<>();
        actionList.add(action);

        if (MTI_OUTPUT.contains(inputObject.getMessageType())) {
            result = ProcessMonitoring.resultDataValue(inputObject.getMessageType());
            otherResult = ProcessMonitoring.labelOperationDes(inputObject.getResponseCode(), inputObject.getMessageType(), "PEER02");
            additionalValue = ProcessMonitoring.additionalInfoValue(inputObject.getResponseCode());
        }

        ResultDataDTO resultData = ResultDataDTO.builder()
                // ======== FIELD 39 (RESPONSE CODE) ========
                .result(result)
                .otherResult(otherResult)
                .otherResultDetails(inputObject.getResponseCode())
                // ======== FIELD 44 (ADDITIONAL RESPONSE DATA) ========
                // additionalResultInformation (PENDING)
                .build();

        AdditionalInformationDTO additionalInformation = AdditionalInformationDTO.builder()
                .key("transaction")
                .value(additionalValue)
                .build();

        List<AdditionalInformationDTO> additionalInformationList = new ArrayList<>();
        additionalInformationList.add(additionalInformation);

        return ProcessingResultDTO.builder()
                .action(actionList)
                .resultData(resultData)
                .additionalInformation(additionalInformationList)
                // ======== FIELD 38 (AUTHORIZATION IDENTIFICATION RESPONSE) ========
                .approvalCode(inputObject.getAuthorizationIdentificationResponse())
                .build();
    }

}
