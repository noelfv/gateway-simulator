package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class AdditionalDataDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 2283261546567688760L;
	private String key;
	private String value;
}
