package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class RecipientDTO implements Serializable {
	@Serial
	private static final long serialVersionUID = -1228646320744172143L;
	private KEKDTO kek;
}
