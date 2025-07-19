package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AmountDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 4759017545189123871L;

    private Double amount;
    private String currency;

}
