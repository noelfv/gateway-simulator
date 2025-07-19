package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SupplementaryDataDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 5459773484292403555L;
	private String placeAndName;
	private String envelope;
	private String key;
}
