package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
public class EnvelopedDataDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -2748355606920098448L;
	private List<RecipientDTO> recipient;
}
