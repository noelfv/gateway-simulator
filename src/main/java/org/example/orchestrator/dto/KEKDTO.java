package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class KEKDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 4701607091577150276L;
	private KEKIdDTO kekId;
}
