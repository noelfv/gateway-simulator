package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class VerificationInformationDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -8361434062932119122L;
	private String key;
	private ValueDTO value;
}
