package com.bbva.orchestrator.parser.refactor.utils;

import com.bbva.gateway.dto.iso20022.EnvironmentDTO;
import com.bbva.gateway.dto.iso20022.MonitoringDTO;
import com.bbva.gateway.dto.iso20022.TransactionDTO;
import com.bbva.orchestrator.parser.iso8583.ISO8583;
import org.springframework.stereotype.Component;

@Component
public class MonitoringService {
    public MonitoringDTO createMonitoring(ISO8583 input, EnvironmentDTO environment, TransactionDTO transaction) {
        return null;
    }
}
