package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class KEKIdDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 7782665061142898333L;
	private String keyId;
}
