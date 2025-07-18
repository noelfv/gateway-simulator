package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
public class SummarySaleDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2873456091577902781L;
    private LoyaltyProgrammeDTO loyaltyProgramme;
    private List<AdjustmentDTO> adjustment;
}
