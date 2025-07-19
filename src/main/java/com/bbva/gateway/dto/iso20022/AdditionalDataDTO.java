package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalDataDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 2283261546567688760L;
	private String key;
	private String value;
}
