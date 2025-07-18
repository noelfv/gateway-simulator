package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FeeReconciliationAmountDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 8807005266288711992L;
	private Double amount;
}
