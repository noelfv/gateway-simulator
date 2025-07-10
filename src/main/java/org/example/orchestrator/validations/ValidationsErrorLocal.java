package org.example.orchestrator.validations;


import org.example.orchestrator.dto.ISO20022;

public class ValidationsErrorLocal {
//public class ValidationsErrorLocal implements IValidationsLocalErr {
    private static final String ERROR_RESULT = "Error";

    public ISO20022 validationsLocalErr(ISO20022 iso20022, String validationName) {
        iso20022.getProcessingResult().getResultData().setResult(ERROR_RESULT);
        return iso20022;
    }

}
