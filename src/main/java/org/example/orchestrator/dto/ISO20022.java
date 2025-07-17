package org.example.orchestrator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.example.gui.utils.MappingMetadata;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class ISO20022 implements Serializable {
	@Serial
	private static final long serialVersionUID = 8509839842943411890L;

	private String networkName;
	private String messageFunction;
	private String socketPort;
	private String exchangeIdentification;
	private String protocolVersion;
	private List<TraceDataDTO> traceData;
	private TransactionDTO transaction;
	private EnvironmentDTO environment;
	private ContextDTO context;
	private ProcessingResultDTO processingResult;
	private SecurityTrailerDTO securityTrailer;
	private String iccRelatedData;
	private List<ProtectedDataDTO> protectedData;
	private List<SupplementaryDataDTO> supplementaryData;
	private InitiatingPartyDTO initiatingParty;
	private RecipientPartyDTO recipientParty;
	private Boolean isSimulation;
	private AddendumDataDTO addendumData;
	private MonitoringDTO monitoring;
	private String socketIp;
	private MappingMetadata mappingMetadata;
}
