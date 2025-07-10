package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class InvoiceDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1823648909971278402L;
    private SummaryDTO summary;
}
