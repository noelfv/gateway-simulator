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
public class AcceptorDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 7458559227954973155L;
	private String id;
	private String nameAndLocation;
	private LocalDataDTO localData;
	private List<AdditionalDataDTO> additionalData;
}
