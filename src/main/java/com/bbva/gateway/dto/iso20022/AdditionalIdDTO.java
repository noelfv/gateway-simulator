package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalIdDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -8975284283319859064L;
	private String key;
	private String value;
}
