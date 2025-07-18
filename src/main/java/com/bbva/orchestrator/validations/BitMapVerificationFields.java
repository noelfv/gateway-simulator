package com.bbva.orchestrator.validations;

import lombok.Setter;
import java.util.List;
import java.util.Map;

public class BitMapVerificationFields {

    @Setter
    private static Map<String, List<Integer>> parserValidations;

    private BitMapVerificationFields() {
    }

    public static boolean checkRequiredFields(String messageType, String binaryBitMap) {
        System.out.println( "[checkRequiredFields]Iniciando validación de los campos");
        if (messageType.equals("0800") || messageType.equals("0810") || messageType.equals("0100")) {
            System.out.println(messageType);
            if (binaryBitMap.length() != 128) return false;
        } else {
            System.out.println("[checkRequiredFields]Tipo de mensaje desconocido: " + messageType);
            return false;
        }

        List<Integer> mandatoryFields = parserValidations.get("M" + messageType + "M");
        List<Integer> optionalFields = parserValidations.get("M" + messageType + "O");
        return processField(mandatoryFields, optionalFields, binaryBitMap);
    }

    private static boolean processField(List<Integer> mandatory, List<Integer> optional, String binaryBitMap) {
        for (int i = 0; i < binaryBitMap.length(); i++) {
            if (mandatory.contains(i)) {
                if (binaryBitMap.charAt(i) == '0') {
                    System.out.println("[checkRequiredFields]Error in field " + (i + 1));
                    return false;
                }
            } else if (!(optional.contains(i)) && binaryBitMap.charAt(i) == '1') {
                System.out.println("[checkRequiredFields]Error in field " + (i + 1));
                return false;
            }
        }
        return true;
    }
}
