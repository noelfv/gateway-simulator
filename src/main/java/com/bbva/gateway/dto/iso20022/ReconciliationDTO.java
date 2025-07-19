package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReconciliationDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -6880450600789176151L;
	private String date;
}
