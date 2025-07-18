package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class SupplementaryDataDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 5459773484292403555L;
	private String placeAndName;
	private String envelope;
	private String key;
}
