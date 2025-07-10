package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class ResultDetailsDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -7162090062932181901L;
    private String key;
    private String value;
}
