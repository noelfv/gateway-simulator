package com.bbva.gui.dto;

import com.bbva.gateway.dto.iso20022.SettlementServiceDatesDTO;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SettlementServiceDatesDTOMixin {
    @JsonCreator
    public SettlementServiceDatesDTOMixin(
            @JsonProperty("settlementDate") String settlementDate)
    {

    }
}