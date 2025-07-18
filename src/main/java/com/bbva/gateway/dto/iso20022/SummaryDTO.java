package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SummaryDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -3745107859971723902L;
    private String invoiceNumber;
    private String additionalData;
}
