package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class LoyaltyProgrammeDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1892387891577128935L;
    private String programmeType;
    private String programmeId;
}
