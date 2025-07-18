package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class DeviceIdentificationDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1738901230413372346L;
    private String key;
    private String otherType;
    private String identification;
}
