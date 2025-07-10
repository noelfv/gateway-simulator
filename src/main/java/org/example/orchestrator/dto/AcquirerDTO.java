package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class AcquirerDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 7267145667814699996L;
	private String id;
	private String acquirerInstitution;
	private String country;
	private AdditionalIdDTO additionalId;
}
