package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
