package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class AdjustmentDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1892763191577687256L;
    private String key;
    private String additionalType;
    private Double adjustmentAmount;
}
