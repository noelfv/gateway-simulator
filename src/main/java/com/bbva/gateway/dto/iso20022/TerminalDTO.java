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
public class TerminalDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -6439494329054012327L;
	private CapabilitiesDTO capabilities;
	private TerminalIdDTO terminalId;
	private String key;
	private String otherType;
	private String geographicLocation;
	private List<PoiComponentDTO> poiComponent;
	private Boolean offPremisesIndicator;
}
