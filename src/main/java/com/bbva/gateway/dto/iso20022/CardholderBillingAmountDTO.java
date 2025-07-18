package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardholderBillingAmountDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 3499965191409678444L;
	private Double amount;
	private String currency;
	private Double effectiveExchangeRate;
}
