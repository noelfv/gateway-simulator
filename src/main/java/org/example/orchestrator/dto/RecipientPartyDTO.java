package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class RecipientPartyDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1278930227954974291L;
    private String id;
}
