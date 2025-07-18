package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InitiatingPartyDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2536780227954971205L;
    private String id;
}
