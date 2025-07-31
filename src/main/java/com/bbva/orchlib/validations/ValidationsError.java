package com.bbva.orchlib.validations;

import com.bbva.gateway.dto.iso20022.ISO20022;


public class ValidationsError implements IResponseError {
    private static final String ERROR_RESULT = "Error";
    public static ISO20022 validationsErr(ISO20022 iso20022, String validationName) {
        ValidationsError instance = new ValidationsError();
        //Convert.mapProcessingResult(iso20022, null, null, null, iso20022.getProcessingResult() != null && iso20022.getProcessingResult().getResultData() != null ? null : ResultDataDTO.builder().build(), null);
        switch (validationName) {
            case "validateBin" -> instance.validateBinError(iso20022);
            case "validateProcessingCode" -> instance.validateProcessingCodeError(iso20022);
            case "validateCurrency" -> instance.validateCurrencyError(iso20022);
            case "validateOperationDate" -> instance.validateOperationDate(iso20022);
            case "validateNonFinancialRequests" -> instance.validateNonFinancialRequests(iso20022);
            default -> iso20022.getProcessingResult().getResultData().setResult(ERROR_RESULT);
        }

        return iso20022;
    }


    @Override
    public void validateBinError(ISO20022 iso20022){
        iso20022.getProcessingResult().getResultData().setOtherResult("gw_invalid_bin");
    }

    @Override
    public void validateProcessingCodeError(ISO20022 iso20022){
    	iso20022.getProcessingResult().getResultData().setOtherResult("gw_invalid_processing_code");
    }

    @Override
    public void validateCurrencyError(ISO20022 iso20022){
    	iso20022.getProcessingResult().getResultData().setOtherResult("gw_invalid_currency");
    }

    @Override
    public void validateOperationDate(ISO20022 iso20022) {
    	iso20022.getProcessingResult().getResultData().setOtherResult("gw_invalid_operation_date");
    }

    @Override
    public void validateNonFinancialRequests(ISO20022 iso20022) {
    	iso20022.getProcessingResult().getResultData().setOtherResult("gw_invalid_non_financial_request");
    }
}
