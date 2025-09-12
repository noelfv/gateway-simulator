package com.bbva.gui.dto;

import com.bbva.gateway.dto.iso20022.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public abstract class TransactionDTOMixin {
    @JsonCreator
    public TransactionDTOMixin(
            @JsonProperty("transactionId") TransactionIdDTO transactionId,
            @JsonProperty("additionalFee") List<AdditionalFeesDTO> additionalFee,
            @JsonProperty("originalAdditionalFee") List<OriginalAdditionalFeesDTO> originalAdditionalFee,
            @JsonProperty("additionalAmount") List<AdditionalAmountDTO> additionalAmount,
            @JsonProperty("additionalData") List<AdditionalDataDTO> additionalData,
            @JsonProperty("transactionSubtype") String transactionSubtype)
    {

    }
}