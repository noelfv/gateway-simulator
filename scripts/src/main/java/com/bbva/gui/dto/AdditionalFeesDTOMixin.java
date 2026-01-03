package com.bbva.gui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class AdditionalFeesDTOMixin {
    @JsonCreator
    public AdditionalFeesDTOMixin(
            @JsonProperty("feeAmount") FeeAmountDTO feeAmount,
            @JsonProperty("feeReconciliationAmount") FeeReconciliationAmountDTO feeReconciliationAmount,
            @JsonProperty("key") String key,
            @JsonProperty("otherType") String otherType)
    {

    }
}