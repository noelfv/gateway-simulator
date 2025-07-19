package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DetailedRequestedAmountDTO implements Serializable  {
    @Serial
    private static final long serialVersionUID = -3847189021979354189L;
    private String amountToDispense;
}
