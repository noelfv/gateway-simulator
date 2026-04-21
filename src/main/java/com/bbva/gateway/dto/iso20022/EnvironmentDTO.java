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
	private PayerDTO payer;
	private PayeeDTO payee;
}
