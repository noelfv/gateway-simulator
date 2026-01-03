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
public class RiskContextDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1872430061102278342L;
    private List<RiskAssessmentDTO> riskAssessment;
}
