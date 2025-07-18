package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalFeesDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 8411586545189133973L;

	private FeeAmountDTO feeAmount;
	private FeeReconciliationAmountDTO feeReconciliationAmount;
	private String key;
	private String otherType;
}
