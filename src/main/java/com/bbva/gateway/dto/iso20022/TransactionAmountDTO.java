package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;



@Builder
@Getter
@Setter
public class TransactionAmountDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 159397134023974048L;
	private Double amount;
	private String currency;
}
