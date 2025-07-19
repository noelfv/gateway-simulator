package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IdentificationDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1738569829052347659L;

    private String itemNumber;
    private String id;
    private String providerIdentification;
    private String key;
    private String serialNumber;
}
