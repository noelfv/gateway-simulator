package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionAmountDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 159397134023974048L;
	private Double amount;
	private String currency;
}
