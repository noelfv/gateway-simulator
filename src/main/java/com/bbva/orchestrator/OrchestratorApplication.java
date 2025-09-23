package com.bbva.orchestrator;

import org.springframework.boot.SpringApplication;
/*
@SpringBootApplication
@ConfigurationPropertiesScan
@ComponentScan(basePackages = {"com.bbva.orchlib", "com.bbva.orchestrator", "com.bbva.gateway"},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {DialogControlHandler.class, IDialogControl.class, GrpcDialogControlService.class}))
*/
public class OrchestratorApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrchestratorApplication.class, args);
    }

}



