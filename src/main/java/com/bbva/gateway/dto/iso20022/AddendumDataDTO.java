package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddendumDataDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1735483043148172398L;
    private List<AdditionalDataDTO> additionalData;
    private InvoiceDTO invoice;
    private SaleDTO sale;
}
