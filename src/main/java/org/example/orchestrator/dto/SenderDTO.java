package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class SenderDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -7729401461257923934L;
	private String id;
	private AdditionalIdDTO additionalId;
	private LocalDataSenderDTO localData;
}
