package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class AmountDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4759017545189123871L;

    private Double amount;
    private String currency;

}
