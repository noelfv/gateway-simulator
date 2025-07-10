package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class OriginalDataElementsDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 2556722737037297482L;

	private String messageFunction;
	private String transactionReference;
	private String systemTraceAuditNumber;
	private String localDate;
	private String localTime;
	private String localDateTime;
	private String acquirerId;
	private String transmissionDateTime;
	private String senderIdentification;
	private String retrievalReferenceNumber;
}
