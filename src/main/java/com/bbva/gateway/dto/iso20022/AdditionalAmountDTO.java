package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalAmountDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -3994104285748714371L;
	private String key;
	private AmountDTO amount;
	private String description;
}
