package com.bbva.orchestrator.configuration;

import com.bbva.orchestrator.configuration.model.LocalCodes;
import com.bbva.orchestrator.validations.BitMapVerificationFields;
import com.bbva.orchestrator.validations.FieldLocalCodeMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Configuration
@PropertySource("classpath:application-validations.yml")
@ConfigurationProperties()
@Getter
@Setter
public class ParserValidationsDataLoad {
    private Map<String, List<Integer>> messageTypeFields;
    private List<LocalCodes> fieldsCodes;

    @PostConstruct
    public void setParserValidations() {
        BitMapVerificationFields.setParserValidations(messageTypeFields);
        FieldLocalCodeMapper.setFieldLocalCodes(fieldsCodes);}
}