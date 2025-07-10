package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
public class AddendumDataDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1735483043148172398L;
    private List<AdditionalDataDTO> additionalData;
    private InvoiceDTO invoice;
    private SaleDTO sale;
}
