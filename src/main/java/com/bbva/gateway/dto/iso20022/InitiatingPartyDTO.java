package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class InitiatingPartyDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2536780227954971205L;
    private String id;
}
