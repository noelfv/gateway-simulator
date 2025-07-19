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
public class AcceptorDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 7458559227954973155L;
	private String id;
	private String nameAndLocation;
	private LocalDataDTO localData;
	private List<AdditionalDataDTO> additionalData;
}
