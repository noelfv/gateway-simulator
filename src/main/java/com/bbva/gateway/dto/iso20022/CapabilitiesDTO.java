package com.bbva.gateway.dto.iso20022;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CapabilitiesDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = 2235472328747913765L;
	private String approvalCodeLength;
	private Boolean cardCaptureCapable;
	@JsonProperty("pINLengthCapability")
	private String pinLengthCapability;
	private List<CardReadingCapabilityDTO> cardReadingCapabilities;
	private List<CardholderVerificationCapabilityDTO> cardholderVerificationCapabilities;
	private List<CardWritingCapabilityDTO> cardWritingCapabilities;
}