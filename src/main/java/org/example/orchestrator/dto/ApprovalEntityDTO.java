package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class ApprovalEntityDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5743749290124783248L;
    private String id;
}
