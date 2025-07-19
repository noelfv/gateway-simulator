package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountFromDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -8406314572089074898L;
	private String accountId;
	private String accountType;
}
