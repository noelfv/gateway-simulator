package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VerificationInformationDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -8361434062932119122L;
	private String key;
	private ValueDTO value;
}
