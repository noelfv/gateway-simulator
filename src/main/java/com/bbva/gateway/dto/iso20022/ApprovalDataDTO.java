package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class ApprovalDataDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 8675309867530954783L;
    private ApprovalEntityDTO approvalEntity;
}
