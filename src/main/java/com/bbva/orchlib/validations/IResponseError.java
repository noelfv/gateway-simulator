package com.bbva.orchlib.validations;

import com.bbva.gateway.dto.iso20022.ISO20022;

public interface IResponseError {

    void validateBinError(ISO20022 iso20022);
    void validateProcessingCodeError(ISO20022 iso20022);
    void validateCurrencyError(ISO20022 iso20022);
    void validateOperationDate(ISO20022 iso20022);
    void validateNonFinancialRequests(ISO20022 iso20022);

}
