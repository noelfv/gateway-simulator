package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class ReconciliationDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -6880450600789176151L;
	private String date;
}
