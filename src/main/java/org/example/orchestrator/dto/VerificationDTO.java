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
public class VerificationDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -5871343842816319349L;
	private String key;
	private List<VerificationInformationDTO> verificationInformation;
	private List<VerificationResultDTO> verificationResult;
}
