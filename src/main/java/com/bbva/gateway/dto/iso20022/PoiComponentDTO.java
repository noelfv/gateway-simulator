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
public class PoiComponentDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -4873103792178231L;
    private IdentificationDTO identification;
    private List<AssessmentDTO> assessment;

}
