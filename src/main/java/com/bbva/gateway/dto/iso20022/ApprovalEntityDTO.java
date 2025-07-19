package com.bbva.gateway.dto.iso20022;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
//@NoArgsConstructor
public class ApprovalEntityDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 5743749290124783248L;
    private String id;
}
