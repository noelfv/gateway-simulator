package com.bbva.gui.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class CardDTOMixin {
    @JsonCreator
    public CardDTOMixin(
            @JsonProperty("pan") String pan,
            @JsonProperty("effectiveDate") String effectiveDate,
            @JsonProperty("expiryDate") String expiryDate,
            @JsonProperty("cardSequenceNumber") String cardSequenceNumber,
            @JsonProperty("track1") String track1,
            @JsonProperty("track2")Track2DTO track2,
            @JsonProperty("track3") String track3,
            @JsonProperty("serviceCode") String serviceCode,
            @JsonProperty("additionalCardData") AdditionalCardDataDTO additionalCardData)
    {

    }
}