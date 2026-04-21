package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
	private String pinLengthCapability;
	private List<CardReadingCapabilityDTO> cardReadingCapabilities;
	private List<CardholderVerificationCapabilityDTO> cardholderVerificationCapabilities;
	private List<CardWritingCapabilityDTO> cardWritingCapabilities;
}