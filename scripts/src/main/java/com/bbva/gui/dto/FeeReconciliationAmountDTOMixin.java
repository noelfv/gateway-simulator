package com.bbva.gui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class FeeReconciliationAmountDTOMixin {
    @JsonCreator
    public FeeReconciliationAmountDTOMixin(
            @JsonProperty("amount") Double amount)
    {

    }
}