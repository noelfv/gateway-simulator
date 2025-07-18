package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalIdentificationDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -2738456329054023784L;

    private String key;
    private String value;
}
