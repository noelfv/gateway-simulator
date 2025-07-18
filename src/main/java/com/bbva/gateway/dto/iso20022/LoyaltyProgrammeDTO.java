package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoyaltyProgrammeDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1892387891577128935L;
    private String programmeType;
    private String programmeId;
}
