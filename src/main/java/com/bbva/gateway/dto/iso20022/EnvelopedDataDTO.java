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
public class EnvelopedDataDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -2748355606920098448L;
	private List<RecipientDTO> recipient;
}
