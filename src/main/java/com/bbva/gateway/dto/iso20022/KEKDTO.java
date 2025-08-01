package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KEKDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 4701607091577150276L;
	private KEKIdDTO kekId;
}
