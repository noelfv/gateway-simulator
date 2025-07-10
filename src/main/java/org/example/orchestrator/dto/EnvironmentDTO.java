package org.example.orchestrator.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Getter
@Setter
public class EnvironmentDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -2950392030413757520L;

	private CardDTO card;
	private TerminalDTO terminal;
	private AcquirerDTO acquirer;
	private SenderDTO sender;
	private AcceptorDTO acceptor;
	private IssuerDTO issuer;
	private CardholderDTO cardholder;
	private ReceiverDTO receiver;
	private String atmManagerIdentification;
	private TokenDTO token;
	private CustomerDeviceDTO customerDevice;
}
