package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class SummaryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3745107859971723902L;
    private String invoiceNumber;
    private String additionalData;
}
