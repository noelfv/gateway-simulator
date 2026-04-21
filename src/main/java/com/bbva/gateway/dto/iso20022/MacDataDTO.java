package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MacDataDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 4527114745222170160L;
	private String algorithm;
	private String derivedInformation;
	private String keyProtection;
	private String keyIndex;
	private String paddingMethod;
}
