package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AcquirerDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 7267145667814699996L;
	private String id;
	private String acquirerInstitution;
	private String country;
	private AdditionalIdDTO additionalId;
}
