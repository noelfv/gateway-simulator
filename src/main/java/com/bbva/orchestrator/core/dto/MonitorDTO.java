package com.bbva.orchestrator.core.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonitorDTO {

    private String networkName;
    private Boolean eCommerceIndicator;
    private String terminalKey;
    private String processingCode;

}
