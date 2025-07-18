package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TerminalIdDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -2880967115181231565L;
	private String id;
	private String assigner;
	private String country;
}
