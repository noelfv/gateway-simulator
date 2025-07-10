package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class AdditionalFeesDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 8411586545189133973L;

	private FeeAmountDTO feeAmount;
	private FeeReconciliationAmountDTO feeReconciliationAmount;
	private String key;
	private String otherType;
}
