package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleContextDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 6070083698145881441L;
	private List<AdditionalDataDTO> additionalData;
}
