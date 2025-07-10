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
public class VerificationResultDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -8634189062932156189L;
    private String key;
    private String entity;
    private String otherEntity;
    private String otherResult;
    private List<ResultDetailsDTO> resultDetails;
}
