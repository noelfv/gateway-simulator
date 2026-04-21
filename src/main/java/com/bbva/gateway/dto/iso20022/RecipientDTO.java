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
public class RecipientDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -1228646320744172143L;
	private KEKDTO kek;
}
