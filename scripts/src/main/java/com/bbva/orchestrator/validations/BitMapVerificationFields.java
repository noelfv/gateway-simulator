package com.bbva.orchestrator.validations;

import com.bbva.gateway.utils.LogsTraces;
import lombok.Setter;

import java.util.List;
import java.util.Map;

public class BitMapVerificationFields {

    @Setter
    private static Map<String, List<Integer>> parserValidations;

    private BitMapVerificationFields() {
    }

    public static boolean checkRequiredFields(String messageType, String binaryBitMap) {
        LogsTraces.writeInfo("[checkRequiredFields]Iniciando validación de los campos");
        if (messageType.equals("0800") || messageType.equals("0810") || messageType.equals("0100")) {
            LogsTraces.writeInfo(messageType);
            if (binaryBitMap.length() != 128) return false;
        } else {
            LogsTraces.writeError("[checkRequiredFields]Tipo de mensaje desconocido: " + messageType);
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
                    LogsTraces.writeError("[checkRequiredFields]Error in field " + (i + 1));
                    return false;
                }
            } else if (!(optional.contains(i)) && binaryBitMap.charAt(i) == '1') {
                LogsTraces.writeError("[checkRequiredFields]Error in field " + (i + 1));
                return false;
            }
        }
        return true;
    }
}
