package com.bbva.orchestrator.core.utils;

import com.bbva.orchestrator.core.builders.ISO8583;
import org.springframework.stereotype.Component;

@Component
public class MonitoringService {
    public MonitoringDTO insertMonitoring(ISO8583 input, EnvironmentDTO environment, TransactionDTO transaction) {
        return null;
    }

    public MonitoringDTO updateMonitoring(ISO8583 input) {
        //Actualizar el monitoring el campo status y podria ser la duration
        return null;
    }
}
