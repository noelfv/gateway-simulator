package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class OriginalAdditionalFeesDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -4007896011539469093L;
	private FeeAmountDTO feeAmount;
	private FeeReconciliationAmountDTO feeReconciliationAmount;
	private String key;
	private String otherType;
}
