package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountToDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -4412578926219366056L;
	private String accountId;
	private String accountType;
}
