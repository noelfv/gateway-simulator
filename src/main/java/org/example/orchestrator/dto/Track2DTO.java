package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class Track2DTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2845377881979354333L;
    private String textValue;
}
