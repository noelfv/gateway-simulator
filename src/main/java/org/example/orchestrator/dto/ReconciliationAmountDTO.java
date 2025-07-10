package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;


@Builder
@Getter
@Setter
public class ReconciliationAmountDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -6264859555050322634L;
	private Double amount;
	private String currency;
	private Double effectiveExchangeRate;
	private String conversionDate;
}
