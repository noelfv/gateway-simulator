package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class CustomerDeviceDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -1736489030413726381L;

    private DeviceIdentificationDTO deviceIdentification;
}
