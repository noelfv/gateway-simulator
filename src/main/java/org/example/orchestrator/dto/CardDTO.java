package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
public class CardDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -568262050950535113L;
	private String pan;
	private String effectiveDate;
	private String expiryDate;
	private String cardSequenceNumber;
	private String track1;
	private Track2DTO track2;
	private String track3;
	private String serviceCode;
	private List<AdditionalCardDataDTO> additionalCardData;
}
