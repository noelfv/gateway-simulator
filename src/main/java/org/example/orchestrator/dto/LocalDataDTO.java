package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class LocalDataDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7473210003792631209L;
    private AddressDTO address;
}
