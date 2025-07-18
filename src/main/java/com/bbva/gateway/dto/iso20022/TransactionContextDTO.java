package com.bbva.gateway.dto.iso20022;

//import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
public class TransactionContextDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -885513621696783344L;
	private SettlementServiceDTO settlementService;
	private String captureDate;
	private String merchantCategoryCode;
	private String merchantCategorySpecificData;
	private ReconciliationDTO reconciliation;
	private String transactionInitiator;
	//@JsonProperty("iCCFallbackIndicator")
	private String iccFallbackIndicator;
	private String magneticStripeFallbackIndicator;
	private Boolean reSubmissionIndicator;
	private List<AdditionalDataDTO> additionalData;
}
