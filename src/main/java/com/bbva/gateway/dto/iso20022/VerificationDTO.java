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
public class VerificationDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -5871343842816319349L;
	private String key;
	private List<VerificationInformationDTO> verificationInformation;
	private List<VerificationResultDTO> verificationResult;
}
