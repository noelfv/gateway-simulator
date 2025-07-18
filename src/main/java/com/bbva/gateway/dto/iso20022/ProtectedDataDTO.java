package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class ProtectedDataDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 2624480204312309912L;
	private EnvelopedDataDTO envelopedData;
}
