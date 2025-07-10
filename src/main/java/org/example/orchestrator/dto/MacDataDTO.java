package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class MacDataDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 4527114745222170160L;
	private String algorithm;
	private String derivedInformation;
	private String keyProtection;
	private String keyIndex;
	private String paddingMethod;
}
