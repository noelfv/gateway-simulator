package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class AssessmentDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = -5748374029054139832L;
    private String itemNumber;
    private String id;
    private String providerIdentification;
}
