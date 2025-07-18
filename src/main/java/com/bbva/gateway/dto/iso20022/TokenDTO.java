package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class TokenDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4362892030421851280L;

    private String paymentToken;

}
