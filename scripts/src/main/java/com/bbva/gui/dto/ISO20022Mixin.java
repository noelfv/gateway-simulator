package com.bbva.gui.dto;


import com.bbva.gateway.dto.iso20022.*;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public abstract class ISO20022Mixin {

    @JsonCreator
    public ISO20022Mixin(
            @JsonProperty("socketPort") String socketPort,
            @JsonProperty("networkName") String networkName,
            @JsonProperty("messageFunction") String messageFunction,
            @JsonProperty("exchangeIdentification") String exchangeIdentification, // Usa Object si no conoces el tipo exacto
            @JsonProperty("protocolVersion") String protocolVersion,
            @JsonProperty("traceData") List<TraceDataDTO> traceData,
            @JsonProperty("transaction") TransactionDTO transaction,
            @JsonProperty("environment") EnvironmentDTO environment,
            @JsonProperty("context") ContextDTO context,
            @JsonProperty("processingResult") ProcessingResultDTO processingResult,
            @JsonProperty("securityTrailer") SecurityTrailerDTO securityTrailer,
            @JsonProperty("iccRelatedData") String iccRelatedData,
            @JsonProperty("protectedData") List<ProtectedDataDTO> protectedData,
            @JsonProperty("supplementaryData") List<SupplementaryDataDTO> supplementaryData,
            @JsonProperty("initiatingParty") InitiatingPartyDTO initiatingParty,
            @JsonProperty("recipientParty") RecipientPartyDTO recipientParty,
            @JsonProperty("isSimulation") Boolean isSimulation,
            @JsonProperty("monitoring") InitiatingPartyDTO monitoring,
            @JsonProperty("socketIp") String socketIp,
            @JsonProperty("socketClusterPort") String socketClusterPort,
            @JsonProperty("customDataLocal") CustomDataLocalDTO customDataLocal,
            @JsonProperty("addendumData") MonitoringDTO addendumData) {
    }
}
