package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class TransactionIdDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 5028387681145858534L;
	private String transactionReference;
	private String transmissionDateTime;
	private String systemTraceAuditNumber;
	private String localDate;
	private String localTime;
	private String localDateTime;
	private String acquirerReferenceData;
	private String retrievalReferenceNumber;
	private OriginalDataElementsDTO originalDataElements;
	private String cardIssuerReferenceData;
}
