package org.example.orchestrator.validations;


import lombok.Setter;
import org.example.orchestrator.configuration.model.In;
import org.example.orchestrator.configuration.model.LocalCodes;
import org.example.orchestrator.configuration.model.Out;

import java.util.List;
import java.util.stream.Stream;

public class FieldLocalCodeMapper {

    public static final String KEY_NOT_FOUND = "Key not found";

    private FieldLocalCodeMapper() {
    }

    @Setter
    private static List<LocalCodes> fieldLocalCodes;

    public static String getCode(String fieldName, String inputOutput, String fieldValue, String network) {
        if (fieldLocalCodes == null) {
            return fieldValue;
        }
        return Stream.of("DEFAULT", network)
                .map(net -> findCodeInNetwork(net, fieldName, inputOutput, fieldValue))
                .filter(result -> !KEY_NOT_FOUND.equals(result))
                .findFirst()
                .orElseGet(() -> {
                    System.out.println("The provided key: " + fieldValue + " was not found in the configuration: " + fieldName );
                    return fieldValue;
                });
    }

    private static String findCodeInNetwork(String network, String fieldName, String inputOutput, String fieldValue) {
        return fieldLocalCodes.stream()
                .filter(localValidation -> localValidation.getNetwork().equals(network)) // Buscar en la red especificada
                .findFirst()
                .map(networkConfig -> sortCode(fieldName, inputOutput, fieldValue, networkConfig)) // Lógica centralizada en sortCode
                .orElse(KEY_NOT_FOUND); // Si la red no existe, retornar "Key not found"
    }

    private static String sortCode(String fieldName, String inputOutput, String fieldValue, LocalCodes network) {
        return network.getFields().stream().filter(networkFields -> networkFields.getName().equals(fieldName)).findFirst()
                .map(field -> {
                    if (inputOutput.equals("in")) {
                        return field.getCodes().getIn().stream().filter(in -> in.getKey().equals(fieldValue)).findFirst().map(In::getValue).orElse(KEY_NOT_FOUND);
                    } else {
                        return field.getCodes().getOut().stream().filter(out -> out.getKey().equals(fieldValue)).findFirst().map(Out::getValue).orElse(KEY_NOT_FOUND);
                    }
                })
                .orElse(KEY_NOT_FOUND);
    }

}