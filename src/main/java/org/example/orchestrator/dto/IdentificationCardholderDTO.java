package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class IdentificationCardholderDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2837191829052318932L;
    private String identificationValue;

}
