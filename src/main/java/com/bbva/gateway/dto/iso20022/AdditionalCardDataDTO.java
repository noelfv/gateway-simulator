package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalCardDataDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4872910541967189302L;
    private String key;
    private String value;
}
