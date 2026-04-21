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
public class PINDataDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -6888080103792637851L;
	private String control;
	private String keySetIdentifier;
	private String derivedInformation;
	private String algorithm;
	private String keyLength;
	private String keyProtection;
	private String keyIndex;
	private String pinBlockFormat;
	private String encryptedPINBlock;
}
