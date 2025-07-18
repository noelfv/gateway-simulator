package com.bbva.gateway.dto.iso20022;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecipientDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -1228646320744172143L;
	private KEKDTO kek;
}
