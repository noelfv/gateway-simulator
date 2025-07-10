package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class SecurityTrailerDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 622908895456134738L;
	private MacDataDTO macData;
	private String mac;
}
