package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class DetailedRequestedAmountDTO implements Serializable  {
    @Serial
    private static final long serialVersionUID = -3847189021979354189L;
    private String amountToDispense;
}
