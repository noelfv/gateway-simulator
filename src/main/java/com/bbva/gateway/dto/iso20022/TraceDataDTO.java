package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TraceDataDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -5370792481979354965L;
	private String key;
	private String value;

}
