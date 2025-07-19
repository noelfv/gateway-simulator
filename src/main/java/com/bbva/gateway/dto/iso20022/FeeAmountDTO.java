package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeeAmountDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 4207074094478395801L;
	private Double amount;
	private String currency;
}
