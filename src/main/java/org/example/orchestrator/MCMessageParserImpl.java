package org.example.orchestrator;



import org.example.orchestrator.common.ISO8583SubFieldsParser;
import org.example.orchestrator.common.ISOField;
import org.example.orchestrator.common.ParseResult;
import org.example.orchestrator.dto.AdditionalDataDTO;
import org.example.orchestrator.dto.ISO20022;
import org.example.orchestrator.iso8583.ISO8583;
import org.example.orchestrator.mastercard.ISOFieldMastercard;
import org.example.orchestrator.mastercard.processor.ISOStringConverterMastercard;
import org.example.orchestrator.mastercard.processor.ISOStringMapper;

import java.util.HashMap;
import java.util.Map;

public class MCMessageParserImpl implements MessageParser {

    public MCMessageParserImpl() {
    }


    @Override
    public Map<String, String> parser(String originalMessage) {
        return ISOStringMapper.mapFields(originalMessage);
    }

    public ParseResult parseWithBothMaps(String originalMessage,boolean clean) throws Exception {
        // Usar el método existente para obtener los campos mapeados
        Map<String, String> originalMappedFields;
        if(!clean) {
            originalMappedFields = ISOStringMapper.mapFields(originalMessage);
        }else {
            originalMappedFields = ISOStringMapper.mapFieldsTramaClaro(originalMessage);
        }


        Map<String, String> fieldsByDescription = new HashMap<>();
        Map<String, String> fieldsById = new HashMap<>();

        // Iterar sobre los campos ya mapeados
        for (Map.Entry<String, String> entry : originalMappedFields.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();

            // El mapa original ya contiene las descripciones, así que lo copiamos
            fieldsByDescription.put(key, value);

            // Para el mapa por ID, necesitamos mapear las descripciones a IDs
            String fieldId = findFieldIdByName(key);
            if (fieldId != null) {
                fieldsById.put(fieldId, value);
            } else {
                // Si no encontramos el ID, usar la descripción como clave
                fieldsById.put(key, value);
            }
        }

        return new ParseResult(fieldsByDescription, fieldsById);
    }

    private String findFieldIdByName(String fieldName) {
        for (ISOField field : ISOFieldMastercard.values()) {
            if (field.getName().equalsIgnoreCase(fieldName)) {
                return String.valueOf(field.getId());
            }
        }
        return null;
    }

    @Override
    public ISO20022 reMap(ISO8583 iso8583, ISO20022 iso20022) {
            switch (iso20022.getAddendumData().getAdditionalData().get(1).getValue()) {
            case "0100", "0110":
                // Remapping mensajes 0100
                break;
            case "0400":
                // Remapping mensajes 0400
                break;
            case "0800", "0810":
                AdditionalDataDTO iso8583AdditionalData = AdditionalDataDTO.builder()
                        .key("ICA")
                        .value(iso8583.getPrimaryAccountNumber())
                        .build();

                // Por el momento el ICA se guarda en additionalData hasta que se encuentre un mapping
                iso20022.getTransaction().getAdditionalData().add(iso8583AdditionalData);
                break;
            default:
                break;
        }

        return iso20022;
    }

    @Override
    public String unParser(Map<String, String> mappedFields, boolean clean) {
        return ISOStringConverterMastercard.getInstance().convertToISOString(mappedFields, clean);
    }

    @Override
    public ISO20022 unMap(ISO20022 iso20022) {
        switch (iso20022.getMessageFunction()) {
            case "0100", "0110":
                // Remapping mensajes 0100
                break;
            case "0400":
                // Remapping mensajes 0400
                break;
            case "0800", "0810":
                // Por el momento el ICA se guarda en additionalData hasta que se encuentre un mapping
                AdditionalDataDTO ica = iso20022
                        .getTransaction()
                        .getAdditionalData()
                        .stream()
                        .filter(data -> data.getKey().equals("ICA"))
                        .findFirst()
                        .orElse(AdditionalDataDTO.builder().value("").build());

                iso20022.getEnvironment().getCard().setPan(ica.getValue());
                break;
            default:
                break;
        }

        return iso20022;
    }

    @Override
    public Map<String, String> mapSubFields(Map<String, String> values) {
        return ISO8583SubFieldsParser.mapSubFieldsMastercard(values);
    }
}
