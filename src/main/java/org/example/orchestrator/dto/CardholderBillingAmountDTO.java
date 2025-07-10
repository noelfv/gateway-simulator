package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class CardholderBillingAmountDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 3499965191409678444L;
	private Double amount;
	private String currency;
	private Double effectiveExchangeRate;
}
