package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class IdentificationDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1738569829052347659L;

    private String itemNumber;
    private String id;
    private String providerIdentification;
    private String key;
    private String serialNumber;
}
