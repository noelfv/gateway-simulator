package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class AdditionalCardDataDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4872910541967189302L;
    private String key;
    private String value;
}
