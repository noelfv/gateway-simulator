package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;


@Builder
@Getter
@Setter
public class FeeAmountDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 4207074094478395801L;
	private Double amount;
	private String currency;
}
