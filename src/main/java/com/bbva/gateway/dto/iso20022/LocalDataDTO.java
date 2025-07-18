package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocalDataDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7473210003792631209L;
    private AddressDTO address;
}
