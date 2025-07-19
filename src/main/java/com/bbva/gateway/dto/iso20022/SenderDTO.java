package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SenderDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -7729401461257923934L;
	private String id;
	private AdditionalIdDTO additionalId;
	private LocalDataSenderDTO localData;
}
