package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class ValueDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 2672629896177244995L;
	private PINDataDTO pinData;
	private String textValue;
}
