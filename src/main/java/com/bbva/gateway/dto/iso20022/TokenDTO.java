package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -4362892030421851280L;

    private String paymentToken;

}
