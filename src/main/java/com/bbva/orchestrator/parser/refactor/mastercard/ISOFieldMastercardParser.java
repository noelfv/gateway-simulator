package com.bbva.orchestrator.parser.refactor.mastercard;

import com.bbva.gateway.utils.LogsTraces;
import com.bbva.orchestrator.parser.refactor.field.ParsedFieldResult;
import com.bbva.orchestrator.parser.refactor.utils.ISOUtil;
import com.bbva.orchlib.parser.ParserException;
import java.util.HashMap;
import java.util.Map;

public class ISOFieldMastercardParser {
    /**
     * Mapea los campos de un mensaje ISO 8583 de Mastercard a un mapa de valores.
     *
     * @param iso Mensaje ISO 8583 en formato hexadecimal.
     * @return Un mapa donde las claves son los nombres de los campos y los valores son sus respectivos valores.
     * @throws ParserException Si ocurre un error al procesar el mensaje ISO.
     */

    public static Map<String, String> mapFields(String iso) {
        Map<String, String> valuesMap = new HashMap<>();
        StringBuilder isoMessage = new StringBuilder(iso);
        int position = 0;
        boolean containsSecondaryBitmap = false;

        try {
            position = processFieldData(ISOFieldMastercard.MESSAGE_TYPE, isoMessage, position, valuesMap);
            position = processFieldData(ISOFieldMastercard.BITMAP_PRIMARY, isoMessage, position, valuesMap);

            String binaryBitMapPrimary = valuesMap.get(ISOFieldMastercard.BITMAP_PRIMARY.getName());
            StringBuilder fullBitmap = new StringBuilder(binaryBitMapPrimary);

            if (binaryBitMapPrimary.charAt(0) == '1') {
                position = processFieldData(ISOFieldMastercard.BITMAP_SECONDARY, isoMessage, position, valuesMap);
                String binaryBitMapSecondary = valuesMap.get(ISOFieldMastercard.BITMAP_SECONDARY.getName());
                fullBitmap.append(binaryBitMapSecondary);
                containsSecondaryBitmap = true;
            }

            for (int i = 2; i <= fullBitmap.length(); i++) {
                if (fullBitmap.charAt(i - 1) == '1') {
                    ISOFieldMastercard field = ISOFieldMastercard.getById(i);

                    if (field == null) {
                        LogsTraces.writeInfo("Campo no permitido: " + i + ". No hay mapeo disponible.");
                        throw new ParserException(createMessageError(i));
                    }

                    position = processFieldData(field, isoMessage, position, valuesMap);
                }
            }
        } catch (ParserException e) {
            throw e;
        } catch (Exception e) {
            throw new ParserException("No se puede parsear el mensaje ISO: " + e.getMessage() + "|" + ISOUtil.processError(iso, containsSecondaryBitmap));
        }
        return valuesMap;
    }

    private static int processFieldData(ISOFieldMastercard isoField, StringBuilder isoMessage, int currentPosition, Map<String, String> valuesMap) {
        try {
            String remainingMessageSegment = isoMessage.substring(currentPosition);
            ParsedFieldResult result;

            if (isoField.isVariable()) {
                result = isoField.getParserStrategy().parse(remainingMessageSegment, 0, isoField);
            } else {
                int expectedHexLength = isoField.getLength() * 2;
                result = isoField.getParserStrategy().parse(remainingMessageSegment, expectedHexLength, isoField);
            }

            valuesMap.put(isoField.getName(), result.value());

            currentPosition += result.consumedLengthInChars(); // Usar getConsumedLength()

            return currentPosition;

        } catch (ParserException e) {
            throw e;
        } catch (Exception e) {
            //throw new ParserException("Error procesando campo " + isoField.getId() + " (" + isoField.getName() + ") en posición " + currentPosition + ": " + e.getMessage(), e);
            throw new ParserException("Error procesando campo " + isoField.getId() + " (" + isoField.getName() + ") en posición " + currentPosition + ": " + e.getMessage());
        }
    }

    private static String createMessageError(int fieldId) {
        return "Error en campo " + fieldId + ": " + "No hay mapeo disponible";
    }
}