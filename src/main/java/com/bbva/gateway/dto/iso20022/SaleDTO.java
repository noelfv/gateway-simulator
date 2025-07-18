package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class SaleDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2893746591577928933L;
    private SummarySaleDTO summary;
}
