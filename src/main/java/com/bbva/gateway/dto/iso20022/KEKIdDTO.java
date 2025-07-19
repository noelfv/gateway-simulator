package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KEKIdDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 7782665061142898333L;
	private String keyId;
}
