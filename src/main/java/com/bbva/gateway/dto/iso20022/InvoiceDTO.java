package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1823648909971278402L;
    private SummaryDTO summary;
}
