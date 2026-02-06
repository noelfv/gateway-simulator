package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SpecialProgrammeQualificationDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = -1725360859971726371L;

    private List<DetailDTO> detail;
}
