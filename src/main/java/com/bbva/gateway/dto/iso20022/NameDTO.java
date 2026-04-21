package com.bbva.gateway.dto.iso20022;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NameDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 2547891384228739843L;

    private String name;
    private String givenName;
    private String middleName;
    private String lastName;
}
