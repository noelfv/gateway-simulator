package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class AccountToDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -4412578926219366056L;
	private String accountId;
	private String accountType;
}
