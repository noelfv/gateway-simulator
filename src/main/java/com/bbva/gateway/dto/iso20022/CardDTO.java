package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
