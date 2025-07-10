package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class AdditionalInformationDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8462540867530952914L;
    private String key;
    private String value;
}
