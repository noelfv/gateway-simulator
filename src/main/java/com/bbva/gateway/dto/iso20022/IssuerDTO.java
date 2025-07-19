package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IssuerDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 5337983214850841812L;
	private String assigner;
	private AdditionalIdentificationDTO additionalIdentification;
}
