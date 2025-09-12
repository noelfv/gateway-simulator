package com.bbva.gui.dto;

import com.bbva.gateway.dto.iso20022.AdditionalCardDataDTO;
import com.bbva.gateway.dto.iso20022.FeeReconciliationAmountDTO;
import com.bbva.gateway.dto.iso20022.Track2DTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class FeeReconciliationAmountDTOMixin {
    @JsonCreator
    public FeeReconciliationAmountDTOMixin(
            @JsonProperty("amount") Double amount)
    {

    }
}